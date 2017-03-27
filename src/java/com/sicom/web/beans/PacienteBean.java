package com.sicom.web.beans;

import com.sicom.controller.PacienteJpaController;

import com.sicom.controller.ResponsableJpaController;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Expediente;
import com.sicom.entities.Login;
import com.sicom.entities.Paciente;
import com.sicom.entities.Personal;
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

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map<String, Object> sessionMap = ec.getSessionMap();

        Paciente p = (Paciente) sessionMap.remove("paciente");
        selectedPaciente = (p != null) ? p : new Paciente();

        Responsable aux1 = (Responsable) sessionMap.remove("responsable1");
        responsable1 = (aux1 != null) ? aux1 : new Responsable();

        Responsable aux2 = (Responsable) sessionMap.remove("responsable2");
        responsable2 = (aux2 != null) ? aux2 : new Responsable();

        Responsable aux3 = (Responsable) sessionMap.remove("selectedResponsable");
        selectedResponsable = (aux3 != null) ? aux3 : new Responsable();

    }

    @PostConstruct
    public void init() {
        listaPacientes = pjc.findPacienteEntities();
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

    public void cancelarAction() {
        this.buscaIdBase();
    }

    public void modificaResposableRedirect(int i) {
    }

    //--------------------------------------------------------------------------
    // GENERAL METHODS
    public void buscaIdBase() {
        Paciente p = (selectedPaciente.getCedula() != null) ? selectedPaciente : ((nuevoPaciente.getCedula() != null) ? nuevoPaciente : null);

        if (p != null) {
            String id = selectedPaciente.getCedula();
            selectedPaciente = this.pjc.findPaciente(p.getCedula());

            if (selectedPaciente != null) {
                try {

                    List<Responsable> lista = selectedPaciente.getResponsableList();

                    if (lista != null) {
                        switch (lista.size()) {
                            case 0:
                                responsable1.setNombre("No Asignado");
                                responsable2.setNombre("No Asignado");
                                break;
                            case 1:
                                responsable1 = lista.get(0);
                                responsable2.setNombre("No Asignado");
                                break;
                            case 2:
                                responsable1 = lista.get(0);
                                responsable2 = lista.get(1);
                                break;
                        }
                    }

                    FacesContext fc = FacesContext.getCurrentInstance();
                    ExternalContext ec = fc.getExternalContext();

                    String URL = ec.getRequestContextPath() + "/app/paciente/informacion#datos";
                    ec.getSessionMap().put("paciente", selectedPaciente);
                    ec.getSessionMap().put("responsable1", responsable1);
                    ec.getSessionMap().put("responsable2", responsable2);
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

}
