/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.ExamenOdontologia;
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
public class ExamenOdontologiaJpaController implements Serializable {

    public ExamenOdontologiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExamenOdontologia examenOdontologia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente expedientePacienteCedula = examenOdontologia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula = em.getReference(expedientePacienteCedula.getClass(), expedientePacienteCedula.getExpedientePK());
                examenOdontologia.setExpedientePacienteCedula(expedientePacienteCedula);
            }
            Personal personalCedula = examenOdontologia.getPersonalCedula();
            if (personalCedula != null) {
                personalCedula = em.getReference(personalCedula.getClass(), personalCedula.getCedula());
                examenOdontologia.setPersonalCedula(personalCedula);
            }
            em.persist(examenOdontologia);
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getExamenOdontologiaList().add(examenOdontologia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            if (personalCedula != null) {
                personalCedula.getExamenOdontologiaList().add(examenOdontologia);
                personalCedula = em.merge(personalCedula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExamenOdontologia examenOdontologia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExamenOdontologia persistentExamenOdontologia = em.find(ExamenOdontologia.class, examenOdontologia.getId());
            Expediente expedientePacienteCedulaOld = persistentExamenOdontologia.getExpedientePacienteCedula();
            Expediente expedientePacienteCedulaNew = examenOdontologia.getExpedientePacienteCedula();
            Personal personalCedulaOld = persistentExamenOdontologia.getPersonalCedula();
            Personal personalCedulaNew = examenOdontologia.getPersonalCedula();
            if (expedientePacienteCedulaNew != null) {
                expedientePacienteCedulaNew = em.getReference(expedientePacienteCedulaNew.getClass(), expedientePacienteCedulaNew.getExpedientePK());
                examenOdontologia.setExpedientePacienteCedula(expedientePacienteCedulaNew);
            }
            if (personalCedulaNew != null) {
                personalCedulaNew = em.getReference(personalCedulaNew.getClass(), personalCedulaNew.getCedula());
                examenOdontologia.setPersonalCedula(personalCedulaNew);
            }
            examenOdontologia = em.merge(examenOdontologia);
            if (expedientePacienteCedulaOld != null && !expedientePacienteCedulaOld.equals(expedientePacienteCedulaNew)) {
                expedientePacienteCedulaOld.getExamenOdontologiaList().remove(examenOdontologia);
                expedientePacienteCedulaOld = em.merge(expedientePacienteCedulaOld);
            }
            if (expedientePacienteCedulaNew != null && !expedientePacienteCedulaNew.equals(expedientePacienteCedulaOld)) {
                expedientePacienteCedulaNew.getExamenOdontologiaList().add(examenOdontologia);
                expedientePacienteCedulaNew = em.merge(expedientePacienteCedulaNew);
            }
            if (personalCedulaOld != null && !personalCedulaOld.equals(personalCedulaNew)) {
                personalCedulaOld.getExamenOdontologiaList().remove(examenOdontologia);
                personalCedulaOld = em.merge(personalCedulaOld);
            }
            if (personalCedulaNew != null && !personalCedulaNew.equals(personalCedulaOld)) {
                personalCedulaNew.getExamenOdontologiaList().add(examenOdontologia);
                personalCedulaNew = em.merge(personalCedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = examenOdontologia.getId();
                if (findExamenOdontologia(id) == null) {
                    throw new NonexistentEntityException("The examenOdontologia with id " + id + " no longer exists.");
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
            ExamenOdontologia examenOdontologia;
            try {
                examenOdontologia = em.getReference(ExamenOdontologia.class, id);
                examenOdontologia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examenOdontologia with id " + id + " no longer exists.", enfe);
            }
            Expediente expedientePacienteCedula = examenOdontologia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getExamenOdontologiaList().remove(examenOdontologia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            Personal personalCedula = examenOdontologia.getPersonalCedula();
            if (personalCedula != null) {
                personalCedula.getExamenOdontologiaList().remove(examenOdontologia);
                personalCedula = em.merge(personalCedula);
            }
            em.remove(examenOdontologia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ExamenOdontologia> findExamenOdontologiaEntities() {
        return findExamenOdontologiaEntities(true, -1, -1);
    }

    public List<ExamenOdontologia> findExamenOdontologiaEntities(int maxResults, int firstResult) {
        return findExamenOdontologiaEntities(false, maxResults, firstResult);
    }

    private List<ExamenOdontologia> findExamenOdontologiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExamenOdontologia.class));
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

    public ExamenOdontologia findExamenOdontologia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExamenOdontologia.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamenOdontologiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExamenOdontologia> rt = cq.from(ExamenOdontologia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
