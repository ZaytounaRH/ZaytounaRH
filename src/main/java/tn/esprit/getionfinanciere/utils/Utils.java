package tn.esprit.getionfinanciere.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import tn.esprit.MainFX;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

  private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

  private Utils() {
  }

  public static void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void actionButton(String name, Button buttonScreen) {
    try {
      FXMLLoader loader = new FXMLLoader(MainFX.class.getResource(name));
      Parent root = loader.load();
      Stage stage = (Stage) buttonScreen.getScene().getWindow();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error occurred while loading the scene", e);
    }
  }

  public static void actionButton(String name, TableView<?> tableScreen) {
    try {
      FXMLLoader loader = new FXMLLoader(MainFX.class.getResource(name));
      Parent root = loader.load();
      Stage stage = (Stage) tableScreen.getScene().getWindow();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error occurred while loading the scene", e);
    }
  }

  public static void actionButton(String name, FlowPane flowPane) {
    try {
      FXMLLoader loader = new FXMLLoader(MainFX.class.getResource(name));
      Parent root = loader.load();
      Stage stage = (Stage) flowPane.getScene().getWindow();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error occurred while loading the scene", e);
    }
  }
}
