/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.web.beans;

import com.sicom.entities.Expediente;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author WVQ
 */
@ManagedBean
@ViewScoped
public class ExpedienteBean {
    private Expediente nuevoExpediente;
    private Expediente selectedExpediente;
    
    public ExpedienteBean() {}

    /**
     * @return the nuevoExpediente
     */
    public Expediente getNuevoExpediente() {
        return nuevoExpediente;
    }

    /**
     * @param nuevoExpediente the nuevoExpediente to set
     */
    public void setNuevoExpediente(Expediente nuevoExpediente) {
        this.nuevoExpediente = nuevoExpediente;
    }

    /**
     * @return the selectedExpediente
     */
    public Expediente getSelectedExpediente() {
        return selectedExpediente;
    }

    /**
     * @param selectedExpediente the selectedExpediente to set
     */
    public void setSelectedExpediente(Expediente selectedExpediente) {
        this.selectedExpediente = selectedExpediente;
    }
}