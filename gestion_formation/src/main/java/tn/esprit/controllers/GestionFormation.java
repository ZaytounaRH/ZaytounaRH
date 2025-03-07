package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployeFormation;
import tn.esprit.services.ServiceEmployee;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.SessionManager;
import java.sql.Date;
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
    private ScrollPane FormationScrollPane;

    @FXML
    private ComboBox<Formation> formationsComboBox;
@FXML
private ComboBox<Employee> employeesComboBox;
    @FXML
    private ListView<Employee> employesListView;


    @FXML
    private Button affecterButton;
    @FXML
    private VBox cardsContainer;
    @FXML
    private ObservableList<Formation> formationsObservableList;

    ServiceFormation serviceFormation = new ServiceFormation();
    private ServiceEmployeFormation serviceEmployeFormation = new ServiceEmployeFormation();
    private ServiceEmployee serviceEmployee = new ServiceEmployee();
    private DatePickerDialog datePickerDialog;

    public GestionFormation() {
        System.out.println("✅ GestionFormation chargé !");
    }
    @FXML
    public void ajouterFormation(ActionEvent actionEvent) {

        if (tfNomFormation.getText() == "") {
            showAlert("Erreur", "Veuillez entrer le nom de la formation.");
            return;
        }
        if (tfDescriptionFormation.getText() == "") {
            showAlert("Erreur", "Veuillez entrer la description de la formation.");
            return;
        }
        if (dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null) {
            showAlert("Erreur", "Veuillez entrer la duree de formation.");
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
            formationsObservableList.setAll(serviceFormation.getAll());
            afficherFormations(new ActionEvent());
        }
    }
    /*
    public void afficherFormations() {
        System.out.println("📢 Mise à jour des formations affichées !");

        if (formationFlowPane == null) {
            System.out.println("❌ Erreur : formationFlowPane n'est pas initialisé !");
            return;
        }

        // Barre de recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher une formation...");
        searchField.setPrefWidth(300);

        // FlowPane pour afficher les formations
        formationFlowPane = new FlowPane();
        formationFlowPane.setVgap(20);
        formationFlowPane.setHgap(20);

        // Ajout de l'écouteur pour filtrer la liste
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Formation> filteredFormations = serviceFormation.searchByName(newValue);
            updateFormationList(filteredFormations);
        });

        // Récupérer la liste mise à jour des formations
        List<Formation> formations = serviceFormation.getAll();

        // Mettre à jour dynamiquement l'affichage des formations
        updateFormationList(formations);
    }



     */

    public void afficherFormations(ActionEvent actionEvent) {

        System.out.println(" afficherFormations() appelé !");


        Stage nouveauStage = new Stage();

        // Barre de recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher une formation...");
        searchField.setPrefWidth(300);

        // FlowPane pour afficher les formations
        formationFlowPane = new FlowPane();
        formationFlowPane.setVgap(20);
        formationFlowPane.setHgap(20);
        // Bouton pour trier par date de début
        Button sortByDateButton = new Button("Trier par date");
        sortByDateButton.setOnAction(e -> {
            List<Formation> formations = serviceFormation.getAll();
            List<Formation> sortedFormations = serviceFormation.sortByDateDebut(formations);
            updateFormationList(sortedFormations);  // Mise à jour de la liste des formations triées
        });

        // Ajout de l'écouteur pour filtrer la liste
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Formation> filteredFormations = serviceFormation.searchByName(newValue);
            updateFormationList(filteredFormations);
        });

        // Création du ScrollPane pour afficher les formations de manière scrollable
        ScrollPane formationScrollPane = new ScrollPane(formationFlowPane);
        formationScrollPane.setFitToWidth(true);

        // Conteneur principal (VBox)
        VBox root = new VBox(10, searchField, formationScrollPane, sortByDateButton);
        root.setPadding(new Insets(10));

        // Chargement initial des formations
        //updateFormationList(serviceFormation.getAll());

        try {
            List<Formation> formations = serviceFormation.getAll();
            //System.out.println("🎯 Formations affichées dans l'interface : " + formations);
            updateFormationList(formations);
        } catch (Exception e) {
            System.out.println("🔥 Erreur critique bloquant l'affichage !");
            e.printStackTrace();
        }

        // Ajout du VBox à la scène (au lieu du FlowPane seul)
        Scene scene = new Scene(root, 1080, 720);
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
                    //afficherFormations();
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


    @FXML
    public void deleteFormation(Formation formation) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette formation ?");
        confirmationAlert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Si l'utilisateur confirme, on supprime la certification
            serviceFormation.delete(formation);

            // Rafraîchir l'affichage après suppression
            afficherFormations(new ActionEvent());
                //afficherFormations();
        } else {
            System.out.println("Suppression annulée.");
        }
    }

    @FXML
    public void initialize() {
        if (formationFlowPane == null) {
            System.out.println("❌ formationFlowPane est NULL !");
        } else {
            System.out.println("✅ formationFlowPane détecté !");
        }
        Label testLabel = new Label("Test Manuel !");
        testLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");
        formationFlowPane.getChildren().add(testLabel);


        // Charger toutes les formations
        List<Formation> formations = serviceFormation.getAll();

        // Définir un StringConverter pour afficher uniquement les noms des formations
        formationsComboBox.setCellFactory(param -> new ListCell<Formation>() {
            @Override
            protected void updateItem(Formation formation, boolean empty) {
                super.updateItem(formation, empty);
                if (empty || formation == null) {
                    setText(null);
                } else {
                    setText(formation.getNomFormation());  // Afficher uniquement le nom
                }
            }
        });

        // Utiliser un StringConverter pour afficher uniquement le nom
        formationsComboBox.setConverter(new StringConverter<Formation>() {
            @Override
            public String toString(Formation formation) {
                return formation == null ? "" : formation.getNomFormation();  // Affiche le nom
            }

            @Override
            public Formation fromString(String string) {
                return null;  // La conversion inverse n'est pas nécessaire ici
            }
        });

        // Remplir le ComboBox avec la liste des formations
        //formationsComboBox.setItems(FXCollections.observableArrayList(formations));

        formationsObservableList = FXCollections.observableArrayList(serviceFormation.getAll());
        formationsComboBox.setItems(formationsObservableList);


        List<Employee> employes = serviceEmployee.getAll();
        ObservableList<Employee> employesObservableList = FXCollections.observableArrayList(employes);

// Associer la liste à la ListView AVANT de configurer la sélection multiple
        employesListView.setItems(employesObservableList);

// Définir le mode de sélection multiple
        employesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        employesListView.setCellFactory(param -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getNom() + " " + employee.getPrenom()); // Afficher Nom + Prénom
                }
            }
        });
        employesListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Employee>) change -> {
            System.out.println("Employés sélectionnés : " + employesListView.getSelectionModel().getSelectedItems());
        });

/*
        employesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        employesListView.setItems(FXCollections.observableArrayList(serviceEmployee.getAll()));
        //employesListView.setItems(FXCollections.observableArrayList(employes));



 */


        System.out.println("Nombre d'éléments dans formationFlowPane : " + formationFlowPane.getChildren().size());


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

        System.out.println("Nombre d'employés trouvés : " + employees.size());
        for (Employee emp : employees) {
            System.out.println(emp.getId() + " - " + emp.getNom() + " " + emp.getPrenom());
        }


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

    private void updateFormationList(List<Formation> formations) {
        if (formationFlowPane == null) {
            System.out.println("Erreur : formationFlowPane n'est pas initialisé !");
            return;
        }

        formationFlowPane.getChildren().clear(); // Nettoyer avant d'ajouter

        for (Formation formation : formations) {
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

            Button btnModifier = new Button("Modifier");
            btnModifier.setOnAction(e -> updateFormation(formation));

            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            deleteButton.setOnAction(e -> deleteFormation(formation));

            VBox cardContent = new VBox(5, nomLabel, descriptionLabel, dateDebutLabel, dateFinLabel, btnModifier, deleteButton);
            card.getChildren().add(cardContent);

            formationFlowPane.getChildren().add(card);
        }
    }


    /*
private void updateFormationList(List<Formation> formations) {
    if (formationFlowPane == null) {
        System.out.println("❌ Erreur : `formationFlowPane` n'est pas initialisé !");
        return;
    }

    formationFlowPane.getChildren().clear(); // Nettoyer avant d'ajouter

    for (Formation formation : formations) {
        VBox card = new VBox(10);
        card.setStyle("-fx-padding: 15px; -fx-border-color: #cccccc; -fx-background-color: white; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(350);  // Largeur uniforme pour toutes les cartes
        card.setPrefHeight(200); // Hauteur uniforme

        Label nomLabel = new Label(formation.getNomFormation());
        nomLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label descriptionLabel = new Label(formation.getDescriptionFormation());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(320);

        Label dateDebutLabel = new Label("Début : " + formation.getDateDebutFormation());
        dateDebutLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        Label dateFinLabel = new Label("Fin : " + formation.getDateFinFormation());
        dateFinLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        Button btnModifier = new Button("Modifier");
        btnModifier.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        btnModifier.setOnAction(e -> updateFormation(formation));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteFormation(formation));

        HBox buttonBox = new HBox(10, btnModifier, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(nomLabel, descriptionLabel, dateDebutLabel, dateFinLabel, buttonBox);
        formationFlowPane.getChildren().add(card);
    }
}

     */

}























