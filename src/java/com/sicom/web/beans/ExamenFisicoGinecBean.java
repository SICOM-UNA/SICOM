package com.sicom.web.beans;

import com.sicom.controller.ExamenGinecologiaJpaController;
import com.sicom.entities.ExamenGinecologia;
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
public class ExamenFisicoGinecBean implements Serializable {

    private ExamenGinecologia examenFisico;
    private Date hoy;
    private ExamenGinecologiaJpaController ejc;
    private Paciente paciente;

    public ExamenFisicoGinecBean() {
        ejc = new ExamenGinecologiaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        examenFisico = new ExamenGinecologia();

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        paciente = (Paciente) ec.getSessionMap().get("paciente");

        System.out.println(paciente.getNombre());
        
        if (paciente == null) {
            try {
                ec.redirect(ec.getRequestContextPath().concat("/app/paciente/consultar"));
            } catch (IOException ex) {
                Logger.getLogger(ExamenFisicoGinecBean.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    public void modificar() {
        try {
            ejc.edit(examenFisico);

        } catch (Exception ex) {
            Logger.getLogger(ExamenFisicoGinecBean.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save() {
        try {
            try {/*
                examenFisico.setBus("Si".equals(bus));
                if (file != null) {
                    examenFisico.setImagenMamas(file.getContents());
                }
                examenFisico.setExpedientePacientecedula(paciente.getExpediente());
                ejc.create(examenFisico);
                 */
            } catch (Exception ex) {
                Logger.getLogger(ExamenFisicoGinecBean.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            examenFisico = new ExamenGinecologia();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen Agregado", "Examen Agregado Correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception ex) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Error, examen No Se Pudo Agregar ");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /*
    public void uploadListener(FileUploadEvent e) {
        file = e.getFile();

        if (file != null) {
//                String str = e.getFile().getFileName();
//                String ext = str.substring(str.lastIndexOf('.'), str.length()); //obtener la extension
//                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Imagen Subida", "La imagen " + file.getFileName() + " se ha subido.");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

    }

    public String getImageContentsAsBase64() {
        return Base64.getEncoder().encodeToString(file.getContents());
    }
     */
    public void volver_a_informacion() {
        try {

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            String URL = ec.getRequestContextPath() + "/app/paciente/informacion";

            ec.redirect(URL);

        } catch (Exception ex) {
        }
    }

}
