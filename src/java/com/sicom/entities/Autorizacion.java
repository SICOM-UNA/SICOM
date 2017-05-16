/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.entities;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author WVQ
 */
@Entity
@Table(name = "autorizacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Autorizacion.findAll", query = "SELECT a FROM Autorizacion a"),
    @NamedQuery(name = "Autorizacion.findById", query = "SELECT a FROM Autorizacion a WHERE a.id = :id"),
    @NamedQuery(name = "Autorizacion.findByNivel", query = "SELECT a FROM Autorizacion a WHERE a.nivel = :nivel"),
    @NamedQuery(name = "Autorizacion.findByDescripcion", query = "SELECT a FROM Autorizacion a WHERE a.descripcion = :descripcion")})
public class Autorizacion implements Serializable {

    private static final long serialVersionUnivel = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nivel")
    private Integer nivel;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "Personal_cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Personal personalCedula;

    public Autorizacion() {
    }

    public Autorizacion(Integer nivel) {
        this.nivel = nivel;
    }

    public Autorizacion(Integer nivel, String descripcion, Personal personal) {
        this.nivel = nivel;
        this.descripcion = descripcion;
        this.personalCedula = personal;
    }

    public Autorizacion(int i, String jefe, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof Autorizacion)) {
            return false;
        }
        
        Autorizacion other = (Autorizacion) object;
        
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Autorizacion[ id=" + id + " ]";
    }
}
