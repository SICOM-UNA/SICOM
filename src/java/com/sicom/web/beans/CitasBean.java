package com.sicom.web.beans;

import com.sicom.controller.CitaJpaController;
import com.sicom.entities.Cita;
import com.sicom.entities.Departamento;
import com.sicom.entities.Login;
import com.sicom.entities.Personal;
import java.io.IOException;
import javax.persistence.Persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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

    private Cita nuevaCita;
    private Cita selectedCita;
    private List<Cita> listaCitas;
    private final CitaJpaController cjc;
    private ScheduleModel eventModel;
    private Departamento departamento;

    private ScheduleEvent event = new DefaultScheduleEvent();

    public CitasBean() {
        cjc = new CitaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));

        nuevaCita = new Cita();
        selectedCita = new Cita();

        listaCitas = cjc.findCitaEntities();

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        departamento = (Departamento) ec.getSessionMap().remove("departamento");
    }

    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        Login login = (Login) ec.getSessionMap().get("login");
        Personal p = login.getPersonal();

        if (p != null) {
            int dpto = p.getDepartamentoId().getId();

            if (dpto != 4 && dpto != 1 && departamento == null) {
                try {
                    departamento = p.getDepartamentoId();
                    String URL = ec.getRequestContextPath() + "/app/calendario/citas";
                    ec.getSessionMap().put("departamento", departamento);
                    ec.redirect(URL);
                } catch (IOException ex) {
                    Logger.getLogger(CitasBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (departamento != null) {
                    eventModel = new DefaultScheduleModel();

                    for (Cita c : this.listaCitas) {
                        if (c.getDepartamentoid().getId() == this.departamento.getId()) {
                            this.eventModel.addEvent(new DefaultScheduleEvent(c.getNombre(), c.getFechaInicial(), c.getFechaFinal()));
                        }
                    }
                }
            }
        }
    }

    public void redirigir_a_Citas() {
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
            cjc.destroy(nuevaCita.getId());
            eventModel.deleteEvent(event);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita eliminada", "Cita de " + nuevaCita.getNombre() + " fue eliminada");
            addMessage(message);
            nuevaCita = new Cita();
            selectedCita = null;

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "La cita no pudo ser eliminada");
            addMessage(message);
        }
    }

    public void addEvent(ActionEvent actionEvent) {
        if (event.getId() == null) { //Si se va a agregar un evento nuevo
            try {
                nuevaCita.setFechaInicial(event.getStartDate()); //se deben tomar estos atributos del event porque si no la interfaz del calendario no muestra los datos
                nuevaCita.setFechaFinal(event.getEndDate());
                nuevaCita.setNombre(event.getTitle());

                //nuevaCita.setDepartamentoid(new Departamento(2, "Ginecología")); //********* Cambiar*************************
                nuevaCita.setDepartamentoid(departamento);
                cjc.create(nuevaCita);
                listaCitas.add(nuevaCita);
                nuevaCita = new Cita();
                eventModel.addEvent(event);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita agregada", "Cita de " + nuevaCita.getNombre() + " agregada correctamente.");
                addMessage(message);

            } catch (Exception ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "La cita de " + nuevaCita.getNombre() + " no se pudo agregar");
                addMessage(message);
                System.err.println(ex.getStackTrace());
            }

        } else { //Al actualizar una cita
            try {
                selectedCita.setFechaInicial(event.getStartDate());
                selectedCita.setFechaFinal(event.getEndDate());
                selectedCita.setNombre(event.getTitle());
                cjc.edit(selectedCita);
                selectedCita = null;
                nuevaCita = new Cita();
                eventModel.updateEvent(event);
            } catch (Exception ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "La cita de " + nuevaCita.getNombre() + " no se pudo editar");
                addMessage(message);
                System.err.println(ex.getStackTrace());
            }

        }

        event = new DefaultScheduleEvent();
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        nuevaCita = buscarCitaPorFechas();
        selectedCita = nuevaCita;

    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        selectedCita = null;
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita Movida", event.getDayDelta() + " dias" + " y " + event.getMinuteDelta() + " minutos");

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita Movida", event.getDayDelta() + " dias" + " y " + event.getMinuteDelta() + " minutos");

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Cita buscarCitaPorFechas() {
        for (Cita c : listaCitas) {
            if (c.getFechaInicial().compareTo(event.getStartDate()) == 0 && c.getFechaFinal().compareTo(event.getEndDate()) == 0) {
                return c;
            }
        }
        return null;
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
