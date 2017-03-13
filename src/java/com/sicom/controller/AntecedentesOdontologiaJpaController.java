/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.AntecedentesOdontologia;
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
 * @author WVQ
 */
public class AntecedentesOdontologiaJpaController implements Serializable {

    public AntecedentesOdontologiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AntecedentesOdontologia antecedentesOdontologia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente expedientePacienteCedula = antecedentesOdontologia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula = em.getReference(expedientePacienteCedula.getClass(), expedientePacienteCedula.getExpedientePK());
                antecedentesOdontologia.setExpedientePacienteCedula(expedientePacienteCedula);
            }
            em.persist(antecedentesOdontologia);
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getAntecedentesOdontologiaList().add(antecedentesOdontologia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AntecedentesOdontologia antecedentesOdontologia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesOdontologia persistentAntecedentesOdontologia = em.find(AntecedentesOdontologia.class, antecedentesOdontologia.getId());
            Expediente expedientePacienteCedulaOld = persistentAntecedentesOdontologia.getExpedientePacienteCedula();
            Expediente expedientePacienteCedulaNew = antecedentesOdontologia.getExpedientePacienteCedula();
            if (expedientePacienteCedulaNew != null) {
                expedientePacienteCedulaNew = em.getReference(expedientePacienteCedulaNew.getClass(), expedientePacienteCedulaNew.getExpedientePK());
                antecedentesOdontologia.setExpedientePacienteCedula(expedientePacienteCedulaNew);
            }
            antecedentesOdontologia = em.merge(antecedentesOdontologia);
            if (expedientePacienteCedulaOld != null && !expedientePacienteCedulaOld.equals(expedientePacienteCedulaNew)) {
                expedientePacienteCedulaOld.getAntecedentesOdontologiaList().remove(antecedentesOdontologia);
                expedientePacienteCedulaOld = em.merge(expedientePacienteCedulaOld);
            }
            if (expedientePacienteCedulaNew != null && !expedientePacienteCedulaNew.equals(expedientePacienteCedulaOld)) {
                expedientePacienteCedulaNew.getAntecedentesOdontologiaList().add(antecedentesOdontologia);
                expedientePacienteCedulaNew = em.merge(expedientePacienteCedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = antecedentesOdontologia.getId();
                if (findAntecedentesOdontologia(id) == null) {
                    throw new NonexistentEntityException("The antecedentesOdontologia with id " + id + " no longer exists.");
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
            AntecedentesOdontologia antecedentesOdontologia;
            try {
                antecedentesOdontologia = em.getReference(AntecedentesOdontologia.class, id);
                antecedentesOdontologia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The antecedentesOdontologia with id " + id + " no longer exists.", enfe);
            }
            Expediente expedientePacienteCedula = antecedentesOdontologia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getAntecedentesOdontologiaList().remove(antecedentesOdontologia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            em.remove(antecedentesOdontologia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AntecedentesOdontologia> findAntecedentesOdontologiaEntities() {
        return findAntecedentesOdontologiaEntities(true, -1, -1);
    }

    public List<AntecedentesOdontologia> findAntecedentesOdontologiaEntities(int maxResults, int firstResult) {
        return findAntecedentesOdontologiaEntities(false, maxResults, firstResult);
    }

    private List<AntecedentesOdontologia> findAntecedentesOdontologiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AntecedentesOdontologia.class));
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

    public AntecedentesOdontologia findAntecedentesOdontologia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AntecedentesOdontologia.class, id);
        } finally {
            em.close();
        }
    }

    public int getAntecedentesOdontologiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AntecedentesOdontologia> rt = cq.from(AntecedentesOdontologia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
