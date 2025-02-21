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
    private Button buttonAjouterEmploye, buttonAfficherEmployes, buttonUpdateEmploye;

    @FXML
    private ListView<String> listViewEmployes; // ListView to display employees' data

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

        // Add formatted employee data to the ListView
        for (Employee emp : employees) {
            String employeeData = String.format(
                    "Nom: %s, Prenom: %s, Email: %s, Department: %s, Designation: %s, Responsable ID: %d",
                    emp.getNom(), emp.getPrenom(), emp.getEmail(), emp.getDepartment(), emp.getDesignation(), emp.getResponsableId()
            );
            listViewEmployes.getItems().add(employeeData);
        }
    }

    @FXML
    public void ajouterEmploye() {
        try {
            // Load the GestionUser.fxml using a classpath-relative path
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
            showAlert("Error", "Unable to load the GestionUser interface. Please check the file path.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    public void updateEmploye() {
        try {
            // Get the selected employee's data from the ListView
            String selectedEmployeeData = listViewEmployes.getSelectionModel().getSelectedItem();
            if (selectedEmployeeData == null) {
                showAlert("Error", "Please select an employee to update.");
                return;
            }

            // Fetch the selected employee from the database
            int employeeId = extractEmployeeId(selectedEmployeeData); // Implement this method to extract the ID
            Employee selectedEmployee = serviceEmployee.getById(employeeId);

            // Load the UpdateEmployee.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEmployee.fxml"));
            AnchorPane updateEmployeePage = loader.load();

            // Pass the selected employee to the UpdateEmployeeController
            UpdateEmployee controller = loader.getController();
            controller.setSelectedEmployee(selectedEmployee);

            // Open the update window
            Stage stage = new Stage();
            stage.setScene(new Scene(updateEmployeePage));
            stage.setTitle("Mettre à Jour un Employé");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the UpdateEmployee interface. Please check the file path.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private int extractEmployeeId(String employeeData) {
        // Extract the employee ID from the formatted string
        // Example: "Nom: John, Prenom: Doe, Email: john.doe@example.com, Department: IT, Designation: Developer, Responsable ID: 1"
        String[] parts = employeeData.split(", ");
        String idPart = parts[parts.length - 1]; // "Responsable ID: 1"
        return Integer.parseInt(idPart.split(": ")[1]); // Extract "1" and convert to int
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}