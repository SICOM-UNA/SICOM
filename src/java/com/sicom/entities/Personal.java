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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = "Personal.findById", query = "SELECT p FROM Personal p WHERE p.id = :id"),
    @NamedQuery(name = "Personal.findByNombre", query = "SELECT p FROM Personal p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Personal.findByTelefono", query = "SELECT p FROM Personal p WHERE p.telefono = :telefono"),
    @NamedQuery(name = "Personal.findByCargo", query = "SELECT p FROM Personal p WHERE p.cargo = :cargo"),
    @NamedQuery(name = "Personal.findByCorreo", query = "SELECT p FROM Personal p WHERE p.correo = :correo")})
public class Personal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "primer_apellido")
    private String primerApellido;
    @Column(name = "segundo_apellido")
    private String segundoApellido;
     @Column(name = "genero")
    private String genero;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "celular")
    private String celular;
     @Column(name = "estado_civil")
    private String estadoCivil;
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "correo")
    private String correo;
    @Column(name = "nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nacimiento;
    @Column(name = "domicilio")
    private String domicilio;
    
    
    @JoinColumn(name = "autorizacion_nivel", referencedColumnName = "nivel")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Autorizacion autorizacionNivel;
    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Departamento departamentoId;
    @JoinColumn(name = "login_usuario", referencedColumnName = "usuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Login loginUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalid", fetch = FetchType.LAZY)
    private List<Cita> citaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalId", fetch = FetchType.LAZY)
    private List<Consulta> consultaList;

    public Personal() {
    }

    public Personal(String id) {
        this.id = id;
    }

    public Personal(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    
    
    
   
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
   public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
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

    public Autorizacion getAutorizacionNivel() {
        return autorizacionNivel;
    }

    public void setAutorizacionNivel(Autorizacion autorizacionNivel) {
        this.autorizacionNivel = autorizacionNivel;
    }

    public Departamento getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Departamento departamentoId) {
        this.departamentoId = departamentoId;
    }

    public Login getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(Login loginUsuario) {
        this.loginUsuario = loginUsuario;
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @XmlTransient
    public List<Cita> getCitaList() {
        return citaList;
    }

    public void setCitaList(List<Cita> citaList) {
        this.citaList = citaList;
    }

    @XmlTransient
    public List<Consulta> getConsultaList() {
        return consultaList;
    }

    public void setConsultaList(List<Consulta> consultaList) {
        this.consultaList = consultaList;
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
        if (!(object instanceof Personal)) {
            return false;
        }
        Personal other = (Personal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Personal[ id=" + id + " ]";
    }
    
}
