package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.models.Employee;
import tn.esprit.services.ServiceEmployee;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class GestionUser implements Initializable {

    @FXML
    private TextField tfNom, tfPrenom, tfTelephone, tfEmail, tfGenre, tfDepartement, tfDesignation, tfAdresse, tfJoursOuvrables;

    @FXML
    private DatePicker dpDateNaissance;

    @FXML
    private Button btnBack;

    @FXML
    private ChoiceBox<String> cbResponsable;

    private ServiceEmployee serviceEmployee = new ServiceEmployee();

    private static final List<String> RESPONSABLES = Arrays.asList("Fares Dammak", "Manel Oughlani");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbResponsable.getItems().addAll(RESPONSABLES);
    }

    @FXML
    public void ajouterPersonne(ActionEvent actionEvent) {
        if (!validateInputs()) return;

        try {
            int numTel = Integer.parseInt(tfTelephone.getText().trim());
            int joursOuvrables = Integer.parseInt(tfJoursOuvrables.getText().trim());

            String nom = tfNom.getText().trim();
            String prenom = tfPrenom.getText().trim();
            String address = tfAdresse.getText().trim();
            String email = tfEmail.getText().trim();
            String gender = tfGenre.getText().trim();
            String department = tfDepartement.getText().trim();
            String designation = tfDesignation.getText().trim();
            String responsableName = cbResponsable.getValue();
            int responsableId = getResponsableId(responsableName);

            Date dateDeNaissance = Date.valueOf(dpDateNaissance.getValue());

            Employee employee = new Employee(numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance, responsableId, responsableName);
            serviceEmployee.add(employee);

            annulerAjout();
            showAlert("Succès", "L'employé a été ajouté avec succès!");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Vérifiez les champs numériques !");
        }
    }

    private boolean validateInputs() {
        if (tfNom.getText().isEmpty() || tfPrenom.getText().isEmpty() || tfTelephone.getText().isEmpty()
                || tfEmail.getText().isEmpty() || tfGenre.getText().isEmpty() || tfDepartement.getText().isEmpty()
                || tfDesignation.getText().isEmpty() || tfAdresse.getText().isEmpty() || tfJoursOuvrables.getText().isEmpty()
                || dpDateNaissance.getValue() == null || cbResponsable.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return false;
        }

        if (!Pattern.matches("\\d{8}", tfTelephone.getText())) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir 8 chiffres !");
            return false;
        }

        if (!tfEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Erreur", "Adresse email invalide !");
            return false;
        }

        if (dpDateNaissance.getValue().isAfter(LocalDate.now())) {
            showAlert("Erreur", "La date de naissance ne peut pas être dans le futur !");
            return false;
        }

        return true;
    }

    private int getResponsableId(String responsableName) {
        return responsableName.equals("Fares Dammak") ? 1 : 2;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void annulerAjout() {
        tfNom.clear(); tfPrenom.clear(); tfTelephone.clear(); tfEmail.clear(); tfGenre.clear();
        tfDepartement.clear(); tfDesignation.clear(); tfAdresse.clear(); tfJoursOuvrables.clear();
        dpDateNaissance.setValue(null); cbResponsable.getSelectionModel().clearSelection();
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEmployee.fxml"));
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger l'interface AfficherEmployee.");
        }
    }
}
