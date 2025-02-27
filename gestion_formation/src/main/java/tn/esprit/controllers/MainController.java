package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import tn.esprit.test.MainFX;

public class MainController {

    @FXML
    private MainFX mainApp;

    public void setMainApp(MainFX mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void showFormations(ActionEvent event) {
        mainApp.showFormations();  // Affiche la vue des formations
    }

    @FXML
    public void showCertifications(ActionEvent event) {
        mainApp.showCertifications();  // Affiche la vue des certifications
    }




}
