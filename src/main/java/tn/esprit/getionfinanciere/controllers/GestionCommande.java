package tn.esprit.getionfinanciere.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import tn.esprit.MainFX;
import tn.esprit.getionfinanciere.models.Commande;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.services.ServiceCommande;
import tn.esprit.getionfinanciere.services.ServiceFournisseur;

import java.io.IOException;

public class GestionCommande {

    @FXML
    private DatePicker dpDateCommande;
    @FXML
    private TextField tfMontantTotal;
    @FXML
    private ComboBox<String> cbStatutCommande;
    @FXML
    private Button ajouterButton;
    @FXML
    private Button afficherButton;
    @FXML
    private Button retourButton;
    @FXML
    private ComboBox<Fournisseur> nomFournissuer;

    private final ServiceCommande serviceCommande = new ServiceCommande();

    private final ServiceFournisseur serviceFournisseur = new ServiceFournisseur();
    @FXML
    public void initialize() {
        cbStatutCommande.getItems().setAll("En attente", "Validée", "Annulée");
        nomFournissuer.getItems().setAll(serviceFournisseur.getAll());
        nomFournissuer.setCellFactory(param -> new ListCell<Fournisseur>() {
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

        nomFournissuer.setButtonCell(new ListCell<Fournisseur>() {
            @Override
            protected void updateItem(Fournisseur item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomFournisseur());  // Affichez uniquement le nom du fournisseur dans le ComboBox
                }
            }
        });
    }

    @FXML
    public void ajouterCommande(ActionEvent actionEvent) {
        if (dpDateCommande.getValue() == null || tfMontantTotal.getText().isEmpty() || cbStatutCommande.getValue() == null) {
            showAlert(AlertType.ERROR, "Formulaire incomplet", "Tous les champs doivent être remplis.");
            return;
        }
        String dateCommande = dpDateCommande.getValue().toString();
        double montant;
        try {
            montant = Double.parseDouble(tfMontantTotal.getText());
            if (montant <= 0) {
                showAlert(AlertType.ERROR, "Saisie invalide", "Le montant doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Saisie invalide", "Le montant doit être un nombre.");
            return;
        }
        String statutCommande = cbStatutCommande.getValue();
        int idFournisseur = nomFournissuer.valueProperty().getValue().getId();
        Commande commande = new Commande();
        commande.setDateCommande(dateCommande);
        commande.setMontantTotal(montant);
        commande.setStatutCommande(statutCommande);
        commande.setIdFournisseur(idFournisseur);
        commande.setIdResponsable(1);

        serviceCommande.add(commande);
        System.out.println("Commande ajoutée : " + commande);

        showAlert(AlertType.INFORMATION, "Commande ajoutée", "La commande a été ajoutée avec succès.");
        clearFields();
    }

    @FXML
    public void afficherCommandes(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("commande_list_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) afficherButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void retour(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("home_view.fxml")); // Remplacez "home_view.fxml" par le chemin de votre vue d'accueil
            Parent root = loader.load();
            Stage stage = (Stage) retourButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        dpDateCommande.setValue(null);
        tfMontantTotal.clear();
        cbStatutCommande.setValue(null);
    }
}