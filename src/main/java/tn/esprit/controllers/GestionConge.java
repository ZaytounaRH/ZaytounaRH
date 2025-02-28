package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.interfaces.Iservice;
import tn.esprit.models.Conge;

import java.time.LocalDate;
import java.sql.Date;

public class GestionCongé {

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField motifField;

    @FXML
    private Button ajouterButton;

    @FXML
    private void ajouterConge(ActionEvent event) {
        // Récupération des valeurs des champs
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        String motif = motifField.getText();

        // Validation des données
        if (dateDebut == null || dateFin == null || motif.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        if (dateFin.isBefore(dateDebut)) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La date de fin doit être après la date de début.");
            return;
        }

        // Ajout du congé (simulé ici, tu peux l'enregistrer en base de données)
        System.out.println("Congé ajouté : Début " + dateDebut + ", Fin " + dateFin + ", Motif : " + motif);

        // Réinitialisation des champs après ajout
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        motifField.clear();

        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Congé ajouté avec succès !");
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
