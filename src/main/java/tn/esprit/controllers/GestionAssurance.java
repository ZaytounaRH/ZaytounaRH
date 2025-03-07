
package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
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
import tn.esprit.models.Reponse;
import tn.esprit.services.ServiceReponse;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import java.time.LocalDate;
import javafx.scene.chart.PieChart;
import java.util.Map;
import tn.esprit.services.OCRService;
import javafx.stage.FileChooser;
import java.io.File;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Collections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;






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

    @FXML
    private FlowPane flowPaneReclamations;

    @FXML
    private TextField tfSearch;

    @FXML
    private ComboBox<String> cbTriDate;

    @FXML
    private ComboBox<Assurance.TypeAssurance> cbFiltreType;

    @FXML
    private PieChart pieChartStats;

    //@FXML
    //private Label lbTotal, lbEnAttente, lbEnCours, lbResolu, lbPourcentageResolues, lbDelaiMoyen;


    private final IService<Assurance> sp = new ServiceAssurance();
    private Assurance assuranceAmodifier = null; // Variable pour stocker l'assurance en cours de modification

    @FXML
    public void initialize() {
        cbTypeAssurance.getItems().setAll(TypeAssurance.values()); // Remplit la ComboBox avec les types d'assurance
        cbFiltreType.getItems().setAll(Assurance.TypeAssurance.values()); // Remplit la ComboBox de filtre avec les types d'assurance
        afficherAssurances();

        // Remplir la ComboBox avec les options pour le tri par date
        cbTriDate.setItems(FXCollections.observableArrayList(
                "Date d'expiration croissante",
                "Date d'expiration décroissante"
        ));

        // 🎯 Ajouter les écouteurs pour déclencher la recherche et le tri dynamiquement
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> rechercherAssurances());
        cbFiltreType.valueProperty().addListener((observable, oldValue, newValue) -> rechercherAssurances());
        cbTriDate.valueProperty().addListener((observable, oldValue, newValue) -> rechercherAssurances());
    }

    private void clearFields() {
        tfNom.clear(); // Effacer le champ de texte du nom
        cbTypeAssurance.getSelectionModel().clearSelection(); // Effacer la sélection dans la ComboBox
        dpDateExpiration.setValue(null); // Réinitialiser la date
    }

    @FXML
    private void rechercherAssurances() {
        String searchKeyword = tfSearch.getText().trim();
        Assurance.TypeAssurance selectedType = cbFiltreType.getValue();
        String selectedSort = cbTriDate.getValue();

        List<Assurance> assurances = sp.getAll(); // Récupérer toutes les assurances

        // 🔍 Appliquer les filtres successivement
        if (!searchKeyword.isEmpty()) {
            assurances = assurances.stream()
                    .filter(a -> a.getNom().toLowerCase().contains(searchKeyword.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (selectedType != null) {
            assurances = assurances.stream()
                    .filter(a -> a.getType().equals(selectedType))
                    .collect(Collectors.toList());
        }

        if (selectedSort != null) {
            boolean ascending = selectedSort.equals("Date d'expiration croissante");
            assurances.sort(Comparator.comparing(Assurance::getDateExpiration));
            if (!ascending) {
                Collections.reverse(assurances);
            }
        }

        afficherAssurances(assurances); // Met à jour l'affichage avec les nouvelles données filtrées
    }

    private void afficherAssurances(List<Assurance> assurances) {
        flowPaneAssurances.getChildren().clear(); // Nettoyer avant d'afficher les nouvelles cartes

        for (Assurance assurance : assurances) {
            VBox card = new VBox(10);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: #E8F5E9; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-border-color: #2E7D32;");

            Label lblNom = new Label("Nom: " + assurance.getNom());
            lblNom.setStyle("-fx-font-weight: bold; -fx-text-fill: #1B5E20;");

            Label lblType = new Label("Type: " + assurance.getType());
            Label lblDate = new Label("Expiration: " + assurance.getDateExpiration());

            Button btnModifier = new Button("Modifier");
            btnModifier.setOnAction(e -> remplirFormulaire(assurance));

            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setStyle("-fx-background-color: #E53935; -fx-text-fill: white;");
            btnSupprimer.setOnAction(e -> supprimerAssurance(assurance));

            Button btnReclamer = new Button("Réclamer");
            btnReclamer.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white;");
            btnReclamer.setOnAction(e -> lancerReclamation(assurance));

            card.getChildren().addAll(lblNom, lblType, lblDate, btnModifier, btnSupprimer, btnReclamer);
            flowPaneAssurances.getChildren().add(card);
        }
    }

    @FXML
    private void testerStats() {
        ServiceReclamation serviceReclamation = new ServiceReclamation();
        Map<String, Object> resultats = serviceReclamation.getStatistiquesReclamations(); // Retourne une map

        // Affichage des statistiques dans les labels
        //afficherStats(resultats);

        // Mise à jour du PieChart avec les données de réclamation
        int enAttente = (int) resultats.get("En Attente");
        int enCours = (int) resultats.get("En Cours");
        int resolu = (int) resultats.get("Résolu");

        // Créer les tranches pour le PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("En Attente", enAttente),
                new PieChart.Data("En Cours", enCours),
                new PieChart.Data("Résolu", resolu)
        );

        // Ajouter les données au PieChart
        pieChartStats.setData(pieChartData);
    }

    /*public void afficherStats(Map<String, Object> stats) {
        lbTotal.setText("Total: " + stats.get("Total"));
        lbEnAttente.setText("En Attente: " + stats.get("En Attente"));
        lbEnCours.setText("En Cours: " + stats.get("En Cours"));
        lbResolu.setText("Résolu: " + stats.get("Résolu"));
        lbPourcentageResolues.setText("Pourcentage Résolu: " + stats.get("Pourcentage Résolu"));
        lbDelaiMoyen.setText("Délai Moyen (jours): " + stats.get("Délai Moyen (jours)"));
    }*/

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
        ComboBox<Reclamation.IncidentType> cbIncidentType = new ComboBox<>();
        cbIncidentType.getItems().addAll(Reclamation.IncidentType.values());  // Ajoute toutes les valeurs de l'énumération

        // Date de soumission (par défaut aujourd'hui et non modifiable)
        Label lblDateSoumission = new Label("Date de soumission:");
        DatePicker dpDateSoumission = new DatePicker(LocalDate.now());
        dpDateSoumission.setDisable(true);

        // Statut (par défaut "EN_ATTENTE" et non modifiable)
        Label lblStatut = new Label("Statut:");
        ComboBox<String> cbStatut = new ComboBox<>();
        cbStatut.getItems().add("EN_ATTENTE");
        cbStatut.setValue("EN_ATTENTE");
        cbStatut.setDisable(true);

        // Priorité
        Label lblPriorite = new Label("Priorité:");
        ComboBox<String> cbPriorite = new ComboBox<>();
        cbPriorite.getItems().addAll("FAIBLE", "MOYENNE", "ELEVEE");

        // Pièce jointe
        Label lblPieceJointe = new Label("Pièce jointe:");
        TextField tfPieceJointe = new TextField();

        // Message d'erreur
        Label lbMessage = new Label();
        lbMessage.setStyle("-fx-text-fill: red;");

        // Bouton pour soumettre le formulaire
        Button btnSoumettre = new Button("Soumettre la réclamation");

        // Action lors de la soumission
        btnSoumettre.setOnAction(e -> {
            String titre = tfTitre.getText().trim();
            String description = taDescription.getText().trim();
            Reclamation.IncidentType incidentType = cbIncidentType.getValue();  // Utiliser l'objet IncidentType sélectionné
            LocalDate dateSoumission = dpDateSoumission.getValue();
            String prioriteString = cbPriorite.getValue();
            String pieceJointe = tfPieceJointe.getText().trim();

            // Vérification des champs obligatoires
            if (titre.isEmpty() || description.isEmpty() || incidentType == null || prioriteString == null) {
                lbMessage.setText("Tous les champs sont obligatoires.");
                return;
            }

            // Conversion des valeurs en types enum pour priorite et statut
            StatutReclamation statut = StatutReclamation.EN_ATTENTE;
            PrioriteReclamation priorite = PrioriteReclamation.valueOf(prioriteString);

            // Création d'une nouvelle réclamation
            Reclamation reclamation = new Reclamation(
                    titre, description, incidentType, dateSoumission, statut, priorite, pieceJointe, assurance.getIdA()
            );

            // Appel au service pour sauvegarder la réclamation
            ServiceReclamation reclamationService = new ServiceReclamation();
            reclamationService.add(reclamation);

            // Rafraîchir l'affichage après l'ajout
            afficherReclamations();

            lbMessage.setStyle("-fx-text-fill: green;");
            lbMessage.setText("Réclamation soumise avec succès !");
            reclamationStage.close(); // Ferme la fenêtre de réclamation
        });

        // Ajouter tous les composants au VBox
        vbox.getChildren().addAll(lblTitre, tfTitre, lblDescription, taDescription, lblIncidentType, cbIncidentType,
                lblDateSoumission, dpDateSoumission, lblStatut, cbStatut, lblPriorite, cbPriorite,
                lblPieceJointe, tfPieceJointe, btnSoumettre, lbMessage);

        // Configurer la fenêtre et afficher
        Scene scene = new Scene(vbox, 400, 450);
        reclamationStage.setScene(scene);
        reclamationStage.show();
    }

    @FXML
    public void afficherAssurances() {
        List<Assurance> assurances = sp.getAll(); // Récupère toutes les assurances
        afficherAssurances(assurances); // Affiche avec la méthode qui gère la mise en page
    }

    @FXML
    private void supprimerReclamation(Reclamation reclamation) {
        ServiceReclamation sr = new ServiceReclamation();
        sr.delete(reclamation);

        afficherReclamations();
    }

    private void ouvrirFenetreReponse(Reclamation reclamation) {
        Stage reponseStage = new Stage();
        reponseStage.setTitle("Répondre à la réclamation");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label lblReponse = new Label("Réponse:");
        TextArea taReponse = new TextArea();

        Button btnEnvoyerReponse = new Button("Envoyer la réponse");
        btnEnvoyerReponse.setOnAction(e -> {
            String reponseText = taReponse.getText().trim();

            if (reponseText.isEmpty()) {
                lbMessage.setText("La réponse ne peut pas être vide.");
                return;
            }

            // Créer une réponse et l'associer à la réclamation
            Reponse reponse = new Reponse(reclamation, reponseText);
            ServiceReponse serviceReponse = new ServiceReponse();
            serviceReponse.add(reponse);

            lbMessage.setStyle("-fx-text-fill: green;");
            lbMessage.setText("Réponse envoyée avec succès !");

            reponseStage.close(); // Fermer la fenêtre de réponse
            afficherReclamations(); // Rafraîchir la liste des réclamations
        });

        vbox.getChildren().addAll(lblReponse, taReponse, btnEnvoyerReponse);
        Scene scene = new Scene(vbox, 400, 300);
        reponseStage.setScene(scene);
        reponseStage.show();
    }
    private void afficherReponses(Reclamation reclamation, VBox card) {
        ServiceReponse serviceReponse = new ServiceReponse();
        FlowPane flowPaneReponses = new FlowPane();
        flowPaneReponses.setHgap(10);
        flowPaneReponses.setVgap(10);

        // Récupérer les réponses de la réclamation
        for (Reponse reponse : serviceReponse.getAllByReclamation(reclamation)) {
            VBox reponseCard = new VBox(10);
            reponseCard.setPadding(new Insets(10));
            reponseCard.setStyle("-fx-background-color: #F1F8E9; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-border-color: #8BC34A;");

            // Ajouter les informations de la réponse
            Label lblReponse = new Label("Réponse: " + reponse.getContenu());
            lblReponse.setStyle("-fx-text-fill: #388E3C;");

            // Ajouter la réponse à son FlowPane
            reponseCard.getChildren().add(lblReponse);
            flowPaneReponses.getChildren().add(reponseCard);
        }

        // Ajouter le FlowPane des réponses à la carte de la réclamation
        card.getChildren().add(flowPaneReponses);
    }
    private void remplirFormulaireReclamation(Reclamation reclamation) {
        Stage modificationStage = new Stage();
        modificationStage.setTitle("Modifier la Réclamation");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label lblTitre = new Label("Titre:");
        TextField tfTitre = new TextField(reclamation.getTitre());

        Label lblDescription = new Label("Description:");
        TextArea taDescription = new TextArea(reclamation.getDescription());

        Label lblIncidentType = new Label("Type d'incident:");
        ComboBox<String> cbIncidentType = new ComboBox<>();
        cbIncidentType.getItems().addAll("ACCIDENT_TRAVAIL", "MALADIE_PROFESSIONNELLE", "DÉFAUT_COUVERTURE", "LITIGE_CONTRAT");
        cbIncidentType.setValue(reclamation.getIncidentType().toString());

        Label lblPriorite = new Label("Priorité:");
        ComboBox<String> cbPriorite = new ComboBox<>();
        cbPriorite.getItems().addAll("FAIBLE", "MOYENNE", "ELEVEE");
        cbPriorite.setValue(reclamation.getPriorite().toString());

        Button btnModifier = new Button("Modifier");
        btnModifier.setOnAction(e -> {
            reclamation.setTitre(tfTitre.getText());
            reclamation.setDescription(taDescription.getText());
            reclamation.setIncidentType(IncidentType.valueOf(cbIncidentType.getValue()));
            reclamation.setPriorite(PrioriteReclamation.valueOf(cbPriorite.getValue()));

            ServiceReclamation sr = new ServiceReclamation();
            sr.update(reclamation);

            modificationStage.close();
            afficherReclamations();
        });

        vbox.getChildren().addAll(lblTitre, tfTitre, lblDescription, taDescription, lblIncidentType, cbIncidentType, lblPriorite, cbPriorite, btnModifier);
        Scene scene = new Scene(vbox, 400, 400);
        modificationStage.setScene(scene);
        modificationStage.show();
    }



    @FXML
    public void afficherReclamations() {
        flowPaneReclamations.getChildren().clear(); // Nettoyer avant de ré-afficher

        ServiceReclamation sr = new ServiceReclamation(); // Service des réclamations

        for (Reclamation reclamation : sr.getAll()) {
            VBox card = new VBox(10);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: #FFF3E0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-border-color: #FF9800;");

            // Labels d'information
            Label lblTitre = new Label("Titre: " + reclamation.getTitre());
            lblTitre.setStyle("-fx-font-weight: bold; -fx-text-fill: #E65100;");

            Label lblDescription = new Label("Description: " + reclamation.getDescription());
            Label lblIncident = new Label("Incident: " + reclamation.getIncidentType());
            Label lblPriorite = new Label("Priorité: " + reclamation.getPriorite());
            Label lblStatut = new Label("Statut: " + reclamation.getStatut());
            Label lblDate = new Label("Soumis le: " + reclamation.getDateSoumission());

            // Bouton Modifier
            Button btnModifier = new Button("Modifier");
            btnModifier.setOnAction(e -> remplirFormulaireReclamation(reclamation));

            // Bouton Supprimer
            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setStyle("-fx-background-color: #E53935; -fx-text-fill: white;");
            btnSupprimer.setOnAction(e -> supprimerReclamation(reclamation));

            // Bouton Répondre
            Button btnRepondre = new Button("Répondre");
            btnRepondre.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            btnRepondre.setOnAction(e -> ouvrirFenetreReponse(reclamation));

            // Ajouter les éléments à la carte
            card.getChildren().addAll(lblTitre, lblDescription, lblIncident, lblPriorite, lblStatut, lblDate, btnModifier, btnSupprimer, btnRepondre);

            // Ajouter la carte au FlowPane
            flowPaneReclamations.getChildren().add(card);

            // Afficher les réponses de la réclamation
            afficherReponses(reclamation, card);

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
        if (assurance == null) {
            lbMessage.setText("Aucune assurance sélectionnée !");
            return;
        }

        // Confirmation de suppression
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'assurance ?");
        alert.setContentText("Voulez-vous vraiment supprimer cette assurance ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer l'assurance
                sp.delete(assurance);

                // Vérifier si l'assurance existe toujours en parcourant la liste
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

                // Rafraîchir la liste affichée
                afficherAssurances();
            } catch (Exception e) {
                lbMessage.setText("Une erreur est survenue : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //////////////////////////////APIIIIIIIIIIIIIIII_OCR///////////////////////////////////////
    // Déclaration du service OCR
    private final OCRService ocrService = new OCRService();

    // Déclaration du TextArea du FXML
    @FXML
    private TextArea textArea; // Cette zone de texte est déjà définie dans ton fichier FXML

    // Déclaration du bouton d'importation du fichier
    @FXML
    private Button importButton;

    // Méthode pour gérer la sélection de l'image
    @FXML
    public void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        // Ouvrir le sélecteur de fichier avec la bonne fenêtre
        File file = fileChooser.showOpenDialog(importButton.getScene().getWindow());
        if (file != null) {
            // Extraction du texte depuis l'image choisie
            String extractedText = ocrService.extractTextFromImage(file);
            // Afficher le texte extrait dans le TextArea
            textArea.setText(extractedText);
        }
    }
    @FXML
    public void exporterEnPDF() {
        // Récupérer le texte du TextArea
        String texte = textArea.getText();

        if (texte.isEmpty()) {
            // Si le texte est vide, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText("Aucun texte à exporter");
            alert.setContentText("Veuillez d'abord extraire le texte de l'image ou saisir du texte.");
            alert.showAndWait();
            return;
        }

        // Créer un document PDF avec iText 7
        try {
            // Demander un emplacement et un nom de fichier pour sauvegarder le PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
            File file = fileChooser.showSaveDialog(importButton.getScene().getWindow());

            if (file != null) {
                // Créer un PdfWriter pour écrire le PDF
                PdfWriter writer = new PdfWriter(file);

                // Créer un PdfDocument en utilisant le PdfWriter
                PdfDocument pdfDoc = new PdfDocument(writer);

                // Créer un Document pour ajouter du contenu
                Document document = new Document(pdfDoc);

                // Ajouter le texte du TextArea au document PDF
                document.add(new Paragraph(texte));

                // Fermer le document PDF
                document.close();

                // Afficher un message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("PDF exporté");
                alert.setContentText("Le texte a été exporté avec succès en PDF.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            // Gérer les erreurs potentielles lors de la création du PDF
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'exportation en PDF");
            alert.setContentText("Une erreur s'est produite lors de la génération du fichier PDF.");
            alert.showAndWait();
        }
    }



}