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
import java.util.ArrayList;
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
            Paciente pacientecedula = responsable.getPacienteCedula();
            if (pacientecedula != null) {
                pacientecedula = em.getReference(pacientecedula.getClass(), pacientecedula.getCedula());
                responsable.setPacienteCedula(pacientecedula);
            }
            em.persist(responsable);
            if (pacientecedula != null) {
                pacientecedula.getResponsableList().add(responsable);
                pacientecedula = em.merge(pacientecedula);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findResponsable(responsable.getCedula()) != null) {
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
            Responsable persistentResponsable = em.find(Responsable.class, responsable.getCedula());
            Paciente pacientecedulaOld = persistentResponsable.getPacienteCedula();
            Paciente pacientecedulaNew = responsable.getPacienteCedula();
            if (pacientecedulaNew != null) {
                pacientecedulaNew = em.getReference(pacientecedulaNew.getClass(), pacientecedulaNew.getCedula());
                responsable.setPacienteCedula(pacientecedulaNew);
            }
            responsable = em.merge(responsable);
            if (pacientecedulaOld != null && !pacientecedulaOld.equals(pacientecedulaNew)) {
                pacientecedulaOld.getResponsableList().remove(responsable);
                pacientecedulaOld = em.merge(pacientecedulaOld);
            }
            if (pacientecedulaNew != null && !pacientecedulaNew.equals(pacientecedulaOld)) {
                pacientecedulaNew.getResponsableList().add(responsable);
                pacientecedulaNew = em.merge(pacientecedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = responsable.getCedula();
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
                responsable.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The responsable with id " + id + " no longer exists.", enfe);
            }
            Paciente pacientecedula = responsable.getPacienteCedula();
            if (pacientecedula != null) {
                pacientecedula.getResponsableList().remove(responsable);
                pacientecedula = em.merge(pacientecedula);
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

    public List<Responsable> findResponsableByCedulaPaciente(String cedulaPaciente) {
        EntityManager em = getEntityManager();
        List<Responsable> listaResponsables = new ArrayList<>();
        
        try {
            Query query = em.createQuery("select r from Responsable r where r.pacienteCedula.cedula = ?1");
            query.setParameter( 1, cedulaPaciente);
            listaResponsables = (List<Responsable>)query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        return listaResponsables;
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