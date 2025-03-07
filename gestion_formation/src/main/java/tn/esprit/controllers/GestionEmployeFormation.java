package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.services.ServiceEmployeFormation;
import tn.esprit.models.Formation;
import tn.esprit.models.Employee;
import tn.esprit.services.ServiceEmployee;
import tn.esprit.services.ServiceFormation;

import java.util.List;

public class GestionEmployeFormation {
    @FXML
    private ComboBox<Formation> formationsComboBox;

    @FXML
    private ListView<Employee> employesListView;

    @FXML
    private Button affecterButton;

    private ServiceEmployeFormation serviceEmployeFormation = new ServiceEmployeFormation();
    private ServiceFormation serviceFormation = new ServiceFormation();
    private ServiceEmployee serviceEmployee = new ServiceEmployee();
    @FXML
    public void initialize() {

        List<Formation> formations = serviceFormation.getAll();
        formationsComboBox.setItems(FXCollections.observableArrayList(formations));


        List<Employee> employes = serviceEmployee.getAll();
        employesListView.setItems(FXCollections.observableArrayList(employes));
        employesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    @FXML
    public void affecterFormation() {
        Formation formationChoisie = formationsComboBox.getSelectionModel().getSelectedItem();
        ObservableList<Employee> employesSelectionnes = employesListView.getSelectionModel().getSelectedItems();

        if (formationChoisie == null || employesSelectionnes.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner une formation et au moins un employé.");
            return;
        }
        List<Integer> employesIds = employesSelectionnes.stream()
                .map(Employee::getId)
                .toList();

            serviceEmployeFormation.affecterFormationAEmployes(formationChoisie.getIdFormation(), employesIds);


        showAlert("Succès", "Les employés ont été affectés à la formation !");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /*
    @FXML
    public void supprimerEmploye() {
        String selectedEmployee = employesListView.getSelectionModel().getSelectedItem();
        // Logique pour supprimer l'employé sélectionné
        if (selectedEmployee != null) {
            // Extraire l'ID de l'employé et supprimer de la formation
            serviceEmployeFormation.supprimerEmployeDeFormation(1,1);
        }
    }
    @FXML
    public void modifierListe() {
        // Logique pour modifier la liste des employés
        // Affichage des employés actuels dans la ListView
        serviceEmployeFormation.afficherEmployesParFormation(1);
    }

     */
}
