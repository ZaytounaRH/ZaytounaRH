package tn.esprit.controllers;
import javafx.fxml.FXML;
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

public class ModifierPresence {
    @FXML
    private DatePicker dateField;

    @FXML
    private TextField heureArriveField;

    @FXML
    private TextField heureDepartField;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnAnnuler;

    private final ServicePresence servicePresence = new ServicePresence();
    private Presence currentPresence;

    public void setPresence(Presence presence) {
        this.currentPresence = presence;

        // ✅ Conversion sécurisée `java.sql.Date` → `LocalDate`
        dateField.setValue(convertToLocalDate(presence.getDate()));

        // ✅ Conversion sécurisée `java.sql.Time` → `LocalTime` (et format HH:mm)
        heureArriveField.setText(convertToLocalTime(presence.getHeureArrive()));
        heureDepartField.setText(convertToLocalTime(presence.getHeureDepart()));
    }

    @FXML
    public void initialize() {
        btnModifier.setOnAction(event -> modifierPresence());

        btnAnnuler.setOnAction(event -> {
            Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            stage.close();
        });
    }

    private void modifierPresence() {
        if (currentPresence == null) {
            System.out.println("❌ Aucune présence sélectionnée !");
            return;
        }

        LocalDate localDate = dateField.getValue();
        String heureArrive = heureArriveField.getText().trim();
        String heureDepart = heureDepartField.getText().trim();

        if (localDate == null || heureArrive.isEmpty() || heureDepart.isEmpty()) {
            System.out.println("❌ Veuillez remplir tous les champs !");
            return;
        }

        // ✅ Convertir les valeurs saisies avant la sauvegarde
        currentPresence.setDate(Date.valueOf(localDate));
        currentPresence.setHeureArrive(Time.valueOf(LocalTime.parse(heureArrive)));
        currentPresence.setHeureDepart(Time.valueOf(LocalTime.parse(heureDepart)));

        // ✅ Mise à jour de la présence
        servicePresence.update(currentPresence);
        System.out.println("✅ Présence modifiée avec succès !");

        // ✅ Fermer la fenêtre après modification
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }

    // ✅ Conversion de `java.sql.Date` en `LocalDate` (gère les `null`)
    private LocalDate convertToLocalDate(Date date) {
        return (date == null) ? null : date.toLocalDate();
    }

    // ✅ Conversion de `java.sql.Time` en `String` format HH:mm
    private String convertToLocalTime(Time time) {
        return (time == null) ? "" : time.toLocalTime().toString();
    }
}

