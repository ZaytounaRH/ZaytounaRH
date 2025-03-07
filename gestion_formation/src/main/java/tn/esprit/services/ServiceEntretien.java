package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Entretien;
import tn.esprit.models.User;
import tn.esprit.models.RH;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.Candidat;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.utils.SessionManager;
public class ServiceEntretien implements IService<Entretien>{
    private Connection cnx;


    public ServiceEntretien(Connection cnx) {
        this.cnx = cnx;
    }

    @Override
    public void add(Entretien entretien) {
        SessionManager sessionManager = SessionManager.getInstance();

        // Vérifier si l'utilisateur est un Candidat
        if (!sessionManager.isCandidat()) {
            System.out.println("Erreur : L'utilisateur n'est pas un Candidat");
            return;  // Retourner immédiatement si l'utilisateur n'est pas un Candidat
        }

        // Récupérer l'ID du Candidat depuis la session
        int candidatId = sessionManager.getCurrentCandidatId();
        System.out.println("ID Candidat récupéré depuis SessionManager : " + candidatId);

        // Vérifier la connexion à la base de données via MyDatabase
        try {
            // Récupérer la connexion via MyDatabase (qui gère la reconnexion automatiquement)
            Connection cnx = MyDatabase.getInstance().getCnx();

            // Vérifier si la connexion est toujours valide après la récupération
            if (cnx == null || cnx.isClosed()) {
                System.out.println("Erreur : Connexion toujours fermée ou invalide.");
                return;
            }

            // Vérifier que la date de l'entretien est valide
            if (entretien.getDateEntretien() == null) {
                System.out.println("Date de l'entretien invalide, l'ajout de l'entretien est annulé.");
                return;
            }

            // Vérifier que l'heure de l'entretien est valide
            if (entretien.getHeureEntretien() == null) {
                System.out.println("Heure de l'entretien invalide, l'ajout de l'entretien est annulé.");
                return;
            }

            System.out.println("ID Candidat utilisé pour l'insertion : " + candidatId);

            // Requête d'insertion de l'entretien
            String qry = "INSERT INTO Entretien(dateEntretien, heureEntretien, typeEntretien, statut, commentaire, idOffre, candidat_id) VALUES (?,?,?,?,?,?,?)";

            try (PreparedStatement stmt = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
                // Remplir la requête avec les valeurs de l'entretien
                stmt.setDate(1, java.sql.Date.valueOf(entretien.getDateEntretien())); // dateEntretien
                stmt.setTime(2, java.sql.Time.valueOf(entretien.getHeureEntretien())); // heureEntretien
                stmt.setString(3, entretien.getTypeEntretien().toString()); // typeEntretien
                stmt.setString(4, entretien.getStatut().toString()); // statut
                stmt.setString(5, entretien.getCommentaire() != null ? entretien.getCommentaire() : ""); // commentaire (valeur par défaut si null)
                stmt.setInt(6, entretien.getOffreEmploi().getIdOffre()); // idOffreEmploi
                stmt.setInt(7, candidatId); // idCandidat

                // Exécuter la mise à jour
                stmt.executeUpdate();

                // Récupérer l'ID généré de l'entretien
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idEntretien = rs.getInt(1);  // Récupérer l'ID généré
                        entretien.setIdEntretien(idEntretien);  // Assigner l'ID à l'objet
                    }
                }

                System.out.println("Entretien ajouté avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'entretien : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Entretien> getAll() {
        // Récupérer l'utilisateur actuellement connecté via SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérifier si l'utilisateur est un RH
        if (currentUser == null || !(currentUser instanceof RH)) {
            System.out.println("Erreur : Seuls les RH peuvent afficher la liste des entretiens !");
            return new ArrayList<>();  // Retourner une liste vide si ce n'est pas un RH
        }

        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT e.*, o.*, c.*, u.nom AS nom_candidat, u.prenom AS prenom_candidat FROM entretien e " +
                "JOIN offreemploi o ON e.idOffre = o.idOffre " +
                "JOIN candidat c ON e.candidat_id = c.candidat_id " +
                "JOIN users u ON c.user_id = u.id";

        try (PreparedStatement stmt = cnx.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Créer l'objet Entretien
                Entretien entretien = new Entretien();
                entretien.setIdEntretien(rs.getInt("idEntretien"));
                entretien.setDateEntretien(rs.getDate("dateEntretien").toLocalDate());
                entretien.setHeureEntretien(rs.getTime("heureEntretien").toLocalTime());
                entretien.setTypeEntretien(Entretien.TypeEntretien.valueOf(rs.getString("typeEntretien")));
                entretien.setStatut(Entretien.StatutEntretien.valueOf(rs.getString("statut")));
                entretien.setCommentaire(rs.getString("commentaire"));

                // Créer l'objet OffreEmploi
                OffreEmploi offreEmploi = new OffreEmploi();
                offreEmploi.setIdOffre(rs.getInt("idOffre"));
                offreEmploi.setTitreOffre(rs.getString("titreOffre"));
                entretien.setOffreEmploi(offreEmploi);

                // Créer l'objet Candidat
                Candidat candidat = new Candidat();
                candidat.setCandidat_id(rs.getInt("candidat_id"));
                candidat.setNom(rs.getString("nom_candidat"));
                candidat.setPrenom(rs.getString("prenom_candidat"));
                entretien.setCandidat(candidat);

                // Ajouter l'entretien à la liste
                entretiens.add(entretien);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des entretiens : " + e.getMessage());
        }
        return entretiens;
    }

    @Override
    public void update(Entretien entretien) {
        // Récupérer l'utilisateur actuellement connecté via SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérifier si l'utilisateur est un RH
        if (currentUser == null || !(currentUser instanceof RH)) {
            System.out.println("Erreur : Seuls les RH peuvent modifier un entretien !");
            return;  // Interrompre l'exécution si ce n'est pas un RH
        }

        // Vérifier que l'entretien existe déjà dans la base de données avant de le mettre à jour
        if (entretien.getIdEntretien() == 0) {
            System.out.println("Erreur : L'ID de l'entretien est invalide !");
            return;
        }

        // Préparer la requête SQL pour vérifier si l'entretien existe
        String checkQuery = "SELECT idEntretien FROM entretien WHERE idEntretien = ?";
        try (PreparedStatement stmtCheck = cnx.prepareStatement(checkQuery)) {
            stmtCheck.setInt(1, entretien.getIdEntretien());
            ResultSet rs = stmtCheck.executeQuery();

            if (!rs.next()) {
                System.out.println("Erreur : L'entretien avec l'ID " + entretien.getIdEntretien() + " n'existe pas.");
                return;  // Si l'entretien n'existe pas, on arrête ici
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence de l'entretien : " + e.getMessage());
            return;
        }

        // Préparer la requête SQL pour mettre à jour l'entretien dans la base de données
        String updateQuery = "UPDATE entretien SET dateEntretien = ?, heureEntretien = ?, typeEntretien = ?, statut = ?, commentaire = ?, idOffre = ?, candidat_id = ? WHERE idEntretien = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(updateQuery)) {
            // Remplir la requête avec les valeurs de l'entretien
            stmt.setDate(1, java.sql.Date.valueOf(entretien.getDateEntretien())); // dateEntretien
            stmt.setTime(2, java.sql.Time.valueOf(entretien.getHeureEntretien())); // heureEntretien
            stmt.setString(3, entretien.getTypeEntretien().toString()); // typeEntretien
            stmt.setString(4, entretien.getStatut().toString()); // statut
            stmt.setString(5, entretien.getCommentaire()); // commentaire
            stmt.setInt(6, entretien.getOffreEmploi().getIdOffre()); // idOffreEmploi
            stmt.setInt(7, entretien.getCandidat().getCandidat_id()); // idCandidat
            stmt.setInt(8, entretien.getIdEntretien()); // idEntretien (pour la condition WHERE)

            // Exécuter la mise à jour
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Entretien mis à jour avec succès !");
            } else {
                System.out.println("Erreur : Aucune mise à jour effectuée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'entretien : " + e.getMessage());
        }
    }

    @Override
    public void delete(Entretien entretien) {
        // Récupérer l'utilisateur actuellement connecté via SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérifier si l'utilisateur est un RH
        if (currentUser == null || !(currentUser instanceof RH)) {
            System.out.println("Erreur : Seuls les RH peuvent supprimer un entretien !");
            return;  // Interrompre l'exécution si ce n'est pas un RH
        }

        // Vérifier si l'entretien existe
        String checkQuery = "SELECT idEntretien FROM entretien WHERE idEntretien = ?";
        try (PreparedStatement stmtCheck = cnx.prepareStatement(checkQuery)) {
            stmtCheck.setInt(1, entretien.getIdEntretien());
            ResultSet rs = stmtCheck.executeQuery();

            if (!rs.next()) {
                System.out.println("Erreur : L'entretien avec l'ID " + entretien.getIdEntretien() + " n'existe pas.");
                return;  // Si l'entretien n'existe pas, on arrête ici
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence de l'entretien : " + e.getMessage());
            return;
        }

        // Préparer la requête SQL pour supprimer l'entretien
        String deleteQuery = "DELETE FROM entretien WHERE idEntretien = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(deleteQuery)) {
            stmt.setInt(1, entretien.getIdEntretien());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Entretien supprimé avec succès !");
            } else {
                System.out.println("Erreur : Aucune suppression effectuée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'entretien : " + e.getMessage());
        }
    }

    public List<Entretien> getAllByOffre(int idOffre) {
        List<Entretien> entretiens = new ArrayList<>();
        String qry = "SELECT e.idEntretien, e.dateEntretien, e.heureEntretien ,e.statut,e.commentaire, e.candidat_id, u.nom AS candidat_nom, u.prenom AS candidat_prenom\n" +
                "FROM Entretien e\n" +
                "JOIN Candidat c ON e.candidat_id = c.candidat_id\n" +
                "JOIN Users u ON c.user_id = u.id\n" +
                "WHERE e.idOffre = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(qry)) {
            stmt.setInt(1, idOffre);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Entretien entretien = new Entretien();
                entretien.setIdEntretien(rs.getInt("idEntretien"));
                entretien.setDateEntretien(rs.getDate("dateEntretien").toLocalDate());
                entretien.setHeureEntretien(rs.getTime("heureEntretien").toLocalTime());
                entretien.setStatut(Entretien.StatutEntretien.valueOf(rs.getString("statut")));
                entretien.setCommentaire(rs.getString("commentaire"));

                // Ajouter l'entretien à la liste
                entretiens.add(entretien);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des entretiens : " + e.getMessage());
        }

        return entretiens;
    }
}
