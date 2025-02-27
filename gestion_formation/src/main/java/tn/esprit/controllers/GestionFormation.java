package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployeFormation;
import tn.esprit.services.ServiceEmployee;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.SessionManager;
import tn.esprit.controllers.DatePickerDialog;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    @FXML
    private VBox cardsContainer;

    ServiceFormation serviceFormation = new ServiceFormation();
    private ServiceEmployeFormation serviceEmployeFormation = new ServiceEmployeFormation();
    private ServiceEmployee serviceEmployee = new ServiceEmployee();
    private DatePickerDialog datePickerDialog;

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
        formationFlowPane.getChildren().clear();
    Stage nouveauStage = new Stage();

    // Crée un FlowPane pour afficher les formations
    FlowPane formationFlowPane = new FlowPane();
    formationFlowPane.setVgap(20);
    formationFlowPane.setHgap(20);

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

            // Bouton de modification
            Button btnModifier = new Button("Modifier");
            btnModifier.setOnAction(e ->updateFormation(formation));
             /*
            // Bouton de suppression
            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            deleteButton.setOnAction(e -> deleteFormation(formation));


             */
 VBox cardContent = new VBox(5, nomLabel, descriptionLabel, dateDebutLabel, dateFinLabel, btnModifier);
            card.getChildren().add(cardContent);

            // Add the card to the existing formationFlowPane
            formationFlowPane.getChildren().add(card);
        }
    Scene scene = new Scene(formationFlowPane, 800, 600);
    nouveauStage.setScene(scene);
    nouveauStage.setTitle("Formations");
    nouveauStage.show();
    }

    @FXML
    public void updateFormation(Formation formation) {
        // Boîte de dialogue pour modifier le nom
        TextInputDialog nomDialog = new TextInputDialog(formation.getNomFormation());
        nomDialog.setTitle("Modification");
        nomDialog.setHeaderText("Modifier la formation");
        nomDialog.setContentText("Nouveau nom :");
        Optional<String> nomResult = nomDialog.showAndWait();

        // Boîte de dialogue pour modifier la description
        TextInputDialog descriptionDialog = new TextInputDialog(formation.getDescriptionFormation());
        descriptionDialog.setTitle("Modification");
        descriptionDialog.setHeaderText("Modifier la description");
        descriptionDialog.setContentText("Nouvelle description :");
        Optional<String> descriptionResult = descriptionDialog.showAndWait();

        // Sélecteur de date pour la date de début
        Label dateDebutLabel = new Label("Date de début :");
        DatePicker dateDebutPicker = new DatePicker();
        dateDebutPicker.setValue(formation.getDateDebutFormation().toLocalDate()); // Conversion de java.sql.Date à LocalDate
        dateDebutPicker.setPromptText("Choisir une date de début");

        // Sélecteur de date pour la date de fin
        Label dateFinLabel = new Label("Date de fin :");
        DatePicker dateFinPicker = new DatePicker();
        dateFinPicker.setValue(formation.getDateFinFormation().toLocalDate()); // Conversion de java.sql.Date à LocalDate
        dateFinPicker.setPromptText("Choisir une date de fin");

        // Boutons Ok et Annuler
        Button okButton = new Button("Ok");
        Button cancelButton = new Button("Annuler");
        Stage dateDialogStage = new Stage();
        okButton.setOnAction(e -> {
            // Vérifier si les dates sont sélectionnées
            if (dateDebutPicker.getValue() != null && dateFinPicker.getValue() != null) {
                formation.setDateDebutFormation(java.sql.Date.valueOf(dateDebutPicker.getValue())); // Conversion de LocalDate à java.sql.Date
                formation.setDateFinFormation(java.sql.Date.valueOf(dateFinPicker.getValue())); // Conversion de LocalDate à java.sql.Date

                // Mettre à jour les autres informations si elles sont présentes
                if (nomResult.isPresent() && descriptionResult.isPresent()) {
                    formation.setNomFormation(nomResult.get());
                    formation.setDescriptionFormation(descriptionResult.get());

                    // Mettre à jour dans la base de données
                    serviceFormation.update(formation);

                    // Rafraîchir l'affichage
                    afficherFormations(new ActionEvent());
                }
            }

            // Fermer la fenêtre après la mise à jour
            dateDialogStage.close();
        });

        cancelButton.setOnAction(e -> {
            // Fermer la fenêtre sans effectuer de modification
            dateDialogStage.close();
        });

        // Ajouter les composants à la fenêtre
        VBox dateDialog = new VBox(10, dateDebutLabel, dateDebutPicker, dateFinLabel, dateFinPicker, okButton, cancelButton);
        HBox buttonBox = new HBox(10, okButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        dateDialog.getChildren().add(buttonBox);


        Scene dateScene = new Scene(dateDialog, 300, 250);
        dateDialogStage.setTitle("Sélectionner les dates");
        dateDialogStage.setScene(dateScene);
        dateDialogStage.showAndWait();
    }





    /*
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


 */
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
        employesListView.setItems(FXCollections.observableArrayList(serviceEmployee.getAll()));
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




    @FXML
    public void afficherEmployesParFormation() {
        Formation formationChoisie = formationsComboBox.getSelectionModel().getSelectedItem();

        if (formationChoisie == null) {
            showAlert("Erreur", "Veuillez sélectionner une formation.");
            return;
        }

        // Appeler la méthode pour obtenir la liste des employés inscrits
        List<Employee> employees = serviceEmployeFormation.afficherEmployesParFormation(formationChoisie.getIdFormation());

        // Vider le conteneur avant d'ajouter de nouvelles cartes
        cardsContainer.getChildren().clear();

        // Si aucun employé n'est inscrit, afficher un message
        if (employees.isEmpty()) {
            Label noEmployeesLabel = new Label("Aucun employé inscrit à cette formation.");
            cardsContainer.getChildren().add(noEmployeesLabel);
        } else {
            // Ajouter une carte pour chaque employé
            for (Employee emp : employees) {
                // Créer une carte pour chaque employé
                VBox employeeCard = new VBox(10);
                employeeCard.setStyle("-fx-border-color: #0078D4; -fx-border-width: 2; -fx-padding: 10;");

                // Ajouter les informations de l'employé dans la carte
                Label nameLabel = new Label("Nom: " + emp.getNom() + " " + emp.getPrenom());
                Label emailLabel = new Label("Email: " + emp.getEmail());

                // Ajouter les labels à la carte
                employeeCard.getChildren().addAll(nameLabel, emailLabel);

                // Ajouter la carte au conteneur
                cardsContainer.getChildren().add(employeeCard);
            }
        }
    }
    @FXML
    private void modifierEmployesFormation() {
        // Récupérer la formation sélectionnée
        Formation formationChoisie = formationsComboBox.getSelectionModel().getSelectedItem();

        // Vérifier si une formation a bien été sélectionnée
        if (formationChoisie == null) {
            showAlert("Erreur", "Veuillez sélectionner une formation.");
            return;
        }

        // Récupérer la liste des employés sélectionnés
        List<Employee> selectedEmployees = employesListView.getSelectionModel().getSelectedItems();

        // Convertir les employés en leurs IDs
        List<Integer> newEmployeeIds = selectedEmployees.stream()
                .map(Employee::getId)
                .collect(Collectors.toList());

        // Appeler la méthode du service pour modifier la liste des employés
        serviceEmployeFormation.modifierListeEmployesFormation(formationChoisie.getIdFormation(), newEmployeeIds);

        // Afficher un message de confirmation
        showAlert("Succès", "La liste des employés a été mise à jour.");
    }
    @FXML
    private void supprimerEmployeFormation() {
        // Récupérer la formation sélectionnée
        Formation formationChoisie = formationsComboBox.getSelectionModel().getSelectedItem();

        // Vérifier si une formation a bien été sélectionnée
        if (formationChoisie == null) {
            showAlert("Erreur", "Veuillez sélectionner une formation.");
            return;
        }

        // Récupérer l'employé sélectionné
        Employee employeSelectionne = employesListView.getSelectionModel().getSelectedItem();

        // Vérifier si un employé a bien été sélectionné
        if (employeSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un employé.");
            return;
        }

        // Appeler la méthode de service pour supprimer l'employé de la formation
        serviceEmployeFormation.supprimerEmployeDeFormation(employeSelectionne.getId(), formationChoisie.getIdFormation());

        // Rafraîchir la liste des employés pour la formation donnée après la suppression
        afficherEmployesParFormation();

        // Afficher un message de confirmation
        showAlert("Succès", "L'employé a été supprimé de la formation.");
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


















