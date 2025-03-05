package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.RH;
import tn.esprit.services.ServiceRH;

import java.sql.Date;
import java.io.IOException;

public class SignUp {

    @FXML
    private TextField nomField, prenomField, emailField, numTelField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    private Button signUpButton, returnButton;

    @FXML
    private Label errorLabel;

    private final ServiceRH serviceRH = new ServiceRH();

    @FXML
    private void handleSignUp() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String numTel = numTelField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        errorLabel.setVisible(false);

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || numTel.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Veuillez remplir tous les champs !");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Les mots de passe ne correspondent pas !");
            return;
        }

        try {
            RH newRH = new RH(numTel, 0, nom, prenom, "", email, "", new Date(System.currentTimeMillis()), "RH", password, "", 0);
            serviceRH.add(newRH);

            showSuccessAndRedirect("Inscription réussie !");
        } catch (Exception e) {
            showError("Erreur lors de l'inscription.");
        }
    }

    @FXML
    private void handleReturn() {
        navigateToLogin();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccessAndRedirect(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        // Redirect to Login Page
        navigateToLogin();
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Page");
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page de connexion.");
        }
    }
}
