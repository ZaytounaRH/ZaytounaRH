package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {
    @FXML
    private Button btnFormation;
    @FXML
    private Button btnCertification;

    // Méthode pour naviguer vers la vue Formation
    @FXML
    private void goToFormation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("formation_view.fxml"));;
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnFormation.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour naviguer vers la vue Certification
    @FXML
    private void goToCertification(ActionEvent event) {
        try {
            // Chargement du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("certification_view.fxml"));
            Parent root = loader.load();

            // Récupération de la scène et du stage actuel
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnCertification.getScene().getWindow();

            // Changement de scène
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
