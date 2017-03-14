package com.sicom.web.beans;

import com.sicom.controller.ValorJpaController;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class MonitoreoFetalBean {

    private String tipo;
    private final ValorJpaController vjc;

    public MonitoreoFetalBean(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        vjc = new ValorJpaController(emf);
        tipo = "";
    }

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.vjc.findByCodeId(codigo);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}