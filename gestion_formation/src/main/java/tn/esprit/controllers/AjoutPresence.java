package tn.esprit.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Presence;
import tn.esprit.services.ServicePresence;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AjoutPresence {
    @FXML
    private DatePicker dateField;

    @FXML
    private TextField heureArriveField;

    @FXML
    private TextField heureDepartField;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnAnnuler;

    private final ServicePresence servicePresence = new ServicePresence();

    @FXML
    public void initialize() {
        btnAjouter.setOnAction(event -> handleAddPresence());
        btnAnnuler.setOnAction(event -> closeWindow());
    }

    @FXML
    public void handleAddPresence() {
        LocalDate localDate = dateField.getValue();
        String heureArrive = heureArriveField.getText().trim();
        String heureDepart = heureDepartField.getText().trim();

        if (localDate == null || heureArrive.isEmpty() || heureDepart.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        try {
            Date sqlDate = Date.valueOf(localDate);
            Time sqlHeureArrive = Time.valueOf(LocalTime.parse(heureArrive));
            Time sqlHeureDepart = Time.valueOf(LocalTime.parse(heureDepart));

            if (sqlHeureArrive.after(sqlHeureDepart)) {
                showAlert("Erreur", "L'heure d'arrivée doit être avant l'heure de départ.");
                return;
            }

            Presence presence = new Presence();
            presence.setDate(sqlDate);
            presence.setHeureArrive(sqlHeureArrive);
            presence.setHeureDepart(sqlHeureDepart);

            servicePresence.add(presence);

            showAlert("Succès", "La présence a été ajoutée avec succès.");
            closeWindow();

        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format d'heure invalide. Utilisez HH:mm (ex: 08:30).");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
