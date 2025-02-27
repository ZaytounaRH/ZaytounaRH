package tn.esprit.services;

import tn.esprit.interfaces.Iservice;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.models.Entretien;
import tn.esprit.models.RH;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;
import tn.esprit.models.User;
import tn.esprit.models.Candidat;
import java.sql.*;
import java.sql.Date;
import java.util.*;

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
      /*  String qry = "SELECT o.idOffre, o.titreOffre, o.description, o.datePublication, o.salaire, o.statut, o.idRH, u.nom AS rh_nom " +
                "FROM offreemploi o " +
                "JOIN RH r ON o.idRH = r.rh_id " +  // Associe la table OffreEmploi à RH
                "JOIN users u ON r.rh_id = u.id " +  // Remplace r.rh_id par r.idUser (si c'est l'ID du RH dans la table users)
                "WHERE u.nom = Zheni";  // Filtrer par le nom du RH connecté
        //String query = "SELECT e.*, o.*, c.*, u.nom AS nom_rh, u.prenom AS prenom_rh FROM offreemploi o " + "JOIN offreemploi o ON e.idOffre = o.idOffre " + "JOIN candidat c ON e.candidat_id = c.candidat_id " + "JOIN users u ON c.user_id = u.id";  // Assurez-vous d'ajouter cette jointure pour récupérer les données de User
        try (PreparedStatement pstmt = cnx.prepareStatement(qry)) {
            pstmt.setString(1, "NomRH");  // Remplacer "NomRH" par la variable contenant le nom du RH connecté

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OffreEmploi offre = new OffreEmploi();
                    offre.setIdOffre(rs.getInt("idOffre"));
                    offre.setTitreOffre(rs.getString("titreOffre"));
                    offre.setDescription(rs.getString("description"));
                    offre.setDatePublication(rs.getDate("datePublication"));
                    offre.setSalaire(rs.getDouble("salaire"));

                    String statutString = rs.getString("statut").toUpperCase();
                    try {
                        StatutOffre statutEnum = StatutOffre.valueOf(statutString);
                        offre.setStatut(statutEnum);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Statut invalide trouvé : " + statutString + ". Statut par défaut 'ENCOURS' utilisé.");
                        offre.setStatut(StatutOffre.ENCOURS);
                    }

                    // Charger RH
                    int rhId = rs.getInt("idRH");
                    RH rh = null;
                    if (rhId > 0) {
                        String rhQry = "SELECT r.nom AS rh_nom FROM users r WHERE r.id = ? AND r.user_type = 'RH'";
                        try (PreparedStatement pstmtRh = cnx.prepareStatement(rhQry)) {
                            pstmtRh.setInt(1, rhId);
                            try (ResultSet rsRh = pstmtRh.executeQuery()) {
                                if (rsRh.next()) {
                                    rh = new RH(rhId, rsRh.getString("rh_nom"));
                                }
                            }
                        }
                    }
                    offre.setRh(rh);

                    // Charger les entretiens
                    ServiceEntretien serviceEntretien = new ServiceEntretien();
                    List<Entretien> entretiens = serviceEntretien.getAllByOffre(offre.getIdOffre());
                    offre.setEntretiens(entretiens != null ? entretiens : new ArrayList<>());

                    offres.add(offre);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des offres : " + e.getMessage());
        }

        return offres;
    }*/

    @Override
    public void update(OffreEmploi offreEmploi) {
        // Récupérer l'utilisateur actuellement connecté via SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérifier si l'utilisateur est un RH
        if (currentUser == null || !(currentUser instanceof RH)) {
            System.out.println("Erreur : Seuls les RH peuvent modifier une offre d'emploi !");
            return;  // Interrompre l'exécution si ce n'est pas un RH
        }

        // Vérifier que l'ID de l'offre est valide
        if (offreEmploi.getIdOffre() == 0) {
            System.out.println("Erreur : L'ID de l'offre d'emploi est invalide !");
            return;
        }

        // Préparer la requête SQL pour vérifier si l'offre d'emploi existe
        String checkQuery = "SELECT idOffre FROM OffreEmploi WHERE idOffre = ?";
        try (PreparedStatement stmtCheck = cnx.prepareStatement(checkQuery)) {
            stmtCheck.setInt(1, offreEmploi.getIdOffre());
            ResultSet rs = stmtCheck.executeQuery();

            if (!rs.next()) {
                System.out.println("Erreur : L'offre d'emploi avec l'ID " + offreEmploi.getIdOffre() + " n'existe pas.");
                return;  // Si l'offre n'existe pas, on arrête ici
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence de l'offre d'emploi : " + e.getMessage());
            return;
        }

        // Préparer la requête SQL pour mettre à jour l'offre d'emploi dans la base de données
        String updateQuery = "UPDATE OffreEmploi SET titreOffre = ?, description = ?, datePublication = ?, salaire = ?, statut = ?, rh_id = ? WHERE idOffre = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(updateQuery)) {
            // Remplir la requête avec les valeurs de l'offre d'emploi
            stmt.setString(1, offreEmploi.getTitreOffre()); // titreOffre
            stmt.setString(2, offreEmploi.getDescription()); // description
            stmt.setDate(3, new java.sql.Date(offreEmploi.getDatePublication().getTime()));
            stmt.setDouble(4, offreEmploi.getSalaire()); // salaire
            stmt.setString(5, offreEmploi.getStatut().toString()); // statut
            stmt.setInt(6, offreEmploi.getRh() != null ? offreEmploi.getRh().getIdRH() : 0); // rh_id (0 si RH non défini)
            stmt.setInt(7, offreEmploi.getIdOffre()); // idOffre (pour la condition WHERE)

            // Exécuter la mise à jour
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Offre d'emploi mise à jour avec succès !");
            } else {
                System.out.println("Erreur : Aucune mise à jour effectuée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'offre d'emploi : " + e.getMessage());
        }
    }

    @Override
    public void remove(int idOffre) {}

}