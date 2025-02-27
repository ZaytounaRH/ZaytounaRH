package tn.esprit.services;

import tn.esprit.interfaces.Iservice;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.models.Entretien;
import tn.esprit.models.RH;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;
import tn.esprit.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffreEmploi implements Iservice<OffreEmploi> {
    private Connection cnx;

    public ServiceOffreEmploi() {
        cnx = MyDatabase.getInstance().getCnx();
    }


    @Override
    public void add(OffreEmploi offreEmploi) {
        // Récupérer le RH actuellement connecté via la session
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérifier si l'utilisateur connecté est bien le RH attendu
        if (currentUser == null || !(currentUser instanceof RH)) {
            System.out.println("Erreur : Seul le RH connecté peut ajouter une offre d'emploi !");
            return;
        }

        // Associer automatiquement le RH connecté à l'offre d'emploi
        RH currentRH = (RH) currentUser;
        offreEmploi.setRh(currentRH);

        // Préparer la requête SQL avec tous les attributs de l'offre d'emploi
        String qry = "INSERT INTO `OffreEmploi`(`titreOffre`, `description`, `datePublication`, `salaire`, `statut`, `idRH`) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, offreEmploi.getTitreOffre());
            pstm.setString(2, offreEmploi.getDescription());
            pstm.setDate(3, new java.sql.Date(offreEmploi.getDatePublication().getTime()));
            pstm.setDouble(4, offreEmploi.getSalaire());
            pstm.setString(5, offreEmploi.getStatut().name());
            pstm.setInt(6, currentRH.getIdRH()); // Utiliser l'ID du RH connecté

            // Exécuter l'insertion dans la base de données
            pstm.executeUpdate();

            // Récupérer l'ID généré pour l'offre d'emploi
            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    int idOffre = rs.getInt(1);
                    offreEmploi.setIdOffre(idOffre);
                }
            }

            // Ajouter les entretiens associés à l'offre d'emploi (si présents)
            if (offreEmploi.getEntretiens() != null) {
                ServiceEntretien serviceEntretien = new ServiceEntretien();
                for (Entretien entretien : offreEmploi.getEntretiens()) {
                    entretien.setOffreEmploi(offreEmploi); // Associer l'offre à chaque entretien
                    serviceEntretien.add(entretien); // Ajouter l'entretien en base
                }
            }

            System.out.println("Offre d'emploi ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'offre d'emploi : " + e.getMessage());
        }
    }


    @Override
    public List<OffreEmploi> getAll() {
        List<OffreEmploi> offres = new ArrayList<>();
        return offres;

    }


    @Override
    public void update(OffreEmploi offreEmploi) {}
    @Override
    public void remove(int idOffre) {}

}