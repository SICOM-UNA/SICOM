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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personal personal = login.getPersonal();
            em.persist(login);
            
            if (personal != null) {
                Login oldLoginusuarioOfPersonal = personal.getLoginUsuario();
                if (oldLoginusuarioOfPersonal != null) {
                    oldLoginusuarioOfPersonal.setPersonal(null);
                    oldLoginusuarioOfPersonal = em.merge(oldLoginusuarioOfPersonal);
                }
                personal.setLoginUsuario(login);
                personal = em.merge(personal);
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
            Personal personalOld = persistentLogin.getPersonal();
            Personal personalNew = login.getPersonal();
            List<String> illegalOrphanMessages = null;
            if (personalOld != null && !personalOld.equals(personalNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Personal " + personalOld + " since its loginusuario field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (personalNew != null) {
                personalNew = em.getReference(personalNew.getClass(), personalNew.getCedula());
                login.setPersonal(personalNew);
            }
            login = em.merge(login);
            if (personalNew != null && !personalNew.equals(personalOld)) {
                Login oldLoginusuarioOfPersonal = personalNew.getLoginUsuario();
                if (oldLoginusuarioOfPersonal != null) {
                    oldLoginusuarioOfPersonal.setPersonal(null);
                    oldLoginusuarioOfPersonal = em.merge(oldLoginusuarioOfPersonal);
                }
                personalNew.setLoginUsuario(login);
                personalNew = em.merge(personalNew);
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
            Personal personalOrphanCheck = login.getPersonal();
            if (personalOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Login (" + login + ") cannot be destroyed since the Personal " + personalOrphanCheck + " in its personal field has a non-nullable loginusuario field.");
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