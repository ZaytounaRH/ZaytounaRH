package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.models.OffreEmploi;
import tn.esprit.services.ServiceOffreEmploi;

public class SupprimerOffreController {

    @FXML
    private Label labelTitre;
    @FXML
    private Label labelDescription;

    private OffreEmploi offre;
    private ServiceOffreEmploi serviceOffre = new ServiceOffreEmploi();

    public void initData(OffreEmploi offre) {
        this.offre = offre;
        labelTitre.setText("Titre: " + offre.getTitreOffre());
        labelDescription.setText("Description: " + offre.getDescription());
    }

    @FXML
    private void confirmerSuppression(ActionEvent event) {
        try {
            serviceOffre.remove(offre.getIdOffre()); // Supprimer l'offre en appelant le service
            Stage stage = (Stage) labelTitre.getScene().getWindow();
            stage.close(); // Fermer la fenêtre après suppression
        } catch (Exception e) {
            e.printStackTrace();
            // Ajouter un message d'erreur ici si nécessaire
        }
    }

    @FXML
    private void annulerSuppression(ActionEvent event) {
        Stage stage = (Stage) labelTitre.getScene().getWindow();
        stage.close(); // Fermer la fenêtre sans effectuer la suppression
    }
}
