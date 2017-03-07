/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Examen;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class ExamenJpaController implements Serializable {

    public ExamenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Examen examen) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesGinecologia pacienteId = examen.getPacienteId();
            if (pacienteId != null) {
                pacienteId = em.getReference(pacienteId.getClass(), pacienteId.getPacienteid());
                examen.setPacienteId(pacienteId);
            }
            em.persist(examen);
            if (pacienteId != null) {
                pacienteId.getExamenCollection().add(examen);
                pacienteId = em.merge(pacienteId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Examen examen) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Examen persistentExamen = em.find(Examen.class, examen.getId());
            AntecedentesGinecologia pacienteIdOld = persistentExamen.getPacienteId();
            AntecedentesGinecologia pacienteIdNew = examen.getPacienteId();
            if (pacienteIdNew != null) {
                pacienteIdNew = em.getReference(pacienteIdNew.getClass(), pacienteIdNew.getPacienteid());
                examen.setPacienteId(pacienteIdNew);
            }
            examen = em.merge(examen);
            if (pacienteIdOld != null && !pacienteIdOld.equals(pacienteIdNew)) {
                pacienteIdOld.getExamenCollection().remove(examen);
                pacienteIdOld = em.merge(pacienteIdOld);
            }
            if (pacienteIdNew != null && !pacienteIdNew.equals(pacienteIdOld)) {
                pacienteIdNew.getExamenCollection().add(examen);
                pacienteIdNew = em.merge(pacienteIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = examen.getId();
                if (findExamen(id) == null) {
                    throw new NonexistentEntityException("The examen with id " + id + " no longer exists.");
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
            Examen examen;
            try {
                examen = em.getReference(Examen.class, id);
                examen.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examen with id " + id + " no longer exists.", enfe);
            }
            AntecedentesGinecologia pacienteId = examen.getPacienteId();
            if (pacienteId != null) {
                pacienteId.getExamenCollection().remove(examen);
                pacienteId = em.merge(pacienteId);
            }
            em.remove(examen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Examen> findExamenEntities() {
        return findExamenEntities(true, -1, -1);
    }

    public List<Examen> findExamenEntities(int maxResults, int firstResult) {
        return findExamenEntities(false, maxResults, firstResult);
    }

    private List<Examen> findExamenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Examen.class));
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

    public Examen findExamen(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Examen.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Examen> rt = cq.from(Examen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
