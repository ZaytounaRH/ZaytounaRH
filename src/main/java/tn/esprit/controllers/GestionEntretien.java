package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Entretien;
import tn.esprit.models.Entretien.TypeEntretien;
import tn.esprit.models.Entretien.StatutEntretien;
import tn.esprit.models.OffreEmploi;
import tn.esprit.services.ServiceEntretien;

import java.time.LocalTime;
import java.util.List;

public class GestionEntretien {

    @FXML
    private DatePicker dateEntretienPicker;
    @FXML

    private ListView<Entretien> listViewEntretiens;

    @FXML
    private Spinner<Integer> heureEntretienSpinner;

    @FXML
    private ComboBox<TypeEntretien> typeEntretienComboBox;

    @FXML
    private ComboBox<StatutEntretien> statutEntretienComboBox;

    @FXML
    private TextArea commentaireField;

    @FXML
    private TextField idOffreField;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private ServiceEntretien serviceEntretien = new ServiceEntretien();

    @FXML
    public void initialize() {
        if (listViewEntretiens == null) {
            System.out.println("La ListView est NULL !");
            return;
        }

        typeEntretienComboBox.setItems(FXCollections.observableArrayList(TypeEntretien.values()));
        statutEntretienComboBox.setItems(FXCollections.observableArrayList(StatutEntretien.values()));

        List<Entretien> entretiensList = serviceEntretien.getAll();
        listViewEntretiens.setItems(FXCollections.observableArrayList(entretiensList));

        listViewEntretiens.setCellFactory(param -> new ListCell<Entretien>() {
            @Override
            protected void updateItem(Entretien item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDateEntretien() + " - " + item.getTypeEntretien());
                }
            }
        });}


    // Méthode pour ajouter un entretien
    @FXML
    private void addEntretien() {
        if (dateEntretienPicker.getValue() == null || heureEntretienSpinner.getValue() == null ||
                typeEntretienComboBox.getValue() == null || statutEntretienComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Entretien entretien = new Entretien();
        entretien.setDateEntretien(dateEntretienPicker.getValue());
        LocalTime heure = LocalTime.of(heureEntretienSpinner.getValue(), 0);
        entretien.setHeureEntretien(heure);
        entretien.setTypeEntretien(typeEntretienComboBox.getValue());
        entretien.setStatut(statutEntretienComboBox.getValue());
        entretien.setCommentaire(commentaireField.getText());

        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setIdOffre(Integer.parseInt(idOffreField.getText()));
        entretien.setOffreEmploi(offreEmploi);

        serviceEntretien.add(entretien);
    }

    // Méthode pour mettre à jour un entretien
    @FXML
    private void updateEntretien() {
        if (dateEntretienPicker.getValue() == null || heureEntretienSpinner.getValue() == null ||
                typeEntretienComboBox.getValue() == null || statutEntretienComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Entretien entretien = new Entretien();
        entretien.setDateEntretien(dateEntretienPicker.getValue());
        LocalTime heure = LocalTime.of(heureEntretienSpinner.getValue(), 0);
        entretien.setHeureEntretien(heure);
        entretien.setTypeEntretien(typeEntretienComboBox.getValue());
        entretien.setStatut(statutEntretienComboBox.getValue());
        entretien.setCommentaire(commentaireField.getText());

        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setIdOffre(Integer.parseInt(idOffreField.getText()));
        entretien.setOffreEmploi(offreEmploi);

        serviceEntretien.update(entretien);
    }
    @FXML
    public void afficherEntretien(ActionEvent actionEvent) {
        // Récupérer la liste des entretiens depuis le service
        List<Entretien> entretiensList = serviceEntretien.getAll();

        // Effacer les éléments existants dans le ListView avant d'ajouter les nouveaux
        listViewEntretiens.getItems().clear();

        // Ajouter les entretiens à la ListView
        listViewEntretiens.getItems().addAll(entretiensList);  // Ajouter directement des objets Entretien
    }

    // Méthode pour supprimer un entretien
    @FXML
    private void deleteEntretien() {
        if (idOffreField.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un ID valide.");
            return;
        }

        int entretienId = Integer.parseInt(idOffreField.getText());
        serviceEntretien.remove(entretienId);
    }

    // Méthode pour afficher un message d'alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
