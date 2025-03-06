package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private Hyperlink signUpLink;

    private ServiceUser userService = new ServiceUser();  // Service d'authentification des utilisateurs

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> login());
    }

    // Méthode pour tenter la connexion de l'utilisateur
    private void login() {
        String email = emailField.getText();  // Récupère l'email
        String password = passwordField.getText();  // Récupère le mot de passe

        if (email.isEmpty() || password.isEmpty()) {
            showError("Tous les champs sont obligatoires.");
            return;
        }

        User user = userService.authenticateUser(email, password);  // Tente de s'authentifier

        if (user != null) {
            // Si l'authentification réussie, rediriger l'utilisateur en fonction de son type
            SessionManager.getInstance().login(user);  // Enregistre l'utilisateur dans la session

            // Exemple : si l'utilisateur est un RH, rediriger vers le tableau de bord RH
            switch (user.getUserType()) {
                case "RH":
                    // Redirige vers la page RH
                    System.out.println("Bienvenue RH !");
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEmployee.fxml"));
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException e) {
                        showError("Erreur lors du chargement de la page AfficherEmployee.");
                    }
                    break;
                case "Employee":
                    // Redirige vers la page Employee
                    System.out.println("Bienvenue Employee !");
                    break;
                case "Candidat":
                    // Redirige vers la page Candidat
                    System.out.println("Bienvenue Candidat !");
                    break;
                default:
                    break;
            }
        } else {
            showError("Email ou mot de passe incorrect.");
        }
    }


    // Méthode pour afficher une alerte d'erreur
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
            showError( "Impossible d'ouvrir la page d'inscription.");
        }
    }
}
