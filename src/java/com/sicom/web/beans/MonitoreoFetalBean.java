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
public class MonitoreoFetalBean {
    @ManagedProperty(value = "#{ValoresBean}")
    private ValoresBean valoresBean;
    private String tipo;

    public MonitoreoFetalBean(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        tipo = "";
    }

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return valoresBean.getValuesByCodeId(codigo);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}