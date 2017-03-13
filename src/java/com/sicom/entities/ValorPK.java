/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author WVQ
 */
@Embeddable
public class ValorPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "codigo_id")
    private int codigoId;

    public ValorPK() {
    }

    public ValorPK(int id, int codigoId) {
        this.id = id;
        this.codigoId = codigoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigoId() {
        return codigoId;
    }

    public void setCodigoId(int codigoId) {
        this.codigoId = codigoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) codigoId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValorPK)) {
            return false;
        }
        ValorPK other = (ValorPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.codigoId != other.codigoId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.ValorPK[ id=" + id + ", codigoId=" + codigoId + " ]";
    }
    
}
