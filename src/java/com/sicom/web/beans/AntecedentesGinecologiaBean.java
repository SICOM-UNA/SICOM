package com.sicom.web.beans;

import com.sicom.controller.AntecedentesGinecologiaJpaController;
import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Paciente;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class AntecedentesGinecologiaBean {

    private AntecedentesGinecologia antecedentesGinecologia;
    private final AntecedentesGinecologiaJpaController agc;
    private Date fecha;
    private Paciente paciente;
    private Boolean antecedenteNuevo;

    /**
     * Constructor
     */
    public AntecedentesGinecologiaBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        agc = new AntecedentesGinecologiaJpaController(emf);

        paciente = (Paciente) ec.getSessionMap().remove("paciente");
        antecedentesGinecologia = (AntecedentesGinecologia) ec.getSessionMap().remove("antecedente");

        if (paciente != null) {
            if (antecedentesGinecologia == null) {
                antecedentesGinecologia = new AntecedentesGinecologia();
                antecedenteNuevo = true;
            }else{
                antecedenteNuevo = false;
            }
        } else {
            try {
                String URL = ec.getRequestContextPath() + "/app/paciente/consultar#formulario";
                ec.redirect(URL);
            } catch (IOException ex) {
                Logger.getLogger(AntecedentesGinecologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void modificar() throws Exception {
        agc.edit(antecedentesGinecologia);
    }

    
    public void save() {

        if (antecedenteNuevo) {
            try {
                antecedentesGinecologia.setFecha(new Date());
                antecedentesGinecologia.setExpedientePacientecedula(paciente.getExpediente());
                agc.create(antecedentesGinecologia);
                
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información agregada exitósamente.", null));
                String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
                ec.getRequestMap().put("paciente", paciente);
                ec.redirect(URL);

            } catch (IllegalOrphanException | IOException ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                antecedentesGinecologia.setFecha(new Date());
                antecedentesGinecologia.setExpedientePacientecedula(paciente.getExpediente());
                agc.edit(antecedentesGinecologia);
                
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información modificada exitósamente.", null));
                String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
                ec.getRequestMap().put("paciente", paciente);
                ec.redirect(URL);

            } catch (IllegalOrphanException | IOException ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public void cancelarAction() {

    }

    public AntecedentesGinecologia getObjAntecedente() {
        return antecedentesGinecologia;
    }

    public void setObjAntecedente(AntecedentesGinecologia nuevoAntecedente) {
        this.antecedentesGinecologia = nuevoAntecedente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
