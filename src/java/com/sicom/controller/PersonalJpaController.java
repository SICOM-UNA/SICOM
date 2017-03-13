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
        Login loginUsuarioOrphanCheck = personal.getLoginUsuario();
        if (loginUsuarioOrphanCheck != null) {
            Personal oldPersonalOfLoginUsuario = loginUsuarioOrphanCheck.getPersonal();
            if (oldPersonalOfLoginUsuario != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Login " + loginUsuarioOrphanCheck + " already has an item of type Personal whose loginUsuario column cannot be null. Please make another selection for the loginUsuario field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autorizacion autorizacionNivel = personal.getAutorizacionNivel();
            if (autorizacionNivel != null) {
                autorizacionNivel = em.getReference(autorizacionNivel.getClass(), autorizacionNivel.getNivel());
                personal.setAutorizacionNivel(autorizacionNivel);
            }
            Departamento departamentoId = personal.getDepartamentoId();
            if (departamentoId != null) {
                departamentoId = em.getReference(departamentoId.getClass(), departamentoId.getId());
                personal.setDepartamentoId(departamentoId);
            }
            Login loginUsuario = personal.getLoginUsuario();
            if (loginUsuario != null) {
                loginUsuario = em.getReference(loginUsuario.getClass(), loginUsuario.getUsuario());
                personal.setLoginUsuario(loginUsuario);
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
            if (autorizacionNivel != null) {
                autorizacionNivel.getPersonalList().add(personal);
                autorizacionNivel = em.merge(autorizacionNivel);
            }
            if (departamentoId != null) {
                departamentoId.getPersonalList().add(personal);
                departamentoId = em.merge(departamentoId);
            }
            if (loginUsuario != null) {
                loginUsuario.setPersonal(personal);
                loginUsuario = em.merge(loginUsuario);
            }
            for (ExamenColposcopia examenColposcopiaListExamenColposcopia : personal.getExamenColposcopiaList()) {
                Personal oldPersonalCedulaOfExamenColposcopiaListExamenColposcopia = examenColposcopiaListExamenColposcopia.getPersonalCedula();
                examenColposcopiaListExamenColposcopia.setPersonalCedula(personal);
                examenColposcopiaListExamenColposcopia = em.merge(examenColposcopiaListExamenColposcopia);
                if (oldPersonalCedulaOfExamenColposcopiaListExamenColposcopia != null) {
                    oldPersonalCedulaOfExamenColposcopiaListExamenColposcopia.getExamenColposcopiaList().remove(examenColposcopiaListExamenColposcopia);
                    oldPersonalCedulaOfExamenColposcopiaListExamenColposcopia = em.merge(oldPersonalCedulaOfExamenColposcopiaListExamenColposcopia);
                }
            }
            for (ExamenOdontologia examenOdontologiaListExamenOdontologia : personal.getExamenOdontologiaList()) {
                Personal oldPersonalCedulaOfExamenOdontologiaListExamenOdontologia = examenOdontologiaListExamenOdontologia.getPersonalCedula();
                examenOdontologiaListExamenOdontologia.setPersonalCedula(personal);
                examenOdontologiaListExamenOdontologia = em.merge(examenOdontologiaListExamenOdontologia);
                if (oldPersonalCedulaOfExamenOdontologiaListExamenOdontologia != null) {
                    oldPersonalCedulaOfExamenOdontologiaListExamenOdontologia.getExamenOdontologiaList().remove(examenOdontologiaListExamenOdontologia);
                    oldPersonalCedulaOfExamenOdontologiaListExamenOdontologia = em.merge(oldPersonalCedulaOfExamenOdontologiaListExamenOdontologia);
                }
            }
            for (ExamenGinecologia examenGinecologiaListExamenGinecologia : personal.getExamenGinecologiaList()) {
                Personal oldPersonalCedulaOfExamenGinecologiaListExamenGinecologia = examenGinecologiaListExamenGinecologia.getPersonalCedula();
                examenGinecologiaListExamenGinecologia.setPersonalCedula(personal);
                examenGinecologiaListExamenGinecologia = em.merge(examenGinecologiaListExamenGinecologia);
                if (oldPersonalCedulaOfExamenGinecologiaListExamenGinecologia != null) {
                    oldPersonalCedulaOfExamenGinecologiaListExamenGinecologia.getExamenGinecologiaList().remove(examenGinecologiaListExamenGinecologia);
                    oldPersonalCedulaOfExamenGinecologiaListExamenGinecologia = em.merge(oldPersonalCedulaOfExamenGinecologiaListExamenGinecologia);
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
            Autorizacion autorizacionNivelOld = persistentPersonal.getAutorizacionNivel();
            Autorizacion autorizacionNivelNew = personal.getAutorizacionNivel();
            Departamento departamentoIdOld = persistentPersonal.getDepartamentoId();
            Departamento departamentoIdNew = personal.getDepartamentoId();
            Login loginUsuarioOld = persistentPersonal.getLoginUsuario();
            Login loginUsuarioNew = personal.getLoginUsuario();
            List<ExamenColposcopia> examenColposcopiaListOld = persistentPersonal.getExamenColposcopiaList();
            List<ExamenColposcopia> examenColposcopiaListNew = personal.getExamenColposcopiaList();
            List<ExamenOdontologia> examenOdontologiaListOld = persistentPersonal.getExamenOdontologiaList();
            List<ExamenOdontologia> examenOdontologiaListNew = personal.getExamenOdontologiaList();
            List<ExamenGinecologia> examenGinecologiaListOld = persistentPersonal.getExamenGinecologiaList();
            List<ExamenGinecologia> examenGinecologiaListNew = personal.getExamenGinecologiaList();
            List<String> illegalOrphanMessages = null;
            if (loginUsuarioNew != null && !loginUsuarioNew.equals(loginUsuarioOld)) {
                Personal oldPersonalOfLoginUsuario = loginUsuarioNew.getPersonal();
                if (oldPersonalOfLoginUsuario != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Login " + loginUsuarioNew + " already has an item of type Personal whose loginUsuario column cannot be null. Please make another selection for the loginUsuario field.");
                }
            }
            for (ExamenColposcopia examenColposcopiaListOldExamenColposcopia : examenColposcopiaListOld) {
                if (!examenColposcopiaListNew.contains(examenColposcopiaListOldExamenColposcopia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenColposcopia " + examenColposcopiaListOldExamenColposcopia + " since its personalCedula field is not nullable.");
                }
            }
            for (ExamenOdontologia examenOdontologiaListOldExamenOdontologia : examenOdontologiaListOld) {
                if (!examenOdontologiaListNew.contains(examenOdontologiaListOldExamenOdontologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenOdontologia " + examenOdontologiaListOldExamenOdontologia + " since its personalCedula field is not nullable.");
                }
            }
            for (ExamenGinecologia examenGinecologiaListOldExamenGinecologia : examenGinecologiaListOld) {
                if (!examenGinecologiaListNew.contains(examenGinecologiaListOldExamenGinecologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenGinecologia " + examenGinecologiaListOldExamenGinecologia + " since its personalCedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (autorizacionNivelNew != null) {
                autorizacionNivelNew = em.getReference(autorizacionNivelNew.getClass(), autorizacionNivelNew.getNivel());
                personal.setAutorizacionNivel(autorizacionNivelNew);
            }
            if (departamentoIdNew != null) {
                departamentoIdNew = em.getReference(departamentoIdNew.getClass(), departamentoIdNew.getId());
                personal.setDepartamentoId(departamentoIdNew);
            }
            if (loginUsuarioNew != null) {
                loginUsuarioNew = em.getReference(loginUsuarioNew.getClass(), loginUsuarioNew.getUsuario());
                personal.setLoginUsuario(loginUsuarioNew);
            }
            List<ExamenColposcopia> attachedExamenColposcopiaListNew = new ArrayList<ExamenColposcopia>();
            for (ExamenColposcopia examenColposcopiaListNewExamenColposcopiaToAttach : examenColposcopiaListNew) {
                examenColposcopiaListNewExamenColposcopiaToAttach = em.getReference(examenColposcopiaListNewExamenColposcopiaToAttach.getClass(), examenColposcopiaListNewExamenColposcopiaToAttach.getId());
                attachedExamenColposcopiaListNew.add(examenColposcopiaListNewExamenColposcopiaToAttach);
            }
            examenColposcopiaListNew = attachedExamenColposcopiaListNew;
            personal.setExamenColposcopiaList(examenColposcopiaListNew);
            List<ExamenOdontologia> attachedExamenOdontologiaListNew = new ArrayList<ExamenOdontologia>();
            for (ExamenOdontologia examenOdontologiaListNewExamenOdontologiaToAttach : examenOdontologiaListNew) {
                examenOdontologiaListNewExamenOdontologiaToAttach = em.getReference(examenOdontologiaListNewExamenOdontologiaToAttach.getClass(), examenOdontologiaListNewExamenOdontologiaToAttach.getId());
                attachedExamenOdontologiaListNew.add(examenOdontologiaListNewExamenOdontologiaToAttach);
            }
            examenOdontologiaListNew = attachedExamenOdontologiaListNew;
            personal.setExamenOdontologiaList(examenOdontologiaListNew);
            List<ExamenGinecologia> attachedExamenGinecologiaListNew = new ArrayList<ExamenGinecologia>();
            for (ExamenGinecologia examenGinecologiaListNewExamenGinecologiaToAttach : examenGinecologiaListNew) {
                examenGinecologiaListNewExamenGinecologiaToAttach = em.getReference(examenGinecologiaListNewExamenGinecologiaToAttach.getClass(), examenGinecologiaListNewExamenGinecologiaToAttach.getId());
                attachedExamenGinecologiaListNew.add(examenGinecologiaListNewExamenGinecologiaToAttach);
            }
            examenGinecologiaListNew = attachedExamenGinecologiaListNew;
            personal.setExamenGinecologiaList(examenGinecologiaListNew);
            personal = em.merge(personal);
            if (autorizacionNivelOld != null && !autorizacionNivelOld.equals(autorizacionNivelNew)) {
                autorizacionNivelOld.getPersonalList().remove(personal);
                autorizacionNivelOld = em.merge(autorizacionNivelOld);
            }
            if (autorizacionNivelNew != null && !autorizacionNivelNew.equals(autorizacionNivelOld)) {
                autorizacionNivelNew.getPersonalList().add(personal);
                autorizacionNivelNew = em.merge(autorizacionNivelNew);
            }
            if (departamentoIdOld != null && !departamentoIdOld.equals(departamentoIdNew)) {
                departamentoIdOld.getPersonalList().remove(personal);
                departamentoIdOld = em.merge(departamentoIdOld);
            }
            if (departamentoIdNew != null && !departamentoIdNew.equals(departamentoIdOld)) {
                departamentoIdNew.getPersonalList().add(personal);
                departamentoIdNew = em.merge(departamentoIdNew);
            }
            if (loginUsuarioOld != null && !loginUsuarioOld.equals(loginUsuarioNew)) {
                loginUsuarioOld.setPersonal(null);
                loginUsuarioOld = em.merge(loginUsuarioOld);
            }
            if (loginUsuarioNew != null && !loginUsuarioNew.equals(loginUsuarioOld)) {
                loginUsuarioNew.setPersonal(personal);
                loginUsuarioNew = em.merge(loginUsuarioNew);
            }
            for (ExamenColposcopia examenColposcopiaListNewExamenColposcopia : examenColposcopiaListNew) {
                if (!examenColposcopiaListOld.contains(examenColposcopiaListNewExamenColposcopia)) {
                    Personal oldPersonalCedulaOfExamenColposcopiaListNewExamenColposcopia = examenColposcopiaListNewExamenColposcopia.getPersonalCedula();
                    examenColposcopiaListNewExamenColposcopia.setPersonalCedula(personal);
                    examenColposcopiaListNewExamenColposcopia = em.merge(examenColposcopiaListNewExamenColposcopia);
                    if (oldPersonalCedulaOfExamenColposcopiaListNewExamenColposcopia != null && !oldPersonalCedulaOfExamenColposcopiaListNewExamenColposcopia.equals(personal)) {
                        oldPersonalCedulaOfExamenColposcopiaListNewExamenColposcopia.getExamenColposcopiaList().remove(examenColposcopiaListNewExamenColposcopia);
                        oldPersonalCedulaOfExamenColposcopiaListNewExamenColposcopia = em.merge(oldPersonalCedulaOfExamenColposcopiaListNewExamenColposcopia);
                    }
                }
            }
            for (ExamenOdontologia examenOdontologiaListNewExamenOdontologia : examenOdontologiaListNew) {
                if (!examenOdontologiaListOld.contains(examenOdontologiaListNewExamenOdontologia)) {
                    Personal oldPersonalCedulaOfExamenOdontologiaListNewExamenOdontologia = examenOdontologiaListNewExamenOdontologia.getPersonalCedula();
                    examenOdontologiaListNewExamenOdontologia.setPersonalCedula(personal);
                    examenOdontologiaListNewExamenOdontologia = em.merge(examenOdontologiaListNewExamenOdontologia);
                    if (oldPersonalCedulaOfExamenOdontologiaListNewExamenOdontologia != null && !oldPersonalCedulaOfExamenOdontologiaListNewExamenOdontologia.equals(personal)) {
                        oldPersonalCedulaOfExamenOdontologiaListNewExamenOdontologia.getExamenOdontologiaList().remove(examenOdontologiaListNewExamenOdontologia);
                        oldPersonalCedulaOfExamenOdontologiaListNewExamenOdontologia = em.merge(oldPersonalCedulaOfExamenOdontologiaListNewExamenOdontologia);
                    }
                }
            }
            for (ExamenGinecologia examenGinecologiaListNewExamenGinecologia : examenGinecologiaListNew) {
                if (!examenGinecologiaListOld.contains(examenGinecologiaListNewExamenGinecologia)) {
                    Personal oldPersonalCedulaOfExamenGinecologiaListNewExamenGinecologia = examenGinecologiaListNewExamenGinecologia.getPersonalCedula();
                    examenGinecologiaListNewExamenGinecologia.setPersonalCedula(personal);
                    examenGinecologiaListNewExamenGinecologia = em.merge(examenGinecologiaListNewExamenGinecologia);
                    if (oldPersonalCedulaOfExamenGinecologiaListNewExamenGinecologia != null && !oldPersonalCedulaOfExamenGinecologiaListNewExamenGinecologia.equals(personal)) {
                        oldPersonalCedulaOfExamenGinecologiaListNewExamenGinecologia.getExamenGinecologiaList().remove(examenGinecologiaListNewExamenGinecologia);
                        oldPersonalCedulaOfExamenGinecologiaListNewExamenGinecologia = em.merge(oldPersonalCedulaOfExamenGinecologiaListNewExamenGinecologia);
                    }
                }
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
                illegalOrphanMessages.add("This Personal (" + personal + ") cannot be destroyed since the ExamenColposcopia " + examenColposcopiaListOrphanCheckExamenColposcopia + " in its examenColposcopiaList field has a non-nullable personalCedula field.");
            }
            List<ExamenOdontologia> examenOdontologiaListOrphanCheck = personal.getExamenOdontologiaList();
            for (ExamenOdontologia examenOdontologiaListOrphanCheckExamenOdontologia : examenOdontologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personal (" + personal + ") cannot be destroyed since the ExamenOdontologia " + examenOdontologiaListOrphanCheckExamenOdontologia + " in its examenOdontologiaList field has a non-nullable personalCedula field.");
            }
            List<ExamenGinecologia> examenGinecologiaListOrphanCheck = personal.getExamenGinecologiaList();
            for (ExamenGinecologia examenGinecologiaListOrphanCheckExamenGinecologia : examenGinecologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personal (" + personal + ") cannot be destroyed since the ExamenGinecologia " + examenGinecologiaListOrphanCheckExamenGinecologia + " in its examenGinecologiaList field has a non-nullable personalCedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Autorizacion autorizacionNivel = personal.getAutorizacionNivel();
            if (autorizacionNivel != null) {
                autorizacionNivel.getPersonalList().remove(personal);
                autorizacionNivel = em.merge(autorizacionNivel);
            }
            Departamento departamentoId = personal.getDepartamentoId();
            if (departamentoId != null) {
                departamentoId.getPersonalList().remove(personal);
                departamentoId = em.merge(departamentoId);
            }
            Login loginUsuario = personal.getLoginUsuario();
            if (loginUsuario != null) {
                loginUsuario.setPersonal(null);
                loginUsuario = em.merge(loginUsuario);
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
