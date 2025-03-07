package tn.esprit.getionfinanciere.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import tn.esprit.MainFX;
import tn.esprit.getionfinanciere.models.Budget;
import tn.esprit.getionfinanciere.repository.BudgetRepository;

import java.io.IOException;

public class GestionBudget {

    @FXML
    private DatePicker dpDateDebut;
    @FXML
    private DatePicker dpDateFin;
    @FXML
    private TextField tfMontantAlloue;
    @FXML
    private TextField tfTypeBudget;
    @FXML
    private Button ajouterButton;
    @FXML
    private Button afficherButton;
    @FXML
    private Button retourButton;

    private final BudgetRepository serviceBudget = new BudgetRepository();

    @FXML
    public void ajouterBudget(ActionEvent actionEvent) {
        if (dpDateDebut.getValue() == null || dpDateFin.getValue() == null || tfMontantAlloue.getText().isEmpty() || tfTypeBudget.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Formulaire incomplet", "Tous les champs doivent être remplis.");
            return;
        }

        String dateDebut = dpDateDebut.getValue().toString();
        String dateFin = dpDateFin.getValue().toString();
        double montant;
        try {
            montant = Double.parseDouble(tfMontantAlloue.getText());
            if (montant <= 0) {
                showAlert(AlertType.ERROR, "Saisie invalide", "Le montant doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Saisie invalide", "Le montant doit être un nombre.");
            return;
        }

        String typeBudget = tfTypeBudget.getText();
        int idResponsable = 1; // À adapter selon votre logique

        Budget budget = new Budget(montant, dateDebut, dateFin, typeBudget, idResponsable);
        serviceBudget.add(budget);

        showAlert(AlertType.INFORMATION, "Budget ajouté", "Le budget a été ajouté avec succès.");
        clearFields();
    }

    @FXML
    public void afficherBudgets(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("budget_list_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) afficherButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void retour(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("home_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) retourButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        dpDateDebut.setValue(null);
        dpDateFin.setValue(null);
        tfMontantAlloue.clear();
        tfTypeBudget.clear();
    }
}
