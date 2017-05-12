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
            Expediente expedientePacientecedula = examenOdontologia.getExpedientePacientecedula();
            if (expedientePacientecedula != null) {
                expedientePacientecedula = em.getReference(expedientePacientecedula.getClass(), expedientePacientecedula.getId());
                examenOdontologia.setExpedientePacientecedula(expedientePacientecedula);
            }
            Personal personalcedula = examenOdontologia.getPersonalCedula();
            if (personalcedula != null) {
                personalcedula = em.getReference(personalcedula.getClass(), personalcedula.getCedula());
                examenOdontologia.setPersonalCedula(personalcedula);
            }
            em.persist(examenOdontologia);
            if (expedientePacientecedula != null) {
                expedientePacientecedula.getExamenOdontologiaList().add(examenOdontologia);
                expedientePacientecedula = em.merge(expedientePacientecedula);
            }
            if (personalcedula != null) {
                personalcedula.getExamenOdontologiaList().add(examenOdontologia);
                personalcedula = em.merge(personalcedula);
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
            Expediente expedientePacientecedulaOld = persistentExamenOdontologia.getExpedientePacientecedula();
            Expediente expedientePacientecedulaNew = examenOdontologia.getExpedientePacientecedula();
            Personal personalcedulaOld = persistentExamenOdontologia.getPersonalCedula();
            Personal personalcedulaNew = examenOdontologia.getPersonalCedula();
            if (expedientePacientecedulaNew != null) {
                expedientePacientecedulaNew = em.getReference(expedientePacientecedulaNew.getClass(), expedientePacientecedulaNew.getId());
                examenOdontologia.setExpedientePacientecedula(expedientePacientecedulaNew);
            }
            if (personalcedulaNew != null) {
                personalcedulaNew = em.getReference(personalcedulaNew.getClass(), personalcedulaNew.getCedula());
                examenOdontologia.setPersonalCedula(personalcedulaNew);
            }
            examenOdontologia = em.merge(examenOdontologia);
            if (expedientePacientecedulaOld != null && !expedientePacientecedulaOld.equals(expedientePacientecedulaNew)) {
                expedientePacientecedulaOld.getExamenOdontologiaList().remove(examenOdontologia);
                expedientePacientecedulaOld = em.merge(expedientePacientecedulaOld);
            }
            if (expedientePacientecedulaNew != null && !expedientePacientecedulaNew.equals(expedientePacientecedulaOld)) {
                expedientePacientecedulaNew.getExamenOdontologiaList().add(examenOdontologia);
                expedientePacientecedulaNew = em.merge(expedientePacientecedulaNew);
            }
            if (personalcedulaOld != null && !personalcedulaOld.equals(personalcedulaNew)) {
                personalcedulaOld.getExamenOdontologiaList().remove(examenOdontologia);
                personalcedulaOld = em.merge(personalcedulaOld);
            }
            if (personalcedulaNew != null && !personalcedulaNew.equals(personalcedulaOld)) {
                personalcedulaNew.getExamenOdontologiaList().add(examenOdontologia);
                personalcedulaNew = em.merge(personalcedulaNew);
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
            Expediente expedientePacientecedula = examenOdontologia.getExpedientePacientecedula();
            if (expedientePacientecedula != null) {
                expedientePacientecedula.getExamenOdontologiaList().remove(examenOdontologia);
                expedientePacientecedula = em.merge(expedientePacientecedula);
            }
            Personal personalcedula = examenOdontologia.getPersonalCedula();
            if (personalcedula != null) {
                personalcedula.getExamenOdontologiaList().remove(examenOdontologia);
                personalcedula = em.merge(personalcedula);
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
