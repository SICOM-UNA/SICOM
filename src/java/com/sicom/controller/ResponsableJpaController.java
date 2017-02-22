/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Paciente;
import com.sicom.entities.Responsable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
 */
public class ResponsableJpaController implements Serializable {

    public ResponsableJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Responsable responsable) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paciente pacienteid = responsable.getPacienteid();
            if (pacienteid != null) {
                pacienteid = em.getReference(pacienteid.getClass(), pacienteid.getId());
                responsable.setPacienteid(pacienteid);
            }
            em.persist(responsable);
            if (pacienteid != null) {
                pacienteid.getResponsableList().add(responsable);
                pacienteid = em.merge(pacienteid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findResponsable(responsable.getId()) != null) {
                throw new PreexistingEntityException("Responsable " + responsable + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Responsable responsable) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Responsable persistentResponsable = em.find(Responsable.class, responsable.getId());
            Paciente pacienteidOld = persistentResponsable.getPacienteid();
            Paciente pacienteidNew = responsable.getPacienteid();
            if (pacienteidNew != null) {
                pacienteidNew = em.getReference(pacienteidNew.getClass(), pacienteidNew.getId());
                responsable.setPacienteid(pacienteidNew);
            }
            responsable = em.merge(responsable);
            if (pacienteidOld != null && !pacienteidOld.equals(pacienteidNew)) {
                pacienteidOld.getResponsableList().remove(responsable);
                pacienteidOld = em.merge(pacienteidOld);
            }
            if (pacienteidNew != null && !pacienteidNew.equals(pacienteidOld)) {
                pacienteidNew.getResponsableList().add(responsable);
                pacienteidNew = em.merge(pacienteidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = responsable.getId();
                if (findResponsable(id) == null) {
                    throw new NonexistentEntityException("The responsable with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Responsable responsable;
            try {
                responsable = em.getReference(Responsable.class, id);
                responsable.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The responsable with id " + id + " no longer exists.", enfe);
            }
            Paciente pacienteid = responsable.getPacienteid();
            if (pacienteid != null) {
                pacienteid.getResponsableList().remove(responsable);
                pacienteid = em.merge(pacienteid);
            }
            em.remove(responsable);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Responsable> findResponsableEntities() {
        return findResponsableEntities(true, -1, -1);
    }

    public List<Responsable> findResponsableEntities(int maxResults, int firstResult) {
        return findResponsableEntities(false, maxResults, firstResult);
    }

    private List<Responsable> findResponsableEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Responsable.class));
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

    public Responsable findResponsable(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Responsable.class, id);
        } finally {
            em.close();
        }
    }

    public int getResponsableCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Responsable> rt = cq.from(Responsable.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
