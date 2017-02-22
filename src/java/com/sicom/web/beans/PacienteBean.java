package com.sicom.web.beans;

import com.sicom.controller.PacienteJpaController;

import com.sicom.controller.ResponsableJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.Paciente;
import com.sicom.entities.Responsable;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.primefaces.event.FlowEvent;

@ManagedBean
@ViewScoped
public class PacienteBean {
    private Paciente nuevoPaciente;
    private Paciente selectedPaciente;
    private Responsable nuevoResponsable1;
    private Responsable nuevoResponsable2;
    private List<Paciente> listaPacientes;
    private final PacienteJpaController pjc;
    private final ResponsableJpaController rjc;
    private final ValorJpaController cjv;
    
    /* Wizard */
    private boolean skip;
        
    public PacienteBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        nuevoPaciente = new Paciente();
        selectedPaciente = new Paciente();
        nuevoResponsable1 = new Responsable();
        nuevoResponsable2 = new Responsable();
        pjc = new PacienteJpaController(emf);
        cjv = new ValorJpaController(emf);
        rjc=new ResponsableJpaController(emf);
    }
    
    @PostConstruct
    public void init() {
        listaPacientes = pjc.findPacienteEntities();
    }
    
    public void agregar() throws Exception {
        pjc.create(nuevoPaciente);
        if(nuevoResponsable1.getId()!=null){
            nuevoResponsable1.setPacienteid(nuevoPaciente);
            rjc.create(nuevoResponsable1);
        }
        if(nuevoResponsable2.getId()!=null){
            nuevoResponsable2.setPacienteid(nuevoPaciente);
            rjc.create(nuevoResponsable2);
        }
        
        
    }
    
    public void modificar() throws Exception {
        pjc.edit(selectedPaciente);
    }
    
    public Paciente consultarPaciente(String id) {
        return pjc.findPaciente(id);
    }

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.cjv.findByCodeId(codigo);
    }

    public void buscaIdBase() throws IOException{
     
        if (selectedPaciente.getId() != null) {

            Paciente p = this.pjc.findPaciente(selectedPaciente.getId());

            if (p != null) {
                this.selectedPaciente = p;
            } else {
                FacesContext.getCurrentInstance().addMessage("No existe paciente asignado a la identificación: ", new FacesMessage(p.getId()));
            } 
        }
    }
    
    /* Wizard Methods*/
     
    public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
    
    public void save() {        
        try {
            agregar();
            
            FacesMessage msg = new FacesMessage("Paciente Agregado Exitosamente: ", this.nuevoPaciente.getNombre()+ nuevoPaciente.getPrimerApellido());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            nuevoPaciente = new Paciente();
            
            
        
        } catch (Exception ex) {
            
            FacesMessage msg = new FacesMessage("Error, Paciente No Se Pudo Agregar ", this.nuevoPaciente.getNombre()+ nuevoPaciente.getPrimerApellido());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
   
    public int PacienteNuevoEdad(){
        if (this.nuevoPaciente != null && this.nuevoPaciente.getNacimiento() != null) {
            
            DateTime birthdate = new DateTime(nuevoPaciente.getNacimiento());
            DateTime now = new DateTime();
            
            return Years.yearsBetween(birthdate, now).getYears();
        } else {
            return 0;
        }
    }
    
    public int calculaEdad(){
        if (selectedPaciente.getNacimiento() != null) {
            LocalDate birthdate = new LocalDate(selectedPaciente.getNacimiento());
            LocalDate now = new LocalDate();
            return Years.yearsBetween(birthdate, now).getYears();
        } else {
            return 0;
        }
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
     * @return the nuevoResponsable1
     */
    public Responsable getNuevoResponsable1() {
        return nuevoResponsable1;
    }

    /**
     * @param nuevoResponsable1 the nuevoResponsable1 to set
     */
    public void setNuevoResponsable1(Responsable nuevoResponsable1) {
        this.nuevoResponsable1 = nuevoResponsable1;
    }

    /**
     * @return the nuevoResponsable2
     */
    public Responsable getNuevoResponsable2() {
        return nuevoResponsable2;
    }

    /**
     * @param nuevoResponsable2 the nuevoResponsable2 to set
     */
    public void setNuevoResponsable2(Responsable nuevoResponsable2) {
        this.nuevoResponsable2 = nuevoResponsable2;
    }
}