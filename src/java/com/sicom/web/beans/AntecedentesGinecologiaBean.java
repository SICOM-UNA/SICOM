package com.sicom.web.beans;

import com.sicom.controller.AntecedentesGinecologiaJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Paciente;
import java.io.IOException;
import java.util.List;
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
    private List<AntecedentesGinecologia> listaAntecedentes;
    private final AntecedentesGinecologiaJpaController agjc;
    private final ValorJpaController vjc;
    private Paciente paciente;

    public AntecedentesGinecologiaBean() {
        paciente = PacienteBean.getSavedPaciente();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        agjc = new AntecedentesGinecologiaJpaController(emf);
        vjc = new ValorJpaController(emf);

        if (paciente != null) {
            antecedentesGinecologia = new AntecedentesGinecologia();
            antecedentesGinecologia.setGesta(0);
            antecedentesGinecologia.setPartos(0);
            antecedentesGinecologia.setAbortos(0);
            antecedentesGinecologia.setEctopico(0);
        } else {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/paciente/consultar";
            try {
                ec.redirect(URL);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        //AntecedentesGinecologia obj_aux = consultarHistorial(paciente.getId());
        /*
        if(obj_aux != null){
            antecedentesGinecologia = obj_aux;
        }else{
            antecedentesGinecologia = new AntecedentesGinecologia();
        }
        */
    }

    public void init() {
        listaAntecedentes = agjc.findAntecedentesGinecologiaEntities();
    }

    public void modificar() throws Exception {
        agjc.edit(antecedentesGinecologia);
    }

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.vjc.findByCodeId(codigo);
    }

    public void save() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
            PacienteBean.setSavedPaciente(this.paciente);
            FacesMessage msg = new FacesMessage("Historial Agregado Exitosamente");
            ec.redirect(URL);         
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage("Error, Paciente No Se Pudo Agregar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<AntecedentesGinecologia> getListaAntecedentes() {
        return listaAntecedentes;
    }

    public void setListaAntecedentes(List<AntecedentesGinecologia> listaAntecedentes) {
        this.listaAntecedentes = listaAntecedentes;
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