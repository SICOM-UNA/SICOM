package com.sicom.web.beans;
 
import com.sicom.controller.AntecedentesOdontologiaJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.AntecedentesOdontologia;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
 
@ManagedBean
@ViewScoped
public class AntecedentesOdontologiaBean{
    private AntecedentesOdontologia obj;
    private List <AntecedentesOdontologia> listaAntecedentes;
    private final AntecedentesOdontologiaJpaController aojc;
    private final ValorJpaController vjc;
   
    public AntecedentesOdontologiaBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        obj = new AntecedentesOdontologia();
        obj = new AntecedentesOdontologia();
        aojc = new AntecedentesOdontologiaJpaController(emf);
        vjc = new ValorJpaController(emf);
    }
    
    public void init(){
        listaAntecedentes = aojc.findAntecedentesOdontologiaEntities();
    }
    
    public void agregar() throws Exception{
        aojc.create(obj);
        obj = new AntecedentesOdontologia();
    }
    
    public void modificar() throws Exception{
        aojc.edit(obj);
    }
    
    public AntecedentesOdontologia consultarHistorial(Integer pacienteId){
        return aojc.findAntecedentesOdontologia(pacienteId);
    }
    
    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.vjc.findByCodeId(codigo);
    } 
   
    public List<AntecedentesOdontologia> getListaAntecedentes(){
        return listaAntecedentes;
    }
    
    public void setListaAntecedentes(List<AntecedentesOdontologia> listaAntecedentes){
        this.listaAntecedentes=listaAntecedentes;
    }
    
    public AntecedentesOdontologia getObjAntecedente(){
        return obj;
    }
    
    public void setObjAntecedente(AntecedentesOdontologia nuevoAntecedente){
        this.obj=nuevoAntecedente;
    }
     
    public void save() {        
           try{
            agregar();
            FacesMessage msg = new FacesMessage("Historial Agregado Exitosamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }catch(Exception ex){
            FacesMessage msg = new FacesMessage("Error, El Historial No Se Pudo Agregar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}