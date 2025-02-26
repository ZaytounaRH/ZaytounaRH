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
        // First, add the user to the users table
        String userQuery = "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstUser = cnx.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstUser.setString(1, candidat.getNumTel());
            pstUser.setInt(2, candidat.getJoursOuvrables());
            pstUser.setString(3, candidat.getNom());
            pstUser.setString(4, candidat.getPrenom());
            pstUser.setString(5, candidat.getAddress());
            pstUser.setString(6, candidat.getEmail());
            pstUser.setString(7, candidat.getGender());
            pstUser.setDate(8, candidat.getDateDeNaissance());
            pstUser.setString(9, "Candidat");  // Ensure it's "Candidat"
            pstUser.setString(10, candidat.getPassword());

            pstUser.executeUpdate();

            // Get the generated user_id
            ResultSet generatedKeys = pstUser.getGeneratedKeys();
            int userId = 0;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }

            // Now insert into the candidat table
            String candidatQuery = "INSERT INTO candidat (user_id, status) VALUES (?, ?)";
            try (PreparedStatement pstCandidat = cnx.prepareStatement(candidatQuery)) {
                pstCandidat.setInt(1, userId); // Use the generated user_id
                pstCandidat.setString(2, candidat.getStatus()); // Ensure status is valid

                pstCandidat.executeUpdate();

                System.out.println("Candidat ajouté avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du Candidat : " + e.getMessage());
        }
    }

    @Override
    public List<Candidat> getAll() {
        List<Candidat> candidats = new ArrayList<>();
        String query = "SELECT * FROM users WHERE user_type='Candidat'";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Candidat candidat = new Candidat(
                        rs.getInt("idCandidat"),
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
        String query = "UPDATE candidat SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, dateDeNaissance=?, user_type=?, password=?, status=? WHERE idCandidat=?";

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
            pst.setString(11, candidat.getStatus());
            pst.setInt(12, candidat.getCandidat_id());

            pst.executeUpdate();
            System.out.println("Candidat mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du Candidat : " + e.getMessage());
        }
    }

    @Override
    public void delete(Candidat candidat) {
        String query = "DELETE FROM candidat WHERE idCandidat=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, candidat.getCandidat_id());

            pst.executeUpdate();
            System.out.println("Candidat supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du Candidat : " + e.getMessage());
        }
    }
}
