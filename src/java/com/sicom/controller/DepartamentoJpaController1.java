/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Personal;
import java.util.ArrayList;
import java.util.List;
import com.sicom.entities.Cita;
import com.sicom.entities.Consulta;
import com.sicom.entities.Departamento;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class DepartamentoJpaController1 implements Serializable {

    public DepartamentoJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) {
        if (departamento.getPersonalList() == null) {
            departamento.setPersonalList(new ArrayList<Personal>());
        }
        if (departamento.getCitaList() == null) {
            departamento.setCitaList(new ArrayList<Cita>());
        }
        if (departamento.getConsultaList() == null) {
            departamento.setConsultaList(new ArrayList<Consulta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personal> attachedPersonalList = new ArrayList<Personal>();
            for (Personal personalListPersonalToAttach : departamento.getPersonalList()) {
                personalListPersonalToAttach = em.getReference(personalListPersonalToAttach.getClass(), personalListPersonalToAttach.getId());
                attachedPersonalList.add(personalListPersonalToAttach);
            }
            departamento.setPersonalList(attachedPersonalList);
            List<Cita> attachedCitaList = new ArrayList<Cita>();
            for (Cita citaListCitaToAttach : departamento.getCitaList()) {
                citaListCitaToAttach = em.getReference(citaListCitaToAttach.getClass(), citaListCitaToAttach.getId());
                attachedCitaList.add(citaListCitaToAttach);
            }
            departamento.setCitaList(attachedCitaList);
            List<Consulta> attachedConsultaList = new ArrayList<Consulta>();
            for (Consulta consultaListConsultaToAttach : departamento.getConsultaList()) {
                consultaListConsultaToAttach = em.getReference(consultaListConsultaToAttach.getClass(), consultaListConsultaToAttach.getFecha());
                attachedConsultaList.add(consultaListConsultaToAttach);
            }
            departamento.setConsultaList(attachedConsultaList);
            em.persist(departamento);
            for (Personal personalListPersonal : departamento.getPersonalList()) {
                Departamento oldDepartamentoIdOfPersonalListPersonal = personalListPersonal.getDepartamentoId();
                personalListPersonal.setDepartamentoId(departamento);
                personalListPersonal = em.merge(personalListPersonal);
                if (oldDepartamentoIdOfPersonalListPersonal != null) {
                    oldDepartamentoIdOfPersonalListPersonal.getPersonalList().remove(personalListPersonal);
                    oldDepartamentoIdOfPersonalListPersonal = em.merge(oldDepartamentoIdOfPersonalListPersonal);
                }
            }
            for (Cita citaListCita : departamento.getCitaList()) {
                Departamento oldDepartamentoIdOfCitaListCita = citaListCita.getDepartamentoId();
                citaListCita.setDepartamentoId(departamento);
                citaListCita = em.merge(citaListCita);
                if (oldDepartamentoIdOfCitaListCita != null) {
                    oldDepartamentoIdOfCitaListCita.getCitaList().remove(citaListCita);
                    oldDepartamentoIdOfCitaListCita = em.merge(oldDepartamentoIdOfCitaListCita);
                }
            }
            for (Consulta consultaListConsulta : departamento.getConsultaList()) {
                Departamento oldDepartamentoIdOfConsultaListConsulta = consultaListConsulta.getDepartamentoId();
                consultaListConsulta.setDepartamentoId(departamento);
                consultaListConsulta = em.merge(consultaListConsulta);
                if (oldDepartamentoIdOfConsultaListConsulta != null) {
                    oldDepartamentoIdOfConsultaListConsulta.getConsultaList().remove(consultaListConsulta);
                    oldDepartamentoIdOfConsultaListConsulta = em.merge(oldDepartamentoIdOfConsultaListConsulta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getId());
            List<Personal> personalListOld = persistentDepartamento.getPersonalList();
            List<Personal> personalListNew = departamento.getPersonalList();
            List<Cita> citaListOld = persistentDepartamento.getCitaList();
            List<Cita> citaListNew = departamento.getCitaList();
            List<Consulta> consultaListOld = persistentDepartamento.getConsultaList();
            List<Consulta> consultaListNew = departamento.getConsultaList();
            List<String> illegalOrphanMessages = null;
            for (Personal personalListOldPersonal : personalListOld) {
                if (!personalListNew.contains(personalListOldPersonal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Personal " + personalListOldPersonal + " since its departamentoId field is not nullable.");
                }
            }
            for (Cita citaListOldCita : citaListOld) {
                if (!citaListNew.contains(citaListOldCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cita " + citaListOldCita + " since its departamentoId field is not nullable.");
                }
            }
            for (Consulta consultaListOldConsulta : consultaListOld) {
                if (!consultaListNew.contains(consultaListOldConsulta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Consulta " + consultaListOldConsulta + " since its departamentoId field is not nullable.");
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
            departamento.setPersonalList(personalListNew);
            List<Cita> attachedCitaListNew = new ArrayList<Cita>();
            for (Cita citaListNewCitaToAttach : citaListNew) {
                citaListNewCitaToAttach = em.getReference(citaListNewCitaToAttach.getClass(), citaListNewCitaToAttach.getId());
                attachedCitaListNew.add(citaListNewCitaToAttach);
            }
            citaListNew = attachedCitaListNew;
            departamento.setCitaList(citaListNew);
            List<Consulta> attachedConsultaListNew = new ArrayList<Consulta>();
            for (Consulta consultaListNewConsultaToAttach : consultaListNew) {
                consultaListNewConsultaToAttach = em.getReference(consultaListNewConsultaToAttach.getClass(), consultaListNewConsultaToAttach.getFecha());
                attachedConsultaListNew.add(consultaListNewConsultaToAttach);
            }
            consultaListNew = attachedConsultaListNew;
            departamento.setConsultaList(consultaListNew);
            departamento = em.merge(departamento);
            for (Personal personalListNewPersonal : personalListNew) {
                if (!personalListOld.contains(personalListNewPersonal)) {
                    Departamento oldDepartamentoIdOfPersonalListNewPersonal = personalListNewPersonal.getDepartamentoId();
                    personalListNewPersonal.setDepartamentoId(departamento);
                    personalListNewPersonal = em.merge(personalListNewPersonal);
                    if (oldDepartamentoIdOfPersonalListNewPersonal != null && !oldDepartamentoIdOfPersonalListNewPersonal.equals(departamento)) {
                        oldDepartamentoIdOfPersonalListNewPersonal.getPersonalList().remove(personalListNewPersonal);
                        oldDepartamentoIdOfPersonalListNewPersonal = em.merge(oldDepartamentoIdOfPersonalListNewPersonal);
                    }
                }
            }
            for (Cita citaListNewCita : citaListNew) {
                if (!citaListOld.contains(citaListNewCita)) {
                    Departamento oldDepartamentoIdOfCitaListNewCita = citaListNewCita.getDepartamentoId();
                    citaListNewCita.setDepartamentoId(departamento);
                    citaListNewCita = em.merge(citaListNewCita);
                    if (oldDepartamentoIdOfCitaListNewCita != null && !oldDepartamentoIdOfCitaListNewCita.equals(departamento)) {
                        oldDepartamentoIdOfCitaListNewCita.getCitaList().remove(citaListNewCita);
                        oldDepartamentoIdOfCitaListNewCita = em.merge(oldDepartamentoIdOfCitaListNewCita);
                    }
                }
            }
            for (Consulta consultaListNewConsulta : consultaListNew) {
                if (!consultaListOld.contains(consultaListNewConsulta)) {
                    Departamento oldDepartamentoIdOfConsultaListNewConsulta = consultaListNewConsulta.getDepartamentoId();
                    consultaListNewConsulta.setDepartamentoId(departamento);
                    consultaListNewConsulta = em.merge(consultaListNewConsulta);
                    if (oldDepartamentoIdOfConsultaListNewConsulta != null && !oldDepartamentoIdOfConsultaListNewConsulta.equals(departamento)) {
                        oldDepartamentoIdOfConsultaListNewConsulta.getConsultaList().remove(consultaListNewConsulta);
                        oldDepartamentoIdOfConsultaListNewConsulta = em.merge(oldDepartamentoIdOfConsultaListNewConsulta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamento.getId();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Personal> personalListOrphanCheck = departamento.getPersonalList();
            for (Personal personalListOrphanCheckPersonal : personalListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Personal " + personalListOrphanCheckPersonal + " in its personalList field has a non-nullable departamentoId field.");
            }
            List<Cita> citaListOrphanCheck = departamento.getCitaList();
            for (Cita citaListOrphanCheckCita : citaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Cita " + citaListOrphanCheckCita + " in its citaList field has a non-nullable departamentoId field.");
            }
            List<Consulta> consultaListOrphanCheck = departamento.getConsultaList();
            for (Consulta consultaListOrphanCheckConsulta : consultaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Consulta " + consultaListOrphanCheckConsulta + " in its consultaList field has a non-nullable departamentoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
