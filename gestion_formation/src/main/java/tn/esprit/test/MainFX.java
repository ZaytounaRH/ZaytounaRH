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

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("formation_view.fxml"));
        try{
            Parent root = loader.load();
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());


            primaryStage.setScene(scene);
            primaryStage.setTitle("---- Gestion Formation-----");
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
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