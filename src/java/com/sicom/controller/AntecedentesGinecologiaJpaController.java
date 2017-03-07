/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import com.sicom.entities.AntecedentesGinecologia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Paciente;
import com.sicom.entities.Examen;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class AntecedentesGinecologiaJpaController implements Serializable {

    public AntecedentesGinecologiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AntecedentesGinecologia antecedentesGinecologia) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (antecedentesGinecologia.getExamenCollection() == null) {
            antecedentesGinecologia.setExamenCollection(new ArrayList<Examen>());
        }
        List<String> illegalOrphanMessages = null;
        Paciente pacienteOrphanCheck = antecedentesGinecologia.getPaciente();
        if (pacienteOrphanCheck != null) {
            AntecedentesGinecologia oldAntecedentesGinecologiaOfPaciente = pacienteOrphanCheck.getAntecedentesGinecologia();
            if (oldAntecedentesGinecologiaOfPaciente != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Paciente " + pacienteOrphanCheck + " already has an item of type AntecedentesGinecologia whose paciente column cannot be null. Please make another selection for the paciente field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paciente paciente = antecedentesGinecologia.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getId());
                antecedentesGinecologia.setPaciente(paciente);
            }
            Collection<Examen> attachedExamenCollection = new ArrayList<Examen>();
            for (Examen examenCollectionExamenToAttach : antecedentesGinecologia.getExamenCollection()) {
                examenCollectionExamenToAttach = em.getReference(examenCollectionExamenToAttach.getClass(), examenCollectionExamenToAttach.getId());
                attachedExamenCollection.add(examenCollectionExamenToAttach);
            }
            antecedentesGinecologia.setExamenCollection(attachedExamenCollection);
            em.persist(antecedentesGinecologia);
            if (paciente != null) {
                paciente.setAntecedentesGinecologia(antecedentesGinecologia);
                paciente = em.merge(paciente);
            }
            for (Examen examenCollectionExamen : antecedentesGinecologia.getExamenCollection()) {
                AntecedentesGinecologia oldPacienteIdOfExamenCollectionExamen = examenCollectionExamen.getPacienteId();
                examenCollectionExamen.setPacienteId(antecedentesGinecologia);
                examenCollectionExamen = em.merge(examenCollectionExamen);
                if (oldPacienteIdOfExamenCollectionExamen != null) {
                    oldPacienteIdOfExamenCollectionExamen.getExamenCollection().remove(examenCollectionExamen);
                    oldPacienteIdOfExamenCollectionExamen = em.merge(oldPacienteIdOfExamenCollectionExamen);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAntecedentesGinecologia(antecedentesGinecologia.getPacienteid()) != null) {
                throw new PreexistingEntityException("AntecedentesGinecologia " + antecedentesGinecologia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AntecedentesGinecologia antecedentesGinecologia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesGinecologia persistentAntecedentesGinecologia = em.find(AntecedentesGinecologia.class, antecedentesGinecologia.getPacienteid());
            Paciente pacienteOld = persistentAntecedentesGinecologia.getPaciente();
            Paciente pacienteNew = antecedentesGinecologia.getPaciente();
            Collection<Examen> examenCollectionOld = persistentAntecedentesGinecologia.getExamenCollection();
            Collection<Examen> examenCollectionNew = antecedentesGinecologia.getExamenCollection();
            List<String> illegalOrphanMessages = null;
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                AntecedentesGinecologia oldAntecedentesGinecologiaOfPaciente = pacienteNew.getAntecedentesGinecologia();
                if (oldAntecedentesGinecologiaOfPaciente != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Paciente " + pacienteNew + " already has an item of type AntecedentesGinecologia whose paciente column cannot be null. Please make another selection for the paciente field.");
                }
            }
            for (Examen examenCollectionOldExamen : examenCollectionOld) {
                if (!examenCollectionNew.contains(examenCollectionOldExamen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Examen " + examenCollectionOldExamen + " since its pacienteId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getId());
                antecedentesGinecologia.setPaciente(pacienteNew);
            }
            Collection<Examen> attachedExamenCollectionNew = new ArrayList<Examen>();
            for (Examen examenCollectionNewExamenToAttach : examenCollectionNew) {
                examenCollectionNewExamenToAttach = em.getReference(examenCollectionNewExamenToAttach.getClass(), examenCollectionNewExamenToAttach.getId());
                attachedExamenCollectionNew.add(examenCollectionNewExamenToAttach);
            }
            examenCollectionNew = attachedExamenCollectionNew;
            antecedentesGinecologia.setExamenCollection(examenCollectionNew);
            antecedentesGinecologia = em.merge(antecedentesGinecologia);
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.setAntecedentesGinecologia(null);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                pacienteNew.setAntecedentesGinecologia(antecedentesGinecologia);
                pacienteNew = em.merge(pacienteNew);
            }
            for (Examen examenCollectionNewExamen : examenCollectionNew) {
                if (!examenCollectionOld.contains(examenCollectionNewExamen)) {
                    AntecedentesGinecologia oldPacienteIdOfExamenCollectionNewExamen = examenCollectionNewExamen.getPacienteId();
                    examenCollectionNewExamen.setPacienteId(antecedentesGinecologia);
                    examenCollectionNewExamen = em.merge(examenCollectionNewExamen);
                    if (oldPacienteIdOfExamenCollectionNewExamen != null && !oldPacienteIdOfExamenCollectionNewExamen.equals(antecedentesGinecologia)) {
                        oldPacienteIdOfExamenCollectionNewExamen.getExamenCollection().remove(examenCollectionNewExamen);
                        oldPacienteIdOfExamenCollectionNewExamen = em.merge(oldPacienteIdOfExamenCollectionNewExamen);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = antecedentesGinecologia.getPacienteid();
                if (findAntecedentesGinecologia(id) == null) {
                    throw new NonexistentEntityException("The antecedentesGinecologia with id " + id + " no longer exists.");
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
            AntecedentesGinecologia antecedentesGinecologia;
            try {
                antecedentesGinecologia = em.getReference(AntecedentesGinecologia.class, id);
                antecedentesGinecologia.getPacienteid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The antecedentesGinecologia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Examen> examenCollectionOrphanCheck = antecedentesGinecologia.getExamenCollection();
            for (Examen examenCollectionOrphanCheckExamen : examenCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AntecedentesGinecologia (" + antecedentesGinecologia + ") cannot be destroyed since the Examen " + examenCollectionOrphanCheckExamen + " in its examenCollection field has a non-nullable pacienteId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paciente paciente = antecedentesGinecologia.getPaciente();
            if (paciente != null) {
                paciente.setAntecedentesGinecologia(null);
                paciente = em.merge(paciente);
            }
            em.remove(antecedentesGinecologia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AntecedentesGinecologia> findAntecedentesGinecologiaEntities() {
        return findAntecedentesGinecologiaEntities(true, -1, -1);
    }

    public List<AntecedentesGinecologia> findAntecedentesGinecologiaEntities(int maxResults, int firstResult) {
        return findAntecedentesGinecologiaEntities(false, maxResults, firstResult);
    }

    private List<AntecedentesGinecologia> findAntecedentesGinecologiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AntecedentesGinecologia.class));
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

    public AntecedentesGinecologia findAntecedentesGinecologia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AntecedentesGinecologia.class, id);
        } finally {
            em.close();
        }
    }

    public int getAntecedentesGinecologiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AntecedentesGinecologia> rt = cq.from(AntecedentesGinecologia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
