package tn.esprit.getionfinanciere.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.models.Produit;
import tn.esprit.getionfinanciere.repository.FournisseurRepository;
import tn.esprit.getionfinanciere.repository.ProduitRepository;
import tn.esprit.getionfinanciere.utils.Utils;

import static tn.esprit.getionfinanciere.utils.Utils.actionButton;

public class GestionProduit {

  @FXML
  private TextField tfProduit;
  @FXML
  private TextField tfPrix;
  @FXML
  private ComboBox<Fournisseur> nomFournisseur;
  @FXML
  private Button afficherButton;
  @FXML
  private Button retourButton;

  private final ProduitRepository serviceProduit = new ProduitRepository();
  private final FournisseurRepository fournisseurRepository = new FournisseurRepository();

  @FXML
  public void initialize() {
    nomFournisseur.getItems().setAll(fournisseurRepository.getAll());
    nomFournisseur.setCellFactory(param -> new ListCell<Fournisseur>() {
      @Override
      protected void updateItem(Fournisseur item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item.getNomFournisseur());
        }
      }
    });

    nomFournisseur.setButtonCell(new ListCell<Fournisseur>() {
      @Override
      protected void updateItem(Fournisseur item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item.getNomFournisseur());
        }
      }
    });
  }

  @FXML
  public void ajouterProduit(ActionEvent actionEvent) {
    if (tfProduit.getText().isEmpty() || tfPrix.getText().isEmpty() || nomFournisseur.getValue() == null) {
      showAlert(Alert.AlertType.ERROR, "Formulaire incomplet", "Tous les champs doivent être remplis.");
      return;
    }

    String produitName = tfProduit.getText();
    double prix;
    try {
      prix = Double.parseDouble(tfPrix.getText());
      if (prix <= 0) {
        showAlert(Alert.AlertType.ERROR, "Saisie invalide", "Le prix doit être un nombre positif.");
        return;
      }
    } catch (NumberFormatException e) {
      showAlert(Alert.AlertType.ERROR, "Saisie invalide", "Le prix doit être un nombre.");
      return;
    }

    int idFournisseur = nomFournisseur.getValue().getId();
    String fournisseurName = nomFournisseur.getValue().getNomFournisseur();
    Produit produit = new Produit();
    produit.setProduitName(produitName);
    produit.setPrix(prix);
    produit.setIdFournisseur(idFournisseur);
    produit.setNomFournisseur(fournisseurName);
    serviceProduit.add(produit);
    System.out.println("Produit ajouté : " + produit);

    showAlert(Alert.AlertType.INFORMATION, "Produit ajouté", "Le produit a été ajouté avec succès.");
    clearFields();
  }

  @FXML
  public void afficherProduits(ActionEvent actionEvent) {
    actionButton("produit_list_view.fxml", afficherButton);
  }


  @FXML
  public void retour(ActionEvent actionEvent) {
    Utils.actionButton("home_view.fxml", retourButton);
  }

  private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void clearFields() {
    tfProduit.clear();
    tfPrix.clear();
    nomFournisseur.getSelectionModel().clearSelection();
  }

}
