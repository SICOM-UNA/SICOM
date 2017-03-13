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
            Expediente expedientePacienteCedula = examenGinecologia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula = em.getReference(expedientePacienteCedula.getClass(), expedientePacienteCedula.getExpedientePK());
                examenGinecologia.setExpedientePacienteCedula(expedientePacienteCedula);
            }
            Personal personalCedula = examenGinecologia.getPersonalCedula();
            if (personalCedula != null) {
                personalCedula = em.getReference(personalCedula.getClass(), personalCedula.getCedula());
                examenGinecologia.setPersonalCedula(personalCedula);
            }
            em.persist(examenGinecologia);
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getExamenGinecologiaList().add(examenGinecologia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            if (personalCedula != null) {
                personalCedula.getExamenGinecologiaList().add(examenGinecologia);
                personalCedula = em.merge(personalCedula);
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
            Expediente expedientePacienteCedulaOld = persistentExamenGinecologia.getExpedientePacienteCedula();
            Expediente expedientePacienteCedulaNew = examenGinecologia.getExpedientePacienteCedula();
            Personal personalCedulaOld = persistentExamenGinecologia.getPersonalCedula();
            Personal personalCedulaNew = examenGinecologia.getPersonalCedula();
            if (expedientePacienteCedulaNew != null) {
                expedientePacienteCedulaNew = em.getReference(expedientePacienteCedulaNew.getClass(), expedientePacienteCedulaNew.getExpedientePK());
                examenGinecologia.setExpedientePacienteCedula(expedientePacienteCedulaNew);
            }
            if (personalCedulaNew != null) {
                personalCedulaNew = em.getReference(personalCedulaNew.getClass(), personalCedulaNew.getCedula());
                examenGinecologia.setPersonalCedula(personalCedulaNew);
            }
            examenGinecologia = em.merge(examenGinecologia);
            if (expedientePacienteCedulaOld != null && !expedientePacienteCedulaOld.equals(expedientePacienteCedulaNew)) {
                expedientePacienteCedulaOld.getExamenGinecologiaList().remove(examenGinecologia);
                expedientePacienteCedulaOld = em.merge(expedientePacienteCedulaOld);
            }
            if (expedientePacienteCedulaNew != null && !expedientePacienteCedulaNew.equals(expedientePacienteCedulaOld)) {
                expedientePacienteCedulaNew.getExamenGinecologiaList().add(examenGinecologia);
                expedientePacienteCedulaNew = em.merge(expedientePacienteCedulaNew);
            }
            if (personalCedulaOld != null && !personalCedulaOld.equals(personalCedulaNew)) {
                personalCedulaOld.getExamenGinecologiaList().remove(examenGinecologia);
                personalCedulaOld = em.merge(personalCedulaOld);
            }
            if (personalCedulaNew != null && !personalCedulaNew.equals(personalCedulaOld)) {
                personalCedulaNew.getExamenGinecologiaList().add(examenGinecologia);
                personalCedulaNew = em.merge(personalCedulaNew);
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
            Expediente expedientePacienteCedula = examenGinecologia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getExamenGinecologiaList().remove(examenGinecologia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            Personal personalCedula = examenGinecologia.getPersonalCedula();
            if (personalCedula != null) {
                personalCedula.getExamenGinecologiaList().remove(examenGinecologia);
                personalCedula = em.merge(personalCedula);
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
