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
@Table(name = "responsable")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Responsable.findAll", query = "SELECT r FROM Responsable r"),
    @NamedQuery(name = "Responsable.findByCedula", query = "SELECT r FROM Responsable r WHERE r.cedula = :cedula"),
    @NamedQuery(name = "Responsable.findByNombre", query = "SELECT r FROM Responsable r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Responsable.findByTelefono", query = "SELECT r FROM Responsable r WHERE r.telefono = :telefono"),
    @NamedQuery(name = "Responsable.findByOcupacion", query = "SELECT r FROM Responsable r WHERE r.ocupacion = :ocupacion"),
    @NamedQuery(name = "Responsable.findByVinculo", query = "SELECT r FROM Responsable r WHERE r.vinculo = :vinculo")})
public class Responsable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cedula")
    private String cedula;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "ocupacion")
    private String ocupacion;
    @Column(name = "vinculo")
    private String vinculo;
    @JoinColumn(name = "Paciente_cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Paciente pacientecedula;

    public Responsable() {
    }

    public Responsable(String cedula) {
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    public Paciente getPacientecedula() {
        return pacientecedula;
    }

    public void setPacientecedula(Paciente pacientecedula) {
        this.pacientecedula = pacientecedula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedula != null ? cedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Responsable)) {
            return false;
        }
        Responsable other = (Responsable) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Responsable[ cedula=" + cedula + " ]";
    }
    
}
