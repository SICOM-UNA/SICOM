package com.sicom.web.beans;

import com.sicom.controller.AntecedentesGinecologiaJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Paciente;
import java.io.IOException;
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
public class AntecedentesGinecologiaBean {

    @ManagedProperty(value = "#{ValoresBean}")
    private ValoresBean valor;

    private AntecedentesGinecologia antecedentesGinecologia;
    private final AntecedentesGinecologiaJpaController agjc;
    private final ValorJpaController vjc;
    private Paciente paciente;

    /**
     * Constructor
     */
    public AntecedentesGinecologiaBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        agjc = new AntecedentesGinecologiaJpaController(emf);
        vjc = new ValorJpaController(emf);

        paciente = (Paciente) ec.getSessionMap().remove("paciente");
        boolean flag = false;

        try {
            int codigoExpediente = (int) ec.getSessionMap().remove("codigoExp");

            if (paciente != null) {
                agjc.findAntecedentesGinecologia(codigoExpediente);
            } else {
                flag = true;
            }
        } catch (NullPointerException ex) {
            flag = true;
        }

        if (flag) {
            try {
                String URL = ec.getRequestContextPath() + "/app/paciente/consultar#formulario";
                ec.redirect(URL);
            } catch (IOException ex) {
                Logger.getLogger(AntecedentesGinecologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void modificar() throws Exception {
        agjc.edit(antecedentesGinecologia);
    }

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return valor.getValuesByCodeId(codigo);
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
    
    public void cancelarAction(){
    
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
