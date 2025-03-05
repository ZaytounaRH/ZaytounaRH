package tn.esprit.services;

import tn.esprit.models.*;
import tn.esprit.utils.MyDatabase;
import tn.esprit.interfaces.IService;
import tn.esprit.utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffreEmploi implements IService<OffreEmploi> {

    private Connection cnx;

    public ServiceOffreEmploi() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(OffreEmploi offreEmploi) {
        SessionManager sessionManager = SessionManager.getInstance();

        // Vérifier si l'utilisateur est un RH
        if (!sessionManager.isRH()) {
            System.out.println("Erreur : L'utilisateur n'est pas un RH");
            return;  // Retourner immédiatement si l'utilisateur n'est pas un RH
        }

        // Récupérer l'ID du RH depuis la session
        int idRH = sessionManager.getCurrentRHId();
        System.out.println("ID RH récupéré depuis SessionManager : " + idRH);

        // Vérifier la connexion à la base de données
        try {
            if (cnx == null || cnx.isClosed()) {
                System.out.println("Erreur : Connexion fermée ou invalide.");
                return;
            }

            // Vérifier que la date de publication est valide
            if (offreEmploi.getDatePublication() == null) {
                System.out.println("Date invalide, l'ajout de l'offre d'emploi est annulé.");
                return;
            }

            System.out.println("ID RH utilisé pour l'insertion : " + idRH);

            // Requête d'insertion de l'offre d'emploi
            String qry = "INSERT INTO OffreEmploi(titreOffre, description, datePublication, salaire, statut, idRH) VALUES (?,?,?,?,?,?)";

            try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
                // Paramétrage de la requête avec les valeurs de l'offre
                pstm.setString(1, offreEmploi.getTitreOffre());
                pstm.setString(2, offreEmploi.getDescription());
                pstm.setDate(3, new java.sql.Date(offreEmploi.getDatePublication().getTime()));
                pstm.setDouble(4, offreEmploi.getSalaire());
                pstm.setString(5, offreEmploi.getStatut().name());
                pstm.setInt(6, idRH);

                // Exécution de la requête d'insertion
                pstm.executeUpdate();

                // Récupérer l'ID généré de l'offre d'emploi
                try (ResultSet rs = pstm.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idOffre = rs.getInt(1);  // Récupérer l'ID généré
                        offreEmploi.setIdOffre(idOffre);  // Assigner l'ID à l'objet
                    }
                }

                // Vérifier et ajouter les entretiens s'il y en a
                if (offreEmploi.getEntretiens() != null && !offreEmploi.getEntretiens().isEmpty()) {
                    ServiceEntretien serviceEntretien = new ServiceEntretien(cnx);
                    for (Entretien entretien : offreEmploi.getEntretiens()) {
                        entretien.setOffreEmploi(offreEmploi); // Associer l'entretien à l'offre
                        serviceEntretien.add(entretien);  // Ajouter chaque entretien
                    }
                }

                System.out.println("Offre d'emploi ajoutée avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'offre d'emploi : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<OffreEmploi> getAll() {
        List<OffreEmploi> offres = new ArrayList<>();
        SessionManager sessionManager = SessionManager.getInstance();

        // Vérifier si l'utilisateur est un RH
        if (!sessionManager.isRH()) {
            System.out.println("Erreur : L'utilisateur n'est pas un RH");
            return offres;  // Retourner immédiatement si l'utilisateur n'est pas un RH
        }

        // Récupérer l'ID du RH depuis la session
        int idRH = sessionManager.getCurrentRHId();
        System.out.println("ID RH récupéré depuis SessionManager : " + idRH);

        // Vérifier la connexion à la base de données
      /*  // Récupérer le RH actuellement connecté
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null || !(currentUser instanceof RH)) {
            System.out.println("❌ Accès refusé : Seul un RH peut afficher la liste des offres.");
            return offres;
        }*/

        // Correction de la requête SQL (enlevant WHERE r.user_type = 'RH')
        String qry = """
        SELECT o.idOffre, o.titreOffre, o.description, o.datePublication, o.salaire, o.statut,
               r.id AS idRH, r.nom AS rh_nom
        FROM offreemploi o
        LEFT JOIN users r ON o.idRH = r.id
    """;

        System.out.println("🔍 Exécution de la requête SQL : " + qry);

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                OffreEmploi offre = new OffreEmploi();
                offre.setIdOffre(rs.getInt("idOffre"));
                offre.setTitreOffre(rs.getString("titreOffre"));
                offre.setDescription(rs.getString("description"));
                offre.setDatePublication(rs.getDate("datePublication"));
                offre.setSalaire(rs.getDouble("salaire"));

                // Gestion du statut
                String statutString = rs.getString("statut").toUpperCase();
                try {
                    OffreEmploi.StatutOffre statutEnum = OffreEmploi.StatutOffre.valueOf(statutString);
                    offre.setStatut(statutEnum);
                } catch (IllegalArgumentException e) {
                    System.out.println("⚠️ Statut invalide trouvé : " + statutString + ". Utilisation du statut par défaut 'ENCOURS'.");
                    offre.setStatut(OffreEmploi.StatutOffre.ENCOURS);
                }

                // Charger le RH
                RH rh = null;
                int rhId = rs.getInt("idRH");
                if (!rs.wasNull()) {
                    rh = new RH();
                    rh.setId(rhId);
                    rh.setNom(rs.getString("rh_nom"));
                }
                offre.setRh(rh);

                // Charger les entretiens
                ServiceEntretien serviceEntretien = new ServiceEntretien(cnx);
                List<Entretien> entretiens = serviceEntretien.getAllByOffre(offre.getIdOffre());
                offre.setEntretiens(entretiens != null ? entretiens : new ArrayList<>());

                offres.add(offre);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des offres : " + e.getMessage());
        }

        System.out.println("✅ Nombre d'offres récupérées : " + offres.size());
        for (OffreEmploi offre : offres) {
            System.out.println(offre);
        }

        return offres;
    }



    @Override
    public void update(OffreEmploi offreEmploi) {
        SessionManager sessionManager = SessionManager.getInstance();

        // Vérifier si l'utilisateur est un RH
        if (!sessionManager.isRH()) {
            System.out.println("Erreur : L'utilisateur n'est pas un RH");
            return;  // Retourner immédiatement si l'utilisateur n'est pas un RH
        }

        // Récupérer l'ID du RH depuis la session
        int idRH = sessionManager.getCurrentRHId();
        System.out.println("ID RH récupéré depuis SessionManager : " + idRH);


        try {
            String query = "UPDATE OffreEmploi SET titreOffre = ?, description = ?, datePublication = ?, salaire = ?, statut = ? WHERE idOffre = ?";
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setString(1, offreEmploi.getTitreOffre());
            pst.setString(2, offreEmploi.getDescription());
            pst.setDate(3, new java.sql.Date(offreEmploi.getDatePublication().getTime()));
            pst.setDouble(4, offreEmploi.getSalaire());
            pst.setString(5, offreEmploi.getStatut().name());
            pst.setInt(6, offreEmploi.getIdOffre());

            pst.executeUpdate();
            System.out.println("Offre d'emploi mise à jour avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void delete(OffreEmploi offreEmploi) {
        SessionManager sessionManager = SessionManager.getInstance();

        // Vérifier si l'utilisateur est un RH
        if (!sessionManager.isRH()) {
            System.out.println("Erreur : L'utilisateur n'est pas un RH");
            return;  // Retourner immédiatement si l'utilisateur n'est pas un RH
        }

        // Récupérer l'ID du RH depuis la session
        int idRH = sessionManager.getCurrentRHId();
        System.out.println("ID RH récupéré depuis SessionManager : " + idRH);


        try {
            String query = "DELETE FROM OffreEmploi WHERE idOffre = ?";
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setInt(1, offreEmploi.getIdOffre());
            pst.executeUpdate();
            System.out.println("Offre d'emploi supprimée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //////////////////////////////////////////////////////////////////////////::
    public List<Entretien> getEntretienByOffre(int idOffre) {
        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT e.idEntretien, e.dateEntretien, e.heureEntretien, e.statut, e.commentaire, e.candidat_id, " +
                "u.nom AS candidat_nom, u.prenom AS candidat_prenom " +
                "FROM entretien e " +
                "JOIN candidat c ON e.candidat_id = c.candidat_id " +
                "JOIN users u ON c.user_id = u.id " +
                "WHERE e.idOffre = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idOffre);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    // Créer un objet Entretien
                    Entretien entretien = new Entretien();
                    entretien.setIdEntretien(rs.getInt("idEntretien"));
                    entretien.setDateEntretien(rs.getDate("dateEntretien").toLocalDate());
                    entretien.setHeureEntretien(rs.getTime("heureEntretien").toLocalTime());
                    entretien.setStatut(Entretien.StatutEntretien.valueOf(rs.getString("statut")));
                    entretien.setCommentaire(rs.getString("commentaire"));

                    // Charger le candidat associé à l'entretien
                    Candidat candidat = new Candidat();
                    candidat.setCandidat_id(rs.getInt("candidat_id"));
                    candidat.setNom(rs.getString("candidat_nom"));
                    candidat.setPrenom(rs.getString("candidat_prenom"));

                    // Associer le candidat à l'entretien
                    entretien.setCandidat(candidat);

                    // Ajouter l'entretien à la liste
                    entretiens.add(entretien);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Utiliser un logger dans un environnement de production
        }
        return entretiens;
    }


}

