package com.sicom.web.beans;

import com.sicom.controller.ExamenOdontologiaJpaController;
import com.sicom.entities.ExamenOdontologia;
import com.sicom.entities.Login;
import com.sicom.entities.Paciente;
import java.io.ByteArrayInputStream;
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
public class ExamenOdontologiaBean implements Serializable{
    
    private ExamenOdontologia examenOdontologia;
    private final ExamenOdontologiaJpaController eojc;
    private Paciente paciente;
    
     public ExamenOdontologiaBean() {
        eojc = new ExamenOdontologiaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        paciente = (Paciente) ec.getSessionMap().get("paciente");
        examenOdontologia = (ExamenOdontologia) ec.getSessionMap().get("examen");
        
        if (examenOdontologia == null) {
            examenOdontologia = new ExamenOdontologia();
            examenOdontologia.setPersonalCedula(((Login) ec.getSessionMap().get("login")).getPersonal());
        }
    }
    
    public void agregar() {
        try {
            examenOdontologia.setFecha(new Date());
            examenOdontologia.setExpedientePacientecedula(paciente.getExpediente());
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

            if(examenOdontologia.getImagen() == null) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe importar el archivo", null));
            } else {
                if (examenOdontologia.getId() == null) {
                    eojc.create(examenOdontologia);
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen Odontológico agregado exitosamente.", null));
                } else {
                    eojc.edit(examenOdontologia);
                    fc.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen Odontológico modificado exitosamente.", null));
                }
                
                ec.getFlash().setKeepMessages(true);
                ec.redirect(ec.getRequestContextPath().concat("/app/paciente/informacion"));
            }
            
            ec.getFlash().setKeepMessages(true);
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
            examenOdontologia.setImagen(bytes);
            FacesMessage message = new FacesMessage("El archivo " + filename + " se ha subido exitosamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir el archivo", null));
        }
    }
    
    public StreamedContent descargar() {
        InputStream stream = new ByteArrayInputStream(examenOdontologia.getImagen());
        StreamedContent file = new DefaultStreamedContent(stream, "image/jpg", "imagen_examen.jpg");
        
        return file;
    }
    
    public ExamenOdontologia getExamenOdontologia() {
        return examenOdontologia;
    }
    
    public void setExamenOdontologia(ExamenOdontologia examenOdontologia) {
        this.examenOdontologia = examenOdontologia;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}