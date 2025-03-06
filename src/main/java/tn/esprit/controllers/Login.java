package tn.esprit.controllers;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.SessionManager;
import javafx.event.ActionEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Login {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink signUpLink;
    @FXML
    private CheckBox showPasswordCheckbox;
    @FXML
    private Hyperlink forgotPasswordLink;
    @FXML
    private ImageView captchaImageView;
    @FXML
    private TextField captchaField;
    @FXML
    private Button refreshCaptchaButton;

    private ServiceUser userService = new ServiceUser();
    private DefaultKaptcha captchaProducer;
    private String generatedCaptcha;

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> login());

        // Initialize password visibility toggle
        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);
        showPasswordCheckbox.setOnAction(event -> togglePasswordVisibility());

        // Initialize CAPTCHA
        setupCaptcha();
        generateCaptcha();
        refreshCaptchaButton.setOnAction(event -> generateCaptcha());
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

    private void setupCaptcha() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.textproducer.char.string", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        properties.setProperty("kaptcha.textproducer.font.size", "40");
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "50");

        Config config = new Config(properties);
        captchaProducer = new DefaultKaptcha();
        captchaProducer.setConfig(config);
    }

    private void generateCaptcha() {
        generatedCaptcha = captchaProducer.createText();
        BufferedImage bufferedImage = captchaProducer.createImage(generatedCaptcha);

        // Convert BufferedImage to JavaFX Image
        Image captchaImage = convertBufferedImageToFXImage(bufferedImage);
        captchaImageView.setImage(captchaImage);
    }

    private Image convertBufferedImageToFXImage(BufferedImage bufferedImage) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
            return new Image(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void login() {
        String email = emailField.getText();
        String password = showPasswordCheckbox.isSelected() ? passwordTextField.getText() : passwordField.getText();
        String enteredCaptcha = captchaField.getText();

        if (email.isEmpty() || password.isEmpty() || enteredCaptcha.isEmpty()) {
            showError("Tous les champs sont obligatoires.");
            return;
        }

        if (!enteredCaptcha.equalsIgnoreCase(generatedCaptcha)) {
            showError("CAPTCHA incorrect !");
            generateCaptcha();
            return;
        }

        User user = userService.authenticateUser(email, password);

        if (user != null) {
            SessionManager.getInstance().login(user);

            // Check user type and navigate to respective dashboard
            switch (user.getUserType()) {
                case "RH":
                    loadScene("/AfficherEmployee.fxml", "Bienvenue RH !");
                    break;
                case "Employee":
                    loadScene("/EmployeeDashboard.fxml", "Bienvenue Employee !");
                    break;
                case "Candidat":
                    loadScene("/CandidatDashboard.fxml", "Bienvenue Candidat !");
                    break;
                default:
                    showError("Type d'utilisateur inconnu.");
                    break;
            }
        } else {
            showError("Email ou mot de passe incorrect.");
        }
    }


    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page.");
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
        loadScene("/SignUp.fxml", "Inscription RH");
    }

    @FXML
    private void handleForgotPassword() {
        loadScene("/ForgotPassword.fxml", "Réinitialisation du mot de passe");
    }
    @FXML
    private void refreshCaptcha(ActionEvent event) {
        generateCaptcha();
        System.out.println("Captcha refreshed!");  // Debugging

    }
}
