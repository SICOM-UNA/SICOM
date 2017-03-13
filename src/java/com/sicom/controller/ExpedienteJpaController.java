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
import com.sicom.entities.Paciente;
import com.sicom.entities.AntecedentesGinecologia;
import java.util.ArrayList;
import java.util.List;
import com.sicom.entities.ExamenColposcopia;
import com.sicom.entities.ExamenOdontologia;
import com.sicom.entities.AntecedentesOdontologia;
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

    public void create(Expediente expediente) throws PreexistingEntityException, Exception {
        if (expediente.getAntecedentesGinecologiaList() == null) {
            expediente.setAntecedentesGinecologiaList(new ArrayList<AntecedentesGinecologia>());
        }
        if (expediente.getExamenColposcopiaList() == null) {
            expediente.setExamenColposcopiaList(new ArrayList<ExamenColposcopia>());
        }
        if (expediente.getExamenOdontologiaList() == null) {
            expediente.setExamenOdontologiaList(new ArrayList<ExamenOdontologia>());
        }
        if (expediente.getAntecedentesOdontologiaList() == null) {
            expediente.setAntecedentesOdontologiaList(new ArrayList<AntecedentesOdontologia>());
        }
        if (expediente.getDocumentosList() == null) {
            expediente.setDocumentosList(new ArrayList<Documentos>());
        }
        if (expediente.getExamenGinecologiaList() == null) {
            expediente.setExamenGinecologiaList(new ArrayList<ExamenGinecologia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paciente pacientecedula = expediente.getPacientecedula();
            if (pacientecedula != null) {
                pacientecedula = em.getReference(pacientecedula.getClass(), pacientecedula.getCedula());
                expediente.setPacientecedula(pacientecedula);
            }
            List<AntecedentesGinecologia> attachedAntecedentesGinecologiaList = new ArrayList<AntecedentesGinecologia>();
            for (AntecedentesGinecologia antecedentesGinecologiaListAntecedentesGinecologiaToAttach : expediente.getAntecedentesGinecologiaList()) {
                antecedentesGinecologiaListAntecedentesGinecologiaToAttach = em.getReference(antecedentesGinecologiaListAntecedentesGinecologiaToAttach.getClass(), antecedentesGinecologiaListAntecedentesGinecologiaToAttach.getId());
                attachedAntecedentesGinecologiaList.add(antecedentesGinecologiaListAntecedentesGinecologiaToAttach);
            }
            expediente.setAntecedentesGinecologiaList(attachedAntecedentesGinecologiaList);
            List<ExamenColposcopia> attachedExamenColposcopiaList = new ArrayList<ExamenColposcopia>();
            for (ExamenColposcopia examenColposcopiaListExamenColposcopiaToAttach : expediente.getExamenColposcopiaList()) {
                examenColposcopiaListExamenColposcopiaToAttach = em.getReference(examenColposcopiaListExamenColposcopiaToAttach.getClass(), examenColposcopiaListExamenColposcopiaToAttach.getId());
                attachedExamenColposcopiaList.add(examenColposcopiaListExamenColposcopiaToAttach);
            }
            expediente.setExamenColposcopiaList(attachedExamenColposcopiaList);
            List<ExamenOdontologia> attachedExamenOdontologiaList = new ArrayList<ExamenOdontologia>();
            for (ExamenOdontologia examenOdontologiaListExamenOdontologiaToAttach : expediente.getExamenOdontologiaList()) {
                examenOdontologiaListExamenOdontologiaToAttach = em.getReference(examenOdontologiaListExamenOdontologiaToAttach.getClass(), examenOdontologiaListExamenOdontologiaToAttach.getId());
                attachedExamenOdontologiaList.add(examenOdontologiaListExamenOdontologiaToAttach);
            }
            expediente.setExamenOdontologiaList(attachedExamenOdontologiaList);
            List<AntecedentesOdontologia> attachedAntecedentesOdontologiaList = new ArrayList<AntecedentesOdontologia>();
            for (AntecedentesOdontologia antecedentesOdontologiaListAntecedentesOdontologiaToAttach : expediente.getAntecedentesOdontologiaList()) {
                antecedentesOdontologiaListAntecedentesOdontologiaToAttach = em.getReference(antecedentesOdontologiaListAntecedentesOdontologiaToAttach.getClass(), antecedentesOdontologiaListAntecedentesOdontologiaToAttach.getId());
                attachedAntecedentesOdontologiaList.add(antecedentesOdontologiaListAntecedentesOdontologiaToAttach);
            }
            expediente.setAntecedentesOdontologiaList(attachedAntecedentesOdontologiaList);
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
            if (pacientecedula != null) {
                pacientecedula.getExpedienteList().add(expediente);
                pacientecedula = em.merge(pacientecedula);
            }
            for (AntecedentesGinecologia antecedentesGinecologiaListAntecedentesGinecologia : expediente.getAntecedentesGinecologiaList()) {
                Expediente oldExpedienteidOfAntecedentesGinecologiaListAntecedentesGinecologia = antecedentesGinecologiaListAntecedentesGinecologia.getExpedienteid();
                antecedentesGinecologiaListAntecedentesGinecologia.setExpedienteid(expediente);
                antecedentesGinecologiaListAntecedentesGinecologia = em.merge(antecedentesGinecologiaListAntecedentesGinecologia);
                if (oldExpedienteidOfAntecedentesGinecologiaListAntecedentesGinecologia != null) {
                    oldExpedienteidOfAntecedentesGinecologiaListAntecedentesGinecologia.getAntecedentesGinecologiaList().remove(antecedentesGinecologiaListAntecedentesGinecologia);
                    oldExpedienteidOfAntecedentesGinecologiaListAntecedentesGinecologia = em.merge(oldExpedienteidOfAntecedentesGinecologiaListAntecedentesGinecologia);
                }
            }
            for (ExamenColposcopia examenColposcopiaListExamenColposcopia : expediente.getExamenColposcopiaList()) {
                Expediente oldExpedienteidOfExamenColposcopiaListExamenColposcopia = examenColposcopiaListExamenColposcopia.getExpedienteid();
                examenColposcopiaListExamenColposcopia.setExpedienteid(expediente);
                examenColposcopiaListExamenColposcopia = em.merge(examenColposcopiaListExamenColposcopia);
                if (oldExpedienteidOfExamenColposcopiaListExamenColposcopia != null) {
                    oldExpedienteidOfExamenColposcopiaListExamenColposcopia.getExamenColposcopiaList().remove(examenColposcopiaListExamenColposcopia);
                    oldExpedienteidOfExamenColposcopiaListExamenColposcopia = em.merge(oldExpedienteidOfExamenColposcopiaListExamenColposcopia);
                }
            }
            for (ExamenOdontologia examenOdontologiaListExamenOdontologia : expediente.getExamenOdontologiaList()) {
                Expediente oldExpedienteidOfExamenOdontologiaListExamenOdontologia = examenOdontologiaListExamenOdontologia.getExpedienteid();
                examenOdontologiaListExamenOdontologia.setExpedienteid(expediente);
                examenOdontologiaListExamenOdontologia = em.merge(examenOdontologiaListExamenOdontologia);
                if (oldExpedienteidOfExamenOdontologiaListExamenOdontologia != null) {
                    oldExpedienteidOfExamenOdontologiaListExamenOdontologia.getExamenOdontologiaList().remove(examenOdontologiaListExamenOdontologia);
                    oldExpedienteidOfExamenOdontologiaListExamenOdontologia = em.merge(oldExpedienteidOfExamenOdontologiaListExamenOdontologia);
                }
            }
            for (AntecedentesOdontologia antecedentesOdontologiaListAntecedentesOdontologia : expediente.getAntecedentesOdontologiaList()) {
                Expediente oldExpedienteidOfAntecedentesOdontologiaListAntecedentesOdontologia = antecedentesOdontologiaListAntecedentesOdontologia.getExpedienteid();
                antecedentesOdontologiaListAntecedentesOdontologia.setExpedienteid(expediente);
                antecedentesOdontologiaListAntecedentesOdontologia = em.merge(antecedentesOdontologiaListAntecedentesOdontologia);
                if (oldExpedienteidOfAntecedentesOdontologiaListAntecedentesOdontologia != null) {
                    oldExpedienteidOfAntecedentesOdontologiaListAntecedentesOdontologia.getAntecedentesOdontologiaList().remove(antecedentesOdontologiaListAntecedentesOdontologia);
                    oldExpedienteidOfAntecedentesOdontologiaListAntecedentesOdontologia = em.merge(oldExpedienteidOfAntecedentesOdontologiaListAntecedentesOdontologia);
                }
            }
            for (Documentos documentosListDocumentos : expediente.getDocumentosList()) {
                Expediente oldExpedienteidOfDocumentosListDocumentos = documentosListDocumentos.getExpedienteid();
                documentosListDocumentos.setExpedienteid(expediente);
                documentosListDocumentos = em.merge(documentosListDocumentos);
                if (oldExpedienteidOfDocumentosListDocumentos != null) {
                    oldExpedienteidOfDocumentosListDocumentos.getDocumentosList().remove(documentosListDocumentos);
                    oldExpedienteidOfDocumentosListDocumentos = em.merge(oldExpedienteidOfDocumentosListDocumentos);
                }
            }
            for (ExamenGinecologia examenGinecologiaListExamenGinecologia : expediente.getExamenGinecologiaList()) {
                Expediente oldExpedienteidOfExamenGinecologiaListExamenGinecologia = examenGinecologiaListExamenGinecologia.getExpedienteid();
                examenGinecologiaListExamenGinecologia.setExpedienteid(expediente);
                examenGinecologiaListExamenGinecologia = em.merge(examenGinecologiaListExamenGinecologia);
                if (oldExpedienteidOfExamenGinecologiaListExamenGinecologia != null) {
                    oldExpedienteidOfExamenGinecologiaListExamenGinecologia.getExamenGinecologiaList().remove(examenGinecologiaListExamenGinecologia);
                    oldExpedienteidOfExamenGinecologiaListExamenGinecologia = em.merge(oldExpedienteidOfExamenGinecologiaListExamenGinecologia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findExpediente(expediente.getId()) != null) {
                throw new PreexistingEntityException("Expediente " + expediente + " already exists.", ex);
            }
            throw ex;
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
            Paciente pacientecedulaOld = persistentExpediente.getPacientecedula();
            Paciente pacientecedulaNew = expediente.getPacientecedula();
            List<AntecedentesGinecologia> antecedentesGinecologiaListOld = persistentExpediente.getAntecedentesGinecologiaList();
            List<AntecedentesGinecologia> antecedentesGinecologiaListNew = expediente.getAntecedentesGinecologiaList();
            List<ExamenColposcopia> examenColposcopiaListOld = persistentExpediente.getExamenColposcopiaList();
            List<ExamenColposcopia> examenColposcopiaListNew = expediente.getExamenColposcopiaList();
            List<ExamenOdontologia> examenOdontologiaListOld = persistentExpediente.getExamenOdontologiaList();
            List<ExamenOdontologia> examenOdontologiaListNew = expediente.getExamenOdontologiaList();
            List<AntecedentesOdontologia> antecedentesOdontologiaListOld = persistentExpediente.getAntecedentesOdontologiaList();
            List<AntecedentesOdontologia> antecedentesOdontologiaListNew = expediente.getAntecedentesOdontologiaList();
            List<Documentos> documentosListOld = persistentExpediente.getDocumentosList();
            List<Documentos> documentosListNew = expediente.getDocumentosList();
            List<ExamenGinecologia> examenGinecologiaListOld = persistentExpediente.getExamenGinecologiaList();
            List<ExamenGinecologia> examenGinecologiaListNew = expediente.getExamenGinecologiaList();
            List<String> illegalOrphanMessages = null;
            for (AntecedentesGinecologia antecedentesGinecologiaListOldAntecedentesGinecologia : antecedentesGinecologiaListOld) {
                if (!antecedentesGinecologiaListNew.contains(antecedentesGinecologiaListOldAntecedentesGinecologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AntecedentesGinecologia " + antecedentesGinecologiaListOldAntecedentesGinecologia + " since its expedienteid field is not nullable.");
                }
            }
            for (ExamenColposcopia examenColposcopiaListOldExamenColposcopia : examenColposcopiaListOld) {
                if (!examenColposcopiaListNew.contains(examenColposcopiaListOldExamenColposcopia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenColposcopia " + examenColposcopiaListOldExamenColposcopia + " since its expedienteid field is not nullable.");
                }
            }
            for (ExamenOdontologia examenOdontologiaListOldExamenOdontologia : examenOdontologiaListOld) {
                if (!examenOdontologiaListNew.contains(examenOdontologiaListOldExamenOdontologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenOdontologia " + examenOdontologiaListOldExamenOdontologia + " since its expedienteid field is not nullable.");
                }
            }
            for (AntecedentesOdontologia antecedentesOdontologiaListOldAntecedentesOdontologia : antecedentesOdontologiaListOld) {
                if (!antecedentesOdontologiaListNew.contains(antecedentesOdontologiaListOldAntecedentesOdontologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AntecedentesOdontologia " + antecedentesOdontologiaListOldAntecedentesOdontologia + " since its expedienteid field is not nullable.");
                }
            }
            for (Documentos documentosListOldDocumentos : documentosListOld) {
                if (!documentosListNew.contains(documentosListOldDocumentos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documentos " + documentosListOldDocumentos + " since its expedienteid field is not nullable.");
                }
            }
            for (ExamenGinecologia examenGinecologiaListOldExamenGinecologia : examenGinecologiaListOld) {
                if (!examenGinecologiaListNew.contains(examenGinecologiaListOldExamenGinecologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenGinecologia " + examenGinecologiaListOldExamenGinecologia + " since its expedienteid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pacientecedulaNew != null) {
                pacientecedulaNew = em.getReference(pacientecedulaNew.getClass(), pacientecedulaNew.getCedula());
                expediente.setPacientecedula(pacientecedulaNew);
            }
            List<AntecedentesGinecologia> attachedAntecedentesGinecologiaListNew = new ArrayList<AntecedentesGinecologia>();
            for (AntecedentesGinecologia antecedentesGinecologiaListNewAntecedentesGinecologiaToAttach : antecedentesGinecologiaListNew) {
                antecedentesGinecologiaListNewAntecedentesGinecologiaToAttach = em.getReference(antecedentesGinecologiaListNewAntecedentesGinecologiaToAttach.getClass(), antecedentesGinecologiaListNewAntecedentesGinecologiaToAttach.getId());
                attachedAntecedentesGinecologiaListNew.add(antecedentesGinecologiaListNewAntecedentesGinecologiaToAttach);
            }
            antecedentesGinecologiaListNew = attachedAntecedentesGinecologiaListNew;
            expediente.setAntecedentesGinecologiaList(antecedentesGinecologiaListNew);
            List<ExamenColposcopia> attachedExamenColposcopiaListNew = new ArrayList<ExamenColposcopia>();
            for (ExamenColposcopia examenColposcopiaListNewExamenColposcopiaToAttach : examenColposcopiaListNew) {
                examenColposcopiaListNewExamenColposcopiaToAttach = em.getReference(examenColposcopiaListNewExamenColposcopiaToAttach.getClass(), examenColposcopiaListNewExamenColposcopiaToAttach.getId());
                attachedExamenColposcopiaListNew.add(examenColposcopiaListNewExamenColposcopiaToAttach);
            }
            examenColposcopiaListNew = attachedExamenColposcopiaListNew;
            expediente.setExamenColposcopiaList(examenColposcopiaListNew);
            List<ExamenOdontologia> attachedExamenOdontologiaListNew = new ArrayList<ExamenOdontologia>();
            for (ExamenOdontologia examenOdontologiaListNewExamenOdontologiaToAttach : examenOdontologiaListNew) {
                examenOdontologiaListNewExamenOdontologiaToAttach = em.getReference(examenOdontologiaListNewExamenOdontologiaToAttach.getClass(), examenOdontologiaListNewExamenOdontologiaToAttach.getId());
                attachedExamenOdontologiaListNew.add(examenOdontologiaListNewExamenOdontologiaToAttach);
            }
            examenOdontologiaListNew = attachedExamenOdontologiaListNew;
            expediente.setExamenOdontologiaList(examenOdontologiaListNew);
            List<AntecedentesOdontologia> attachedAntecedentesOdontologiaListNew = new ArrayList<AntecedentesOdontologia>();
            for (AntecedentesOdontologia antecedentesOdontologiaListNewAntecedentesOdontologiaToAttach : antecedentesOdontologiaListNew) {
                antecedentesOdontologiaListNewAntecedentesOdontologiaToAttach = em.getReference(antecedentesOdontologiaListNewAntecedentesOdontologiaToAttach.getClass(), antecedentesOdontologiaListNewAntecedentesOdontologiaToAttach.getId());
                attachedAntecedentesOdontologiaListNew.add(antecedentesOdontologiaListNewAntecedentesOdontologiaToAttach);
            }
            antecedentesOdontologiaListNew = attachedAntecedentesOdontologiaListNew;
            expediente.setAntecedentesOdontologiaList(antecedentesOdontologiaListNew);
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
            if (pacientecedulaOld != null && !pacientecedulaOld.equals(pacientecedulaNew)) {
                pacientecedulaOld.getExpedienteList().remove(expediente);
                pacientecedulaOld = em.merge(pacientecedulaOld);
            }
            if (pacientecedulaNew != null && !pacientecedulaNew.equals(pacientecedulaOld)) {
                pacientecedulaNew.getExpedienteList().add(expediente);
                pacientecedulaNew = em.merge(pacientecedulaNew);
            }
            for (AntecedentesGinecologia antecedentesGinecologiaListNewAntecedentesGinecologia : antecedentesGinecologiaListNew) {
                if (!antecedentesGinecologiaListOld.contains(antecedentesGinecologiaListNewAntecedentesGinecologia)) {
                    Expediente oldExpedienteidOfAntecedentesGinecologiaListNewAntecedentesGinecologia = antecedentesGinecologiaListNewAntecedentesGinecologia.getExpedienteid();
                    antecedentesGinecologiaListNewAntecedentesGinecologia.setExpedienteid(expediente);
                    antecedentesGinecologiaListNewAntecedentesGinecologia = em.merge(antecedentesGinecologiaListNewAntecedentesGinecologia);
                    if (oldExpedienteidOfAntecedentesGinecologiaListNewAntecedentesGinecologia != null && !oldExpedienteidOfAntecedentesGinecologiaListNewAntecedentesGinecologia.equals(expediente)) {
                        oldExpedienteidOfAntecedentesGinecologiaListNewAntecedentesGinecologia.getAntecedentesGinecologiaList().remove(antecedentesGinecologiaListNewAntecedentesGinecologia);
                        oldExpedienteidOfAntecedentesGinecologiaListNewAntecedentesGinecologia = em.merge(oldExpedienteidOfAntecedentesGinecologiaListNewAntecedentesGinecologia);
                    }
                }
            }
            for (ExamenColposcopia examenColposcopiaListNewExamenColposcopia : examenColposcopiaListNew) {
                if (!examenColposcopiaListOld.contains(examenColposcopiaListNewExamenColposcopia)) {
                    Expediente oldExpedienteidOfExamenColposcopiaListNewExamenColposcopia = examenColposcopiaListNewExamenColposcopia.getExpedienteid();
                    examenColposcopiaListNewExamenColposcopia.setExpedienteid(expediente);
                    examenColposcopiaListNewExamenColposcopia = em.merge(examenColposcopiaListNewExamenColposcopia);
                    if (oldExpedienteidOfExamenColposcopiaListNewExamenColposcopia != null && !oldExpedienteidOfExamenColposcopiaListNewExamenColposcopia.equals(expediente)) {
                        oldExpedienteidOfExamenColposcopiaListNewExamenColposcopia.getExamenColposcopiaList().remove(examenColposcopiaListNewExamenColposcopia);
                        oldExpedienteidOfExamenColposcopiaListNewExamenColposcopia = em.merge(oldExpedienteidOfExamenColposcopiaListNewExamenColposcopia);
                    }
                }
            }
            for (ExamenOdontologia examenOdontologiaListNewExamenOdontologia : examenOdontologiaListNew) {
                if (!examenOdontologiaListOld.contains(examenOdontologiaListNewExamenOdontologia)) {
                    Expediente oldExpedienteidOfExamenOdontologiaListNewExamenOdontologia = examenOdontologiaListNewExamenOdontologia.getExpedienteid();
                    examenOdontologiaListNewExamenOdontologia.setExpedienteid(expediente);
                    examenOdontologiaListNewExamenOdontologia = em.merge(examenOdontologiaListNewExamenOdontologia);
                    if (oldExpedienteidOfExamenOdontologiaListNewExamenOdontologia != null && !oldExpedienteidOfExamenOdontologiaListNewExamenOdontologia.equals(expediente)) {
                        oldExpedienteidOfExamenOdontologiaListNewExamenOdontologia.getExamenOdontologiaList().remove(examenOdontologiaListNewExamenOdontologia);
                        oldExpedienteidOfExamenOdontologiaListNewExamenOdontologia = em.merge(oldExpedienteidOfExamenOdontologiaListNewExamenOdontologia);
                    }
                }
            }
            for (AntecedentesOdontologia antecedentesOdontologiaListNewAntecedentesOdontologia : antecedentesOdontologiaListNew) {
                if (!antecedentesOdontologiaListOld.contains(antecedentesOdontologiaListNewAntecedentesOdontologia)) {
                    Expediente oldExpedienteidOfAntecedentesOdontologiaListNewAntecedentesOdontologia = antecedentesOdontologiaListNewAntecedentesOdontologia.getExpedienteid();
                    antecedentesOdontologiaListNewAntecedentesOdontologia.setExpedienteid(expediente);
                    antecedentesOdontologiaListNewAntecedentesOdontologia = em.merge(antecedentesOdontologiaListNewAntecedentesOdontologia);
                    if (oldExpedienteidOfAntecedentesOdontologiaListNewAntecedentesOdontologia != null && !oldExpedienteidOfAntecedentesOdontologiaListNewAntecedentesOdontologia.equals(expediente)) {
                        oldExpedienteidOfAntecedentesOdontologiaListNewAntecedentesOdontologia.getAntecedentesOdontologiaList().remove(antecedentesOdontologiaListNewAntecedentesOdontologia);
                        oldExpedienteidOfAntecedentesOdontologiaListNewAntecedentesOdontologia = em.merge(oldExpedienteidOfAntecedentesOdontologiaListNewAntecedentesOdontologia);
                    }
                }
            }
            for (Documentos documentosListNewDocumentos : documentosListNew) {
                if (!documentosListOld.contains(documentosListNewDocumentos)) {
                    Expediente oldExpedienteidOfDocumentosListNewDocumentos = documentosListNewDocumentos.getExpedienteid();
                    documentosListNewDocumentos.setExpedienteid(expediente);
                    documentosListNewDocumentos = em.merge(documentosListNewDocumentos);
                    if (oldExpedienteidOfDocumentosListNewDocumentos != null && !oldExpedienteidOfDocumentosListNewDocumentos.equals(expediente)) {
                        oldExpedienteidOfDocumentosListNewDocumentos.getDocumentosList().remove(documentosListNewDocumentos);
                        oldExpedienteidOfDocumentosListNewDocumentos = em.merge(oldExpedienteidOfDocumentosListNewDocumentos);
                    }
                }
            }
            for (ExamenGinecologia examenGinecologiaListNewExamenGinecologia : examenGinecologiaListNew) {
                if (!examenGinecologiaListOld.contains(examenGinecologiaListNewExamenGinecologia)) {
                    Expediente oldExpedienteidOfExamenGinecologiaListNewExamenGinecologia = examenGinecologiaListNewExamenGinecologia.getExpedienteid();
                    examenGinecologiaListNewExamenGinecologia.setExpedienteid(expediente);
                    examenGinecologiaListNewExamenGinecologia = em.merge(examenGinecologiaListNewExamenGinecologia);
                    if (oldExpedienteidOfExamenGinecologiaListNewExamenGinecologia != null && !oldExpedienteidOfExamenGinecologiaListNewExamenGinecologia.equals(expediente)) {
                        oldExpedienteidOfExamenGinecologiaListNewExamenGinecologia.getExamenGinecologiaList().remove(examenGinecologiaListNewExamenGinecologia);
                        oldExpedienteidOfExamenGinecologiaListNewExamenGinecologia = em.merge(oldExpedienteidOfExamenGinecologiaListNewExamenGinecologia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = expediente.getId();
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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<AntecedentesGinecologia> antecedentesGinecologiaListOrphanCheck = expediente.getAntecedentesGinecologiaList();
            for (AntecedentesGinecologia antecedentesGinecologiaListOrphanCheckAntecedentesGinecologia : antecedentesGinecologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the AntecedentesGinecologia " + antecedentesGinecologiaListOrphanCheckAntecedentesGinecologia + " in its antecedentesGinecologiaList field has a non-nullable expedienteid field.");
            }
            List<ExamenColposcopia> examenColposcopiaListOrphanCheck = expediente.getExamenColposcopiaList();
            for (ExamenColposcopia examenColposcopiaListOrphanCheckExamenColposcopia : examenColposcopiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the ExamenColposcopia " + examenColposcopiaListOrphanCheckExamenColposcopia + " in its examenColposcopiaList field has a non-nullable expedienteid field.");
            }
            List<ExamenOdontologia> examenOdontologiaListOrphanCheck = expediente.getExamenOdontologiaList();
            for (ExamenOdontologia examenOdontologiaListOrphanCheckExamenOdontologia : examenOdontologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the ExamenOdontologia " + examenOdontologiaListOrphanCheckExamenOdontologia + " in its examenOdontologiaList field has a non-nullable expedienteid field.");
            }
            List<AntecedentesOdontologia> antecedentesOdontologiaListOrphanCheck = expediente.getAntecedentesOdontologiaList();
            for (AntecedentesOdontologia antecedentesOdontologiaListOrphanCheckAntecedentesOdontologia : antecedentesOdontologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the AntecedentesOdontologia " + antecedentesOdontologiaListOrphanCheckAntecedentesOdontologia + " in its antecedentesOdontologiaList field has a non-nullable expedienteid field.");
            }
            List<Documentos> documentosListOrphanCheck = expediente.getDocumentosList();
            for (Documentos documentosListOrphanCheckDocumentos : documentosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the Documentos " + documentosListOrphanCheckDocumentos + " in its documentosList field has a non-nullable expedienteid field.");
            }
            List<ExamenGinecologia> examenGinecologiaListOrphanCheck = expediente.getExamenGinecologiaList();
            for (ExamenGinecologia examenGinecologiaListOrphanCheckExamenGinecologia : examenGinecologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the ExamenGinecologia " + examenGinecologiaListOrphanCheckExamenGinecologia + " in its examenGinecologiaList field has a non-nullable expedienteid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paciente pacientecedula = expediente.getPacientecedula();
            if (pacientecedula != null) {
                pacientecedula.getExpedienteList().remove(expediente);
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

    public Expediente findExpediente(String id) {
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
