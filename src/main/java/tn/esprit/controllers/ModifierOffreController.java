package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.RH;
import tn.esprit.services.ServiceOffreEmploi;
import java.time.LocalDate;

public class ModifierOffreController {

    @FXML
    private TextField tfTitre;
    @FXML
    private TextField tfDescription;
    @FXML
    private TextField tfSalaire;
    @FXML
    private DatePicker dpDatePublication;
    @FXML
    private ComboBox<OffreEmploi.StatutOffre> cbStatut;
    @FXML
    private ComboBox<RH> cbResponsableRH;

    private OffreEmploi offre;

    public void initData(OffreEmploi offre) {
        this.offre = offre;
        tfTitre.setText(offre.getTitreOffre());
        tfDescription.setText(offre.getDescription());
        tfSalaire.setText(String.valueOf(offre.getSalaire()));

        // Vérification si la date est présente dans le DatePicker avant de l'utiliser
        LocalDate localDate = dpDatePublication.getValue();
        if (localDate != null) {
            offre.setDatePublication(java.sql.Date.valueOf(localDate));
        } else {
            // Gérer le cas où la date est nulle, par exemple en affichant un message d'erreur ou en attribuant une valeur par défaut
            System.out.println("Date de publication non définie");
        }

        cbStatut.setValue(offre.getStatut());
        cbResponsableRH.setValue(offre.getRh());
    }
    @FXML
    public void enregistrerOffre() {
        // Vérification des valeurs saisies et mise à jour de l'objet offre
        if (tfTitre.getText() != null && !tfTitre.getText().isEmpty()) {
            offre.setTitreOffre(tfTitre.getText());
        }

        if (tfDescription.getText() != null && !tfDescription.getText().isEmpty()) {
            offre.setDescription(tfDescription.getText());
        }

        if (tfSalaire.getText() != null && !tfSalaire.getText().isEmpty()) {
            try {
                offre.setSalaire(Double.valueOf(tfSalaire.getText()));
            } catch (NumberFormatException e) {
                // Gérer le cas où le salaire n'est pas un nombre valide
                System.out.println("Salaire invalide");
            }
        }

        // Mise à jour de la date de publication
        LocalDate localDate = dpDatePublication.getValue();
        if (localDate != null) {
            offre.setDatePublication(java.sql.Date.valueOf(localDate));
        } else {
            // Gérer le cas où la date est nulle
            System.out.println("Date de publication non définie");
        }

        // Mise à jour du statut et du responsable RH
        if (cbStatut.getValue() != null) {
            offre.setStatut(cbStatut.getValue());
        }

        if (cbResponsableRH.getValue() != null) {
            offre.setRh(cbResponsableRH.getValue());
        }

        // Enregistrement de l'offre modifiée (dans le service, base de données, etc.)
        ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
        serviceOffreEmploi.update(offre); // Méthode hypothétique dans votre service

        // Afficher un message ou fermer la fenêtre
        System.out.println("Offre modifiée avec succès");
    }


    @FXML
    public void annulerModification() {
        // Handle the cancellation action
    }
}
