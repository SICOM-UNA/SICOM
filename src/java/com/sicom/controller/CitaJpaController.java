/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.Cita;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Departamento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class CitaJpaController implements Serializable {

    public CitaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cita cita) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento departamentoid = cita.getDepartamentoid();
            if (departamentoid != null) {
                departamentoid = em.getReference(departamentoid.getClass(), departamentoid.getId());
                cita.setDepartamentoid(departamentoid);
            }
            em.persist(cita);
            if (departamentoid != null) {
                departamentoid.getCitaList().add(cita);
                departamentoid = em.merge(departamentoid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cita cita) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cita persistentCita = em.find(Cita.class, cita.getId());
            Departamento departamentoidOld = persistentCita.getDepartamentoid();
            Departamento departamentoidNew = cita.getDepartamentoid();
            if (departamentoidNew != null) {
                departamentoidNew = em.getReference(departamentoidNew.getClass(), departamentoidNew.getId());
                cita.setDepartamentoid(departamentoidNew);
            }
            cita = em.merge(cita);
            if (departamentoidOld != null && !departamentoidOld.equals(departamentoidNew)) {
                departamentoidOld.getCitaList().remove(cita);
                departamentoidOld = em.merge(departamentoidOld);
            }
            if (departamentoidNew != null && !departamentoidNew.equals(departamentoidOld)) {
                departamentoidNew.getCitaList().add(cita);
                departamentoidNew = em.merge(departamentoidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cita.getId();
                if (findCita(id) == null) {
                    throw new NonexistentEntityException("The cita with id " + id + " no longer exists.");
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
            Cita cita;
            try {
                cita = em.getReference(Cita.class, id);
                cita.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cita with id " + id + " no longer exists.", enfe);
            }
            Departamento departamentoid = cita.getDepartamentoid();
            if (departamentoid != null) {
                departamentoid.getCitaList().remove(cita);
                departamentoid = em.merge(departamentoid);
            }
            em.remove(cita);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cita> findCitaEntities() {
        return findCitaEntities(true, -1, -1);
    }

    public List<Cita> findCitaEntities(int maxResults, int firstResult) {
        return findCitaEntities(false, maxResults, firstResult);
    }

    private List<Cita> findCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cita.class));
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

    public Cita findCita(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cita.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cita> rt = cq.from(Cita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
