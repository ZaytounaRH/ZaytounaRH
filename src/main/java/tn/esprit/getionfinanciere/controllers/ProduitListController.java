package tn.esprit.getionfinanciere.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import tn.esprit.getionfinanciere.models.Commande;
import tn.esprit.getionfinanciere.models.Produit;
import tn.esprit.getionfinanciere.repository.ProduitRepository;
import tn.esprit.getionfinanciere.utils.Utils;

import static tn.esprit.getionfinanciere.utils.Utils.showAlert;


public class ProduitListController {
  @FXML
  public FlowPane flowPaneProduit;
  @FXML
  private ObservableList<Produit> produitsList;
  @FXML
  private TextField searchField;

  private final ProduitRepository produitRepository = new ProduitRepository();

  public void initialize() {
    produitsList = FXCollections.observableArrayList(produitRepository.getAll());
    afficherProduits(produitsList);
    searchField.textProperty().addListener((observable, oldValue, newValue) -> searchProduit(newValue));
  }


  private void afficherProduits(ObservableList<Produit> produits) {
    flowPaneProduit.getChildren().clear();
    for (Produit produit : produits) {
      VBox card = new VBox();
      card.getStyleClass().add("card");
      Label nomProduit = new Label("Nom de Produit: " + produit.getProduitName());
      Label nomFournisseur = new Label("Nom de Fournissuer: " + produit.getNomFournisseur() );
      Label prixProduit = new Label("Prix: " + produit.getPrix());
      Button btnSupprimer = new Button("Supprimer");
      btnSupprimer.getStyleClass().add("delete-button");
      btnSupprimer.setOnAction(e -> deleteProduit(produit));
      card.getChildren().addAll(nomProduit, nomFournisseur, prixProduit, btnSupprimer);
      flowPaneProduit.getChildren().add(card);
    }
  }

  private void deleteProduit(Produit produit) {
    produitRepository.delete(produit);
    produitsList.remove(produit);
    afficherProduits(produitsList);
    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "La commande a été supprimée.");
  }

  public void goBack() {
    Utils.actionButton("gestion_produit.fxml", flowPaneProduit);
  }

  private void searchProduit(String searchText) {
    ObservableList<Produit> filteredList = FXCollections.observableArrayList();
    for (Produit produit : produitsList) {
      if (produit.getNomFournisseur().toLowerCase().contains(searchText.toLowerCase())) {
        filteredList.add(produit);
      }
    }
    afficherProduits(filteredList);
  }
}
