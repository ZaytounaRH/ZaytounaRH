package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.models.RH;
import tn.esprit.services.ServiceRH;
import tn.esprit.utils.SessionManager;

public class Login {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label emailError, passwordError;

    @FXML
    private Hyperlink signUpLink;

    private final ServiceRH serviceRH = new ServiceRH();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        emailError.setVisible(false);
        passwordError.setVisible(false);

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

        RH authenticatedRH = serviceRH.authenticate(email, password);

        if (authenticatedRH != null) {
            SessionManager.getInstance().login(authenticatedRH);

            if (authenticatedRH instanceof RH) {
                showSuccess("Connexion réussie !", "Bienvenue, " + authenticatedRH.getNom());
                navigateToDashboard();
            } else {
                showError("Accès refusé", "Vous n'avez pas les droits pour accéder à cette section.");
                SessionManager.getInstance().logout();
            }
        } else {
            showError("Échec de connexion", "Email ou mot de passe incorrect.");
        }
    }

    @FXML
    private void goToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));
            AnchorPane signUpPage = loader.load();

            Stage stage = (Stage) signUpLink.getScene().getWindow();
            Scene scene = new Scene(signUpPage);
            stage.setScene(scene);
            stage.setTitle("Inscription RH");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir la page d'inscription.");
        }
    }

    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEmployee.fxml"));
            AnchorPane dashboardPage = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(dashboardPage);
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
