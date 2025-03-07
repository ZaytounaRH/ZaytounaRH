package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Conge;
import tn.esprit.services.ServiceConge;
import javafx.scene.control.ComboBox;
import tn.esprit.models.Conge.Statut;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
public class ModifierConge {
    @FXML
    private TextField motifField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private ComboBox<String> statutComboBox;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnAnnuler;

    private final ServiceConge serviceConge = new ServiceConge();
    private Conge currentConge;

    public void setConge(Conge conge) {
        this.currentConge = conge;
        motifField.setText(conge.getMotif());

        statutComboBox.getItems().addAll("EN_ATTENTE", "ACCEPTE", "REFUSE");
        statutComboBox.setValue(conge.getStatut().toString());
        // ✅ Nouvelle conversion pour éviter l'erreur
        dateDebutPicker.setValue(convertToLocalDate(conge.getDateDebut()));
        dateFinPicker.setValue(convertToLocalDate(conge.getDateFin()));
    }

    @FXML
    public void initialize() {
        btnModifier.setOnAction(event -> modifierConge());

        btnAnnuler.setOnAction(event -> {
            Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            stage.close();
        });
    }

    private void modifierConge() {
        if (currentConge == null) {
            System.out.println("❌ Aucun congé sélectionné !");
            return;
        }

        String motif = motifField.getText();
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        String statutString = statutComboBox.getValue(); // Récupérer la valeur sélectionnée

        if (motif.isEmpty() || dateDebut == null || dateFin == null) {
            System.out.println("❌ Veuillez remplir tous les champs !");
            return;
        }

        // ✅ Convertir LocalDate en java.sql.Date avant la sauvegarde
        currentConge.setMotif(motif);
        currentConge.setDateDebut(Date.valueOf(dateDebut));
        currentConge.setDateFin(Date.valueOf(dateFin));
        currentConge.setStatut(Statut.valueOf(statutString)); // Convertir la chaîne en ENUM

        serviceConge.update(currentConge);
        System.out.println("✅ Congé modifié avec succès !");

        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }

    // ✅ Méthode pour convertir java.sql.Date en LocalDate
    private LocalDate convertToLocalDate(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

