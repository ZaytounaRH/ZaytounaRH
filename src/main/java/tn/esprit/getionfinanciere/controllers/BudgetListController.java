package tn.esprit.getionfinanciere.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.MainFX;
import tn.esprit.getionfinanciere.models.Budget;
import tn.esprit.getionfinanciere.repository.BudgetRepository;

import static tn.esprit.getionfinanciere.utils.Utils.showAlert;

public class BudgetListController {

    @FXML
    private FlowPane flowPaneBudget;
    @FXML
    private TextField searchField;

    private final BudgetRepository serviceBudget = new BudgetRepository();
    private ObservableList<Budget> budgetList;

    public void initialize() {
        budgetList = FXCollections.observableArrayList(serviceBudget.getAll());
        afficherBudgets(budgetList);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchBudget(newValue));
    }

    private void afficherBudgets(ObservableList<Budget> budgets) {
        flowPaneBudget.getChildren().clear();

        for (Budget budget : budgets) {
            VBox card = new VBox();
            card.getStyleClass().add("card");

            Label dateDebut = new Label("Début: " + budget.getDateDebut());
            Label dateFin = new Label("Fin: " + budget.getDateFin());
            Label montant = new Label("Montant: " + budget.getMontantAlloue() + " €");
            Label typeBudget = new Label("Type: " + budget.getTypeBudget());

            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.getStyleClass().add("delete-button");
            btnSupprimer.setOnAction(e -> deleteBudget(budget));

            card.getChildren().addAll(dateDebut, dateFin, montant, typeBudget, btnSupprimer);
            flowPaneBudget.getChildren().add(card);
        }
    }

    private void deleteBudget(Budget budget) {
        serviceBudget.delete(budget);
        budgetList.remove(budget);
        afficherBudgets(budgetList);
        showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le budget a été supprimé.");
    }

    public void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("gestion_budget.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) flowPaneBudget.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchBudget(String searchText) {
        ObservableList<Budget> filteredList = FXCollections.observableArrayList();
        for (Budget budget : budgetList) {
            if (budget.getTypeBudget().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(budget);
            }
        }
        afficherBudgets(filteredList);
    }
}
