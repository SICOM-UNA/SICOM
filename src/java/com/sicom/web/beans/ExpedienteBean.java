package com.sicom.web.beans;

import com.sicom.controller.ExpedienteJpaController;
import com.sicom.entities.Expediente;
import com.sicom.entities.Login;
import com.sicom.entities.Paciente;
import com.sicom.entities.Personal;
import com.sicom.entities.Valor;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Luis
 */
@ManagedBean
@ViewScoped
public class ExpedienteBean implements Serializable {

    private Expediente expediente;
    private ExpedienteJpaController ejc;
    private Paciente paciente;
    private Valor selectedExamen;

    public ExpedienteBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        ejc = new ExpedienteJpaController(emf);

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        paciente = (Paciente) ec.getSessionMap().get("paciente");

        if (paciente != null) {
            expediente = paciente.getExpediente();
            
            if (expediente == null) {
                expediente = ejc.findExpedienteID(paciente.getCedula());
                paciente.setExpediente(expediente);
                ec.getRequestMap().put("paciente", paciente);
            }
        } else {
            try {
                String URL = ec.getRequestContextPath() + "/app/paciente/consultar#formulario";
                ec.redirect(URL);
            } catch (IOException ex) {
                Logger.getLogger(AntecedentesGinecologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void historiaClinica() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String URL = ec.getRequestContextPath();
        Login log = (Login) ec.getSessionMap().get("login");
        Personal p = log.getPersonal();

        int consultorio = p.getDepartamentoId().getId();
        boolean permiso_editar = (p.getAutorizacionNivel().getNivel() < 5);

        String direccion = createUrl(consultorio, permiso_editar);
        subirVerificacion(consultorio, ec);

        if (direccion.trim().equals("")) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No posee los permisos para acceder.", null));
        } else {
            try {
                subirVerificacion(consultorio, ec);
                URL += direccion;
                ec.redirect(URL);
            } catch (IOException ex) {
                Logger.getLogger(PacienteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param consultorio
     * @param permiso_editar
     * @return String URL
     */
    private String createUrl(int consultorio, boolean permiso_editar) {
        switch (consultorio) {
            case 2: // Ginecologia
                if (permiso_editar) {
                    return "/app/consultorios/ginecologia/antecedentes";
                } else {
                    return "/app/consultorios/ginecologia/consultarAntecedentes";
                }
            case 3: // Odontologia
                if (permiso_editar) {
                    return "/app/consultorios/odontologia/antecedentes";
                } else {
                    return "/app/consultorios/odontologia/consultarAntecedentes";
                }
            default:
                return "";
        }
    }

    /**
     *
     * @param consultorio
     * @return Object
     */
    private void subirVerificacion(int consultorio, ExternalContext ec) {

        Expediente e = paciente.getExpediente();

        switch (consultorio) {
            case 2:
                ec.getSessionMap().put("validacionGinecologia", true);
                break;
            case 3:
                ec.getSessionMap().put("validacionOdontologia", true);
                break;
        }
    }

    public void nuevoExamen() {

        if (this.selectedExamen != null) {

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            String URL = ec.getRequestContextPath();

            switch (selectedExamen.getValorPK().getId()) {
                case 36:/*Examen Fisico*/
                    URL += "/app/consultorios/ginecologia/examen/fisico";
                    break;
                case 37:/*Monitoreo Fetal*/
                    URL += "/app/consultorios/ginecologia/examen/monitoreoFetal";
                    break;
                case 38:/*Colposcopia*/
                    URL += "/app/consultorios/ginecologia/examen/colposcopia";
                    break;
                case 39:/*Odontograma*/

                    break;
                default:
                    URL += "/app/paciente/informacion";
            }

            try {
                ec.redirect(URL);
            } catch (IOException ex) {
                Logger.getLogger(ExpedienteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar un exámen primero.", null));
        }
    }

    public void buscaExamenes() {

    }

    public void setSelectedExamen(Valor selectedExamen) {
        this.selectedExamen = selectedExamen;
    }

    public Valor getSelectedExamen() {
        return selectedExamen;
    }

}
