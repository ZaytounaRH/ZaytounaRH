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
            // Load the FXML file and set the scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionUser.fxml"));
            Parent root = loader.load();

            // Create a scene with the loaded root
            Scene scene = new Scene(root);

            // Set the scene to the primaryStage
            primaryStage.setScene(scene);
            primaryStage.setTitle("---- Gestion Personne -----");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML: " + e.getMessage());
        }
    }
}
