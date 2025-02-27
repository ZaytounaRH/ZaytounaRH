package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import tn.esprit.models.Entretien;
import tn.esprit.services.ServiceEntretien;
import javafx.collections.FXCollections;
import java.time.LocalTime;

public class GestionEntretien {

    @FXML
    private DatePicker dateEntretienPicker;

    @FXML
    private Spinner<Integer> heureEntretienSpinner;

    @FXML
    private ComboBox<String> typeEntretienComboBox;

    @FXML
    private ComboBox<String> statutEntretienComboBox;

    @FXML
    private TextArea commentaireTextArea;

    @FXML
    private TextField idEntretienField;

    @FXML
    private Button ajouterButton;

    @FXML
    private Button updateButton;

    @FXML
    private TableView<Entretien> tableEntretien;

    private ServiceEntretien serviceEntretien;

    public GestionEntretien() {
        serviceEntretien = new ServiceEntretien();
    }

    @FXML
    public void initialize() {
        // Remplir les ComboBox avec les options de type et statut
        typeEntretienComboBox.setItems(FXCollections.observableArrayList("PRESENTIEL", "DISTANCIEL", "TELEPHONIQUE"));
        statutEntretienComboBox.setItems(FXCollections.observableArrayList("PLANIFIE", "EN_COURS", "TERMINE", "ANNULE"));

        // Initialisation du Spinner avec une plage d'heure
        heureEntretienSpinner.setValueFactory(new IntegerSpinnerValueFactory(0, 23, 9)); // Plage de 0 à 23 heures, valeur par défaut : 9 heures
    }

    // Méthode pour ajouter un entretien
    @FXML
    public void ajouterEntretien() {
        if (isInputValid()) {
            Entretien entretien = new Entretien();
            entretien.setDateEntretien(dateEntretienPicker.getValue());

            // Convertir l'heure (Integer) en LocalTime
            LocalTime heure = LocalTime.of(heureEntretienSpinner.getValue(), 0); // 0 pour les minutes par défaut
            entretien.setHeureEntretien(heure);

            // Convertir le String en TypeEntretien
            String typeEntretienString = typeEntretienComboBox.getValue();
            Entretien.TypeEntretien typeEntretien = Entretien.TypeEntretien.valueOf(typeEntretienString);
            entretien.setTypeEntretien(typeEntretien);

            // Convertir le String en StatutEntretien
            String statutEntretienString = statutEntretienComboBox.getValue();
            Entretien.StatutEntretien statutEntretien = Entretien.StatutEntretien.valueOf(statutEntretienString);
            entretien.setStatut(statutEntretien);

            entretien.setCommentaire(commentaireTextArea.getText());

            serviceEntretien.add(entretien);

            clearFields();
            showAlert("Succès", "L'entretien a été ajouté avec succès!", AlertType.INFORMATION);
        }
    }

    @FXML
    public void updateEntretien() {
        if (isInputValid()) {
            String idText = idEntretienField.getText();
            if (idText.isEmpty()) {
                showAlert("Erreur", "L'ID de l'entretien ne peut pas être vide!", AlertType.ERROR);
                return;
            }

            int idEntretien;
            try {
                idEntretien = Integer.parseInt(idText); // Assurez-vous que l'ID est un entier valide
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID de l'entretien doit être un nombre valide!", AlertType.ERROR);
                return;
            }

            Entretien entretien = new Entretien();
            entretien.setIdEntretien(idEntretien);
            entretien.setDateEntretien(dateEntretienPicker.getValue());

            // Convertir l'heure (Integer) en LocalTime
            LocalTime heure = LocalTime.of(heureEntretienSpinner.getValue(), 0); // minutes = 0 par défaut
            entretien.setHeureEntretien(heure);

            // Convertir le String en TypeEntretien
            String typeEntretienString = typeEntretienComboBox.getValue();
            Entretien.TypeEntretien typeEntretien = Entretien.TypeEntretien.valueOf(typeEntretienString);
            entretien.setTypeEntretien(typeEntretien);

            // Convertir le String en StatutEntretien
            String statutEntretienString = statutEntretienComboBox.getValue();
            Entretien.StatutEntretien statutEntretien = Entretien.StatutEntretien.valueOf(statutEntretienString);
            entretien.setStatut(statutEntretien);

            entretien.setCommentaire(commentaireTextArea.getText());

            // Appel du service pour mettre à jour l'entretien
            serviceEntretien.update(entretien);

            clearFields();
            showAlert("Succès", "L'entretien a été mis à jour avec succès!", AlertType.INFORMATION);
        }
    }

    // Vérifie si les champs sont valides
    private boolean isInputValid() {
        if (dateEntretienPicker.getValue() == null || heureEntretienSpinner.getValue() == null ||
                typeEntretienComboBox.getValue() == null || statutEntretienComboBox.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis!", AlertType.ERROR);
            return false;
        }
        return true;
    }

    // Affiche une alerte
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Réinitialise les champs
    private void clearFields() {
        dateEntretienPicker.setValue(null);
        heureEntretienSpinner.getValueFactory().setValue(9); // Remise à zéro de l'heure (9 heures par défaut)
        typeEntretienComboBox.setValue(null);
        statutEntretienComboBox.setValue(null);
        commentaireTextArea.clear();
        idEntretienField.clear();
    }
}
