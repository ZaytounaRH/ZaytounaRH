package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Employee;
import tn.esprit.services.ServiceEmployee;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.List;

public class AfficherEmployee {

    @FXML
    private VBox vboxEmployes;

    @FXML
    private Button buttonAfficherEmployes;

    @FXML
    private Button buttonAjouterEmploye;

    @FXML
    private Button buttonUpdateEmploye;

    @FXML
    private Button buttonDeleteEmploye;

    private final ServiceEmployee serviceEmployee = new ServiceEmployee();

    @FXML
    public void afficherTousEmployes() {
        vboxEmployes.getChildren().clear(); // Clean up before displaying

        List<Employee> employees = serviceEmployee.getAll();

        for (Employee employee : employees) {
            // Create an HBox for each employee
            HBox card = new HBox(10);
            card.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-radius: 5;");

            // Create the labels for employee information
            Label nameLabel = new Label("Name: " + employee.getNom() + " " + employee.getPrenom());
            Label emailLabel = new Label("Email: " + employee.getEmail());
            Label phoneLabel = new Label("Phone: " + employee.getNumTel());
            Label dobLabel = new Label("Date of Birth: " + employee.getDateDeNaissance().toString());
            Label addressLabel = new Label("Address: " + employee.getAddress());
            Label workingDaysLabel = new Label("Working Days: " + employee.getJoursOuvrables());
            Label genderLabel = new Label("Gender: " + employee.getGender());
            Label idLabel = new Label("ID: " + employee.getIdEmployee());  // Add the ID label

            // Create a region to space out the labels
            Region spacer = new Region();
            spacer.setPrefWidth(200);

            // Add labels to the HBox
            card.getChildren().addAll(nameLabel, spacer, emailLabel, phoneLabel, dobLabel, addressLabel, workingDaysLabel, genderLabel, idLabel);

            // Add the click event to each HBox (employee card)
            card.setOnMouseClicked(event -> {
                // Reset all previous selections
                for (Node hbox : vboxEmployes.getChildren()) {
                    hbox.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-radius: 5;");
                }

                // Highlight the selected employee card
                card.setStyle("-fx-padding: 10; -fx-border-color: blue; -fx-border-radius: 5; -fx-background-color: lightblue;");

                // Store the selected employee (you can use this for update or delete operations)
                selectedEmployee = employee;
            });

            // Add the employee card to the VBox
            vboxEmployes.getChildren().add(card);
        }
    }

    // Variable to store the selected employee
    private Employee selectedEmployee;

    // Method to get the selected employee for other operations
    public Employee getSelectedEmployee() {
        return selectedEmployee;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateEmploye() {
        if (selectedEmployee != null) {
            try {
                // Load the Update Employee FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEmployee.fxml"));
                AnchorPane updateEmployeePage = loader.load();

                // Get the controller of the UpdateEmployee FXML
                UpdateEmployee updateEmployeeController = loader.getController();

                // Pass the selected employee to the UpdateEmployee controller
                updateEmployeeController.setEmployeeData(selectedEmployee);

                // Get the current stage and set the new scene
                Stage stage = (Stage) buttonUpdateEmploye.getScene().getWindow();
                Scene scene = new Scene(updateEmployeePage);
                stage.setScene(scene);
                stage.setTitle("Update Employee");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case where no employee is selected
            System.out.println("No employee selected!");
        }
    }

    @FXML
    public void supprimerEmploye() {
        if (selectedEmployee != null) {
            // Confirmation dialog before deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Employee");
            alert.setHeaderText("Are you sure you want to delete this employee?");
            alert.setContentText("This action cannot be undone.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Delete the employee from the database using the service
                        serviceEmployee.delete(selectedEmployee);

                        // Remove the employee from the VBox
                        vboxEmployes.getChildren().removeIf(node -> {
                            if (node instanceof HBox) {
                                HBox hbox = (HBox) node;
                                // Match the HBox based on the employee ID or another unique identifier
                                Label idLabel = (Label) hbox.getChildren().stream()
                                        .filter(child -> child instanceof Label)
                                        .filter(child -> ((Label) child).getText().contains("ID: " + selectedEmployee.getIdEmployee()))
                                        .findFirst()
                                        .orElse(null);

                                return idLabel != null;
                            }
                            return false;
                        });

                        // Reset selected employee after deletion
                        selectedEmployee = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        showError("Error deleting the employee.");
                    }
                }
            });
        } else {
            showError("No employee selected to delete.");
        }
    }

    // Method to show an error alert
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
