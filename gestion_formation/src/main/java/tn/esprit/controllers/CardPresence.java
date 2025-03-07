package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.models.Presence;

public class CardPresence {
    @FXML
    private Label lblDate;

    @FXML
    private Label lblHeureArrive;  // 🔥 Vérifier que ce champ existe bien

    @FXML
    private Label lblHeureDepart;

    private Presence currentPresence;

    public void setPresence(Presence presence) {
        this.currentPresence = presence;

        // ✅ Vérification que les labels existent avant de les utiliser
        if (lblDate != null && presence.getDate() != null) {
            lblDate.setText(presence.getDate().toString());
        }

        if (lblHeureArrive != null && presence.getHeureArrive() != null) {
            lblHeureArrive.setText(presence.getHeureArrive().toString());
        }

        if (lblHeureDepart != null && presence.getHeureDepart() != null) {
            lblHeureDepart.setText(presence.getHeureDepart().toString());
        }
    }
}
