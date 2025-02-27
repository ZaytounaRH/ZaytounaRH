package tn.esprit.getionfinanciere.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import tn.esprit.MainFX;
import tn.esprit.getionfinanciere.models.Commande;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.repository.CommandeRepository;
import tn.esprit.getionfinanciere.repository.FournisseurRepository;

import java.io.IOException;

import static tn.esprit.getionfinanciere.utils.Constants.SAISIE_INVALIDE;

public class GestionCommande {

    @FXML
    private DatePicker dpDateCommande;
    @FXML
    private TextField tfquantite;
    @FXML
    private TextField dpquantite;
    @FXML
    private TextField tfDescription;
    @FXML
    private Button ajouterButton;
    @FXML
    private Button afficherButton;
    @FXML
    private Button retourButton;
    @FXML
    private ComboBox<Fournisseur> nomFournissuer;

    private final CommandeRepository serviceCommande = new CommandeRepository();
    private final FournisseurRepository serviceFournisseur = new FournisseurRepository();

    @FXML
    public void initialize() {
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
                    setText(item.getNomFournisseur());
                }
            }
        });
        tfquantite.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Accepte uniquement les chiffres
                tfquantite.setText(oldValue);
            }
        });
    }

    @FXML
    public void ajouterCommande(ActionEvent actionEvent) {
        if (dpDateCommande.getValue() == null || tfquantite.getText().isEmpty() || tfDescription.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Formulaire incomplet", "Tous les champs doivent être remplis.");
            return;
        }
        String dateCommande = dpDateCommande.getValue().toString();
        int quantite;
        try {
            quantite = Integer.parseInt(tfquantite.getText());
            if (quantite <= 0) {
                showAlert(AlertType.ERROR, SAISIE_INVALIDE, "Le quantite doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, SAISIE_INVALIDE, "Le quantite doit être un nombre.");
            return;
        }
        String description = tfDescription.getText();
        int idFournisseur = nomFournissuer.valueProperty().getValue().getId();
        Commande commande = new Commande();
        commande.setDateCommande(dateCommande);
        commande.setQuantite(quantite);
        commande.setDescription(description);
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
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("home_view.fxml"));
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
        tfquantite.clear();
        tfDescription.clear();
    }
}
