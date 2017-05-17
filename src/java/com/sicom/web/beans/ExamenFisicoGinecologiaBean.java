package com.sicom.web.beans;

import com.sicom.controller.ExamenGinecologiaJpaController;
import com.sicom.entities.ExamenGinecologia;
import com.sicom.entities.Login;
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
public class ExamenFisicoGinecologiaBean implements Serializable {

    private ExamenGinecologia examenFisico;
    private Date hoy;
    private ExamenGinecologiaJpaController ejc;
    private Paciente paciente;

    public ExamenFisicoGinecologiaBean() {
        ejc = new ExamenGinecologiaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        paciente = (Paciente) ec.getSessionMap().get("paciente");
        examenFisico = (ExamenGinecologia) ec.getSessionMap().remove("examen");
        
        if (examenFisico == null) {
            examenFisico = new ExamenGinecologia();
            examenFisico.setPersonalCedula(((Login) ec.getSessionMap().get("login")).getPersonal());
            
            if (paciente == null) {
                try {
                    ec.redirect(ec.getRequestContextPath().concat("/app/paciente/consultar"));
                } catch (IOException ex) {
                    Logger.getLogger(ExamenFisicoGinecologiaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void save() {
        try {
            examenFisico.setFecha(new Date());
            examenFisico.setExpedientePacientecedula(paciente.getExpediente());
            FacesMessage message;

            if (examenFisico.getId() == null) {
                ejc.create(examenFisico);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen Físico agregado exitosamente.", null);
            } else {
                ejc.edit(examenFisico);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen Físico modificado exitosamente.", null);
            }

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, message);
            ec.redirect(ec.getRequestContextPath().concat("/app/paciente/informacion"));
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "El examen no se pudo agregar.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Logger.getLogger(ExamenFisicoGinecologiaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void volver_a_informacion() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
            ec.redirect(URL);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public Date getHoy() {
        return hoy;
    }

    public ExamenGinecologia getExamenFisico() {
        return examenFisico;
    }

    public void setExamenFisico(ExamenGinecologia examenFisico) {
        this.examenFisico = examenFisico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}