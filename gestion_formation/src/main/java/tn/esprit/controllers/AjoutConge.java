package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Conge;
import tn.esprit.models.User;
import tn.esprit.services.ServiceConge;
import tn.esprit.utils.SessionManager;

import java.sql.Date;
import java.time.LocalDate;
public class AjoutConge {

    @FXML
    private TextField motifField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnAnnuler;

    private final ServiceConge serviceConge = new ServiceConge();

    @FXML
    public void initialize() {
        // ✅ Ajout du congé
        btnAjouter.setOnAction(event -> {
            System.out.println("Bouton Ajouter cliqué !");
            ajouterConge();
        });

        // ✅ Annuler et fermer la fenêtre
        btnAnnuler.setOnAction(event -> {
            Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            stage.close();
        });
    }

    private void ajouterConge() {
        System.out.println("➡️ Tentative d'ajout d'un congé...");

        String motif = motifField.getText();
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        if (motif.isEmpty() || dateDebut == null || dateFin == null) {
            System.out.println("❌ Veuillez remplir tous les champs !");
            return;
        }

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Aucun utilisateur connecté !");
            return;
        }

        Conge conge = new Conge();
        conge.setMotif(motif);
        conge.setDateDebut(Date.valueOf(dateDebut));
        conge.setDateFin(Date.valueOf(dateFin));

        serviceConge.add(conge);
        System.out.println("✅ Congé ajouté avec succès !");

        Stage stage = (Stage) btnAjouter.getScene().getWindow();
        stage.close();
    }
}
