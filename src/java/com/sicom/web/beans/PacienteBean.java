package com.sicom.web.beans;

import com.sicom.controller.PacienteJpaController;

import com.sicom.controller.ResponsableJpaController;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.Paciente;
import com.sicom.entities.Responsable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class PacienteBean implements Serializable {

    private Paciente nuevoPaciente;
    private Paciente selectedPaciente;
    private Responsable nuevoResponsable;
    private List<Responsable> listaResponsable;
    private Responsable selectedResponsable;
    private List<Paciente> listaPacientes;
    private final PacienteJpaController pjc;
    private final ResponsableJpaController rjc;

    public PacienteBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        nuevoPaciente = new Paciente();
        selectedPaciente = new Paciente();
        nuevoResponsable = new Responsable();
        listaResponsable = new ArrayList<>();
        listaPacientes = new ArrayList<>();
        pjc = new PacienteJpaController(emf);
        rjc = new ResponsableJpaController(emf);
    }

    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map<String, Object> sessionMap = ec.getSessionMap();

        Paciente p = (Paciente) sessionMap.get("paciente");

        if (p != null) {
            selectedPaciente = pjc.findPaciente(p.getCedula());
            sessionMap.put("paciente", selectedPaciente);
        } else {
            selectedPaciente = new Paciente();
        }
    }

    /**
     * Agregar paciente
     * @return la página a la cual será redireccionado el usuario 
     */
    public String agregar() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            Paciente p = pjc.findPaciente(nuevoPaciente.getCedula());

            if (p != null) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede agregar, ya existe el paciente con la cédula: ", nuevoPaciente.getCedula()));
            } else {
                nuevoPaciente.setResponsableList(listaResponsable);
                pjc.create(nuevoPaciente);
                fc.getExternalContext().getSessionMap().put("paciente", nuevoPaciente);
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paciente agregado exitosamente.", null));
                nuevoPaciente = new Paciente();
                
                return "informacion?faces-redirect=true";
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "agregar";
    }

    /**
     * Modificar paciente
     * @return la página a la cual será redireccionado el usuario
     */
    public String modificar() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            pjc.edit(selectedPaciente);
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información actualizada exitosamente.", null));
            fc.getExternalContext().getFlash().setKeepMessages(true);
            selectedPaciente = new Paciente();
            nuevoResponsable = new Responsable();
            
            return "informacion?faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Muestra los datos del paciente solicitado por el usuario
     * @return la página a la cual será redireccionado el usuario
     */
    public String consultar() {
        selectedPaciente = pjc.findPaciente(nuevoPaciente.getCedula());

        if (selectedPaciente != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paciente", selectedPaciente);
            
            return "informacion?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El paciente con la identificación " + nuevoPaciente.getCedula() + " no ha sido encontrado", null));
            
            return "consultar";
        }
    }
    
    /**
     * Consultar paciente por cédula
     */
    public void consultarPacientePorCedula() {
        selectedPaciente = pjc.findPaciente(nuevoPaciente.getCedula());

        if (selectedPaciente != null) {
            selectedPaciente.setResponsableList(rjc.findResponsableByCedulaPaciente(selectedPaciente.getCedula()));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paciente", selectedPaciente);
            RequestContext.getCurrentInstance().execute("PF('existePacienteDialog').show()");
        }
    }
    
    /**
     * Borra la información del responsable actual después de ser ingresado en el collector
     * @return null
     */
    public String reinit() {
        nuevoResponsable = new Responsable();
        
        return null;
    }

    public Date disablePastDates() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -43800); //43800 = 120 años
        return c.getTime();
    }

    /**
     * @return the listaPacientes
     */
    public List<Paciente> getListaPacientes() {
        return listaPacientes;
    }

    /**
     * @param listaPacientes the listaPacientes to set
     */
    public void setListaPacientes(List<Paciente> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    /**
     * @return the nuevoPaciente
     */
    public Paciente getNuevoPaciente() {
        return nuevoPaciente;
    }

    /**
     * @param nuevoPaciente the nuevoPaciente to set
     */
    public void setNuevoPaciente(Paciente nuevoPaciente) {
        this.nuevoPaciente = nuevoPaciente;
    }

    /**
     * @return the selectedPaciente
     */
    public Paciente getSelectedPaciente() {
        return selectedPaciente;
    }

    /**
     * @param selectedPaciente the selectedPaciente to set
     */
    public void setSelectedPaciente(Paciente selectedPaciente) {
        this.selectedPaciente = selectedPaciente;
    }

    /**
     *
     * @return Responsable
     */
    public Responsable getSelectedResponsable() {
        return selectedResponsable;
    }

    /**
     *
     * @param selectedResponsable
     */
    public void setSelectedResponsable(Responsable selectedResponsable) {
        this.selectedResponsable = selectedResponsable;
    }

    public void resetSelectedPaciente(){
        selectedPaciente = new Paciente();
    }

    /**
     * @return the nuevoResponsable
     */
    public Responsable getNuevoResponsable() {
        return nuevoResponsable;
    }

    /**
     * @param nuevoResponsable the nuevoResponsable to set
     */
    public void setNuevoResponsable(Responsable nuevoResponsable) {
        this.nuevoResponsable = nuevoResponsable;
    }

    /**
     * @return the listaResponsable
     */
    public List<Responsable> getListaResponsable() {
        return listaResponsable;
    }

    /**
     * @param listaResponsable the listaResponsable to set
     */
    public void setListaResponsable(List<Responsable> listaResponsable) {
        this.listaResponsable = listaResponsable;
    }
}
