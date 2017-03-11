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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Pablo
 */
@Entity
@Table(name = "autorizacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Autorizacion.findAll", query = "SELECT a FROM Autorizacion a")
    , @NamedQuery(name = "Autorizacion.findByNivel", query = "SELECT a FROM Autorizacion a WHERE a.nivel = :nivel")
    , @NamedQuery(name = "Autorizacion.findByDescripcion", query = "SELECT a FROM Autorizacion a WHERE a.descripcion = :descripcion")})
public class Autorizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nivel")
    private Integer nivel;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autorizacionnivel")
    private List<Personal> personalList;

    public Autorizacion() {
    }

    public Autorizacion(Integer nivel) {
        this.nivel = nivel;
    }

    public Autorizacion(Integer nivel, String descripcion) {
        this.nivel = nivel;
        this.descripcion = descripcion;
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

    @XmlTransient
    public List<Personal> getPersonalList() {
        return personalList;
    }

    public void setPersonalList(List<Personal> personalList) {
        this.personalList = personalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nivel != null ? nivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Autorizacion)) {
            return false;
        }
        Autorizacion other = (Autorizacion) object;
        if ((this.nivel == null && other.nivel != null) || (this.nivel != null && !this.nivel.equals(other.nivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Autorizacion[ nivel=" + nivel + " ]";
    }
    
}
