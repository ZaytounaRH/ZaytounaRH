package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.models.RH;
import tn.esprit.services.ServiceRH;

public class Login {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label emailError;

    @FXML
    private Label passwordError;

    private final ServiceRH serviceRH = new ServiceRH(); // Service to get RH details

    @FXML
    private void handleLogin() {
        // Get user input
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Reset error labels
        emailError.setVisible(false);
        passwordError.setVisible(false);

        // Validate input fields
        if (email.isEmpty()) {
            emailError.setText("L'email est requis !");
            emailError.setVisible(true);
            return;
        }

        if (password.isEmpty()) {
            passwordError.setText("Le mot de passe est requis !");
            passwordError.setVisible(true);
            return;
        }

        // Authenticate user
        RH authenticatedRH = serviceRH.authenticate(email, password);

        if (authenticatedRH != null) {
            showSuccess("Connexion réussie !", "Bienvenue, " + authenticatedRH.getNom());
            navigateToDashboard();
        } else {
            showError("Échec de connexion", "Email ou mot de passe incorrect.");
        }
    }

    private void navigateToDashboard() {
        try {
            // Load the RH Dashboard interface
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/AfficherEmployee.fxml"));
            AnchorPane dashboardPage = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(dashboardPage);
            stage.setScene(scene);
            stage.setTitle("Dashboard RH");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir le tableau de bord.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
