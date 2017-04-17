package com.sicom.web.beans;

import com.sicom.controller.AntecedentesGinecologiaJpaController;
import com.sicom.controller.exceptions.IllegalOrphanException;
import com.sicom.entities.AntecedentesGinecologia;
import com.sicom.entities.Paciente;
import java.io.FileInputStream;
import java.io.IOException;
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
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

@ManagedBean
@ViewScoped
public class AntecedentesGinecologiaBean {

    private AntecedentesGinecologia antecedentesGinecologia;
    private final AntecedentesGinecologiaJpaController agc;
    private Date fecha;
    private Paciente paciente;
    private Boolean antecedenteNuevo;

    /**
     * Constructor
     */
    public AntecedentesGinecologiaBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SICOM_v1PU");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        agc = new AntecedentesGinecologiaJpaController(emf);

        paciente = (Paciente) ec.getSessionMap().remove("paciente");
        antecedentesGinecologia = (AntecedentesGinecologia) ec.getSessionMap().remove("antecedente");

        if (paciente != null) {
            if (antecedentesGinecologia == null) {
                antecedentesGinecologia = new AntecedentesGinecologia();
                antecedenteNuevo = true;
            } else {
                antecedenteNuevo = false;
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

    public void modificar() throws Exception {
        agc.edit(antecedentesGinecologia);
    }

    public void save() {

        if (antecedenteNuevo) {
            try {
                antecedentesGinecologia.setFecha(new Date());
                antecedentesGinecologia.setExpedientePacientecedula(paciente.getExpediente());
                agc.create(antecedentesGinecologia);

                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información agregada exitósamente.", null));
                String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
                ec.getRequestMap().put("paciente", paciente);
                ec.redirect(URL);

            } catch (IllegalOrphanException | IOException ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                antecedentesGinecologia.setFecha(new Date());
                antecedentesGinecologia.setExpedientePacientecedula(paciente.getExpediente());
                agc.edit(antecedentesGinecologia);

                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información modificada exitósamente.", null));
                String URL = ec.getRequestContextPath() + "/app/paciente/informacion";
                ec.getRequestMap().put("paciente", paciente);
                ec.redirect(URL);

            } catch (IllegalOrphanException | IOException ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(AntecedentesOdontologiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void cancelarAction() {

    }

    public AntecedentesGinecologia getObjAntecedente() {
        return antecedentesGinecologia;
    }

    public void setObjAntecedente(AntecedentesGinecologia nuevoAntecedente) {
        this.antecedentesGinecologia = nuevoAntecedente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
            int spaceIndex = texto.indexOf(' ', lastSpace + 1);
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
        try {
            Date fecha = new Date();
            System.out.println("Creando el pdf");
            String fileName = "Historia_Clinica_" + paciente.getNombre() + ".pdf";
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();

            int y = 720;

            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            //<editor-fold defaultstate="collapsed" desc="Header del pdf">
            //PDXObjectImage image = new PDJpeg(doc, new FileInputStream("/imagenes/icono.png"));
            //content.drawImage(image, 450, 650);
            nuevaLinea(content, 550, 40, 12, "1");
            nuevaLinea(content, 80, y, 10, "Fecha:" + fecha.getDay() + "/" + fecha.getMonth() + "/" + (fecha.getYear() + 1900));

            nuevaLinea(content, 280, y, 10, "Centro Medico Navas");
            y -= 10;
            nuevaLinea(content, 305, y, 10, "La sabana");
            y -= 10;
            nuevaLinea(content, 282, y, 10, "Telefono: 2233-1010");
            y -= 10;
            nuevaLinea(content, 285, y, 10, "Dra. Maria del Pilar");

            y -= 60;
            nuevaLinea(content, 80, y, 12, "Historia Clinica: "+paciente.getNombre()+paciente.getPrimerApellido());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Fecha de creacion: "+antecedentesGinecologia.getFecha());
//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Datos personales">
            y -= 10;
            content.drawLine(80, y, 440, y);
            y -= 15;
            nuevaLinea(content, 80, y, 12, "Datos Personales");
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Nombre y apellidos: "+paciente.getNombre()+" "+paciente.getPrimerApellido()+" "+paciente.getSegundoApellido());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Identificación: "+paciente.getCedula());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Edad: "+(fecha.getYear()-(paciente.getNacimiento().getYear()+1900)));
            nuevaLinea(content, 150, y, 10, "Sexo: "+paciente.getGenero());
            nuevaLinea(content, 240, y, 10, "Ocupacion: "+paciente.getOcupacion());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Fecha de nacimiento: "+paciente.getNacimiento());
            nuevaLinea(content, 240, y, 10, "Domicilio: "+paciente.getDomicilio());
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Estado Civil: "+paciente.getEstadoCivil());
            //nuevaLinea(content, 210, y, 10, "Nacionalidad: "+paciente.ge);
            y -= 15;
            nuevaLinea(content, 80, y, 10, "Telefono(s): "+paciente.getTelefono()+"/"+paciente.getCelular());
            nuevaLinea(content, 250, y, 10, "Correo electronico: "+paciente.getCorreo());

            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="Antecedentes">
            y -= 20;
            content.drawLine(80, y, 440, y);
            y -= 25;
            nuevaLinea(content, 80, y, 11, "Antecedentes Hereditarios: ");
            y = textoLargo(antecedentesGinecologia.getHerediatarios(), content, page, 220, y);

            y -= 20;
            nuevaLinea(content, 80, y, 11, "Antecedentes Patologicos: ");
            y = textoLargo(antecedentesGinecologia.getPatologicos(), content, page, 220, y);

            y -= 20;
            nuevaLinea(content, 80, y, 11, "Antecedentes No Patologicos: ");
            y = textoLargo(antecedentesGinecologia.getNoPatologicos(), content, page, 230, y);

            y -= 20;
            nuevaLinea(content, 80, y, 11, "Antecedentes Quirurgicos: ");
            y = textoLargo(antecedentesGinecologia.getQuirurgicos(), content, page, 220, y);
            y -= 20;//200
            content.drawLine(80, y, 440, y);

            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="Resto de la historia clinica">
            if (y < 80) { //verificar si es necesario crear una nueva pagina despues de los antecedentes
                content.close();
                PDPage page2 = new PDPage();
                doc.addPage(page2);
                content = new PDPageContentStream(doc, page2);
                y = 720;
                nuevaLinea(content, 550, 40, 12, "2");
            }

            y -= 30;
            nuevaLinea(content, 80, y, 10, "Menarca: "+antecedentesGinecologia.getMenarca());
            nuevaLinea(content, 160, y, 10, "FUR: "+antecedentesGinecologia.getFur().getYear()+"/"+antecedentesGinecologia.getFur().getMonth());
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Ciclo menstrual: "+antecedentesGinecologia.getCicloMestrual()+", "+antecedentesGinecologia.getComentarioCicloMestrual());
            y -= 20;
            nuevaLinea(content, 80, y, 10, "PRS: "+antecedentesGinecologia.getPrs());
            nuevaLinea(content, 160, y, 10, "Compañeros Sexuales: "+antecedentesGinecologia.getCompanerosSexuales());
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Planificacion: "+antecedentesGinecologia.getPlanificacion());
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Actividad Sexual: "+antecedentesGinecologia.getActividadSexual());
            y -= 20;
            if (y < 80) {
                content.close();
                PDPage page2 = new PDPage();
                doc.addPage(page2);
                content = new PDPageContentStream(doc, page2);
                y = 680;
                nuevaLinea(content, 550, 40, 12, "2");
            }
            nuevaLinea(content, 80, y, 10, "Ultimo Parto: "+antecedentesGinecologia.getUltimoParto().getYear());
            nuevaLinea(content, 210, y, 10, "Ultimo Pap: "+antecedentesGinecologia.getUltimoPap());
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Lactancia Materna: "+(antecedentesGinecologia.getLactanciaMaterna()? "Si":"No")+" "+antecedentesGinecologia.getComentarioLactancia());
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Tipo de parto: "+antecedentesGinecologia.getTipoParto());
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Menopausia: "+antecedentesGinecologia.getMenopausia());
            nuevaLinea(content, 170, y, 10, "Gesta: "+antecedentesGinecologia.getGesta());
            nuevaLinea(content, 235, y, 10, "Partos: "+antecedentesGinecologia.getPartos());
            nuevaLinea(content, 310, y, 10, "Abortos: "+antecedentesGinecologia.getAbortos());
            nuevaLinea(content, 390, y, 10, "ectopico: "+antecedentesGinecologia.getEctopico());
            y -= 20;
            nuevaLinea(content, 80, y, 10, "GPA: "+antecedentesGinecologia.getComentarioGPA());
            y -= 20;
            nuevaLinea(content, 80, y, 10, "Informacion Adicional:");
            y = textoLargo(antecedentesGinecologia.getInformacionAdicional(), content, page, 190, y);

//</editor-fold>

//            content.close();
//            doc.save(fileName);
            // Prepare response.
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/pdf");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            content.close();
            // Write file to response body.
            doc.save(externalContext.getResponseOutputStream());

            // Inform JSF that response is completed and it thus doesn't have to navigate.
            facesContext.responseComplete();

            doc.close();

            System.out.println("El Archivo fue guardado en: " + externalContext.getResponseOutputStream());

        } catch (IOException e) {

            System.out.println(e.getMessage());

        }

    }

}
