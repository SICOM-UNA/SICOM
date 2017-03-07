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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pablo
 */
@Entity
@Table(name = "antecedentesodontologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecedentesOdontologia.findAll", query = "SELECT a FROM AntecedentesOdontologia a")
    , @NamedQuery(name = "AntecedentesOdontologia.findByPacienteid", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.pacienteid = :pacienteid")
    , @NamedQuery(name = "AntecedentesOdontologia.findByFecha", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.fecha = :fecha")
    , @NamedQuery(name = "AntecedentesOdontologia.findByPatologicos", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.patologicos = :patologicos")
    , @NamedQuery(name = "AntecedentesOdontologia.findByNoPatologicos", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.noPatologicos = :noPatologicos")
    , @NamedQuery(name = "AntecedentesOdontologia.findByHereditarios", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.hereditarios = :hereditarios")
    , @NamedQuery(name = "AntecedentesOdontologia.findByAlergias", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.alergias = :alergias")
    , @NamedQuery(name = "AntecedentesOdontologia.findByQuirurgicos", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.quirurgicos = :quirurgicos")
    , @NamedQuery(name = "AntecedentesOdontologia.findByMoitvoConsulta", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.moitvoConsulta = :moitvoConsulta")
    , @NamedQuery(name = "AntecedentesOdontologia.findByHabitos", query = "SELECT a FROM AntecedentesOdontologia a WHERE a.habitos = :habitos")})
public class AntecedentesOdontologia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Paciente_id")
    private String pacienteid;
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
    @Column(name = "moitvoConsulta")
    private String moitvoConsulta;
    @Column(name = "habitos")
    private String habitos;
    @JoinColumn(name = "Paciente_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Paciente paciente;

    public AntecedentesOdontologia() {
    }

    public AntecedentesOdontologia(String pacienteid) {
        this.pacienteid = pacienteid;
    }

    public AntecedentesOdontologia(String pacienteid, Date fecha, String patologicos) {
        this.pacienteid = pacienteid;
        this.fecha = fecha;
        this.patologicos = patologicos;
    }

    public String getPacienteid() {
        return pacienteid;
    }

    public void setPacienteid(String pacienteid) {
        this.pacienteid = pacienteid;
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

    public String getMoitvoConsulta() {
        return moitvoConsulta;
    }

    public void setMoitvoConsulta(String moitvoConsulta) {
        this.moitvoConsulta = moitvoConsulta;
    }

    public String getHabitos() {
        return habitos;
    }

    public void setHabitos(String habitos) {
        this.habitos = habitos;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pacienteid != null ? pacienteid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AntecedentesOdontologia)) {
            return false;
        }
        AntecedentesOdontologia other = (AntecedentesOdontologia) object;
        if ((this.pacienteid == null && other.pacienteid != null) || (this.pacienteid != null && !this.pacienteid.equals(other.pacienteid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.AntecedentesOdontologia[ pacienteid=" + pacienteid + " ]";
    }
    
}
