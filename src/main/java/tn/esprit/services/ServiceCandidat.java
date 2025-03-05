package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Candidat;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCandidat implements IService<Candidat> {
    protected Connection cnx;

    public ServiceCandidat() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Candidat candidat) {
        // Validate user_type
        String userType = candidat.getUserType();
        if (!userType.equals("Candidat")) {
            System.out.println("Erreur : Le type d'utilisateur doit être 'Candidat' !");
            return;
        }

        // Insert into the 'users' table first
        String userQuery = "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, candidat.getNumTel());
            pst.setInt(2, candidat.getJoursOuvrables());
            pst.setString(3, candidat.getNom());
            pst.setString(4, candidat.getPrenom());
            pst.setString(5, candidat.getAddress());
            pst.setString(6, candidat.getEmail());
            pst.setString(7, candidat.getGender());
            pst.setDate(8, candidat.getDateDeNaissance());
            pst.setString(9, userType);
            pst.setString(10, candidat.getPassword());
            pst.setString(11, candidat.getImage());

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    candidat.setIdCandidat(userId);  // Store the generated user ID as Candidat ID
                    System.out.println("Utilisateur Candidat ajouté avec succès !");
                }
            }

            // Now insert the Candidat-specific information into the 'candidat' table
            String candidatQuery = "INSERT INTO candidat (user_id, status) VALUES (?, ?)";
            try (PreparedStatement pstCandidat = cnx.prepareStatement(candidatQuery)) {
                pstCandidat.setInt(1, candidat.getIdCandidat());
                pstCandidat.setString(2, candidat.getStatus());
                pstCandidat.executeUpdate();
                System.out.println("Candidat spécifique ajouté avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du Candidat : " + e.getMessage());
        }
    }

    @Override
    public List<Candidat> getAll() {
        List<Candidat> candidats = new ArrayList<>();
        String query = "SELECT * FROM users WHERE user_type = 'Candidat'";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Candidat candidat = new Candidat(
                        rs.getInt("id"),
                        rs.getString("numTel"),
                        rs.getInt("joursOuvrables"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getDate("dateDeNaissance"),
                        rs.getString("user_type"),
                        rs.getString("password"),
                        rs.getString("image"),
                        rs.getInt("idCandidat"),  // Assuming idCandidat is stored in the 'users' table
                        rs.getString("status")
                );
                candidats.add(candidat);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des Candidats : " + e.getMessage());
        }

        return candidats;
    }

    @Override
    public void update(Candidat candidat) {
        // Update user information
        String query = "UPDATE users SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, dateDeNaissance=?, user_type=?, password=?, image=? WHERE id=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, candidat.getNumTel());
            pst.setInt(2, candidat.getJoursOuvrables());
            pst.setString(3, candidat.getNom());
            pst.setString(4, candidat.getPrenom());
            pst.setString(5, candidat.getAddress());
            pst.setString(6, candidat.getEmail());
            pst.setString(7, candidat.getGender());
            pst.setDate(8, candidat.getDateDeNaissance());
            pst.setString(9, candidat.getUserType());
            pst.setString(10, candidat.getPassword());
            pst.setString(11, candidat.getImage());
            pst.setInt(12, candidat.getIdCandidat());

            pst.executeUpdate();

            // Update Candidat-specific information
            String updateCandidatQuery = "UPDATE candidat SET status=? WHERE user_id=?";
            try (PreparedStatement pstCandidat = cnx.prepareStatement(updateCandidatQuery)) {
                pstCandidat.setString(1, candidat.getStatus());
                pstCandidat.setInt(2, candidat.getIdCandidat());
                pstCandidat.executeUpdate();
                System.out.println("Candidat mis à jour avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du Candidat : " + e.getMessage());
        }
    }

    @Override
    public void delete(Candidat candidat) {
        // Delete from 'candidat' table
        String deleteCandidatQuery = "DELETE FROM candidat WHERE user_id=?";
        String deleteUserQuery = "DELETE FROM users WHERE id=?";

        try (PreparedStatement pstCandidat = cnx.prepareStatement(deleteCandidatQuery);
             PreparedStatement pstUser = cnx.prepareStatement(deleteUserQuery)) {

            pstCandidat.setInt(1, candidat.getIdCandidat());
            pstCandidat.executeUpdate();

            pstUser.setInt(1, candidat.getIdCandidat());
            pstUser.executeUpdate();

            System.out.println("Candidat supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du Candidat : " + e.getMessage());
        }
    }
}
