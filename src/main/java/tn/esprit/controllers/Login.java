package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.SessionManager;

public class Login {

    @FXML
    private TextField emailField;  // Champ pour l'email
    @FXML
    private PasswordField passwordField;  // Champ pour le mot de passe
    @FXML
    private Button loginButton;  // Bouton de connexion

    private ServiceUser userService = new ServiceUser();  // Service d'authentification des utilisateurs

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> login());
    }
@FXML
    private void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Tous les champs sont obligatoires.");
            return;
        }

        User user = userService.authenticateUser(email, password);

        if (user != null) {
            // Enregistre l'utilisateur dans la session
            SessionManager.getInstance().login(user);
            System.out.println("Tentative de connexion avec : " + user.getId() + " | " + user.getUserType());

            // Ajouter un log pour vérifier l'utilisateur connecté
            System.out.println("Utilisateur connecté : " + user.getUserType());

            // Vérification du type d'utilisateur pour rediriger
            if (SessionManager.getInstance().isRH()) {
                openOffreEmploiForRH();
            } else if (SessionManager.getInstance().isCandidat()) {  // Vérifie si l'utilisateur est un candidat
                openGestionEntretienForCandidat();
            } else {
                showError("Vous n'avez pas les permissions pour accéder à cette plateforme.");
            }

        } else {
            showError("Email ou mot de passe incorrect.");
        }
    }

    private void openOffreEmploiForRH() {
        try {
            // Vérification de l'emailField.getScene()
            if (emailField.getScene() == null) {
                showError("Erreur : La scène actuelle est introuvable.");
                return;
            }

            Stage stage = (Stage) emailField.getScene().getWindow();  // Obtient la fenêtre actuelle (stage)

            // Charge le fichier FXML (Assurez-vous que le chemin est correct)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MenuController.fxml"));
            Scene scene = new Scene(loader.load());  // Crée une nouvelle scène à partir du FXML
           stage.setScene(scene);  // Définit la scène sur le stage
           // stage.setTitle("Gestion des Offres d'Emploi (RH)");  // Change le titre de la fenêtre

        } catch (Exception e) {
            e.printStackTrace();
            showError("Impossible d'ouvrir la page des offres pour RH.");
        }
    }
    private void openGestionEntretienForCandidat() {
        try {
            if (emailField.getScene() == null) {
                showError("Erreur : La scène actuelle est introuvable.");
                return;
            }

            Stage stage = (Stage) emailField.getScene().getWindow();  // Récupère la fenêtre actuelle

            // Charge le fichier FXML de la gestion des entretiens
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionEntretien.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Gestion des Entretiens (Candidat)");  // Change le titre de la fenêtre

        } catch (Exception e) {
            e.printStackTrace();
            showError("Impossible d'ouvrir la page GestionEntretien.");
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
}
