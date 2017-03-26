/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Codigo;
import com.sicom.entities.Valor;
import com.sicom.entities.ValorPK;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author WVQ
 */
public class ValorJpaController implements Serializable {

    public ValorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Valor valor) throws PreexistingEntityException, Exception {
        if (valor.getValorPK() == null) {
            valor.setValorPK(new ValorPK());
        }
        valor.getValorPK().setCodigoId(valor.getCodigo().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codigo codigo = valor.getCodigo();
            if (codigo != null) {
                codigo = em.getReference(codigo.getClass(), codigo.getId());
                valor.setCodigo(codigo);
            }
            em.persist(valor);
            if (codigo != null) {
                codigo.getValorList().add(valor);
                codigo = em.merge(codigo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findValor(valor.getValorPK()) != null) {
                throw new PreexistingEntityException("Valor " + valor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Valor valor) throws NonexistentEntityException, Exception {
        valor.getValorPK().setCodigoId(valor.getCodigo().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Valor persistentValor = em.find(Valor.class, valor.getValorPK());
            Codigo codigoOld = persistentValor.getCodigo();
            Codigo codigoNew = valor.getCodigo();
            if (codigoNew != null) {
                codigoNew = em.getReference(codigoNew.getClass(), codigoNew.getId());
                valor.setCodigo(codigoNew);
            }
            valor = em.merge(valor);
            if (codigoOld != null && !codigoOld.equals(codigoNew)) {
                codigoOld.getValorList().remove(valor);
                codigoOld = em.merge(codigoOld);
            }
            if (codigoNew != null && !codigoNew.equals(codigoOld)) {
                codigoNew.getValorList().add(valor);
                codigoNew = em.merge(codigoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ValorPK id = valor.getValorPK();
                if (findValor(id) == null) {
                    throw new NonexistentEntityException("The valor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ValorPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Valor valor;
            try {
                valor = em.getReference(Valor.class, id);
                valor.getValorPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valor with id " + id + " no longer exists.", enfe);
            }
            Codigo codigo = valor.getCodigo();
            if (codigo != null) {
                codigo.getValorList().remove(valor);
                codigo = em.merge(codigo);
            }
            em.remove(valor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Valor> findValorEntities() {
        return findValorEntities(true, -1, -1);
    }

    public List<Valor> findValorEntities(int maxResults, int firstResult) {
        return findValorEntities(false, maxResults, firstResult);
    }

    private List<Valor> findValorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Valor.class));
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

    public Valor findValor(ValorPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Valor.class, id);
        } finally {
            em.close();
        }
    }

    public List<Valor> findByCodeId(Integer codigo_id) {
        EntityManager em = getEntityManager();

        TypedQuery consulta = em.createNamedQuery("Valor.findByCodigoId", String.class);
        consulta.setParameter("codigoId", codigo_id);
        List<Valor> listaValores = consulta.getResultList();
        
        return listaValores;
    }

    public List<String> findDescriptionByCodeId(Integer codigo_id) {
        EntityManager em = getEntityManager();

        TypedQuery consulta = em.createNamedQuery("Valor.findByCodigoId", String.class);
        consulta.setParameter("codigoId", codigo_id);
        List<Valor> listaValores = consulta.getResultList();

        List<String> listaDescripciones = new ArrayList<>();
        Iterator<Valor> itr = listaValores.iterator();

        while (itr.hasNext()) {
            Valor aux = itr.next();
            listaDescripciones.add(aux.getDescripcion());
        }
        return listaDescripciones;
    }

    public int findByValueDescription(String des) {
        EntityManager em = getEntityManager();
        TypedQuery consulta = em.createNamedQuery("Valor.findByDescripcion", String.class);
        consulta.setParameter("descripcion", des);

        List<Valor> listaValores = consulta.getResultList();

        if (!listaValores.isEmpty()) {
            return listaValores.get(0).getValorPK().getId();
        }

        return -1;
    }

    public int getValorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Valor> rt = cq.from(Valor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
