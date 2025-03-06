package tn.esprit.getionfinanciere.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.services.ServiceFournisseur;
import tn.esprit.getionfinanciere.utils.Utils;

public class FournisseurListController {

  @FXML
  private FlowPane flowPaneFournisseur;
  @FXML
  private TextField searchField;

  private final ServiceFournisseur serviceFournisseur = new ServiceFournisseur();
  private ObservableList<Fournisseur> fournisseursList;

  public void initialize() {
    fournisseursList = FXCollections.observableArrayList(serviceFournisseur.getAll());
    displayFournisseurs(fournisseursList);
    searchField.textProperty().addListener((observable, oldValue, newValue) -> searchFournisseur(newValue));
  }

  private void displayFournisseurs(ObservableList<Fournisseur> list) {
    flowPaneFournisseur.getChildren().clear();
    for (Fournisseur fournisseur : list) {
      VBox card = createFournisseurCard(fournisseur);
      flowPaneFournisseur.getChildren().add(card);
    }
  }

  private VBox createFournisseurCard(Fournisseur fournisseur) {
    VBox card = new VBox(10);
    card.getStyleClass().add("card");


    Text name = new Text("Nom: " + fournisseur.getNomFournisseur());
    Text address = new Text("Adresse: " + fournisseur.getAdresse());
    Text contact = new Text("Contact: " + fournisseur.getContact());
    Text type = new Text("Service: " + fournisseur.getTypeService().name());

    Button deleteButton = new Button("Supprimer");
    deleteButton.setOnAction(event -> deleteFournisseur(fournisseur));

    card.getChildren().addAll(name, address, contact, type, deleteButton);
    return card;
  }

  private void searchFournisseur(String searchText) {
    ObservableList<Fournisseur> filteredList = FXCollections.observableArrayList();
    for (Fournisseur fournisseur : fournisseursList) {
      if (fournisseur.getNomFournisseur().toLowerCase().contains(searchText.toLowerCase())) {
        filteredList.add(fournisseur);
      }
    }
    displayFournisseurs(filteredList);
  }

  private void deleteFournisseur(Fournisseur fournisseur) {
    serviceFournisseur.delete(fournisseur);
    fournisseursList.remove(fournisseur);
    displayFournisseurs(fournisseursList);
  }

  @FXML
  public void goBack() {
    Utils.actionButton("gestion_fournisseur.fxml", flowPaneFournisseur);
  }
}
