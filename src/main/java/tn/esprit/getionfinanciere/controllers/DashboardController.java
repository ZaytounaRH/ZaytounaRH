package tn.esprit.getionfinanciere.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tn.esprit.getionfinanciere.repository.BudgetRepository;
import tn.esprit.getionfinanciere.repository.DepenseRepository;

import javafx.event.ActionEvent;
import tn.esprit.getionfinanciere.utils.Utils;

import java.util.Map;

public class DashboardController {
    @FXML
    public Button backhome;
    @FXML
    private BarChart<String, Number> budgetDepenseChart;


    private final BudgetRepository budgetRepository = new BudgetRepository();
    private final DepenseRepository depenseRepository = new DepenseRepository();

    @FXML
    private void goBack(ActionEvent event) {
        Utils.actionButton("home_view.fxml", backhome );
    }

    public void initialize() {
        afficherGraphique();
    }

    private void afficherGraphique() {
        Map<String, Double> budgets = budgetRepository.getBudgetParMois();
        Map<String, Double> depenses = depenseRepository.getDepensesParMois();

        XYChart.Series<String, Number> budgetSeries = new XYChart.Series<>();
        budgetSeries.setName("Budget");

        XYChart.Series<String, Number> depenseSeries = new XYChart.Series<>();
        depenseSeries.setName("Dépenses");

        for (String mois : budgets.keySet()) {
            budgetSeries.getData().add(new XYChart.Data<>(mois, budgets.get(mois)));
        }

        for (String mois : depenses.keySet()) {
            depenseSeries.getData().add(new XYChart.Data<>(mois, depenses.getOrDefault(mois, 0.0)));
        }

        budgetDepenseChart.getData().clear();
        budgetDepenseChart.getData().addAll(budgetSeries, depenseSeries);
    }
}
