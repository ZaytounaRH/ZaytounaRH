package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.esprit.models.Entretien;
import tn.esprit.models.OffreEmploi;
import tn.esprit.services.ServiceEntretien;
import tn.esprit.services.ServiceOffreEmploi;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class EntretienRH {
    private Connection cnx;
    private final ServiceEntretien serviceEntretien;
    private final ServiceOffreEmploi serviceOffreEmploi;
    // Liste des entretiens favoris en mémoire
    private ObservableList<Entretien> favorisList = FXCollections.observableArrayList();

    public EntretienRH() {
        cnx = MyDatabase.getInstance().getCnx();
        serviceEntretien = new ServiceEntretien(cnx);
        serviceOffreEmploi = new ServiceOffreEmploi();
    }

    @FXML
    private FlowPane entretienFlowPane;

    @FXML
    public void initialize() {
        afficherEntretiens();
    }

    @FXML
    public void afficherEntretiens() {
        entretienFlowPane.getChildren().clear();
        try {
            List<Entretien> entretiens = serviceEntretien.getAll();
            updateEntretienList(entretiens);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void updateEntretienList(List<Entretien> entretiens) {
        entretienFlowPane.getChildren().clear();

        for (Entretien entretien : entretiens) {
            VBox card = new VBox();
            card.setPadding(new Insets(15));
            card.setSpacing(10);
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dcdcdc; -fx-border-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 3);");

            // Labels pour les détails de l'entretien
            Label titreLabel = new Label("Entretien pour : " + entretien.getCandidat().getNom() + " " + entretien.getCandidat().getPrenom());
            Label offreLabel = new Label("Offre : " + entretien.getOffreEmploi().getTitreOffre());
            Label typeLabel = new Label("Type : " + entretien.getTypeEntretien());
            Label commentaireLabel = new Label("Commentaire : " + (entretien.getCommentaire().isEmpty() ? "Aucun" : entretien.getCommentaire()));
            Label statutLabel = new Label("Statut : " + entretien.getStatut()); // Statut
            Label dateLabel = new Label("Date : " + entretien.getDateEntretien().toString()); // Date
            Label heureLabel = new Label("Heure : " + entretien.getHeureEntretien().toString()); // Heure

            /////////////////////////////////////////Favoris////////////////////////////////////////
            // Création du bouton avec image
            Button starButton = new Button();
            starButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

            // Charger les images
            Image starEmpty = new Image(getClass().getResource("/star_empty.png").toExternalForm());
            Image starFilled = new Image(getClass().getResource("/star_filled.png").toExternalForm());

            // Créer un ImageView pour l'étoile et ajuster sa taille
            ImageView starImageView = new ImageView();
            starImageView.setFitWidth(20);  // Définir la largeur de l'étoile
            starImageView.setFitHeight(20); // Définir la hauteur de l'étoile

            // Vérifier si l'entretien est dans la liste des favoris
            if (favorisList.contains(entretien)) {
                starImageView.setImage(starFilled); // Étoile pleine si c'est un favori
            } else {
                starImageView.setImage(starEmpty); // Étoile vide sinon
            }

            // Ajouter l'ImageView à un bouton
            starButton.setGraphic(starImageView);

            // Ajouter un événement de clic pour basculer l'état du favori
            starButton.setOnAction(event -> {
                if (favorisList.contains(entretien)) {
                    favorisList.remove(entretien); // Retirer des favoris
                    starImageView.setImage(starEmpty); // Remettre l'étoile vide
                } else {
                    favorisList.add(entretien); // Ajouter aux favoris
                    starImageView.setImage(starFilled); // Mettre l'étoile pleine
                }
            });

            // Ajouter le bouton étoile à la carte
            card.getChildren().add(starButton);
            ///////////////////////////////////////////////////////////////////////

            // Boutons pour modifier et supprimer
            Button modifierButton = new Button("Modifier");
            Button supprimerButton = new Button("Supprimer");

            modifierButton.setOnAction(e -> ouvrirInterfaceModification(entretien));
            supprimerButton.setOnAction(e -> afficherConfirmationSuppression(entretien));

            HBox buttonContainer = new HBox(10, modifierButton, supprimerButton);
            buttonContainer.setAlignment(Pos.CENTER_RIGHT);

            // Ajouter tous les labels et le bouton favoris à la carte
            card.getChildren().addAll(
                    titreLabel,
                    offreLabel,
                    typeLabel,
                    commentaireLabel,
                    statutLabel,
                    dateLabel,
                    heureLabel,
                    buttonContainer
            );

            entretienFlowPane.getChildren().add(card);
        }
    }

    @FXML
    private void ouvrirInterfaceModification(Entretien entretien) {
        Stage modificationStage = new Stage();

        DatePicker dateEntretienPicker = new DatePicker(entretien.getDateEntretien());
        Spinner<Integer> heureEntretienSpinner = new Spinner<>(0, 23, entretien.getHeureEntretien().getHour());
        Spinner<Integer> minuteEntretienSpinner = new Spinner<>(0, 59, entretien.getHeureEntretien().getMinute());

        // Utilisation d'un ComboBox correctement typé pour TypeEntretien
        ComboBox<Entretien.TypeEntretien> typeComboBox = new ComboBox<>();
        typeComboBox.setItems(FXCollections.observableArrayList(Entretien.TypeEntretien.values()));
        typeComboBox.setValue(entretien.getTypeEntretien());

        TextArea commentaireTextArea = new TextArea(entretien.getCommentaire());
        commentaireTextArea.setPrefRowCount(3);

        // Récupération des offres depuis ServiceOffreEmploi
        List<OffreEmploi> offres = serviceOffreEmploi.getAll();
        ComboBox<String> offreComboBox = new ComboBox<>();
        offreComboBox.setItems(FXCollections.observableArrayList(
                offres.stream().map(OffreEmploi::getTitreOffre).collect(Collectors.toList())
        ));
        offreComboBox.setValue(entretien.getOffreEmploi().getTitreOffre());

        Button saveButton = new Button("Sauvegarder");
        saveButton.setOnAction(e -> {
            entretien.setDateEntretien(dateEntretienPicker.getValue());
            LocalTime heure = LocalTime.of(heureEntretienSpinner.getValue(), minuteEntretienSpinner.getValue());
            entretien.setHeureEntretien(heure);
            entretien.setTypeEntretien(typeComboBox.getValue());
            entretien.setCommentaire(commentaireTextArea.getText());

            // Trouver l'objet OffreEmploi sélectionné
            OffreEmploi selectedOffre = offres.stream()
                    .filter(offre -> offre.getTitreOffre().equals(offreComboBox.getValue()))
                    .findFirst().orElse(null);

            if (selectedOffre != null) {
                entretien.setOffreEmploi(selectedOffre);
            }

            serviceEntretien.update(entretien);
            modificationStage.close();
            afficherEntretiens();
        });

        VBox modificationLayout = new VBox(10,
                new Label("Modifier l'entretien"),
                new Label("Date :"), dateEntretienPicker,
                new Label("Heure :"), heureEntretienSpinner, minuteEntretienSpinner,
                new Label("Type :"), typeComboBox,
                new Label("Commentaire :"), commentaireTextArea,
                new Label("Offre :"), offreComboBox,
                saveButton);

        modificationLayout.setPadding(new Insets(20));
        Scene modificationScene = new Scene(modificationLayout, 400, 450);

        modificationStage.setScene(modificationScene);
        modificationStage.setTitle("Modifier l'entretien");
        modificationStage.show();
    }

    @FXML
    private void afficherConfirmationSuppression(Entretien entretien) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet entretien ?");
        alert.setContentText("Cette action est irréversible.");

        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                serviceEntretien.delete(entretien);
                afficherEntretiens();
            }
        });
    }
}
