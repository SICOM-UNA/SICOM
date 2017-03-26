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
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Paciente;
import com.sicom.entities.AntecedentesOdontologia;
import com.sicom.entities.ExamenColposcopia;
import java.util.ArrayList;
import java.util.List;
import com.sicom.entities.ExamenOdontologia;
import com.sicom.entities.Documentos;
import com.sicom.entities.ExamenGinecologia;
import com.sicom.entities.Expediente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pablo
 */
public class ExpedienteJpaController implements Serializable {

    public ExpedienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    
    public void create(Expediente expediente) throws IllegalOrphanException {
        if (expediente.getExamenColposcopiaList() == null) {
            expediente.setExamenColposcopiaList(new ArrayList<ExamenColposcopia>());
        }
        if (expediente.getExamenOdontologiaList() == null) {
            expediente.setExamenOdontologiaList(new ArrayList<ExamenOdontologia>());
        }
        if (expediente.getDocumentosList() == null) {
            expediente.setDocumentosList(new ArrayList<Documentos>());
        }
        if (expediente.getExamenGinecologiaList() == null) {
            expediente.setExamenGinecologiaList(new ArrayList<ExamenGinecologia>());
        }
        List<String> illegalOrphanMessages = null;
        Paciente pacientecedulaOrphanCheck = expediente.getPacientecedula();
        if (pacientecedulaOrphanCheck != null) {
            Expediente oldExpedienteOfPacientecedula = pacientecedulaOrphanCheck.getExpediente();
            if (oldExpedienteOfPacientecedula != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Paciente " + pacientecedulaOrphanCheck + " already has an item of type Expediente whose pacientecedula column cannot be null. Please make another selection for the pacientecedula field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AntecedentesGinecologia antecedentesGinecologia = expediente.getAntecedentesGinecologia();
            if (antecedentesGinecologia != null) {
                antecedentesGinecologia = em.getReference(antecedentesGinecologia.getClass(), antecedentesGinecologia.getId());
                expediente.setAntecedentesGinecologia(antecedentesGinecologia);
            }
            Paciente pacientecedula = expediente.getPacientecedula();
            if (pacientecedula != null) {
                pacientecedula = em.getReference(pacientecedula.getClass(), pacientecedula.getCedula());
                expediente.setPacientecedula(pacientecedula);
            }
            AntecedentesOdontologia antecedentesOdontologia = expediente.getAntecedentesOdontologia();
            if (antecedentesOdontologia != null) {
                antecedentesOdontologia = em.getReference(antecedentesOdontologia.getClass(), antecedentesOdontologia.getId());
                expediente.setAntecedentesOdontologia(antecedentesOdontologia);
            }
            List<ExamenColposcopia> attachedExamenColposcopiaList = new ArrayList<ExamenColposcopia>();
            for (ExamenColposcopia examenColposcopiaListExamenColposcopiaToAttach : expediente.getExamenColposcopiaList()) {
                examenColposcopiaListExamenColposcopiaToAttach = em.getReference(examenColposcopiaListExamenColposcopiaToAttach.getClass(), examenColposcopiaListExamenColposcopiaToAttach.getId());
                attachedExamenColposcopiaList.add(examenColposcopiaListExamenColposcopiaToAttach);
            }
            expediente.setExamenColposcopiaList(attachedExamenColposcopiaList);
            List<ExamenOdontologia> attachedExamenOdontologiaList = new ArrayList<ExamenOdontologia>();
            for (ExamenOdontologia examenodontologiaListExamenOdontologiaToAttach : expediente.getExamenOdontologiaList()) {
                examenodontologiaListExamenOdontologiaToAttach = em.getReference(examenodontologiaListExamenOdontologiaToAttach.getClass(), examenodontologiaListExamenOdontologiaToAttach.getId());
                attachedExamenOdontologiaList.add(examenodontologiaListExamenOdontologiaToAttach);
            }
            expediente.setExamenOdontologiaList(attachedExamenOdontologiaList);
            List<Documentos> attachedDocumentosList = new ArrayList<Documentos>();
            for (Documentos documentosListDocumentosToAttach : expediente.getDocumentosList()) {
                documentosListDocumentosToAttach = em.getReference(documentosListDocumentosToAttach.getClass(), documentosListDocumentosToAttach.getId());
                attachedDocumentosList.add(documentosListDocumentosToAttach);
            }
            expediente.setDocumentosList(attachedDocumentosList);
            List<ExamenGinecologia> attachedExamenGinecologiaList = new ArrayList<ExamenGinecologia>();
            for (ExamenGinecologia examenGinecologiaListExamenGinecologiaToAttach : expediente.getExamenGinecologiaList()) {
                examenGinecologiaListExamenGinecologiaToAttach = em.getReference(examenGinecologiaListExamenGinecologiaToAttach.getClass(), examenGinecologiaListExamenGinecologiaToAttach.getId());
                attachedExamenGinecologiaList.add(examenGinecologiaListExamenGinecologiaToAttach);
            }
            expediente.setExamenGinecologiaList(attachedExamenGinecologiaList);
            em.persist(expediente);
            if (antecedentesGinecologia != null) {
                Expediente oldExpedientePacientecedulaOfAntecedentesGinecologia = antecedentesGinecologia.getExpedientePacientecedula();
                if (oldExpedientePacientecedulaOfAntecedentesGinecologia != null) {
                    oldExpedientePacientecedulaOfAntecedentesGinecologia.setAntecedentesGinecologia(null);
                    oldExpedientePacientecedulaOfAntecedentesGinecologia = em.merge(oldExpedientePacientecedulaOfAntecedentesGinecologia);
                }
                antecedentesGinecologia.setExpedientePacientecedula(expediente);
                antecedentesGinecologia = em.merge(antecedentesGinecologia);
            }
            if (pacientecedula != null) {
                pacientecedula.setExpediente(expediente);
                pacientecedula = em.merge(pacientecedula);
            }
            if (antecedentesOdontologia != null) {
                Expediente oldExpedientePacientecedulaOfAntecedentesOdontologia = antecedentesOdontologia.getExpedientePacientecedula();
                if (oldExpedientePacientecedulaOfAntecedentesOdontologia != null) {
                    oldExpedientePacientecedulaOfAntecedentesOdontologia.setAntecedentesOdontologia(null);
                    oldExpedientePacientecedulaOfAntecedentesOdontologia = em.merge(oldExpedientePacientecedulaOfAntecedentesOdontologia);
                }
                antecedentesOdontologia.setExpedientePacientecedula(expediente);
                antecedentesOdontologia = em.merge(antecedentesOdontologia);
            }
            for (ExamenColposcopia examenColposcopiaListExamenColposcopia : expediente.getExamenColposcopiaList()) {
                Expediente oldExpedientePacientecedulaOfExamenColposcopiaListExamenColposcopia = examenColposcopiaListExamenColposcopia.getExpedientePacientecedula();
                examenColposcopiaListExamenColposcopia.setExpedientePacientecedula(expediente);
                examenColposcopiaListExamenColposcopia = em.merge(examenColposcopiaListExamenColposcopia);
                if (oldExpedientePacientecedulaOfExamenColposcopiaListExamenColposcopia != null) {
                    oldExpedientePacientecedulaOfExamenColposcopiaListExamenColposcopia.getExamenColposcopiaList().remove(examenColposcopiaListExamenColposcopia);
                    oldExpedientePacientecedulaOfExamenColposcopiaListExamenColposcopia = em.merge(oldExpedientePacientecedulaOfExamenColposcopiaListExamenColposcopia);
                }
            }
            for (ExamenOdontologia examenodontologiaListExamenOdontologia : expediente.getExamenOdontologiaList()) {
                Expediente oldExpedientePacientecedulaOfExamenOdontologiaListExamenOdontologia = examenodontologiaListExamenOdontologia.getExpedientePacientecedula();
                examenodontologiaListExamenOdontologia.setExpedientePacientecedula(expediente);
                examenodontologiaListExamenOdontologia = em.merge(examenodontologiaListExamenOdontologia);
                if (oldExpedientePacientecedulaOfExamenOdontologiaListExamenOdontologia != null) {
                    oldExpedientePacientecedulaOfExamenOdontologiaListExamenOdontologia.getExamenOdontologiaList().remove(examenodontologiaListExamenOdontologia);
                    oldExpedientePacientecedulaOfExamenOdontologiaListExamenOdontologia = em.merge(oldExpedientePacientecedulaOfExamenOdontologiaListExamenOdontologia);
                }
            }
            for (Documentos documentosListDocumentos : expediente.getDocumentosList()) {
                Expediente oldExpedientePacientecedulaOfDocumentosListDocumentos = documentosListDocumentos.getExpedientePacientecedula();
                documentosListDocumentos.setExpedientePacientecedula(expediente);
                documentosListDocumentos = em.merge(documentosListDocumentos);
                if (oldExpedientePacientecedulaOfDocumentosListDocumentos != null) {
                    oldExpedientePacientecedulaOfDocumentosListDocumentos.getDocumentosList().remove(documentosListDocumentos);
                    oldExpedientePacientecedulaOfDocumentosListDocumentos = em.merge(oldExpedientePacientecedulaOfDocumentosListDocumentos);
                }
            }
            for (ExamenGinecologia examenGinecologiaListExamenGinecologia : expediente.getExamenGinecologiaList()) {
                Expediente oldExpedientePacientecedulaOfExamenGinecologiaListExamenGinecologia = examenGinecologiaListExamenGinecologia.getExpedientePacientecedula();
                examenGinecologiaListExamenGinecologia.setExpedientePacientecedula(expediente);
                examenGinecologiaListExamenGinecologia = em.merge(examenGinecologiaListExamenGinecologia);
                if (oldExpedientePacientecedulaOfExamenGinecologiaListExamenGinecologia != null) {
                    oldExpedientePacientecedulaOfExamenGinecologiaListExamenGinecologia.getExamenGinecologiaList().remove(examenGinecologiaListExamenGinecologia);
                    oldExpedientePacientecedulaOfExamenGinecologiaListExamenGinecologia = em.merge(oldExpedientePacientecedulaOfExamenGinecologiaListExamenGinecologia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Expediente expediente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente persistentExpediente = em.find(Expediente.class, expediente.getId());
            AntecedentesGinecologia antecedentesGinecologiaOld = persistentExpediente.getAntecedentesGinecologia();
            AntecedentesGinecologia antecedentesGinecologiaNew = expediente.getAntecedentesGinecologia();
            Paciente pacientecedulaOld = persistentExpediente.getPacientecedula();
            Paciente pacientecedulaNew = expediente.getPacientecedula();
            AntecedentesOdontologia antecedentesOdontologiaOld = persistentExpediente.getAntecedentesOdontologia();
            AntecedentesOdontologia antecedentesOdontologiaNew = expediente.getAntecedentesOdontologia();
            List<ExamenColposcopia> examenColposcopiaListOld = persistentExpediente.getExamenColposcopiaList();
            List<ExamenColposcopia> examenColposcopiaListNew = expediente.getExamenColposcopiaList();
            List<ExamenOdontologia> examenodontologiaListOld = persistentExpediente.getExamenOdontologiaList();
            List<ExamenOdontologia> examenodontologiaListNew = expediente.getExamenOdontologiaList();
            List<Documentos> documentosListOld = persistentExpediente.getDocumentosList();
            List<Documentos> documentosListNew = expediente.getDocumentosList();
            List<ExamenGinecologia> examenGinecologiaListOld = persistentExpediente.getExamenGinecologiaList();
            List<ExamenGinecologia> examenGinecologiaListNew = expediente.getExamenGinecologiaList();
            List<String> illegalOrphanMessages = null;
            if (antecedentesGinecologiaOld != null && !antecedentesGinecologiaOld.equals(antecedentesGinecologiaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain AntecedentesGinecologia " + antecedentesGinecologiaOld + " since its expedientePacientecedula field is not nullable.");
            }
            if (pacientecedulaNew != null && !pacientecedulaNew.equals(pacientecedulaOld)) {
                Expediente oldExpedienteOfPacientecedula = pacientecedulaNew.getExpediente();
                if (oldExpedienteOfPacientecedula != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Paciente " + pacientecedulaNew + " already has an item of type Expediente whose pacientecedula column cannot be null. Please make another selection for the pacientecedula field.");
                }
            }
            if (antecedentesOdontologiaOld != null && !antecedentesOdontologiaOld.equals(antecedentesOdontologiaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain AntecedentesOdontologia " + antecedentesOdontologiaOld + " since its expedientePacientecedula field is not nullable.");
            }
            for (ExamenColposcopia examenColposcopiaListOldExamenColposcopia : examenColposcopiaListOld) {
                if (!examenColposcopiaListNew.contains(examenColposcopiaListOldExamenColposcopia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenColposcopia " + examenColposcopiaListOldExamenColposcopia + " since its expedientePacientecedula field is not nullable.");
                }
            }
            for (ExamenOdontologia examenodontologiaListOldExamenOdontologia : examenodontologiaListOld) {
                if (!examenodontologiaListNew.contains(examenodontologiaListOldExamenOdontologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenOdontologia " + examenodontologiaListOldExamenOdontologia + " since its expedientePacientecedula field is not nullable.");
                }
            }
            for (Documentos documentosListOldDocumentos : documentosListOld) {
                if (!documentosListNew.contains(documentosListOldDocumentos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documentos " + documentosListOldDocumentos + " since its expedientePacientecedula field is not nullable.");
                }
            }
            for (ExamenGinecologia examenGinecologiaListOldExamenGinecologia : examenGinecologiaListOld) {
                if (!examenGinecologiaListNew.contains(examenGinecologiaListOldExamenGinecologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenGinecologia " + examenGinecologiaListOldExamenGinecologia + " since its expedientePacientecedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (antecedentesGinecologiaNew != null) {
                antecedentesGinecologiaNew = em.getReference(antecedentesGinecologiaNew.getClass(), antecedentesGinecologiaNew.getId());
                expediente.setAntecedentesGinecologia(antecedentesGinecologiaNew);
            }
            if (pacientecedulaNew != null) {
                pacientecedulaNew = em.getReference(pacientecedulaNew.getClass(), pacientecedulaNew.getCedula());
                expediente.setPacientecedula(pacientecedulaNew);
            }
            if (antecedentesOdontologiaNew != null) {
                antecedentesOdontologiaNew = em.getReference(antecedentesOdontologiaNew.getClass(), antecedentesOdontologiaNew.getId());
                expediente.setAntecedentesOdontologia(antecedentesOdontologiaNew);
            }
            List<ExamenColposcopia> attachedExamenColposcopiaListNew = new ArrayList<ExamenColposcopia>();
            for (ExamenColposcopia examenColposcopiaListNewExamenColposcopiaToAttach : examenColposcopiaListNew) {
                examenColposcopiaListNewExamenColposcopiaToAttach = em.getReference(examenColposcopiaListNewExamenColposcopiaToAttach.getClass(), examenColposcopiaListNewExamenColposcopiaToAttach.getId());
                attachedExamenColposcopiaListNew.add(examenColposcopiaListNewExamenColposcopiaToAttach);
            }
            examenColposcopiaListNew = attachedExamenColposcopiaListNew;
            expediente.setExamenColposcopiaList(examenColposcopiaListNew);
            List<ExamenOdontologia> attachedExamenOdontologiaListNew = new ArrayList<ExamenOdontologia>();
            for (ExamenOdontologia examenodontologiaListNewExamenOdontologiaToAttach : examenodontologiaListNew) {
                examenodontologiaListNewExamenOdontologiaToAttach = em.getReference(examenodontologiaListNewExamenOdontologiaToAttach.getClass(), examenodontologiaListNewExamenOdontologiaToAttach.getId());
                attachedExamenOdontologiaListNew.add(examenodontologiaListNewExamenOdontologiaToAttach);
            }
            examenodontologiaListNew = attachedExamenOdontologiaListNew;
            expediente.setExamenOdontologiaList(examenodontologiaListNew);
            List<Documentos> attachedDocumentosListNew = new ArrayList<Documentos>();
            for (Documentos documentosListNewDocumentosToAttach : documentosListNew) {
                documentosListNewDocumentosToAttach = em.getReference(documentosListNewDocumentosToAttach.getClass(), documentosListNewDocumentosToAttach.getId());
                attachedDocumentosListNew.add(documentosListNewDocumentosToAttach);
            }
            documentosListNew = attachedDocumentosListNew;
            expediente.setDocumentosList(documentosListNew);
            List<ExamenGinecologia> attachedExamenGinecologiaListNew = new ArrayList<ExamenGinecologia>();
            for (ExamenGinecologia examenGinecologiaListNewExamenGinecologiaToAttach : examenGinecologiaListNew) {
                examenGinecologiaListNewExamenGinecologiaToAttach = em.getReference(examenGinecologiaListNewExamenGinecologiaToAttach.getClass(), examenGinecologiaListNewExamenGinecologiaToAttach.getId());
                attachedExamenGinecologiaListNew.add(examenGinecologiaListNewExamenGinecologiaToAttach);
            }
            examenGinecologiaListNew = attachedExamenGinecologiaListNew;
            expediente.setExamenGinecologiaList(examenGinecologiaListNew);
            expediente = em.merge(expediente);
            if (antecedentesGinecologiaNew != null && !antecedentesGinecologiaNew.equals(antecedentesGinecologiaOld)) {
                Expediente oldExpedientePacientecedulaOfAntecedentesGinecologia = antecedentesGinecologiaNew.getExpedientePacientecedula();
                if (oldExpedientePacientecedulaOfAntecedentesGinecologia != null) {
                    oldExpedientePacientecedulaOfAntecedentesGinecologia.setAntecedentesGinecologia(null);
                    oldExpedientePacientecedulaOfAntecedentesGinecologia = em.merge(oldExpedientePacientecedulaOfAntecedentesGinecologia);
                }
                antecedentesGinecologiaNew.setExpedientePacientecedula(expediente);
                antecedentesGinecologiaNew = em.merge(antecedentesGinecologiaNew);
            }
            if (pacientecedulaOld != null && !pacientecedulaOld.equals(pacientecedulaNew)) {
                pacientecedulaOld.setExpediente(null);
                pacientecedulaOld = em.merge(pacientecedulaOld);
            }
            if (pacientecedulaNew != null && !pacientecedulaNew.equals(pacientecedulaOld)) {
                pacientecedulaNew.setExpediente(expediente);
                pacientecedulaNew = em.merge(pacientecedulaNew);
            }
            if (antecedentesOdontologiaNew != null && !antecedentesOdontologiaNew.equals(antecedentesOdontologiaOld)) {
                Expediente oldExpedientePacientecedulaOfAntecedentesOdontologia = antecedentesOdontologiaNew.getExpedientePacientecedula();
                if (oldExpedientePacientecedulaOfAntecedentesOdontologia != null) {
                    oldExpedientePacientecedulaOfAntecedentesOdontologia.setAntecedentesOdontologia(null);
                    oldExpedientePacientecedulaOfAntecedentesOdontologia = em.merge(oldExpedientePacientecedulaOfAntecedentesOdontologia);
                }
                antecedentesOdontologiaNew.setExpedientePacientecedula(expediente);
                antecedentesOdontologiaNew = em.merge(antecedentesOdontologiaNew);
            }
            for (ExamenColposcopia examenColposcopiaListNewExamenColposcopia : examenColposcopiaListNew) {
                if (!examenColposcopiaListOld.contains(examenColposcopiaListNewExamenColposcopia)) {
                    Expediente oldExpedientePacientecedulaOfExamenColposcopiaListNewExamenColposcopia = examenColposcopiaListNewExamenColposcopia.getExpedientePacientecedula();
                    examenColposcopiaListNewExamenColposcopia.setExpedientePacientecedula(expediente);
                    examenColposcopiaListNewExamenColposcopia = em.merge(examenColposcopiaListNewExamenColposcopia);
                    if (oldExpedientePacientecedulaOfExamenColposcopiaListNewExamenColposcopia != null && !oldExpedientePacientecedulaOfExamenColposcopiaListNewExamenColposcopia.equals(expediente)) {
                        oldExpedientePacientecedulaOfExamenColposcopiaListNewExamenColposcopia.getExamenColposcopiaList().remove(examenColposcopiaListNewExamenColposcopia);
                        oldExpedientePacientecedulaOfExamenColposcopiaListNewExamenColposcopia = em.merge(oldExpedientePacientecedulaOfExamenColposcopiaListNewExamenColposcopia);
                    }
                }
            }
            for (ExamenOdontologia examenodontologiaListNewExamenOdontologia : examenodontologiaListNew) {
                if (!examenodontologiaListOld.contains(examenodontologiaListNewExamenOdontologia)) {
                    Expediente oldExpedientePacientecedulaOfExamenOdontologiaListNewExamenOdontologia = examenodontologiaListNewExamenOdontologia.getExpedientePacientecedula();
                    examenodontologiaListNewExamenOdontologia.setExpedientePacientecedula(expediente);
                    examenodontologiaListNewExamenOdontologia = em.merge(examenodontologiaListNewExamenOdontologia);
                    if (oldExpedientePacientecedulaOfExamenOdontologiaListNewExamenOdontologia != null && !oldExpedientePacientecedulaOfExamenOdontologiaListNewExamenOdontologia.equals(expediente)) {
                        oldExpedientePacientecedulaOfExamenOdontologiaListNewExamenOdontologia.getExamenOdontologiaList().remove(examenodontologiaListNewExamenOdontologia);
                        oldExpedientePacientecedulaOfExamenOdontologiaListNewExamenOdontologia = em.merge(oldExpedientePacientecedulaOfExamenOdontologiaListNewExamenOdontologia);
                    }
                }
            }
            for (Documentos documentosListNewDocumentos : documentosListNew) {
                if (!documentosListOld.contains(documentosListNewDocumentos)) {
                    Expediente oldExpedientePacientecedulaOfDocumentosListNewDocumentos = documentosListNewDocumentos.getExpedientePacientecedula();
                    documentosListNewDocumentos.setExpedientePacientecedula(expediente);
                    documentosListNewDocumentos = em.merge(documentosListNewDocumentos);
                    if (oldExpedientePacientecedulaOfDocumentosListNewDocumentos != null && !oldExpedientePacientecedulaOfDocumentosListNewDocumentos.equals(expediente)) {
                        oldExpedientePacientecedulaOfDocumentosListNewDocumentos.getDocumentosList().remove(documentosListNewDocumentos);
                        oldExpedientePacientecedulaOfDocumentosListNewDocumentos = em.merge(oldExpedientePacientecedulaOfDocumentosListNewDocumentos);
                    }
                }
            }
            for (ExamenGinecologia examenGinecologiaListNewExamenGinecologia : examenGinecologiaListNew) {
                if (!examenGinecologiaListOld.contains(examenGinecologiaListNewExamenGinecologia)) {
                    Expediente oldExpedientePacientecedulaOfExamenGinecologiaListNewExamenGinecologia = examenGinecologiaListNewExamenGinecologia.getExpedientePacientecedula();
                    examenGinecologiaListNewExamenGinecologia.setExpedientePacientecedula(expediente);
                    examenGinecologiaListNewExamenGinecologia = em.merge(examenGinecologiaListNewExamenGinecologia);
                    if (oldExpedientePacientecedulaOfExamenGinecologiaListNewExamenGinecologia != null && !oldExpedientePacientecedulaOfExamenGinecologiaListNewExamenGinecologia.equals(expediente)) {
                        oldExpedientePacientecedulaOfExamenGinecologiaListNewExamenGinecologia.getExamenGinecologiaList().remove(examenGinecologiaListNewExamenGinecologia);
                        oldExpedientePacientecedulaOfExamenGinecologiaListNewExamenGinecologia = em.merge(oldExpedientePacientecedulaOfExamenGinecologiaListNewExamenGinecologia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = expediente.getId();
                if (findExpediente(id) == null) {
                    throw new NonexistentEntityException("The expediente with id " + id + " no longer exists.");
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
            Expediente expediente;
            try {
                expediente = em.getReference(Expediente.class, id);
                expediente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The expediente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            AntecedentesGinecologia antecedentesGinecologiaOrphanCheck = expediente.getAntecedentesGinecologia();
            if (antecedentesGinecologiaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the AntecedentesGinecologia " + antecedentesGinecologiaOrphanCheck + " in its antecedentesGinecologia field has a non-nullable expedientePacientecedula field.");
            }
            AntecedentesOdontologia antecedentesOdontologiaOrphanCheck = expediente.getAntecedentesOdontologia();
            if (antecedentesOdontologiaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the AntecedentesOdontologia " + antecedentesOdontologiaOrphanCheck + " in its antecedentesOdontologia field has a non-nullable expedientePacientecedula field.");
            }
            List<ExamenColposcopia> examenColposcopiaListOrphanCheck = expediente.getExamenColposcopiaList();
            for (ExamenColposcopia examenColposcopiaListOrphanCheckExamenColposcopia : examenColposcopiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the ExamenColposcopia " + examenColposcopiaListOrphanCheckExamenColposcopia + " in its examenColposcopiaList field has a non-nullable expedientePacientecedula field.");
            }
            List<ExamenOdontologia> examenodontologiaListOrphanCheck = expediente.getExamenOdontologiaList();
            for (ExamenOdontologia examenodontologiaListOrphanCheckExamenOdontologia : examenodontologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the ExamenOdontologia " + examenodontologiaListOrphanCheckExamenOdontologia + " in its examenodontologiaList field has a non-nullable expedientePacientecedula field.");
            }
            List<Documentos> documentosListOrphanCheck = expediente.getDocumentosList();
            for (Documentos documentosListOrphanCheckDocumentos : documentosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the Documentos " + documentosListOrphanCheckDocumentos + " in its documentosList field has a non-nullable expedientePacientecedula field.");
            }
            List<ExamenGinecologia> examenGinecologiaListOrphanCheck = expediente.getExamenGinecologiaList();
            for (ExamenGinecologia examenGinecologiaListOrphanCheckExamenGinecologia : examenGinecologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the ExamenGinecologia " + examenGinecologiaListOrphanCheckExamenGinecologia + " in its examenGinecologiaList field has a non-nullable expedientePacientecedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paciente pacientecedula = expediente.getPacientecedula();
            if (pacientecedula != null) {
                pacientecedula.setExpediente(null);
                pacientecedula = em.merge(pacientecedula);
            }
            em.remove(expediente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Expediente> findExpedienteEntities() {
        return findExpedienteEntities(true, -1, -1);
    }

    public List<Expediente> findExpedienteEntities(int maxResults, int firstResult) {
        return findExpedienteEntities(false, maxResults, firstResult);
    }

    private List<Expediente> findExpedienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Expediente.class));
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

    public Expediente findExpediente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Expediente.class, id);
        } finally {
            em.close();
        }
    }

    public int getExpedienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Expediente> rt = cq.from(Expediente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
