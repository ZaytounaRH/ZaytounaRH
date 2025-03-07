package tn.esprit.getionfinanciere.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.models.enums.TypeService;
import tn.esprit.getionfinanciere.services.ServiceFournisseur;

import static tn.esprit.getionfinanciere.utils.Constants.REGEX_ADRESS;
import static tn.esprit.getionfinanciere.utils.Constants.REGEX_NOM;
import static tn.esprit.getionfinanciere.utils.Constants.REGEX_NUMBER;
import static tn.esprit.getionfinanciere.utils.Constants.SAISIE_INVALIDE;
import static tn.esprit.getionfinanciere.utils.Utils.actionButton;
import static tn.esprit.getionfinanciere.utils.Utils.showAlert;

public class GestionFournisseur {

    @FXML
    private TextField tfNomFournisseur;
    @FXML
    private TextField tfAdresse;
    @FXML
    private TextField tfContact;
    @FXML
    private ComboBox<TypeService> cbTypeService;
    @FXML
    private Button afficherButton;
    @FXML
    private Button homepageButton;

    private final ServiceFournisseur serviceFournisseur = new ServiceFournisseur();

    @FXML
    public void initialize() {
        cbTypeService.getItems().setAll(TypeService.values());
    }

    @FXML
    public void ajouterFournisseur() {
        if (tfNomFournisseur.getText().isEmpty() || tfAdresse.getText().isEmpty() ||
            tfContact.getText().isEmpty() || cbTypeService.getValue() == null) {
            showAlert(AlertType.ERROR, "Formulaire incomplet", "Tous les champs doivent être remplis.");
            return;
        }
        if (!tfNomFournisseur.getText().matches(REGEX_NOM)) {
            showAlert(AlertType.ERROR,SAISIE_INVALIDE ,"Le nom du fournisseur ne doit contenir que des lettres.");
            return;
        }
        if (!tfAdresse.getText().matches(REGEX_ADRESS)) {
            showAlert(AlertType.ERROR,SAISIE_INVALIDE ,"L'adresse ne doit contenir que des lettres et des chiffres.");
            return;
        }
        if (!tfContact.getText().matches(REGEX_NUMBER)) {
            showAlert(AlertType.ERROR,SAISIE_INVALIDE ,"Le contact doit être composé de 8 chiffres et ne doit pas commencer par 0.");
            return;
        }
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNomFournisseur(tfNomFournisseur.getText());
        fournisseur.setAdresse(tfAdresse.getText());
        fournisseur.setContact(tfContact.getText());
        fournisseur.setTypeService(cbTypeService.getValue());

        serviceFournisseur.ajouterFournisseur(fournisseur);
        clearFields();
    }

    @FXML
    public void afficherFournisseurs() {
        actionButton("fournisseur_list_view.fxml", afficherButton);
    }

    @FXML
    public void backHome() {
        actionButton("home_view.fxml", homepageButton);
    }

    private void clearFields() {
        tfNomFournisseur.clear();
        tfAdresse.clear();
        tfContact.clear();
        cbTypeService.setValue(null);
    }

}
