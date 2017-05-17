/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author WVQ
 */
@Entity
@Table(name = "personal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personal.findAll", query = "SELECT p FROM Personal p"),
    @NamedQuery(name = "Personal.findByCedula", query = "SELECT p FROM Personal p WHERE p.cedula = :cedula"),
    @NamedQuery(name = "Personal.findByNombre", query = "SELECT p FROM Personal p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Personal.findByPrimerApellido", query = "SELECT p FROM Personal p WHERE p.primerApellido = :primerApellido"),
    @NamedQuery(name = "Personal.findBySegundoApellido", query = "SELECT p FROM Personal p WHERE p.segundoApellido = :segundoApellido"),
    @NamedQuery(name = "Personal.findByGenero", query = "SELECT p FROM Personal p WHERE p.genero = :genero"),
    @NamedQuery(name = "Personal.findByCelular", query = "SELECT p FROM Personal p WHERE p.celular = :celular"),
    @NamedQuery(name = "Personal.findByTelefono", query = "SELECT p FROM Personal p WHERE p.telefono = :telefono"),
    @NamedQuery(name = "Personal.findByCargo", query = "SELECT p FROM Personal p WHERE p.cargo = :cargo"),
    @NamedQuery(name = "Personal.findByCorreo", query = "SELECT p FROM Personal p WHERE p.correo = :correo"),
    @NamedQuery(name = "Personal.findByNacimiento", query = "SELECT p FROM Personal p WHERE p.nacimiento = :nacimiento"),
    @NamedQuery(name = "Personal.findByDomicilio", query = "SELECT p FROM Personal p WHERE p.domicilio = :domicilio"),
    @NamedQuery(name = "Personal.findByEstadoCivil", query = "SELECT p FROM Personal p WHERE p.estadoCivil = :estadoCivil")})
public class Personal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cedula")
    private String cedula;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "primerApellido")
    private String primerApellido;
    @Basic(optional = false)
    @Column(name = "segundoApellido")
    private String segundoApellido;
    @Column(name = "genero")
    private String genero;
    @Column(name = "celular")
    private String celular;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "correo")
    private String correo;
    @Column(name = "nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nacimiento;
    @Column(name = "domicilio")
    private String domicilio;
    @Column(name = "estadoCivil")
    private String estadoCivil;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalCedula")
    private List<ExamenColposcopia> examenColposcopiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalcedula")
    private List<MonitoreoFetal> monitoreoFetalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalCedula")
    private List<ExamenOdontologia> examenOdontologiaList;
    @JoinColumn(name = "Departamento_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamento departamentoId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "personalCedula")
    private List<Autorizacion> autorizacionList;
    @JoinColumn(name = "Login_usuario", referencedColumnName = "usuario")
    @OneToOne(optional = false)
    private Login loginUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalCedula")
    private List<ExamenGinecologia> examenGinecologiaList;

    public Personal() {
    }

    public Personal(String cedula) {
        this.cedula = cedula;
    }

    public Personal(String cedula, String nombre, String primerApellido, String segundoApellido) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
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

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    @XmlTransient
    public List<ExamenColposcopia> getExamenColposcopiaList() {
        return examenColposcopiaList;
    }

    public void setExamenColposcopiaList(List<ExamenColposcopia> examenColposcopiaList) {
        this.examenColposcopiaList = examenColposcopiaList;
    }

    @XmlTransient
    public List<MonitoreoFetal> getMonitoreoFetalList() {
        return monitoreoFetalList;
    }

    public void setMonitoreoFetalList(List<MonitoreoFetal> monitoreoFetalList) {
        this.monitoreoFetalList = monitoreoFetalList;
    }
    
    @XmlTransient
    public List<ExamenOdontologia> getExamenOdontologiaList() {
        return examenOdontologiaList;
    }

    public void setExamenOdontologiaList(List<ExamenOdontologia> examenOdontologiaList) {
        this.examenOdontologiaList = examenOdontologiaList;
    }

    public Departamento getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Departamento departamentoId) {
        this.departamentoId = departamentoId;
    }
    
    @XmlTransient
    public List<Autorizacion> getAutorizacionList() {
        return autorizacionList;
    }

    public void setAutorizacionList(List<Autorizacion> autorizacionList) {
        this.autorizacionList = autorizacionList;
    }
    
    public Login getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(Login loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    @XmlTransient
    public List<ExamenGinecologia> getExamenGinecologiaList() {
        return examenGinecologiaList;
    }

    public void setExamenGinecologiaList(List<ExamenGinecologia> examenGinecologiaList) {
        this.examenGinecologiaList = examenGinecologiaList;
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
        if (!(object instanceof Personal)) {
            return false;
        }
        Personal other = (Personal) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Personal[ cedula=" + cedula + " ]";
    }
}