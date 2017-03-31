package com.sicom.web.beans;

import com.sicom.controller.PacienteJpaController;
import com.sicom.controller.ResponsableJpaController;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.Expediente;
import com.sicom.entities.Login;
import com.sicom.entities.Paciente;
import com.sicom.entities.Personal;
import com.sicom.entities.Responsable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * Agregar Paciente Redirecciona a información del paciente si logra
     * agregarlo exitosamente.
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

                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ec.getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paciente agregado exitosamente.", null));
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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

    public void HistoriaClinica() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        if (selectedPaciente != null && !selectedPaciente.getCedula().equals("")) {

            String URL = ec.getRequestContextPath();
            Login log = (Login) ec.getSessionMap().get("login");
            Personal p = log.getPersonal();

            int consultorio = p.getDepartamentoId().getId();
            boolean permiso_editar = (p.getAutorizacionNivel().getNivel() < 5);

            String direccion = createUrl(consultorio, permiso_editar);

            if (direccion.equals("")) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No posee los permisos para acceder.", null));
            } else {
                try {
                    subirObjetosExternalContext(consultorio, ec);
                    URL += direccion;
                    ec.redirect(URL);
                } catch (IOException ex) {
                    Logger.getLogger(PacienteBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                String URL = ec.getRequestContextPath();
                URL += "/app/paciente/consultar";
                ec.redirect(URL);
            } catch (IOException ex) {
                Logger.getLogger(ExpedienteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param consultorio
     * @param permiso_editar
     * @return String URL
     */
    private String createUrl(int consultorio, boolean permiso_editar) {
        switch (consultorio) {
            case 2: // Ginecologia
                if (permiso_editar) {
                    return "/app/consultorios/ginecologia/antecedentes";
                } else {
                    return "/app/consultorios/ginecologia/consultarAntecedentes";
                }
            case 3: // Odontologia
                if (permiso_editar) {
                    return "/app/consultorios/odontologia/antecedentes";
                } else {
                    return "/app/consultorios/odontologia/consultarAntecedentes";
                }
            default:
                return "";
        }
    }

    /**
     *
     * @param consultorio
     * @return Object
     */
    private void subirObjetosExternalContext(int consultorio, ExternalContext ec) {

        Expediente e = selectedPaciente.getExpediente();

        switch (consultorio) {
            case 2:
                ec.getSessionMap().put("antecedente", e.getAntecedentesGinecologia());
                break;
            case 3:
                ec.getSessionMap().put("antecedente", e.getAntecedentesOdontologia());
                break;
        }
        ec.getSessionMap().put("paciente", selectedPaciente);
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