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
import com.sicom.entities.ExpedientePK;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
 */
public class ExpedienteJpaController implements Serializable {

    public ExpedienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Expediente expediente) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (expediente.getExpedientePK() == null) {
            expediente.setExpedientePK(new ExpedientePK());
        }
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
        expediente.getExpedientePK().setPacienteCedula(expediente.getPaciente().getCedula());
        List<String> illegalOrphanMessages = null;
        Paciente pacienteOrphanCheck = expediente.getPaciente();
        if (pacienteOrphanCheck != null) {
            Expediente oldExpedienteOfPaciente = pacienteOrphanCheck.getExpediente();
            if (oldExpedienteOfPaciente != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Paciente " + pacienteOrphanCheck + " already has an item of type Expediente whose paciente column cannot be null. Please make another selection for the paciente field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paciente paciente = expediente.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getCedula());
                expediente.setPaciente(paciente);
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
            if (paciente != null) {
                paciente.setExpediente(expediente);
                paciente = em.merge(paciente);
            }
            for (AntecedentesGinecologia antecedentesGinecologiaListAntecedentesGinecologia : expediente.getAntecedentesGinecologiaList()) {
                Expediente oldExpedientePacienteCedulaOfAntecedentesGinecologiaListAntecedentesGinecologia = antecedentesGinecologiaListAntecedentesGinecologia.getExpedientePacienteCedula();
                antecedentesGinecologiaListAntecedentesGinecologia.setExpedientePacienteCedula(expediente);
                antecedentesGinecologiaListAntecedentesGinecologia = em.merge(antecedentesGinecologiaListAntecedentesGinecologia);
                if (oldExpedientePacienteCedulaOfAntecedentesGinecologiaListAntecedentesGinecologia != null) {
                    oldExpedientePacienteCedulaOfAntecedentesGinecologiaListAntecedentesGinecologia.getAntecedentesGinecologiaList().remove(antecedentesGinecologiaListAntecedentesGinecologia);
                    oldExpedientePacienteCedulaOfAntecedentesGinecologiaListAntecedentesGinecologia = em.merge(oldExpedientePacienteCedulaOfAntecedentesGinecologiaListAntecedentesGinecologia);
                }
            }
            for (ExamenColposcopia examenColposcopiaListExamenColposcopia : expediente.getExamenColposcopiaList()) {
                Expediente oldExpedientePacienteCedulaOfExamenColposcopiaListExamenColposcopia = examenColposcopiaListExamenColposcopia.getExpedientePacienteCedula();
                examenColposcopiaListExamenColposcopia.setExpedientePacienteCedula(expediente);
                examenColposcopiaListExamenColposcopia = em.merge(examenColposcopiaListExamenColposcopia);
                if (oldExpedientePacienteCedulaOfExamenColposcopiaListExamenColposcopia != null) {
                    oldExpedientePacienteCedulaOfExamenColposcopiaListExamenColposcopia.getExamenColposcopiaList().remove(examenColposcopiaListExamenColposcopia);
                    oldExpedientePacienteCedulaOfExamenColposcopiaListExamenColposcopia = em.merge(oldExpedientePacienteCedulaOfExamenColposcopiaListExamenColposcopia);
                }
            }
            for (ExamenOdontologia examenOdontologiaListExamenOdontologia : expediente.getExamenOdontologiaList()) {
                Expediente oldExpedientePacienteCedulaOfExamenOdontologiaListExamenOdontologia = examenOdontologiaListExamenOdontologia.getExpedientePacienteCedula();
                examenOdontologiaListExamenOdontologia.setExpedientePacienteCedula(expediente);
                examenOdontologiaListExamenOdontologia = em.merge(examenOdontologiaListExamenOdontologia);
                if (oldExpedientePacienteCedulaOfExamenOdontologiaListExamenOdontologia != null) {
                    oldExpedientePacienteCedulaOfExamenOdontologiaListExamenOdontologia.getExamenOdontologiaList().remove(examenOdontologiaListExamenOdontologia);
                    oldExpedientePacienteCedulaOfExamenOdontologiaListExamenOdontologia = em.merge(oldExpedientePacienteCedulaOfExamenOdontologiaListExamenOdontologia);
                }
            }
            for (AntecedentesOdontologia antecedentesOdontologiaListAntecedentesOdontologia : expediente.getAntecedentesOdontologiaList()) {
                Expediente oldExpedientePacienteCedulaOfAntecedentesOdontologiaListAntecedentesOdontologia = antecedentesOdontologiaListAntecedentesOdontologia.getExpedientePacienteCedula();
                antecedentesOdontologiaListAntecedentesOdontologia.setExpedientePacienteCedula(expediente);
                antecedentesOdontologiaListAntecedentesOdontologia = em.merge(antecedentesOdontologiaListAntecedentesOdontologia);
                if (oldExpedientePacienteCedulaOfAntecedentesOdontologiaListAntecedentesOdontologia != null) {
                    oldExpedientePacienteCedulaOfAntecedentesOdontologiaListAntecedentesOdontologia.getAntecedentesOdontologiaList().remove(antecedentesOdontologiaListAntecedentesOdontologia);
                    oldExpedientePacienteCedulaOfAntecedentesOdontologiaListAntecedentesOdontologia = em.merge(oldExpedientePacienteCedulaOfAntecedentesOdontologiaListAntecedentesOdontologia);
                }
            }
            for (Documentos documentosListDocumentos : expediente.getDocumentosList()) {
                Expediente oldExpedientePacienteCedulaOfDocumentosListDocumentos = documentosListDocumentos.getExpedientePacienteCedula();
                documentosListDocumentos.setExpedientePacienteCedula(expediente);
                documentosListDocumentos = em.merge(documentosListDocumentos);
                if (oldExpedientePacienteCedulaOfDocumentosListDocumentos != null) {
                    oldExpedientePacienteCedulaOfDocumentosListDocumentos.getDocumentosList().remove(documentosListDocumentos);
                    oldExpedientePacienteCedulaOfDocumentosListDocumentos = em.merge(oldExpedientePacienteCedulaOfDocumentosListDocumentos);
                }
            }
            for (ExamenGinecologia examenGinecologiaListExamenGinecologia : expediente.getExamenGinecologiaList()) {
                Expediente oldExpedientePacienteCedulaOfExamenGinecologiaListExamenGinecologia = examenGinecologiaListExamenGinecologia.getExpedientePacienteCedula();
                examenGinecologiaListExamenGinecologia.setExpedientePacienteCedula(expediente);
                examenGinecologiaListExamenGinecologia = em.merge(examenGinecologiaListExamenGinecologia);
                if (oldExpedientePacienteCedulaOfExamenGinecologiaListExamenGinecologia != null) {
                    oldExpedientePacienteCedulaOfExamenGinecologiaListExamenGinecologia.getExamenGinecologiaList().remove(examenGinecologiaListExamenGinecologia);
                    oldExpedientePacienteCedulaOfExamenGinecologiaListExamenGinecologia = em.merge(oldExpedientePacienteCedulaOfExamenGinecologiaListExamenGinecologia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findExpediente(expediente.getExpedientePK()) != null) {
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
        expediente.getExpedientePK().setPacienteCedula(expediente.getPaciente().getCedula());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente persistentExpediente = em.find(Expediente.class, expediente.getExpedientePK());
            Paciente pacienteOld = persistentExpediente.getPaciente();
            Paciente pacienteNew = expediente.getPaciente();
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
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                Expediente oldExpedienteOfPaciente = pacienteNew.getExpediente();
                if (oldExpedienteOfPaciente != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Paciente " + pacienteNew + " already has an item of type Expediente whose paciente column cannot be null. Please make another selection for the paciente field.");
                }
            }
            for (AntecedentesGinecologia antecedentesGinecologiaListOldAntecedentesGinecologia : antecedentesGinecologiaListOld) {
                if (!antecedentesGinecologiaListNew.contains(antecedentesGinecologiaListOldAntecedentesGinecologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AntecedentesGinecologia " + antecedentesGinecologiaListOldAntecedentesGinecologia + " since its expedientePacienteCedula field is not nullable.");
                }
            }
            for (ExamenColposcopia examenColposcopiaListOldExamenColposcopia : examenColposcopiaListOld) {
                if (!examenColposcopiaListNew.contains(examenColposcopiaListOldExamenColposcopia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenColposcopia " + examenColposcopiaListOldExamenColposcopia + " since its expedientePacienteCedula field is not nullable.");
                }
            }
            for (ExamenOdontologia examenOdontologiaListOldExamenOdontologia : examenOdontologiaListOld) {
                if (!examenOdontologiaListNew.contains(examenOdontologiaListOldExamenOdontologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenOdontologia " + examenOdontologiaListOldExamenOdontologia + " since its expedientePacienteCedula field is not nullable.");
                }
            }
            for (AntecedentesOdontologia antecedentesOdontologiaListOldAntecedentesOdontologia : antecedentesOdontologiaListOld) {
                if (!antecedentesOdontologiaListNew.contains(antecedentesOdontologiaListOldAntecedentesOdontologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AntecedentesOdontologia " + antecedentesOdontologiaListOldAntecedentesOdontologia + " since its expedientePacienteCedula field is not nullable.");
                }
            }
            for (Documentos documentosListOldDocumentos : documentosListOld) {
                if (!documentosListNew.contains(documentosListOldDocumentos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documentos " + documentosListOldDocumentos + " since its expedientePacienteCedula field is not nullable.");
                }
            }
            for (ExamenGinecologia examenGinecologiaListOldExamenGinecologia : examenGinecologiaListOld) {
                if (!examenGinecologiaListNew.contains(examenGinecologiaListOldExamenGinecologia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenGinecologia " + examenGinecologiaListOldExamenGinecologia + " since its expedientePacienteCedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getCedula());
                expediente.setPaciente(pacienteNew);
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
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.setExpediente(null);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                pacienteNew.setExpediente(expediente);
                pacienteNew = em.merge(pacienteNew);
            }
            for (AntecedentesGinecologia antecedentesGinecologiaListNewAntecedentesGinecologia : antecedentesGinecologiaListNew) {
                if (!antecedentesGinecologiaListOld.contains(antecedentesGinecologiaListNewAntecedentesGinecologia)) {
                    Expediente oldExpedientePacienteCedulaOfAntecedentesGinecologiaListNewAntecedentesGinecologia = antecedentesGinecologiaListNewAntecedentesGinecologia.getExpedientePacienteCedula();
                    antecedentesGinecologiaListNewAntecedentesGinecologia.setExpedientePacienteCedula(expediente);
                    antecedentesGinecologiaListNewAntecedentesGinecologia = em.merge(antecedentesGinecologiaListNewAntecedentesGinecologia);
                    if (oldExpedientePacienteCedulaOfAntecedentesGinecologiaListNewAntecedentesGinecologia != null && !oldExpedientePacienteCedulaOfAntecedentesGinecologiaListNewAntecedentesGinecologia.equals(expediente)) {
                        oldExpedientePacienteCedulaOfAntecedentesGinecologiaListNewAntecedentesGinecologia.getAntecedentesGinecologiaList().remove(antecedentesGinecologiaListNewAntecedentesGinecologia);
                        oldExpedientePacienteCedulaOfAntecedentesGinecologiaListNewAntecedentesGinecologia = em.merge(oldExpedientePacienteCedulaOfAntecedentesGinecologiaListNewAntecedentesGinecologia);
                    }
                }
            }
            for (ExamenColposcopia examenColposcopiaListNewExamenColposcopia : examenColposcopiaListNew) {
                if (!examenColposcopiaListOld.contains(examenColposcopiaListNewExamenColposcopia)) {
                    Expediente oldExpedientePacienteCedulaOfExamenColposcopiaListNewExamenColposcopia = examenColposcopiaListNewExamenColposcopia.getExpedientePacienteCedula();
                    examenColposcopiaListNewExamenColposcopia.setExpedientePacienteCedula(expediente);
                    examenColposcopiaListNewExamenColposcopia = em.merge(examenColposcopiaListNewExamenColposcopia);
                    if (oldExpedientePacienteCedulaOfExamenColposcopiaListNewExamenColposcopia != null && !oldExpedientePacienteCedulaOfExamenColposcopiaListNewExamenColposcopia.equals(expediente)) {
                        oldExpedientePacienteCedulaOfExamenColposcopiaListNewExamenColposcopia.getExamenColposcopiaList().remove(examenColposcopiaListNewExamenColposcopia);
                        oldExpedientePacienteCedulaOfExamenColposcopiaListNewExamenColposcopia = em.merge(oldExpedientePacienteCedulaOfExamenColposcopiaListNewExamenColposcopia);
                    }
                }
            }
            for (ExamenOdontologia examenOdontologiaListNewExamenOdontologia : examenOdontologiaListNew) {
                if (!examenOdontologiaListOld.contains(examenOdontologiaListNewExamenOdontologia)) {
                    Expediente oldExpedientePacienteCedulaOfExamenOdontologiaListNewExamenOdontologia = examenOdontologiaListNewExamenOdontologia.getExpedientePacienteCedula();
                    examenOdontologiaListNewExamenOdontologia.setExpedientePacienteCedula(expediente);
                    examenOdontologiaListNewExamenOdontologia = em.merge(examenOdontologiaListNewExamenOdontologia);
                    if (oldExpedientePacienteCedulaOfExamenOdontologiaListNewExamenOdontologia != null && !oldExpedientePacienteCedulaOfExamenOdontologiaListNewExamenOdontologia.equals(expediente)) {
                        oldExpedientePacienteCedulaOfExamenOdontologiaListNewExamenOdontologia.getExamenOdontologiaList().remove(examenOdontologiaListNewExamenOdontologia);
                        oldExpedientePacienteCedulaOfExamenOdontologiaListNewExamenOdontologia = em.merge(oldExpedientePacienteCedulaOfExamenOdontologiaListNewExamenOdontologia);
                    }
                }
            }
            for (AntecedentesOdontologia antecedentesOdontologiaListNewAntecedentesOdontologia : antecedentesOdontologiaListNew) {
                if (!antecedentesOdontologiaListOld.contains(antecedentesOdontologiaListNewAntecedentesOdontologia)) {
                    Expediente oldExpedientePacienteCedulaOfAntecedentesOdontologiaListNewAntecedentesOdontologia = antecedentesOdontologiaListNewAntecedentesOdontologia.getExpedientePacienteCedula();
                    antecedentesOdontologiaListNewAntecedentesOdontologia.setExpedientePacienteCedula(expediente);
                    antecedentesOdontologiaListNewAntecedentesOdontologia = em.merge(antecedentesOdontologiaListNewAntecedentesOdontologia);
                    if (oldExpedientePacienteCedulaOfAntecedentesOdontologiaListNewAntecedentesOdontologia != null && !oldExpedientePacienteCedulaOfAntecedentesOdontologiaListNewAntecedentesOdontologia.equals(expediente)) {
                        oldExpedientePacienteCedulaOfAntecedentesOdontologiaListNewAntecedentesOdontologia.getAntecedentesOdontologiaList().remove(antecedentesOdontologiaListNewAntecedentesOdontologia);
                        oldExpedientePacienteCedulaOfAntecedentesOdontologiaListNewAntecedentesOdontologia = em.merge(oldExpedientePacienteCedulaOfAntecedentesOdontologiaListNewAntecedentesOdontologia);
                    }
                }
            }
            for (Documentos documentosListNewDocumentos : documentosListNew) {
                if (!documentosListOld.contains(documentosListNewDocumentos)) {
                    Expediente oldExpedientePacienteCedulaOfDocumentosListNewDocumentos = documentosListNewDocumentos.getExpedientePacienteCedula();
                    documentosListNewDocumentos.setExpedientePacienteCedula(expediente);
                    documentosListNewDocumentos = em.merge(documentosListNewDocumentos);
                    if (oldExpedientePacienteCedulaOfDocumentosListNewDocumentos != null && !oldExpedientePacienteCedulaOfDocumentosListNewDocumentos.equals(expediente)) {
                        oldExpedientePacienteCedulaOfDocumentosListNewDocumentos.getDocumentosList().remove(documentosListNewDocumentos);
                        oldExpedientePacienteCedulaOfDocumentosListNewDocumentos = em.merge(oldExpedientePacienteCedulaOfDocumentosListNewDocumentos);
                    }
                }
            }
            for (ExamenGinecologia examenGinecologiaListNewExamenGinecologia : examenGinecologiaListNew) {
                if (!examenGinecologiaListOld.contains(examenGinecologiaListNewExamenGinecologia)) {
                    Expediente oldExpedientePacienteCedulaOfExamenGinecologiaListNewExamenGinecologia = examenGinecologiaListNewExamenGinecologia.getExpedientePacienteCedula();
                    examenGinecologiaListNewExamenGinecologia.setExpedientePacienteCedula(expediente);
                    examenGinecologiaListNewExamenGinecologia = em.merge(examenGinecologiaListNewExamenGinecologia);
                    if (oldExpedientePacienteCedulaOfExamenGinecologiaListNewExamenGinecologia != null && !oldExpedientePacienteCedulaOfExamenGinecologiaListNewExamenGinecologia.equals(expediente)) {
                        oldExpedientePacienteCedulaOfExamenGinecologiaListNewExamenGinecologia.getExamenGinecologiaList().remove(examenGinecologiaListNewExamenGinecologia);
                        oldExpedientePacienteCedulaOfExamenGinecologiaListNewExamenGinecologia = em.merge(oldExpedientePacienteCedulaOfExamenGinecologiaListNewExamenGinecologia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ExpedientePK id = expediente.getExpedientePK();
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

    public void destroy(ExpedientePK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente expediente;
            try {
                expediente = em.getReference(Expediente.class, id);
                expediente.getExpedientePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The expediente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AntecedentesGinecologia> antecedentesGinecologiaListOrphanCheck = expediente.getAntecedentesGinecologiaList();
            for (AntecedentesGinecologia antecedentesGinecologiaListOrphanCheckAntecedentesGinecologia : antecedentesGinecologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the AntecedentesGinecologia " + antecedentesGinecologiaListOrphanCheckAntecedentesGinecologia + " in its antecedentesGinecologiaList field has a non-nullable expedientePacienteCedula field.");
            }
            List<ExamenColposcopia> examenColposcopiaListOrphanCheck = expediente.getExamenColposcopiaList();
            for (ExamenColposcopia examenColposcopiaListOrphanCheckExamenColposcopia : examenColposcopiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the ExamenColposcopia " + examenColposcopiaListOrphanCheckExamenColposcopia + " in its examenColposcopiaList field has a non-nullable expedientePacienteCedula field.");
            }
            List<ExamenOdontologia> examenOdontologiaListOrphanCheck = expediente.getExamenOdontologiaList();
            for (ExamenOdontologia examenOdontologiaListOrphanCheckExamenOdontologia : examenOdontologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the ExamenOdontologia " + examenOdontologiaListOrphanCheckExamenOdontologia + " in its examenOdontologiaList field has a non-nullable expedientePacienteCedula field.");
            }
            List<AntecedentesOdontologia> antecedentesOdontologiaListOrphanCheck = expediente.getAntecedentesOdontologiaList();
            for (AntecedentesOdontologia antecedentesOdontologiaListOrphanCheckAntecedentesOdontologia : antecedentesOdontologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the AntecedentesOdontologia " + antecedentesOdontologiaListOrphanCheckAntecedentesOdontologia + " in its antecedentesOdontologiaList field has a non-nullable expedientePacienteCedula field.");
            }
            List<Documentos> documentosListOrphanCheck = expediente.getDocumentosList();
            for (Documentos documentosListOrphanCheckDocumentos : documentosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the Documentos " + documentosListOrphanCheckDocumentos + " in its documentosList field has a non-nullable expedientePacienteCedula field.");
            }
            List<ExamenGinecologia> examenGinecologiaListOrphanCheck = expediente.getExamenGinecologiaList();
            for (ExamenGinecologia examenGinecologiaListOrphanCheckExamenGinecologia : examenGinecologiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expediente (" + expediente + ") cannot be destroyed since the ExamenGinecologia " + examenGinecologiaListOrphanCheckExamenGinecologia + " in its examenGinecologiaList field has a non-nullable expedientePacienteCedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Paciente paciente = expediente.getPaciente();
            if (paciente != null) {
                paciente.setExpediente(null);
                paciente = em.merge(paciente);
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

    public Expediente findExpediente(ExpedientePK id) {
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
