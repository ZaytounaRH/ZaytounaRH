package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployeFormation;
import tn.esprit.services.ServiceEmployee;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.SessionManager;

import java.sql.Date;
import java.util.List;


public class GestionFormation {

    @FXML
    private TextField tfNomFormation;
    @FXML
    private TextField tfDescriptionFormation;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private Label lbFormations;
    @FXML
    private FlowPane formationFlowPane;

    @FXML
    private ComboBox<Formation> formationsComboBox;

    @FXML
    private ListView<Employee> employesListView;

    @FXML
    private Button affecterButton;

    ServiceFormation serviceFormation = new ServiceFormation();
    private ServiceEmployeFormation serviceEmployeFormation = new ServiceEmployeFormation();
    private ServiceEmployee serviceEmployee = new ServiceEmployee();

    @FXML
    public void ajouterFormation(ActionEvent actionEvent) {
        User rhUser = new User();
        rhUser.setUserType("RH");
        SessionManager.getInstance().login(rhUser);

        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null || !"RH".equalsIgnoreCase(currentUser.getUserType())) {
            System.out.println("Erreur : Seuls les RH peuvent ajouter une formation !");
            return;
        }


        Formation formation = new Formation();
        formation.setNomFormation(tfNomFormation.getText());
        formation.setDescriptionFormation(tfDescriptionFormation.getText());
        if (dateDebutPicker.getValue() != null && dateFinPicker.getValue() != null) {
            // Conversion de LocalDate en java.sql.Date
            Date dateDebut = Date.valueOf(dateDebutPicker.getValue());
            Date dateFin = Date.valueOf(dateFinPicker.getValue());

            formation.setDateDebutFormation(dateDebut);
            formation.setDateFinFormation(dateFin);

            serviceFormation.add(formation);
            afficherFormations(actionEvent);
        }
    }



public void afficherFormations(ActionEvent actionEvent) {

        User rhUser = new User();
        rhUser.setUserType("RH");
        SessionManager.getInstance().login(rhUser);

        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null || !"RH".equalsIgnoreCase(currentUser.getUserType())) {
            System.out.println("Erreur : Seuls les RH peuvent ajouter une formation !");
            return;
        }

        // Clear the current cards before adding new ones
        formationFlowPane.getChildren().clear();

        // Loop through the formations and create cards
        for (Formation formation : serviceFormation.getAll()) {
            HBox card = new HBox(10);
            card.setStyle("-fx-padding: 10px; -fx-border-color: #cccccc; -fx-background-color: #f9f9f9; -fx-border-radius: 5px;");

            Label nomLabel = new Label(formation.getNomFormation());
            nomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label descriptionLabel = new Label(formation.getDescriptionFormation());
            descriptionLabel.setStyle("-fx-font-size: 14px;");
            Label dateDebutLabel = new Label("Début : " + formation.getDateDebutFormation().toString());
            dateDebutLabel.setStyle("-fx-font-size: 12px;");
            Label dateFinLabel = new Label("Fin : " + formation.getDateFinFormation().toString());
            dateFinLabel.setStyle("-fx-font-size: 12px;");

            VBox cardContent = new VBox(5, nomLabel, descriptionLabel, dateDebutLabel, dateFinLabel);
            card.getChildren().add(cardContent);

            // Add the card to the existing formationFlowPane
            formationFlowPane.getChildren().add(card);
        }
    }




    @FXML
    public void updateFormation(ActionEvent actionEvent) {
        User rhUser = new User();
        rhUser.setUserType("RH");
        SessionManager.getInstance().login(rhUser);

        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null || !"RH".equalsIgnoreCase(currentUser.getUserType())) {
            System.out.println("Erreur : Seuls les RH peuvent ajouter une formation !");
            return;
        }
        String nomFormation = tfNomFormation.getText();
        Formation formationExistante = serviceFormation.getByName(nomFormation);


        if (formationExistante != null) {
            formationExistante.setNomFormation(tfNomFormation.getText());
            formationExistante.setDescriptionFormation(tfDescriptionFormation.getText());
            formationExistante.setDateDebutFormation(Date.valueOf(dateDebutPicker.getValue()));
            formationExistante.setDateFinFormation(Date.valueOf(dateFinPicker.getValue()));

            serviceFormation.update(formationExistante);
            afficherFormations(actionEvent);
        } else {
            lbFormations.setText("Formation non trouvée !");
        }
    }

    @FXML
    public void deleteFormation(ActionEvent actionEvent) {
        String nomFormation = tfNomFormation.getText();
        Formation formation = serviceFormation.getByName(nomFormation);

        if (formation != null) {
            int idFormation = formation.getIdFormation();
            serviceFormation.delete(formation);
            afficherFormations(actionEvent);

            // Afficher un message de confirmation ou de succès
            System.out.println("Formation supprimée avec succès !");
        } else {
            // Si aucune formation avec ce nom n'est trouvée
            System.out.println("Aucune formation trouvée avec ce nom !");
        }
    }

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
        alert.getDialogPane().setStyle("-fx-background-color: #f4f4f4; -fx-font-size: 14px;");

        alert.showAndWait();
    }


}




    /*
    public void chargerListes() {
        ServiceFormation serviceFormation = new ServiceFormation();


       List<Employee> employes = serviceFormation.g();
       comboBoxEmploye.getItems().addAll(employes);


        List<Rh> rhList = serviceFormation.getAllRH();
        comboBoxRH.getItems().addAll(rhList);


        List<Certification> certifications = serviceFormation.getAllCertifications();
        comboBoxCertification.getItems().addAll(certifications);
    }
/////////////
@FXML
    private void handleEmployeSelection() {
        Employee selectedEmploye = comboBoxEmploye.getSelectionModel().getSelectedItem();
        if (selectedEmploye != null) {
            System.out.println("Employé sélectionné : " + selectedEmploye.getNom());
        }
    }
     */


















