package com.sicom.web.beans;

import com.sicom.controller.ValorJpaController;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class ColposcopiaBean {
    
    private String resultado;

    public ColposcopiaBean(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        resultado = "";
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    
}