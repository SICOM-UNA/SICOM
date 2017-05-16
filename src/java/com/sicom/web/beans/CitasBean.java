package com.sicom.web.beans;

import com.sicom.controller.CitaJpaController;
import com.sicom.entities.Cita;
import com.sicom.entities.Departamento;
import com.sicom.entities.Login;
import java.io.IOException;
import javax.persistence.Persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author SICOM
 */
@ManagedBean
@ViewScoped
public class CitasBean implements Serializable {

    public final int DURACION_CITA_MINUTOS = 30;

    private Cita nuevaCita;
    private Cita selectedCita;
    private List<Cita> listaCitas;
    private final CitaJpaController cjc;
    private ScheduleModel eventModel;
    private Departamento departamento;

    private ScheduleEvent event;

    public CitasBean() {
        cjc = new CitaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));

        String url = ((HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest())).getRequestURL().toString();
        url = url.substring(url.lastIndexOf("/") + 1);

        switch (url) {
            case "seleccion": {
                this.validacion_redirect(); //
                break;
            }
            case "citas":
            case "index": {
                this.init_Datos();
                break;
            }

        }
    }

    /*METODOS DEL CONSTRUCTOR*/
    private void init_Citas() {
        boolean generaCitas = false;

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        if (departamento != null) {
            generaCitas = true;
        } else {
            Departamento dpto = ((Login) ec.getSessionMap().get("login")).getPersonal().getDepartamentoId();

            if (dpto.getId() != 4 && dpto.getId() != 1) {
                departamento = dpto;
                generaCitas = true;
            }
        }
        if (generaCitas) {
            int id = departamento.getId();

            listaCitas = cjc.findCitaEntities();
            for (Cita c : listaCitas) {
                if (c.getDepartamentoid().getId() == id) {
                    DefaultScheduleEvent dse = new DefaultScheduleEvent(c.getNombre(), c.getFechaInicial(), c.getFechaFinal(), c);
                    this.eventModel.addEvent(dse);
                }
            }
        } else {
            try {
                ec.redirect(ec.getRequestContextPath().concat("/app/calendario/seleccion"));
            } catch (IOException ex) {
                Logger.getLogger(CitasBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void init_Datos() {
        eventModel = new DefaultScheduleModel();
        event = new DefaultScheduleEvent();
        departamento = (Departamento) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("departamento");
        selectedCita = new Cita();
        nuevaCita = new Cita();
        this.init_Citas();
    }

    private void validacion_redirect() {

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Departamento dpto = ((Login) ec.getSessionMap().get("login")).getPersonal().getDepartamentoId();

        if (dpto.getId() != 4 && dpto.getId() != 1) {
            try {
                departamento = dpto;
                String URL = ec.getRequestContextPath() + "/app/calendario/citas";
                ec.getSessionMap().put("departamento", departamento);
                ec.redirect(URL);
            } catch (IOException ex) {
                Logger.getLogger(CitasBean.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*--------------------------------------------------------------------------------*/
    public void redireccion_a_Citas() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            String URL = ec.getRequestContextPath() + "/app/calendario/citas";
            ec.getSessionMap().put("departamento", departamento);
            ec.redirect(URL);
        } catch (IOException ex) {
            Logger.getLogger(CitasBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar() {
        try {
            nuevaCita = (Cita) event.getData();
            cjc.destroy(nuevaCita.getId());
            eventModel.deleteEvent(event);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita eliminada exitosamente",null);
            addMessage(message);
            nuevaCita = new Cita();
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "La cita no pudo ser eliminada");
            addMessage(message);
        }
    }

    public void addEvent(ActionEvent actionEvent) {
        FacesMessage message;

        if (event.getId() == null) { //Si se va a agregar un evento nuevo
            try {
                Date endDate = new DateTime(event.getStartDate()).plusMinutes(DURACION_CITA_MINUTOS).toDate();

                nuevaCita.setFechaInicial(event.getStartDate());
                nuevaCita.setFechaFinal(endDate);
                nuevaCita.setNombre(event.getTitle());
                nuevaCita.setDepartamentoid(departamento);

                cjc.create(nuevaCita);
                listaCitas.add(nuevaCita);

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita agregada", "Cita agregada correctamente.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "La cita no se pudo agregar");
            }
        } else { //Al actualizar una cita
            try {
                Date endDate = new DateTime(event.getStartDate()).plusMinutes(DURACION_CITA_MINUTOS).toDate();

                selectedCita = (Cita) event.getData();
                selectedCita.setFechaInicial(event.getStartDate());
                selectedCita.setFechaFinal(endDate);
                selectedCita.setNombre(event.getTitle());
                selectedCita.setMotivo(nuevaCita.getMotivo());
                selectedCita.setTelefono(nuevaCita.getTelefono());

                cjc.edit(selectedCita);
                selectedCita = null;

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita moficada", "La cita se actualizo correctamente.");
            } catch (Exception ex) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "La cita no se pudo editar");
            }
        }
        this.addMessage(message);
        nuevaCita = new Cita();
        event = new DefaultScheduleEvent();
        actualizaCitas();
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        nuevaCita = buscarCita();
        selectedCita = nuevaCita;
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), null);
        nuevaCita = new Cita();
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Cita buscarCita() {
        for (Cita c : listaCitas) {
            int id = ((Cita) event.getData()).getId();
            if (Objects.equals(c.getId(), id)) {
                return c;
            }
        }
        return new Cita();
    }

    public void actualizaCitas() {

        int id = departamento.getId();
        listaCitas = cjc.findCitaEntities();
        eventModel.clear();
        for (Cita c : listaCitas) {
            if (c.getDepartamentoid().getId() == id) {
                DefaultScheduleEvent dse = new DefaultScheduleEvent(c.getNombre(), c.getFechaInicial(), c.getFechaFinal(), c);
                this.eventModel.addEvent(dse);
            }
        }
    }

    public boolean validaEliminar() {
        return (event.getId() != null);
    }

    /**
     * @return the nuevaCita
     */
    public Cita getNuevaCita() {
        return nuevaCita;
    }

    /**
     * @param nuevaCita the nuevaCita to set
     */
    public void setNuevaCita(Cita nuevaCita) {
        this.nuevaCita = nuevaCita;
    }

    /**
     * @return the selectedCita
     */
    public Cita getSelectedCita() {
        return selectedCita;
    }

    /**
     * @param selectedCita the selectedCita to set
     */
    public void setSelectedCita(Cita selectedCita) {
        this.selectedCita = selectedCita;
    }

    /**
     * @return the listaCitas
     */
    public List<Cita> getListaCitas() {
        return listaCitas;
    }

    /**
     * @param listaCitas the listaCitas to set
     */
    public void setListaCitas(ArrayList<Cita> listaCitas) {
        this.listaCitas = listaCitas;
    }

    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar.getTime();
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}
