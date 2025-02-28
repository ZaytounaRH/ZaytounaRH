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

        // Validation de l'email
        if (!isValidEmail(email)) {
            emailError.setText("Format d'email invalide !");
            emailError.setVisible(true);
            isValid = false;
        }

        // Validation du mot de passe
        if (password.isEmpty()) {
            passwordError.setText("Mot de passe requis !");
            passwordError.setVisible(true);
            isValid = false;
        }

        // Si les informations sont valides, on vérifie l'utilisateur
        if (isValid) {
            if (isValidUser(email, password)) {
                // La méthode isValidUser vérifiera le rôle et ouvrira la bonne scène
            } else {
                showAlert("Erreur", "Email ou mot de passe incorrect !");
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidUser(String email, String password) {
        String query = "SELECT u.user_type FROM users u WHERE u.email = ? AND u.password = ?";

        try (Connection cnx = MyDatabase.getInstance().getCnx();
             PreparedStatement pst = cnx.prepareStatement(query)) {

            if (cnx == null || cnx.isClosed()) {
                showAlert("Erreur", "La connexion à la base de données est fermée.");
                return false;
            }

            pst.setString(1, email);
            pst.setString(2, password); // Il serait préférable de hacher le mot de passe avant de le stocker en base

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("user_type");

                    // Vérifier le rôle et ouvrir la bonne scène
                    switch (role) {
                        case "RH":
                            openGestionEntretienForRH();
                            break;
                        case "Candidat":
                            openGestionEntretienForCandidat();
                            break;
                        default:
                            showAlert("Erreur", "Rôle inconnu, accès non autorisé.");
                            return false;
                    }
                    return true;  // Connexion réussie
                } else {
                    showAlert("Erreur", "Email ou mot de passe incorrect !");
                    return false;  // Aucun utilisateur trouvé
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
                showAlert("Erreur", "Erreur de connexion à la base de données.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'utilisateur : " + e.getMessage());
            showAlert("Erreur", "Erreur de connexion à la base de données.");
        }
        return false;  // Connexion échouée
    }


    private void openGestionEntretienForRH() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionOffreEmploi.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Gestion des Entretiens (RH)");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page des offres pour RH.");
        }
    }

    private void openGestionEntretienForCandidat() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionEntretien.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Gestion des Entretiens (Candidat)");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page des entretiens pour Candidat.");
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
