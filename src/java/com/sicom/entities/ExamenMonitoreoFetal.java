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
@Table(name = "examenmonitoreofetal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExamenMonitoreoFetal.findAll", query = "SELECT m FROM ExamenMonitoreoFetal m"),
    @NamedQuery(name = "ExamenMonitoreoFetal.findById", query = "SELECT m FROM ExamenMonitoreoFetal m WHERE m.id = :id"),
    @NamedQuery(name = "ExamenMonitoreoFetal.findByFecha", query = "SELECT m FROM ExamenMonitoreoFetal m WHERE m.fecha = :fecha"),
    @NamedQuery(name = "ExamenMonitoreoFetal.findByTipo", query = "SELECT m FROM ExamenMonitoreoFetal m WHERE m.tipo = :tipo"),
    @NamedQuery(name = "ExamenMonitoreoFetal.findByComentario", query = "SELECT m FROM ExamenMonitoreoFetal m WHERE m.comentario = :comentario")})
public class ExamenMonitoreoFetal implements Serializable {

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
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "comentario")
    private String comentario;
    @JoinColumn(name = "Expediente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Expediente expedienteid;
    @JoinColumn(name = "Personal_cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Personal personalcedula;

    public ExamenMonitoreoFetal() {
    }

    public ExamenMonitoreoFetal(Integer id) {
        this.id = id;
    }

    public ExamenMonitoreoFetal(Integer id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Expediente getExpedienteid() {
        return expedienteid;
    }

    public void setExpedienteid(Expediente expedienteid) {
        this.expedienteid = expedienteid;
    }

    public Personal getPersonalcedula() {
        return personalcedula;
    }

    public void setPersonalcedula(Personal personalcedula) {
        this.personalcedula = personalcedula;
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
        if (!(object instanceof ExamenMonitoreoFetal)) {
            return false;
        }
        ExamenMonitoreoFetal other = (ExamenMonitoreoFetal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.ExamenMonitoreoFetal[ id=" + id + " ]";
    }
}