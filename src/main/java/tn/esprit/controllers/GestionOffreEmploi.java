package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.interfaces.Iservice;
import tn.esprit.models.OffreEmploi;
import tn.esprit.services.ServiceOffreEmploi;

import java.util.List;

public class GestionOffreEmploi {
    @FXML
    private TextField tfTitre;
    @FXML
    private TextField tfDescription;
    @FXML
    private TextField tfSalaire;

    Iservice<OffreEmploi> serviceOffre = new ServiceOffreEmploi();

    @FXML
    private Label lbOffres;

    @FXML
    public void ajouterOffre(ActionEvent actionEvent) {
        OffreEmploi offre = new OffreEmploi();
        offre.setTitreOffre(tfTitre.getText());
        offre.setDescription(tfDescription.getText());
        offre.setSalaire(Double.parseDouble(tfSalaire.getText()));

        serviceOffre.add(offre);
    }

    @FXML
    public void afficherOffres(ActionEvent actionEvent) {
        List<OffreEmploi> offresList = serviceOffre.getAll();
        StringBuilder sb = new StringBuilder();
        for (OffreEmploi offre : offresList) {
            sb.append("Titre: ").append(offre.getTitreOffre()).append("\n")
                    .append("Description: ").append(offre.getDescription()).append("\n")
                    .append("Salaire: ").append(offre.getSalaire()).append("\n\n");
        }
        lbOffres.setText(sb.toString());
    }
}
