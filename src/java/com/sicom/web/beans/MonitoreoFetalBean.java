package com.sicom.web.beans;

import com.sicom.controller.MonitoreoFetalJpaController;
import com.sicom.entities.Login;
import com.sicom.entities.MonitoreoFetal;
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
public class MonitoreoFetalBean implements Serializable {

    private MonitoreoFetal monitoreoFetal;

    private MonitoreoFetalJpaController mfc;
    private Paciente paciente;

    public MonitoreoFetalBean() {
        mfc = new MonitoreoFetalJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        paciente = (Paciente) ec.getSessionMap().get("paciente");
        monitoreoFetal = (MonitoreoFetal) ec.getSessionMap().remove("examen");

        if (monitoreoFetal == null) {
            monitoreoFetal = new MonitoreoFetal();
            monitoreoFetal.setPersonalcedula(((Login) ec.getSessionMap().get("login")).getPersonal());

            if (paciente == null) {
                try {
                    ec.redirect(ec.getRequestContextPath().concat("/app/paciente/consultar"));
                } catch (IOException ex) {
                    Logger.getLogger(MonitoreoFetalBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void save() {
        try {
            monitoreoFetal.setFecha(new Date());
            monitoreoFetal.setExpedienteid(paciente.getExpediente());
            FacesMessage message;

            if (monitoreoFetal.getId() == null) {
                mfc.create(monitoreoFetal);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Monitoreo Fetal agregado exitosamente.", null);
            } else {
                mfc.edit(monitoreoFetal);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Monitoreo Fetal modificado exitosamente.", null);
            }

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, message);
            ec.redirect(ec.getRequestContextPath().concat("/app/paciente/informacion"));
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "El Monitoreo Fetal no se pudo agregar.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Logger.getLogger(MonitoreoFetalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void volver_a_informacion() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
            ec.redirect(URL);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public MonitoreoFetal getMonitoreoFetal() {
        return monitoreoFetal;
    }

    public void setMonitoreoFetal(MonitoreoFetal monitoreoFetal) {
        this.monitoreoFetal = monitoreoFetal;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

}
