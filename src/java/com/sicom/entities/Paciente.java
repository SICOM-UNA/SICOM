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
@Table(name = "paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p")
    ,
    @NamedQuery(name = "Paciente.findById", query = "SELECT p FROM Paciente p WHERE p.id = :id")
    ,
    @NamedQuery(name = "Paciente.findByNombre", query = "SELECT p FROM Paciente p WHERE p.nombre = :nombre")
    ,
    @NamedQuery(name = "Paciente.findByPrimerApellido", query = "SELECT p FROM Paciente p WHERE p.primerApellido = :primerApellido")
    ,
    @NamedQuery(name = "Paciente.findBySegundoApellido", query = "SELECT p FROM Paciente p WHERE p.segundoApellido = :segundoApellido")
    ,
    @NamedQuery(name = "Paciente.findByTelefono", query = "SELECT p FROM Paciente p WHERE p.telefono = :telefono")
    ,
    @NamedQuery(name = "Paciente.findByCelular", query = "SELECT p FROM Paciente p WHERE p.celular = :celular")
    ,
    @NamedQuery(name = "Paciente.findByEstadoCivil", query = "SELECT p FROM Paciente p WHERE p.estadoCivil = :estadoCivil")
    ,
    @NamedQuery(name = "Paciente.findByNacimiento", query = "SELECT p FROM Paciente p WHERE p.nacimiento = :nacimiento")
    ,
    @NamedQuery(name = "Paciente.findByDomicilio", query = "SELECT p FROM Paciente p WHERE p.domicilio = :domicilio")
    ,
    @NamedQuery(name = "Paciente.findByOcupacion", query = "SELECT p FROM Paciente p WHERE p.ocupacion = :ocupacion")
    ,
    @NamedQuery(name = "Paciente.findByCorreo", query = "SELECT p FROM Paciente p WHERE p.correo = :correo")
    ,
    @NamedQuery(name = "Paciente.findByGenero", query = "SELECT p FROM Paciente p WHERE p.genero = :genero")})
public class Paciente implements Serializable {

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
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "celular")
    private String celular;
    @Column(name = "estado_civil")
    private String estadoCivil;
    @Column(name = "nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nacimiento;
    @Column(name = "domicilio")
    private String domicilio;
    @Column(name = "ocupacion")
    private String ocupacion;
    @Column(name = "correo")
    private String correo;
    @Column(name = "genero")
    private String genero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pacienteid", fetch = FetchType.LAZY)
    private List<AntecedentesGinecologia> antecedentesGinecologiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pacienteid", fetch = FetchType.LAZY)
    private List<Responsable> responsableList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pacienteid", fetch = FetchType.LAZY)
    private List<AntecedentesOdontologia> antecedentesOdontologiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pacienteid", fetch = FetchType.LAZY)
    private List<Cita> citaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pacienteid", fetch = FetchType.LAZY)
    private List<Consulta> consultaList;

    public Paciente() {
    }

    public Paciente(String id) {
        this.id = id;
    }

    public Paciente(String id, String nombre) {
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

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @XmlTransient
    public List<AntecedentesGinecologia> getAntecedentesGinecologiaList() {
        return antecedentesGinecologiaList;
    }

    public void setAntecedentesGinecologiaList(List<AntecedentesGinecologia> antecedentesGinecologiaList) {
        this.antecedentesGinecologiaList = antecedentesGinecologiaList;
    }

    @XmlTransient
    public List<Responsable> getResponsableList() {
        return responsableList;
    }

    public void setResponsableList(List<Responsable> responsableList) {
        this.responsableList = responsableList;
    }

    @XmlTransient
    public List<AntecedentesOdontologia> getAntecedentesOdontologiaList() {
        return antecedentesOdontologiaList;
    }

    public void setAntecedentesOdontologiaList(List<AntecedentesOdontologia> antecedentesOdontologiaList) {
        this.antecedentesOdontologiaList = antecedentesOdontologiaList;
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
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public Responsable getResponsable(int i) {
        return (i >= 0 && i < responsableList.size())
                ? responsableList.get(i)
                : null;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Paciente[ id=" + id + " ]";
    }

}
