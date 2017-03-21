package com.sicom.web.beans;

import com.sicom.controller.ExamenGinecologiaJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.ExamenGinecologia;
import com.sicom.entities.Paciente;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class ExamenFisicoGinecBean {
   private ExamenGinecologia examenFisico;
    private Date hoy;
    private ExamenGinecologiaJpaController ejc;
    private UploadedFile file;
    private Paciente paciente;
    private StreamedContent imagen;
    private String bus;
    private byte[] imageContents;

    public ExamenFisicoGinecBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        ejc = new ExamenGinecologiaJpaController(emf);
        imagen = null;
        bus = "";
        examenFisico = new ExamenGinecologia();

        imageContents = null;
        hoy = new Date();
        file = null;
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public StreamedContent getImagen() {
        return imagen;
    }

    public void setImagen(StreamedContent imagen) {
        this.imagen = imagen;
    }

    public byte[] getImageContents() {
        return imageContents;
    }

    public void setImageContents(byte[] imageContents) {
        this.imageContents = imageContents;
    }

    public void agregar() {
        try {
            examenFisico.setBus("Si".equals(bus));
            examenFisico.setImagenMamas(file.getContents());
            examenFisico.setExpedientePacientecedula(paciente.getExpediente());
            ejc.create(examenFisico);

        } catch (Exception ex) {
            Logger.getLogger(ExamenFisicoGinecBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificar() {
        try {
            ejc.edit(examenFisico);
        } catch (Exception ex) {
            Logger.getLogger(ExamenFisicoGinecBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save() {
        try {
            agregar();
            examenFisico = new ExamenGinecologia();
            volver_a_informacion();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Examen Agregado", "Examen Agregado Correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception ex) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Error, examen No Se Pudo Agregar ");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void uploadListener(FileUploadEvent e) {
        file = e.getFile();

        if (file != null) {
            imageContents = file.getContents();
            try {
                String str = file.getFileName();
                String ext = str.substring(str.lastIndexOf('.'), str.length()); //obtener la extension
                String date = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                String path = ec.getApplicationContextPath();
                file.write(path + "/imagenes/" + date + ext);

                //imagen = new DefaultStreamedContent(new ByteArrayInputStream(file.getContents()));
                //FacesContext fc = FacesContext.getCurrentInstance();
                //ExternalContext ec = fc.getExternalContext();
                String sRootPath = new File(date + ext).getAbsolutePath();
                file.write(sRootPath);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Imagen Subida", "La imagen " + file.getFileName() + " se ha subido.");
                FacesContext.getCurrentInstance().addMessage(null, message);

            } catch (Exception ex) {
                Logger.getLogger(ExamenFisicoGinecBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public String getImageContentsAsBase64() {
        return Base64.getEncoder().encodeToString(file.getContents());
    }

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
