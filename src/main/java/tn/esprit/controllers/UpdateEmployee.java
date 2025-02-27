package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Employee;
import tn.esprit.services.ServiceEmployee;

import java.io.IOException;
import java.sql.Date;

public class UpdateEmployee {

    @FXML
    private TextField tfNom;
    @FXML
    private Button btnRetour;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfTelephone;
    @FXML
    private TextField tfEmail;
    @FXML
    private DatePicker dpDateNaissance;
    @FXML
    private TextField tfGenre;
    @FXML
    private TextField tfAdresse;
    @FXML
    private TextField tfJoursOuvrables;
    @FXML
    private TextField tfPassword;
    @FXML
    private ChoiceBox<String> choiceBoxUserType;

    private Employee selectedEmployee;
    private final ServiceEmployee serviceEmployee = new ServiceEmployee();

    // Method to set the selected employee data in the form
    public void setEmployeeData(Employee employee) {
        this.selectedEmployee = employee;
        System.out.println("Setting Employee ID: " + employee.getId());  // Logging for debug purposes

        // Fill the form with employee's data, excluding the ID field
        tfNom.setText(employee.getNom());
        tfPrenom.setText(employee.getPrenom());
        tfTelephone.setText(employee.getNumTel());
        tfEmail.setText(employee.getEmail());
        dpDateNaissance.setValue(employee.getDateDeNaissance().toLocalDate());
        tfGenre.setText(employee.getGender());
        tfAdresse.setText(employee.getAddress());
        tfJoursOuvrables.setText(String.valueOf(employee.getJoursOuvrables()));
        tfPassword.setText(employee.getPassword());

        // Set the user type
        choiceBoxUserType.getItems().clear();
        choiceBoxUserType.getItems().addAll("RH", "Employee", "Candidat");
        choiceBoxUserType.setValue(employee.getUserType());
    }

    @FXML
    public void updateEmployee() {
        System.out.println("Selected Employee ID: " + selectedEmployee.getId());  // Logging for debug purposes

        // Validation to ensure no fields are empty
        if (tfNom.getText().isEmpty() || tfPrenom.getText().isEmpty() || tfTelephone.getText().isEmpty() ||
                tfEmail.getText().isEmpty() || dpDateNaissance.getValue() == null || tfGenre.getText().isEmpty() ||
                tfAdresse.getText().isEmpty() || tfJoursOuvrables.getText().isEmpty() || tfPassword.getText().isEmpty() ||
                choiceBoxUserType.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields!");
            alert.showAndWait();
            return;
        }

        // Parsing the joursOuvrables value
        int joursOuvrables = 0;
        try {
            joursOuvrables = Integer.parseInt(tfJoursOuvrables.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Jours Ouvrables must be a valid number!");
            alert.showAndWait();
            return;
        }

        // Creating an updated Employee object with values from the text fields
        Employee updatedEmployee = new Employee(
                selectedEmployee.getId(),  // Use the existing ID
                tfTelephone.getText(),
                joursOuvrables,
                tfNom.getText(),           // Get new value for nom
                tfPrenom.getText(),        // Get new value for prenom
                tfAdresse.getText(),       // Get new value for adresse
                tfEmail.getText(),         // Get new value for email
                tfGenre.getText(),         // Get new value for genre
                Date.valueOf(dpDateNaissance.getValue()),  // Get new value for dateDeNaissance
                choiceBoxUserType.getValue(),  // Get new value for userType
                tfPassword.getText()       // Get new value for password
        );

        // Log before update for debugging
        System.out.println("before update employee with ID: " + selectedEmployee.getId());
        System.out.println("Employee before update details: " + updatedEmployee.getNom() + ", " + updatedEmployee.getPrenom() + ", " + updatedEmployee.getEmail());

        // Call service to update the employee in the database
        serviceEmployee.update(updatedEmployee);

        // Log after update for debugging
        System.out.println("after update employee with ID: " + selectedEmployee.getId());
        System.out.println("Employee after update details: " + updatedEmployee.getNom() + ", " + updatedEmployee.getPrenom() + ", " + updatedEmployee.getEmail());

        // Show success message
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully!");
        successAlert.showAndWait();
    }


    @FXML
    public void goBackToAfficherEmployee(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEmployee.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
        }
    }
}
