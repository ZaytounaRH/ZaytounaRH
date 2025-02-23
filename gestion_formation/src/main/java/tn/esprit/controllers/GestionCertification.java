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
import tn.esprit.services.ServiceCertification;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class GestionCertification {
    @FXML
    private Label lbCertifications;
    @FXML
    private Label lbCertification;
    @FXML
    private TextField tfTitreCertification;
    @FXML
    private TextField tfOrganismeCertification;


    private ServiceCertification serviceCertification = new ServiceCertification();
    IService<Certification> sc = new ServiceCertification();
    @FXML
    public void ajouterCertification(ActionEvent event) {
        Certification c = new Certification();
        c.setTitreCertif(tfTitreCertification.getText());
        c.setOrganismeCertif(tfOrganismeCertification.getText());
        sc.add(c);
    }
    @FXML
    public void afficherCertification(ActionEvent event) {
        lbCertifications.setText(sc.getAll().toString());
    }
    @FXML
    public void initialize() {
        if (lbCertification != null) {
            lbCertification.setText("Bienvenue sur la vue Certification");
        } else {
            System.out.println("Erreur : lbCertification est null.");
        }    }

    @FXML
    public void retourFormation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("formation_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tfTitreCertification.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du retour à la page formation : " + e.getMessage());
        }
    }


}

