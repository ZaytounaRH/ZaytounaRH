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
    public void start(Stage primaryStage)  {
    this.primaryStage=primaryStage;
        this.primaryStage.setTitle("Gestion des Formations et Certifications");

        initRootLayout();
        //showFormations();

        }
    public void initRootLayout() {
        try {
            // Charger le layout principal (BorderPane)
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main-view.fxml"));
            rootLayout = loader.load();
            MainController controller = loader.getController();
            controller.setMainApp(this);
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFormations() {
        if (rootLayout != null) {
        try {
            // Charger la vue des formations
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("formation_view.fxml"));
            ScrollPane formationsView = loader.load();
            GestionFormation formation = loader.getController();
            rootLayout.setCenter(formationsView);



        } catch (IOException e) {
            e.printStackTrace();
        }
        } else {
            System.err.println("rootLayout n'est pas initialisé !");
        }
    }
    public void showCertifications() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("certification_view.fxml"));
            FlowPane certificationsView = loader.load();

            // Mettre à jour la vue dans le centre du BorderPane
            rootLayout.setCenter(certificationsView);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    }


/*
package tn.esprit.test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;




public class MainFX extends Application {
    public static void main(String[] args) {

        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main-view.fxml"));
        try{
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("---- Gestion Personne -----");
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        }



    }
 */