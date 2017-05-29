package com.sicom.web.beans;

import com.sicom.controller.ExamenGinecologiaJpaController;
import com.sicom.controller.ExamenOdontologiaJpaController;
import com.sicom.entities.ExamenGinecologia;
import com.sicom.entities.ExamenOdontologia;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


@ManagedBean
@ViewScoped
public class ExamenOdontologiaBean implements Serializable{
    
    private ExamenOdontologia examen;
    private ExamenOdontologiaJpaController ejc;
    private Paciente paciente;
    
     public ExamenOdontologiaBean() {
        ejc = new ExamenOdontologiaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        paciente = (Paciente) ec.getSessionMap().get("paciente");
        examen = (ExamenOdontologia) ec.getSessionMap().remove("examen");
        
        if (examen == null) {
            examen = new ExamenOdontologia();
            examen.setPersonalCedula(((Login) ec.getSessionMap().get("login")).getPersonal());
            
            if (paciente == null) {
                try {
                    ec.redirect(ec.getRequestContextPath().concat("/app/paciente/consultar"));
                } catch (IOException ex) {
                    Logger.getLogger(ExamenFisicoGinecologiaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
     public void agregar() {
        try {
            examen.setFecha(new Date());
            examen.setExpedientePacientecedula(paciente.getExpediente());
            FacesMessage message;

            if (examen.getId() == null) {
                ejc.create(examen);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen Odontológico agregado exitosamente.", null);
            } else {
                ejc.edit(examen);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen Odontológico modificado exitosamente.", null);
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
     
     
      public void redireccionarAInformacion() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
            ec.redirect(URL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
      
      
      
       public void redireccionarAlEditor() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/consultorios/ultimaversion/public_html/classic_with_gui/odontologia";
            ec.redirect(URL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     
       
     
       public void importar(FileUploadEvent event) {
        try {
            UploadedFile archivo = event.getFile();
            byte[] bytes = archivo.getContents();
            String filename = archivo.getFileName();
            examen.setImagen(bytes);
            FacesMessage message = new FacesMessage("El archivo " + filename + " se ha subido exitosamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir el archivo", null));
        }
    }
       
       
       
       
       
     
       public ExamenOdontologia getExamen() {
        return examen;
    }

    public void setExamen(ExamenOdontologia examen) {
        this.examen = examen;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
