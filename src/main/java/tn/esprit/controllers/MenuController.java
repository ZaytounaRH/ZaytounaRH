package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class MenuController {

    @FXML
    public void handleEntretienClick() {
        try {
            // Charger le fichier FXML de la page EntretienRH
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EntretienRH.fxml"));
            VBox root = loader.load();  // Remplacez AnchorPane par VBox ici
            Stage stage = new Stage();
            stage.setTitle("Gestion des entretiens");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleOffreClick() {
        try {
            // Charger le fichier FXML de la page GestionOffreEmploi
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionOffreEmploi.fxml"));
            AnchorPane root = loader.load();  // Remplacez StackPane par AnchorPane ici
            Stage stage = new Stage();
            stage.setTitle("Gestion des offres d'emploi");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
