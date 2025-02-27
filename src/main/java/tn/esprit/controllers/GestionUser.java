package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Employee;
import tn.esprit.services.ServiceEmployee;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class GestionUser {

    @FXML
    private TextField tfNom, tfPrenom, tfTelephone, tfEmail, tfGenre, tfAdresse, tfJoursOuvrables, tfJoursOuvrables1; // Password field

    @FXML
    private DatePicker dpDateNaissance;

    @FXML
    private Button btnAjouter, btnAnnuler, btnBack;

    private final ServiceEmployee serviceEmployee = new ServiceEmployee(); // Service instance

    @FXML
    private void ajouterPersonne() {
        // Retrieve input values
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String telephone = tfTelephone.getText();
        String email = tfEmail.getText();
        String genre = tfGenre.getText();
        String adresse = tfAdresse.getText();
        String password = tfJoursOuvrables1.getText();
        LocalDate dateNaissance = dpDateNaissance.getValue();
        int joursOuvrables = 0;

        // Validate numeric input
        try {
            joursOuvrables = Integer.parseInt(tfJoursOuvrables.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Jours ouvrables doit être un nombre valide !");
            return;
        }

        // Validate required fields
        if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || email.isEmpty() || genre.isEmpty() || adresse.isEmpty() || password.isEmpty() || dateNaissance == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Create Employee object
        Employee employee = new Employee();
        employee.setNom(nom);
        employee.setPrenom(prenom);
        employee.setNumTel(telephone);
        employee.setEmail(email);
        employee.setGender(genre);
        employee.setAddress(adresse);
        employee.setPassword(password);
        employee.setJoursOuvrables(joursOuvrables);
        employee.setDateDeNaissance(Date.valueOf(dateNaissance));
        employee.setUserType("EMPLOYEE"); // Automatically set as EMPLOYEE

        // Add employee to database
        serviceEmployee.add(employee);

        // Show success message
        showAlert("Succès", "L'employé a été ajouté avec succès !");

        // Clear fields
        clearFields();
    }

    @FXML
    private void annulerAjout() {
        clearFields();
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEmployee.fxml"));
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger l'interface AfficherEmployee.");
        }
    }

    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        tfTelephone.clear();
        tfEmail.clear();
        tfGenre.clear();
        tfAdresse.clear();
        tfJoursOuvrables.clear();
        tfJoursOuvrables1.clear();
        dpDateNaissance.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
