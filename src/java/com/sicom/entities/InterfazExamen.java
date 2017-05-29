/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Eduardo
 */
public class InterfazExamen implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String tipoExamen;
    private String hechoPor;
    private Date fechaModificacion;
    private Object examen;

    /**
     * Creates a new instance of InterfazExamenes
     *
     * @param examen
     * @param id
     */
    public InterfazExamen(ExamenColposcopia examen, int id) {
        this.examen = examen;
        this.tipoExamen = "Colposcopía";
        this.fechaModificacion = examen.getFecha();
        this.hechoPor = examen.getPersonalCedula().getNombre().concat(" ").concat(examen.getPersonalCedula().getPrimerApellido());
    }

    /**
     * Creates a new instance of InterfazExamenes
     *
     * @param examen
     * @param id
     */
    public InterfazExamen(ExamenMonitoreoFetal examen, int id) {
        this.examen = examen;
        this.tipoExamen = "Monitoreo Fetal";
        this.fechaModificacion = examen.getFecha();
        this.hechoPor = examen.getPersonalcedula().getNombre().concat(" ").concat(examen.getPersonalcedula().getPrimerApellido());
    }

    /**
     * Creates a new instance of InterfazExamenes
     *
     * @param examen
     * @param id
     */
    public InterfazExamen(ExamenGinecologia examen, int id) {
        this.examen = examen;
        this.tipoExamen = "Físico";
        this.fechaModificacion = examen.getFecha();
        this.hechoPor = examen.getPersonalCedula().getNombre().concat(" ").concat(examen.getPersonalCedula().getPrimerApellido());
        this.id = id;
    }

    /**
     * Creates a new instance of InterfazExamenes
     *
     * @param examen
     * @param id
     */
    public InterfazExamen(ExamenOdontologia examen, int id) {
        this.examen = examen;
        this.tipoExamen = "Odontológico";
        this.fechaModificacion = examen.getFecha();
        this.hechoPor = examen.getPersonalCedula().getNombre().concat(" ").concat(examen.getPersonalCedula().getPrimerApellido());
        this.id = id;
    }

    public String getTipoExamen() {
        return tipoExamen;
    }

    public void setTipoExamen(String tipoExamen) {
        this.tipoExamen = tipoExamen;
    }

    public String getHechoPor() {
        return hechoPor;
    }

    public void setHechoPor(String hechoPor) {
        this.hechoPor = hechoPor;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Object getExamen() {
        return examen;
    }

    public void setExamen(Object examen) {
        this.examen = examen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
