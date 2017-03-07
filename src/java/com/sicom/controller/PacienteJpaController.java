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
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.AntecedentesOdontologia;
import com.sicom.entities.Paciente;
import java.util.ArrayList;
import java.util.List;
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesGinecologia antecedentesGinecologia = paciente.getAntecedentesGinecologia();
            if (antecedentesGinecologia != null) {
                antecedentesGinecologia = em.getReference(antecedentesGinecologia.getClass(), antecedentesGinecologia.getPacienteid());
                paciente.setAntecedentesGinecologia(antecedentesGinecologia);
            }
            AntecedentesOdontologia antecedentesOdontologia = paciente.getAntecedentesOdontologia();
            if (antecedentesOdontologia != null) {
                antecedentesOdontologia = em.getReference(antecedentesOdontologia.getClass(), antecedentesOdontologia.getPacienteid());
                paciente.setAntecedentesOdontologia(antecedentesOdontologia);
            }
            em.persist(paciente);
            if (antecedentesGinecologia != null) {
                Paciente oldPacienteOfAntecedentesGinecologia = antecedentesGinecologia.getPaciente();
                if (oldPacienteOfAntecedentesGinecologia != null) {
                    oldPacienteOfAntecedentesGinecologia.setAntecedentesGinecologia(null);
                    oldPacienteOfAntecedentesGinecologia = em.merge(oldPacienteOfAntecedentesGinecologia);
                }
                antecedentesGinecologia.setPaciente(paciente);
                antecedentesGinecologia = em.merge(antecedentesGinecologia);
            }
            if (antecedentesOdontologia != null) {
                Paciente oldPacienteOfAntecedentesOdontologia = antecedentesOdontologia.getPaciente();
                if (oldPacienteOfAntecedentesOdontologia != null) {
                    oldPacienteOfAntecedentesOdontologia.setAntecedentesOdontologia(null);
                    oldPacienteOfAntecedentesOdontologia = em.merge(oldPacienteOfAntecedentesOdontologia);
                }
                antecedentesOdontologia.setPaciente(paciente);
                antecedentesOdontologia = em.merge(antecedentesOdontologia);
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
            AntecedentesGinecologia antecedentesGinecologiaOld = persistentPaciente.getAntecedentesGinecologia();
            AntecedentesGinecologia antecedentesGinecologiaNew = paciente.getAntecedentesGinecologia();
            AntecedentesOdontologia antecedentesOdontologiaOld = persistentPaciente.getAntecedentesOdontologia();
            AntecedentesOdontologia antecedentesOdontologiaNew = paciente.getAntecedentesOdontologia();
            List<String> illegalOrphanMessages = null;
            if (antecedentesGinecologiaOld != null && !antecedentesGinecologiaOld.equals(antecedentesGinecologiaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain AntecedentesGinecologia " + antecedentesGinecologiaOld + " since its paciente field is not nullable.");
            }
            if (antecedentesOdontologiaOld != null && !antecedentesOdontologiaOld.equals(antecedentesOdontologiaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain AntecedentesOdontologia " + antecedentesOdontologiaOld + " since its paciente field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (antecedentesGinecologiaNew != null) {
                antecedentesGinecologiaNew = em.getReference(antecedentesGinecologiaNew.getClass(), antecedentesGinecologiaNew.getPacienteid());
                paciente.setAntecedentesGinecologia(antecedentesGinecologiaNew);
            }
            if (antecedentesOdontologiaNew != null) {
                antecedentesOdontologiaNew = em.getReference(antecedentesOdontologiaNew.getClass(), antecedentesOdontologiaNew.getPacienteid());
                paciente.setAntecedentesOdontologia(antecedentesOdontologiaNew);
            }
            paciente = em.merge(paciente);
            if (antecedentesGinecologiaNew != null && !antecedentesGinecologiaNew.equals(antecedentesGinecologiaOld)) {
                Paciente oldPacienteOfAntecedentesGinecologia = antecedentesGinecologiaNew.getPaciente();
                if (oldPacienteOfAntecedentesGinecologia != null) {
                    oldPacienteOfAntecedentesGinecologia.setAntecedentesGinecologia(null);
                    oldPacienteOfAntecedentesGinecologia = em.merge(oldPacienteOfAntecedentesGinecologia);
                }
                antecedentesGinecologiaNew.setPaciente(paciente);
                antecedentesGinecologiaNew = em.merge(antecedentesGinecologiaNew);
            }
            if (antecedentesOdontologiaNew != null && !antecedentesOdontologiaNew.equals(antecedentesOdontologiaOld)) {
                Paciente oldPacienteOfAntecedentesOdontologia = antecedentesOdontologiaNew.getPaciente();
                if (oldPacienteOfAntecedentesOdontologia != null) {
                    oldPacienteOfAntecedentesOdontologia.setAntecedentesOdontologia(null);
                    oldPacienteOfAntecedentesOdontologia = em.merge(oldPacienteOfAntecedentesOdontologia);
                }
                antecedentesOdontologiaNew.setPaciente(paciente);
                antecedentesOdontologiaNew = em.merge(antecedentesOdontologiaNew);
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
            AntecedentesGinecologia antecedentesGinecologiaOrphanCheck = paciente.getAntecedentesGinecologia();
            if (antecedentesGinecologiaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the AntecedentesGinecologia " + antecedentesGinecologiaOrphanCheck + " in its antecedentesGinecologia field has a non-nullable paciente field.");
            }
            AntecedentesOdontologia antecedentesOdontologiaOrphanCheck = paciente.getAntecedentesOdontologia();
            if (antecedentesOdontologiaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the AntecedentesOdontologia " + antecedentesOdontologiaOrphanCheck + " in its antecedentesOdontologia field has a non-nullable paciente field.");
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
