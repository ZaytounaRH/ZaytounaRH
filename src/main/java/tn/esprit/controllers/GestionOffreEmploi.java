package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import tn.esprit.interfaces.Iservice;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.services.ServiceOffreEmploi;
import tn.esprit.utils.MyDatabase;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

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
    private ListView<OffreEmploi> listViewOffres;

    Iservice<OffreEmploi> serviceOffre = new ServiceOffreEmploi();

    @FXML
    public void initialize() {
        cbStatut.getItems().setAll(StatutOffre.values());
        afficherOffres(null);
    }

    @FXML
    public void ajouterOffre(ActionEvent actionEvent) {
        if (tfTitre.getText().isEmpty() || tfDescription.getText().isEmpty() ||
                tfSalaire.getText().isEmpty() || dpDatePublication.getValue() == null ||
                cbStatut.getValue() == null) {
            showAlert(AlertType.ERROR, "Erreur de saisie", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            OffreEmploi offre = new OffreEmploi();
            offre.setTitreOffre(tfTitre.getText());
            offre.setDescription(tfDescription.getText());
            offre.setSalaire(Double.parseDouble(tfSalaire.getText()));
            LocalDate localDate = dpDatePublication.getValue();
            offre.setDatePublication(java.sql.Date.valueOf(localDate));
            offre.setStatut(cbStatut.getValue());

            serviceOffre.add(offre);
            showAlert(AlertType.INFORMATION, "Succès", "Offre ajoutée avec succès !");
            afficherOffres(null);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un salaire valide.");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout de l'offre.");
            e.printStackTrace();
        }
    }

    @FXML
    public void afficherOffres(ActionEvent actionEvent) {
        List<OffreEmploi> offresList = serviceOffre.getAll();
        listViewOffres.getItems().clear();
        listViewOffres.getItems().addAll(offresList);  // Directly add OffreEmploi objects
    }

    @FXML
    public void modifierOffre(ActionEvent actionEvent) {
        OffreEmploi selectedOffre = listViewOffres.getSelectionModel().getSelectedItem();

        if (selectedOffre == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner une offre à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierOffre.fxml"));
            Parent root = loader.load();
            ModifierOffreController controller = loader.getController();
            controller.initData(selectedOffre);  // Pass the selected OffreEmploi to the modifier window

            Stage stage = new Stage();
            stage.setTitle("Modifier l'offre");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            afficherOffres(null);  // Rafraîchir la liste après modification
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ouverture de la fenêtre de modification.");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        tfTitre.clear();
        tfDescription.clear();
        tfSalaire.clear();
        dpDatePublication.setValue(null);
        cbStatut.setValue(null);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void supprimerOffre(ActionEvent actionEvent) {
        OffreEmploi selectedOffre = listViewOffres.getSelectionModel().getSelectedItem();

        if (selectedOffre == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner une offre à supprimer.");
            return;
        }

        try {
            serviceOffre.remove(selectedOffre.getIdOffre());  // Pass only the ID

            // After deleting, refresh the list
            afficherOffres(null);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre supprimée avec succès !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression de l'offre.");
            e.printStackTrace();
        }
    }

    @FXML
    public void genererPDF(ActionEvent actionEvent) {
        List<OffreEmploi> offresList = serviceOffre.getAll();
        if (offresList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucune offre à générer en PDF.");
            return;
        }

        // Créer un document PDF
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();

        try {
            // Ajouter une page au document
            document.addPage(page);

            // Préparer le flux de contenu pour ajouter du texte
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Définir la police et la taille du texte
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 750); // Position du texte

            // Ajouter le titre du document
            contentStream.showText("Liste des Offres d'Emploi");
            contentStream.newLineAtOffset(0, -30); // Saut de ligne

            // Ajouter les informations de chaque offre
            contentStream.setFont(PDType1Font.HELVETICA, 12); // Changer de police pour le contenu
            for (OffreEmploi offre : offresList) {
                contentStream.showText("Titre : " + offre.getTitreOffre());
                contentStream.newLineAtOffset(0, -20); // Saut de ligne

                contentStream.showText("Description : " + offre.getDescription());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Salaire : " + offre.getSalaire());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Date de publication : " + offre.getDatePublication());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Statut : " + offre.getStatut());
                contentStream.newLineAtOffset(0, -30); // Espacer les offres
            }

            // Fermer le flux de contenu
            contentStream.endText();
            contentStream.close();

            // Sauvegarder le document dans un fichier
            document.save("OffresEmploi.pdf");

            // Fermer le document
            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le PDF a été généré avec succès !");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la génération du PDF.");
            e.printStackTrace();
        }

    }

    /*@FXML
    public void allerGestionEntretien(ActionEvent event) {
        // Logique pour naviguer vers la page de gestion des entretiens
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/GestionEntretien.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    private void allerGestionConge() {
        try {
            // Charger le fichier FXML de la gestion des congés
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/GestionConge.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène pour la gestion des congés
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Congés");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
