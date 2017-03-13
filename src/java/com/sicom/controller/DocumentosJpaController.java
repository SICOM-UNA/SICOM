/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Departamento;
import com.sicom.entities.Documentos;
import com.sicom.entities.Expediente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
 */
public class DocumentosJpaController implements Serializable {

    public DocumentosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Documentos documentos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento departamentoId = documentos.getDepartamentoId();
            if (departamentoId != null) {
                departamentoId = em.getReference(departamentoId.getClass(), departamentoId.getId());
                documentos.setDepartamentoId(departamentoId);
            }
            Expediente expedientePacienteCedula = documentos.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula = em.getReference(expedientePacienteCedula.getClass(), expedientePacienteCedula.getExpedientePK());
                documentos.setExpedientePacienteCedula(expedientePacienteCedula);
            }
            em.persist(documentos);
            if (departamentoId != null) {
                departamentoId.getDocumentosList().add(documentos);
                departamentoId = em.merge(departamentoId);
            }
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getDocumentosList().add(documentos);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documentos documentos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documentos persistentDocumentos = em.find(Documentos.class, documentos.getId());
            Departamento departamentoIdOld = persistentDocumentos.getDepartamentoId();
            Departamento departamentoIdNew = documentos.getDepartamentoId();
            Expediente expedientePacienteCedulaOld = persistentDocumentos.getExpedientePacienteCedula();
            Expediente expedientePacienteCedulaNew = documentos.getExpedientePacienteCedula();
            if (departamentoIdNew != null) {
                departamentoIdNew = em.getReference(departamentoIdNew.getClass(), departamentoIdNew.getId());
                documentos.setDepartamentoId(departamentoIdNew);
            }
            if (expedientePacienteCedulaNew != null) {
                expedientePacienteCedulaNew = em.getReference(expedientePacienteCedulaNew.getClass(), expedientePacienteCedulaNew.getExpedientePK());
                documentos.setExpedientePacienteCedula(expedientePacienteCedulaNew);
            }
            documentos = em.merge(documentos);
            if (departamentoIdOld != null && !departamentoIdOld.equals(departamentoIdNew)) {
                departamentoIdOld.getDocumentosList().remove(documentos);
                departamentoIdOld = em.merge(departamentoIdOld);
            }
            if (departamentoIdNew != null && !departamentoIdNew.equals(departamentoIdOld)) {
                departamentoIdNew.getDocumentosList().add(documentos);
                departamentoIdNew = em.merge(departamentoIdNew);
            }
            if (expedientePacienteCedulaOld != null && !expedientePacienteCedulaOld.equals(expedientePacienteCedulaNew)) {
                expedientePacienteCedulaOld.getDocumentosList().remove(documentos);
                expedientePacienteCedulaOld = em.merge(expedientePacienteCedulaOld);
            }
            if (expedientePacienteCedulaNew != null && !expedientePacienteCedulaNew.equals(expedientePacienteCedulaOld)) {
                expedientePacienteCedulaNew.getDocumentosList().add(documentos);
                expedientePacienteCedulaNew = em.merge(expedientePacienteCedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentos.getId();
                if (findDocumentos(id) == null) {
                    throw new NonexistentEntityException("The documentos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documentos documentos;
            try {
                documentos = em.getReference(Documentos.class, id);
                documentos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentos with id " + id + " no longer exists.", enfe);
            }
            Departamento departamentoId = documentos.getDepartamentoId();
            if (departamentoId != null) {
                departamentoId.getDocumentosList().remove(documentos);
                departamentoId = em.merge(departamentoId);
            }
            Expediente expedientePacienteCedula = documentos.getExpedientePacienteCedula();
            if (expedientePacienteCedula != null) {
                expedientePacienteCedula.getDocumentosList().remove(documentos);
                expedientePacienteCedula = em.merge(expedientePacienteCedula);
            }
            em.remove(documentos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documentos> findDocumentosEntities() {
        return findDocumentosEntities(true, -1, -1);
    }

    public List<Documentos> findDocumentosEntities(int maxResults, int firstResult) {
        return findDocumentosEntities(false, maxResults, firstResult);
    }

    private List<Documentos> findDocumentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documentos.class));
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

    public Documentos findDocumentos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documentos> rt = cq.from(Documentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
