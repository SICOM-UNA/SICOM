package com.sicom.web.beans;

import com.sicom.controller.CitaJpaController;
import com.sicom.entities.Cita;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.Persistence;

/**
 *
 * @author SICOM
 */
@ManagedBean
@ViewScoped
public class CitasBean {
    private Cita nuevaCita;
    private Cita selectedCita;
    private ArrayList<Cita> listaCitas;
    private final CitaJpaController cjc;

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
}