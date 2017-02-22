package com.sicom.web.beans;
 
import com.sicom.controller.AntecedentesGinecologiaJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Paciente;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.event.FlowEvent;
 
@ManagedBean
@ViewScoped
public class GinecologiaBean{
    private AntecedentesGinecologia nuevoAntecedentes;
    private AntecedentesGinecologia selectedAntecedentes;
    private List <AntecedentesGinecologia> listaAntecedentes;
    private final AntecedentesGinecologiaJpaController agjc;
    private final ValorJpaController vjc;
    private boolean skip;
    
    public GinecologiaBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        nuevoAntecedentes = new AntecedentesGinecologia();
        selectedAntecedentes = new AntecedentesGinecologia();
        agjc = new AntecedentesGinecologiaJpaController(emf);
        vjc = new ValorJpaController(emf);
    }
    
    public void init(){
        listaAntecedentes = agjc.findAntecedentesGinecologiaEntities();
    }
    
    public void agregar() throws Exception{
        agjc.create(nuevoAntecedentes);
        nuevoAntecedentes = new AntecedentesGinecologia();
    }
    
    public void modificar() throws Exception{
        agjc.edit(selectedAntecedentes);
    }
    
    public AntecedentesGinecologia consultarAntecedentes(Integer pacienteId){
        return agjc.findAntecedentesGinecologia(pacienteId);
    }
    
    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.vjc.findByCodeId(codigo);
    } 
     
    public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
    
    public void save(){
        try{
            agregar();
            FacesMessage msg = new FacesMessage("Antecedentes Agregado Exitosamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }catch(Exception ex){
            FacesMessage msg = new FacesMessage("Error, Paciente No Se Pudo Agregar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public List<AntecedentesGinecologia> getListaAntecedentes(){
        return listaAntecedentes;
    }
    
    public void setListaAntecedentes(List<AntecedentesGinecologia> listaAntecedentes){
        this.listaAntecedentes=listaAntecedentes;
    }
    
    public AntecedentesGinecologia getNuevoAntecedentes(){
        return nuevoAntecedentes;
    }
    
    public void setNuevoAntecedentes(AntecedentesGinecologia nuevoAntecedentes){
        this.nuevoAntecedentes=nuevoAntecedentes;
    }
    
    public AntecedentesGinecologia getSelectedAntecedentes(){
        return selectedAntecedentes;
    }
    
    public void setSelectedAntecedentes(AntecedentesGinecologia selectedAntecedentes){
        this.selectedAntecedentes=selectedAntecedentes;
    }
}