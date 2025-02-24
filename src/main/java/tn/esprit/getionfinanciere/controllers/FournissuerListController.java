package tn.esprit.getionfinanciere.controllers;

import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import tn.esprit.MainFX;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.services.ServiceFournisseur;

public class FournissuerListController {
  @FXML
  private TableView<Fournisseur> tableFournisseurs;
  @FXML
  private TableColumn<Fournisseur, String> colNomFournisseur;
  @FXML
  private TableColumn<Fournisseur, String> colAdresse;
  @FXML
  private TableColumn<Fournisseur, String> colContact;
  @FXML
  private TableColumn<Fournisseur, String> colTypeService;

  private final ServiceFournisseur serviceFournisseur = new ServiceFournisseur();

  public void initialize() {
    colNomFournisseur.setCellValueFactory(new PropertyValueFactory<>("nomFournisseur"));
    colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
    colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
    colTypeService.setCellValueFactory(new PropertyValueFactory<>("typeService"));
    tableFournisseurs.getItems().setAll(serviceFournisseur.getAll());
  }


  @FXML
  public void deleteFournisseur() {
    // Get the selected supplier from the TableView
    Fournisseur selectedFournisseur = tableFournisseurs.getSelectionModel().getSelectedItem();

    if (selectedFournisseur != null) {
      // Remove the selected supplier from the service
      serviceFournisseur.delete(selectedFournisseur);

      // Refresh the TableView
      tableFournisseurs.getItems().remove(selectedFournisseur);

      // Show success alert
      showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le fournisseur a été supprimé.");
    } else {
      // Show error alert if no supplier is selected
      showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un fournisseur à supprimer.");
    }
  }

  private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  // Back button handler to go back to the previous view
  @FXML
  public void goBack() {
    try {
      FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("gestion_fournisseur.fxml"));
      Parent root = loader.load();
      Stage stage = (Stage) tableFournisseurs.getScene().getWindow();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
