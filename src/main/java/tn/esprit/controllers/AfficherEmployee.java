/*package tn.esprit.controllers;

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
    private Button buttonAjouterEmploye, buttonAfficherEmployes, buttonUpdateEmploye,buttonDeleteEmploye;

    @FXML
    private ListView<String> listViewEmployes;

    private ServiceEmployee serviceEmployee = new ServiceEmployee();

    @FXML
    public void initialize() {
        afficherTousEmployes();
    }

    @FXML
    public void afficherTousEmployes() {
        listViewEmployes.getItems().clear();

        List<Employee> employees = serviceEmployee.getAll();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionUser.fxml"));
            AnchorPane gestionUserPage = loader.load();

            Stage stage = (Stage) buttonAjouterEmploye.getScene().getWindow();

            Scene scene = new Scene(gestionUserPage);
            stage.setScene(scene);

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
            String selectedEmployeeData = listViewEmployes.getSelectionModel().getSelectedItem();
            if (selectedEmployeeData == null) {
                showAlert("Error", "Please select an employee to update.");
                return;
            }

            int employeeId = extractEmployeeId(selectedEmployeeData); // Implement this method to extract the ID
            Employee selectedEmployee = serviceEmployee.getById(employeeId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEmployee.fxml"));
            AnchorPane updateEmployeePage = loader.load();

            UpdateEmployee controller = loader.getController();
            controller.setSelectedEmployee(selectedEmployee);

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
        String[] parts = employeeData.split(", ");
        String idPart = parts[parts.length - 1];
        return Integer.parseInt(idPart.split(": ")[1]);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void supprimerEmploye() {
        try {
            String selectedEmployeeData = listViewEmployes.getSelectionModel().getSelectedItem();
            if (selectedEmployeeData == null) {
                showAlert("Erreur", "Veuillez sélectionner un employé à supprimer.");
                return;
            }

            int employeeId = extractEmployeeId(selectedEmployeeData);

            Employee employeeToDelete = serviceEmployee.getById(employeeId);
            if (employeeToDelete == null) {
                showAlert("Erreur", "L'employé sélectionné n'existe pas.");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet employé ?");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response.getText().equals("OK")) {
                    serviceEmployee.delete(employeeToDelete);

                    afficherTousEmployes();

                    showAlert("Succès", "L'employé a été supprimé avec succès !");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la suppression : " + e.getMessage());
        }
    }

}*/