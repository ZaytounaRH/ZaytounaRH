package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.regex.Pattern;

public class Login {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label emailError, passwordError;

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        emailError.setVisible(false);
        passwordError.setVisible(false);

        boolean isValid = true;

        if (!isValidEmail(email)) {
            emailError.setText("Format d'email invalide !");
            emailError.setVisible(true);
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordError.setText("Mot de passe requis !");
            passwordError.setVisible(true);
            isValid = false;
        }

        if (isValid) {
            if (isValidCandidate(email, password)) {
                openGestionEntretien();
            } else {
                showAlert("Erreur", "Email ou mot de passe incorrect !");
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidCandidate(String email, String password) {
        // Requête avec une jointure entre 'users' et 'candidat'
        String query = "SELECT u.* FROM users u " +
                "JOIN candidat c ON u.id = c.candidat_id " +  // 'user_id' fait référence à 'users'
                "WHERE u.email = ? AND u.password = ?";

        try (Connection cnx = MyDatabase.getInstance().getCnx();
             PreparedStatement pst = cnx.prepareStatement(query)) {

            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return true;  // Connexion réussie
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'utilisateur : " + e.getMessage());
        }
        return false;  // Connexion échouée
    }

    private void openGestionEntretien() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionEntretien.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Gestion des Entretiens");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page des entretiens.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
