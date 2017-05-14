package com.sicom.web.beans;

import com.sicom.controller.LoginJpaController;
import com.sicom.entities.Personal;
import com.sicom.controller.PersonalJpaController;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.Login;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ManagedBean
@ViewScoped
public class PersonalBean implements Serializable {

    private Personal nuevoPersonal;
    private Personal selectedPersonal;
    private List<Personal> listaPersonal;
    private Login nuevoUsuario;
    private Login selectedUsuario;
    private final LoginJpaController ljc;
    private final PersonalJpaController pjc;
   
    public PersonalBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        nuevoPersonal = new Personal();
        nuevoUsuario = new Login();
        ljc = new LoginJpaController(emf);
        pjc = new PersonalJpaController(emf);
    }

    @PostConstruct
    public void init() {
        listaPersonal = pjc.findPersonalEntities();
    }

    /**
     * Agrega un usuario y el personal asociado a este
     * @return la página a la cual será redireccionado el usuario
     */
    public String agregar() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            Login login = ljc.findLogin(nuevoUsuario.getUsuario());
            Personal personal = pjc.findPersonal(nuevoUsuario.getPersonal().getCedula());
            
            // Valida si el usuario ya existe
            if(login != null && login.getUsuario().toLowerCase().equals(nuevoUsuario.getUsuario().toLowerCase())) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El usuario ya existe, por favor digite un nombre de usuario diferente de " + login.getUsuario(), null));
            // Valida si el personal ya existe
            } else if(personal != null && personal.getCedula().equals(nuevoUsuario.getPersonal().getCedula())) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El personal ya existe, por favor digite un número de cédula diferente de " + personal.getCedula(), null));
            } else {
                // Se agrega el usuario
                ljc.create(nuevoUsuario);
                fc.getExternalContext().getSessionMap().put("personal", nuevoUsuario.getPersonal());
                fc.addMessage(null, new FacesMessage("Usuario creado exitosamente", nuevoUsuario.getUsuario()));
                fc.getExternalContext().getFlash().setKeepMessages(true);
                nuevoUsuario = new Login();
                
                return "informacion?faces-redirect=true";
            }   
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "agregar";
    }
    
    /**
     * Modificar los datos del personal
     * @return la página a la cual será redireccionado el usuario
     */
    public String modificar() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            pjc.edit(selectedPersonal);
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información actualizada exitosamente.", null));
            fc.getExternalContext().getFlash().setKeepMessages(true);
            selectedPersonal = new Personal();
            
            return "informacion?faces-redirect=true";
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    /**
     * Muestra los datos del personal solicitado por el usuario
     * @return 
     */
    public String consultar() {
        selectedPersonal = pjc.findPersonal(nuevoPersonal.getCedula());

        if (selectedPersonal != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("personal", selectedPersonal);
            
            return "informacion?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El personal con la identificación " + nuevoPersonal.getCedula() + " no ha sido encontrado", null));
            
            return "consultar";
        }
    }

    /**
     * Modificar los datos del usuario
     */
    public void modificarUsuario() {
        try {
            ljc.edit(selectedUsuario);
            selectedUsuario.setAutenticado(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("login", selectedUsuario);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado exitosamente", selectedUsuario.getUsuario()));
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the listaPersonal
     */
    public List<Personal> getListaPersonal() {
        return listaPersonal;
    }

    /**
     * @param listaPersonal the listaPersonal to set
     */
    public void setListaPersonal(List<Personal> listaPersonal) {
        this.listaPersonal = listaPersonal;
    }

    /**
     * @return the nuevoPersonal
     */
    public Personal getNuevoPersonal() {
        return nuevoPersonal;
    }

    /**
     * @param nuevoPersonal the nuevoPersonal to set
     */
    public void setNuevoPersonal(Personal nuevoPersonal) {
        this.nuevoPersonal = nuevoPersonal;
    }

    /**
     * @return the selectedPersonal
     */
    public Personal getSelectedPersonal() {
        return selectedPersonal;
    }

    /**
     * @param selectedPersonal the selectedPersonal to set
     */
    public void setSelectedPersonal(Personal selectedPersonal) {
        this.selectedPersonal = selectedPersonal;
    }

    /**
     * @return the nuevoUsuario
     */
    public Login getNuevoUsuario() {
        return nuevoUsuario;
    }

    /**
     * @param nuevoUsuario the nuevoUsuario to set
     */
    public void setNuevoUsuario(Login nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }

    /**
     * @return the selectedUsuario
     */
    public Login getSelectedUsuario() {
        return selectedUsuario;
    }

    /**
     * @param selectedUsuario the selectedUsuario to set
     */
    public void setSelectedUsuario(Login selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }
}