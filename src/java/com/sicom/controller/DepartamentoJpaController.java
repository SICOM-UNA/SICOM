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
import com.sicom.entities.Documentos;
import com.sicom.entities.Cita;
import com.sicom.entities.Departamento;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(EntityManagerFactory emf) {
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
        if (departamento.getDocumentosList() == null) {
            departamento.setDocumentosList(new ArrayList<Documentos>());
        }
        if (departamento.getCitaList() == null) {
            departamento.setCitaList(new ArrayList<Cita>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personal> attachedPersonalList = new ArrayList<Personal>();
            for (Personal personalListPersonalToAttach : departamento.getPersonalList()) {
                personalListPersonalToAttach = em.getReference(personalListPersonalToAttach.getClass(), personalListPersonalToAttach.getCedula());
                attachedPersonalList.add(personalListPersonalToAttach);
            }
            departamento.setPersonalList(attachedPersonalList);
            List<Documentos> attachedDocumentosList = new ArrayList<Documentos>();
            for (Documentos documentosListDocumentosToAttach : departamento.getDocumentosList()) {
                documentosListDocumentosToAttach = em.getReference(documentosListDocumentosToAttach.getClass(), documentosListDocumentosToAttach.getId());
                attachedDocumentosList.add(documentosListDocumentosToAttach);
            }
            departamento.setDocumentosList(attachedDocumentosList);
            List<Cita> attachedCitaList = new ArrayList<Cita>();
            for (Cita citaListCitaToAttach : departamento.getCitaList()) {
                citaListCitaToAttach = em.getReference(citaListCitaToAttach.getClass(), citaListCitaToAttach.getId());
                attachedCitaList.add(citaListCitaToAttach);
            }
            departamento.setCitaList(attachedCitaList);
            em.persist(departamento);
            for (Personal personalListPersonal : departamento.getPersonalList()) {
                Departamento oldDepartamentoidOfPersonalListPersonal = personalListPersonal.getDepartamentoid();
                personalListPersonal.setDepartamentoid(departamento);
                personalListPersonal = em.merge(personalListPersonal);
                if (oldDepartamentoidOfPersonalListPersonal != null) {
                    oldDepartamentoidOfPersonalListPersonal.getPersonalList().remove(personalListPersonal);
                    oldDepartamentoidOfPersonalListPersonal = em.merge(oldDepartamentoidOfPersonalListPersonal);
                }
            }
            for (Documentos documentosListDocumentos : departamento.getDocumentosList()) {
                Departamento oldDepartamentoidOfDocumentosListDocumentos = documentosListDocumentos.getDepartamentoid();
                documentosListDocumentos.setDepartamentoid(departamento);
                documentosListDocumentos = em.merge(documentosListDocumentos);
                if (oldDepartamentoidOfDocumentosListDocumentos != null) {
                    oldDepartamentoidOfDocumentosListDocumentos.getDocumentosList().remove(documentosListDocumentos);
                    oldDepartamentoidOfDocumentosListDocumentos = em.merge(oldDepartamentoidOfDocumentosListDocumentos);
                }
            }
            for (Cita citaListCita : departamento.getCitaList()) {
                Departamento oldDepartamentoidOfCitaListCita = citaListCita.getDepartamentoid();
                citaListCita.setDepartamentoid(departamento);
                citaListCita = em.merge(citaListCita);
                if (oldDepartamentoidOfCitaListCita != null) {
                    oldDepartamentoidOfCitaListCita.getCitaList().remove(citaListCita);
                    oldDepartamentoidOfCitaListCita = em.merge(oldDepartamentoidOfCitaListCita);
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
            List<Documentos> documentosListOld = persistentDepartamento.getDocumentosList();
            List<Documentos> documentosListNew = departamento.getDocumentosList();
            List<Cita> citaListOld = persistentDepartamento.getCitaList();
            List<Cita> citaListNew = departamento.getCitaList();
            List<String> illegalOrphanMessages = null;
            for (Personal personalListOldPersonal : personalListOld) {
                if (!personalListNew.contains(personalListOldPersonal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Personal " + personalListOldPersonal + " since its departamentoid field is not nullable.");
                }
            }
            for (Documentos documentosListOldDocumentos : documentosListOld) {
                if (!documentosListNew.contains(documentosListOldDocumentos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documentos " + documentosListOldDocumentos + " since its departamentoid field is not nullable.");
                }
            }
            for (Cita citaListOldCita : citaListOld) {
                if (!citaListNew.contains(citaListOldCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cita " + citaListOldCita + " since its departamentoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Personal> attachedPersonalListNew = new ArrayList<Personal>();
            for (Personal personalListNewPersonalToAttach : personalListNew) {
                personalListNewPersonalToAttach = em.getReference(personalListNewPersonalToAttach.getClass(), personalListNewPersonalToAttach.getCedula());
                attachedPersonalListNew.add(personalListNewPersonalToAttach);
            }
            personalListNew = attachedPersonalListNew;
            departamento.setPersonalList(personalListNew);
            List<Documentos> attachedDocumentosListNew = new ArrayList<Documentos>();
            for (Documentos documentosListNewDocumentosToAttach : documentosListNew) {
                documentosListNewDocumentosToAttach = em.getReference(documentosListNewDocumentosToAttach.getClass(), documentosListNewDocumentosToAttach.getId());
                attachedDocumentosListNew.add(documentosListNewDocumentosToAttach);
            }
            documentosListNew = attachedDocumentosListNew;
            departamento.setDocumentosList(documentosListNew);
            List<Cita> attachedCitaListNew = new ArrayList<Cita>();
            for (Cita citaListNewCitaToAttach : citaListNew) {
                citaListNewCitaToAttach = em.getReference(citaListNewCitaToAttach.getClass(), citaListNewCitaToAttach.getId());
                attachedCitaListNew.add(citaListNewCitaToAttach);
            }
            citaListNew = attachedCitaListNew;
            departamento.setCitaList(citaListNew);
            departamento = em.merge(departamento);
            for (Personal personalListNewPersonal : personalListNew) {
                if (!personalListOld.contains(personalListNewPersonal)) {
                    Departamento oldDepartamentoidOfPersonalListNewPersonal = personalListNewPersonal.getDepartamentoid();
                    personalListNewPersonal.setDepartamentoid(departamento);
                    personalListNewPersonal = em.merge(personalListNewPersonal);
                    if (oldDepartamentoidOfPersonalListNewPersonal != null && !oldDepartamentoidOfPersonalListNewPersonal.equals(departamento)) {
                        oldDepartamentoidOfPersonalListNewPersonal.getPersonalList().remove(personalListNewPersonal);
                        oldDepartamentoidOfPersonalListNewPersonal = em.merge(oldDepartamentoidOfPersonalListNewPersonal);
                    }
                }
            }
            for (Documentos documentosListNewDocumentos : documentosListNew) {
                if (!documentosListOld.contains(documentosListNewDocumentos)) {
                    Departamento oldDepartamentoidOfDocumentosListNewDocumentos = documentosListNewDocumentos.getDepartamentoid();
                    documentosListNewDocumentos.setDepartamentoid(departamento);
                    documentosListNewDocumentos = em.merge(documentosListNewDocumentos);
                    if (oldDepartamentoidOfDocumentosListNewDocumentos != null && !oldDepartamentoidOfDocumentosListNewDocumentos.equals(departamento)) {
                        oldDepartamentoidOfDocumentosListNewDocumentos.getDocumentosList().remove(documentosListNewDocumentos);
                        oldDepartamentoidOfDocumentosListNewDocumentos = em.merge(oldDepartamentoidOfDocumentosListNewDocumentos);
                    }
                }
            }
            for (Cita citaListNewCita : citaListNew) {
                if (!citaListOld.contains(citaListNewCita)) {
                    Departamento oldDepartamentoidOfCitaListNewCita = citaListNewCita.getDepartamentoid();
                    citaListNewCita.setDepartamentoid(departamento);
                    citaListNewCita = em.merge(citaListNewCita);
                    if (oldDepartamentoidOfCitaListNewCita != null && !oldDepartamentoidOfCitaListNewCita.equals(departamento)) {
                        oldDepartamentoidOfCitaListNewCita.getCitaList().remove(citaListNewCita);
                        oldDepartamentoidOfCitaListNewCita = em.merge(oldDepartamentoidOfCitaListNewCita);
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
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Personal " + personalListOrphanCheckPersonal + " in its personalList field has a non-nullable departamentoid field.");
            }
            List<Documentos> documentosListOrphanCheck = departamento.getDocumentosList();
            for (Documentos documentosListOrphanCheckDocumentos : documentosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Documentos " + documentosListOrphanCheckDocumentos + " in its documentosList field has a non-nullable departamentoid field.");
            }
            List<Cita> citaListOrphanCheck = departamento.getCitaList();
            for (Cita citaListOrphanCheckCita : citaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Cita " + citaListOrphanCheckCita + " in its citaList field has a non-nullable departamentoid field.");
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
