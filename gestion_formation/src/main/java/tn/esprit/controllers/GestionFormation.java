package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
import tn.esprit.models.Formation;
import tn.esprit.models.Rh;
import tn.esprit.services.ServiceFormation;
import tn.esprit.models.Employe;

import java.util.List;

public class GestionFormation {
    @FXML
    private TextField tfNomFormation;
    @FXML
    private TextField tfDescriptionFormation;
    @FXML
    private TextField tfDureeFormation;
    IService<Formation> sf = new ServiceFormation();
    private ServiceFormation serviceFormation = new ServiceFormation();
    @FXML
    private Label lbFormations;
    @FXML
    private ComboBox<Employe> comboBoxEmploye;
    @FXML
    private ComboBox<Rh> comboBoxRH;

    @FXML
    private ComboBox<Certification> comboBoxCertification;

    public void chargerListes() {
        ServiceFormation serviceFormation = new ServiceFormation();

        // Charger la liste des employés
        List<Employe> employes = serviceFormation.getAllEmployes();
        comboBoxEmploye.getItems().addAll(employes);

        // Charger la liste des RH (depuis la table RH)
        List<Rh> rhList = serviceFormation.getAllRH();
        comboBoxRH.getItems().addAll(rhList);

        // Charger la liste des certifications
        List<Certification> certifications = serviceFormation.getAllCertifications();
        comboBoxCertification.getItems().addAll(certifications);
    }

    @FXML
    private void handleEmployeSelection() {
        Employe selectedEmploye = comboBoxEmploye.getSelectionModel().getSelectedItem();
        if (selectedEmploye != null) {
            System.out.println("Employé sélectionné : " + selectedEmploye.getNom());
        }
    }
@FXML
private void handleRhSelection() {
        Rh selectedRh = comboBoxRH.getSelectionModel().getSelectedItem();
        if (selectedRh != null) {
            System.out.println("Rh sélectionné : " + selectedRh.getNom());
        }
}
@FXML
private void handleCertificationSelection() {
        Certification selectedCertification = comboBoxCertification.getSelectionModel().getSelectedItem();
        if (selectedCertification != null) {
            System.out.println("Certification sélectionné : " + selectedCertification.getTitreCertif());
        }
}
    @FXML
    public void ajouterFormation(ActionEvent event) {
        Employe employeSelectionne = comboBoxEmploye.getSelectionModel().getSelectedItem();
        Rh rhSelectionne = comboBoxRH.getSelectionModel().getSelectedItem();
        Certification certificationSelectionnee = comboBoxCertification.getSelectionModel().getSelectedItem();


        if (employeSelectionne == null || rhSelectionne == null || certificationSelectionnee == null) {
            System.out.println("Veuillez sélectionner un employé, un RH et une certification.");
            return;
        }

        Formation formation = new Formation();
        formation.setNomFormation(tfNomFormation.getText());
        formation.setDescriptionFormation(tfDescriptionFormation.getText());
        formation.setDureeFormation(tfDureeFormation.getText());
        formation.setEmploye(employeSelectionne);
        formation.setRh(rhSelectionne);
        formation.setCertification(certificationSelectionnee);

        sf.add(formation);
    }
    @FXML
    public void afficherFormation(ActionEvent event) {
        lbFormations.setText(sf.getAll().toString());
    }

    @FXML
    public void initialize() {
        chargerListes();
    }



}


