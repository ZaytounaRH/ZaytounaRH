package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AjouterConge {

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField motif;

    @FXML
    void validerConge() {
        System.out.println("Congé ajouté : " + dateDebut.getValue() + " - " + dateFin.getValue() + ", Motif: " + motif.getText());
        // Ici, tu peux insérer les données dans la base de données
    }
}




