/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.web.beans;

import com.sicom.controller.ExpedienteJpaController;
import com.sicom.entities.Expediente;
import com.sicom.entities.Paciente;
import com.sicom.entities.Personal;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author WVQ
 */
@ManagedBean
@ViewScoped
public class ExpedienteBean {

    private Expediente expediente;
    private ExpedienteJpaController ejc;
    private Paciente paciente;

    public ExpedienteBean() throws NullPointerException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ejc = new ExpedienteJpaController(emf);
        paciente = (Paciente) ec.getSessionMap().remove("paciente");

        if (paciente != null) {
            Init();
        } else {
            throw new NullPointerException();
        }

    }

    private void Init() {
        
        expediente = paciente.getExpediente();
        
    }

    /**
     * * Type : true = Histroria Clínica; false = Histria Clínica No
     * Modificable;
     *
     * @param permiso_editar
     */
    public void HistoriaClinica(boolean permiso_editar) {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String URL = ec.getRequestContextPath();

        Personal p = usuarioBean.getPersonal();
        int consultorio = p.getDepartamentoId().getId();

        switch (consultorio) {
            case 2: // Ginecologia
                if (permiso_editar) {
                    URL += "/app/consultorios/ginecologia/antecedentes";
                } else {
                    URL += "/app/consultorios/ginecologia/consultarAntecedentes";
                }
              
                break;
            case 3: // Odontologia
                if (permiso_editar) {
                    URL += "/app/consultorios/odontologia/antecedentes";
                } else {
                    URL += "/app/consultorios/odontologia/consultarAntecedentes";
                }
        }

        try {
            ec.redirect(URL);
        } catch (IOException ex) {
            Logger.getLogger(PacienteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    public Expediente getExpediente() {
        return expediente;
    }

    /**
     *
     * @param expediente
     */
    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

}
