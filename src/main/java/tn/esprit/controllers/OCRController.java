/*
package tn.esprit.controllers;

import tn.esprit.services.OCRService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class OCRController {
    private final OCRService ocrService = new OCRService();

    @FXML
    private TextArea textArea;

    @FXML
    public void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            String extractedText = ocrService.extractTextFromImage(file);
            textArea.setText(extractedText);
        }
    }
}

 */

