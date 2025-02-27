package tn.esprit.getionfinanciere.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.MainFX;
import tn.esprit.getionfinanciere.models.Commande;
import tn.esprit.getionfinanciere.repository.CommandeRepository;
import static tn.esprit.getionfinanciere.utils.Utils.showAlert;

public class CommandeListController {

  @FXML
  private FlowPane flowPaneCommande;
  @FXML
  private TextField searchField;

  private final CommandeRepository serviceCommande = new CommandeRepository();
  private ObservableList<Commande> commandeList;

  public void initialize() {
    commandeList = FXCollections.observableArrayList(serviceCommande.getAll());
    afficherCommandes(commandeList);

    searchField.textProperty().addListener((observable, oldValue, newValue) -> searchCommande(newValue));
  }

  private void afficherCommandes(ObservableList<Commande> commandes) {
    flowPaneCommande.getChildren().clear();

    for (Commande commande : commandes) {
      VBox card = new VBox();
      card.getStyleClass().add("card");

      Label dateCommande = new Label("Date: " + commande.getDateCommande());
      Label quantite = new Label("Quantité: " + commande.getQuantite() );
      Label description = new Label("Description: " + commande.getDescription() );
      Label statutCommande = new Label("Statut: " + commande.getStatutCommande());

      Button btnSupprimer = new Button("Supprimer");
      btnSupprimer.getStyleClass().add("delete-button");
      btnSupprimer.setOnAction(e -> deleteCommande(commande));



      card.getChildren().addAll(dateCommande, quantite, description , statutCommande, btnSupprimer);
      flowPaneCommande.getChildren().add(card);
    }
  }

  private void deleteCommande(Commande commande) {
    serviceCommande.delete(commande);
    commandeList.remove(commande);
    afficherCommandes(commandeList);
    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "La commande a été supprimée.");
  }



  public void goBack() {
    try {
      FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("gestion_commande.fxml"));
      Parent root = loader.load();
      Stage stage = (Stage) flowPaneCommande.getScene().getWindow();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void searchCommande(String searchText) {
    ObservableList<Commande> filteredList = FXCollections.observableArrayList();
    for (Commande commande : commandeList) {
      if (commande.getStatutCommande().toLowerCase().contains(searchText.toLowerCase())) {
        filteredList.add(commande);
      }
    }
    afficherCommandes(filteredList);
  }
}
