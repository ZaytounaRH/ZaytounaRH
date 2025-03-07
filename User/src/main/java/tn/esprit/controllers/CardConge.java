package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.models.Conge;

public class CardConge {

    @FXML
    private Label lblMotif;

    @FXML
    private Label lblDateDebut;

    @FXML
    private Label lblDateFin;

    @FXML
    private Label lblStatut;

    public void setConge(Conge conge) {
        lblMotif.setText("Motif: " + conge.getMotif());
        lblDateDebut.setText("Début: " + conge.getDateDebut());
        lblDateFin.setText("Fin: " + conge.getDateFin());
        lblStatut.setText("Statut: " + conge.getStatut());

        // 🎨 Couleur du statut
        if (conge.getStatut().toString().equalsIgnoreCase("ACCEPTE")) {
            lblStatut.setStyle("-fx-text-fill: green;");
        } else if (conge.getStatut().toString().equalsIgnoreCase("REFUSE")) {
            lblStatut.setStyle("-fx-text-fill: red;");
        } else {
            lblStatut.setStyle("-fx-text-fill: orange;");
        }
    }
}
