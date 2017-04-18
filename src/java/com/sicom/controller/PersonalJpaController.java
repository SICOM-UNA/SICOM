/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Autorizacion;
import com.sicom.entities.Departamento;
import com.sicom.entities.Login;
import com.sicom.entities.ExamenColposcopia;
import java.util.ArrayList;
import java.util.List;
import com.sicom.entities.ExamenOdontologia;
import com.sicom.entities.ExamenGinecologia;
import com.sicom.entities.Personal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
 */
public class PersonalJpaController implements Serializable {

    public PersonalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personal personal) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (personal.getExamenColposcopiaList() == null) {
            personal.setExamenColposcopiaList(new ArrayList<ExamenColposcopia>());
        }
        if (personal.getExamenOdontologiaList() == null) {
            personal.setExamenOdontologiaList(new ArrayList<ExamenOdontologia>());
        }
        if (personal.getExamenGinecologiaList() == null) {
            personal.setExamenGinecologiaList(new ArrayList<ExamenGinecologia>());
        }
        List<String> illegalOrphanMessages = null;
        Login loginusuarioOrphanCheck = personal.getLoginUsuario();
        if (loginusuarioOrphanCheck != null) {
            Personal oldPersonalOfLoginusuario = loginusuarioOrphanCheck.getPersonal();
            if (oldPersonalOfLoginusuario != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Login " + loginusuarioOrphanCheck + " already has an item of type Personal whose loginusuario column cannot be null. Please make another selection for the loginusuario field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autorizacion autorizacionnivel = personal.getAutorizacionNivel();
            if (autorizacionnivel != null) {
                autorizacionnivel = em.getReference(autorizacionnivel.getClass(), autorizacionnivel.getNivel());
                personal.setAutorizacionNivel(autorizacionnivel);
            }
            Departamento departamentoid = personal.getDepartamentoId();
            if (departamentoid != null) {
                departamentoid = em.getReference(departamentoid.getClass(), departamentoid.getId());
                personal.setDepartamentoId(departamentoid);
            }
            Login loginusuario = personal.getLoginUsuario();
            if (loginusuario != null) {
                loginusuario = em.getReference(loginusuario.getClass(), loginusuario.getUsuario());
                personal.setLoginUsuario(loginusuario);
            }
            List<ExamenColposcopia> attachedExamenColposcopiaList = new ArrayList<ExamenColposcopia>();
            for (ExamenColposcopia examenColposcopiaListExamenColposcopiaToAttach : personal.getExamenColposcopiaList()) {
                examenColposcopiaListExamenColposcopiaToAttach = em.getReference(examenColposcopiaListExamenColposcopiaToAttach.getClass(), examenColposcopiaListExamenColposcopiaToAttach.getId());
                attachedExamenColposcopiaList.add(examenColposcopiaListExamenColposcopiaToAttach);
            }
            personal.setExamenColposcopiaList(attachedExamenColposcopiaList);
            List<ExamenOdontologia> attachedExamenOdontologiaList = new ArrayList<ExamenOdontologia>();
            for (ExamenOdontologia examenOdontologiaListExamenOdontologiaToAttach : personal.getExamenOdontologiaList()) {
                examenOdontologiaListExamenOdontologiaToAttach = em.getReference(examenOdontologiaListExamenOdontologiaToAttach.getClass(), examenOdontologiaListExamenOdontologiaToAttach.getId());
                attachedExamenOdontologiaList.add(examenOdontologiaListExamenOdontologiaToAttach);
            }
            personal.setExamenOdontologiaList(attachedExamenOdontologiaList);
            List<ExamenGinecologia> attachedExamenGinecologiaList = new ArrayList<ExamenGinecologia>();
            for (ExamenGinecologia examenGinecologiaListExamenGinecologiaToAttach : personal.getExamenGinecologiaList()) {
                examenGinecologiaListExamenGinecologiaToAttach = em.getReference(examenGinecologiaListExamenGinecologiaToAttach.getClass(), examenGinecologiaListExamenGinecologiaToAttach.getId());
                attachedExamenGinecologiaList.add(examenGinecologiaListExamenGinecologiaToAttach);
            }
            personal.setExamenGinecologiaList(attachedExamenGinecologiaList);
            em.persist(personal);
            if (autorizacionnivel != null) {
                autorizacionnivel.getPersonalList().add(personal);
                autorizacionnivel = em.merge(autorizacionnivel);
            }
            if (departamentoid != null) {
                departamentoid.getPersonalList().add(personal);
                departamentoid = em.merge(departamentoid);
            }
            if (loginusuario != null) {
                loginusuario.setPersonal(personal);
                loginusuario = em.merge(loginusuario);
            }
            for (ExamenColposcopia examenColposcopiaListExamenColposcopia : personal.getExamenColposcopiaList()) {
                Personal oldPersonalcedulaOfExamenColposcopiaListExamenColposcopia = examenColposcopiaListExamenColposcopia.getPersonalcedula();
                examenColposcopiaListExamenColposcopia.setPersonalcedula(personal);
                examenColposcopiaListExamenColposcopia = em.merge(examenColposcopiaListExamenColposcopia);
                if (oldPersonalcedulaOfExamenColposcopiaListExamenColposcopia != null) {
                    oldPersonalcedulaOfExamenColposcopiaListExamenColposcopia.getExamenColposcopiaList().remove(examenColposcopiaListExamenColposcopia);
                    oldPersonalcedulaOfExamenColposcopiaListExamenColposcopia = em.merge(oldPersonalcedulaOfExamenColposcopiaListExamenColposcopia);
                }
            }
            for (ExamenOdontologia examenOdontologiaListExamenOdontologia : personal.getExamenOdontologiaList()) {
                Personal oldPersonalcedulaOfExamenOdontologiaListExamenOdontologia = examenOdontologiaListExamenOdontologia.getPersonalcedula();
                examenOdontologiaListExamenOdontologia.setPersonalcedula(personal);
                examenOdontologiaListExamenOdontologia = em.merge(examenOdontologiaListExamenOdontologia);
                if (oldPersonalcedulaOfExamenOdontologiaListExamenOdontologia != null) {
                    oldPersonalcedulaOfExamenOdontologiaListExamenOdontologia.getExamenOdontologiaList().remove(examenOdontologiaListExamenOdontologia);
                    oldPersonalcedulaOfExamenOdontologiaListExamenOdontologia = em.merge(oldPersonalcedulaOfExamenOdontologiaListExamenOdontologia);
                }
            }
            for (ExamenGinecologia examenGinecologiaListExamenGinecologia : personal.getExamenGinecologiaList()) {
                Personal oldPersonalcedulaOfExamenGinecologiaListExamenGinecologia = examenGinecologiaListExamenGinecologia.getPersonalcedula();
                examenGinecologiaListExamenGinecologia.setPersonalcedula(personal);
                examenGinecologiaListExamenGinecologia = em.merge(examenGinecologiaListExamenGinecologia);
                if (oldPersonalcedulaOfExamenGinecologiaListExamenGinecologia != null) {
                    oldPersonalcedulaOfExamenGinecologiaListExamenGinecologia.getExamenGinecologiaList().remove(examenGinecologiaListExamenGinecologia);
                    oldPersonalcedulaOfExamenGinecologiaListExamenGinecologia = em.merge(oldPersonalcedulaOfExamenGinecologiaListExamenGinecologia);
                }
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

    public void edit(Personal personal) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personal persistentPersonal = em.find(Personal.class, personal.getCedula());
//            Autorizacion autorizacionnivelOld = persistentPersonal.getAutorizacionNivel();
//            Autorizacion autorizacionnivelNew = personal.getAutorizacionNivel();
//            Departamento departamentoidOld = persistentPersonal.getDepartamentoId();
//            Departamento departamentoidNew = personal.getDepartamentoId();
//            Login loginusuarioOld = persistentPersonal.getLoginUsuario();
//            Login loginusuarioNew = personal.getLoginUsuario();
//
//            if (autorizacionnivelNew != null) {
//                autorizacionnivelNew = em.getReference(autorizacionnivelNew.getClass(), autorizacionnivelNew.getNivel());
//                personal.setAutorizacionNivel(autorizacionnivelNew);
//            }
//            
//            if (departamentoidNew != null) {
//                departamentoidNew = em.getReference(departamentoidNew.getClass(), departamentoidNew.getId());
//                personal.setDepartamentoId(departamentoidNew);
//            }
//            
//            if (loginusuarioNew != null) {
//                loginusuarioNew = em.getReference(loginusuarioNew.getClass(), loginusuarioNew.getUsuario());
//                personal.setLoginUsuario(loginusuarioNew);
//            }

            personal = em.merge(personal);
            
//            if (autorizacionnivelOld != null && !autorizacionnivelOld.equals(autorizacionnivelNew)) {
//                autorizacionnivelOld.getPersonalList().remove(personal);
//                autorizacionnivelOld = em.merge(autorizacionnivelOld);
//            }
//            
//            if (autorizacionnivelNew != null && !autorizacionnivelNew.equals(autorizacionnivelOld)) {
//                autorizacionnivelNew.getPersonalList().add(personal);
//                autorizacionnivelNew = em.merge(autorizacionnivelNew);
//            }
//            
//            if (departamentoidOld != null && !departamentoidOld.equals(departamentoidNew)) {
//                departamentoidOld.getPersonalList().remove(personal);
//                departamentoidOld = em.merge(departamentoidOld);
//            }
//            
//            if (departamentoidNew != null && !departamentoidNew.equals(departamentoidOld)) {
//                departamentoidNew.getPersonalList().add(personal);
//                departamentoidNew = em.merge(departamentoidNew);
//            }
//            
//            if (loginusuarioOld != null && !loginusuarioOld.equals(loginusuarioNew)) {
//                loginusuarioOld.setPersonal(null);
//                loginusuarioOld = em.merge(loginusuarioOld);
//            }
//            
//            if (loginusuarioNew != null && !loginusuarioNew.equals(loginusuarioOld)) {
//                loginusuarioNew.setPersonal(personal);
//                loginusuarioNew = em.merge(loginusuarioNew);
//            }

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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<ExamenColposcopia> examenColposcopiaListOrphanCheck = personal.getExamenColposcopiaList();
            for (ExamenColposcopia examenColposcopiaListOrphanCheckExamenColposcopia : examenColposcopiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personal (" + personal + ") cannot be destroyed since the ExamenColposcopia " + examenColposcopiaListOrphanCheckExamenColposcopia + " in its examenColposcopiaList field has a non-nullable personalcedula field.");
            }
            List<ExamenOdontologia> examenOdontologiaListOrphanCheck = personal.getExamenOdontologiaList();
            for (ExamenOdontologia examenOdontologiaListOrphanCheckExamenOdontologia : examenOdontologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personal (" + personal + ") cannot be destroyed since the ExamenOdontologia " + examenOdontologiaListOrphanCheckExamenOdontologia + " in its examenOdontologiaList field has a non-nullable personalcedula field.");
            }
            List<ExamenGinecologia> examenGinecologiaListOrphanCheck = personal.getExamenGinecologiaList();
            for (ExamenGinecologia examenGinecologiaListOrphanCheckExamenGinecologia : examenGinecologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personal (" + personal + ") cannot be destroyed since the ExamenGinecologia " + examenGinecologiaListOrphanCheckExamenGinecologia + " in its examenGinecologiaList field has a non-nullable personalcedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Autorizacion autorizacionnivel = personal.getAutorizacionNivel();
            if (autorizacionnivel != null) {
                autorizacionnivel.getPersonalList().remove(personal);
                autorizacionnivel = em.merge(autorizacionnivel);
            }
            Departamento departamentoid = personal.getDepartamentoId();
            if (departamentoid != null) {
                departamentoid.getPersonalList().remove(personal);
                departamentoid = em.merge(departamentoid);
            }
            Login loginusuario = personal.getLoginUsuario();
            if (loginusuario != null) {
                loginusuario.setPersonal(null);
                loginusuario = em.merge(loginusuario);
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

    public Personal findPersonalByLoginUsuario(String loginUsuario) {
        EntityManager em = getEntityManager();
        Personal personal = new Personal();
        try {
            Query query = em.createQuery("select p from Personal p where p.loginUsuario.usuario = ?1");
            query.setParameter( 1, loginUsuario);
            personal = (Personal)query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        return personal;
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
