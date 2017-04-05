package com.sicom.web.beans;

import com.sicom.controller.AntecedentesOdontologiaJpaController;
import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.entities.AntecedentesOdontologia;
import com.sicom.entities.Paciente;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class AntecedentesOdontologiaBean implements Serializable {

    private AntecedentesOdontologia antecedentesOdontologia;
    private final AntecedentesOdontologiaJpaController aoc;
    private Paciente paciente;
    private boolean antecedenteNuevo;

    public AntecedentesOdontologiaBean() {
        aoc = new AntecedentesOdontologiaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        paciente = (Paciente) ec.getSessionMap().get("paciente");
        antecedentesOdontologia = (AntecedentesOdontologia) ec.getSessionMap().remove("antecedenteOdontologia");

        if (paciente != null) {
            if (antecedentesOdontologia == null) {
                antecedentesOdontologia = new AntecedentesOdontologia();
                antecedenteNuevo = true;
            } else {
                antecedenteNuevo = false;
            }
        } else {
            try {
                String URL = ec.getRequestContextPath() + "/app/paciente/consultar#formulario";
                ec.redirect(URL);
            } catch (IOException ex) {
                Logger.getLogger(AntecedentesGinecologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void save() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        if (antecedenteNuevo) {
            try {
                antecedentesOdontologia.setFecha(new Date());
                antecedentesOdontologia.setExpedientePacientecedula(paciente.getExpediente());
                aoc.create(antecedentesOdontologia);

                ec.getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información agregada exitósamente.", null));
                String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
                ec.redirect(URL);

            } catch (IllegalOrphanException | IOException ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                antecedentesOdontologia.setExpedientePacientecedula(paciente.getExpediente());
                aoc.edit(antecedentesOdontologia);

                ec.getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información modificada exitósamente.", null));
                String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
                ec.redirect(URL);

            } catch (IllegalOrphanException | IOException ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cancelarAction() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/paciente/informacion#formulario";
            ec.redirect(URL);
        } catch (IOException ex) {
            Logger.getLogger(AntecedentesGinecologiaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public AntecedentesOdontologia getObjAntecedente() {
        return antecedentesOdontologia;
    }

    public void setObjAntecedente(AntecedentesOdontologia nuevoAntecedente) {
        this.antecedentesOdontologia = nuevoAntecedente;
    }

}
