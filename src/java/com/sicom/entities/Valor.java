/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "valor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Valor.findAll", query = "SELECT v FROM Valor v"),
    @NamedQuery(name = "Valor.findById", query = "SELECT v FROM Valor v WHERE v.valorPK.id = :id"),
    @NamedQuery(name = "Valor.findByDescripcion", query = "SELECT v FROM Valor v WHERE v.descripcion = :descripcion"),
    @NamedQuery(name = "Valor.findByCodigoId", query = "SELECT v FROM Valor v WHERE v.valorPK.codigoId = :codigoId")})
public class Valor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ValorPK valorPK;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "codigo_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Codigo codigo;

    public Valor() {
        codigo = new Codigo();
    }

    public Valor(ValorPK valorPK) {
        this.valorPK = valorPK;
    }

    public Valor(ValorPK valorPK, String descripcion) {
        this.valorPK = valorPK;
        this.descripcion = descripcion;
    }

    public Valor(int id, int codigoId) {
        this.valorPK = new ValorPK(id, codigoId);
    }

    public ValorPK getValorPK() {
        return valorPK;
    }

    public void setValorPK(ValorPK valorPK) {
        this.valorPK = valorPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Codigo getCodigo() {
        return codigo;
    }

    public void setCodigo(Codigo codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (valorPK != null ? valorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Valor)) {
            return false;
        }
        Valor other = (Valor) object;
        if ((this.valorPK == null && other.valorPK != null) || (this.valorPK != null && !this.valorPK.equals(other.valorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.Valor[ valorPK=" + valorPK + " ]";
    }
    
}
