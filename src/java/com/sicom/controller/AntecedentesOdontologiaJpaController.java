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
            Expediente expedientePacientecedula = antecedentesOdontologia.getExpedientePacientecedula();
            if (expedientePacientecedula != null) {
                expedientePacientecedula = em.getReference(expedientePacientecedula.getClass(), expedientePacientecedula.getId());
                antecedentesOdontologia.setExpedientePacientecedula(expedientePacientecedula);
            }
            em.persist(antecedentesOdontologia);
            if (expedientePacientecedula != null) {
                expedientePacientecedula.getAntecedentesOdontologiaList().add(antecedentesOdontologia);
                expedientePacientecedula = em.merge(expedientePacientecedula);
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
            Expediente expedientePacientecedulaOld = persistentAntecedentesOdontologia.getExpedientePacientecedula();
            Expediente expedientePacientecedulaNew = antecedentesOdontologia.getExpedientePacientecedula();
            if (expedientePacientecedulaNew != null) {
                expedientePacientecedulaNew = em.getReference(expedientePacientecedulaNew.getClass(), expedientePacientecedulaNew.getId());
                antecedentesOdontologia.setExpedientePacientecedula(expedientePacientecedulaNew);
            }
            antecedentesOdontologia = em.merge(antecedentesOdontologia);
            if (expedientePacientecedulaOld != null && !expedientePacientecedulaOld.equals(expedientePacientecedulaNew)) {
                expedientePacientecedulaOld.getAntecedentesOdontologiaList().remove(antecedentesOdontologia);
                expedientePacientecedulaOld = em.merge(expedientePacientecedulaOld);
            }
            if (expedientePacientecedulaNew != null && !expedientePacientecedulaNew.equals(expedientePacientecedulaOld)) {
                expedientePacientecedulaNew.getAntecedentesOdontologiaList().add(antecedentesOdontologia);
                expedientePacientecedulaNew = em.merge(expedientePacientecedulaNew);
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
            Expediente expedientePacientecedula = antecedentesOdontologia.getExpedientePacientecedula();
            if (expedientePacientecedula != null) {
                expedientePacientecedula.getAntecedentesOdontologiaList().remove(antecedentesOdontologia);
                expedientePacientecedula = em.merge(expedientePacientecedula);
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
