package com.sicom.web.beans;

import com.sicom.controller.PacienteJpaController;
import com.sicom.controller.ResponsableJpaController;
import com.sicom.entities.Paciente;
import com.sicom.entities.Responsable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    private List<Paciente> listaPaciente;
    private List<Responsable> listaResponsable;
    private final PacienteJpaController pjc;
    private final ResponsableJpaController rjc;
    
    /**
     * Constructor
     */
    public PacienteBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        nuevoPaciente = new Paciente();
        selectedPaciente = new Paciente();
        nuevoResponsable = new Responsable();
        listaPaciente = new ArrayList<>();
        listaResponsable = new ArrayList<>();
        pjc = new PacienteJpaController(emf);
        rjc = new ResponsableJpaController(emf);
    }

    /**
     * Agregar paciente
     */
    public void agregar() {
        try {            
            Paciente paciente = pjc.findPaciente(nuevoPaciente.getCedula());

            // Valida si el paciente ya existe
            if(paciente != null && paciente.getCedula().equals(nuevoPaciente.getCedula())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El paciente ya existe, por favor digite una identificación diferente de " + paciente.getCedula(), null));
            } else if(listaResponsable.size() < 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe existir al menos un responsable asignado al paciente " + nuevoPaciente.getCedula(), null));
            } else {
                nuevoPaciente.setResponsableList(listaResponsable);
                pjc.create(nuevoPaciente);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paciente creado exitosamente.", null));
                nuevoPaciente = new Paciente();
                nuevoResponsable = new Responsable();
                listaResponsable = new ArrayList<>();
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Modificar paciente
     * @return agregar.xhtml
     */
    public String modificar() {
        try {            
            if(selectedPaciente.getResponsableList().size() < 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe existir al menos un responsable asignado al paciente " + selectedPaciente.getCedula(), null));
            } else {
                pjc.edit(selectedPaciente);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información actualizada exitosamente.", null));
                selectedPaciente = new Paciente();
                nuevoResponsable = new Responsable();
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "agregar?faces-redirect=true";
    }
    
    public String consultarPaciente() {
        selectedPaciente = pjc.findPaciente(nuevoPaciente.getCedula());

        if (selectedPaciente != null) {
            selectedPaciente.setResponsableList(rjc.findResponsableByCedulaPaciente(selectedPaciente.getCedula()));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paciente", selectedPaciente);
            
            return "editar?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El paciente con la identificación " + nuevoPaciente.getCedula() + " no ha sido encontrado", null));
            
            return null;
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

    /**
     *
     * Calcula la edad del paciente ya sea nuevo o selected
     *
     * @return edad
     */
    public int PacienteNuevoEdad() {
        Paciente aux = (nuevoPaciente != null) ? nuevoPaciente : selectedPaciente;

        if (aux != null && aux.getNacimiento() != null) {

            DateTime birthdate = new DateTime(aux.getNacimiento());
            DateTime now = new DateTime();

            return Years.yearsBetween(birthdate, now).getYears();
        } else {
            return 0;
        }
    }

    public int calculaEdad() {
        if (selectedPaciente.getNacimiento() != null) {
            LocalDate birthdate = new LocalDate(selectedPaciente.getNacimiento());
            LocalDate now = new LocalDate();
            return Years.yearsBetween(birthdate, now).getYears();
        }
        return 0;
    }

    /**
     * @return the listaPaciente
     */
    public List<Paciente> getListaPaciente() {
        return listaPaciente;
    }

    /**
     * @param listaPaciente the listaPaciente to set
     */
    public void setListaPaciente(List<Paciente> listaPaciente) {
        this.listaPaciente = listaPaciente;
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