package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RHDashboard {

    @FXML
    private Button buttonEmployee,buttonLogout;
    @FXML
    public void goEmployee() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEmployee.fxml"));
            AnchorPane gestionUserPage = loader.load();

            Stage stage = (Stage) buttonEmployee.getScene().getWindow();

            Scene scene = new Scene(gestionUserPage);
            stage.setScene(scene);

            stage.setTitle("Gestion Financiere");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void logout() {
        // Here you can add the logic to clear the user session or any other cleanup if necessary
        // For now, we just navigate to SignUp.fxml

        try {
            // Load SignUp.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            AnchorPane signUpPage = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) buttonLogout.getScene().getWindow();
            Scene scene = new Scene(signUpPage);
            stage.setScene(scene);
            stage.setTitle("Inscription");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page de connexion.");
        }
    }
    // Method to show an error alert
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
