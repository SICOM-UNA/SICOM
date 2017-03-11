package com.sicom.web.beans;

import com.sicom.controller.ValorJpaController;
import com.sicom.entities.Personal;
import com.sicom.controller.PersonalJpaController;
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
public class PersonalBean {
    private Personal nuevoPersonal;
    private Personal selectedPersonal;
    private List<Personal> listaPersonal;
    private final PersonalJpaController pjc;
    private final ValorJpaController cjv;
    
    /* Wizard */
    private boolean skip;
        
    public PersonalBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        nuevoPersonal = new Personal();
        selectedPersonal = new Personal();
        pjc = new PersonalJpaController(emf);
        cjv = new ValorJpaController(emf);
    }
    
    @PostConstruct
    public void init() {
        listaPersonal = pjc.findPersonalEntities();
    }
    
    public void agregar() throws Exception {
        pjc.create(nuevoPersonal);
    }
    
    public void modificar() throws Exception {
        pjc.edit(selectedPersonal);
    }
    
    public Personal consultarPersonal(String id) {
        return pjc.findPersonal(id);
    }

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.cjv.findByCodeId(codigo);
    }

    public void buscaIdBase() throws IOException{
     
        if (selectedPersonal.getCedula() != null) {
            Personal p = this.pjc.findPersonal(selectedPersonal.getCedula());
            if (p != null) {
                this.selectedPersonal = p;
            }else {
                FacesContext.getCurrentInstance().addMessage("No existe personal asignado a la identificación: ", new FacesMessage(p.getCedula()));
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
            
            FacesMessage msg = new FacesMessage("Personal Agregado Exitosamente: ", this.nuevoPersonal.getNombre()+ nuevoPersonal.getPrimerApellido());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            nuevoPersonal = new Personal();
        } catch (Exception ex) {
            
            FacesMessage msg = new FacesMessage("Error, Personal No Se Pudo Agregar ", this.nuevoPersonal.getNombre()+ nuevoPersonal.getPrimerApellido());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
   
      public int PersonalNuevoEdad(){
        if (this.nuevoPersonal != null && this.nuevoPersonal.getNacimiento() != null) {
            DateTime birthdate = new DateTime(nuevoPersonal.getNacimiento());
            DateTime now = new DateTime();
            return Years.yearsBetween(birthdate, now).getYears();
        } else {
            return 0;
        }
    }
    
  public int calculaEdad(){
        if (selectedPersonal.getNacimiento() != null) {
            LocalDate birthdate = new LocalDate(selectedPersonal.getNacimiento());
            LocalDate now = new LocalDate();
            return Years.yearsBetween(birthdate, now).getYears();
        } else {
            return 0;
        }
    }
  
  
  
    
    /**
     * @return the listaPersonal
     */
    public List<Personal> getListaPersonal() {
        return listaPersonal;
    }

    /**
     * @param listaPersonal the listaPersonal to set
     */
    public void setListaPersonal(List<Personal> listaPersonal) {
        this.listaPersonal = listaPersonal;
    }

    /**
     * @return the nuevoPersonal
     */
    public Personal getNuevoPersonal() {
        return nuevoPersonal;
    }

    /**
     * @param nuevoPersonal the nuevoPersonal to set
     */
    public void setNuevoPersonal(Personal nuevoPersonal) {
        this.nuevoPersonal = nuevoPersonal;
    }

    /**
     * @return the selectedPersonal
     */
    public Personal getSelectedPersonal() {
        return selectedPersonal;
    }

    /**
     * @param selectedPersonal the selectedPersonal to set
     */
    public void setSelectedPersonal(Personal selectedPersonal) {
        this.selectedPersonal = selectedPersonal;
    }
}

   