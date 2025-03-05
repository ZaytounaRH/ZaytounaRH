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
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gestion des Formations et Certifications");

        initLoginView();  // Démarre la vue de login
    }

    public void initLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
            Parent root = loader.load();
            Login loginController = loader.getController();
            loginController.setMainApp(this);  // Passe mainApp au contrôleur de login

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            loginController.setOnLoginSuccess(() -> initRootLayout());  // Exemple d'usage

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main-view.fxml"));
            rootLayout = loader.load();  // Utilisation de Parent au lieu de BorderPane

            MainController controller = loader.getController();
            if (controller != null) {
                controller.setMainApp(this);  // Passe `mainApp` au contrôleur
                System.out.println("✅ MainController initialisé avec succès !");
            } else {
                System.err.println("❌ Impossible de récupérer MainController !");
            }
            if (rootLayout == null) {
                System.err.println("⚠️ rootLayout est toujours NULL après initRootLayout !");
            } else {
                System.out.println("✅ rootLayout est correctement initialisé !");
            }

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFormations() {
        if (rootLayout == null) {
            System.err.println("⚠️ rootLayout est NULL dans showFormations() !");
            return;
        }
        try {
            System.out.println("🔄 Chargement de formation_view.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("formation_view.fxml"));
            ScrollPane formationsView = loader.load();
            rootLayout.setCenter(formationsView);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de formation_view.fxml !");
            e.printStackTrace();
        }
    }

    public void showCertifications() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("certification_view.fxml"));
            FlowPane certificationsView = loader.load();
            rootLayout.setCenter(certificationsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showEmployers(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("employeFormationCertification.fxml"));
            FlowPane certificationsView = loader.load();
            rootLayout.setCenter(certificationsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
