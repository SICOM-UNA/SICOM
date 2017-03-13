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
public class ExpedientePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "Paciente_cedula")
    private String pacienteCedula;

    public ExpedientePK() {
    }

    public ExpedientePK(int id, String pacientecedula) {
        this.id = id;
        this.pacienteCedula = pacientecedula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPacienteCedula() {
        return pacienteCedula;
    }

    public void setPacienteCedula(String pacienteCedula) {
        this.pacienteCedula = pacienteCedula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (pacienteCedula != null ? pacienteCedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpedientePK)) {
            return false;
        }
        ExpedientePK other = (ExpedientePK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.pacienteCedula == null && other.pacienteCedula != null) || (this.pacienteCedula != null && !this.pacienteCedula.equals(other.pacienteCedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sicom.entities.ExpedientePK[ id=" + id + ", pacientecedula=" + pacienteCedula + " ]";
    }
    
}
