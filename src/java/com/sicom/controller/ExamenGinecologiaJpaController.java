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
import com.sicom.entities.Personal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
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
            Expediente expedientePacientecedula = examenGinecologia.getExpedientePacientecedula();
            if (expedientePacientecedula != null) {
                expedientePacientecedula = em.getReference(expedientePacientecedula.getClass(), expedientePacientecedula.getId());
                examenGinecologia.setExpedientePacientecedula(expedientePacientecedula);
            }
            Personal personalcedula = examenGinecologia.getPersonalcedula();
            if (personalcedula != null) {
                personalcedula = em.getReference(personalcedula.getClass(), personalcedula.getCedula());
                examenGinecologia.setPersonalcedula(personalcedula);
            }
            em.persist(examenGinecologia);
            if (expedientePacientecedula != null) {
                expedientePacientecedula.getExamenGinecologiaList().add(examenGinecologia);
                expedientePacientecedula = em.merge(expedientePacientecedula);
            }
            if (personalcedula != null) {
                personalcedula.getExamenGinecologiaList().add(examenGinecologia);
                personalcedula = em.merge(personalcedula);
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
            Expediente expedientePacientecedulaOld = persistentExamenGinecologia.getExpedientePacientecedula();
            Expediente expedientePacientecedulaNew = examenGinecologia.getExpedientePacientecedula();
            Personal personalcedulaOld = persistentExamenGinecologia.getPersonalcedula();
            Personal personalcedulaNew = examenGinecologia.getPersonalcedula();
            if (expedientePacientecedulaNew != null) {
                expedientePacientecedulaNew = em.getReference(expedientePacientecedulaNew.getClass(), expedientePacientecedulaNew.getId());
                examenGinecologia.setExpedientePacientecedula(expedientePacientecedulaNew);
            }
            if (personalcedulaNew != null) {
                personalcedulaNew = em.getReference(personalcedulaNew.getClass(), personalcedulaNew.getCedula());
                examenGinecologia.setPersonalcedula(personalcedulaNew);
            }
            examenGinecologia = em.merge(examenGinecologia);
            if (expedientePacientecedulaOld != null && !expedientePacientecedulaOld.equals(expedientePacientecedulaNew)) {
                expedientePacientecedulaOld.getExamenGinecologiaList().remove(examenGinecologia);
                expedientePacientecedulaOld = em.merge(expedientePacientecedulaOld);
            }
            if (expedientePacientecedulaNew != null && !expedientePacientecedulaNew.equals(expedientePacientecedulaOld)) {
                expedientePacientecedulaNew.getExamenGinecologiaList().add(examenGinecologia);
                expedientePacientecedulaNew = em.merge(expedientePacientecedulaNew);
            }
            if (personalcedulaOld != null && !personalcedulaOld.equals(personalcedulaNew)) {
                personalcedulaOld.getExamenGinecologiaList().remove(examenGinecologia);
                personalcedulaOld = em.merge(personalcedulaOld);
            }
            if (personalcedulaNew != null && !personalcedulaNew.equals(personalcedulaOld)) {
                personalcedulaNew.getExamenGinecologiaList().add(examenGinecologia);
                personalcedulaNew = em.merge(personalcedulaNew);
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
            Expediente expedientePacientecedula = examenGinecologia.getExpedientePacientecedula();
            if (expedientePacientecedula != null) {
                expedientePacientecedula.getExamenGinecologiaList().remove(examenGinecologia);
                expedientePacientecedula = em.merge(expedientePacientecedula);
            }
            Personal personalcedula = examenGinecologia.getPersonalcedula();
            if (personalcedula != null) {
                personalcedula.getExamenGinecologiaList().remove(examenGinecologia);
                personalcedula = em.merge(personalcedula);
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
