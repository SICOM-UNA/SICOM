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
    
    private final ValorJpaController vjc;
    private final DepartamentoJpaController djc;
    private final AutorizacionJpaController ajc;
    
    public ValoresBean() {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        vjc = new ValorJpaController(emf);
        djc = new DepartamentoJpaController(emf);
        ajc = new AutorizacionJpaController(emf);
    }
    
    /**
     * Obtiene la lista de valores asociados al código dado
     * @param codigo
     * @return lista de valores
     */
    public List<String> getValuesByCodeId(Integer codigo) {
        return vjc.findByCodeId(codigo);
    }
    
    /**
     * Obtiene la lista de todos los departamentos
     * @return lista de departamentos
     */
    public List<Departamento> getDepartamentosList() {
        return djc.findDepartamentoEntities();
    }
    
    /**
     * Obtiene la lista de todos los niveles de autorización
     * @return lista de niveles de autorización
     */
    public List<Autorizacion> getAutorizacionList() {
        return ajc.findAutorizacionEntities();
    }
}