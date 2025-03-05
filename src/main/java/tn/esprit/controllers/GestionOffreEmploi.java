package tn.esprit.controllers;
import javafx.stage.Modality;
import tn.esprit.services.ServiceEntretien;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Entretien;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.services.ServiceOffreEmploi;
import tn.esprit.utils.MyDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class GestionOffreEmploi {
    private Connection cnx;

    public GestionOffreEmploi() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @FXML
    private TextField tfTitre;
    @FXML
    private TextField tfDescription;
    @FXML
    private TextField tfSalaire;
    @FXML
    private DatePicker dpDatePublication;
    @FXML
    private ComboBox<StatutOffre> cbStatut;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox cardContainer;
    @FXML
    private FlowPane offreFlowPane;

    private final IService<OffreEmploi> serviceOffre = new ServiceOffreEmploi();

    @FXML
    public void initialize() {
        System.out.println("GestionOffreEmploi : initialize() exécuté !");

        cbStatut.getItems().setAll(StatutOffre.values());
        //afficherOffres();  // Affiche les offres au démarrage
        //cbStatut.setItems(FXCollections.observableArrayList(StatutOffre.values()));
        afficherOffres(cardContainer);

    }

    @FXML
    public void ajouterOffre(ActionEvent actionEvent) {
        if (tfTitre.getText().isEmpty() || tfDescription.getText().isEmpty() ||
                tfSalaire.getText().isEmpty() || dpDatePublication.getValue() == null ||
                cbStatut.getValue() == null ) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez remplir tous les champs ");
            return;
        }

        try {
            OffreEmploi offre = new OffreEmploi();
            offre.setTitreOffre(tfTitre.getText());
            offre.setDescription(tfDescription.getText());
            offre.setSalaire(Double.parseDouble(tfSalaire.getText()));
            offre.setDatePublication(Date.valueOf(dpDatePublication.getValue()));
            offre.setStatut(cbStatut.getValue());

            serviceOffre.add(offre);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre ajoutée avec succès !");
            //afficherOffres();  // Rafraîchit la liste des offres
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un salaire valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout de l'offre.");
            e.printStackTrace();
        }

        // Rafraîchir la liste des offres dans l'interface
        try {
            List<OffreEmploi> offres = serviceOffre.getAll();
            updateOffreList(offreFlowPane, offres);  // Rafraîchir le FlowPane avec les nouvelles offres
        } catch (Exception e) {
            System.out.println("🔥 Erreur critique lors du rafraîchissement des offres !");
            e.printStackTrace();
        }
    }
    ////////////////////////////////////////////////////:
    @FXML
    public void afficherOffres(VBox cardContainer) {
        System.out.println("afficherOffres() appelé !");

        Stage nouveauStage = new Stage();

        // Barre de recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher une offre...");
        searchField.setPrefWidth(300);

        // Initialisation du FlowPane
        offreFlowPane = new FlowPane();
        offreFlowPane.setVgap(20);
        offreFlowPane.setHgap(20);

        // Création du ScrollPane pour afficher les cartes des offres de manière scrollable
        ScrollPane offreScrollPane = new ScrollPane(offreFlowPane);
        offreScrollPane.setFitToWidth(true);

        // Conteneur principal (VBox)
        VBox root = new VBox(10, searchField, offreScrollPane);
        root.setPadding(new Insets(10));

        // Chargement initial des offres
        try {
            List<OffreEmploi> offres = serviceOffre.getAll();
            System.out.println("🎯 Offres affichées dans l'interface : " + offres);
            updateOffreList(offreFlowPane, offres);
        } catch (Exception e) {
            System.out.println("🔥 Erreur critique bloquant l'affichage des offres !");
            e.printStackTrace();
        }

        // Ajout du VBox à la scène
        Scene scene = new Scene(root, 800, 600);
        nouveauStage.setScene(scene);
        nouveauStage.setTitle("Offres d'Emploi");
        nouveauStage.show();
    }

    private void updateOffreList(FlowPane offreFlowPane, List<OffreEmploi> offres) {
        offreFlowPane.getChildren().clear(); // Vider le FlowPane avant d'ajouter les nouvelles cartes

        for (OffreEmploi offre : offres) {
            // Créer un conteneur pour chaque offre, ici un VBox
            VBox card = new VBox();
            card.setPadding(new Insets(15));
            card.setSpacing(10);
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dcdcdc; -fx-border-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 3);");

            // Titre de l'offre
            Label titreLabel = new Label(offre.getTitreOffre());
            titreLabel.setFont(new Font("Arial", 18));
            titreLabel.setTextFill(Color.web("#2c3e50"));

            // Description
            Label descriptionLabel = new Label(offre.getDescription());
            descriptionLabel.setFont(new Font("Arial", 14));
            descriptionLabel.setWrapText(true);

            // Salaire
            Label salaireLabel = new Label("Salaire : " + offre.getSalaire() + " TND");
            salaireLabel.setFont(new Font("Arial", 14));
            salaireLabel.setTextFill(Color.web("#16a085"));

            // Statut
            Label statutLabel = new Label("Statut : " + offre.getStatut());
            statutLabel.setFont(new Font("Arial", 14));
            statutLabel.setTextFill(offre.getStatut().equals(OffreEmploi.StatutOffre.ENCOURS) ? Color.web("#f39c12") : Color.web("#27ae60"));

            // Date de publication
            Label datePublicationLabel = new Label("Publié le : " + offre.getDatePublication().toString());
            datePublicationLabel.setFont(new Font("Arial", 14));
            datePublicationLabel.setTextFill(Color.web("#8e44ad"));

            // Ajouter les éléments à la carte
            card.getChildren().addAll(titreLabel, descriptionLabel, salaireLabel, statutLabel, datePublicationLabel);

            // Créer les boutons de modification et de suppression
            Button modifierButton = new Button("Modifier");
            Button supprimerButton = new Button("Supprimer");

            modifierButton.setOnAction(e -> ouvrirInterfaceModification(offre));
            supprimerButton.setOnAction(e -> afficherConfirmationSuppression(offre));

            // Ajouter le bouton "Voir les entretiens"
            Button voirEntretiensButton = new Button("Voir les entretiens");
            voirEntretiensButton.setOnAction(e -> afficherEntretiens(offre));

            // Ajouter les boutons à la carte
            HBox buttonContainer = new HBox(10, modifierButton, supprimerButton, voirEntretiensButton);
            buttonContainer.setAlignment(Pos.CENTER_RIGHT);
            card.getChildren().add(buttonContainer);

            // Ajouter la carte au FlowPane
            offreFlowPane.getChildren().add(card);
        }
    }

    private void ouvrirInterfaceModification(OffreEmploi offre) {
        Stage modificationStage = new Stage();

        // Création des champs de saisie pré-remplis
        TextField titreField = new TextField(offre.getTitreOffre());
        TextArea descriptionField = new TextArea(offre.getDescription());
        TextField salaireField = new TextField(String.valueOf(offre.getSalaire()));

        // Si l'offre utilise java.sql.Date, convertissez-le en LocalDate pour le DatePicker
        java.sql.Date sqlDate = (Date) offre.getDatePublication();
        LocalDate localDate = sqlDate.toLocalDate();
        DatePicker datePublicationPicker = new DatePicker(localDate);

        Button saveButton = new Button("Sauvegarder");
        saveButton.setOnAction(e -> {
            try {
                // Sauvegarder les modifications
                offre.setTitreOffre(titreField.getText());
                offre.setDescription(descriptionField.getText());
                offre.setSalaire(Double.parseDouble(salaireField.getText()));

                // Conversion de LocalDate à java.sql.Date pour le setter
                java.sql.Date datePublication = Date.valueOf(datePublicationPicker.getValue());
                offre.setDatePublication(datePublication);

                // Mise à jour dans le service
                serviceOffre.update(offre);

                // Fermer la fenêtre de modification
                modificationStage.close();

                // Rafraîchir l'affichage des offres
                List<OffreEmploi> offres = serviceOffre.getAll();
                updateOffreList(offreFlowPane, offres);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox modificationLayout = new VBox(10,
                new Label("Modifier l'offre"),
                titreField,
                descriptionField,
                salaireField,
                datePublicationPicker,
                saveButton
        );
        modificationLayout.setPadding(new Insets(20));
        Scene modificationScene = new Scene(modificationLayout, 400, 350);

        modificationStage.setScene(modificationScene);
        modificationStage.setTitle("Modifier l'offre");
        modificationStage.show();
    }


    private void afficherConfirmationSuppression(OffreEmploi offre) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette offre ?");
        alert.setContentText("Cette action est irréversible.");

        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                // Suppression de l'offre
                serviceOffre.delete(offre);

                // Rafraîchir l'affichage des offres après la suppression
                try {
                    List<OffreEmploi> offres = serviceOffre.getAll();
                    updateOffreList(offreFlowPane, offres);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @FXML
    private void afficherEntretiens(OffreEmploi offre) {
        // Récupérer l'ID de l'offre
        int idOffre = offre.getIdOffre();  // Suppose que tu as une méthode getIdOffre() dans ta classe OffreEmploi

        // Récupérer les entretiens pour l'offre donnée
        ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
        List<Entretien> entretiens = serviceOffreEmploi.getEntretienByOffre(idOffre);

        // Créer un conteneur pour afficher les entretiens
        VBox entretienContainer = new VBox(10);
        entretienContainer.setPadding(new Insets(10));

        // Si des entretiens sont trouvés, les afficher
        if (!entretiens.isEmpty()) {
            for (Entretien entretien : entretiens) {
                Label entretienLabel = new Label("Entretien prévu pour : " + entretien.getCandidat().getNom() + " " + entretien.getCandidat().getPrenom());
                Button detailsButton = new Button("Voir les détails");

                // Ouvrir les détails de l'entretien lorsqu'on clique
                detailsButton.setOnAction(e -> {
                    System.out.println("Bouton cliqué !");
                    afficherDetailsEntretien(entretien);
                });

                // Créer une HBox pour chaque entretien
                HBox entretienHBox = new HBox(10, entretienLabel, detailsButton);
                entretienContainer.getChildren().add(entretienHBox);
            }
        } else {
            Label noEntretiensLabel = new Label("Aucun entretien programmé pour cette offre.");
            entretienContainer.getChildren().add(noEntretiensLabel);
        }

        // Créer une nouvelle scène pour afficher les entretiens
        Scene entretienScene = new Scene(entretienContainer, 400, 300);
        Stage entretienStage = new Stage();
        entretienStage.setScene(entretienScene);
        entretienStage.setTitle("Entretiens pour l'offre " + idOffre);
        entretienStage.show();
    }

    @FXML
    private void afficherDetailsEntretien(Entretien entretien) {
        if (entretien.getCandidat() == null) {
            // Si le candidat est null, afficher un message d'erreur ou une valeur par défaut
            System.out.println("Candidat non assigné pour cet entretien.");
            return; // Ou tu peux afficher une fenêtre d'erreur
        }

        // Si le candidat est non-null, tu peux afficher les détails comme prévu
        Stage detailsStage = new Stage();

        // Créer les labels pour les détails de l'entretien
        Label candidatLabel = new Label("Candidat: " + entretien.getCandidat().getNom() + " " + entretien.getCandidat().getPrenom());
        Label dateLabel = new Label("Date: " + entretien.getDateEntretien());
        Label heureLabel = new Label("Heure: " + entretien.getHeureEntretien());
        Label statutLabel = new Label("Statut: " + entretien.getStatut());
        Label commentaireLabel = new Label("Commentaire: " + entretien.getCommentaire());

        // Créer un layout pour afficher les détails
        VBox detailsLayout = new VBox(10, candidatLabel, dateLabel, heureLabel, statutLabel, commentaireLabel);
        detailsLayout.setPadding(new Insets(20));

        // Créer une scène avec les détails
        Scene detailsScene = new Scene(detailsLayout, 400, 300);
        detailsStage.setScene(detailsScene);
        detailsStage.setTitle("Détails de l'entretien");

        // Afficher la scène de détails avec un comportement modal
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.showAndWait();  // Cela bloque l'accès à la fenêtre principale tant que cette fenêtre est ouverte
    }
    //////////////////////////////////////////
   /* @FXML
    public void afficherOffres(VBox cardContainer) {
        // Vider le conteneur avant d'ajouter de nouvelles cartes
        cardContainer.getChildren().clear();

        // Récupérer la liste des offres
        List<OffreEmploi> offres = serviceOffre.getAll();

        for (OffreEmploi offre : offres) {
            // Créer un conteneur pour chaque offre, ici nous utilisons un VBox pour simuler un CardView
            VBox card = new VBox();
            card.setPadding(new Insets(15));
            card.setSpacing(10);
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dcdcdc; -fx-border-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 3);");

            // Titre de l'offre
            Label titreLabel = new Label(offre.getTitreOffre());
            titreLabel.setFont(new Font("Arial", 18));
            titreLabel.setTextFill(Color.web("#2c3e50"));

            // Description
            Label descriptionLabel = new Label(offre.getDescription());
            descriptionLabel.setFont(new Font("Arial", 14));
            descriptionLabel.setWrapText(true);

            // Salaire
            Label salaireLabel = new Label("Salaire : " + offre.getSalaire() + " TND");
            salaireLabel.setFont(new Font("Arial", 14));
            salaireLabel.setTextFill(Color.web("#16a085"));

            // Statut
            Label statutLabel = new Label("Statut : " + offre.getStatut());
            statutLabel.setFont(new Font("Arial", 14));
            statutLabel.setTextFill(offre.getStatut().equals(OffreEmploi.StatutOffre.ENCOURS) ? Color.web("#f39c12") : Color.web("#27ae60"));

            // Ajouter les éléments à la carte
            card.getChildren().addAll(titreLabel, descriptionLabel, salaireLabel, statutLabel);

            // Ajouter la carte au conteneur principal (cardContainer)
            cardContainer.getChildren().add(card);
        }
    }

    public void updateOffre(OffreEmploi offre) {
        try {
            // Charger le fichier FXML pour la modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierOffre.fxml"));
            Parent root = loader.load();

            // Passer l'offre à la nouvelle fenêtre
            ModifierOffreController controller = loader.getController();
            controller.initialize(offre);

            // Créer une nouvelle scène pour la modification
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Modifier l'Offre");
            stage.setScene(scene);

            // Ajouter un gestionnaire d'événements pour fermer la fenêtre lorsque la modification est terminée
            stage.setOnHiding(event -> afficherOffresEmploi()); // Par exemple, réactualiser la liste des offres
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ouverture de la fenêtre de modification.");
            e.printStackTrace();
        }
    }
    @FXML
    public void deleteOffre(OffreEmploi offre) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette offre ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            serviceOffre.delete(offre);
            afficherOffresEmploi();  // Rafraîchit la liste des offres après suppression
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre supprimée avec succès !");
        }
    }*/


    private void clearFields() {
        tfTitre.clear();
        tfDescription.clear();
        tfSalaire.clear();
        dpDatePublication.setValue(null);
        cbStatut.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
