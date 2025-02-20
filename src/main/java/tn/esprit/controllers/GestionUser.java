package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tn.esprit.models.Employee;
import tn.esprit.models.RH;
import tn.esprit.services.ServiceUser;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GestionUser implements Initializable {

    @FXML
    private TextField tfNom, tfPrenom, tfTelephone, tfEmail, tfGenre, tfDepartement, tfDesignation, tfAdresse, tfJoursOuvrables;

    @FXML
    private DatePicker dpDateNaissance;

    @FXML
    private ChoiceBox<String> cbResponsable;

    @FXML
    private Label lbPersonnes;

    // No need for IService for responsibles now
    private ServiceUser<Employee> serviceEmployee = new ServiceUser<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Fetch and populate responsables when the controller is initialized
        List<RH> responsables = getResponsables();

        // Populate the ChoiceBox with RH names
        for (RH rh : responsables) {
            cbResponsable.getItems().add(rh.getNom() + " " + rh.getPrenom());  // Assuming RH has getNom and getPrenom
        }
    }

    @FXML
    public void ajouterPersonne(ActionEvent actionEvent) {
        try {
            // Get text fields values and convert them to the appropriate data types
            int numTel = Integer.parseInt(tfTelephone.getText().trim());
            int joursOuvrables = Integer.parseInt(tfJoursOuvrables.getText().trim());
            String nom = tfNom.getText().trim();
            String prenom = tfPrenom.getText().trim();
            String address = tfAdresse.getText().trim();
            String email = tfEmail.getText().trim();
            String gender = tfGenre.getText().trim();
            String department = tfDepartement.getText().trim();
            String designation = tfDesignation.getText().trim();

            // Get the selected responsable name and convert it to the corresponding ID
            String responsableName = cbResponsable.getValue();
            int responsableId = getResponsableId(responsableName);

            // Convert LocalDate from DatePicker to SQL Date
            Date dateDeNaissance = Date.valueOf(dpDateNaissance.getValue());

            // Create Employee object with the correct types (including responsableName)
            Employee employee = new Employee(numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance, responsableId, responsableName);

            // Call service to add the employee
            serviceEmployee.add(employee);

            // Clear fields after adding
            annulerAjout();

            // Show success alert
            showAlert("Succès", "L'employé a été ajouté avec succès!");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Erreur de conversion : Vérifiez les champs numériques !");
        }
    }

    /**
     * Fetch the list of RH responsables directly from the database.
     */
    private List<RH> getResponsables() {
        List<RH> responsables = new ArrayList<>();

        // SQL query to fetch RH users (user_type = 'RH')
        String query = "SELECT * FROM users WHERE user_type = 'RH'";

        try (Connection connection = DriverManager.getConnection("your-database-url", "username", "password");
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Create RH object from the result set
                RH rh = new RH();
                rh.setId(rs.getInt("id"));
                rh.setNom(rs.getString("nom"));
                rh.setPrenom(rs.getString("prenom"));
                // Add other fields as necessary

                // Add to the list of responsables
                responsables.add(rh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responsables;
    }

    /**
     * Convert a Responsable's name to its corresponding ID.
     */
    private int getResponsableId(String responsableName) {
        // Example: Hardcoded mapping, replace with database lookup if needed
        switch (responsableName) {
            case "Responsable 1":
                return 101;
            case "Responsable 2":
                return 102;
            case "Responsable 3":
                return 103;
            default:
                return 0;  // Default case if no match is found
        }
    }

    /**
     * Show alert dialog for errors or success messages.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Clear all input fields after adding an employee.
     */
    @FXML
    private void annulerAjout() {
        tfNom.clear();
        tfPrenom.clear();
        tfTelephone.clear();
        tfEmail.clear();
        dpDateNaissance.setValue(null);
        tfGenre.clear();
        tfDepartement.clear();
        tfDesignation.clear();
        tfAdresse.clear();
        tfJoursOuvrables.clear();
        cbResponsable.getSelectionModel().clearSelection();
    }
}
