package com.sicom.web.beans;

import com.sicom.controller.PacienteJpaController;

import com.sicom.controller.ResponsableJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.Paciente;
import com.sicom.entities.Responsable;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class PacienteBean {

    private Paciente nuevoPaciente, selectedPaciente;
    private static Paciente savedPaciente;
    private Responsable nuevoResponsable;
    private List<Responsable> listaResponsable;
    private List<Paciente> listaPacientes;
    private final PacienteJpaController pjc;
    private final ResponsableJpaController rjc;
    private final ValorJpaController cjv;

    /* Wizard */
    private boolean skip;

    public PacienteBean() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");

        nuevoPaciente = new Paciente();

        if (savedPaciente == null) {
            selectedPaciente = new Paciente();
        } else {
            selectedPaciente = savedPaciente;
            savedPaciente = null;
        }

        nuevoResponsable = new Responsable();
        pjc = new PacienteJpaController(emf);
        cjv = new ValorJpaController(emf);
        rjc = new ResponsableJpaController(emf);
    }

    @PostConstruct
    public void init() {
        listaPacientes = pjc.findPacienteEntities();
    }

    public String reinit() {
        nuevoResponsable = new Responsable();
        return null;
    }

    //--------------------------------------------------------------------------
    // AGREGAR PACIENTE
    public void agregar() throws Exception {
        pjc.create(nuevoPaciente);

        if (nuevoResponsable.getCedula() != null) {
            nuevoResponsable.setPacientecedula(nuevoPaciente);
            rjc.create(nuevoResponsable);
        }
    }

    public void save() {
        try {
            agregar();

            FacesMessage msg = new FacesMessage("Paciente Agregado Exitosamente: ", this.nuevoPaciente.getNombre() + nuevoPaciente.getPrimerApellido());
            FacesContext.getCurrentInstance().addMessage(null, msg);

            selectedPaciente = nuevoPaciente;
            nuevoPaciente = new Paciente();

            buscaIdBase();

        } catch (Exception ex) {

            FacesMessage msg = new FacesMessage("Error, Paciente No Se Pudo Agregar ", this.nuevoPaciente.getNombre() + nuevoPaciente.getPrimerApellido());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public int PacienteNuevoEdad() {
        if (this.nuevoPaciente != null && this.nuevoPaciente.getNacimiento() != null) {

            DateTime birthdate = new DateTime(nuevoPaciente.getNacimiento());
            DateTime now = new DateTime();

            return Years.yearsBetween(birthdate, now).getYears();
        } else {
            return 0;
        }
    }

    //--------------------------------------------------------------------------
    // INFORMACION DEL PACIENTE
    public void modificaRedirect() {
        try {

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            String URL = ec.getRequestContextPath() + "/app/paciente/editar";

            savedPaciente = selectedPaciente;

            ec.redirect(URL);

        } catch (IOException ex) {
        } catch (Exception ex) {
        }
    }

    public void modificarAction() {
        try {

            pjc.edit(selectedPaciente);

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            String URL = ec.getRequestContextPath() + "/app/paciente/informacion";

            savedPaciente = selectedPaciente;

            ec.redirect(URL);

        } catch (IOException ex) {
        } catch (Exception ex) {
        }
    }

    public void cancelarAction() {
        this.buscaIdBase();
    }

    //--------------------------------------------------------------------------
    // HISTORIA CLINICA DE PACIENTE
    public void HistoriaClinica(boolean permiso_editar, int consultorio) {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String URL = ec.getRequestContextPath();

        savedPaciente = selectedPaciente;

        switch (consultorio) {
            case 0:
                if (permiso_editar) {
                    URL += "/app/consultorios/ginecologia/antecedentes";
                } else {
                    URL += "/app/consultorios/ginecologia/consultarAntecedentes";
                }
                break;
            case 1:
                if (permiso_editar) {
                    URL += "/app/consultorios/odontologia/antecedentes";
                } else {
                    URL += "/app/consultorios/odontologia/consultarAntecedentes";
                }
        }
        try {
            ec.redirect(URL);
        } catch (IOException ex) {
        }
    }

    //--------------------------------------------------------------------------
    // GENERAL METHODS
    public void buscaIdBase() {

        if (selectedPaciente.getCedula() != null) {

            String id = selectedPaciente.getCedula();

            Paciente p = this.pjc.findPaciente(selectedPaciente.getCedula());

            if (p != null) {

                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();

                String URL = ec.getRequestContextPath() + "/app/paciente/informacion";

                savedPaciente = p;

                try {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No existe paciente asignado a la identificación: " + id));
                    ec.redirect(URL);
                } catch (IOException ex) {

                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No existe paciente asignado a la identificación: " + id));
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

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.cjv.findByCodeId(codigo);
    }

    public void verificaID() {
        if (nuevoPaciente.getCedula() != null) {
            selectedPaciente.setCedula(nuevoPaciente.getCedula());
            this.buscaIdBase();
        }
    }

    //--------------------------------------------------------------------------\
    //  COLLECTOR
    //--------------------------------------------------------------------------\
    //  GETTERS & SETTERS
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
    public Responsable getNuevoResponsable() {
        return nuevoResponsable;
    }

    /**
     * @param nuevoResponsable the nuevoResponsable to set
     */
    public void setNuevoResponsable(Responsable nuevoResponsable) {
        this.nuevoResponsable = nuevoResponsable;
    }

    public static Paciente getSavedPaciente() {
        return savedPaciente;
    }

    public static void setSavedPaciente(Paciente savedPaciente) {
        PacienteBean.savedPaciente = savedPaciente;
    }

    public List<Responsable> getListaResponsables() {
        return listaResponsable;
    }

}
