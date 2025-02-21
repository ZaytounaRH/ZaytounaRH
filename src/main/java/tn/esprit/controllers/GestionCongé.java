package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Congé;
import tn.esprit.services.ServiceCongé;

import java.time.LocalDate;
import java.sql.Date;

public class GestionCongé {

    @FXML
    private DatePicker dpDateDebut; // DatePicker pour date début
    @FXML
    private DatePicker dpDateFin;   // DatePicker pour date fin
    @FXML
    private TextField tfMotif;       // TextField pour le motif
    @FXML
    private Label lbConges;          // Label pour afficher la liste des congés

    // Service pour la gestion des congés
    IService<Congé> serviceCongé = new ServiceCongé();

    /**
     * Ajouter un congé à la base de données
     */
    @FXML
    public void ajouterConge(ActionEvent actionEvent) {
        // Réinitialiser le message d'erreur ou de succès
        lbConges.setText("");  // Efface le texte du Label au début
        lbConges.setStyle("-fx-text-fill: black;");  // Remet la couleur par défaut

        // Vérification que tous les champs sont remplis
        if (dpDateDebut.getValue() == null || dpDateFin.getValue() == null || tfMotif.getText().isEmpty()) {
            lbConges.setText("Veuillez remplir tous les champs !");
            lbConges.setStyle("-fx-text-fill: red;");  // Afficher le message en rouge dans l'interface
            return;
        }

        LocalDate dateDebut = dpDateDebut.getValue();
        LocalDate dateFin = dpDateFin.getValue();

        // Vérification si la date de début est antérieure à la date de fin
        if (dateDebut.isAfter(dateFin)) {
            lbConges.setText("La date de début ne peut pas être après la date de fin !");
            lbConges.setStyle("-fx-text-fill: red;");
            return;
        }

        String motif = tfMotif.getText().trim();  // Supprimer les espaces avant et après
        if (motif.isEmpty()) {
            lbConges.setText("Le motif ne peut pas être vide !");
            lbConges.setStyle("-fx-text-fill: red;");
            return;
        }

        // Création de l'objet Congé
        Congé conge = new Congé(Date.valueOf(dateDebut), Date.valueOf(dateFin), new Date(System.currentTimeMillis()), motif);

        // Ajout du congé via le service
        serviceCongé.add(conge);

        // Affichage du message de succès
        lbConges.setText("Congé ajouté avec succès !");
        lbConges.setStyle("-fx-text-fill: green;");  // Afficher le message en vert
        afficherConges();
    }



    /**
     * Afficher tous les congés enregistrés
     */
    @FXML
    public void afficherConges() {
        lbConges.setText(serviceCongé.getAll().toString());
    }
}
