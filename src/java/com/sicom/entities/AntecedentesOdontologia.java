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
@Table(name = "antecedentesodontologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecedentesOdontologia.findAll", query = "SELECT a FROM AntecedentesOdontologia a"),
    @NamedQuery(name = "AntecedentesOdontologia.findById", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.id = :id"),
    @NamedQuery(name = "AntecedentesOdontologia.findByFecha", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "AntecedentesOdontologia.findByPatologicos", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.patologicos = :patologicos"),
    @NamedQuery(name = "AntecedentesOdontologia.findByNoPatologicos", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.noPatologicos = :noPatologicos"),
    @NamedQuery(name = "AntecedentesOdontologia.findByHereditarios", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.hereditarios = :hereditarios"),
    @NamedQuery(name = "AntecedentesOdontologia.findByAlergias", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.alergias = :alergias"),
    @NamedQuery(name = "AntecedentesOdontologia.findByQuirurgicos", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.quirurgicos = :quirurgicos"),
    @NamedQuery(name = "AntecedentesOdontologia.findByHabitos", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.habitos = :habitos")})
public class AntecedentesOdontologia implements Serializable {

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
    @Basic(optional = false)
    @Column(name = "patologicos")
    private String patologicos;
    @Column(name = "noPatologicos")
    private String noPatologicos;
    @Column(name = "hereditarios")
    private String hereditarios;
    @Column(name = "alergias")
    private String alergias;
    @Column(name = "quirurgicos")
    private String quirurgicos;
    @Column(name = "habitos")
    private String habitos;
    @JoinColumn(name = "Expediente_Paciente_cedula", referencedColumnName = "Paciente_cedula")
    @ManyToOne(optional = false)
    private Expediente expedientePacienteCedula;

    public AntecedentesOdontologia() {
        expedientePacienteCedula = new Expediente();
    }

    public AntecedentesOdontologia(Integer id) {
        this.id = id;
    }

    public AntecedentesOdontologia(Integer id, Date fecha, String patologicos) {
        this.id = id;
        this.fecha = fecha;
        this.patologicos = patologicos;
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

    public String getHereditarios() {
        return hereditarios;
    }

    public void setHereditarios(String hereditarios) {
        this.hereditarios = hereditarios;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getQuirurgicos() {
        return quirurgicos;
    }

    public void setQuirurgicos(String quirurgicos) {
        this.quirurgicos = quirurgicos;
    }

    public String getHabitos() {
        return habitos;
    }

    public void setHabitos(String habitos) {
        this.habitos = habitos;
    }

    public Expediente getExpedientePacienteCedula() {
        return expedientePacienteCedula;
    }

    public void setExpedientePacienteCedula(Expediente expedientePacienteCedula) {
        this.expedientePacienteCedula = expedientePacienteCedula;
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
        if (!(object instanceof AntecedentesOdontologia)) {
            return false;
        }
        AntecedentesOdontologia other = (AntecedentesOdontologia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.AntecedentesOdontologia[ id=" + id + " ]";
    }
    
}
