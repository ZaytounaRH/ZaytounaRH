package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;

public class GestionFormation {
    @FXML
    private TextField tfNomFormation;
    @FXML
    private TextField tfDescriptionFormation;
    @FXML
    private TextField tfDureeFormation;
    IService<Formation> sf = new ServiceFormation();
    @FXML
    private Label lbFormations;

    @FXML
    public void ajouterFormation(ActionEvent event) {
        Formation formation = new Formation();
        formation.setNomFormation(tfNomFormation.getText());
        formation.setDescriptionFormation(tfDescriptionFormation.getText());
        formation.setDureeFormation(tfDureeFormation.getText());
        sf.add(formation);
    }
    @FXML
    public void afficherFormation(ActionEvent event) {
        lbFormations.setText(sf.getAll().toString());
    }


}
