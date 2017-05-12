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
import javax.persistence.Lob;
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
@Table(name = "examencolposcopia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExamenColposcopia.findAll", query = "SELECT e FROM ExamenColposcopia e"),
    @NamedQuery(name = "ExamenColposcopia.findById", query = "SELECT e FROM ExamenColposcopia e WHERE e.id = :id"),
    @NamedQuery(name = "ExamenColposcopia.findByFecha", query = "SELECT e FROM ExamenColposcopia e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "ExamenColposcopia.findByResultado", query = "SELECT e FROM ExamenColposcopia e WHERE e.resultado = :resultado"),
    @NamedQuery(name = "ExamenColposcopia.findByResultadoComentario", query = "SELECT e FROM ExamenColposcopia e WHERE e.resultadoComentario = :resultadoComentario"),
    @NamedQuery(name = "ExamenColposcopia.findByMoco", query = "SELECT e FROM ExamenColposcopia e WHERE e.moco = :moco"),
    @NamedQuery(name = "ExamenColposcopia.findByAcidoAcetico", query = "SELECT e FROM ExamenColposcopia e WHERE e.acidoAcetico = :acidoAcetico"),
    @NamedQuery(name = "ExamenColposcopia.findBySchiller", query = "SELECT e FROM ExamenColposcopia e WHERE e.schiller = :schiller"),
    @NamedQuery(name = "ExamenColposcopia.findByComentarioSchiller", query = "SELECT e FROM ExamenColposcopia e WHERE e.comentarioSchiller = :comentarioSchiller"),
    @NamedQuery(name = "ExamenColposcopia.findByLx", query = "SELECT e FROM ExamenColposcopia e WHERE e.lx = :lx"),
    @NamedQuery(name = "ExamenColposcopia.findByOtros", query = "SELECT e FROM ExamenColposcopia e WHERE e.otros = :otros"),
    @NamedQuery(name = "ExamenColposcopia.findByBx", query = "SELECT e FROM ExamenColposcopia e WHERE e.bx = :bx")})
public class ExamenColposcopia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "resultado")
    private Boolean resultado;
    @Column(name = "resultadoComentario")
    private String resultadoComentario;
    @Column(name = "moco")
    private Boolean moco;
    @Lob
    @Column(name = "vasosAtipicos")
    private byte[] vasosAtipicos;
    @Column(name = "acidoAcetico")
    private String acidoAcetico;
    @Column(name = "schiller")
    private Boolean schiller;
    @Column(name = "comentarioSchiller")
    private String comentarioSchiller;
    @Column(name = "lx")
    private String lx;
    @Column(name = "otros")
    private String otros;
    @Column(name = "bx")
    private Boolean bx;
    @JoinColumn(name = "Expediente_Paciente_cedula", referencedColumnName = "Paciente_cedula")
    @ManyToOne(optional = false)
    private Expediente expedientePacientecedula;
    @JoinColumn(name = "Personal_cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Personal personalCedula;

    public ExamenColposcopia() {
    }

    public ExamenColposcopia(Integer id) {
        this.id = id;
    }

    public ExamenColposcopia(Integer id, String fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Boolean getResultado() {
        return resultado;
    }

    public void setResultado(Boolean resultado) {
        this.resultado = resultado;
    }

    public String getResultadoComentario() {
        return resultadoComentario;
    }

    public void setResultadoComentario(String resultadoComentario) {
        this.resultadoComentario = resultadoComentario;
    }

    public Boolean getMoco() {
        return moco;
    }

    public void setMoco(Boolean moco) {
        this.moco = moco;
    }

    public byte[] getVasosAtipicos() {
        return vasosAtipicos;
    }

    public void setVasosAtipicos(byte[] vasosAtipicos) {
        this.vasosAtipicos = vasosAtipicos;
    }

    public String getAcidoAcetico() {
        return acidoAcetico;
    }

    public void setAcidoAcetico(String acidoAcetico) {
        this.acidoAcetico = acidoAcetico;
    }

    public Boolean getSchiller() {
        return schiller;
    }

    public void setSchiller(Boolean schiller) {
        this.schiller = schiller;
    }

    public String getComentarioSchiller() {
        return comentarioSchiller;
    }

    public void setComentarioSchiller(String comentarioSchiller) {
        this.comentarioSchiller = comentarioSchiller;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public Boolean getBx() {
        return bx;
    }

    public void setBx(Boolean bx) {
        this.bx = bx;
    }

    public Expediente getExpedientePacientecedula() {
        return expedientePacientecedula;
    }

    public void setExpedientePacientecedula(Expediente expedientePacientecedula) {
        this.expedientePacientecedula = expedientePacientecedula;
    }

    public Personal getPersonalCedula() {
        return personalCedula;
    }

    public void setPersonalCedula(Personal personalCedula) {
        this.personalCedula = personalCedula;
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
        if (!(object instanceof ExamenColposcopia)) {
            return false;
        }
        ExamenColposcopia other = (ExamenColposcopia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.ExamenColposcopia[ id=" + id + " ]";
    }
    
}
