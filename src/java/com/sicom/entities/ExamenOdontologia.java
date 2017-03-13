/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author WVQ
 */
@Entity
@Table(name = "examenodontologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExamenOdontologia.findAll", query = "SELECT e FROM ExamenOdontologia e"),
    @NamedQuery(name = "ExamenOdontologia.findById", query = "SELECT e FROM ExamenOdontologia e WHERE e.id = :id"),
    @NamedQuery(name = "ExamenOdontologia.findByFecha", query = "SELECT e FROM ExamenOdontologia e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "ExamenOdontologia.findByMotivoConsulta", query = "SELECT e FROM ExamenOdontologia e WHERE e.motivoConsulta = :motivoConsulta")})
public class ExamenOdontologia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "Motivo_Consulta")
    private String motivoConsulta;
    @Basic(optional = false)
    @Lob
    @Column(name = "imagen")
    private byte[] imagen;
    @JoinColumn(name = "Expediente_Paciente_cedula", referencedColumnName = "Paciente_cedula")
    @ManyToOne(optional = false)
    private Expediente expedientePacienteCedula;
    @JoinColumn(name = "Personal_cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Personal personalCedula;

    public ExamenOdontologia() {
        personalCedula = new Personal();
    }

    public ExamenOdontologia(Integer id) {
        this.id = id;
    }

    public ExamenOdontologia(Integer id, Date fecha, byte[] imagen) {
        this.id = id;
        this.fecha = fecha;
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public Expediente getExpedientePacienteCedula() {
        return expedientePacienteCedula;
    }

    public void setExpedientePacienteCedula(Expediente expedientePacienteCedula) {
        this.expedientePacienteCedula = expedientePacienteCedula;
    }

    public Personal getPersonalCedula() {
        return personalCedula;
    }

    public void setPersonalCedula(Personal personalCedula) {
        this.personalCedula = personalCedula;
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
        if (!(object instanceof ExamenOdontologia)) {
            return false;
        }
        ExamenOdontologia other = (ExamenOdontologia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.ExamenOdontologia[ id=" + id + " ]";
    }
    
}
