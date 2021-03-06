package com.sicom.web.beans;

import com.sicom.controller.ExamenGinecologiaJpaController;
import com.sicom.entities.ExamenGinecologia;
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
public class ExamenFisicoGinecologiaBean implements Serializable {

    private ExamenGinecologia examenFisico;
    private ExamenGinecologiaJpaController egjc;
    private Paciente paciente;

    public ExamenFisicoGinecologiaBean() {
        egjc = new ExamenGinecologiaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        paciente = (Paciente) ec.getSessionMap().get("paciente");
        examenFisico = (ExamenGinecologia) ec.getSessionMap().get("examen");
        
        if (examenFisico == null) {
            examenFisico = new ExamenGinecologia();
            examenFisico.setPersonalCedula(((Login) ec.getSessionMap().get("login")).getPersonal());
        }
    }

    public void agregar() {
        try {
            examenFisico.setFecha(new Date());
            examenFisico.setExpedientePacientecedula(paciente.getExpediente());
            FacesMessage message;

            if (examenFisico.getId() == null) {
                egjc.create(examenFisico);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen Físico agregado exitosamente.", null);
            } else {
                egjc.edit(examenFisico);
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
            String URL = ec.getRequestContextPath() + "/app/consultorios/ultimaversion/public_html/classic_with_gui/ginecologia";
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
            examenFisico.setImagenMamas(bytes);
            FacesMessage message = new FacesMessage("El archivo " + filename + " se ha subido exitosamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir el archivo", null));
        }
    }
    
    public StreamedContent descargar() {
        InputStream stream = new ByteArrayInputStream(examenFisico.getImagenMamas());
        StreamedContent file = new DefaultStreamedContent(stream, "image/jpg", "imagen_examen.jpg");
        
        return file;
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