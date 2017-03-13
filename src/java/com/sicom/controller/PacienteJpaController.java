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
import com.sicom.entities.Expediente;
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
        if (paciente.getExpedienteList() == null) {
            paciente.setExpedienteList(new ArrayList<Expediente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Responsable> attachedResponsableList = new ArrayList<Responsable>();
            for (Responsable responsableListResponsableToAttach : paciente.getResponsableList()) {
                responsableListResponsableToAttach = em.getReference(responsableListResponsableToAttach.getClass(), responsableListResponsableToAttach.getCedula());
                attachedResponsableList.add(responsableListResponsableToAttach);
            }
            paciente.setResponsableList(attachedResponsableList);
            List<Expediente> attachedExpedienteList = new ArrayList<Expediente>();
            for (Expediente expedienteListExpedienteToAttach : paciente.getExpedienteList()) {
                expedienteListExpedienteToAttach = em.getReference(expedienteListExpedienteToAttach.getClass(), expedienteListExpedienteToAttach.getId());
                attachedExpedienteList.add(expedienteListExpedienteToAttach);
            }
            paciente.setExpedienteList(attachedExpedienteList);
            em.persist(paciente);
            for (Responsable responsableListResponsable : paciente.getResponsableList()) {
                Paciente oldPacientecedulaOfResponsableListResponsable = responsableListResponsable.getPacientecedula();
                responsableListResponsable.setPacientecedula(paciente);
                responsableListResponsable = em.merge(responsableListResponsable);
                if (oldPacientecedulaOfResponsableListResponsable != null) {
                    oldPacientecedulaOfResponsableListResponsable.getResponsableList().remove(responsableListResponsable);
                    oldPacientecedulaOfResponsableListResponsable = em.merge(oldPacientecedulaOfResponsableListResponsable);
                }
            }
            for (Expediente expedienteListExpediente : paciente.getExpedienteList()) {
                Paciente oldPacientecedulaOfExpedienteListExpediente = expedienteListExpediente.getPacientecedula();
                expedienteListExpediente.setPacientecedula(paciente);
                expedienteListExpediente = em.merge(expedienteListExpediente);
                if (oldPacientecedulaOfExpedienteListExpediente != null) {
                    oldPacientecedulaOfExpedienteListExpediente.getExpedienteList().remove(expedienteListExpediente);
                    oldPacientecedulaOfExpedienteListExpediente = em.merge(oldPacientecedulaOfExpedienteListExpediente);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPaciente(paciente.getCedula()) != null) {
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
            Paciente persistentPaciente = em.find(Paciente.class, paciente.getCedula());
            List<Responsable> responsableListOld = persistentPaciente.getResponsableList();
            List<Responsable> responsableListNew = paciente.getResponsableList();
            List<Expediente> expedienteListOld = persistentPaciente.getExpedienteList();
            List<Expediente> expedienteListNew = paciente.getExpedienteList();
            List<String> illegalOrphanMessages = null;
            for (Responsable responsableListOldResponsable : responsableListOld) {
                if (!responsableListNew.contains(responsableListOldResponsable)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Responsable " + responsableListOldResponsable + " since its pacientecedula field is not nullable.");
                }
            }
            for (Expediente expedienteListOldExpediente : expedienteListOld) {
                if (!expedienteListNew.contains(expedienteListOldExpediente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Expediente " + expedienteListOldExpediente + " since its pacientecedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Responsable> attachedResponsableListNew = new ArrayList<Responsable>();
            for (Responsable responsableListNewResponsableToAttach : responsableListNew) {
                responsableListNewResponsableToAttach = em.getReference(responsableListNewResponsableToAttach.getClass(), responsableListNewResponsableToAttach.getCedula());
                attachedResponsableListNew.add(responsableListNewResponsableToAttach);
            }
            responsableListNew = attachedResponsableListNew;
            paciente.setResponsableList(responsableListNew);
            List<Expediente> attachedExpedienteListNew = new ArrayList<Expediente>();
            for (Expediente expedienteListNewExpedienteToAttach : expedienteListNew) {
                expedienteListNewExpedienteToAttach = em.getReference(expedienteListNewExpedienteToAttach.getClass(), expedienteListNewExpedienteToAttach.getId());
                attachedExpedienteListNew.add(expedienteListNewExpedienteToAttach);
            }
            expedienteListNew = attachedExpedienteListNew;
            paciente.setExpedienteList(expedienteListNew);
            paciente = em.merge(paciente);
            for (Responsable responsableListNewResponsable : responsableListNew) {
                if (!responsableListOld.contains(responsableListNewResponsable)) {
                    Paciente oldPacientecedulaOfResponsableListNewResponsable = responsableListNewResponsable.getPacientecedula();
                    responsableListNewResponsable.setPacientecedula(paciente);
                    responsableListNewResponsable = em.merge(responsableListNewResponsable);
                    if (oldPacientecedulaOfResponsableListNewResponsable != null && !oldPacientecedulaOfResponsableListNewResponsable.equals(paciente)) {
                        oldPacientecedulaOfResponsableListNewResponsable.getResponsableList().remove(responsableListNewResponsable);
                        oldPacientecedulaOfResponsableListNewResponsable = em.merge(oldPacientecedulaOfResponsableListNewResponsable);
                    }
                }
            }
            for (Expediente expedienteListNewExpediente : expedienteListNew) {
                if (!expedienteListOld.contains(expedienteListNewExpediente)) {
                    Paciente oldPacientecedulaOfExpedienteListNewExpediente = expedienteListNewExpediente.getPacientecedula();
                    expedienteListNewExpediente.setPacientecedula(paciente);
                    expedienteListNewExpediente = em.merge(expedienteListNewExpediente);
                    if (oldPacientecedulaOfExpedienteListNewExpediente != null && !oldPacientecedulaOfExpedienteListNewExpediente.equals(paciente)) {
                        oldPacientecedulaOfExpedienteListNewExpediente.getExpedienteList().remove(expedienteListNewExpediente);
                        oldPacientecedulaOfExpedienteListNewExpediente = em.merge(oldPacientecedulaOfExpedienteListNewExpediente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = paciente.getCedula();
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
                paciente.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Responsable> responsableListOrphanCheck = paciente.getResponsableList();
            for (Responsable responsableListOrphanCheckResponsable : responsableListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Responsable " + responsableListOrphanCheckResponsable + " in its responsableList field has a non-nullable pacientecedula field.");
            }
            List<Expediente> expedienteListOrphanCheck = paciente.getExpedienteList();
            for (Expediente expedienteListOrphanCheckExpediente : expedienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Expediente " + expedienteListOrphanCheckExpediente + " in its expedienteList field has a non-nullable pacientecedula field.");
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
