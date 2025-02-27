package tn.esprit.getionfinanciere.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tn.esprit.getionfinanciere.utils.Utils;

import java.io.IOException;

public class HomeController {
    @FXML
    private Button hoFournisseur;
    @FXML
    private Button hoCommande;
    @FXML
    private Button hoBudget;
    @FXML
    private Button hoDepence;
    @FXML
    private BarChart<String, Number> budgetDepenseChart;

    @FXML
    public void openGF() {
        Utils.actionButton("gestion_fournisseur.fxml", hoFournisseur);
    }

    @FXML
    public void openGC(ActionEvent actionEvent) {
        Utils.actionButton("gestion_commande.fxml", hoCommande);
    }

    @FXML
    public void openGB() {
        Utils.actionButton("gestion_budget.fxml", hoBudget);
    }

    @FXML
    public void openGD() {
        Utils.actionButton("Dashboard.fxml", hoDepence);
    }

    @FXML
    private void ouvrirDashboard(ActionEvent event) {
        Utils.actionButton("Dashboard.fxml", hoDepence);
    }
}
