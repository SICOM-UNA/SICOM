package com.sicom.web.beans;

import com.sicom.controller.ExamenColposcopiaJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.ExamenColposcopia;
import com.sicom.entities.Login;
import com.sicom.entities.Paciente;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class ColposcopiaBean implements Serializable{
    
    private ExamenColposcopia examen;
    private ExamenColposcopiaJpaController ecc;
    private Paciente paciente;
   
    public ColposcopiaBean(){
        ecc=new ExamenColposcopiaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
         paciente = (Paciente) ec.getSessionMap().get("paciente");
        examen = (ExamenColposcopia) ec.getSessionMap().remove("examen");
        
        if (examen == null) {
            examen = new ExamenColposcopia();
            examen.setPersonalCedula(((Login) ec.getSessionMap().get("login")).getPersonal());

            if (paciente == null) {
                try {
                    ec.redirect(ec.getRequestContextPath().concat("/app/paciente/consultar"));
                } catch (IOException ex) {
                    Logger.getLogger(ColposcopiaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
       
       
    }
    
    public void save() {
        try {
            examen.setFecha(new Date());
            examen.setExpedientePacientecedula(paciente.getExpediente());
            FacesMessage message;

            if (examen.getId() == null) {
                ecc.create(examen);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen de colposcopia agregado exitosamente.", null);
            } else {
                ecc.edit(examen);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen de colposcopia modificado exitosamente.", null);
            }

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, message);
            ec.redirect(ec.getRequestContextPath().concat("/app/paciente/informacion"));
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "El examen de colposcopia no se pudo agregar.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Logger.getLogger(ColposcopiaBean.class.getName()).log(Level.SEVERE, null, ex);
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
    

    public ExamenColposcopia getExamen() {
        return examen;
    }

    public void setExamen(ExamenColposcopia examen) {
        this.examen = examen;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    
    
    
}