package com.sicom.web.beans;

import com.sicom.controller.ExamenColposcopiaJpaController;
import com.sicom.entities.ExamenColposcopia;
import com.sicom.entities.Login;
import com.sicom.entities.Paciente;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class ExamenColposcopiaBean implements Serializable{
    
    private ExamenColposcopia examenColposcopia;
    private ExamenColposcopiaJpaController ecjc;
    private Paciente paciente;
   
    public ExamenColposcopiaBean(){
        ecjc = new ExamenColposcopiaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        paciente = (Paciente) ec.getSessionMap().get("paciente");
        examenColposcopia = (ExamenColposcopia) ec.getSessionMap().remove("examen");
        
        if (examenColposcopia == null) {
            examenColposcopia = new ExamenColposcopia();
            examenColposcopia.setPersonalCedula(((Login) ec.getSessionMap().get("login")).getPersonal());

            if (paciente == null) {
                try {
                    ec.redirect(ec.getRequestContextPath().concat("/app/paciente/consultar"));
                } catch (IOException ex) {
                    Logger.getLogger(ExamenColposcopiaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void agregar() {
        try {
            examenColposcopia.setFecha(new Date());
            examenColposcopia.setExpedientePacientecedula(paciente.getExpediente());
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            
            if (examenColposcopia.getId() == null) {
                ecjc.create(examenColposcopia);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen de colposcopia agregado exitosamente", null));
            } else {
                ecjc.edit(examenColposcopia);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen de colposcopia modificado exitosamente", null));
            }

            ec.getFlash().setKeepMessages(true);
            ec.redirect(ec.getRequestContextPath().concat("/app/paciente/informacion"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar examen", null));
            Logger.getLogger(ExamenColposcopiaBean.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void redireccionarAlEditor() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/consultorios/ultimaversion/public_html/classic_with_gui/colposcopia";
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
            examenColposcopia.setVasosAtipicos(bytes);
            FacesMessage message = new FacesMessage("El archivo " + filename + " se ha subido exitosamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir el archivo", null));
        }
    }
        
    public StreamedContent descargar() {
        InputStream stream = new ByteArrayInputStream(examenColposcopia.getVasosAtipicos());
        StreamedContent file = new DefaultStreamedContent(stream, "image/jpg", "imagen_examen.jpg");
        
        return file;
    }
    
    public ExamenColposcopia getExamenColposcopia() {
        return examenColposcopia;
    }

    public void setExamenColposcopia(ExamenColposcopia examenColposcopia) {
        this.examenColposcopia = examenColposcopia;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}