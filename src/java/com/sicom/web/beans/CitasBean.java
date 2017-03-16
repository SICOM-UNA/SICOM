package com.sicom.web.beans;

import com.sicom.controller.CitaJpaController;
import com.sicom.entities.Cita;
import java.util.ArrayList;
import javax.persistence.Persistence;
 
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
public class CitasBean implements Serializable{
    private Cita nuevaCita;
    private Cita selectedCita;
    private ArrayList<Cita> listaCitas;
    private final CitaJpaController cjc;
    private ScheduleModel eventModel;
     
 
    private ScheduleEvent event = new DefaultScheduleEvent();

    public CitasBean() {
        cjc = new CitaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        nuevaCita = new Cita();
        selectedCita = new Cita();
        listaCitas = new ArrayList<>();
    }
    
    public void crear() {}
    
    
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
    public ArrayList<Cita> getListaCitas() {
        return listaCitas;
    }

    /**
     * @param listaCitas the listaCitas to set
     */
    public void setListaCitas(ArrayList<Cita> listaCitas) {
        this.listaCitas = listaCitas;
    }
    
    
    
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        
         
    }
     
    public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);    //set random day of month
         
        return date.getTime();
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
     
    public void addEvent(ActionEvent actionEvent) {
        if(event.getId() == null)
            eventModel.addEvent(event);
        else
            eventModel.updateEvent(event);
         
        event = new DefaultScheduleEvent();
    }
     
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita Movida", event.getDayDelta() + " dias despues" + " y " + event.getMinuteDelta() + " minutos despues");
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita Movida", event.getDayDelta() + " dias despues" + " y " + event.getMinuteDelta() + " minutos despues");
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}