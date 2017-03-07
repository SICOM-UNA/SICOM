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
import com.sicom.entities.Cita;
import java.util.ArrayList;
import java.util.List;
import com.sicom.entities.Consulta;
import com.sicom.entities.Personal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class PersonalJpaController1 implements Serializable {

    public PersonalJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personal personal) throws PreexistingEntityException, Exception {
        if (personal.getCitaList() == null) {
            personal.setCitaList(new ArrayList<Cita>());
        }
        if (personal.getConsultaList() == null) {
            personal.setConsultaList(new ArrayList<Consulta>());
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
            List<Cita> attachedCitaList = new ArrayList<Cita>();
            for (Cita citaListCitaToAttach : personal.getCitaList()) {
                citaListCitaToAttach = em.getReference(citaListCitaToAttach.getClass(), citaListCitaToAttach.getId());
                attachedCitaList.add(citaListCitaToAttach);
            }
            personal.setCitaList(attachedCitaList);
            List<Consulta> attachedConsultaList = new ArrayList<Consulta>();
            for (Consulta consultaListConsultaToAttach : personal.getConsultaList()) {
                consultaListConsultaToAttach = em.getReference(consultaListConsultaToAttach.getClass(), consultaListConsultaToAttach.getFecha());
                attachedConsultaList.add(consultaListConsultaToAttach);
            }
            personal.setConsultaList(attachedConsultaList);
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
                loginUsuario.getPersonalList().add(personal);
                loginUsuario = em.merge(loginUsuario);
            }
            for (Cita citaListCita : personal.getCitaList()) {
                Personal oldPersonalidOfCitaListCita = citaListCita.getPersonalid();
                citaListCita.setPersonalid(personal);
                citaListCita = em.merge(citaListCita);
                if (oldPersonalidOfCitaListCita != null) {
                    oldPersonalidOfCitaListCita.getCitaList().remove(citaListCita);
                    oldPersonalidOfCitaListCita = em.merge(oldPersonalidOfCitaListCita);
                }
            }
            for (Consulta consultaListConsulta : personal.getConsultaList()) {
                Personal oldPersonalIdOfConsultaListConsulta = consultaListConsulta.getPersonalId();
                consultaListConsulta.setPersonalId(personal);
                consultaListConsulta = em.merge(consultaListConsulta);
                if (oldPersonalIdOfConsultaListConsulta != null) {
                    oldPersonalIdOfConsultaListConsulta.getConsultaList().remove(consultaListConsulta);
                    oldPersonalIdOfConsultaListConsulta = em.merge(oldPersonalIdOfConsultaListConsulta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonal(personal.getId()) != null) {
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
            Personal persistentPersonal = em.find(Personal.class, personal.getId());
            Autorizacion autorizacionNivelOld = persistentPersonal.getAutorizacionNivel();
            Autorizacion autorizacionNivelNew = personal.getAutorizacionNivel();
            Departamento departamentoIdOld = persistentPersonal.getDepartamentoId();
            Departamento departamentoIdNew = personal.getDepartamentoId();
            Login loginUsuarioOld = persistentPersonal.getLoginUsuario();
            Login loginUsuarioNew = personal.getLoginUsuario();
            List<Cita> citaListOld = persistentPersonal.getCitaList();
            List<Cita> citaListNew = personal.getCitaList();
            List<Consulta> consultaListOld = persistentPersonal.getConsultaList();
            List<Consulta> consultaListNew = personal.getConsultaList();
            List<String> illegalOrphanMessages = null;
            for (Cita citaListOldCita : citaListOld) {
                if (!citaListNew.contains(citaListOldCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cita " + citaListOldCita + " since its personalid field is not nullable.");
                }
            }
            for (Consulta consultaListOldConsulta : consultaListOld) {
                if (!consultaListNew.contains(consultaListOldConsulta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Consulta " + consultaListOldConsulta + " since its personalId field is not nullable.");
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
            List<Cita> attachedCitaListNew = new ArrayList<Cita>();
            for (Cita citaListNewCitaToAttach : citaListNew) {
                citaListNewCitaToAttach = em.getReference(citaListNewCitaToAttach.getClass(), citaListNewCitaToAttach.getId());
                attachedCitaListNew.add(citaListNewCitaToAttach);
            }
            citaListNew = attachedCitaListNew;
            personal.setCitaList(citaListNew);
            List<Consulta> attachedConsultaListNew = new ArrayList<Consulta>();
            for (Consulta consultaListNewConsultaToAttach : consultaListNew) {
                consultaListNewConsultaToAttach = em.getReference(consultaListNewConsultaToAttach.getClass(), consultaListNewConsultaToAttach.getFecha());
                attachedConsultaListNew.add(consultaListNewConsultaToAttach);
            }
            consultaListNew = attachedConsultaListNew;
            personal.setConsultaList(consultaListNew);
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
                loginUsuarioOld.getPersonalList().remove(personal);
                loginUsuarioOld = em.merge(loginUsuarioOld);
            }
            if (loginUsuarioNew != null && !loginUsuarioNew.equals(loginUsuarioOld)) {
                loginUsuarioNew.getPersonalList().add(personal);
                loginUsuarioNew = em.merge(loginUsuarioNew);
            }
            for (Cita citaListNewCita : citaListNew) {
                if (!citaListOld.contains(citaListNewCita)) {
                    Personal oldPersonalidOfCitaListNewCita = citaListNewCita.getPersonalid();
                    citaListNewCita.setPersonalid(personal);
                    citaListNewCita = em.merge(citaListNewCita);
                    if (oldPersonalidOfCitaListNewCita != null && !oldPersonalidOfCitaListNewCita.equals(personal)) {
                        oldPersonalidOfCitaListNewCita.getCitaList().remove(citaListNewCita);
                        oldPersonalidOfCitaListNewCita = em.merge(oldPersonalidOfCitaListNewCita);
                    }
                }
            }
            for (Consulta consultaListNewConsulta : consultaListNew) {
                if (!consultaListOld.contains(consultaListNewConsulta)) {
                    Personal oldPersonalIdOfConsultaListNewConsulta = consultaListNewConsulta.getPersonalId();
                    consultaListNewConsulta.setPersonalId(personal);
                    consultaListNewConsulta = em.merge(consultaListNewConsulta);
                    if (oldPersonalIdOfConsultaListNewConsulta != null && !oldPersonalIdOfConsultaListNewConsulta.equals(personal)) {
                        oldPersonalIdOfConsultaListNewConsulta.getConsultaList().remove(consultaListNewConsulta);
                        oldPersonalIdOfConsultaListNewConsulta = em.merge(oldPersonalIdOfConsultaListNewConsulta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = personal.getId();
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
                personal.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personal with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cita> citaListOrphanCheck = personal.getCitaList();
            for (Cita citaListOrphanCheckCita : citaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personal (" + personal + ") cannot be destroyed since the Cita " + citaListOrphanCheckCita + " in its citaList field has a non-nullable personalid field.");
            }
            List<Consulta> consultaListOrphanCheck = personal.getConsultaList();
            for (Consulta consultaListOrphanCheckConsulta : consultaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personal (" + personal + ") cannot be destroyed since the Consulta " + consultaListOrphanCheckConsulta + " in its consultaList field has a non-nullable personalId field.");
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
                loginUsuario.getPersonalList().remove(personal);
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
