package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
import tn.esprit.models.Formation;
import tn.esprit.models.Rh;
import tn.esprit.services.ServiceFormation;
import tn.esprit.models.Employe;

import java.io.IOException;
import java.util.List;

public class GestionFormation {

    @FXML
    private TextField tfNomFormation;
    @FXML
    private TextField tfDescriptionFormation;

    IService<Formation> sf = new ServiceFormation();
    //private ServiceFormation serviceFormation = new ServiceFormation();
    @FXML
    private Label lbFormations;
    @FXML
    private ComboBox<Employe> comboBoxEmploye;
    @FXML
    private ComboBox<Rh> comboBoxRH;

    @FXML
    private ComboBox<Certification> comboBoxCertification;
    @FXML
    private ScrollPane formationsScrollPane;
    @FXML
    private VBox cardContainer;
    @FXML
    private AnchorPane logoContainer;
    @FXML
    private VBox cardContainerCertification;
    @FXML
    private Label lbCertifications;
    @FXML
    private ScrollPane certificationsScrollPane;
    @FXML
    private Button btnSubmitCertification;
    @FXML
    private Button btnShow;
    @FXML
    private TextField tfDescriptionCertification;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnShowCertification;
    @FXML
    private TextField tfDureeCertification;
    @FXML
    private TextField tfTitreCertification;

    public void chargerListes() {
        ServiceFormation serviceFormation = new ServiceFormation();


        List<Employe> employes = serviceFormation.getAllEmployes();
        comboBoxEmploye.getItems().addAll(employes);


        List<Rh> rhList = serviceFormation.getAllRH();
        comboBoxRH.getItems().addAll(rhList);


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
        //formation.setDateDebutFormation(tfDebutFormation.getDate());
        //formation.setDateFinFormation(tfDateFin.getDate());
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
    @FXML
    public void ouvrirCertification(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("certification_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tfNomFormation.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture de la page certification : " + e.getMessage());
        }
    }


}








