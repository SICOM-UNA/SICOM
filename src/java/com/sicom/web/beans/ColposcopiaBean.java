package com.sicom.web.beans;

import com.sicom.controller.ValorJpaController;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class ColposcopiaBean {

    private final ValorJpaController vjc;
    private String resultado;

    public ColposcopiaBean(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        vjc = new ValorJpaController(emf);
        resultado = "";
    }

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.vjc.findByCodeId(codigo);
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    
}