package com.sicom.web.beans;

import com.sicom.controller.LoginJpaController;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.Login;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;

/**
 *
 * @author WVQ
 */
@ManagedBean
@ViewScoped
public class UsuarioBean {
    private Login nuevoUsuario;
    private Login selectedUsuario;
    private final LoginJpaController ljc;
    
    public UsuarioBean() {
        nuevoUsuario = new Login();
        selectedUsuario = new Login();
        ljc = new LoginJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
    }

    public void agregarUsuario() {
        try {
            Login usuario = ljc.findLogin(nuevoUsuario.getUsuario());
            
            if(usuario != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe un usuario con ese nombre, por favor asigne otro", selectedUsuario.getUsuario()));
            } else {
                ljc.create(nuevoUsuario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Se ha añadido un nuevo usuario", selectedUsuario.getUsuario()));
            }
        } catch (Exception ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void modificarUsuario() {
        try {
            ljc.edit(selectedUsuario);
            selectedUsuario.setAutenticado(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("login", selectedUsuario);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado exitosamente", selectedUsuario.getUsuario()));
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultarUsuario() {
        selectedUsuario = ljc.findLogin(selectedUsuario.getUsuario());
        
        if(selectedUsuario == null) {
            setSelectedUsuario(new Login());
        }
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