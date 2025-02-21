package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Assurance;
import tn.esprit.models.Assurance.TypeAssurance;
import tn.esprit.services.ServiceAssurance;
import tn.esprit.models.Reclamation;
import tn.esprit.models.Reclamation.PrioriteReclamation;
import tn.esprit.models.Reclamation.StatutReclamation;
import tn.esprit.models.Reclamation.IncidentType;
import tn.esprit.services.ServiceReclamation;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import tn.esprit.utils.MyDatabase;

import java.time.LocalDate;

public class GestionAssurance {

    @FXML
    private TextField tfNom;

    @FXML
    private ComboBox<TypeAssurance> cbTypeAssurance;

    @FXML
    private DatePicker dpDateExpiration;

    @FXML
    private FlowPane flowPaneAssurances;

    @FXML
    private Label lbMessage;

    private final IService<Assurance> sp = new ServiceAssurance();
    private Assurance assuranceAmodifier = null; // Variable pour stocker l'assurance en cours de modification

    @FXML
    public void initialize() {
        cbTypeAssurance.getItems().setAll(TypeAssurance.values()); // Remplit la ComboBox avec les types d'assurance
        afficherAssurances();
    }

    private void clearFields() {
        tfNom.clear(); // Effacer le champ de texte du nom
        cbTypeAssurance.getSelectionModel().clearSelection(); // Effacer la sélection dans la ComboBox
        dpDateExpiration.setValue(null); // Réinitialiser la date
    }

    @FXML
    public void ajouterOuMettreAJourAssurance(ActionEvent actionEvent) {
        String nom = tfNom.getText();
        TypeAssurance typeSelectionne = cbTypeAssurance.getValue();
        LocalDate dateExpiration = dpDateExpiration.getValue();

        if (nom.isEmpty()) {
            lbMessage.setText("Le nom ne peut pas être vide.");
            return;
        }

        if (typeSelectionne == null) {
            lbMessage.setText("Veuillez sélectionner un type d'assurance.");
            return;
        }

        if (dateExpiration == null) {
            lbMessage.setText("Veuillez sélectionner une date d'expiration.");
            return;
        }

        LocalDate troisMoisApres = LocalDate.now().plusMonths(3);
        if (dateExpiration.isBefore(troisMoisApres)) {
            lbMessage.setText("La date d'expiration doit être au moins trois mois après la date actuelle.");
            return;
        }

        if (assuranceAmodifier == null) {
            for (Assurance assuranceExistante : sp.getAll()) {
                if (assuranceExistante.getNom().equals(nom) &&
                        assuranceExistante.getType().equals(typeSelectionne) &&
                        assuranceExistante.getDateExpiration().equals(dateExpiration)) {
                    lbMessage.setText("Cette assurance existe déjà.");
                    return;
                }
            }

            Assurance nouvelleAssurance = new Assurance(nom, typeSelectionne, dateExpiration);
            sp.add(nouvelleAssurance);
            lbMessage.setText("Assurance ajoutée avec succès !");
        } else {
            assuranceAmodifier.setNom(nom);
            assuranceAmodifier.setType(typeSelectionne);
            assuranceAmodifier.setDateExpiration(dateExpiration);
            sp.update(assuranceAmodifier);
            lbMessage.setText("Assurance mise à jour avec succès !");
            assuranceAmodifier = null;
        }

        clearFields();
        afficherAssurances();
    }

    private void lancerReclamation(Assurance assurance) {
        // Création d'une nouvelle fenêtre pour la réclamation
        Stage reclamationStage = new Stage();
        reclamationStage.setTitle("Formulaire de Réclamation");

        // Création du layout
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        // Titre de la réclamation
        Label lblTitre = new Label("Titre:");
        TextField tfTitre = new TextField();
        Label lblDescription = new Label("Description:");
        TextArea taDescription = new TextArea();

        // Type d'incident
        Label lblIncidentType = new Label("Type d'incident:");
        ComboBox<String> cbIncidentType = new ComboBox<>();
        cbIncidentType.getItems().addAll("ACCIDENT_TRAVAIL", "MALADIE_PROFESSIONNELLE", "DÉFAUT_COUVERTURE", "LETIGE_CONTRAT"); // Les types d'incidents

        // Date de soumission (non modifiable)
        Label lblDateSoumission = new Label("Date de soumission:");
        DatePicker dpDateSoumission = new DatePicker(LocalDate.now());
        dpDateSoumission.setEditable(false); // DatePicker non modifiable

        // Statut (non modifiable, initialisé à EN_ATTENTE)
        Label lblStatut = new Label("Statut:");
        ComboBox<String> cbStatut = new ComboBox<>();
        cbStatut.getItems().addAll("EN_ATTENTE");
        cbStatut.setValue("EN_ATTENTE");
        cbStatut.setDisable(true); // Rendre non modifiable

        // Priorité
        Label lblPriorite = new Label("Priorité:");
        ComboBox<String> cbPriorite = new ComboBox<>();
        cbPriorite.getItems().addAll("FAIBLE", "MOYENNE", "ELEVEE");

        // Pièce jointe
        Label lblPieceJointe = new Label("Pièce jointe:");
        TextField tfPieceJointe = new TextField();

        // Message de validation
        Label lbMessage = new Label();

        // Bouton pour soumettre le formulaire
        Button btnSoumettre = new Button("Soumettre la réclamation");

        // Action lors de la soumission
        btnSoumettre.setOnAction(e -> {
            String titre = tfTitre.getText();
            String description = taDescription.getText();
            String incidentTypeString = cbIncidentType.getValue();  // Valeur sélectionnée (String)
            LocalDate dateSoumission = dpDateSoumission.getValue();
            String statutString = cbStatut.getValue();  // Valeur sélectionnée (String)
            String prioriteString = cbPriorite.getValue();  // Valeur sélectionnée (String)
            String pieceJointe = tfPieceJointe.getText();

            // Vérification des champs (vous pouvez ajuster la validation selon vos besoins)
            if (titre.isEmpty() || description.isEmpty() || incidentTypeString == null || statutString == null || prioriteString == null) {
                lbMessage.setText("Tous les champs sont obligatoires.");
                return;
            }

            // Vérification si la réclamation existe déjà
            ServiceReclamation reclamationService = new ServiceReclamation();
            boolean reclamationExistante = reclamationService.exists(titre, description, incidentTypeString, dateSoumission, statutString, prioriteString);

            if (reclamationExistante) {
                lbMessage.setText("Une réclamation identique existe déjà.");
                return;
            }

            // Conversion des valeurs en types enum
            IncidentType incidentType = IncidentType.valueOf(incidentTypeString);  // Convertir String en enum IncidentType
            StatutReclamation statut = StatutReclamation.valueOf(statutString);  // Convertir String en enum StatutReclamation
            PrioriteReclamation priorite = PrioriteReclamation.valueOf(prioriteString);  // Convertir String en enum PrioriteReclamation

            // Création d'une nouvelle réclamation avec les informations saisies
            Reclamation reclamation = new Reclamation(
                    titre, description, incidentType, dateSoumission, statut, priorite, pieceJointe, assurance.getIdA()
            );

            // Appel au service pour sauvegarder la réclamation
            reclamationService.add(reclamation);  // Appel de la méthode add

            lbMessage.setText("Réclamation soumise avec succès !");
            reclamationStage.close(); // Ferme la fenêtre de réclamation
        });

        // Ajouter tous les composants au VBox
        vbox.getChildren().addAll(lblTitre, tfTitre, lblDescription, taDescription, lblIncidentType, cbIncidentType,
                lblDateSoumission, dpDateSoumission, lblStatut, cbStatut, lblPriorite, cbPriorite,
                lblPieceJointe, tfPieceJointe, btnSoumettre, lbMessage);

        // Configurer la fenêtre et afficher
        Scene scene = new Scene(vbox, 400, 400);
        reclamationStage.setScene(scene);
        reclamationStage.show();
    }

    @FXML
    public void afficherAssurances() {
        flowPaneAssurances.getChildren().clear(); // Nettoyer avant de ré-afficher

        for (Assurance assurance : sp.getAll()) {
            // Création du conteneur de la carte
            VBox card = new VBox(10);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: #E8F5E9; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-border-color: #2E7D32;");

            // Ajout des labels
            Label lblNom = new Label("Nom: " + assurance.getNom());
            lblNom.setStyle("-fx-font-weight: bold; -fx-text-fill: #1B5E20;");

            Label lblType = new Label("Type: " + assurance.getType());
            Label lblDate = new Label("Expiration: " + assurance.getDateExpiration());

            // Bouton Modifier
            Button btnModifier = new Button("Modifier");
            btnModifier.setOnAction(e -> remplirFormulaire(assurance));

            // Bouton Supprimer
            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setStyle("-fx-background-color: #E53935; -fx-text-fill: white;");
            btnSupprimer.setOnAction(e -> supprimerAssurance(assurance));

            // Bouton Réclamer
            Button btnReclamer = new Button("Réclamer");
            btnReclamer.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white;");
            btnReclamer.setOnAction(e -> lancerReclamation(assurance));

            // Ajouter les éléments à la carte
            card.getChildren().addAll(lblNom, lblType, lblDate, btnModifier, btnSupprimer, btnReclamer);

            // Ajouter la carte au FlowPane
            flowPaneAssurances.getChildren().add(card);
        }
    }


    private void remplirFormulaire(Assurance assurance) {
        tfNom.setText(assurance.getNom());
        cbTypeAssurance.setValue(assurance.getType());
        dpDateExpiration.setValue(assurance.getDateExpiration());
        assuranceAmodifier = assurance;
    }

    @FXML
    private void supprimerAssurance(Assurance assurance) {
        // Supprimer l'assurance
        sp.delete(assurance);

        // Vérifier si l'assurance a bien été supprimée
        boolean exists = false;
        for (Assurance a : sp.getAll()) {
            if (a.getIdA() == assurance.getIdA()) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            lbMessage.setText("Assurance supprimée avec succès !");
        } else {
            lbMessage.setText("Erreur lors de la suppression de l'assurance.");
        }

        afficherAssurances();
    }

}
