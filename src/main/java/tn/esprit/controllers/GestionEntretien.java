package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import tn.esprit.models.Entretien;
import tn.esprit.models.OffreEmploi;
import tn.esprit.services.ServiceEntretien;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GestionEntretien {
    private Connection cnx;

    public GestionEntretien() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @FXML
    private DatePicker dateEntretienPicker;

    @FXML
    private Spinner<Integer> heureEntretienSpinner;

    @FXML
    private Spinner<Integer> minuteEntretienSpinner;

    @FXML
    private ComboBox<Entretien.TypeEntretien> typeEntretienComboBox;

    @FXML
    private TextField commentaireField;

    @FXML
    private ComboBox<OffreEmploi> offreEmploiComboBox;

    @FXML
    private Button ajouterButton;

    private ServiceEntretien serviceEntretien = new ServiceEntretien(cnx);

    @FXML
    public void initialize() {
        // Charger les offres dans le ComboBox
        loadOffres();

        // Initialiser le Spinner pour les heures (0 à 23)
        SpinnerValueFactory<Integer> heureValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        heureEntretienSpinner.setValueFactory(heureValueFactory);

        // Initialiser le Spinner pour les minutes (0 à 59)
        SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteEntretienSpinner.setValueFactory(minuteValueFactory);

        // Remplir les types d'entretien
        typeEntretienComboBox.setItems(FXCollections.observableArrayList(Entretien.TypeEntretien.values()));

        // Ajouter un écouteur pour le bouton
        ajouterButton.setOnAction(event -> addEntretien());
    }

    private void loadOffres() {
        List<OffreEmploi> offres = new ArrayList<>();
        String query = "SELECT idOffre, titreOffre, salaire, description, datePublication FROM offreemploi";

        try (PreparedStatement stmt = cnx.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                OffreEmploi offre = new OffreEmploi();
                offre.setIdOffre(rs.getInt("idOffre"));
                offre.setTitreOffre(rs.getString("titreOffre"));
                offre.setSalaire(rs.getInt("salaire"));
                offre.setDescription(rs.getString("description"));
                offre.setDatePublication(rs.getDate("datePublication"));
                offres.add(offre);
            }

            // Charger les éléments dans le ComboBox
            offreEmploiComboBox.setItems(FXCollections.observableArrayList(offres));

            // Personnaliser l'affichage dans le ComboBox (ListCell et ButtonCell)
            offreEmploiComboBox.setCellFactory(param -> new ListCell<OffreEmploi>() {
                @Override
                protected void updateItem(OffreEmploi offre, boolean empty) {
                    super.updateItem(offre, empty);
                    if (offre == null || empty) {
                        setText(null);
                    } else {
                        setText(offre.toString());  // Affichage personnalisé de l'offre
                    }
                }
            });

            // Personnaliser l'affichage du bouton sélectionné
            offreEmploiComboBox.setButtonCell(new ListCell<OffreEmploi>() {
                @Override
                protected void updateItem(OffreEmploi offre, boolean empty) {
                    super.updateItem(offre, empty);
                    if (offre == null || empty) {
                        setText(null);
                    } else {
                        setText(offre.toString()); // Affichage du bouton sélectionné
                    }
                }
            });

        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des offres : " + e.getMessage());
        }
    }

    private boolean offreEmploiExists(OffreEmploi offre) {
        String query = "SELECT COUNT(*) FROM offreemploi WHERE idOffre = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, offre.getIdOffre());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;  // L'offre existe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // L'offre n'existe pas
    }

    @FXML
    private void addEntretien() {
        // Vérification des champs obligatoires
        if (dateEntretienPicker.getValue() == null || heureEntretienSpinner.getValue() == null ||
                minuteEntretienSpinner.getValue() == null || typeEntretienComboBox.getValue() == null ||
                offreEmploiComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier si l'offre d'emploi existe
        if (!offreEmploiExists(offreEmploiComboBox.getValue())) {
            showAlert("Erreur", "L'offre d'emploi sélectionnée n'existe pas.");
            return;
        }

        // Création de l'entretien
        Entretien entretien = new Entretien();
        entretien.setDateEntretien(dateEntretienPicker.getValue());

        // Combiner heure et minute pour définir l'heure complète
        LocalTime heure = LocalTime.of(heureEntretienSpinner.getValue(), minuteEntretienSpinner.getValue());
        entretien.setHeureEntretien(heure);

        entretien.setTypeEntretien(typeEntretienComboBox.getValue());
        entretien.setStatut(Entretien.StatutEntretien.EN_COURS);
        entretien.setCommentaire(commentaireField.getText());

        // Associer l'offre sélectionnée
        entretien.setOffreEmploi(offreEmploiComboBox.getValue());

        // Ajouter l'entretien via le service
        serviceEntretien.add(entretien);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
