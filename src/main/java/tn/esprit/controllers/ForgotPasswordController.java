package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.EmailService;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;
    @FXML
    public Label errorMessage;
    @FXML
    private Hyperlink backToLogin;

    private ServiceUser userService = new ServiceUser();
    private EmailService emailService = new EmailService();


    @FXML
    private void handleSendCode() {
        String email = emailField.getText();

        if (email.isEmpty()) {
            showError("L'email est requis.");
            return;
        }

        // Check if the email exists in the system
        if (userService.isEmailRegistered(email)) {
            // Generate a verification code
            String verificationCode = emailService.generateVerificationCode();

            // Send email with verification code
            String subject = "Code de vérification pour la réinitialisation du mot de passe";
            String body = "Votre code de vérification est : " + verificationCode;
            emailService.sendEmail(email, subject, body);

            showError("Un code de vérification a été envoyé à votre email.");
        } else {
            showError("Cet email n'est pas enregistré.");
        }
    }
    @FXML
    private void handleBackToLogin() {
        if (backToLogin != null) {
            try {
                // Load the Login.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                AnchorPane loginPage = loader.load();

                // Get the current stage and set the new scene
                Stage stage = (Stage) backToLogin.getScene().getWindow();
                Scene scene = new Scene(loginPage);
                stage.setScene(scene);
                stage.setTitle("Connexion");
            } catch (IOException e) {
                e.printStackTrace();
                showError("Impossible d'ouvrir la page de connexion.");
            }
        } else {
            System.out.println("Hyperlink 'backToLogin' is null.");
        }
    }


    private void showError(String message) {
        errorMessage.setText(message);
    }
}
