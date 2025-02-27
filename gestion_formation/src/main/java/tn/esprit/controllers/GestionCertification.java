package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
import tn.esprit.models.EmployeCertification;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.services.ServiceCertification;
import tn.esprit.services.ServiceEmployeCertification;
import tn.esprit.services.ServiceEmployee;
import tn.esprit.services.ServiceFormation;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class GestionCertification {
    @FXML
    private Label lbCertifications;
    @FXML
    private Label lbCertification;
    @FXML
    private TextField tfTitreCertification;
    @FXML
    private TextField tfOrganismeCertification;
    @FXML
    private DatePicker dateObtention;
    @FXML
    private ComboBox<Formation> cbFormation;
    @FXML
    private ComboBox<Employee> comboBoxEmploye;
    @FXML
    private ComboBox<Certification> comboBoxCertification;
    @FXML
    private FlowPane certificationFlowPane;

    private ServiceCertification serviceCertification = new ServiceCertification();
    private ServiceFormation serviceFormation = new ServiceFormation();
    private ServiceEmployeCertification serviceEmployeCertification = new ServiceEmployeCertification();
    private ServiceEmployee serviceEmployee = new ServiceEmployee();

    @FXML
    public void ajouterCertification(ActionEvent event) {
        String titre = tfTitreCertification.getText();
        String organisme = tfOrganismeCertification.getText();
        Formation formationSelectionnee = cbFormation.getValue(); // Récupération de la formation

        if (formationSelectionnee == null) {
            System.out.println("Veuillez sélectionner une formation !");
            return;
        }

        Certification certification = new Certification(titre, organisme, formationSelectionnee);
        serviceCertification.add(certification);
    }
    @FXML
    public void afficherCertification(ActionEvent event) {


        certificationFlowPane.getChildren().clear();
        for (Certification certification : serviceCertification.getAll()) {
            HBox card = new HBox(10);
            card.setStyle("-fx-padding: 10px; -fx-border-color: #cccccc; -fx-background-color: #f9f9f9; -fx-border-radius: 5px;");
            Label titreLabel=new Label(certification.getTitreCertif());
            titreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label organismeLabel=new Label(certification.getOrganismeCertif());
            organismeLabel.setStyle("-fx-font-size: 14px;");
            Label formationLabel=new Label(certification.getFormation().getNomFormation());
            formationLabel.setStyle("-fx-font-size: 14px;");
            // Bouton de modification
            Button btnModifier = new Button("Modifier");
            btnModifier.setOnAction(e -> modifierCertification(certification));
            // Bouton de suppression
            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            deleteButton.setOnAction(e -> deleteCertification(certification));
            VBox cardContent = new VBox(5,titreLabel,organismeLabel,formationLabel,btnModifier,deleteButton);
            card.getChildren().add(cardContent);

            // Add the card to the existing formationFlowPane
            certificationFlowPane.getChildren().add(card);
        }

    }
    @FXML
    public void modifierCertification(Certification certification) {
        TextInputDialog titreDialog = new TextInputDialog(certification.getTitreCertif());
        titreDialog.setTitle("Modification");
        titreDialog.setHeaderText("Modifier la certification");
        titreDialog.setContentText("Nouveau titre :");
        Optional<String> titreResult = titreDialog.showAndWait();

        // Boîte de dialogue pour modifier l'organisme
        TextInputDialog organismeDialog = new TextInputDialog(certification.getOrganismeCertif());
        organismeDialog.setTitle("Modification");
        organismeDialog.setHeaderText("Modifier l'organisme");
        organismeDialog.setContentText("Nouvel organisme :");
        Optional<String> organismeResult = organismeDialog.showAndWait();

        // Récupérer la liste des formations existantes
        List<Formation> formations = serviceFormation.getAll(); // Assure-toi que ce service existe
        ChoiceDialog<Formation> formationDialog = new ChoiceDialog<>(certification.getFormation(), formations);
        formationDialog.setTitle("Modification");
        formationDialog.setHeaderText("Modifier la formation");
        formationDialog.setContentText("Sélectionnez une formation :");
        Optional<Formation> formationResult = formationDialog.showAndWait();

        // Vérifier si l'utilisateur a entré des nouvelles valeurs
        if (titreResult.isPresent() && organismeResult.isPresent()) {
            certification.setTitreCertif(titreResult.get());
            certification.setOrganismeCertif(organismeResult.get());
            certification.setFormation(formationResult.get());

            // Mettre à jour dans la base de données
            serviceCertification.update(certification);

            // Rafraîchir l'affichage
            afficherCertification(new ActionEvent());
        }
    }
@FXML
public void deleteCertification(Certification certification) {
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Confirmation de suppression");
    confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette certification ?");
    confirmationAlert.setContentText("Cette action est irréversible.");

    Optional<ButtonType> result = confirmationAlert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
        // Si l'utilisateur confirme, on supprime la certification
        serviceCertification.delete(certification);

        // Rafraîchir l'affichage après suppression
        afficherCertification(new ActionEvent());
    } else {
        System.out.println("Suppression annulée.");
    }
}
    @FXML
    public void affecterCertification(ActionEvent event) {

        EmployeCertification employeCertification = new EmployeCertification();

        Employee employeSelectionne = comboBoxEmploye.getSelectionModel().getSelectedItem();
        Certification certificationSelectionnee = comboBoxCertification.getSelectionModel().getSelectedItem();


        if (employeSelectionne == null || certificationSelectionnee == null) {
            System.out.println("Erreur : Veuillez sélectionner un employé et une certification.");
            return;
        }
        if (dateObtention.getValue() == null) {
            System.out.println("Erreur : Veuillez sélectionner une date d'obtention.");
            return;
        }

        java.sql.Date dateObtentionSql = java.sql.Date.valueOf(dateObtention.getValue());

try {
    serviceEmployeCertification.ajouterCertificationAEmploye(employeSelectionne.getIdEmployee(), certificationSelectionnee.getIdCertif(), dateObtentionSql);
    System.out.println("Certification affectée à l'employé avec succès !");
}
catch (Exception e) {
    System.out.println("Erreur lors de l'ajout de la certification : " + e.getMessage());
}

    }
    @FXML
    public void afficherCertifsParEmploye(ActionEvent event) {

        Employee employeSelectionne = comboBoxEmploye.getSelectionModel().getSelectedItem();

        if (employeSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un employé.");
            return;
        }
        List<Certification> certifications = serviceEmployeCertification.getCertificationsByEmployee(employeSelectionne.getId());

        certificationFlowPane.getChildren().clear();

        // Afficher les certifications dans des cartes
        for (Certification certif : certifications) {
            HBox card = new HBox(10);
            card.setStyle("-fx-padding: 10px; -fx-border-color: #cccccc; -fx-background-color: #f9f9f9; -fx-border-radius: 5px;");

            // Créer les labels pour chaque certification
            Label titreCertifLabel = new Label(certif.getTitreCertif());
            titreCertifLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            // Créer une VBox pour contenir les informations de la certification
            VBox cardContent = new VBox(4, titreCertifLabel);
            card.getChildren().add(cardContent);
            // Ajouter la carte au FlowPane
            certificationFlowPane.getChildren().add(card);
        }

        // Afficher un message si aucune certification n'est trouvée
        if (certifications.isEmpty()) {
            showAlert("Information", "Aucune certification trouvée pour cet employé.");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void initialize(){
        List<Formation> formations = serviceFormation.getAll();
        cbFormation.setItems(FXCollections.observableArrayList(formations));

       List<Employee> employes = serviceEmployee.getAll();
        comboBoxEmploye.setItems(FXCollections.observableArrayList(employes));


       List<Certification> certifications = serviceCertification.getAll();
        comboBoxCertification.setItems(FXCollections.observableArrayList(certifications));


    }







    @FXML
    public void retourFormation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("formation_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tfTitreCertification.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du retour à la page formation : " + e.getMessage());
        }
    }


}

