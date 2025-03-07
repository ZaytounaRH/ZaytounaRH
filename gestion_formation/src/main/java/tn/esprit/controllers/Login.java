package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.test.MainFX;
import tn.esprit.utils.SessionManager;

import java.io.IOException;

public class Login {

    @FXML
    private TextField emailField;  // Champ pour l'email
    @FXML
    private PasswordField passwordField;  // Champ pour le mot de passe
    @FXML
    private Button loginButton;  // Bouton de connexion

    private ServiceUser userService = new ServiceUser();
    private MainFX mainApp;
    private Runnable onLoginSuccess;


    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> login());
    }

    public void setMainApp(MainFX mainApp) {
        this.mainApp = mainApp;  // Définit l'instance de MainFX
    }

    @FXML
    private void login() {
        String email = emailField.getText();  // Récupère l'email
        String password = passwordField.getText();  // Récupère le mot de passe

        if (email.isEmpty() || password.isEmpty()) {
            showError("Tous les champs sont obligatoires.");
            return;
        }

        User user = userService.authenticateUser(email, password);
        if (user != null) {
            SessionManager.getInstance().login(user);
            System.out.println("Bienvenue " + user.getUserType() + "!");
            //loadMainView();  // Charger l'interface principale après une connexion réussie
/*
            if (onLoginSuccess != null) {
                onLoginSuccess.run();  // Cette ligne va appeler initRootLayout() dans MainFX
            }

 */
            if (SessionManager.getInstance().isEmployee()){
                openEmployerFormationCertification();
            } else if (SessionManager.getInstance().isRH()) {
                //onLoginSuccess.run();
                openMenuController();
            } else if (SessionManager.getInstance().isCandidat()) {  // Vérifie si l'utilisateur est un candidat
                openGestionEntretienForCandidat();
            }

        } else {
            showError("Email ou mot de passe incorrect.");
        }
    }
    private void openMenuController(){
        try {
            // Charger le fichier FXML de la page GestionOffreEmploi
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MenuController.fxml"));
            Parent root = loader.load();  // Utilisation de Parent au lieu de AnchorPane
            Stage stage = new Stage();
            stage.setTitle("Dashboard RH");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
            // Fermer la fenêtre de connexion
            Stage loginStage = (Stage) emailField.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
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


    private void openEmployerFormationCertification() {
        try{

            Stage stage = (Stage) emailField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("employeFormationCertification.fxml"));
            Scene scene= new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Employer Formation Certification");

        }
        catch (IOException e){
            e.printStackTrace();
            showError("Impossible d'ouvrir la page des employers formations certification.");
        }
    }
/*
    @FXML
    private void loadMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
            Scene scene = new Scene(loader.load());
            MainController controller = loader.getController();
            controller.setMainApp(mainApp);  // Passe l'instance de MainFX au contrôleur

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestion des Formations et Certifications");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Impossible de charger l'interface principale.");
        }
    }


 */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }



}
