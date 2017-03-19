package com.sicom.web.beans;

import com.sicom.controller.CitaJpaController;
import com.sicom.entities.Cita;
import com.sicom.entities.Departamento;
import javax.persistence.Persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class CitasBean implements Serializable {

    private Cita nuevaCita;
    private Cita selectedCita;
    private List<Cita> listaCitas;
    private final CitaJpaController cjc;
    private ScheduleModel eventModel;
    private String diaElegido;

    private ScheduleEvent event = new DefaultScheduleEvent();

    public CitasBean() {
        cjc = new CitaJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
        nuevaCita = new Cita();
        selectedCita = new Cita();
        listaCitas = new ArrayList<Cita>();
        diaElegido="";
        
    }

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        listaCitas = cjc.findCitaEntities();
        for (Cita c : listaCitas) {
            
            eventModel.addEvent(new DefaultScheduleEvent(c.getNombre(), c.getFecha_Inicial(), c.getFecha_Final()));
            
        }

    }

    public void crear() throws Exception {
        
        nuevaCita.setFecha_Inicial(event.getStartDate());
        nuevaCita.setFecha_Final(event.getEndDate());
        nuevaCita.setNombre(event.getTitle());
        nuevaCita.setDepartamentoid(new Departamento(2, "Ginecología")); //********* Cambiar*************************

        cjc.create(nuevaCita);
        listaCitas.add(nuevaCita);
        nuevaCita = new Cita();
        eventModel.addEvent(event);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita agregada", "Cita de " + nuevaCita.getNombre() + " agregada correctamente.");
        addMessage(message);
    }
    
    public void update() throws Exception{
        selectedCita.setFecha_Inicial(event.getStartDate());
        selectedCita.setFecha_Final(event.getEndDate());
        selectedCita.setNombre(event.getTitle());
        cjc.edit(selectedCita);
        selectedCita=null;
        nuevaCita=new Cita();
    }

    public void eliminar() {
        try{
        cjc.destroy(nuevaCita.getId());
        eventModel.deleteEvent(event);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita eliminada", "Cita de " + nuevaCita.getNombre() + " fue eliminada");
        addMessage(message);
        nuevaCita=new Cita();
        selectedCita=null;
        
        }
        catch(Exception ex){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "La cita no pudo ser eliminada");
            addMessage(message);
        }
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
    
    public String getDiaElegido() {
        return diaElegido;
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
        if (event.getId() == null) {
            try {
                
                crear();
            } catch (Exception ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "La cita de " + nuevaCita.getNombre() + " no se pudo agregar");
                addMessage(message);
                System.err.println(ex.getStackTrace());
            }

        } else {
            try{
                update();
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
        defineDiaText();
        nuevaCita=buscarCitaPorFechas();
        selectedCita=nuevaCita;
        
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        defineDiaText();
        selectedCita=null;
        //nuevaCita=new Cita();
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
    
    public void defineDiaText(){
        switch(event.getStartDate().getDay()){
            case 0:diaElegido="Domingo ";break;
            case 1:diaElegido="Lunes ";break;
            case 2:diaElegido="Martes ";break;
            case 3:diaElegido="Miercoles ";break;
            case 4:diaElegido="Jueves ";break;
            case 5:diaElegido="Viernes ";break;
            case 6:diaElegido="Sabado ";break;
            
        }
        diaElegido=diaElegido.concat(""+event.getStartDate().getDate());
    }
    
    public Cita buscarCitaPorFechas(){
        for (Cita c : listaCitas) {
            if(c.getFecha_Inicial().compareTo(event.getStartDate())==0 && c.getFecha_Final().compareTo(event.getEndDate())==0 )
                return c;
        }
        return null;
    }
}
