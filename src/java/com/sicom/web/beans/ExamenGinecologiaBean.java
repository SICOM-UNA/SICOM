package com.sicom.web.beans;

import com.sicom.controller.ValorJpaController;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class ExamenGinecologiaBean {
    @ManagedProperty(value = "#{ValoresBean}")
    private ValoresBean valoresBean;
    private Date hoy;

    public ExamenGinecologiaBean(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");    
        hoy = new Date();
    }

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return valoresBean.getValuesByCodeId(codigo);
    }
    
    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public Date getHoy() {
        return hoy;
    }
    
}

