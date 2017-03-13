/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.AntecedentesGinecologia;
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
public class AntecedentesGinecologiaJpaController implements Serializable {

    public AntecedentesGinecologiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AntecedentesGinecologia antecedentesGinecologia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente expedientePacienteCedula = antecedentesGinecologia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula = em.getReference(expedientePacienteCedula.getClass(), expedientePacienteCedula.getExpedientePK());
                antecedentesGinecologia.setExpedientePacienteCedula(expedientePacienteCedula);
            }
            em.persist(antecedentesGinecologia);
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getAntecedentesGinecologiaList().add(antecedentesGinecologia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AntecedentesGinecologia antecedentesGinecologia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesGinecologia persistentAntecedentesGinecologia = em.find(AntecedentesGinecologia.class, antecedentesGinecologia.getId());
            Expediente expedientePacienteCedulaOld = persistentAntecedentesGinecologia.getExpedientePacienteCedula();
            Expediente expedientePacienteCedulaNew = antecedentesGinecologia.getExpedientePacienteCedula();
            if (expedientePacienteCedulaNew != null) {
                expedientePacienteCedulaNew = em.getReference(expedientePacienteCedulaNew.getClass(), expedientePacienteCedulaNew.getExpedientePK());
                antecedentesGinecologia.setExpedientePacienteCedula(expedientePacienteCedulaNew);
            }
            antecedentesGinecologia = em.merge(antecedentesGinecologia);
            if (expedientePacienteCedulaOld != null && !expedientePacienteCedulaOld.equals(expedientePacienteCedulaNew)) {
                expedientePacienteCedulaOld.getAntecedentesGinecologiaList().remove(antecedentesGinecologia);
                expedientePacienteCedulaOld = em.merge(expedientePacienteCedulaOld);
            }
            if (expedientePacienteCedulaNew != null && !expedientePacienteCedulaNew.equals(expedientePacienteCedulaOld)) {
                expedientePacienteCedulaNew.getAntecedentesGinecologiaList().add(antecedentesGinecologia);
                expedientePacienteCedulaNew = em.merge(expedientePacienteCedulaNew);
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
            Expediente expedientePacienteCedula = antecedentesGinecologia.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getAntecedentesGinecologiaList().remove(antecedentesGinecologia);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
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
