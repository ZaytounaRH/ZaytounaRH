package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.models.Formation;
import tn.esprit.models.User;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.SessionManager;

import java.sql.Date;


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

    ServiceFormation serviceFormation = new ServiceFormation();

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


















