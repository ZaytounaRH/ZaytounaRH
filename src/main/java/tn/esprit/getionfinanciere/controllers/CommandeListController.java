package tn.esprit.getionfinanciere.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.MainFX;
import tn.esprit.getionfinanciere.models.Commande;
import tn.esprit.getionfinanciere.services.ServiceCommande;


public class CommandeListController {
  @FXML
  public TableView<Commande> tableCommande;
  @FXML
  public TableColumn<Commande, String> colDateCommande;
  @FXML
  public TableColumn<Commande, Double> colMontantTotal;
  @FXML
  public TableColumn<Commande, String> colStatusCommande;

  private final ServiceCommande serviceCommande = new ServiceCommande();

  public void initialize() {
    colDateCommande.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
    colMontantTotal.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
    colStatusCommande.setCellValueFactory(new PropertyValueFactory<>("statutCommande"));
    tableCommande.getItems().setAll(serviceCommande.getAll());
  }

  public void deleteCommande(ActionEvent actionEvent) {
    Commande selectedCommande = tableCommande.getSelectionModel().getSelectedItem();
    if (selectedCommande != null) {
      serviceCommande.delete(selectedCommande);
      tableCommande.getItems().remove(selectedCommande);
      showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le fournisseur a été supprimé.");
    } else {
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


  public void goBack() {
    try {
      FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("gestion_commande.fxml"));
      Parent root = loader.load();
      Stage stage = (Stage) tableCommande.getScene().getWindow();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
