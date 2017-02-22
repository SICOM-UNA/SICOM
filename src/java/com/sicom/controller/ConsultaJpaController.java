/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import com.sicom.entities.Consulta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Departamento;
import com.sicom.entities.Paciente;
import com.sicom.entities.Personal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
 */
public class ConsultaJpaController implements Serializable {

    public ConsultaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consulta consulta) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento departamentoId = consulta.getDepartamentoId();
            if (departamentoId != null) {
                departamentoId = em.getReference(departamentoId.getClass(), departamentoId.getId());
                consulta.setDepartamentoId(departamentoId);
            }
            Paciente pacienteid = consulta.getPacienteid();
            if (pacienteid != null) {
                pacienteid = em.getReference(pacienteid.getClass(), pacienteid.getId());
                consulta.setPacienteid(pacienteid);
            }
            Personal personalId = consulta.getPersonalId();
            if (personalId != null) {
                personalId = em.getReference(personalId.getClass(), personalId.getId());
                consulta.setPersonalId(personalId);
            }
            em.persist(consulta);
            if (departamentoId != null) {
                departamentoId.getConsultaList().add(consulta);
                departamentoId = em.merge(departamentoId);
            }
            if (pacienteid != null) {
                pacienteid.getConsultaList().add(consulta);
                pacienteid = em.merge(pacienteid);
            }
            if (personalId != null) {
                personalId.getConsultaList().add(consulta);
                personalId = em.merge(personalId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConsulta(consulta.getFecha()) != null) {
                throw new PreexistingEntityException("Consulta " + consulta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Consulta consulta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consulta persistentConsulta = em.find(Consulta.class, consulta.getFecha());
            Departamento departamentoIdOld = persistentConsulta.getDepartamentoId();
            Departamento departamentoIdNew = consulta.getDepartamentoId();
            Paciente pacienteidOld = persistentConsulta.getPacienteid();
            Paciente pacienteidNew = consulta.getPacienteid();
            Personal personalIdOld = persistentConsulta.getPersonalId();
            Personal personalIdNew = consulta.getPersonalId();
            if (departamentoIdNew != null) {
                departamentoIdNew = em.getReference(departamentoIdNew.getClass(), departamentoIdNew.getId());
                consulta.setDepartamentoId(departamentoIdNew);
            }
            if (pacienteidNew != null) {
                pacienteidNew = em.getReference(pacienteidNew.getClass(), pacienteidNew.getId());
                consulta.setPacienteid(pacienteidNew);
            }
            if (personalIdNew != null) {
                personalIdNew = em.getReference(personalIdNew.getClass(), personalIdNew.getId());
                consulta.setPersonalId(personalIdNew);
            }
            consulta = em.merge(consulta);
            if (departamentoIdOld != null && !departamentoIdOld.equals(departamentoIdNew)) {
                departamentoIdOld.getConsultaList().remove(consulta);
                departamentoIdOld = em.merge(departamentoIdOld);
            }
            if (departamentoIdNew != null && !departamentoIdNew.equals(departamentoIdOld)) {
                departamentoIdNew.getConsultaList().add(consulta);
                departamentoIdNew = em.merge(departamentoIdNew);
            }
            if (pacienteidOld != null && !pacienteidOld.equals(pacienteidNew)) {
                pacienteidOld.getConsultaList().remove(consulta);
                pacienteidOld = em.merge(pacienteidOld);
            }
            if (pacienteidNew != null && !pacienteidNew.equals(pacienteidOld)) {
                pacienteidNew.getConsultaList().add(consulta);
                pacienteidNew = em.merge(pacienteidNew);
            }
            if (personalIdOld != null && !personalIdOld.equals(personalIdNew)) {
                personalIdOld.getConsultaList().remove(consulta);
                personalIdOld = em.merge(personalIdOld);
            }
            if (personalIdNew != null && !personalIdNew.equals(personalIdOld)) {
                personalIdNew.getConsultaList().add(consulta);
                personalIdNew = em.merge(personalIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Date id = consulta.getFecha();
                if (findConsulta(id) == null) {
                    throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Date id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consulta consulta;
            try {
                consulta = em.getReference(Consulta.class, id);
                consulta.getFecha();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.", enfe);
            }
            Departamento departamentoId = consulta.getDepartamentoId();
            if (departamentoId != null) {
                departamentoId.getConsultaList().remove(consulta);
                departamentoId = em.merge(departamentoId);
            }
            Paciente pacienteid = consulta.getPacienteid();
            if (pacienteid != null) {
                pacienteid.getConsultaList().remove(consulta);
                pacienteid = em.merge(pacienteid);
            }
            Personal personalId = consulta.getPersonalId();
            if (personalId != null) {
                personalId.getConsultaList().remove(consulta);
                personalId = em.merge(personalId);
            }
            em.remove(consulta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Consulta> findConsultaEntities() {
        return findConsultaEntities(true, -1, -1);
    }

    public List<Consulta> findConsultaEntities(int maxResults, int firstResult) {
        return findConsultaEntities(false, maxResults, firstResult);
    }

    private List<Consulta> findConsultaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consulta.class));
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

    public Consulta findConsulta(Date id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consulta.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consulta> rt = cq.from(Consulta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
