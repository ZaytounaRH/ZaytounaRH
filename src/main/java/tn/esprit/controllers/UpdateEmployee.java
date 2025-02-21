package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Employee;
import tn.esprit.services.ServiceEmployee;

import java.sql.Date;

public class UpdateEmployee {

    @FXML
    private TextField tfNom, tfPrenom, tfTelephone, tfEmail, tfGenre, tfDepartement, tfDesignation, tfAdresse, tfJoursOuvrables;

    @FXML
    private DatePicker dpDateNaissance;

    @FXML
    private Button btnUpdate, btnAnnuler;

    private Employee selectedEmployee; // The employee to update
    private ServiceEmployee serviceEmployee = new ServiceEmployee();

    // Method to set the selected employee
    public void setSelectedEmployee(Employee employee) {
        this.selectedEmployee = employee;
        populateFields();
    }

    // Populate the fields with the selected employee's data
    private void populateFields() {
        if (selectedEmployee != null) {
            tfNom.setText(selectedEmployee.getNom());
            tfPrenom.setText(selectedEmployee.getPrenom());
            tfTelephone.setText(String.valueOf(selectedEmployee.getNumTel()));
            tfEmail.setText(selectedEmployee.getEmail());
            dpDateNaissance.setValue(selectedEmployee.getDateDeNaissance().toLocalDate());
            tfGenre.setText(selectedEmployee.getGender());
            tfDepartement.setText(selectedEmployee.getDepartment());
            tfDesignation.setText(selectedEmployee.getDesignation());
            tfAdresse.setText(selectedEmployee.getAddress());
            tfJoursOuvrables.setText(String.valueOf(selectedEmployee.getJoursOuvrables()));
        }
    }

    @FXML
    public void updateEmployee() {
        try {
            // Update the selected employee's data
            selectedEmployee.setNom(tfNom.getText());
            selectedEmployee.setPrenom(tfPrenom.getText());
            selectedEmployee.setNumTel(Integer.parseInt(tfTelephone.getText()));
            selectedEmployee.setEmail(tfEmail.getText());
            selectedEmployee.setDateDeNaissance(Date.valueOf(dpDateNaissance.getValue()));
            selectedEmployee.setGender(tfGenre.getText());
            selectedEmployee.setDepartment(tfDepartement.getText());
            selectedEmployee.setDesignation(tfDesignation.getText());
            selectedEmployee.setAddress(tfAdresse.getText());
            selectedEmployee.setJoursOuvrables(Integer.parseInt(tfJoursOuvrables.getText()));

            // Call the service to update the employee
            serviceEmployee.update(selectedEmployee);

            // Close the update window
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Show an error message
        }
    }

    @FXML
    public void annulerUpdate() {
        // Close the update window without saving changes
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
}