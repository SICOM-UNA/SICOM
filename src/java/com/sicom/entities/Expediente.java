/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author WVQ
 */
@Entity
@Table(name = "expediente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expediente.findAll", query = "SELECT e FROM Expediente e"),
    @NamedQuery(name = "Expediente.findById", query = "SELECT e FROM Expediente e WHERE e.expedientePK.id = :id"),
    @NamedQuery(name = "Expediente.findByPacientecedula", query = "SELECT e FROM Expediente e WHERE e.expedientePK.pacientecedula = :pacientecedula")})
public class Expediente implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExpedientePK expedientePK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacienteCedula")
    private List<AntecedentesGinecologia> antecedentesGinecologiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacienteCedula")
    private List<ExamenColposcopia> examenColposcopiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacienteCedula")
    private List<ExamenOdontologia> examenOdontologiaList;
    @JoinColumn(name = "Paciente_cedula", referencedColumnName = "cedula", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Paciente paciente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacienteCedula")
    private List<AntecedentesOdontologia> antecedentesOdontologiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacienteCedula")
    private List<Documentos> documentosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacienteCedula")
    private List<ExamenGinecologia> examenGinecologiaList;

    public Expediente() {
        paciente = new Paciente();
    }

    public Expediente(ExpedientePK expedientePK) {
        this.expedientePK = expedientePK;
    }

    public Expediente(int id, String pacientecedula) {
        this.expedientePK = new ExpedientePK(id, pacientecedula);
    }

    public ExpedientePK getExpedientePK() {
        return expedientePK;
    }

    public void setExpedientePK(ExpedientePK expedientePK) {
        this.expedientePK = expedientePK;
    }

    @XmlTransient
    public List<AntecedentesGinecologia> getAntecedentesGinecologiaList() {
        return antecedentesGinecologiaList;
    }

    public void setAntecedentesGinecologiaList(List<AntecedentesGinecologia> antecedentesGinecologiaList) {
        this.antecedentesGinecologiaList = antecedentesGinecologiaList;
    }

    @XmlTransient
    public List<ExamenColposcopia> getExamenColposcopiaList() {
        return examenColposcopiaList;
    }

    public void setExamenColposcopiaList(List<ExamenColposcopia> examenColposcopiaList) {
        this.examenColposcopiaList = examenColposcopiaList;
    }

    @XmlTransient
    public List<ExamenOdontologia> getExamenOdontologiaList() {
        return examenOdontologiaList;
    }

    public void setExamenOdontologiaList(List<ExamenOdontologia> examenOdontologiaList) {
        this.examenOdontologiaList = examenOdontologiaList;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @XmlTransient
    public List<AntecedentesOdontologia> getAntecedentesOdontologiaList() {
        return antecedentesOdontologiaList;
    }

    public void setAntecedentesOdontologiaList(List<AntecedentesOdontologia> antecedentesOdontologiaList) {
        this.antecedentesOdontologiaList = antecedentesOdontologiaList;
    }

    @XmlTransient
    public List<Documentos> getDocumentosList() {
        return documentosList;
    }

    public void setDocumentosList(List<Documentos> documentosList) {
        this.documentosList = documentosList;
    }

    @XmlTransient
    public List<ExamenGinecologia> getExamenGinecologiaList() {
        return examenGinecologiaList;
    }

    public void setExamenGinecologiaList(List<ExamenGinecologia> examenGinecologiaList) {
        this.examenGinecologiaList = examenGinecologiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expedientePK != null ? expedientePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Expediente)) {
            return false;
        }
        Expediente other = (Expediente) object;
        if ((this.expedientePK == null && other.expedientePK != null) || (this.expedientePK != null && !this.expedientePK.equals(other.expedientePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Expediente[ expedientePK=" + expedientePK + " ]";
    }
    
}
