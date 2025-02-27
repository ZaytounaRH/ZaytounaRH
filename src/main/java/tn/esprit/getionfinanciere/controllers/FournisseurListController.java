package tn.esprit.getionfinanciere.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.models.enums.TypeService;
import tn.esprit.getionfinanciere.services.ServiceFournisseur;
import tn.esprit.getionfinanciere.utils.Utils;

import java.util.Optional;

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

    Button editButton = new Button("Modifier");
    editButton.setOnAction(event -> editFournisseur(fournisseur));

    Button deleteButton = new Button("Supprimer");
    deleteButton.setOnAction(event -> deleteFournisseur(fournisseur));

    card.getChildren().addAll(name, address, contact, type, editButton, deleteButton);
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

  private void editFournisseur(Fournisseur fournisseur) {
    Dialog<Fournisseur> dialog = new Dialog<>();
    dialog.setTitle("Modifier Fournisseur");
    dialog.setHeaderText("Modifier les informations du fournisseur");

    // Création des champs de texte
    TextField tfNomFournisseur = new TextField(fournisseur.getNomFournisseur());
    TextField tfAdresse = new TextField(fournisseur.getAdresse());
    TextField tfContact = new TextField(fournisseur.getContact());

    // Création du choix pour le type de service
    ChoiceBox<TypeService> cbTypeService = new ChoiceBox<>();
    cbTypeService.getItems().addAll(TypeService.values());
    cbTypeService.setValue(fournisseur.getTypeService());

    // Ajout des champs au contenu de la boîte de dialogue
    VBox content = new VBox(10);
    content.getChildren().addAll(
            new Text("Nom:"), tfNomFournisseur,
            new Text("Adresse:"), tfAdresse,
            new Text("Contact:"), tfContact,
            new Text("Type de Service:"), cbTypeService
    );
    dialog.getDialogPane().setContent(content);

    // Ajout des boutons OK et Annuler
    ButtonType saveButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

    Button saveBtn = (Button) dialog.getDialogPane().lookupButton(saveButton);
    saveBtn.addEventFilter(ActionEvent.ACTION, event -> {
      if (!validerChamps(tfNomFournisseur, tfAdresse, tfContact, cbTypeService)) {
        event.consume(); // Empêche la fermeture de la boîte de dialogue si la validation échoue
      }
    });

    dialog.setResultConverter(buttonType -> {
      if (buttonType == saveButton) {
        fournisseur.setNomFournisseur(tfNomFournisseur.getText());
        fournisseur.setAdresse(tfAdresse.getText());
        fournisseur.setContact(tfContact.getText());
        fournisseur.setTypeService(cbTypeService.getValue());
        return fournisseur;
      }
      return null;
    });

    Optional<Fournisseur> result = dialog.showAndWait();
    result.ifPresent(updatedFournisseur -> {
      serviceFournisseur.update(updatedFournisseur);
      displayFournisseurs(fournisseursList);
    });
  }

  // Méthode de validation des champs
  private boolean validerChamps(TextField tfNomFournisseur, TextField tfAdresse, TextField tfContact, ChoiceBox<TypeService> cbTypeService) {
    final String REGEX_NOM = "^[a-zA-Z ]+$";
    final String REGEX_ADRESS = "^[a-zA-Z0-9 ]+$";
    final String REGEX_NUMBER = "^[1-9][0-9]{7}$"; // 8 chiffres, ne commence pas par 0

    if (tfNomFournisseur.getText().isEmpty() || tfAdresse.getText().isEmpty() ||
            tfContact.getText().isEmpty() || cbTypeService.getValue() == null) {
      showAlert(AlertType.ERROR, "Formulaire incomplet", "Tous les champs doivent être remplis.");
      return false;
    }
    if (!tfNomFournisseur.getText().matches(REGEX_NOM)) {
      showAlert(AlertType.ERROR, "Saisie invalide", "Le nom du fournisseur ne doit contenir que des lettres.");
      return false;
    }
    if (!tfAdresse.getText().matches(REGEX_ADRESS)) {
      showAlert(AlertType.ERROR, "Saisie invalide", "L'adresse ne doit contenir que des lettres et des chiffres.");
      return false;
    }
    if (!tfContact.getText().matches(REGEX_NUMBER)) {
      showAlert(AlertType.ERROR, "Saisie invalide", "Le contact doit être composé de 8 chiffres et ne doit pas commencer par 0.");
      return false;
    }
    return true;
  }

  // Méthode pour afficher une alerte
  private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }


  @FXML
  public void goBack() {
    Utils.actionButton("gestion_fournisseur.fxml", flowPaneFournisseur);
  }
}
