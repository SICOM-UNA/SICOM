package com.sicom.PDF;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

/**
 *
 * @author luis
 */

/*

EJEMPLO!!!!!!!!!!!!!!


*/
public class Creator {

    private static void newLine(PDPageContentStream content, int y, int x, int font, String str) throws IOException {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA, font);
        content.moveTextPositionByAmount(x, y);
        content.drawString(str);
        content.endText();
    }

    public Creator() {
        try {

            System.out.println("Create Simple PDF file with Text");
            String fileName = "PdfWithtext.pdf"; // name of our file

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();

            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            int y = 750, x = 50, f = 10;

            for (int i = 0; i < 10; i++) {
                newLine(content, y, x, f, "Centro Médico Navas");
                y -= 50;
                f++;
            }

            content.drawLine(10, y, 600, y);

            content.close();
            doc.save(fileName);
            doc.close();

            System.out.println("your file created in : " + System.getProperty("user.dir"));

        } catch (IOException | COSVisitorException e) {

            System.out.println(e.getMessage());

        }
    }

}
