/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.AntecedentesGinecologia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Expediente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class AntecedentesGinecologiaJpaController implements Serializable {

    public AntecedentesGinecologiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AntecedentesGinecologia antecedentesGinecologia) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Expediente expedientePacientecedulaOrphanCheck = antecedentesGinecologia.getExpedientePacientecedula();
        if (expedientePacientecedulaOrphanCheck != null) {
            AntecedentesGinecologia oldAntecedentesGinecologiaOfExpedientePacientecedula = expedientePacientecedulaOrphanCheck.getAntecedentesGinecologia();
            if (oldAntecedentesGinecologiaOfExpedientePacientecedula != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Expediente " + expedientePacientecedulaOrphanCheck + " already has an item of type AntecedentesGinecologia whose expedientePacientecedula column cannot be null. Please make another selection for the expedientePacientecedula field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente expedientePacientecedula = antecedentesGinecologia.getExpedientePacientecedula();
            if (expedientePacientecedula != null) {
                expedientePacientecedula = em.getReference(expedientePacientecedula.getClass(), expedientePacientecedula.getId());
                antecedentesGinecologia.setExpedientePacientecedula(expedientePacientecedula);
            }
            em.persist(antecedentesGinecologia);
            if (expedientePacientecedula != null) {
                expedientePacientecedula.setAntecedentesGinecologia(antecedentesGinecologia);
                expedientePacientecedula = em.merge(expedientePacientecedula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AntecedentesGinecologia antecedentesGinecologia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesGinecologia persistentAntecedentesGinecologia = em.find(AntecedentesGinecologia.class, antecedentesGinecologia.getId());
            Expediente expedientePacientecedulaOld = persistentAntecedentesGinecologia.getExpedientePacientecedula();
            Expediente expedientePacientecedulaNew = antecedentesGinecologia.getExpedientePacientecedula();
            List<String> illegalOrphanMessages = null;
            if (expedientePacientecedulaNew != null && !expedientePacientecedulaNew.equals(expedientePacientecedulaOld)) {
                AntecedentesGinecologia oldAntecedentesGinecologiaOfExpedientePacientecedula = expedientePacientecedulaNew.getAntecedentesGinecologia();
                if (oldAntecedentesGinecologiaOfExpedientePacientecedula != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Expediente " + expedientePacientecedulaNew + " already has an item of type AntecedentesGinecologia whose expedientePacientecedula column cannot be null. Please make another selection for the expedientePacientecedula field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (expedientePacientecedulaNew != null) {
                expedientePacientecedulaNew = em.getReference(expedientePacientecedulaNew.getClass(), expedientePacientecedulaNew.getId());
                antecedentesGinecologia.setExpedientePacientecedula(expedientePacientecedulaNew);
            }
            antecedentesGinecologia = em.merge(antecedentesGinecologia);
            if (expedientePacientecedulaOld != null && !expedientePacientecedulaOld.equals(expedientePacientecedulaNew)) {
                expedientePacientecedulaOld.setAntecedentesGinecologia(null);
                expedientePacientecedulaOld = em.merge(expedientePacientecedulaOld);
            }
            if (expedientePacientecedulaNew != null && !expedientePacientecedulaNew.equals(expedientePacientecedulaOld)) {
                expedientePacientecedulaNew.setAntecedentesGinecologia(antecedentesGinecologia);
                expedientePacientecedulaNew = em.merge(expedientePacientecedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = antecedentesGinecologia.getId();
                if (findAntecedentesGinecologia(id) == null) {
                    throw new NonexistentEntityException("The antecedentesGinecologia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesGinecologia antecedentesGinecologia;
            try {
                antecedentesGinecologia = em.getReference(AntecedentesGinecologia.class, id);
                antecedentesGinecologia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The antecedentesGinecologia with id " + id + " no longer exists.", enfe);
            }
            Expediente expedientePacientecedula = antecedentesGinecologia.getExpedientePacientecedula();
            if (expedientePacientecedula != null) {
                expedientePacientecedula.setAntecedentesGinecologia(null);
                expedientePacientecedula = em.merge(expedientePacientecedula);
            }
            em.remove(antecedentesGinecologia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AntecedentesGinecologia> findAntecedentesGinecologiaEntities() {
        return findAntecedentesGinecologiaEntities(true, -1, -1);
    }

    public List<AntecedentesGinecologia> findAntecedentesGinecologiaEntities(int maxResults, int firstResult) {
        return findAntecedentesGinecologiaEntities(false, maxResults, firstResult);
    }

    private List<AntecedentesGinecologia> findAntecedentesGinecologiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AntecedentesGinecologia.class));
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

    public AntecedentesGinecologia findAntecedentesGinecologia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AntecedentesGinecologia.class, id);
        } finally {
            em.close();
        }
    }

    public int getAntecedentesGinecologiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AntecedentesGinecologia> rt = cq.from(AntecedentesGinecologia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
