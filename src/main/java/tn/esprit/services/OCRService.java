package tn.esprit.services;

import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.Tesseract;


import java.io.File;

public class OCRService {
    private final Tesseract tesseract;

    public OCRService() {
        tesseract = new Tesseract();
        // Le chemin vers tessdata sous le répertoire lib
        tesseract.setDatapath("lib/tessdata");  // Chemin relatif
        tesseract.setLanguage("fra"); // Langue française
    }

    public String extractTextFromImage(File file) {
        try {
            return tesseract.doOCR(file);
        } catch (TesseractException e) {
            e.printStackTrace();
            return "Erreur lors de l'extraction du texte.";
        }
    }
}
