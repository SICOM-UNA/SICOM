package com.sicom.web.beans;

import com.sicom.controller.LoginJpaController;
import com.sicom.entities.Login;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;

@ManagedBean
@ViewScoped
public class LoginBean {
    private Login login;
    private String originalURL;
    private final LoginJpaController ljc;
    
    public LoginBean() {
        login = new Login();
        ljc = new LoginJpaController(Persistence.createEntityManagerFactory("SICOM_v1PU"));
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/app/index";
        }else{
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public void iniciarSesion(String from) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Login nuevo = ljc.findLogin(login.getUsuario());
        
        if(nuevo != null && nuevo.getContrasena().equals(login.getContrasena())) {
            login.setAutenticado(true);
            ec.getSessionMap().put("login", getLogin());
            ec.getFlash().setKeepMessages(true);
            fc.addMessage("msg", new FacesMessage("Bienvenido "+login.getUsuario()));
            
            if(from == null || from.isEmpty()) {
                ec.redirect(originalURL);
            } else {
                ec.redirect(from);
            }
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o contraseña incorrectos", getLogin().getUsuario()));
        }
    }
    
    public void cerrarSesion() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        fc.addMessage(null, new FacesMessage("Sesión finalizada", login.getUsuario()));
        ec.getFlash().setKeepMessages(true);
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/login");
    }

    /**
     * @return the login
     */
    public Login getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(Login login) {
        this.login = login;
    }
}