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
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordTextField; // New TextField to display the password as plain text
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink signUpLink;
    @FXML
    private CheckBox showPasswordCheckbox;
    @FXML
    public Hyperlink forgotPasswordLink;

    private ServiceUser userService = new ServiceUser();

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> login());

        // Initialize passwordTextField to be invisible
        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);

        showPasswordCheckbox.setOnAction(event -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        if (showPasswordCheckbox.isSelected()) {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setManaged(true);
            passwordTextField.setVisible(true);
            passwordField.setManaged(false);
            passwordField.setVisible(false);
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordField.setManaged(true);
            passwordField.setVisible(true);
            passwordTextField.setManaged(false);
            passwordTextField.setVisible(false);
        }
    }

    private void login() {
        String email = emailField.getText();
        String password = showPasswordCheckbox.isSelected() ? passwordTextField.getText() : passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Tous les champs sont obligatoires.");
            return;
        }

        User user = userService.authenticateUser(email, password);

        if (user != null) {
            SessionManager.getInstance().login(user);

            switch (user.getUserType()) {
                case "RH":
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
                    System.out.println("Bienvenue Employee !");
                    break;
                case "Candidat":
                    System.out.println("Bienvenue Candidat !");
                    break;
                default:
                    break;
            }
        } else {
            showError("Email ou mot de passe incorrect.");
        }
    }

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
            showError("Impossible d'ouvrir la page d'inscription.");
        }
    }

    @FXML
    private void handleForgotPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForgotPassword.fxml"));
            AnchorPane forgotPasswordPage = loader.load();

            // Use emailField to get the stage in case forgotPasswordLink has issues
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(forgotPasswordPage);
            stage.setScene(scene);
            stage.setTitle("Réinitialisation du mot de passe");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Impossible d'ouvrir la page de réinitialisation du mot de passe.");
        }
    }

}
