package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Certification;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployeCertification;
import tn.esprit.utils.SessionManager;
import tn.esprit.utils.MyDatabase;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.sql.*;
import java.util.List;

public class GestionEmployeFormationCertification {
    @FXML
    private ComboBox<Employee> comboBoxEmploye;
    @FXML
    private ComboBox<Certification> comboBoxCertification;
    @FXML
    private FlowPane certificationFlowPane;
    private ServiceEmployeCertification serviceEmployeCertification = new ServiceEmployeCertification();

    @FXML
    public void afficherCertifsParEmploye(ActionEvent event) {

        Employee employeSelectionne = comboBoxEmploye.getSelectionModel().getSelectedItem();

        if (employeSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un employé.");
            return;
        }
        List<Certification> certifications = serviceEmployeCertification.getCertificationsByEmployee(employeSelectionne.getId());

        certificationFlowPane.getChildren().clear();

        // Afficher les certifications dans des cartes
        for (Certification certif : certifications) {
            HBox card = new HBox(10);
            card.setStyle("-fx-padding: 10px; -fx-border-color: #cccccc; -fx-background-color: #f9f9f9; -fx-border-radius: 5px;");

            // Créer les labels pour chaque certification
            Label titreCertifLabel = new Label(certif.getTitreCertif());
            titreCertifLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label organismeCertifLabel = new Label(certif.getOrganismeCertif());
            organismeCertifLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");


            // Créer une VBox pour contenir les informations de la certification
            VBox cardContent = new VBox(4, titreCertifLabel, organismeCertifLabel);
            card.getChildren().add(cardContent);
            // Ajouter la carte au FlowPane
            certificationFlowPane.getChildren().add(card);
        }

        // Afficher un message si aucune certification n'est trouvée
        if (certifications.isEmpty()) {
            showAlert("Information", "Aucune certification trouvée pour cet employé.");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private User currentUser; // L'utilisateur connecté

    // Initialisation des tables et récupération des données
    @FXML
    public void initialize() {
        // Récupérer l'utilisateur connecté depuis la session
        currentUser = SessionManager.getInstance().getCurrentUser();


    }



}
