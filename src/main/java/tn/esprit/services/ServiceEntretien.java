package tn.esprit.services;

import tn.esprit.interfaces.Iservice;
import tn.esprit.models.Entretien;
import tn.esprit.models.User;

import tn.esprit.models.OffreEmploi;
import tn.esprit.models.Candidat;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import javafx.collections.FXCollections;
import tn.esprit.utils.SessionManager;

public class ServiceEntretien implements Iservice<Entretien> {
    private Connection cnx;

    public ServiceEntretien() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Entretien entretien) {
        // Récupérer l'utilisateur actuellement connecté via SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérifier si l'utilisateur est un Candidat
        if (currentUser == null || !(currentUser instanceof Candidat)) {
            System.out.println("Erreur : Seuls les Candidats peuvent ajouter un entretien !");
            return;  // Interrompre l'exécution si ce n'est pas un Candidat
        }

        // Caster l'utilisateur en Candidat pour accéder à ses informations
        Candidat candidat = (Candidat) currentUser;

        // Définir le statut par défaut à "EN_COURS"
        entretien.setStatut(Entretien.StatutEntretien.EN_COURS);

        // Lier l'entretien au candidat
        entretien.setCandidat(candidat);


        // Préparer la requête SQL pour ajouter l'entretien dans la base de données
        String query = "INSERT INTO entretien (dateEntretien, heureEntretien, typeEntretien, statut, commentaire, idOffre, candidat_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            // Remplir la requête avec les valeurs de l'entretien
            stmt.setDate(1, java.sql.Date.valueOf(entretien.getDateEntretien())); // dateEntretien
            stmt.setTime(2, java.sql.Time.valueOf(entretien.getHeureEntretien())); // heureEntretien
            stmt.setString(3, entretien.getTypeEntretien().toString()); // typeEntretien
            stmt.setString(4, entretien.getStatut().toString()); // statut
            stmt.setString(5, entretien.getCommentaire()); // commentaire
            stmt.setInt(6, entretien.getOffreEmploi().getIdOffre()); // idOffreEmploi
            stmt.setInt(7, entretien.getCandidat().getCandidat_id()); // idCandidat

            // Exécuter la mise à jour
            stmt.executeUpdate();
            System.out.println("Entretien ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'entretien : " + e.getMessage());
        }
    }

}




