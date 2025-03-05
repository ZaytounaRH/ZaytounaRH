package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import tn.esprit.test.MainFX;

public class MainController {

    @FXML
    private MainFX mainApp;
    /*
    @FXML
    public void initialize() {
        if (mainApp == null) {
            System.err.println("⚠️ mainApp est null dans MainController");
        } else {
            System.out.println("✅ mainApp est bien défini après initialize() !");
        }
    }

     */
    public void setMainApp(MainFX mainApp) {

        this.mainApp = mainApp;
        if (mainApp != null) {
            System.out.println("✅ mainApp correctement défini dans MainController !");
        } else {
            System.err.println("❌ mainApp est toujours NULL !");
        }
    }

    @FXML
    public void showFormations(ActionEvent event) {

        if (mainApp != null) {
            mainApp.showFormations();
        } else {
            System.err.println("❌ mainApp est NULL dans showFormations !");
        }
    }

    @FXML
    public void showCertifications(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showCertifications();
        } else {
            System.err.println("❌ mainApp est NULL dans showCertifications !");
        }
    }
@FXML
    public void showEmployers(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showEmployers();
        }
        else {
            System.err.println("❌ mainApp est NULL dans showEmployers !");

        }
}



}
