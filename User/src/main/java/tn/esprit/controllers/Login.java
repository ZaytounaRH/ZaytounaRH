package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.SessionManager;

import java.io.IOException;

public class Login {

    @FXML
    private TextField emailField;  // Champ pour l'email
    @FXML
    private PasswordField passwordField;  // Champ pour le mot de passe
    @FXML
    private Button loginButton;  // Bouton de connexion

    private final ServiceUser userService = new ServiceUser();  // Service d'authentification des utilisateurs

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> login());
    }

    // ✅ Méthode pour tenter la connexion de l'utilisateur
    private void login() {
        String email = emailField.getText();  // Récupère l'email
        String password = passwordField.getText();  // Récupère le mot de passe

        if (email.isEmpty() || password.isEmpty()) {
            showError("Tous les champs sont obligatoires.");
            return;
        }

        User user = userService.authenticateUser(email, password);  // Tente de s'authentifier

        if (user != null) {
            // ✅ Enregistre l'utilisateur dans la session
            SessionManager.getInstance().login(user);

            // ✅ Redirige vers l'interface appropriée
            redirectToConges();
        } else {
            showError("Email ou mot de passe incorrect.");
        }
    }

    // ✅ Méthode pour afficher une alerte d'erreur
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ✅ Méthode pour rediriger vers la page des congés après connexion
    private void redirectToConges() {
        try {
            // ✅ Récupère la fenêtre actuelle
            Stage stage = (Stage) loginButton.getScene().getWindow();

            // ✅ Charge le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeConges.fxml"));
            Parent root = loader.load();

            // ✅ Change la scène pour afficher la liste des congés
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Congés");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page des congés.");
        }
    }
}
