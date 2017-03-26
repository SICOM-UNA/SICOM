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
import com.sicom.entities.Expediente;
import com.sicom.entities.Paciente;
import com.sicom.entities.Responsable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente expediente = paciente.getExpediente();
            if (expediente != null) {
                expediente = em.getReference(expediente.getClass(), expediente.getId());
                paciente.setExpediente(expediente);
            }
            List<Responsable> attachedResponsableList = new ArrayList<Responsable>();
            for (Responsable responsableListResponsableToAttach : paciente.getResponsableList()) {
                responsableListResponsableToAttach = em.getReference(responsableListResponsableToAttach.getClass(), responsableListResponsableToAttach.getCedula());
                attachedResponsableList.add(responsableListResponsableToAttach);
            }
            paciente.setResponsableList(attachedResponsableList);
            em.persist(paciente);
            if (expediente != null) {
                Paciente oldPacientecedulaOfExpediente = expediente.getPacientecedula();
                if (oldPacientecedulaOfExpediente != null) {
                    oldPacientecedulaOfExpediente.setExpediente(null);
                    oldPacientecedulaOfExpediente = em.merge(oldPacientecedulaOfExpediente);
                }
                expediente.setPacientecedula(paciente);
                expediente = em.merge(expediente);
            }
            for (Responsable responsableListResponsable : paciente.getResponsableList()) {
                Paciente oldPacientecedulaOfResponsableListResponsable = responsableListResponsable.getPacientecedula();
                responsableListResponsable.setPacientecedula(paciente);
                responsableListResponsable = em.merge(responsableListResponsable);
                if (oldPacientecedulaOfResponsableListResponsable != null) {
                    oldPacientecedulaOfResponsableListResponsable.getResponsableList().remove(responsableListResponsable);
                    oldPacientecedulaOfResponsableListResponsable = em.merge(oldPacientecedulaOfResponsableListResponsable);
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
            Expediente expedienteOld = persistentPaciente.getExpediente();
            Expediente expedienteNew = paciente.getExpediente();
            List<Responsable> responsableListOld = persistentPaciente.getResponsableList();
            List<Responsable> responsableListNew = paciente.getResponsableList();
            List<String> illegalOrphanMessages = null;
            if (expedienteOld != null && !expedienteOld.equals(expedienteNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Expediente " + expedienteOld + " since its pacientecedula field is not nullable.");
            }
            for (Responsable responsableListOldResponsable : responsableListOld) {
                if (!responsableListNew.contains(responsableListOldResponsable)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Responsable " + responsableListOldResponsable + " since its pacientecedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (expedienteNew != null) {
                expedienteNew = em.getReference(expedienteNew.getClass(), expedienteNew.getId());
                paciente.setExpediente(expedienteNew);
            }
            List<Responsable> attachedResponsableListNew = new ArrayList<Responsable>();
            for (Responsable responsableListNewResponsableToAttach : responsableListNew) {
                responsableListNewResponsableToAttach = em.getReference(responsableListNewResponsableToAttach.getClass(), responsableListNewResponsableToAttach.getCedula());
                attachedResponsableListNew.add(responsableListNewResponsableToAttach);
            }
            responsableListNew = attachedResponsableListNew;
            paciente.setResponsableList(responsableListNew);
            paciente = em.merge(paciente);
            if (expedienteNew != null && !expedienteNew.equals(expedienteOld)) {
                Paciente oldPacientecedulaOfExpediente = expedienteNew.getPacientecedula();
                if (oldPacientecedulaOfExpediente != null) {
                    oldPacientecedulaOfExpediente.setExpediente(null);
                    oldPacientecedulaOfExpediente = em.merge(oldPacientecedulaOfExpediente);
                }
                expedienteNew.setPacientecedula(paciente);
                expedienteNew = em.merge(expedienteNew);
            }
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
            Expediente expedienteOrphanCheck = paciente.getExpediente();
            if (expedienteOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Expediente " + expedienteOrphanCheck + " in its expediente field has a non-nullable pacientecedula field.");
            }
            List<Responsable> responsableListOrphanCheck = paciente.getResponsableList();
            for (Responsable responsableListOrphanCheckResponsable : responsableListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Responsable " + responsableListOrphanCheckResponsable + " in its responsableList field has a non-nullable pacientecedula field.");
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
