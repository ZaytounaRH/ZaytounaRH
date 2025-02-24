package tn.esprit.getionfinanciere.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.MainFX;
import tn.esprit.getionfinanciere.interfaces.IService;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.models.enums.TypeService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import tn.esprit.getionfinanciere.services.ServiceFournisseur;

import javax.swing.*;

public class GestionFournisseur {

    @FXML
    private TextField tfNomFournisseur;
    @FXML
    private TextField tfAdresse;
    @FXML
    private TextField tfContact;
    @FXML
    private ComboBox<TypeService> cbTypeService;
    @FXML
    private Button afficherButton;
    @FXML
    private Button homepageButton;

    IService<Fournisseur> sf = new ServiceFournisseur();

    @FXML
    public void initialize() {
        // Populate ComboBox with values from TypeService enum
        cbTypeService.getItems().setAll(TypeService.values());
    }
    // Adding a new supplier with validation
    @FXML
    public void ajouterFournisseur(ActionEvent actionEvent) {
        // Validate that all fields are filled
        if (tfNomFournisseur.getText().isEmpty() || tfAdresse.getText().isEmpty() ||
            tfContact.getText().isEmpty() || cbTypeService.getValue() == null) {
            // Show alert if validation fails
            showAlert(AlertType.ERROR, "Formulaire incomplet", "Tous les champs doivent être remplis.");
            return;
        }

        if (!tfNomFournisseur.getText().matches("[a-zA-ZÀ-ÿ\\s&'%',]+")) {
            showAlert(AlertType.ERROR,"Saisie invalide" ,"Le nom du fournisseur ne doit contenir que des lettres.");
            return;
        }

        if (!tfAdresse.getText().matches("[a-zA-Z0-9À-ÿ\\s]+")) {
            showAlert(AlertType.ERROR,"Saisie invalide" ,"L'adresse ne doit contenir que des lettres et des chiffres.");
            return;
        }

        // Validation du Contact (exactement 8 chiffres)
        if (!tfContact.getText().matches("[1-9][0-9]{7}")) {
            showAlert(AlertType.ERROR,"Saisie invalide" ,"Le contact doit être composé de 8 chiffres et ne doit pas commencer par 0.");
            return;
        }



        // Create and add the new Fournisseur
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNomFournisseur(tfNomFournisseur.getText());
        fournisseur.setAdresse(tfAdresse.getText());
        fournisseur.setContact(tfContact.getText());
        fournisseur.setTypeService(cbTypeService.getValue());

        sf.add(fournisseur);
        System.out.println("Fournisseur ajouté : " + fournisseur);

        // Clear the input fields after submission
        showAlert(AlertType.INFORMATION, "Fournisseur ajouté", "Le fournisseur a été ajouté.");

        clearFields();
    }

    // Display the list of all suppliers with formatting
    @FXML
    public void afficherFournisseurs(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("fournisseur_list_view.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) afficherButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backhome(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("home_view.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) homepageButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to clear input fields
    private void clearFields() {
        tfNomFournisseur.clear();
        tfAdresse.clear();
        tfContact.clear();
        cbTypeService.setValue(null);
    }

    // Helper method to show alerts for errors or confirmations
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
