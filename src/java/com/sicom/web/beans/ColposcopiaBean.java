package com.sicom.web.beans;

import com.sicom.controller.ValorJpaController;
import com.sicom.entities.ExamenColposcopia;
import com.sicom.entities.Paciente;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class ColposcopiaBean implements Serializable{
    
    private ExamenColposcopia examen;
    private Paciente paciente;
   
    public ColposcopiaBean(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        examen = new ExamenColposcopia();
        
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        paciente = (Paciente) ec.getSessionMap().get("paciente");
       
    }

    public ExamenColposcopia getExamen() {
        return examen;
    }

    public void setExamen(ExamenColposcopia examen) {
        this.examen = examen;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    
    
    
}