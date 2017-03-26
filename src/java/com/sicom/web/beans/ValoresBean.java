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
import com.sicom.entities.Valor;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * @author WVQ
 */
@ManagedBean
@ViewScoped
public class ValoresBean {
    
    private final DepartamentoJpaController djc;
    private final AutorizacionJpaController ajc;
    private final ValorJpaController vjc;
    public ValoresBean() {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        djc = new DepartamentoJpaController(emf);
        ajc = new AutorizacionJpaController(emf);
        vjc = new ValorJpaController(emf);
    }
    
    /**
     * Obtiene la lista de valores asociados al código dado
     * @param codigo
     * @return lista de valores
     */
    public List<Valor> getValuesByCodeId(Integer codigo) {
        return vjc.findByCodeId(codigo);
    }
    
    public List<String> getDescripcionByCodeId(Integer cod){
        return vjc.findDescriptionByCodeId(cod);
    }
    
    /**
     * Obtiene la lista de todos los departamentos
     * @return lista de departamentos
     */
    public List<Departamento> getDepartamentosList() {
        return djc.findDepartamentoEntities();
    }
    
    public List<Departamento> getDepartamentoCalendarioList(){
        List<Departamento> list = djc.findDepartamentoEntities();
        List<Departamento> aux = new ArrayList<>();
        aux.add(list.get(1));
        aux.add(list.get(2));
        
        return aux;
    }
    
    /**
     * Obtiene la lista de todos los niveles de autorización
     * @return lista de niveles de autorización
     */
    public List<Autorizacion> getAutorizacionList() {
        return ajc.findAutorizacionEntities();
    }    
}