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
 * @author Pablo
 */
@Entity
@Table(name = "examen")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Examen.findAll", query = "SELECT e FROM Examen e")
    , @NamedQuery(name = "Examen.findById", query = "SELECT e FROM Examen e WHERE e.id = :id")
    , @NamedQuery(name = "Examen.findByTalla", query = "SELECT e FROM Examen e WHERE e.talla = :talla")
    , @NamedQuery(name = "Examen.findByPeso", query = "SELECT e FROM Examen e WHERE e.peso = :peso")
    , @NamedQuery(name = "Examen.findByPa", query = "SELECT e FROM Examen e WHERE e.pa = :pa")
    , @NamedQuery(name = "Examen.findByCardioPulmonar", query = "SELECT e FROM Examen e WHERE e.cardioPulmonar = :cardioPulmonar")
    , @NamedQuery(name = "Examen.findByVulva", query = "SELECT e FROM Examen e WHERE e.vulva = :vulva")
    , @NamedQuery(name = "Examen.findByBus", query = "SELECT e FROM Examen e WHERE e.bus = :bus")
    , @NamedQuery(name = "Examen.findByVagina", query = "SELECT e FROM Examen e WHERE e.vagina = :vagina")
    , @NamedQuery(name = "Examen.findByCuello", query = "SELECT e FROM Examen e WHERE e.cuello = :cuello")
    , @NamedQuery(name = "Examen.findByUtero", query = "SELECT e FROM Examen e WHERE e.utero = :utero")
    , @NamedQuery(name = "Examen.findByAnexos", query = "SELECT e FROM Examen e WHERE e.anexos = :anexos")
    , @NamedQuery(name = "Examen.findByComentarioGine", query = "SELECT e FROM Examen e WHERE e.comentarioGine = :comentarioGine")
    , @NamedQuery(name = "Examen.findByMotivoConsulta", query = "SELECT e FROM Examen e WHERE e.motivoConsulta = :motivoConsulta")})
public class Examen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "talla")
    private Integer talla;
    @Column(name = "peso")
    private Integer peso;
    @Column(name = "PA")
    private String pa;
    @Column(name = "cardioPulmonar")
    private String cardioPulmonar;
    @Column(name = "vulva")
    private String vulva;
    @Column(name = "bus")
    private String bus;
    @Column(name = "vagina")
    private String vagina;
    @Column(name = "cuello")
    private String cuello;
    @Column(name = "utero")
    private String utero;
    @Column(name = "anexos")
    private String anexos;
    @Column(name = "comentarioGine")
    private String comentarioGine;
    @Column(name = "motivoConsulta")
    private String motivoConsulta;
    @JoinColumn(name = "paciente_id", referencedColumnName = "Paciente_id")
    @ManyToOne(optional = false)
    private AntecedentesGinecologia pacienteId;

    public Examen() {
    }

    public Examen(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTalla() {
        return talla;
    }

    public void setTalla(Integer talla) {
        this.talla = talla;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public String getPa() {
        return pa;
    }

    public void setPa(String pa) {
        this.pa = pa;
    }

    public String getCardioPulmonar() {
        return cardioPulmonar;
    }

    public void setCardioPulmonar(String cardioPulmonar) {
        this.cardioPulmonar = cardioPulmonar;
    }

    public String getVulva() {
        return vulva;
    }

    public void setVulva(String vulva) {
        this.vulva = vulva;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getVagina() {
        return vagina;
    }

    public void setVagina(String vagina) {
        this.vagina = vagina;
    }

    public String getCuello() {
        return cuello;
    }

    public void setCuello(String cuello) {
        this.cuello = cuello;
    }

    public String getUtero() {
        return utero;
    }

    public void setUtero(String utero) {
        this.utero = utero;
    }

    public String getAnexos() {
        return anexos;
    }

    public void setAnexos(String anexos) {
        this.anexos = anexos;
    }

    public String getComentarioGine() {
        return comentarioGine;
    }

    public void setComentarioGine(String comentarioGine) {
        this.comentarioGine = comentarioGine;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public AntecedentesGinecologia getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(AntecedentesGinecologia pacienteId) {
        this.pacienteId = pacienteId;
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
        if (!(object instanceof Examen)) {
            return false;
        }
        Examen other = (Examen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Examen[ id=" + id + " ]";
    }
    
}
