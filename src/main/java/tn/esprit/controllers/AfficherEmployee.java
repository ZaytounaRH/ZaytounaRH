package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.models.Employee;
import tn.esprit.services.ServiceEmployee;

import java.io.IOException;
import java.util.List;

public class AfficherEmployee {

    @FXML
    private Button buttonAjouterEmploye;

    @FXML
    private Button buttonAfficherEmployes;

    @FXML
    private ListView<String> listViewEmployes; // ListView to display employees' names

    private ServiceEmployee serviceEmployee = new ServiceEmployee();

    @FXML
    public void initialize() {
        // Initialize the ListView with data when the controller is loaded
        afficherTousEmployes();
    }

    @FXML
    public void afficherTousEmployes() {
        // Clear the ListView before adding new items
        listViewEmployes.getItems().clear();

        // Fetch all employees from the database using ServiceEmployee
        List<Employee> employees = serviceEmployee.getAll();

        // Add employee names and responsable_id to the ListView
        for (Employee emp : employees) {
            listViewEmployes.getItems().add(emp.getNom() + " " + emp.getPrenom() + " (Responsable ID: " + emp.getResponsableId() + ")");
        }
    }

    @FXML
    public void ajouterEmploye() {


        try {
            // Load the GestionUser.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionUser.fxml"));
            AnchorPane gestionUserPage = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) buttonAjouterEmploye.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(gestionUserPage);
            stage.setScene(scene);

            // Optionally, set the title of the new window
            stage.setTitle("Gestion des Utilisateurs");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the GestionUser interface.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}