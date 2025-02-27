package tn.esprit.getionfinanciere.services;


import javafx.scene.control.Alert;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.repository.FournisseurRepository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static tn.esprit.getionfinanciere.utils.Utils.showAlert;


public class ServiceFournisseur {

    private final FournisseurRepository fournisseurRepository = new FournisseurRepository();
    private final Logger log = Logger.getLogger(ServiceFournisseur.class.getName());

    public void ajouterFournisseur(Fournisseur fournisseur) {
        fournisseurRepository.add(fournisseur);
        log.log(Level.INFO, "Fournisseur ajouté {0} ", fournisseur);  // String formatting only applied if needed
        showAlert(Alert.AlertType.INFORMATION, "Fournisseur ajouté", "Le fournisseur a été ajouté.");
    }

    public List<Fournisseur> getAll() {
        return fournisseurRepository.getAll();
    }

    public void delete(Fournisseur selectedFournisseur) {
        fournisseurRepository.delete(selectedFournisseur);
        showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le fournisseur a été supprimé.");
    }

    public void update(Fournisseur fournisseur) {
        fournisseurRepository.update(fournisseur);
        showAlert(Alert.AlertType.INFORMATION, "Mise à jour réussie", "Le fournisseur a été modifié.");
    }
}