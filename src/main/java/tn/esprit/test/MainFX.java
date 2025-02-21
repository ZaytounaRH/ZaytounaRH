package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Chargement du FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionOffreEmploi.fxml"));
            Parent root = loader.load();

            // Création de la scène avec le CSS appliqué
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            // Configuration de la fenêtre
            primaryStage.setScene(scene);
            primaryStage.setTitle("---- Gestion Offre Emploi -----");
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(); // Meilleur affichage des erreurs
        }
    }
}
