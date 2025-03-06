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
    private TextField verificationCodeField; // New field for verification code
    @FXML
    private TextField newPasswordField; // New field for new password
    @FXML
    public Label errorMessage;
    @FXML
    private Hyperlink backToLogin;

    private ServiceUser userService = new ServiceUser();
    private EmailService emailService = new EmailService();

    private String generatedCode; // Store the generated verification code for comparison

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
            generatedCode = emailService.generateVerificationCode();

            // Send email with verification code
            String subject = "Code de vérification pour la réinitialisation du mot de passe";
            String body = "Votre code de vérification est : " + generatedCode;
            emailService.sendEmail(email, subject, body);

            showError("Un code de vérification a été envoyé à votre email.");
        } else {
            showError("Cet email n'est pas enregistré.");
        }
    }

    @FXML
    private void handleVerifyCodeAndChangePassword() {
        String enteredCode = verificationCodeField.getText();
        String newPassword = newPasswordField.getText();

        // Validate inputs
        if (enteredCode.isEmpty() || newPassword.isEmpty()) {
            showError("Le code de vérification et le mot de passe sont requis.");
            return;
        }

        if (!enteredCode.equals(generatedCode)) {
            showError("Code de vérification incorrect.");
            return;
        }

        // Code is correct, update the password in the system
        userService.updatePassword(emailField.getText(), newPassword);

        showError("Votre mot de passe a été réinitialisé avec succès.");
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
