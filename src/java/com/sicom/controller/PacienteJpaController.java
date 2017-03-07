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
import com.sicom.entities.Responsable;
import java.util.ArrayList;
import java.util.List;
import com.sicom.entities.Cita;
import com.sicom.entities.Consulta;
import com.sicom.entities.Paciente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class PacienteJpaController implements Serializable {

    public PacienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paciente paciente) throws PreexistingEntityException, Exception {
        if (paciente.getResponsableList() == null) {
            paciente.setResponsableList(new ArrayList<Responsable>());
        }
        if (paciente.getCitaList() == null) {
            paciente.setCitaList(new ArrayList<Cita>());
        }
        if (paciente.getConsultaList() == null) {
            paciente.setConsultaList(new ArrayList<Consulta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Responsable> attachedResponsableList = new ArrayList<Responsable>();
            for (Responsable responsableListResponsableToAttach : paciente.getResponsableList()) {
                responsableListResponsableToAttach = em.getReference(responsableListResponsableToAttach.getClass(), responsableListResponsableToAttach.getId());
                attachedResponsableList.add(responsableListResponsableToAttach);
            }
            paciente.setResponsableList(attachedResponsableList);
            List<Cita> attachedCitaList = new ArrayList<Cita>();
            for (Cita citaListCitaToAttach : paciente.getCitaList()) {
                citaListCitaToAttach = em.getReference(citaListCitaToAttach.getClass(), citaListCitaToAttach.getId());
                attachedCitaList.add(citaListCitaToAttach);
            }
            paciente.setCitaList(attachedCitaList);
            List<Consulta> attachedConsultaList = new ArrayList<Consulta>();
            for (Consulta consultaListConsultaToAttach : paciente.getConsultaList()) {
                consultaListConsultaToAttach = em.getReference(consultaListConsultaToAttach.getClass(), consultaListConsultaToAttach.getFecha());
                attachedConsultaList.add(consultaListConsultaToAttach);
            }
            paciente.setConsultaList(attachedConsultaList);
            em.persist(paciente);
            for (Responsable responsableListResponsable : paciente.getResponsableList()) {
                Paciente oldPacienteidOfResponsableListResponsable = responsableListResponsable.getPacienteid();
                responsableListResponsable.setPacienteid(paciente);
                responsableListResponsable = em.merge(responsableListResponsable);
                if (oldPacienteidOfResponsableListResponsable != null) {
                    oldPacienteidOfResponsableListResponsable.getResponsableList().remove(responsableListResponsable);
                    oldPacienteidOfResponsableListResponsable = em.merge(oldPacienteidOfResponsableListResponsable);
                }
            }
            for (Cita citaListCita : paciente.getCitaList()) {
                Paciente oldPacienteidOfCitaListCita = citaListCita.getPacienteid();
                citaListCita.setPacienteid(paciente);
                citaListCita = em.merge(citaListCita);
                if (oldPacienteidOfCitaListCita != null) {
                    oldPacienteidOfCitaListCita.getCitaList().remove(citaListCita);
                    oldPacienteidOfCitaListCita = em.merge(oldPacienteidOfCitaListCita);
                }
            }
            for (Consulta consultaListConsulta : paciente.getConsultaList()) {
                Paciente oldPacienteidOfConsultaListConsulta = consultaListConsulta.getPacienteid();
                consultaListConsulta.setPacienteid(paciente);
                consultaListConsulta = em.merge(consultaListConsulta);
                if (oldPacienteidOfConsultaListConsulta != null) {
                    oldPacienteidOfConsultaListConsulta.getConsultaList().remove(consultaListConsulta);
                    oldPacienteidOfConsultaListConsulta = em.merge(oldPacienteidOfConsultaListConsulta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPaciente(paciente.getId()) != null) {
                throw new PreexistingEntityException("Paciente " + paciente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paciente paciente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paciente persistentPaciente = em.find(Paciente.class, paciente.getId());
            List<Responsable> responsableListOld = persistentPaciente.getResponsableList();
            List<Responsable> responsableListNew = paciente.getResponsableList();
            List<Cita> citaListOld = persistentPaciente.getCitaList();
            List<Cita> citaListNew = paciente.getCitaList();
            List<Consulta> consultaListOld = persistentPaciente.getConsultaList();
            List<Consulta> consultaListNew = paciente.getConsultaList();
            List<String> illegalOrphanMessages = null;
            for (Responsable responsableListOldResponsable : responsableListOld) {
                if (!responsableListNew.contains(responsableListOldResponsable)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Responsable " + responsableListOldResponsable + " since its pacienteid field is not nullable.");
                }
            }
            for (Cita citaListOldCita : citaListOld) {
                if (!citaListNew.contains(citaListOldCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cita " + citaListOldCita + " since its pacienteid field is not nullable.");
                }
            }
            for (Consulta consultaListOldConsulta : consultaListOld) {
                if (!consultaListNew.contains(consultaListOldConsulta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Consulta " + consultaListOldConsulta + " since its pacienteid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Responsable> attachedResponsableListNew = new ArrayList<Responsable>();
            for (Responsable responsableListNewResponsableToAttach : responsableListNew) {
                responsableListNewResponsableToAttach = em.getReference(responsableListNewResponsableToAttach.getClass(), responsableListNewResponsableToAttach.getId());
                attachedResponsableListNew.add(responsableListNewResponsableToAttach);
            }
            responsableListNew = attachedResponsableListNew;
            paciente.setResponsableList(responsableListNew);
            List<Cita> attachedCitaListNew = new ArrayList<Cita>();
            for (Cita citaListNewCitaToAttach : citaListNew) {
                citaListNewCitaToAttach = em.getReference(citaListNewCitaToAttach.getClass(), citaListNewCitaToAttach.getId());
                attachedCitaListNew.add(citaListNewCitaToAttach);
            }
            citaListNew = attachedCitaListNew;
            paciente.setCitaList(citaListNew);
            List<Consulta> attachedConsultaListNew = new ArrayList<Consulta>();
            for (Consulta consultaListNewConsultaToAttach : consultaListNew) {
                consultaListNewConsultaToAttach = em.getReference(consultaListNewConsultaToAttach.getClass(), consultaListNewConsultaToAttach.getFecha());
                attachedConsultaListNew.add(consultaListNewConsultaToAttach);
            }
            consultaListNew = attachedConsultaListNew;
            paciente.setConsultaList(consultaListNew);
            paciente = em.merge(paciente);
            for (Responsable responsableListNewResponsable : responsableListNew) {
                if (!responsableListOld.contains(responsableListNewResponsable)) {
                    Paciente oldPacienteidOfResponsableListNewResponsable = responsableListNewResponsable.getPacienteid();
                    responsableListNewResponsable.setPacienteid(paciente);
                    responsableListNewResponsable = em.merge(responsableListNewResponsable);
                    if (oldPacienteidOfResponsableListNewResponsable != null && !oldPacienteidOfResponsableListNewResponsable.equals(paciente)) {
                        oldPacienteidOfResponsableListNewResponsable.getResponsableList().remove(responsableListNewResponsable);
                        oldPacienteidOfResponsableListNewResponsable = em.merge(oldPacienteidOfResponsableListNewResponsable);
                    }
                }
            }
            for (Cita citaListNewCita : citaListNew) {
                if (!citaListOld.contains(citaListNewCita)) {
                    Paciente oldPacienteidOfCitaListNewCita = citaListNewCita.getPacienteid();
                    citaListNewCita.setPacienteid(paciente);
                    citaListNewCita = em.merge(citaListNewCita);
                    if (oldPacienteidOfCitaListNewCita != null && !oldPacienteidOfCitaListNewCita.equals(paciente)) {
                        oldPacienteidOfCitaListNewCita.getCitaList().remove(citaListNewCita);
                        oldPacienteidOfCitaListNewCita = em.merge(oldPacienteidOfCitaListNewCita);
                    }
                }
            }
            for (Consulta consultaListNewConsulta : consultaListNew) {
                if (!consultaListOld.contains(consultaListNewConsulta)) {
                    Paciente oldPacienteidOfConsultaListNewConsulta = consultaListNewConsulta.getPacienteid();
                    consultaListNewConsulta.setPacienteid(paciente);
                    consultaListNewConsulta = em.merge(consultaListNewConsulta);
                    if (oldPacienteidOfConsultaListNewConsulta != null && !oldPacienteidOfConsultaListNewConsulta.equals(paciente)) {
                        oldPacienteidOfConsultaListNewConsulta.getConsultaList().remove(consultaListNewConsulta);
                        oldPacienteidOfConsultaListNewConsulta = em.merge(oldPacienteidOfConsultaListNewConsulta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = paciente.getId();
                if (findPaciente(id) == null) {
                    throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.");
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
            Paciente paciente;
            try {
                paciente = em.getReference(Paciente.class, id);
                paciente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Responsable> responsableListOrphanCheck = paciente.getResponsableList();
            for (Responsable responsableListOrphanCheckResponsable : responsableListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Responsable " + responsableListOrphanCheckResponsable + " in its responsableList field has a non-nullable pacienteid field.");
            }
            List<Cita> citaListOrphanCheck = paciente.getCitaList();
            for (Cita citaListOrphanCheckCita : citaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Cita " + citaListOrphanCheckCita + " in its citaList field has a non-nullable pacienteid field.");
            }
            List<Consulta> consultaListOrphanCheck = paciente.getConsultaList();
            for (Consulta consultaListOrphanCheckConsulta : consultaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Consulta " + consultaListOrphanCheckConsulta + " in its consultaList field has a non-nullable pacienteid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(paciente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paciente> findPacienteEntities() {
        return findPacienteEntities(true, -1, -1);
    }

    public List<Paciente> findPacienteEntities(int maxResults, int firstResult) {
        return findPacienteEntities(false, maxResults, firstResult);
    }

    private List<Paciente> findPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paciente.class));
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

    public Paciente findPaciente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paciente> rt = cq.from(Paciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
