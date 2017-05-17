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
@Table(name = "monitoreo fetal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MonitoreoFetal.findAll", query = "SELECT m FROM MonitoreoFetal m"),
    @NamedQuery(name = "MonitoreoFetal.findById", query = "SELECT m FROM MonitoreoFetal m WHERE m.id = :id"),
    @NamedQuery(name = "MonitoreoFetal.findByFecha", query = "SELECT m FROM MonitoreoFetal m WHERE m.fecha = :fecha"),
    @NamedQuery(name = "MonitoreoFetal.findByTipo", query = "SELECT m FROM MonitoreoFetal m WHERE m.tipo = :tipo"),
    @NamedQuery(name = "MonitoreoFetal.findByComentario", query = "SELECT m FROM MonitoreoFetal m WHERE m.comentario = :comentario")})
public class MonitoreoFetal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
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

    public MonitoreoFetal() {
    }

    public MonitoreoFetal(Integer id) {
        this.id = id;
    }

    public MonitoreoFetal(Integer id, Date fecha) {
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
        if (!(object instanceof MonitoreoFetal)) {
            return false;
        }
        MonitoreoFetal other = (MonitoreoFetal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.MonitoreoFetal[ id=" + id + " ]";
    }
    
}
