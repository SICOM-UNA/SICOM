/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import com.sicom.entities.Autorizacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Personal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
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
        if (autorizacion.getPersonalList() == null) {
            autorizacion.setPersonalList(new ArrayList<Personal>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personal> attachedPersonalList = new ArrayList<Personal>();
            for (Personal personalListPersonalToAttach : autorizacion.getPersonalList()) {
                personalListPersonalToAttach = em.getReference(personalListPersonalToAttach.getClass(), personalListPersonalToAttach.getCedula());
                attachedPersonalList.add(personalListPersonalToAttach);
            }
            autorizacion.setPersonalList(attachedPersonalList);
            em.persist(autorizacion);
            for (Personal personalListPersonal : autorizacion.getPersonalList()) {
                Autorizacion oldAutorizacionnivelOfPersonalListPersonal = personalListPersonal.getAutorizacionnivel();
                personalListPersonal.setAutorizacionnivel(autorizacion);
                personalListPersonal = em.merge(personalListPersonal);
                if (oldAutorizacionnivelOfPersonalListPersonal != null) {
                    oldAutorizacionnivelOfPersonalListPersonal.getPersonalList().remove(personalListPersonal);
                    oldAutorizacionnivelOfPersonalListPersonal = em.merge(oldAutorizacionnivelOfPersonalListPersonal);
                }
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

    public void edit(Autorizacion autorizacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autorizacion persistentAutorizacion = em.find(Autorizacion.class, autorizacion.getNivel());
            List<Personal> personalListOld = persistentAutorizacion.getPersonalList();
            List<Personal> personalListNew = autorizacion.getPersonalList();
            List<String> illegalOrphanMessages = null;
            for (Personal personalListOldPersonal : personalListOld) {
                if (!personalListNew.contains(personalListOldPersonal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Personal " + personalListOldPersonal + " since its autorizacionnivel field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Personal> attachedPersonalListNew = new ArrayList<Personal>();
            for (Personal personalListNewPersonalToAttach : personalListNew) {
                personalListNewPersonalToAttach = em.getReference(personalListNewPersonalToAttach.getClass(), personalListNewPersonalToAttach.getCedula());
                attachedPersonalListNew.add(personalListNewPersonalToAttach);
            }
            personalListNew = attachedPersonalListNew;
            autorizacion.setPersonalList(personalListNew);
            autorizacion = em.merge(autorizacion);
            for (Personal personalListNewPersonal : personalListNew) {
                if (!personalListOld.contains(personalListNewPersonal)) {
                    Autorizacion oldAutorizacionnivelOfPersonalListNewPersonal = personalListNewPersonal.getAutorizacionnivel();
                    personalListNewPersonal.setAutorizacionnivel(autorizacion);
                    personalListNewPersonal = em.merge(personalListNewPersonal);
                    if (oldAutorizacionnivelOfPersonalListNewPersonal != null && !oldAutorizacionnivelOfPersonalListNewPersonal.equals(autorizacion)) {
                        oldAutorizacionnivelOfPersonalListNewPersonal.getPersonalList().remove(personalListNewPersonal);
                        oldAutorizacionnivelOfPersonalListNewPersonal = em.merge(oldAutorizacionnivelOfPersonalListNewPersonal);
                    }
                }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<Personal> personalListOrphanCheck = autorizacion.getPersonalList();
            for (Personal personalListOrphanCheckPersonal : personalListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Autorizacion (" + autorizacion + ") cannot be destroyed since the Personal " + personalListOrphanCheckPersonal + " in its personalList field has a non-nullable autorizacionnivel field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
