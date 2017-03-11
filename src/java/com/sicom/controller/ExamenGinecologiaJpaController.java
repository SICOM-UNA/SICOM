/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.ExamenGinecologia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Expediente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class ExamenGinecologiaJpaController implements Serializable {

    public ExamenGinecologiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExamenGinecologia examenGinecologia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente expedienteid = examenGinecologia.getExpedienteid();
            if (expedienteid != null) {
                expedienteid = em.getReference(expedienteid.getClass(), expedienteid.getId());
                examenGinecologia.setExpedienteid(expedienteid);
            }
            em.persist(examenGinecologia);
            if (expedienteid != null) {
                expedienteid.getExamenGinecologiaList().add(examenGinecologia);
                expedienteid = em.merge(expedienteid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExamenGinecologia examenGinecologia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExamenGinecologia persistentExamenGinecologia = em.find(ExamenGinecologia.class, examenGinecologia.getId());
            Expediente expedienteidOld = persistentExamenGinecologia.getExpedienteid();
            Expediente expedienteidNew = examenGinecologia.getExpedienteid();
            if (expedienteidNew != null) {
                expedienteidNew = em.getReference(expedienteidNew.getClass(), expedienteidNew.getId());
                examenGinecologia.setExpedienteid(expedienteidNew);
            }
            examenGinecologia = em.merge(examenGinecologia);
            if (expedienteidOld != null && !expedienteidOld.equals(expedienteidNew)) {
                expedienteidOld.getExamenGinecologiaList().remove(examenGinecologia);
                expedienteidOld = em.merge(expedienteidOld);
            }
            if (expedienteidNew != null && !expedienteidNew.equals(expedienteidOld)) {
                expedienteidNew.getExamenGinecologiaList().add(examenGinecologia);
                expedienteidNew = em.merge(expedienteidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = examenGinecologia.getId();
                if (findExamenGinecologia(id) == null) {
                    throw new NonexistentEntityException("The examenGinecologia with id " + id + " no longer exists.");
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
            ExamenGinecologia examenGinecologia;
            try {
                examenGinecologia = em.getReference(ExamenGinecologia.class, id);
                examenGinecologia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examenGinecologia with id " + id + " no longer exists.", enfe);
            }
            Expediente expedienteid = examenGinecologia.getExpedienteid();
            if (expedienteid != null) {
                expedienteid.getExamenGinecologiaList().remove(examenGinecologia);
                expedienteid = em.merge(expedienteid);
            }
            em.remove(examenGinecologia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ExamenGinecologia> findExamenGinecologiaEntities() {
        return findExamenGinecologiaEntities(true, -1, -1);
    }

    public List<ExamenGinecologia> findExamenGinecologiaEntities(int maxResults, int firstResult) {
        return findExamenGinecologiaEntities(false, maxResults, firstResult);
    }

    private List<ExamenGinecologia> findExamenGinecologiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExamenGinecologia.class));
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

    public ExamenGinecologia findExamenGinecologia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExamenGinecologia.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamenGinecologiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExamenGinecologia> rt = cq.from(ExamenGinecologia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
