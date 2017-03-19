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
@Table(name = "cita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c"),
    @NamedQuery(name = "Cita.findById", query = "SELECT c FROM Cita c WHERE c.id = :id"),
    @NamedQuery(name = "Cita.findByFecha_Inicial", query = "SELECT c FROM Cita c WHERE c.fecha_Inicial = :fecha_Inicial"),
    @NamedQuery(name = "Cita.findByFecha_Final", query = "SELECT c FROM Cita c WHERE c.fecha_Final = :fecha_Final"),
    @NamedQuery(name = "Cita.findByEstado", query = "SELECT c FROM Cita c WHERE c.estado = :estado"),
    @NamedQuery(name = "Cita.findByMotivo", query = "SELECT c FROM Cita c WHERE c.motivo = :motivo"),
    @NamedQuery(name = "Cita.findByNombre", query = "SELECT c FROM Cita c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cita.findByTelefono", query = "SELECT c FROM Cita c WHERE c.telefono = :telefono")})
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha_Inicial")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_Inicial;
    @Column(name = "fecha_Final")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_Final;
    @Column(name = "estado")
    private String estado;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "telefono")
    private String telefono;
    @JoinColumn(name = "Departamento_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamento departamentoid;

    public Cita() {
    }

    public Cita(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha_Inicial() {
        return fecha_Inicial;
    }

    public void setFecha_Inicial(Date fecha_Inicial) {
        this.fecha_Inicial = fecha_Inicial;
    }

    public Date getFecha_Final() {
        return fecha_Final;
    }

    public void setFecha_Final(Date fecha_Final) {
        this.fecha_Final = fecha_Final;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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

    public Departamento getDepartamentoid() {
        return departamentoid;
    }

    public void setDepartamentoid(Departamento departamentoid) {
        this.departamentoid = departamentoid;
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
        if (!(object instanceof Cita)) {
            return false;
        }
        Cita other = (Cita) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Cita[ id=" + id + " ]";
    }
    
}
