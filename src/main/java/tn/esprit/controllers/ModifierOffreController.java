package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.RH;
import tn.esprit.services.ServiceOffreEmploi;

import java.time.ZoneId;

public class ModifierOffreController {

    @FXML
    private TextField tfTitre;
    @FXML
    private TextField tfDescription;
    @FXML
    private TextField tfSalaire;
    @FXML
    private DatePicker dpDatePublication;
    @FXML
    private ComboBox<OffreEmploi.StatutOffre> cbStatut;
    @FXML
    private ComboBox<RH> cbResponsableRH;
    @FXML
    private Button btnSauvegarder;
    @FXML
    private Button btnAnnuler;

    private OffreEmploi offre;
    private ServiceOffreEmploi serviceOffre = new ServiceOffreEmploi();

    @FXML
    public void initialize(OffreEmploi offre) {
        this.offre = offre;

        // Initialiser les champs avec les valeurs de l'offre
        tfTitre.setText(offre.getTitreOffre());
        tfDescription.setText(offre.getDescription());
        tfSalaire.setText(String.valueOf(offre.getSalaire()));
        dpDatePublication.setValue(offre.getDatePublication().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        cbStatut.setValue(offre.getStatut());

        // Configurer le bouton "Sauvegarder"
        btnSauvegarder.setOnAction(this::sauvegarderModifications);

        // Configurer le bouton "Annuler"
        btnAnnuler.setOnAction(event -> annulerModifications());
    }

    @FXML
    private void sauvegarderModifications(ActionEvent event) {
        try {
            offre.setTitreOffre(tfTitre.getText());
            offre.setDescription(tfDescription.getText());
            offre.setSalaire(Double.parseDouble(tfSalaire.getText()));
            offre.setDatePublication(java.sql.Date.valueOf(dpDatePublication.getValue()));
            offre.setStatut(cbStatut.getValue());
            offre.setRh(cbResponsableRH.getValue());

            serviceOffre.update(offre);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre modifiée avec succès !");
            closeWindow();  // Fermer la fenêtre de modification
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un salaire valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la modification de l'offre.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annulerModifications() {
        closeWindow();  // Fermer sans sauvegarder
    }

    @FXML
    private void closeWindow() {
        // Fermer la fenêtre courante (celle de modification)
        btnSauvegarder.getScene().getWindow().hide();
    }

    @FXML
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
