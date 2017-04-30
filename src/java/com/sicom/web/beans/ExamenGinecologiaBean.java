package com.sicom.web.beans;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class ExamenGinecologiaBean implements Serializable{
    private Date hoy;

    public ExamenGinecologiaBean(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");    
        hoy = new Date();
    }
  
    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public Date getHoy() {
        return hoy;
    }
    
}

