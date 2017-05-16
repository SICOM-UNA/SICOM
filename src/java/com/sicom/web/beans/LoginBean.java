package com.sicom.web.beans;

import com.sicom.controller.LoginJpaController;
import com.sicom.controller.PersonalJpaController;
import com.sicom.entities.Autorizacion;
import com.sicom.entities.Departamento;
import com.sicom.entities.Login;
import com.sicom.entities.Personal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;

@ManagedBean
@ViewScoped
public class LoginBean implements Serializable{

    private Login login;
    private String originalURL;
    private final LoginJpaController ljc;
    private final PersonalJpaController pjc;

    public LoginBean() {
        login = new Login();
        EntityManagerFactory em = Persistence.createEntityManagerFactory("SICOM_v1PU");
        ljc = new LoginJpaController(em);
        pjc = new PersonalJpaController(em);
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/app/index";
        } else {
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

        if (nuevo != null && nuevo.getContrasena().equals(login.getContrasena())) {
            login = nuevo;
            login.setAutenticado(true);
            Personal personal = login.getPersonal();

            if (personal != null) {
                ec.getFlash().setKeepMessages(true);
                fc.addMessage("msg", new FacesMessage("Bienvenido " + personal.getNombre()));
                ec.getSessionMap().put("login", login);

                if (from == null || from.isEmpty()) {
                    ec.redirect(originalURL);
                } else {
                    ec.redirect(from);
                }
            } else {
                fc.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario no tiene un Personal asociado, por favor contacte con el administrador del sistema", null));
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
        login = new Login();
    }
    
    public Boolean visibleUserInRole(String nivelesAutorizacionPermitidos) {
        String[] nivelesPermitidos = nivelesAutorizacionPermitidos.split(",");
        List<Autorizacion> nivelesDelUsuario = ((Login) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("login")).getPersonal().getAutorizacionList();
        
        for(String nivel:nivelesPermitidos) {
            for(Autorizacion autorizacion:nivelesDelUsuario) {
                if(nivel.toLowerCase().trim().equals(autorizacion.getDescripcion().toLowerCase().trim())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public Boolean visibleDepartamento(String departamentosPermitidos) {
        String[] departamentos = departamentosPermitidos.split(",");
        Departamento departamentoUsuario = ((Login) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("login")).getPersonal().getDepartamentoId();
        
        for(String departamento:departamentos) {
            if(departamento.toLowerCase().trim().equals(departamentoUsuario.getNombre().toLowerCase().trim())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Si el usuario no tiene los permisos necesarios es redireccionado a otra página
     * @throws java.io.IOException
     */
    public void redireccionarUsuario() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        String referrer = ec.getRequestHeaderMap().get("referer");

        if(referrer == null) {
            ec.redirect(ec.getRequestContextPath() + "/app/index");
        } else {
            ec.redirect(referrer);
        }
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