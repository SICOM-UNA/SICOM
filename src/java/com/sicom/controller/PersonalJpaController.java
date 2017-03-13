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
import com.sicom.entities.Autorizacion;
import com.sicom.entities.Departamento;
import com.sicom.entities.Personal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class PersonalJpaController implements Serializable {

    public PersonalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personal personal) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autorizacion autorizacionnivel = personal.getAutorizacionnivel();
            if (autorizacionnivel != null) {
                autorizacionnivel = em.getReference(autorizacionnivel.getClass(), autorizacionnivel.getNivel());
                personal.setAutorizacionnivel(autorizacionnivel);
            }
            Departamento departamentoid = personal.getDepartamentoid();
            if (departamentoid != null) {
                departamentoid = em.getReference(departamentoid.getClass(), departamentoid.getId());
                personal.setDepartamentoid(departamentoid);
            }
            em.persist(personal);
            if (autorizacionnivel != null) {
                autorizacionnivel.getPersonalList().add(personal);
                autorizacionnivel = em.merge(autorizacionnivel);
            }
            if (departamentoid != null) {
                departamentoid.getPersonalList().add(personal);
                departamentoid = em.merge(departamentoid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonal(personal.getCedula()) != null) {
                throw new PreexistingEntityException("Personal " + personal + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Personal personal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personal persistentPersonal = em.find(Personal.class, personal.getCedula());
            Autorizacion autorizacionnivelOld = persistentPersonal.getAutorizacionnivel();
            Autorizacion autorizacionnivelNew = personal.getAutorizacionnivel();
            Departamento departamentoidOld = persistentPersonal.getDepartamentoid();
            Departamento departamentoidNew = personal.getDepartamentoid();
            if (autorizacionnivelNew != null) {
                autorizacionnivelNew = em.getReference(autorizacionnivelNew.getClass(), autorizacionnivelNew.getNivel());
                personal.setAutorizacionnivel(autorizacionnivelNew);
            }
            if (departamentoidNew != null) {
                departamentoidNew = em.getReference(departamentoidNew.getClass(), departamentoidNew.getId());
                personal.setDepartamentoid(departamentoidNew);
            }
            personal = em.merge(personal);
            if (autorizacionnivelOld != null && !autorizacionnivelOld.equals(autorizacionnivelNew)) {
                autorizacionnivelOld.getPersonalList().remove(personal);
                autorizacionnivelOld = em.merge(autorizacionnivelOld);
            }
            if (autorizacionnivelNew != null && !autorizacionnivelNew.equals(autorizacionnivelOld)) {
                autorizacionnivelNew.getPersonalList().add(personal);
                autorizacionnivelNew = em.merge(autorizacionnivelNew);
            }
            if (departamentoidOld != null && !departamentoidOld.equals(departamentoidNew)) {
                departamentoidOld.getPersonalList().remove(personal);
                departamentoidOld = em.merge(departamentoidOld);
            }
            if (departamentoidNew != null && !departamentoidNew.equals(departamentoidOld)) {
                departamentoidNew.getPersonalList().add(personal);
                departamentoidNew = em.merge(departamentoidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = personal.getCedula();
                if (findPersonal(id) == null) {
                    throw new NonexistentEntityException("The personal with id " + id + " no longer exists.");
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
            Personal personal;
            try {
                personal = em.getReference(Personal.class, id);
                personal.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personal with id " + id + " no longer exists.", enfe);
            }
            Autorizacion autorizacionnivel = personal.getAutorizacionnivel();
            if (autorizacionnivel != null) {
                autorizacionnivel.getPersonalList().remove(personal);
                autorizacionnivel = em.merge(autorizacionnivel);
            }
            Departamento departamentoid = personal.getDepartamentoid();
            if (departamentoid != null) {
                departamentoid.getPersonalList().remove(personal);
                departamentoid = em.merge(departamentoid);
            }
            em.remove(personal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Personal> findPersonalEntities() {
        return findPersonalEntities(true, -1, -1);
    }

    public List<Personal> findPersonalEntities(int maxResults, int firstResult) {
        return findPersonalEntities(false, maxResults, firstResult);
    }

    private List<Personal> findPersonalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Personal.class));
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

    public Personal findPersonal(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Personal.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Personal> rt = cq.from(Personal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
