package com.sicom.web.beans;

import com.sicom.controller.DocumentosJpaController;
import com.sicom.controller.ExpedienteJpaController;
import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.AntecedentesOdontologia;
import com.sicom.entities.Documentos;
import com.sicom.entities.ExamenColposcopia;
import com.sicom.entities.ExamenGinecologia;
import com.sicom.entities.ExamenOdontologia;
import com.sicom.entities.Expediente;
import com.sicom.entities.InterfazExamen;
import com.sicom.entities.Login;
import com.sicom.entities.ExamenMonitoreoFetal;
import com.sicom.entities.Paciente;
import com.sicom.entities.Personal;
import com.sicom.entities.Valor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis
 */
@ManagedBean
@ViewScoped
public class ExpedienteBean implements Serializable {

    private Expediente expediente;
    private final ExpedienteJpaController ejc;
    private Paciente paciente;
    private Valor selectedExamen;
    private UploadedFile archivo = null;
    private Documentos documento;
    private List<Documentos> ultimosDocumentoss = new ArrayList<>();
    private List<Documentos> listaDocumentos = new ArrayList<>();
    private final DocumentosJpaController documentosController;

    List<InterfazExamen> resultado, listaFiltrada;
    InterfazExamen dato;

    public ExpedienteBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        ejc = new ExpedienteJpaController(emf);
        documentosController = new DocumentosJpaController(emf);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();        
        paciente = (Paciente) ec.getSessionMap().get("paciente");

        if (paciente != null) {
            expediente = paciente.getExpediente();

            if (expediente == null) {
                expediente = ejc.findExpedienteID(paciente.getCedula());
                paciente.setExpediente(expediente);
                ec.getRequestMap().put("paciente", paciente);
            }
        }
    }

    public void historiaClinica() {

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Personal p = ((Login) ec.getSessionMap().get("login")).getPersonal();
        int consultorio = p.getDepartamentoId().getId();
        
        try {
            String URL = ec.getRequestContextPath().concat(createUrl(consultorio));
            ec.redirect(URL);
        } catch (IOException ex) {
            Logger.getLogger(PacienteBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param consultorio
     * @return String URL
     */
    private String createUrl(int consultorio) {
        switch (consultorio) {
            case 2: // Ginecologia
                return "/app/consultorios/ginecologia/antecedentes";

            case 3: // Odontologia
                return "/app/consultorios/odontologia/antecedentes";

            default:
                return null;
        }
    }

    public void nuevoExamen() {
        if (this.selectedExamen != null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath();
            ec.getSessionMap().remove("examen");

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
                    URL += "/app/consultorios/odontologia/examen/odontologico";
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
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar un examen primero", null));
        }
    }

    private void nuevaLinea(PDPageContentStream content, int x, int y, int tamañoFuente, String texto) throws IOException {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA, tamañoFuente);
        content.moveTextPositionByAmount(x, y);
        content.drawString(texto);
        content.endText();
    }

    private int textoLargo(String texto, PDPageContentStream contentStream, PDPage page, int startX, int startY) throws IOException {
        PDRectangle mediabox = page.findMediaBox();
        float margin = 52;
        float width = mediabox.getWidth() - 2 * margin;
        PDFont pdfFont = PDType1Font.HELVETICA;
        float fontSize = 10;
        List<String> lines = new ArrayList<String>();
        int lastSpace = -1;

        while (texto.length() > 0) {
            int spaceIndex = 0;

            if (texto.indexOf(' ', lastSpace + 1) > texto.indexOf("\n", lastSpace + 1)) {
                spaceIndex = texto.indexOf(' ', lastSpace + 1);
            } else {
                spaceIndex = texto.indexOf("\n", lastSpace + 1);
            }

            if (spaceIndex < 0) {
                spaceIndex = texto.length();
            }

            String subString = texto.substring(0, spaceIndex);
            float size = fontSize * pdfFont.getStringWidth(subString) / 500;

            if (size > width) {
                if (lastSpace < 0) {
                    lastSpace = spaceIndex;
                }

                subString = texto.substring(0, lastSpace);
                lines.add(subString);
                texto = texto.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == texto.length()) {
                lines.add(texto);
                texto = "";
            } else {
                lastSpace = spaceIndex;
            }
        }

        for (String line : lines) {
            contentStream.beginText();
            contentStream.setFont(pdfFont, fontSize);
            contentStream.moveTextPositionByAmount(startX, startY);
            contentStream.drawString(line);
            contentStream.endText();
            startY -= 10;
        }

        return startY;
    }

    public void exportar() throws COSVisitorException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Login log = (Login) ec.getSessionMap().get("login");
        Personal p = log.getPersonal();
        int consultorio = p.getDepartamentoId().getId();

        if (consultorio == 2 && expediente.getAntecedentesGinecologia() != null) {
            AntecedentesGinecologia antecedentesGinecologia = expediente.getAntecedentesGinecologia();
            exportarGinecologia(antecedentesGinecologia);
        } else if (consultorio == 3 && expediente.getAntecedentesOdontologia() != null) {
            AntecedentesOdontologia antecedentesOdontologia = expediente.getAntecedentesOdontologia();
            exportarOdontologia(antecedentesOdontologia);
        } else if (consultorio == 1 || consultorio == 4) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No posee los permisos para acceder a esta opcion.", null));
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El paciente no posee historia clínica.", null));
        }
    }

    public void exportarGinecologia(AntecedentesGinecologia antecedentesGinecologia) {
        try {
            Date fecha = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Creando el pdf");
            String fileName = "Historia_Clinica_" + paciente.getNombre() + "_" + paciente.getPrimerApellido() + ".pdf";
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            int y = 720;
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);
            //<editor-fold defaultstate="collapsed" desc="Header del pdf">
            //PDXObjectImage image = new PDJpeg(doc, new FileInputStream("/imagenes/icono.png"));
            //content.drawImage(image, 450, 650);
            nuevaLinea(content, 550, 40, 12, "1");
            nuevaLinea(content, 80, y, 10, "Fecha:" + dateFormat.format(fecha));
            nuevaLinea(content, 280, y, 10, "Centro Médico Navas");
            y -= 10;
            nuevaLinea(content, 305, y, 10, "La Sabana");
            y -= 10;
            nuevaLinea(content, 282, y, 10, "Teléfono: 2233-1010");
            y -= 10;
            nuevaLinea(content, 250, y, 10, "Dra. María del Pilar Navas Aparicio");
            y -= 60;
            nuevaLinea(content, 80, y, 12, "Historia Clínica: " + paciente.getNombre() + " " + paciente.getPrimerApellido() + " " + paciente.getSegundoApellido());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Última modificación: " + dateFormat.format(antecedentesGinecologia.getFecha()));
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Datos personales">
            y -= 10;
            content.drawLine(80, y, 440, y);
            y -= 15;
            nuevaLinea(content, 80, y, 12, "Datos Personales");
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Nombre y apellidos: " + paciente.getNombre() + " " + paciente.getPrimerApellido() + " " + paciente.getSegundoApellido());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Identificación: " + paciente.getCedula());
            y -= 15;

            DateTime birthdate = new DateTime(paciente.getNacimiento());
            DateTime now = new DateTime();
            nuevaLinea(content, 80, y, 10, "Edad: " + (Years.yearsBetween(birthdate, now).getYears()));
            nuevaLinea(content, 150, y, 10, "Sexo: " + paciente.getGenero());
            nuevaLinea(content, 240, y, 10, "Ocupación: " + paciente.getOcupacion());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Fecha de nacimiento: " + dateFormat.format(paciente.getNacimiento()));
            nuevaLinea(content, 240, y, 10, "Domicilio: " + paciente.getDomicilio());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Estado civil: " + paciente.getEstadoCivil());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Teléfono(s): " + paciente.getTelefono() + "/" + paciente.getCelular());
            nuevaLinea(content, 250, y, 10, "Correo electrónico: " + paciente.getCorreo());
            y -= 20;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Antecedentes">
            content.drawLine(80, y, 440, y);
            y -= 25;

            if (antecedentesGinecologia.getHerediatarios().equals("")) {
                nuevaLinea(content, 80, y, 11, "Antecedentes hereditarios: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Antecedentes hereditarios: ");
                y = textoLargo(antecedentesGinecologia.getHerediatarios(), content, page, 220, y);
            }
            y -= 20;

            if (antecedentesGinecologia.getPatologicos().equals("")) {
                nuevaLinea(content, 80, y, 11, "Antecedentes Patológicos: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Antecedentes Patológicos: ");
                y = textoLargo(antecedentesGinecologia.getPatologicos(), content, page, 220, y);
            }
            y -= 20;
            if (antecedentesGinecologia.getNoPatologicos().equals("")) {
                nuevaLinea(content, 80, y, 11, "Antecedentes No Patológicos: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Antecedentes No Patológicos: ");
                y = textoLargo(antecedentesGinecologia.getNoPatologicos(), content, page, 230, y);
            }
            y -= 20;
            if (antecedentesGinecologia.getQuirurgicos().equals("")) {
                nuevaLinea(content, 80, y, 11, "Antecedentes Quirúrgicos: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Antecedentes Quirúrgicos: ");
                y = textoLargo(antecedentesGinecologia.getQuirurgicos(), content, page, 220, y);
            }
            y -= 20;//200

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Resto de la historia clinica">
            if (y < 160) { //verificar si es necesario crear una nueva pagina despues de los antecedentes
                content.close();
                PDPage page2 = new PDPage();
                doc.addPage(page2);
                content = new PDPageContentStream(doc, page2);
                y = 720;
                nuevaLinea(content, 550, 40, 12, "2");
            }
            content.drawLine(80, y, 440, y);
            y -= 30;
            if (antecedentesGinecologia.getMenarca() == null) {
                nuevaLinea(content, 80, y, 10, "Menarca: N/A");
            } else {
                nuevaLinea(content, 80, y, 10, "Menarca: " + antecedentesGinecologia.getMenarca());
            }

            if (antecedentesGinecologia.getFur() != null) {
                nuevaLinea(content, 160, y, 10, "FUR: " + antecedentesGinecologia.getFur().getYear() + "/" + antecedentesGinecologia.getFur().getMonth());
            } else {
                nuevaLinea(content, 160, y, 10, "FUR: N/A");
            }
            y -= 20;
            if (y < 160) {
                content.close();
                PDPage page2 = new PDPage();
                doc.addPage(page2);
                content = new PDPageContentStream(doc, page2);
                y = 680;
                nuevaLinea(content, 550, 40, 12, "2");
            }

            if (antecedentesGinecologia.getCicloMestrual().equals("") && antecedentesGinecologia.getComentarioCicloMestrual().equals("")) {
                nuevaLinea(content, 80, y, 10, "Ciclo menstrual: N/A");
            } else {
                nuevaLinea(content, 80, y, 10, "Ciclo menstrual: " + antecedentesGinecologia.getCicloMestrual() + " " + antecedentesGinecologia.getComentarioCicloMestrual());
            }
            y -= 20;

            if (y < 160) {
                content.close();
                PDPage page2 = new PDPage();
                doc.addPage(page2);
                content = new PDPageContentStream(doc, page2);
                y = 680;
                nuevaLinea(content, 550, 40, 12, "2");
            }

            if (antecedentesGinecologia.getPrs() == null) {
                nuevaLinea(content, 80, y, 10, "PRS: N/A");
            } else {
                nuevaLinea(content, 80, y, 10, "PRS: " + antecedentesGinecologia.getPrs());
            }

            if (antecedentesGinecologia.getCompanerosSexuales() == null) {
                nuevaLinea(content, 160, y, 10, "Compañeros sexuales: N/A");
            } else {
                nuevaLinea(content, 160, y, 10, "Compañeros sexuales: " + antecedentesGinecologia.getCompanerosSexuales());
            }
            y -= 20;

            if (y < 160) {
                content.close();
                PDPage page2 = new PDPage();
                doc.addPage(page2);
                content = new PDPageContentStream(doc, page2);
                y = 680;
                nuevaLinea(content, 550, 40, 12, "2");
            }

            if (antecedentesGinecologia.getPlanificacion().equals("")) {
                nuevaLinea(content, 80, y, 10, "Planificación: N/A");
            } else {
                nuevaLinea(content, 80, y, 10, "Planificación: " + antecedentesGinecologia.getPlanificacion());
            }
            y -= 20;

            if (antecedentesGinecologia.getActividadSexual().equals("")) {
                nuevaLinea(content, 80, y, 10, "Actividad sexual: N/A");
            } else {
                nuevaLinea(content, 80, y, 10, "Actividad sexual: " + antecedentesGinecologia.getActividadSexual());
            }
            y -= 20;

            if (y < 160) {
                content.close();
                PDPage page2 = new PDPage();
                doc.addPage(page2);
                content = new PDPageContentStream(doc, page2);
                y = 680;
                nuevaLinea(content, 550, 40, 12, "2");
            }

            if (antecedentesGinecologia.getUltimoParto() != null) {
                nuevaLinea(content, 80, y, 10, "Último parto: " + antecedentesGinecologia.getUltimoParto());
            } else {
                nuevaLinea(content, 80, y, 10, "Último parto: N/A");
            }

            if (antecedentesGinecologia.getUltimoPap().equals("")) {
                nuevaLinea(content, 210, y, 10, "Último PAP: N/A");
            } else {
                nuevaLinea(content, 210, y, 10, "Último PAP: " + antecedentesGinecologia.getUltimoPap());
            }

            y -= 20;

            if (antecedentesGinecologia.getComentarioLactancia().equals("")) {
                nuevaLinea(content, 80, y, 10, "Lactancia materna: " + (antecedentesGinecologia.getLactanciaMaterna() ? "Si" : "No"));
            } else {
                nuevaLinea(content, 80, y, 10, "Lactancia materna: " + (antecedentesGinecologia.getLactanciaMaterna() ? "Si" : "No") + ", " + antecedentesGinecologia.getComentarioLactancia());
            }

            y -= 20;

            if (antecedentesGinecologia.getMenopausia() == null) {
                nuevaLinea(content, 80, y, 10, "Menopausia: N/A");
            } else {
                nuevaLinea(content, 80, y, 10, "Menopausia: " + antecedentesGinecologia.getMenopausia());
            }
            if (antecedentesGinecologia.getGesta() == null) {
                nuevaLinea(content, 170, y, 10, "Gesta: N/A");
            } else {
                nuevaLinea(content, 170, y, 10, "Gesta: " + antecedentesGinecologia.getGesta());
            }
            if (antecedentesGinecologia.getPartos() == null) {
                nuevaLinea(content, 235, y, 10, "Partos: N/A");
            } else {
                nuevaLinea(content, 235, y, 10, "Partos: " + antecedentesGinecologia.getPartos());
            }
            if (antecedentesGinecologia.getAbortos() == null) {
                nuevaLinea(content, 310, y, 10, "Abortos: N/A");
            } else {
                nuevaLinea(content, 310, y, 10, "Abortos: " + antecedentesGinecologia.getAbortos());
            }
            if (antecedentesGinecologia.getEctopico() == null) {
                nuevaLinea(content, 390, y, 10, "Ectópico: N/A");
            } else {
                nuevaLinea(content, 390, y, 10, "Ectópico: " + antecedentesGinecologia.getEctopico());
            }
            y -= 20;

            if (antecedentesGinecologia.getComentarioGPA().equals("")) {
                nuevaLinea(content, 80, y, 10, "GPA: N/A");
            } else {
                nuevaLinea(content, 80, y, 10, "GPA: ");
                y = textoLargo(antecedentesGinecologia.getComentarioGPA(), content, page, 130, y);
            }
            y -= 20;

            if (antecedentesGinecologia.getInformacionAdicional().equals("")) {
                nuevaLinea(content, 80, y, 10, "Información adicional: N/A");
            } else {
                nuevaLinea(content, 80, y, 10, "Información adicional:");
                y = textoLargo(antecedentesGinecologia.getInformacionAdicional(), content, page, 190, y);
            }
            content.close();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="guardando el archivo">
            try {
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ByteArrayOutputStream byteoutput = new ByteArrayOutputStream();
                doc.save(byteoutput);
                doc.close();
                ec.responseReset();
                ec.setResponseContentType("application/pdf");
                ec.setResponseContentLength(byteoutput.size());
                ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                OutputStream output = ec.getResponseOutputStream();
                output.write(byteoutput.toByteArray());
                fc.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(ExpedienteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            //</editor-fold>
            //</editor-fold>
            System.out.println("El archivo fue guardado");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void exportarOdontologia(AntecedentesOdontologia antecedentesOdontologia) {
        try {
            Date fecha = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Creando el pdf");
            String fileName = "Historia_Clinica_" + paciente.getNombre() + "_" + paciente.getPrimerApellido() + ".pdf";
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            int y = 720;
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);

            //<editor-fold defaultstate="collapsed" desc="Header del pdf">
            //PDXObjectImage image = new PDJpeg(doc, new FileInputStream("/imagenes/icono.png"));
            //content.drawImage(image, 450, 650);
            nuevaLinea(content, 550, 40, 12, "1");
            nuevaLinea(content, 80, y, 10, "Fecha:" + dateFormat.format(fecha));

            nuevaLinea(content, 280, y, 10, "Centro Médico Navas");
            y -= 10;
            nuevaLinea(content, 305, y, 10, "La Sabana");
            y -= 10;
            nuevaLinea(content, 282, y, 10, "Teléfono: 2233-1010");
            y -= 10;
            nuevaLinea(content, 250, y, 10, "Dra. María del Carmen Navas Aparicio");

            y -= 60;
            nuevaLinea(content, 80, y, 12, "Historia Clínica: " + paciente.getNombre() + " " + paciente.getPrimerApellido() + " " + paciente.getSegundoApellido());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Última modificación: " + dateFormat.format(antecedentesOdontologia.getFecha()));
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Datos personales">
            y -= 10;
            content.drawLine(80, y, 440, y);
            y -= 15;
            nuevaLinea(content, 80, y, 12, "Datos personales");
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Nombre y apellidos: " + paciente.getNombre() + " " + paciente.getPrimerApellido() + " " + paciente.getSegundoApellido());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Identificación: " + paciente.getCedula());
            y -= 15;

            DateTime birthdate = new DateTime(paciente.getNacimiento());
            DateTime now = new DateTime();
            nuevaLinea(content, 80, y, 10, "Edad: " + (Years.yearsBetween(birthdate, now).getYears()));
            nuevaLinea(content, 150, y, 10, "Sexo: " + paciente.getGenero());
            nuevaLinea(content, 240, y, 10, "Ocupación: " + paciente.getOcupacion());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Fecha de nacimiento: " + dateFormat.format(paciente.getNacimiento()));
            nuevaLinea(content, 240, y, 10, "Domicilio: " + paciente.getDomicilio());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Estado civil: " + paciente.getEstadoCivil());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Teléfono(s): " + paciente.getTelefono() + "/" + paciente.getCelular());
            nuevaLinea(content, 250, y, 10, "Correo electrónico: " + paciente.getCorreo());
            y -= 20;

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Antecedentes">
            content.drawLine(80, y, 440, y);
            y -= 25;

            if (antecedentesOdontologia.getHereditarios().equals("")) {
                nuevaLinea(content, 80, y, 11, "Antecedentes hereditarios: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Antecedentes hereditarios: ");
                y = textoLargo(antecedentesOdontologia.getHereditarios(), content, page, 220, y);
            }
            y -= 20;

            if (antecedentesOdontologia.getPatologicos().equals("")) {
                nuevaLinea(content, 80, y, 11, "Antecedentes patológicos: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Antecedentes patológicos: ");
                y = textoLargo(antecedentesOdontologia.getPatologicos(), content, page, 220, y);
            }
            y -= 20;
            if (antecedentesOdontologia.getNoPatologicos().equals("")) {
                nuevaLinea(content, 80, y, 11, "Antecedentes no patológicos: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Antecedentes no patológicos: ");
                y = textoLargo(antecedentesOdontologia.getNoPatologicos(), content, page, 230, y);
            }
            y -= 20;
            if (antecedentesOdontologia.getQuirurgicos().equals("")) {
                nuevaLinea(content, 80, y, 11, "Antecedentes quirúrgicos: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Antecedentes quirúrgicos: ");
                y = textoLargo(antecedentesOdontologia.getQuirurgicos(), content, page, 220, y);
            }
            y -= 20;//200

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Resto de la historia clinica">
            if (y < 160) { //verificar si es necesario crear una nueva pagina despues de los antecedentes
                content.close();
                PDPage page2 = new PDPage();
                doc.addPage(page2);
                content = new PDPageContentStream(doc, page2);
                y = 720;
                nuevaLinea(content, 550, 40, 12, "2");
            }

            y -= 20;
            if (antecedentesOdontologia.getAlergias().equals("")) {
                nuevaLinea(content, 80, y, 11, "Alergias: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Alergias: ");
                y = textoLargo(antecedentesOdontologia.getAlergias(), content, page, 220, y);
            }
            y -= 20;

            if (y < 160) {
                content.close();
                PDPage page2 = new PDPage();
                doc.addPage(page2);
                content = new PDPageContentStream(doc, page2);
                y = 680;
                nuevaLinea(content, 550, 40, 12, "2");
            }

            if (antecedentesOdontologia.getHabitos().equals("")) {
                nuevaLinea(content, 80, y, 11, "Hábitos: N/A");
            } else {
                nuevaLinea(content, 80, y, 11, "Hábitos: ");
                y = textoLargo(antecedentesOdontologia.getHabitos(), content, page, 220, y);
            }
            y -= 20;
            content.drawLine(80, y, 440, y);
            content.close();

            //</editor-fold>
            ////////////////////////////////////////////FALTA INSERTAR IMAGEN DE ODONTOGRAMA////////////////////////////////////////////
            //<editor-fold defaultstate="collapsed" desc="guardando el archivo">
            try {
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                ByteArrayOutputStream byteoutput = new ByteArrayOutputStream();
                doc.save(byteoutput);
                doc.close();
                ec.responseReset();
                ec.setResponseContentType("application/pdf");
                ec.setResponseContentLength(byteoutput.size());
                ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                OutputStream output = ec.getResponseOutputStream();
                output.write(byteoutput.toByteArray());
                fc.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(ExpedienteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            //</editor-fold>
            //</editor-fold>
            System.out.println("El archivo fue guardado exitosamente");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void descargarDocumento(Documentos doc) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ByteArrayOutputStream byteoutput = new ByteArrayOutputStream(doc.getArchivo().length);
            byteoutput.write(doc.getArchivo(), 0, doc.getArchivo().length);
            ec.responseReset();
            ec.setResponseContentType("application/pdf");
            ec.setResponseContentLength(byteoutput.size());
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + doc.getNombre() + "\"");
            OutputStream output = ec.getResponseOutputStream();
            output.write(byteoutput.toByteArray());
            fc.responseComplete();
        } catch (Exception ex) {
            Logger.getLogger(ExpedienteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarDocumento(Documentos doc) {
        FacesContext fc = FacesContext.getCurrentInstance();

        try {
            documentosController.destroy(doc.getId());
            fc.addMessage(null, new FacesMessage("Archivo " + doc.getNombre() + " eliminado exitosamente"));
        } catch (NonexistentEntityException ex) {

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar el archivo", null));
            Logger.getLogger(ExpedienteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importar(FileUploadEvent event) {
        try {
            documento = new Documentos();
            UploadedFile uploadFile = event.getFile();
            byte[] bytes = uploadFile.getContents();
            documento.setArchivo(bytes);
            documento.setFecha(new Date());
            String filename = uploadFile.getFileName();
            documento.setNombre(filename);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            Login log = (Login) ec.getSessionMap().get("login");
            Personal persona = log.getPersonal();
            documento.setDepartamentoid(persona.getDepartamentoId());
            documento.setExpedientePacientecedula(expediente);
            documentosController.create(documento);
            FacesMessage message = new FacesMessage(filename + " se ha subido.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir el archivo", null));
        }
    }
    
    public List<InterfazExamen> getListaExamenes() {
        resultado = new ArrayList<>();
        List<ExamenGinecologia> examenGinecologiaList = expediente.getExamenGinecologiaList();
        String departamento = ((Login) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("login")).getPersonal().getDepartamentoId().getNombre();
        int i = 0;

        if(departamento.equals("Ginecología")) {
            for (ExamenGinecologia eg : examenGinecologiaList) {
                resultado.add(new InterfazExamen(eg, i++));
            }

            List<ExamenColposcopia> examenColposcopiaList = expediente.getExamenColposcopiaList();
            for (ExamenColposcopia ec : examenColposcopiaList) {
                resultado.add(new InterfazExamen(ec,i++));
            }

            List<ExamenMonitoreoFetal> monitoreoFetalList = expediente.getMonitoreoFetalList();
            for (ExamenMonitoreoFetal mf : monitoreoFetalList) {
                resultado.add(new InterfazExamen(mf,i++));
            }
        }

        if(departamento.equals("Odontología")) {
            List<ExamenOdontologia> examenOdontologiaList = expediente.getExamenOdontologiaList();
            for (ExamenOdontologia eo : examenOdontologiaList) {
                resultado.add(new InterfazExamen(eo, i++));
            }
        }
        
        return resultado;
    }

    public List<Documentos> getUltimosDocumentos(Integer maxResults) {
        ultimosDocumentoss = documentosController.findUltimos(paciente.getCedula(), maxResults);

        return ultimosDocumentoss;
    }

    public void setUltimosDocumentoss(List<Documentos> ultimosDocumentoss) {
        this.ultimosDocumentoss = ultimosDocumentoss;
    }

    public List<Documentos> getListaDocumentos() {
        this.listaDocumentos = documentosController.findRDocumentosByCedulaPaciente(expediente.getPacientecedula().getCedula());

        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public UploadedFile getArchivo() {
        return archivo;
    }

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }

    public void setSelectedExamen(Valor selectedExamen) {
        this.selectedExamen = selectedExamen;
    }

    public Valor getSelectedExamen() {
        return selectedExamen;
    }

    public List<InterfazExamen> getResultado() {
        return resultado;
    }

    public void setResultado(List<InterfazExamen> resultado) {
        this.resultado = resultado;
    }

    public List<InterfazExamen> getListaFiltrada() {
        return listaFiltrada;
    }

    public void setListaFiltrada(List<InterfazExamen> listaFiltrada) {
        this.listaFiltrada = listaFiltrada;
    }

    public String[] getExamenesPorDepartamento() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Login log = (Login) ec.getSessionMap().get("login");

        Personal p = log.getPersonal();
        int consultorio = p.getDepartamentoId().getId();

        String[] listaGine = {"Físico", "Colposcopía", "Monitoreo Fetal"};
        String[] listaOdonto = {"Odontograma"};

        switch (consultorio) {
            case 2:
                return listaGine;
            case 3:
                return listaOdonto;
            default:
                return null;
        }
    }

    public void redireccionExamenes() {
        if (dato != null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String URL = ec.getRequestContextPath();

            switch (dato.getTipoExamen()) {
                case "Físico":/*Examen Fisico*/
                    URL += "/app/consultorios/ginecologia/examen/fisico?faces-redirect=true";
                    break;
                case "Monitoreo Fetal":/*Monitoreo Fetal*/
                    URL += "/app/consultorios/ginecologia/examen/monitoreoFetal?faces-redirect=true";
                    break;
                case "Colposcopía":/*Colposcopia*/
                    URL += "/app/consultorios/ginecologia/examen/colposcopia?faces-redirect=true";
                    break;
                case "Odontológico":/*Odontograma*/
                    URL += "/app/consultorios/odontologia/examen/odontologico?faces-redirect=true";
                    break;
            }

            try {
                ec.getSessionMap().put("examen", dato.getExamen());
                ec.redirect(URL);
            } catch (IOException ex) {
                Logger.getLogger(ExpedienteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public InterfazExamen getDato() {
        return dato;
    }

    public void setDato(InterfazExamen dato) {
        this.dato = dato;
    }

}
