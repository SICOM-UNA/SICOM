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
@Table(name = "antecedentesginecologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecedentesGinecologia.findAll", query = "SELECT a FROM AntecedentesGinecologia a"),
    @NamedQuery(name = "AntecedentesGinecologia.findById", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.id = :id"),
    @NamedQuery(name = "AntecedentesGinecologia.findByFecha", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "AntecedentesGinecologia.findByHerediatarios", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.herediatarios = :herediatarios"),
    @NamedQuery(name = "AntecedentesGinecologia.findByPatologicos", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.patologicos = :patologicos"),
    @NamedQuery(name = "AntecedentesGinecologia.findByNoPatologicos", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.noPatologicos = :noPatologicos"),
    @NamedQuery(name = "AntecedentesGinecologia.findByQuirurgicos", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.quirurgicos = :quirurgicos"),
    @NamedQuery(name = "AntecedentesGinecologia.findByMenarca", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.menarca = :menarca"),
    @NamedQuery(name = "AntecedentesGinecologia.findByFur", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.fur = :fur"),
    @NamedQuery(name = "AntecedentesGinecologia.findByCicloMestrual", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.cicloMestrual = :cicloMestrual"),
    @NamedQuery(name = "AntecedentesGinecologia.findByComentarioCicloMestrual", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.comentarioCicloMestrual = :comentarioCicloMestrual"),
    @NamedQuery(name = "AntecedentesGinecologia.findByPrs", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.prs = :prs"),
    @NamedQuery(name = "AntecedentesGinecologia.findByCompanerosSexuales", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.companerosSexuales = :companerosSexuales"),
    @NamedQuery(name = "AntecedentesGinecologia.findByPlanificacion", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.planificacion = :planificacion"),
    @NamedQuery(name = "AntecedentesGinecologia.findByComentarioPlanificacion", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.comentarioPlanificacion = :comentarioPlanificacion"),
    @NamedQuery(name = "AntecedentesGinecologia.findByActividadSexual", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.actividadSexual = :actividadSexual"),
    @NamedQuery(name = "AntecedentesGinecologia.findByUltimoParto", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.ultimoParto = :ultimoParto"),
    @NamedQuery(name = "AntecedentesGinecologia.findByUltimoPap", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.ultimoPap = :ultimoPap"),
    @NamedQuery(name = "AntecedentesGinecologia.findByLactanciaMaterna", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.lactanciaMaterna = :lactanciaMaterna"),
    @NamedQuery(name = "AntecedentesGinecologia.findByComentarioLactancia", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.comentarioLactancia = :comentarioLactancia"),
    @NamedQuery(name = "AntecedentesGinecologia.findByTipoParto", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.tipoParto = :tipoParto"),
    @NamedQuery(name = "AntecedentesGinecologia.findByMenopausia", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.menopausia = :menopausia"),
    @NamedQuery(name = "AntecedentesGinecologia.findByGesta", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.gesta = :gesta"),
    @NamedQuery(name = "AntecedentesGinecologia.findByPartos", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.partos = :partos"),
    @NamedQuery(name = "AntecedentesGinecologia.findByAbortos", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.abortos = :abortos"),
    @NamedQuery(name = "AntecedentesGinecologia.findByEctopico", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.ectopico = :ectopico"),
    @NamedQuery(name = "AntecedentesGinecologia.findByComentarioGPA", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.comentarioGPA = :comentarioGPA"),
    @NamedQuery(name = "AntecedentesGinecologia.findByInformacionAdicional", query = "SELECT a FROM AntecedentesGinecologia a WHERE a.informacionAdicional = :informacionAdicional")})
public class AntecedentesGinecologia implements Serializable {

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
    @Column(name = "herediatarios")
    private String herediatarios;
    @Column(name = "patologicos")
    private String patologicos;
    @Column(name = "noPatologicos")
    private String noPatologicos;
    @Column(name = "quirurgicos")
    private String quirurgicos;
    @Column(name = "menarca")
    private Integer menarca;
    @Column(name = "FUR")
    @Temporal(TemporalType.DATE)
    private Date fur;
    @Column(name = "cicloMestrual")
    private String cicloMestrual;
    @Column(name = "comentarioCicloMestrual")
    private String comentarioCicloMestrual;
    @Column(name = "PRS")
    private Integer prs;
    @Column(name = "companerosSexuales")
    private Integer companerosSexuales;
    @Column(name = "planificacion")
    private String planificacion;
    @Column(name = "comentarioPlanificacion")
    private String comentarioPlanificacion;
    @Column(name = "actividadSexual")
    private String actividadSexual;
    @Column(name = "ultimoParto")
    @Temporal(TemporalType.DATE)
    private Date ultimoParto;
    @Column(name = "ultimoPap")
    private String ultimoPap;
    @Column(name = "lactanciaMaterna")
    private Boolean lactanciaMaterna;
    @Column(name = "comentarioLactancia")
    private String comentarioLactancia;
    @Column(name = "tipoParto")
    private String tipoParto;
    @Column(name = "menopausia")
    private Integer menopausia;
    @Column(name = "gesta")
    private Integer gesta;
    @Column(name = "partos")
    private Integer partos;
    @Column(name = "abortos")
    private Integer abortos;
    @Column(name = "ectopico")
    private Integer ectopico;
    @Column(name = "comentarioGPA")
    private String comentarioGPA;
    @Column(name = "informacionAdicional")
    private String informacionAdicional;
    @JoinColumn(name = "Expediente_Paciente_cedula", referencedColumnName = "Paciente_cedula")
    @ManyToOne(optional = false)
    private Expediente expedientePacientecedula;

    public AntecedentesGinecologia() {
    }

    public AntecedentesGinecologia(Integer id) {
        this.id = id;
    }

    public AntecedentesGinecologia(Integer id, Date fecha) {
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

    public String getHerediatarios() {
        return herediatarios;
    }

    public void setHerediatarios(String herediatarios) {
        this.herediatarios = herediatarios;
    }

    public String getPatologicos() {
        return patologicos;
    }

    public void setPatologicos(String patologicos) {
        this.patologicos = patologicos;
    }

    public String getNoPatologicos() {
        return noPatologicos;
    }

    public void setNoPatologicos(String noPatologicos) {
        this.noPatologicos = noPatologicos;
    }

    public String getQuirurgicos() {
        return quirurgicos;
    }

    public void setQuirurgicos(String quirurgicos) {
        this.quirurgicos = quirurgicos;
    }

    public Integer getMenarca() {
        return menarca;
    }

    public void setMenarca(Integer menarca) {
        this.menarca = menarca;
    }

    public Date getFur() {
        return fur;
    }

    public void setFur(Date fur) {
        this.fur = fur;
    }

    public String getCicloMestrual() {
        return cicloMestrual;
    }

    public void setCicloMestrual(String cicloMestrual) {
        this.cicloMestrual = cicloMestrual;
    }

    public String getComentarioCicloMestrual() {
        return comentarioCicloMestrual;
    }

    public void setComentarioCicloMestrual(String comentarioCicloMestrual) {
        this.comentarioCicloMestrual = comentarioCicloMestrual;
    }

    public Integer getPrs() {
        return prs;
    }

    public void setPrs(Integer prs) {
        this.prs = prs;
    }

    public Integer getCompanerosSexuales() {
        return companerosSexuales;
    }

    public void setCompanerosSexuales(Integer companerosSexuales) {
        this.companerosSexuales = companerosSexuales;
    }

    public String getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(String planificacion) {
        this.planificacion = planificacion;
    }

    public String getComentarioPlanificacion() {
        return comentarioPlanificacion;
    }

    public void setComentarioPlanificacion(String comentarioPlanificacion) {
        this.comentarioPlanificacion = comentarioPlanificacion;
    }

    public String getActividadSexual() {
        return actividadSexual;
    }

    public void setActividadSexual(String actividadSexual) {
        this.actividadSexual = actividadSexual;
    }

    public Date getUltimoParto() {
        return ultimoParto;
    }

    public void setUltimoParto(Date ultimoParto) {
        this.ultimoParto = ultimoParto;
    }

    public String getUltimoPap() {
        return ultimoPap;
    }

    public void setUltimoPap(String ultimoPap) {
        this.ultimoPap = ultimoPap;
    }

    public Boolean getLactanciaMaterna() {
        return lactanciaMaterna;
    }

    public void setLactanciaMaterna(Boolean lactanciaMaterna) {
        this.lactanciaMaterna = lactanciaMaterna;
    }

    public String getComentarioLactancia() {
        return comentarioLactancia;
    }

    public void setComentarioLactancia(String comentarioLactancia) {
        this.comentarioLactancia = comentarioLactancia;
    }

    public String getTipoParto() {
        return tipoParto;
    }

    public void setTipoParto(String tipoParto) {
        this.tipoParto = tipoParto;
    }

    public Integer getMenopausia() {
        return menopausia;
    }

    public void setMenopausia(Integer menopausia) {
        this.menopausia = menopausia;
    }

    public Integer getGesta() {
        return gesta;
    }

    public void setGesta(Integer gesta) {
        this.gesta = gesta;
    }

    public Integer getPartos() {
        return partos;
    }

    public void setPartos(Integer partos) {
        this.partos = partos;
    }

    public Integer getAbortos() {
        return abortos;
    }

    public void setAbortos(Integer abortos) {
        this.abortos = abortos;
    }

    public Integer getEctopico() {
        return ectopico;
    }

    public void setEctopico(Integer ectopico) {
        this.ectopico = ectopico;
    }

    public String getComentarioGPA() {
        return comentarioGPA;
    }

    public void setComentarioGPA(String comentarioGPA) {
        this.comentarioGPA = comentarioGPA;
    }

    public String getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public Expediente getExpedientePacientecedula() {
        return expedientePacientecedula;
    }

    public void setExpedientePacientecedula(Expediente expedientePacientecedula) {
        this.expedientePacientecedula = expedientePacientecedula;
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
        if (!(object instanceof AntecedentesGinecologia)) {
            return false;
        }
        AntecedentesGinecologia other = (AntecedentesGinecologia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.AntecedentesGinecologia[ id=" + id + " ]";
    }
    
}
