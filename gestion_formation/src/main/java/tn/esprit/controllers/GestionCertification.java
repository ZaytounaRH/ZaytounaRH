package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
import tn.esprit.services.ServiceCertification;

import java.util.List;


public class GestionCertification {
    @FXML
    private Label lbCertification;

    @FXML
    public void initialize() {
        lbCertification.setText("Bienvenue sur la vue Certification");
    }
}

