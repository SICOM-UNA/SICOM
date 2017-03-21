package com.sicom.web.beans;

import com.sicom.controller.LoginJpaController;
import com.sicom.controller.ValorJpaController;
import com.sicom.entities.Personal;
import com.sicom.controller.PersonalJpaController;
import com.sicom.entities.Login;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;

@ManagedBean
@ViewScoped
public class PersonalBean {

    private static Personal savedPersonal;
    private static Login savedUsuario;
    private Personal nuevoPersonal;
    private Personal selectedPersonal;
    private List<Personal> listaPersonal;
    private Login nuevoUsuario;
    private Login selectedUsuario;
    private final LoginJpaController ljc;
    private final PersonalJpaController pjc;
    private final ValorJpaController vjc;

    public PersonalBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        nuevoPersonal = new Personal();
        nuevoUsuario = new Login();
        ljc = new LoginJpaController(emf);
        pjc = new PersonalJpaController(emf);
        vjc = new ValorJpaController(emf);
        selectedPersonal = (savedPersonal != null)? savedPersonal : new Personal();
        selectedUsuario = (savedUsuario != null)? savedUsuario : new Login();
    }

    @PostConstruct
    public void init() {
        listaPersonal = pjc.findPersonalEntities();
    }

    /**
     * Agregar usuario
     */
    public void agregar() {
        try {
            Login login = ljc.findLogin(nuevoUsuario.getUsuario());
            Personal personal = pjc.findPersonal(nuevoUsuario.getPersonal().getCedula());
            
            // Valida si el usuario ya existe
            if(login != null && login.getUsuario().equals(nuevoUsuario.getUsuario())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El usuario ya existe, por favor digite un nombre de usuario diferente a", login.getUsuario()));
            // Valida si el personal ya existe
            } else if(personal != null && personal.getCedula().equals(nuevoUsuario.getPersonal().getCedula())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El personal ya existe, por favor digite un número de cédula diferente de", personal.getCedula()));
            } else {
                // Se agrega el usuario
                ljc.create(nuevoUsuario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario creado exitosamente", nuevoUsuario.getUsuario()));
            }   
        } catch (Exception ex) {
            Logger.getLogger(PersonalBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void consultar() {
        selectedUsuario = ljc.findLogin(selectedUsuario.getUsuario());

        if (selectedUsuario == null) {
            selectedUsuario = new Login();
        }
    }

    public void modificar() throws Exception {
        pjc.edit(selectedPersonal);
    }

    /*
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
     */
    public Personal consultarPersonalPorId(String id) {
        return pjc.findPersonal(id);
    }

    public List<String> consultarValoresPorCodigo(Integer codigo) {
        return this.vjc.findByCodeId(codigo);
    }

    public void buscaIdBase(){

        if (selectedPersonal.getCedula() != null) {

            Personal p = this.pjc.findPersonal(selectedPersonal.getCedula());
            
            if (p != null) {

                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();

                String URL = ec.getRequestContextPath() + "/app/personal/informacion";

                savedPersonal = p;
                savedUsuario = ljc.findLogin(p.getLoginUsuario().getUsuario());

                try {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Personal encontrado exitosamente."));
                    ec.redirect(URL);
                } catch (IOException ex) {

                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No existe pesonl asignado a la identificación: " + selectedPersonal.getCedula()));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("La cédula no puede ser nula."));
        }
    }

//-------------------------------------------------------------------------------------------
// Otros metodos
    public Date disablePastDates() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -43800); //43800 = 120 años
        return c.getTime();
    }

    public int calculaEdad() {
        if (selectedPersonal.getNacimiento() != null) {
            LocalDate birthdate = new LocalDate(selectedPersonal.getNacimiento());
            LocalDate now = new LocalDate();
            return Years.yearsBetween(birthdate, now).getYears();
        }
        return 0;
    }

    public int PersonalNuevoEdad() {
        if (this.nuevoPersonal != null && this.nuevoPersonal.getNacimiento() != null) {
            DateTime birthdate = new DateTime(nuevoPersonal.getNacimiento());
            DateTime now = new DateTime();
            return Years.yearsBetween(birthdate, now).getYears();
        } else {
            return 0;
        }
    }

    //----------------------------------------------------------------------------------------------
    // GETTERS Y SETTERS
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

    public static Personal getSavedPersonal() {
        return savedPersonal;
    }

    public static void setSavedPersonal(Personal savedPersonal) {
        PersonalBean.savedPersonal = savedPersonal;
    }

    public static Login getSavedUsuario() {
        return savedUsuario;
    }

    public static void setSavedUsuario(Login savedUsuario) {
        PersonalBean.savedUsuario = savedUsuario;
    }
}