package com.sicom.web.beans;
 
import com.sicom.controller.AntecedentesOdontologiaJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.AntecedentesOdontologia;
import com.sicom.entities.Paciente;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
 
@ManagedBean
@ViewScoped
public class AntecedentesOdontologiaBean{
    private AntecedentesOdontologia antecedentesOdontologia;
    private List <AntecedentesOdontologia> listaAntecedentes;
    private final AntecedentesOdontologiaJpaController controladorOdontologia;
    private final ValorJpaController controladoraValores;
    
    private Paciente paciente;

   
    public AntecedentesOdontologiaBean() {
        paciente = PacienteBean.getSavedPaciente();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        antecedentesOdontologia = new AntecedentesOdontologia();
        controladorOdontologia = new AntecedentesOdontologiaJpaController(emf);
        controladoraValores = new ValorJpaController(emf);
    }
    
    public void init(){
        listaAntecedentes = controladorOdontologia.findAntecedentesOdontologiaEntities();
    }
    
    public void agregar() throws Exception{
        
        antecedentesOdontologia.setPacienteid(paciente.getId());
        antecedentesOdontologia.setFecha(new Date());
        controladorOdontologia.create(antecedentesOdontologia);
        
        antecedentesOdontologia = new AntecedentesOdontologia();
        
        
        
    }
    
    public void modificar() throws Exception{
        controladorOdontologia.edit(antecedentesOdontologia);
    }
    
    public AntecedentesOdontologia consultarHistorial(String pacienteId){
        return controladorOdontologia.findAntecedentesOdontologia(pacienteId);
    }
    
    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.controladoraValores.findByCodeId(codigo);
    } 
   
    public List<AntecedentesOdontologia> getListaAntecedentes(){
        return listaAntecedentes;
    }
    
    public void setListaAntecedentes(List<AntecedentesOdontologia> listaAntecedentes){
        this.listaAntecedentes=listaAntecedentes;
    }
    
    public AntecedentesOdontologia getObjAntecedente(){
        return antecedentesOdontologia;
    }
    
    public void setObjAntecedente(AntecedentesOdontologia nuevoAntecedente){
        this.antecedentesOdontologia=nuevoAntecedente;
    }
     
    public void save() {
        try {
            agregar();
            
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            String URL = ec.getRequestContextPath() + "/app/paciente/informacion";

            PacienteBean.setSavedPaciente(this.paciente);

            FacesMessage msg = new FacesMessage("Historial Agregado Exitosamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            ec.redirect(URL);
            
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage("Error, Paciente No Se Pudo Agregar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

  
     
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    
    
    
    
    
    
    
    
    
    
    
}