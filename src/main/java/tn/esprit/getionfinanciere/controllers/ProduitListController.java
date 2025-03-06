package tn.esprit.getionfinanciere.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.getionfinanciere.models.Produit;
import tn.esprit.getionfinanciere.repository.ProduitRepository;
import tn.esprit.getionfinanciere.utils.Utils;


public class ProduitListController {
  @FXML
  public TableView<Produit> tableProduit;
  @FXML
  public TableColumn<Produit, String> colNomProduit;
  @FXML
  public TableColumn<Produit, Double> colPrix;
  @FXML
  public TableColumn<Produit, String> colNomFournisseur;
  @FXML
  private TextField searchField;

  private final ProduitRepository produitRepository = new ProduitRepository();
  private ObservableList<Produit> produitList;

  public void initialize() {
    colNomProduit.setCellValueFactory(new PropertyValueFactory<>("produitName"));
    colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
    colNomFournisseur.setCellValueFactory(new PropertyValueFactory<>("nomFournisseur"));
    produitList = FXCollections.observableArrayList(produitRepository.getAll());
    tableProduit.getItems().setAll(produitList);
    searchField.textProperty().addListener((observable, oldValue, newValue) -> searchProduit(newValue));
  }

  /*public void deleteCommande() {
    Commande selectedCommande = tableProduit.getSelectionModel().getSelectedItem();
    if (selectedCommande != null) {
      commandeRepository.delete(selectedCommande);
      tableProduit.getItems().remove(selectedCommande);
      showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le fournisseur a été supprimé.");
    } else {
      showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un fournisseur à supprimer.");
    }
  }*/

  public void goBack() {
    Utils.actionButton("gestion_produit.fxml", tableProduit);
  }

  private void searchProduit(String searchText) {
    ObservableList<Produit> filteredList = FXCollections.observableArrayList();
    for (Produit produit : produitList) {
      if (produit.getNomFournisseur().toLowerCase().contains(searchText.toLowerCase())) {
        filteredList.add(produit);
      }
    }
    tableProduit.setItems(filteredList);
  }
}
