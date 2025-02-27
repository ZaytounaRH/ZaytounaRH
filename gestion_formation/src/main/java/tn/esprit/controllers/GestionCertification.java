package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
import tn.esprit.models.Formation;
import tn.esprit.services.ServiceCertification;
import tn.esprit.services.ServiceFormation;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class GestionCertification {
    @FXML
    private Label lbCertifications;
    @FXML
    private Label lbCertification;
    @FXML
    private TextField tfTitreCertification;
    @FXML
    private TextField tfOrganismeCertification;
    @FXML
    private ComboBox<Formation> cbFormation;
    @FXML
    private FlowPane certificationFlowPane;

    private ServiceCertification serviceCertification = new ServiceCertification();
    private ServiceFormation serviceFormation = new ServiceFormation();
    IService<Certification> sc = new ServiceCertification();
    @FXML
    public void ajouterCertification(ActionEvent event) {
        String titre = tfTitreCertification.getText();
        String organisme = tfOrganismeCertification.getText();
        Formation formationSelectionnee = cbFormation.getValue(); // Récupération de la formation

        if (formationSelectionnee == null) {
            System.out.println("Veuillez sélectionner une formation !");
            return;
        }

        Certification certification = new Certification(titre, organisme, formationSelectionnee);
        serviceCertification.add(certification);
    }
    @FXML
    public void afficherCertification(ActionEvent event) {
        certificationFlowPane.getChildren().clear();
        for (Certification certification : serviceCertification.getAll()) {
            HBox card = new HBox(10);
            card.setStyle("-fx-padding: 10px; -fx-border-color: #cccccc; -fx-background-color: #f9f9f9; -fx-border-radius: 5px;");
            Label titreLabel=new Label(certification.getTitreCertif());
            titreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label organismeLabel=new Label(certification.getOrganismeCertif());
            organismeLabel.setStyle("-fx-font-size: 14px;");
            Label formationLabel=new Label(certification.getFormation().getNomFormation());
            formationLabel.setStyle("-fx-font-size: 14px;");
            VBox cardContent = new VBox(4,titreLabel,organismeLabel,formationLabel);
            card.getChildren().add(cardContent);

            // Add the card to the existing formationFlowPane
            certificationFlowPane.getChildren().add(card);
        }

    }

    @FXML
    public void initialize(){
        List<Formation> formations = serviceFormation.getAll();
        cbFormation.setItems(FXCollections.observableArrayList(formations));
    }





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

