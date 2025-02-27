package tn.esprit.services;

import tn.esprit.interfaces.Iservice;
import tn.esprit.models.Entretien;
import tn.esprit.models.User;
import tn.esprit.models.RH;
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
        String query = "SELECT e.*, o.*, c.*, u.nom AS nom_candidat, u.prenom AS prenom_candidat FROM entretien e " + "JOIN offreemploi o ON e.idOffre = o.idOffre " + "JOIN candidat c ON e.candidat_id = c.candidat_id " + "JOIN users u ON c.user_id = u.id";  // Assurez-vous d'ajouter cette jointure pour récupérer les données de User

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
                candidat.setNom(rs.getString("nom_candidat")); // Utilisation de l'alias 'nom_candidat'
                candidat.setPrenom(rs.getString("prenom_candidat")); // Utilisation de l'alias 'prenom_candidat'
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
    public void remove(int id) {
        // Récupérer l'utilisateur actuellement connecté via SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérifier si l'utilisateur est un RH
        if (currentUser == null || !(currentUser instanceof RH)) {
            System.out.println("Erreur : Seuls les RH peuvent supprimer un entretien !");
            return;  // Interrompre l'exécution si ce n'est pas un RH
        }

        // Préparer la requête SQL pour vérifier si l'entretien existe
        String checkQuery = "SELECT idEntretien FROM entretien WHERE idEntretien = ?";
        try (PreparedStatement stmtCheck = cnx.prepareStatement(checkQuery)) {
            stmtCheck.setInt(1, id);
            ResultSet rs = stmtCheck.executeQuery();

            if (!rs.next()) {
                System.out.println("Erreur : L'entretien avec l'ID " + id + " n'existe pas.");
                return;  // Si l'entretien n'existe pas, on arrête ici
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence de l'entretien : " + e.getMessage());
            return;
        }

        // Préparer la requête SQL pour supprimer l'entretien
        String deleteQuery = "DELETE FROM entretien WHERE idEntretien = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(deleteQuery)) {
            stmt.setInt(1, id);

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


}




