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
 * @author Pablo
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
            Departamento departamentoid = documentos.getDepartamentoid();
            if (departamentoid != null) {
                departamentoid = em.getReference(departamentoid.getClass(), departamentoid.getId());
                documentos.setDepartamentoid(departamentoid);
            }
            Expediente expedienteid = documentos.getExpedienteid();
            if (expedienteid != null) {
                expedienteid = em.getReference(expedienteid.getClass(), expedienteid.getId());
                documentos.setExpedienteid(expedienteid);
            }
            em.persist(documentos);
            if (departamentoid != null) {
                departamentoid.getDocumentosList().add(documentos);
                departamentoid = em.merge(departamentoid);
            }
            if (expedienteid != null) {
                expedienteid.getDocumentosList().add(documentos);
                expedienteid = em.merge(expedienteid);
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
            Departamento departamentoidOld = persistentDocumentos.getDepartamentoid();
            Departamento departamentoidNew = documentos.getDepartamentoid();
            Expediente expedienteidOld = persistentDocumentos.getExpedienteid();
            Expediente expedienteidNew = documentos.getExpedienteid();
            if (departamentoidNew != null) {
                departamentoidNew = em.getReference(departamentoidNew.getClass(), departamentoidNew.getId());
                documentos.setDepartamentoid(departamentoidNew);
            }
            if (expedienteidNew != null) {
                expedienteidNew = em.getReference(expedienteidNew.getClass(), expedienteidNew.getId());
                documentos.setExpedienteid(expedienteidNew);
            }
            documentos = em.merge(documentos);
            if (departamentoidOld != null && !departamentoidOld.equals(departamentoidNew)) {
                departamentoidOld.getDocumentosList().remove(documentos);
                departamentoidOld = em.merge(departamentoidOld);
            }
            if (departamentoidNew != null && !departamentoidNew.equals(departamentoidOld)) {
                departamentoidNew.getDocumentosList().add(documentos);
                departamentoidNew = em.merge(departamentoidNew);
            }
            if (expedienteidOld != null && !expedienteidOld.equals(expedienteidNew)) {
                expedienteidOld.getDocumentosList().remove(documentos);
                expedienteidOld = em.merge(expedienteidOld);
            }
            if (expedienteidNew != null && !expedienteidNew.equals(expedienteidOld)) {
                expedienteidNew.getDocumentosList().add(documentos);
                expedienteidNew = em.merge(expedienteidNew);
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
            Departamento departamentoid = documentos.getDepartamentoid();
            if (departamentoid != null) {
                departamentoid.getDocumentosList().remove(documentos);
                departamentoid = em.merge(departamentoid);
            }
            Expediente expedienteid = documentos.getExpedienteid();
            if (expedienteid != null) {
                expedienteid.getDocumentosList().remove(documentos);
                expedienteid = em.merge(expedienteid);
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
