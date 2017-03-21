package com.sicom.web.beans;

import com.sicom.controller.AntecedentesGinecologiaJpaController;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Paciente;
import java.io.IOException;
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
    private final AntecedentesGinecologiaJpaController agjc;
    private Paciente paciente;

    /**
     * Constructor
     */
    public AntecedentesGinecologiaBean() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");

        agjc = new AntecedentesGinecologiaJpaController(emf);
        boolean flag = false;

        if (validaPersonal()) {
            paciente = (Paciente) ec.getSessionMap().remove("paciente");
            Object obj = ec.getSessionMap().remove("codigoExp");
            int codigoExpediente = (obj != null) ? (int) obj : -1;

            if (paciente != null && codigoExpediente != -1) {
                init(ec);
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }
        if (flag) {
            redirect(ec);
        }
    }

    private void init(ExternalContext ec) {

        Object obj = ec.getSessionMap().get("antecedente");
        antecedentesGinecologia = (obj != null) ? (AntecedentesGinecologia) obj : new AntecedentesGinecologia();

    }

    public void modificar() throws Exception {
        agjc.edit(antecedentesGinecologia);
    }

    public void save() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            String URL = ec.getRequestContextPath() + "/app/paciente/informacion#datos";
            ec.getSessionMap().put("paciente", paciente);

            ec.redirect(URL);
        } catch (IOException ex) {
            FacesMessage msg = new FacesMessage("Error, Paciente No Se Pudo Agregar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void cancelarAction() {

    }

    public final boolean validaPersonal() {
        return true/*(usuario.getAutorizacion() == 1 && usuario.getAutorizacion() == 1)*/;
    }

    public final void redirect(ExternalContext ec) {
        try {
            String URL = ec.getRequestContextPath() + "/app/paciente/consultar#formulario";
            ec.redirect(URL);
        } catch (IOException ex) {
            Logger.getLogger(AntecedentesGinecologiaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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

}
