/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import com.sicom.entities.Login;
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
 * @author WVQ
 */
public class LoginJpaController implements Serializable {

    public LoginJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Login login) throws PreexistingEntityException, Exception {
        if (login.getPersonalList() == null) {
            login.setPersonalList(new ArrayList<Personal>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personal> attachedPersonalList = new ArrayList<Personal>();
            for (Personal personalListPersonalToAttach : login.getPersonalList()) {
                personalListPersonalToAttach = em.getReference(personalListPersonalToAttach.getClass(), personalListPersonalToAttach.getId());
                attachedPersonalList.add(personalListPersonalToAttach);
            }
            login.setPersonalList(attachedPersonalList);
            em.persist(login);
            for (Personal personalListPersonal : login.getPersonalList()) {
                Login oldLoginUsuarioOfPersonalListPersonal = personalListPersonal.getLoginUsuario();
                personalListPersonal.setLoginUsuario(login);
                personalListPersonal = em.merge(personalListPersonal);
                if (oldLoginUsuarioOfPersonalListPersonal != null) {
                    oldLoginUsuarioOfPersonalListPersonal.getPersonalList().remove(personalListPersonal);
                    oldLoginUsuarioOfPersonalListPersonal = em.merge(oldLoginUsuarioOfPersonalListPersonal);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLogin(login.getUsuario()) != null) {
                throw new PreexistingEntityException("Login " + login + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Login login) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Login persistentLogin = em.find(Login.class, login.getUsuario());
            List<Personal> personalListOld = persistentLogin.getPersonalList();
            List<Personal> personalListNew = login.getPersonalList();
            List<String> illegalOrphanMessages = null;
            for (Personal personalListOldPersonal : personalListOld) {
                if (!personalListNew.contains(personalListOldPersonal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Personal " + personalListOldPersonal + " since its loginUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Personal> attachedPersonalListNew = new ArrayList<Personal>();
            for (Personal personalListNewPersonalToAttach : personalListNew) {
                personalListNewPersonalToAttach = em.getReference(personalListNewPersonalToAttach.getClass(), personalListNewPersonalToAttach.getId());
                attachedPersonalListNew.add(personalListNewPersonalToAttach);
            }
            personalListNew = attachedPersonalListNew;
            login.setPersonalList(personalListNew);
            login = em.merge(login);
            for (Personal personalListNewPersonal : personalListNew) {
                if (!personalListOld.contains(personalListNewPersonal)) {
                    Login oldLoginUsuarioOfPersonalListNewPersonal = personalListNewPersonal.getLoginUsuario();
                    personalListNewPersonal.setLoginUsuario(login);
                    personalListNewPersonal = em.merge(personalListNewPersonal);
                    if (oldLoginUsuarioOfPersonalListNewPersonal != null && !oldLoginUsuarioOfPersonalListNewPersonal.equals(login)) {
                        oldLoginUsuarioOfPersonalListNewPersonal.getPersonalList().remove(personalListNewPersonal);
                        oldLoginUsuarioOfPersonalListNewPersonal = em.merge(oldLoginUsuarioOfPersonalListNewPersonal);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = login.getUsuario();
                if (findLogin(id) == null) {
                    throw new NonexistentEntityException("The login with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Login login;
            try {
                login = em.getReference(Login.class, id);
                login.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The login with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Personal> personalListOrphanCheck = login.getPersonalList();
            for (Personal personalListOrphanCheckPersonal : personalListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Login (" + login + ") cannot be destroyed since the Personal " + personalListOrphanCheckPersonal + " in its personalList field has a non-nullable loginUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(login);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Login> findLoginEntities() {
        return findLoginEntities(true, -1, -1);
    }

    public List<Login> findLoginEntities(int maxResults, int firstResult) {
        return findLoginEntities(false, maxResults, firstResult);
    }

    private List<Login> findLoginEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Login.class));
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

    public Login findLogin(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Login.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoginCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Login> rt = cq.from(Login.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
