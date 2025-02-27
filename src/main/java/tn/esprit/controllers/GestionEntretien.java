package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory;
import tn.esprit.models.Entretien;
import tn.esprit.services.ServiceEntretien;
import tn.esprit.models.Candidat;
import tn.esprit.models.OffreEmploi;

import java.time.LocalDate;
import java.time.LocalTime;

public class GestionEntretien {

    @FXML
    private DatePicker dateEntretienPicker;

    @FXML
    private Spinner<LocalTime> heureEntretienSpinner;

    @FXML
    private ComboBox<String> typeEntretienComboBox;

    @FXML
    private ComboBox<String> statutEntretienComboBox;

    @FXML
    private TextArea commentaireTextArea;

    @FXML
    private Button ajouterButton;

    private ServiceEntretien serviceEntretien;

    public GestionEntretien() {
        this.serviceEntretien = new ServiceEntretien();
    }

    @FXML
    private void initialize() {
        // Initialisation des ComboBox
        typeEntretienComboBox.setItems(FXCollections.observableArrayList("PRESENTIEL", "DISTANCIEL", "TELEPHONIQUE"));
        statutEntretienComboBox.setItems(FXCollections.observableArrayList("PLANIFIE", "EN_COURS", "TERMINE", "ANNULE"));

        // Configurer le Spinner pour l'heure de l'entretien
        SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
            @Override
            public void decrement(int steps) {
                LocalTime currentTime = heureEntretienSpinner.getValue();
                if (currentTime != null) {
                    // Décrémenter l'heure par 30 minutes (ou vous pouvez ajuster l'intervalle selon votre besoin)
                    LocalTime newTime = currentTime.minusMinutes(30 * steps);
                    setValue(newTime);
                }
            }

            @Override
            public void increment(int steps) {
                LocalTime currentTime = heureEntretienSpinner.getValue();
                if (currentTime != null) {
                    // Incrémenter l'heure par 30 minutes (ou vous pouvez ajuster l'intervalle selon votre besoin)
                    LocalTime newTime = currentTime.plusMinutes(30 * steps);
                    setValue(newTime);
                }
            }
        };

        // Définir une valeur de départ pour le spinner (par exemple, 9h00)
        valueFactory.setValue(LocalTime.of(9, 0));

        heureEntretienSpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void ajouterEntretien() {
        // Vérification des champs
        if (dateEntretienPicker.getValue() == null || heureEntretienSpinner.getValue() == null ||
                typeEntretienComboBox.getValue() == null || statutEntretienComboBox.getValue() == null) {
            // Afficher un message d'erreur si des champs sont vides
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        // Créer un objet Entretien
        Entretien entretien = new Entretien();
        entretien.setDateEntretien(dateEntretienPicker.getValue());
        entretien.setHeureEntretien(heureEntretienSpinner.getValue());
        entretien.setTypeEntretien(Entretien.TypeEntretien.valueOf(typeEntretienComboBox.getValue()));
        entretien.setStatut(Entretien.StatutEntretien.valueOf(statutEntretienComboBox.getValue()));
        entretien.setCommentaire(commentaireTextArea.getText());

        // Créer un candidat fictif pour la démonstration
        Candidat candidat = new Candidat();
        candidat.setCandidat_id(1); // Exemple d'ID fictif
        entretien.setCandidat(candidat);

        // Lier l'entretien à une offre d'emploi fictive
        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setIdOffre(1); // Exemple d'ID fictif
        entretien.setOffreEmploi(offreEmploi);

        // Ajouter l'entretien via le service
        serviceEntretien.add(entretien);

        // Afficher un message de succès
        showAlert("Succès", "Entretien ajouté avec succès.", Alert.AlertType.INFORMATION);
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
