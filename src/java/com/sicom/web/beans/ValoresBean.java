/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.web.beans;

import com.sicom.entities.Autorizacion;
import com.sicom.entities.Departamento;
import com.sicom.controller.AutorizacionJpaController;
import com.sicom.controller.DepartamentoJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.Login;
import com.sicom.entities.Personal;
import com.sicom.entities.Valor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.joda.time.LocalDate;
import org.joda.time.Years;

/**
 *
 * @author WVQ
 */
@ManagedBean
@ViewScoped
public class ValoresBean implements Serializable {

    private final DepartamentoJpaController djc;
    private final AutorizacionJpaController ajc;
    private final ValorJpaController vjc;
    private String tipoId = "nacional";
    private String tipoId2 = "nacional";

    public ValoresBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        djc = new DepartamentoJpaController(emf);
        ajc = new AutorizacionJpaController(emf);
        vjc = new ValorJpaController(emf);
    }

    /**
     * Obtiene la lista de valores asociados al código dado
     *
     * @param codigo
     * @return lista de valores
     */
    public List<Valor> getValuesByCodeId(Integer codigo) {
        return vjc.findByCodeId(codigo);
    }
    
    public List<String> getDescripcionByCodeId(Integer cod) {
        return vjc.findDescriptionByCodeId(cod);
    }
    
    /**
     * Obtiene la lista de todos los departamentos
     *
     * @return lista de departamentos
     */
    public List<Departamento> getDepartamentosList() {
        return djc.findDepartamentoEntities();
    }
    
    /**
     * Obtiene la lista de todos los niveles de autorización
     *
     * @return lista de niveles de autorización
     */
    public List<Autorizacion> getAutorizacionList() {
        return ajc.findAutorizacionByPersonalCedula("0-0000-0000");
    }

    public List<Departamento> getDepartamentoCalendarioList() {
        List<Departamento> list = djc.findDepartamentoEntities();
        List<Departamento> aux = new ArrayList<>();
        aux.add(list.get(1));
        aux.add(list.get(2));

        return aux;
    }

    public List<Valor> getExamenes() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        Login log = (Login) ec.getSessionMap().get("login");
        Personal p = log.getPersonal();

        if (p != null) {
            switch (p.getDepartamentoId().getId()) {

                case 2:
                    return this.getValuesByCodeId(11);
                case 3:
                    return this.getValuesByCodeId(12);
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * @return the tipoId
     */
    public String getTipoId() {
        return tipoId;
    }

    /**
     * @param tipoId the tipoId to set
     */
    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    /**
     * @return the tipoId2
     */
    public String getTipoId2() {
        return tipoId2;
    }

    /**
     * @param tipoId2 the tipoId2 to set
     */
    public void setTipoId2(String tipoId2) {
        this.tipoId2 = tipoId2;
    }
}
