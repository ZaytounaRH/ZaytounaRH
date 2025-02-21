package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gestionConge.fxml"));
            AnchorPane root = loader.load();
            primaryStage.setTitle("Gestion des Congés");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false); // Empêcher le redimensionnement si ce n'est pas nécessaire
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erreur de chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
