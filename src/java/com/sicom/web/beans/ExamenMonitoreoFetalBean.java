package com.sicom.web.beans;

import com.sicom.controller.ExamenMonitoreoFetalJpaController;
import com.sicom.entities.Login;
import com.sicom.entities.ExamenMonitoreoFetal;
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
public class ExamenMonitoreoFetalBean implements Serializable {

    private ExamenMonitoreoFetal monitoreoFetal;
    private ExamenMonitoreoFetalJpaController mfjc;
    private Paciente paciente;

    public ExamenMonitoreoFetalBean() {
        mfjc = new ExamenMonitoreoFetalJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        paciente = (Paciente) ec.getSessionMap().get("paciente");
        monitoreoFetal = (ExamenMonitoreoFetal) ec.getSessionMap().get("examen");

        if (monitoreoFetal == null) {
            monitoreoFetal = new ExamenMonitoreoFetal();
            monitoreoFetal.setPersonalcedula(((Login) ec.getSessionMap().get("login")).getPersonal());
        }
    }

    public void agregar() {
        try {
            monitoreoFetal.setFecha(new Date());
            monitoreoFetal.setExpedienteid(paciente.getExpediente());
            FacesMessage message;

            if (monitoreoFetal.getId() == null) {
                mfjc.create(monitoreoFetal);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Monitoreo Fetal agregado exitosamente.", null);
            } else {
                mfjc.edit(monitoreoFetal);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Monitoreo Fetal modificado exitosamente.", null);
            }

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, message);
            ec.redirect(ec.getRequestContextPath().concat("/app/paciente/informacion"));
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar Monitoreo Fetal", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            Logger.getLogger(ExamenMonitoreoFetalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redireccionarAInformacion() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
            ec.redirect(URL);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ExamenMonitoreoFetal getMonitoreoFetal() {
        return monitoreoFetal;
    }

    public void setMonitoreoFetal(ExamenMonitoreoFetal monitoreoFetal) {
        this.monitoreoFetal = monitoreoFetal;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}