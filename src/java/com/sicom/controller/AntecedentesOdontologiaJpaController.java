/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import com.sicom.entities.AntecedentesOdontologia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Paciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class AntecedentesOdontologiaJpaController implements Serializable {

    public AntecedentesOdontologiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AntecedentesOdontologia antecedentesOdontologia) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Paciente pacienteOrphanCheck = antecedentesOdontologia.getPaciente();
        if (pacienteOrphanCheck != null) {
            AntecedentesOdontologia oldAntecedentesOdontologiaOfPaciente = pacienteOrphanCheck.getAntecedentesOdontologia();
            if (oldAntecedentesOdontologiaOfPaciente != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Paciente " + pacienteOrphanCheck + " already has an item of type AntecedentesOdontologia whose paciente column cannot be null. Please make another selection for the paciente field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paciente paciente = antecedentesOdontologia.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getId());
                antecedentesOdontologia.setPaciente(paciente);
            }
            em.persist(antecedentesOdontologia);
            if (paciente != null) {
                paciente.setAntecedentesOdontologia(antecedentesOdontologia);
                paciente = em.merge(paciente);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAntecedentesOdontologia(antecedentesOdontologia.getPacienteid()) != null) {
                throw new PreexistingEntityException("AntecedentesOdontologia " + antecedentesOdontologia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AntecedentesOdontologia antecedentesOdontologia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesOdontologia persistentAntecedentesOdontologia = em.find(AntecedentesOdontologia.class, antecedentesOdontologia.getPacienteid());
            Paciente pacienteOld = persistentAntecedentesOdontologia.getPaciente();
            Paciente pacienteNew = antecedentesOdontologia.getPaciente();
            List<String> illegalOrphanMessages = null;
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                AntecedentesOdontologia oldAntecedentesOdontologiaOfPaciente = pacienteNew.getAntecedentesOdontologia();
                if (oldAntecedentesOdontologiaOfPaciente != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Paciente " + pacienteNew + " already has an item of type AntecedentesOdontologia whose paciente column cannot be null. Please make another selection for the paciente field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getId());
                antecedentesOdontologia.setPaciente(pacienteNew);
            }
            antecedentesOdontologia = em.merge(antecedentesOdontologia);
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.setAntecedentesOdontologia(null);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                pacienteNew.setAntecedentesOdontologia(antecedentesOdontologia);
                pacienteNew = em.merge(pacienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = antecedentesOdontologia.getPacienteid();
                if (findAntecedentesOdontologia(id) == null) {
                    throw new NonexistentEntityException("The antecedentesOdontologia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesOdontologia antecedentesOdontologia;
            try {
                antecedentesOdontologia = em.getReference(AntecedentesOdontologia.class, id);
                antecedentesOdontologia.getPacienteid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The antecedentesOdontologia with id " + id + " no longer exists.", enfe);
            }
            Paciente paciente = antecedentesOdontologia.getPaciente();
            if (paciente != null) {
                paciente.setAntecedentesOdontologia(null);
                paciente = em.merge(paciente);
            }
            em.remove(antecedentesOdontologia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AntecedentesOdontologia> findAntecedentesOdontologiaEntities() {
        return findAntecedentesOdontologiaEntities(true, -1, -1);
    }

    public List<AntecedentesOdontologia> findAntecedentesOdontologiaEntities(int maxResults, int firstResult) {
        return findAntecedentesOdontologiaEntities(false, maxResults, firstResult);
    }

    private List<AntecedentesOdontologia> findAntecedentesOdontologiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AntecedentesOdontologia.class));
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

    public AntecedentesOdontologia findAntecedentesOdontologia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AntecedentesOdontologia.class, id);
        } finally {
            em.close();
        }
    }

    public int getAntecedentesOdontologiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AntecedentesOdontologia> rt = cq.from(AntecedentesOdontologia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
