package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class HomePage {

    @FXML
    private void afficherConges() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeConges.fxml"));
            AnchorPane listeCongesPane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des Congés");
            stage.setScene(new Scene(listeCongesPane));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'ouverture de la liste des congés : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherPresences() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListePresences.fxml"));
            AnchorPane listePresencesPane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des Présences");
            stage.setScene(new Scene(listePresencesPane));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'ouverture de la liste des présences : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
