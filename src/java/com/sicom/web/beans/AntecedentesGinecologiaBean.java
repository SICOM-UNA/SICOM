package com.sicom.web.beans;

import com.sicom.controller.AntecedentesGinecologiaJpaController;
import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Expediente;
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
public class AntecedentesGinecologiaBean implements Serializable {

    private AntecedentesGinecologia antecedentesGinecologia;
    private final AntecedentesGinecologiaJpaController agc;
    private Paciente paciente;
    private Boolean antecedenteNuevo;

    /**
     * Constructor
     */
    public AntecedentesGinecologiaBean() {
        agc = new AntecedentesGinecologiaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        paciente = (Paciente) ec.getSessionMap().get("paciente");
        Object obj = ec.getSessionMap().remove("validacionGinecologia");
        boolean permiso = (obj != null);

        if (paciente != null && permiso) {
            Expediente e = paciente.getExpediente();
            antecedentesGinecologia = e.getAntecedentesGinecologia();

            if (antecedentesGinecologia == null) {
                antecedentesGinecologia = new AntecedentesGinecologia();
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
                antecedentesGinecologia.setFecha(new Date());
                antecedentesGinecologia.setExpedientePacientecedula(paciente.getExpediente());
                agc.create(antecedentesGinecologia);

                ec.getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información agregada exitósamente.", null));
                String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
                ec.getRequestMap().put("paciente", paciente);
                ec.redirect(URL);

            } catch (IllegalOrphanException | IOException ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                antecedentesGinecologia.setFecha(new Date());
                antecedentesGinecologia.setExpedientePacientecedula(paciente.getExpediente());
                agc.edit(antecedentesGinecologia);

                ec.getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información modificada exitósamente.", null));
                String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
                ec.getRequestMap().put("paciente", paciente);
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

    /**
     *
     * @return
     */
    public AntecedentesGinecologia getObjAntecedente() {
        return antecedentesGinecologia;
    }

    /**
     *
     * @param nuevoAntecedente
     */
    public void setObjAntecedente(AntecedentesGinecologia nuevoAntecedente) {
        this.antecedentesGinecologia = nuevoAntecedente;
    }

    /**
     *
     * @return
     */
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

}
