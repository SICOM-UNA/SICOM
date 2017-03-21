package com.sicom.web.beans;

import com.sicom.controller.DepartamentoJpaController;
import com.sicom.controller.LoginJpaController;
import com.sicom.controller.PersonalJpaController;
import com.sicom.entities.Autorizacion;
import com.sicom.entities.Departamento;
import com.sicom.entities.Login;
import com.sicom.entities.Personal;
import java.io.IOException;
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
public class LoginBean {

    private Login login;
    private String originalURL;
    private final LoginJpaController ljc;
    private final PersonalJpaController pjc;

    public LoginBean() {
        EntityManagerFactory em = Persistence.createEntityManagerFactory("SICOM_v1PU");
        login = new Login();
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

            if (personal != null && personal.getCedula() != null) {
                String dim = ("Masculino".equals(personal.getGenero())) ? "Sr. " : "Sra. ";
                ec.getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage("Bienvenido " + dim + personal.getNombre().concat(" ") + personal.getPrimerApellido().concat(" ") + personal.getSegundoApellido().concat(".")));
            }

            ec.getSessionMap().put("login", login);

            if (from == null || from.isEmpty()) {
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
        login = new Login();
    }

    /**
     * @return the login
     */
    public Login getLogin() {
        return login;
    }

    public String valorAutorizacionPersonal() {
        Autorizacion au = login.getPersonal().getAutorizacionNivel();
        return (au == null) ? null : au.getDescripcion();
    }

    public String valorDepartamentoPersonal() {
        Departamento dpto = login.getPersonal().getDepartamentoId();
        return (dpto == null) ? null : dpto.getNombre();
    }

    /**
     * @param login the login to set
     */
    public void setLogin(Login login) {
        this.login = login;
    }
}
