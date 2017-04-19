package com.sicom.web.beans;

import com.sicom.controller.PacienteJpaController;

import com.sicom.controller.ResponsableJpaController;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.Paciente;
import com.sicom.entities.Responsable;
import java.io.IOException;
import java.io.Serializable;
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

@ManagedBean
@ViewScoped
public class PacienteBean implements Serializable {

    private Paciente nuevoPaciente;
    private Paciente selectedPaciente;
    private Responsable responsable1;
    private Responsable responsable2;
    private Responsable selectedResponsable;
    private List<Paciente> listaPacientes;
    private final PacienteJpaController pjc;
    private final ResponsableJpaController rjc;

    /**
     * Constructor
     */
    public PacienteBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");

        nuevoPaciente = new Paciente();
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
        int index = (p != null && p.getResponsableList() != null) ? p.getResponsableList().size() : 0;

        switch (index) {
            case 0:
                responsable1 = responsable2 = new Responsable();
                responsable1.setNombre("No definido");
                responsable2.setNombre("No definido");
                break;
            case 1:
                responsable1 = p.getResponsableList().get(0);
                responsable2 = new Responsable();
                responsable2.setNombre("No definido");
                break;
            case 2:
                responsable1 = p.getResponsableList().get(0);
                responsable2 = p.getResponsableList().get(1);
                break;

        }

        Responsable aux = (Responsable) sessionMap.remove("selectedResponsable");
        selectedResponsable = (aux != null) ? aux : new Responsable();
    }

    /**
     * Agregar Paciente Redirecciona a información del paciente si logra
     * agregarlo exitosamente.
     */
    public void agregar() {
        try {
            Paciente p = pjc.findPaciente(nuevoPaciente.getCedula());

            if (p != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede agregar, ya existe el paciente con la cédula: ", nuevoPaciente.getCedula()));
            } else {
                pjc.create(nuevoPaciente);
                selectedPaciente = nuevoPaciente;

                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();

                ec.getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paciente agregado exitosamente.", null));

                buscaIdBase();
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    /**
     * Redireccion al form modificar
     */
    public void modificaRedirect() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath() + "/app/paciente/editar#formulario";
            ec.getSessionMap().put("paciente", selectedPaciente);
            ec.redirect(URL);
        } catch (IOException ex) {
            Logger.getLogger(PacienteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarAction() {

        try {
            pjc.edit(selectedPaciente);

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            ec.getFlash().setKeepMessages(true);
            ec.getSessionMap().put("paciente", selectedPaciente);
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paciente modificado exitosamente.", null));

            String URL = ec.getRequestContextPath() + "/app/paciente/informacion#datos";

            ec.redirect(URL);
        } catch (IOException ex) {
            Logger.getLogger(PacienteBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PacienteBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PacienteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancelarAction() {
        this.buscaIdBase();
    }

    public void modificaResposableRedirect(int i) {
    }

    //--------------------------------------------------------------------------
    // GENERAL METHODS
    public void buscaIdBase() {
        Paciente p = (selectedPaciente.getCedula() != null) ? selectedPaciente : null;

        if (p != null) {
            String id = p.getCedula();
            selectedPaciente = null;
            selectedPaciente = this.pjc.findPaciente(p.getCedula());
            
            if (selectedPaciente != null) {
                try {

                    FacesContext fc = FacesContext.getCurrentInstance();
                    ExternalContext ec = fc.getExternalContext();

                    String URL = ec.getRequestContextPath() + "/app/paciente/informacion#datos";
                    ec.getSessionMap().put("paciente", selectedPaciente);
                    ec.redirect(URL);
                } catch (IOException ex) {
                    Logger.getLogger(PacienteBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe paciente asignado a la identificación: ", id));
            }

        }
    }

    public Date disablePastDates() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -43800); //43800 = 120 años
        return c.getTime();
    }

    public int calculaEdad() {
        if (selectedPaciente.getNacimiento() != null) {
            LocalDate birthdate = new LocalDate(selectedPaciente.getNacimiento());
            LocalDate now = new LocalDate();
            return Years.yearsBetween(birthdate, now).getYears();
        }
        return 0;
    }

    public Paciente consultarPaciente(String id) {
        return pjc.findPaciente(id);
    }

    public void verificaID() {
        if (nuevoPaciente.getCedula() != null) {
            selectedPaciente.setCedula(nuevoPaciente.getCedula());
            this.buscaIdBase();
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
     *
     * @return Responsable
     */
    public Responsable getResponsable1() {
        return responsable1;
    }

    /**
     *
     * @param responsable1
     */
    public void setResponsable1(Responsable responsable1) {
        this.responsable1 = responsable1;
    }

    /**
     *
     * @return Responsable
     */
    public Responsable getResponsable2() {
        return responsable2;
    }

    /**
     *
     * @param responsable2
     */
    public void setResponsable2(Responsable responsable2) {
        this.responsable2 = responsable2;
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
}
