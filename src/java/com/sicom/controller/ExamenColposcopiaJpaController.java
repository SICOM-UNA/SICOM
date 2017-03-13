/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.ExamenColposcopia;
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
public class ExamenColposcopiaJpaController implements Serializable {

    public ExamenColposcopiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExamenColposcopia examenColposcopia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente expedientePacienteCedula = examenColposcopia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula = em.getReference(expedientePacienteCedula.getClass(), expedientePacienteCedula.getExpedientePK());
                examenColposcopia.setExpedientePacienteCedula(expedientePacienteCedula);
            }
            Personal personalCedula = examenColposcopia.getPersonalCedula();
            if (personalCedula != null) {
                personalCedula = em.getReference(personalCedula.getClass(), personalCedula.getCedula());
                examenColposcopia.setPersonalCedula(personalCedula);
            }
            em.persist(examenColposcopia);
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getExamenColposcopiaList().add(examenColposcopia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            if (personalCedula != null) {
                personalCedula.getExamenColposcopiaList().add(examenColposcopia);
                personalCedula = em.merge(personalCedula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExamenColposcopia examenColposcopia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExamenColposcopia persistentExamenColposcopia = em.find(ExamenColposcopia.class, examenColposcopia.getId());
            Expediente expedientePacienteCedulaOld = persistentExamenColposcopia.getExpedientePacienteCedula();
            Expediente expedientePacienteCedulaNew = examenColposcopia.getExpedientePacienteCedula();
            Personal personalCedulaOld = persistentExamenColposcopia.getPersonalCedula();
            Personal personalCedulaNew = examenColposcopia.getPersonalCedula();
            if (expedientePacienteCedulaNew != null) {
                expedientePacienteCedulaNew = em.getReference(expedientePacienteCedulaNew.getClass(), expedientePacienteCedulaNew.getExpedientePK());
                examenColposcopia.setExpedientePacienteCedula(expedientePacienteCedulaNew);
            }
            if (personalCedulaNew != null) {
                personalCedulaNew = em.getReference(personalCedulaNew.getClass(), personalCedulaNew.getCedula());
                examenColposcopia.setPersonalCedula(personalCedulaNew);
            }
            examenColposcopia = em.merge(examenColposcopia);
            if (expedientePacienteCedulaOld != null && !expedientePacienteCedulaOld.equals(expedientePacienteCedulaNew)) {
                expedientePacienteCedulaOld.getExamenColposcopiaList().remove(examenColposcopia);
                expedientePacienteCedulaOld = em.merge(expedientePacienteCedulaOld);
            }
            if (expedientePacienteCedulaNew != null && !expedientePacienteCedulaNew.equals(expedientePacienteCedulaOld)) {
                expedientePacienteCedulaNew.getExamenColposcopiaList().add(examenColposcopia);
                expedientePacienteCedulaNew = em.merge(expedientePacienteCedulaNew);
            }
            if (personalCedulaOld != null && !personalCedulaOld.equals(personalCedulaNew)) {
                personalCedulaOld.getExamenColposcopiaList().remove(examenColposcopia);
                personalCedulaOld = em.merge(personalCedulaOld);
            }
            if (personalCedulaNew != null && !personalCedulaNew.equals(personalCedulaOld)) {
                personalCedulaNew.getExamenColposcopiaList().add(examenColposcopia);
                personalCedulaNew = em.merge(personalCedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = examenColposcopia.getId();
                if (findExamenColposcopia(id) == null) {
                    throw new NonexistentEntityException("The examenColposcopia with id " + id + " no longer exists.");
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
            ExamenColposcopia examenColposcopia;
            try {
                examenColposcopia = em.getReference(ExamenColposcopia.class, id);
                examenColposcopia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examenColposcopia with id " + id + " no longer exists.", enfe);
            }
            Expediente expedientePacienteCedula = examenColposcopia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getExamenColposcopiaList().remove(examenColposcopia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            Personal personalCedula = examenColposcopia.getPersonalCedula();
            if (personalCedula != null) {
                personalCedula.getExamenColposcopiaList().remove(examenColposcopia);
                personalCedula = em.merge(personalCedula);
            }
            em.remove(examenColposcopia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ExamenColposcopia> findExamenColposcopiaEntities() {
        return findExamenColposcopiaEntities(true, -1, -1);
    }

    public List<ExamenColposcopia> findExamenColposcopiaEntities(int maxResults, int firstResult) {
        return findExamenColposcopiaEntities(false, maxResults, firstResult);
    }

    private List<ExamenColposcopia> findExamenColposcopiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExamenColposcopia.class));
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

    public ExamenColposcopia findExamenColposcopia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExamenColposcopia.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamenColposcopiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExamenColposcopia> rt = cq.from(ExamenColposcopia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
