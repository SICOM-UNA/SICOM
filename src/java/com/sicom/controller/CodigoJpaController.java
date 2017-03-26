/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.Codigo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Valor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
 */
public class CodigoJpaController implements Serializable {

    public CodigoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Codigo codigo) {
        if (codigo.getValorList() == null) {
            codigo.setValorList(new ArrayList<Valor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Valor> attachedValorList = new ArrayList<Valor>();
            for (Valor valorListValorToAttach : codigo.getValorList()) {
                valorListValorToAttach = em.getReference(valorListValorToAttach.getClass(), valorListValorToAttach.getValorPK());
                attachedValorList.add(valorListValorToAttach);
            }
            codigo.setValorList(attachedValorList);
            em.persist(codigo);
            for (Valor valorListValor : codigo.getValorList()) {
                Codigo oldCodigoOfValorListValor = valorListValor.getCodigo();
                valorListValor.setCodigo(codigo);
                valorListValor = em.merge(valorListValor);
                if (oldCodigoOfValorListValor != null) {
                    oldCodigoOfValorListValor.getValorList().remove(valorListValor);
                    oldCodigoOfValorListValor = em.merge(oldCodigoOfValorListValor);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Codigo codigo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codigo persistentCodigo = em.find(Codigo.class, codigo.getId());
            List<Valor> valorListOld = persistentCodigo.getValorList();
            List<Valor> valorListNew = codigo.getValorList();
            List<String> illegalOrphanMessages = null;
            for (Valor valorListOldValor : valorListOld) {
                if (!valorListNew.contains(valorListOldValor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Valor " + valorListOldValor + " since its codigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Valor> attachedValorListNew = new ArrayList<Valor>();
            for (Valor valorListNewValorToAttach : valorListNew) {
                valorListNewValorToAttach = em.getReference(valorListNewValorToAttach.getClass(), valorListNewValorToAttach.getValorPK());
                attachedValorListNew.add(valorListNewValorToAttach);
            }
            valorListNew = attachedValorListNew;
            codigo.setValorList(valorListNew);
            codigo = em.merge(codigo);
            for (Valor valorListNewValor : valorListNew) {
                if (!valorListOld.contains(valorListNewValor)) {
                    Codigo oldCodigoOfValorListNewValor = valorListNewValor.getCodigo();
                    valorListNewValor.setCodigo(codigo);
                    valorListNewValor = em.merge(valorListNewValor);
                    if (oldCodigoOfValorListNewValor != null && !oldCodigoOfValorListNewValor.equals(codigo)) {
                        oldCodigoOfValorListNewValor.getValorList().remove(valorListNewValor);
                        oldCodigoOfValorListNewValor = em.merge(oldCodigoOfValorListNewValor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = codigo.getId();
                if (findCodigo(id) == null) {
                    throw new NonexistentEntityException("The codigo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codigo codigo;
            try {
                codigo = em.getReference(Codigo.class, id);
                codigo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The codigo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Valor> valorListOrphanCheck = codigo.getValorList();
            for (Valor valorListOrphanCheckValor : valorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Codigo (" + codigo + ") cannot be destroyed since the Valor " + valorListOrphanCheckValor + " in its valorList field has a non-nullable codigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(codigo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Codigo> findCodigoEntities() {
        return findCodigoEntities(true, -1, -1);
    }

    public List<Codigo> findCodigoEntities(int maxResults, int firstResult) {
        return findCodigoEntities(false, maxResults, firstResult);
    }

    private List<Codigo> findCodigoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Codigo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Codigo findCodigo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Codigo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCodigoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Codigo> rt = cq.from(Codigo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
