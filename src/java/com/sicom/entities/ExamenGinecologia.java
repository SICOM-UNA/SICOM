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
@Table(name = "examenginecologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExamenGinecologia.findAll", query = "SELECT e FROM ExamenGinecologia e"),
    @NamedQuery(name = "ExamenGinecologia.findById", query = "SELECT e FROM ExamenGinecologia e WHERE e.id = :id"),
    @NamedQuery(name = "ExamenGinecologia.findByFecha", query = "SELECT e FROM ExamenGinecologia e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "ExamenGinecologia.findByMotivoConsulta", query = "SELECT e FROM ExamenGinecologia e WHERE e.motivoConsulta = :motivoConsulta"),
    @NamedQuery(name = "ExamenGinecologia.findByTalla", query = "SELECT e FROM ExamenGinecologia e WHERE e.talla = :talla"),
    @NamedQuery(name = "ExamenGinecologia.findByPeso", query = "SELECT e FROM ExamenGinecologia e WHERE e.peso = :peso"),
    @NamedQuery(name = "ExamenGinecologia.findByPresionArterial", query = "SELECT e FROM ExamenGinecologia e WHERE e.presionArterial = :presionArterial"),
    @NamedQuery(name = "ExamenGinecologia.findByCardioPulmonar", query = "SELECT e FROM ExamenGinecologia e WHERE e.cardioPulmonar = :cardioPulmonar"),
    @NamedQuery(name = "ExamenGinecologia.findByComentarioCardioPulmonar", query = "SELECT e FROM ExamenGinecologia e WHERE e.comentarioCardioPulmonar = :comentarioCardioPulmonar"),
    @NamedQuery(name = "ExamenGinecologia.findByMamas", query = "SELECT e FROM ExamenGinecologia e WHERE e.mamas = :mamas"),
    @NamedQuery(name = "ExamenGinecologia.findByAbdomen", query = "SELECT e FROM ExamenGinecologia e WHERE e.abdomen = :abdomen"),
    @NamedQuery(name = "ExamenGinecologia.findByVulva", query = "SELECT e FROM ExamenGinecologia e WHERE e.vulva = :vulva"),
    @NamedQuery(name = "ExamenGinecologia.findByBus", query = "SELECT e FROM ExamenGinecologia e WHERE e.bus = :bus"),
    @NamedQuery(name = "ExamenGinecologia.findByComentarioBus", query = "SELECT e FROM ExamenGinecologia e WHERE e.comentarioBus = :comentarioBus"),
    @NamedQuery(name = "ExamenGinecologia.findByVagina", query = "SELECT e FROM ExamenGinecologia e WHERE e.vagina = :vagina"),
    @NamedQuery(name = "ExamenGinecologia.findByCuello", query = "SELECT e FROM ExamenGinecologia e WHERE e.cuello = :cuello"),
    @NamedQuery(name = "ExamenGinecologia.findByUtero", query = "SELECT e FROM ExamenGinecologia e WHERE e.utero = :utero"),
    @NamedQuery(name = "ExamenGinecologia.findByAnexos", query = "SELECT e FROM ExamenGinecologia e WHERE e.anexos = :anexos"),
    @NamedQuery(name = "ExamenGinecologia.findByOtros", query = "SELECT e FROM ExamenGinecologia e WHERE e.otros = :otros"),
    @NamedQuery(name = "ExamenGinecologia.findByAbdomenObstetrico", query = "SELECT e FROM ExamenGinecologia e WHERE e.abdomenObstetrico = :abdomenObstetrico"),
    @NamedQuery(name = "ExamenGinecologia.findByAu", query = "SELECT e FROM ExamenGinecologia e WHERE e.au = :au"),
    @NamedQuery(name = "ExamenGinecologia.findByCf", query = "SELECT e FROM ExamenGinecologia e WHERE e.cf = :cf"),
    @NamedQuery(name = "ExamenGinecologia.findByMf", query = "SELECT e FROM ExamenGinecologia e WHERE e.mf = :mf"),
    @NamedQuery(name = "ExamenGinecologia.findByTu", query = "SELECT e FROM ExamenGinecologia e WHERE e.tu = :tu"),
    @NamedQuery(name = "ExamenGinecologia.findByIdx", query = "SELECT e FROM ExamenGinecologia e WHERE e.idx = :idx"),
    @NamedQuery(name = "ExamenGinecologia.findByTx", query = "SELECT e FROM ExamenGinecologia e WHERE e.tx = :tx")})
public class ExamenGinecologia implements Serializable {

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
    @Column(name = "talla")
    private Integer talla;
    @Column(name = "peso")
    private Integer peso;
    @Column(name = "presionArterial")
    private String presionArterial;
    @Column(name = "cardioPulmonar")
    private String cardioPulmonar;
    @Column(name = "comentarioCardioPulmonar")
    private String comentarioCardioPulmonar;
    @Column(name = "mamas")
    private String mamas;
    @Lob
    @Column(name = "imagenMamas")
    private byte[] imagenMamas;
    @Column(name = "abdomen")
    private String abdomen;
    @Column(name = "vulva")
    private String vulva;
    @Column(name = "bus")
    private Boolean bus;
    @Column(name = "comentarioBus")
    private String comentarioBus;
    @Column(name = "vagina")
    private String vagina;
    @Column(name = "cuello")
    private String cuello;
    @Column(name = "utero")
    private String utero;
    @Column(name = "anexos")
    private String anexos;
    @Column(name = "otros")
    private String otros;
    @Column(name = "abdomenObstetrico")
    private String abdomenObstetrico;
    @Column(name = "AU")
    private Integer au;
    @Column(name = "CF")
    private String cf;
    @Column(name = "MF")
    private Boolean mf;
    @Column(name = "TU")
    private String tu;
    @Column(name = "IDX")
    private String idx;
    @Column(name = "TX")
    private String tx;
    @JoinColumn(name = "Expediente_Paciente_cedula", referencedColumnName = "Paciente_cedula")
    @ManyToOne(optional = false)
    private Expediente expedientePacientecedula;
    @JoinColumn(name = "Personal_cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Personal personalcedula;

    public ExamenGinecologia() {
    }

    public ExamenGinecologia(Integer id) {
        this.id = id;
    }

    public ExamenGinecologia(Integer id, Date fecha) {
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

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
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

    public String getPresionArterial() {
        return presionArterial;
    }

    public void setPresionArterial(String presionArterial) {
        this.presionArterial = presionArterial;
    }

    public String getCardioPulmonar() {
        return cardioPulmonar;
    }

    public void setCardioPulmonar(String cardioPulmonar) {
        this.cardioPulmonar = cardioPulmonar;
    }

    public String getComentarioCardioPulmonar() {
        return comentarioCardioPulmonar;
    }

    public void setComentarioCardioPulmonar(String comentarioCardioPulmonar) {
        this.comentarioCardioPulmonar = comentarioCardioPulmonar;
    }

    public String getMamas() {
        return mamas;
    }

    public void setMamas(String mamas) {
        this.mamas = mamas;
    }

    public byte[] getImagenMamas() {
        return imagenMamas;
    }

    public void setImagenMamas(byte[] imagenMamas) {
        this.imagenMamas = imagenMamas;
    }

    public String getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(String abdomen) {
        this.abdomen = abdomen;
    }

    public String getVulva() {
        return vulva;
    }

    public void setVulva(String vulva) {
        this.vulva = vulva;
    }

    public Boolean getBus() {
        return bus;
    }

    public void setBus(Boolean bus) {
        this.bus = bus;
    }

    public String getComentarioBus() {
        return comentarioBus;
    }

    public void setComentarioBus(String comentarioBus) {
        this.comentarioBus = comentarioBus;
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

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getAbdomenObstetrico() {
        return abdomenObstetrico;
    }

    public void setAbdomenObstetrico(String abdomenObstetrico) {
        this.abdomenObstetrico = abdomenObstetrico;
    }

    public Integer getAu() {
        return au;
    }

    public void setAu(Integer au) {
        this.au = au;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public Boolean getMf() {
        return mf;
    }

    public void setMf(Boolean mf) {
        this.mf = mf;
    }

    public String getTu() {
        return tu;
    }

    public void setTu(String tu) {
        this.tu = tu;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public Expediente getExpedientePacientecedula() {
        return expedientePacientecedula;
    }

    public void setExpedientePacientecedula(Expediente expedientePacientecedula) {
        this.expedientePacientecedula = expedientePacientecedula;
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
        if (!(object instanceof ExamenGinecologia)) {
            return false;
        }
        ExamenGinecologia other = (ExamenGinecologia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.ExamenGinecologia[ id=" + id + " ]";
    }
    
}
