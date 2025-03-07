package tn.esprit.test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.controllers.GestionCertification;
import tn.esprit.controllers.GestionFormation;
import tn.esprit.controllers.Login;
import tn.esprit.controllers.MainController;

import java.io.IOException;
import java.net.URL;


public class MainFX extends Application {
    private BorderPane rootLayout;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
            Parent root = loader.load();

            // Vérification si le fichier est bien chargé
            if (root == null) {
                System.out.println("Erreur lors du chargement du fichier FXML.");
                return;
            }

            // Créer la scène et l'afficher
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestion des Offres d'Emploi");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML: " + e.getMessage());
            e.printStackTrace();  // Afficher la pile d'erreurs complète
        }
    }


    }








