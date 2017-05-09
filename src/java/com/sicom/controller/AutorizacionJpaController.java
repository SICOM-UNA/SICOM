/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import com.sicom.entities.Autorizacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Personal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
 */
public class AutorizacionJpaController implements Serializable {

    public AutorizacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Autorizacion autorizacion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personal personalcedula = autorizacion.getPersonalCedula();
            if (personalcedula != null) {
                personalcedula = em.getReference(personalcedula.getClass(), personalcedula.getCedula());
                autorizacion.setPersonalCedula(personalcedula);
            }
            em.persist(autorizacion);
            if (personalcedula != null) {
                personalcedula.getAutorizacionList().add(autorizacion);
                personalcedula = em.merge(personalcedula);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAutorizacion(autorizacion.getNivel()) != null) {
                throw new PreexistingEntityException("Autorizacion " + autorizacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Autorizacion autorizacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autorizacion persistentAutorizacion = em.find(Autorizacion.class, autorizacion.getNivel());
            Personal personalcedulaOld = persistentAutorizacion.getPersonalCedula();
            Personal personalcedulaNew = autorizacion.getPersonalCedula();
            if (personalcedulaNew != null) {
                personalcedulaNew = em.getReference(personalcedulaNew.getClass(), personalcedulaNew.getCedula());
                autorizacion.setPersonalCedula(personalcedulaNew);
            }
            autorizacion = em.merge(autorizacion);
            if (personalcedulaOld != null && !personalcedulaOld.equals(personalcedulaNew)) {
                personalcedulaOld.getAutorizacionList().remove(autorizacion);
                personalcedulaOld = em.merge(personalcedulaOld);
            }
            if (personalcedulaNew != null && !personalcedulaNew.equals(personalcedulaOld)) {
                personalcedulaNew.getAutorizacionList().add(autorizacion);
                personalcedulaNew = em.merge(personalcedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = autorizacion.getNivel();
                if (findAutorizacion(id) == null) {
                    throw new NonexistentEntityException("The autorizacion with id " + id + " no longer exists.");
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
            Autorizacion autorizacion;
            try {
                autorizacion = em.getReference(Autorizacion.class, id);
                autorizacion.getNivel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autorizacion with id " + id + " no longer exists.", enfe);
            }
            Personal personalcedula = autorizacion.getPersonalCedula();
            if (personalcedula != null) {
                personalcedula.getAutorizacionList().remove(autorizacion);
                personalcedula = em.merge(personalcedula);
            }
            em.remove(autorizacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Autorizacion> findAutorizacionEntities() {
        return findAutorizacionEntities(true, -1, -1);
    }

    public List<Autorizacion> findAutorizacionEntities(int maxResults, int firstResult) {
        return findAutorizacionEntities(false, maxResults, firstResult);
    }

    private List<Autorizacion> findAutorizacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Autorizacion.class));
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

    public Autorizacion findAutorizacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Autorizacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutorizacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Autorizacion> rt = cq.from(Autorizacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
