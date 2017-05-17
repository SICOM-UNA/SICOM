/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author Pablo
 */
@Entity
@Table(name = "expediente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expediente.findAll", query = "SELECT e FROM Expediente e")
    , @NamedQuery(name = "Expediente.findById", query = "SELECT e FROM Expediente e WHERE e.id = :id")})
public class Expediente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "expedientePacientecedula")
    private AntecedentesGinecologia antecedentesGinecologia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacientecedula")
    private List<ExamenColposcopia> examenColposcopiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedienteid")
    private List<MonitoreoFetal> monitoreoFetalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacientecedula")
    private List<ExamenOdontologia> examenOdontologiaList;
    @JoinColumn(name = "Paciente_cedula", referencedColumnName = "cedula")
    @OneToOne(optional = false)
    private Paciente pacientecedula;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "expedientePacientecedula")
    private AntecedentesOdontologia antecedentesOdontologia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacientecedula")
    private List<Documentos> documentosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expedientePacientecedula")
    private List<ExamenGinecologia> examenGinecologiaList;

    public Expediente() {
    }

    public Expediente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AntecedentesGinecologia getAntecedentesGinecologia() {
        return antecedentesGinecologia;
    }

    public void setAntecedentesGinecologia(AntecedentesGinecologia antecedentesGinecologia) {
        this.antecedentesGinecologia = antecedentesGinecologia;
    }

    @XmlTransient
    public List<ExamenColposcopia> getExamenColposcopiaList() {
        return examenColposcopiaList;
    }

    public void setExamenColposcopiaList(List<ExamenColposcopia> examenColposcopiaList) {
        this.examenColposcopiaList = examenColposcopiaList;
    }

    @XmlTransient
    public List<MonitoreoFetal> getMonitoreoFetalList() {
        return monitoreoFetalList;
    }

    public void setMonitoreoFetalList(List<MonitoreoFetal> monitoreoFetalList) {
        this.monitoreoFetalList = monitoreoFetalList;
    }
    
    @XmlTransient
    public List<ExamenOdontologia> getExamenOdontologiaList() {
        return examenOdontologiaList;
    }

    public void setExamenOdontologiaList(List<ExamenOdontologia> examenOdontologiaList) {
        this.examenOdontologiaList = examenOdontologiaList;
    }

    public Paciente getPacientecedula() {
        return pacientecedula;
    }

    public void setPacientecedula(Paciente pacientecedula) {
        this.pacientecedula = pacientecedula;
    }

    public AntecedentesOdontologia getAntecedentesOdontologia() {
        return antecedentesOdontologia;
    }

    public void setAntecedentesOdontologia(AntecedentesOdontologia antecedentesOdontologia) {
        this.antecedentesOdontologia = antecedentesOdontologia;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Expediente)) {
            return false;
        }
        Expediente other = (Expediente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Expediente[ id=" + id + " ]";
    }

}
