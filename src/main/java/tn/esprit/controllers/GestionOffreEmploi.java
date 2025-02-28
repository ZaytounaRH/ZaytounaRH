package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import tn.esprit.interfaces.Iservice;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.models.RH;
import tn.esprit.services.ServiceOffreEmploi;
import tn.esprit.utils.MyDatabase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    @FXML
    private ListView<String> listViewRH;
    @FXML
    private ComboBox<RH> cbResponsableRH;

    Iservice<OffreEmploi> serviceOffre = new ServiceOffreEmploi();

    @FXML
    public void initialize() {
        cbStatut.getItems().setAll(StatutOffre.values());
        afficherOffres(null);
        afficherRH(null);
    }

    @FXML
    public void ajouterOffre(ActionEvent actionEvent) {
        if (tfTitre.getText().isEmpty() || tfDescription.getText().isEmpty() ||
                tfSalaire.getText().isEmpty() || dpDatePublication.getValue() == null ||
                cbStatut.getValue() == null || cbResponsableRH.getValue() == null) {
            showAlert(AlertType.ERROR, "Erreur de saisie", "Veuillez remplir tous les champs et sélectionner un responsable RH.");
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
            offre.setRh(cbResponsableRH.getValue());

            serviceOffre.add(offre);
            showAlert(AlertType.INFORMATION, "Succès", "Offre ajoutée avec succès !");
            afficherOffres(null);
            afficherRH(null);
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
    public void afficherRH(ActionEvent actionEvent) {
        List<RH> rhListWithOffres = new ArrayList<>();
        List<RH> allRhList = new ArrayList<>();

        String queryTousRH = "SELECT rh.idRH, rh.nom FROM rh";
        try (PreparedStatement pst = cnx.prepareStatement(queryTousRH); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                RH rh = new RH();
                rh.setIdRH(rs.getInt("idRH"));
                rh.setNom(rs.getString("nom"));
                allRhList.add(rh);
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la récupération des responsables RH.");
        }

        cbResponsableRH.getItems().clear();
        cbResponsableRH.getItems().setAll(allRhList);

        String queryRHWithOffres = "SELECT DISTINCT rh.idRH, rh.nom " +
                "FROM rh " +
                "JOIN OffreEmploi oe ON oe.idRH = rh.idRH";
        try (PreparedStatement pst = cnx.prepareStatement(queryRHWithOffres); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                RH rh = new RH();
                rh.setIdRH(rs.getInt("idRH"));
                rh.setNom(rs.getString("nom"));
                rhListWithOffres.add(rh);
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la récupération des responsables RH ayant des offres.");
        }

        listViewRH.getItems().clear();
        for (RH rh : rhListWithOffres) {
            listViewRH.getItems().add("ID RH: " + rh.getIdRH() + ", Nom: " + rh.getNom());
        }
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
            //ModifierOffreController controller = loader.getController();
           // controller.initData(selectedOffre);  // Pass the selected OffreEmploi to the modifier window

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


}
