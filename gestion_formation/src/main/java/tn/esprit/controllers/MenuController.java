package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
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
    public void showFormations() {

        try {
            System.out.println("🔄 Chargement de formation_view.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("formation_view.fxml"));
            ScrollPane formationsView = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des Formations");
            stage.setScene(new Scene(formationsView, 600, 400));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de formation_view.fxml !");
            e.printStackTrace();
        }
    }
    public void showCertifications() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("certification_view.fxml"));
            FlowPane certificationsView = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des Certifications");
            stage.setScene(new Scene(certificationsView, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showEmployers(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("employeFormationCertification.fxml"));
            FlowPane certificationsView = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Certifications et Formations Des Employers");
            stage.setScene(new Scene(certificationsView, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showConges(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ListeConges.fxml"));
           VBox Congeview = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Certifications et Formations Des Employers");
            stage.setScene(new Scene(Congeview, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showPresences(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ListePresence.fxml"));
            VBox presenceview = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Presence");
            stage.setScene(new Scene(presenceview, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
