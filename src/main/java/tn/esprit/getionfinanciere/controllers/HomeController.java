package tn.esprit.getionfinanciere.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tn.esprit.MainFX;


public class HomeController {
    @FXML
    private Button hoFournisseur;
    @FXML
    private Button hoCommande;
    @FXML
    private Button hoBudget;
    @FXML
    private Button hoDepence;
    @FXML
    public void openGF(ActionEvent actionEvent) {


        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("gestion_fournisseur.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) hoFournisseur.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openGC(ActionEvent actionEvent) {


        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("gestion_commande.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) hoCommande.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openGB(ActionEvent actionEvent) {
    }

    public void openGD(ActionEvent actionEvent) {
    }
}
