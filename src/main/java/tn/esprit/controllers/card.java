package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.models.Entretien;
import tn.esprit.services.ServiceEntretien;

import java.util.List;

public class card {

    @FXML
    private TableView<Entretien> tableViewEntretien;

    @FXML
    private TableColumn<Entretien, Integer> colIdEntretien;

    @FXML
    private TableColumn<Entretien, String> colDateEntretien;

    @FXML
    private TableColumn<Entretien, String> colHeureEntretien;

    @FXML
    private TableColumn<Entretien, String> colTypeEntretien;

    @FXML
    private TableColumn<Entretien, String> colStatutEntretien;

    @FXML
    private TableColumn<Entretien, String> colCommentaire;

    @FXML
    private TableColumn<Entretien, String> colCandidat;

    @FXML
    private TableColumn<Entretien, String> colOffreEmploi;

    private ServiceEntretien serviceEntretien;

    public card() {
        serviceEntretien = new ServiceEntretien();
    }

    @FXML
    public void initialize() {
        // Initialiser les colonnes du TableView
        colIdEntretien.setCellValueFactory(new PropertyValueFactory<>("idEntretien"));
        colDateEntretien.setCellValueFactory(new PropertyValueFactory<>("dateEntretien"));
        colHeureEntretien.setCellValueFactory(new PropertyValueFactory<>("heureEntretien"));
        colTypeEntretien.setCellValueFactory(new PropertyValueFactory<>("typeEntretien"));
        colStatutEntretien.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colCommentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        colCandidat.setCellValueFactory(new PropertyValueFactory<>("candidat"));
        colOffreEmploi.setCellValueFactory(new PropertyValueFactory<>("offreEmploi"));

        // Charger les entretiens dans le TableView
        loadEntretienData();
    }

    private void loadEntretienData() {
        List<Entretien> entretiens = serviceEntretien.getAll();
        ObservableList<Entretien> entretienList = FXCollections.observableArrayList(entretiens);
        tableViewEntretien.setItems(entretienList);
    }
}
