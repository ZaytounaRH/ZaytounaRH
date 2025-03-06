package tn.esprit.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import tn.esprit.models.Certification;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PdfService {
    public void genererCertificatPdf(Certification certif, Date dateObtention) {
        // Chemin de sauvegarde du PDF
        String outputPath = "certification_" + certif.getTitreCertif() + ".pdf";

        try {
            // Créer un PdfWriter pour écrire dans le fichier de sortie
            PdfWriter writer = new PdfWriter(outputPath);

            // Créer un PdfDocument avec le writer
            PdfDocument pdf = new PdfDocument(writer);

            // Créer un Document (interface de haut niveau)
            Document document = new Document(pdf);

            // Ajouter des éléments dans le PDF
            // Titre de la certification
            document.add(new Paragraph(new Text(certif.getTitreCertif()).setBold().setFontSize(18)));

            // Organisme de certification
            document.add(new Paragraph("Organisme: " + certif.getOrganismeCertif()).setFontSize(12));

            // Date d'obtention
            String dateString = (dateObtention != null) ? "Date d'obtention: " + dateObtention.toString() : "Date d'obtention: N/A";
            document.add(new Paragraph(dateString).setFontSize(12));

            // Fermer le document pour finaliser le PDF
            document.close();

            System.out.println("PDF généré avec succès: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
