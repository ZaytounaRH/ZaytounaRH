package tn.esprit.services;

import tn.esprit.models.Candidat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceCandidat extends ServiceUser<Candidat> {
    @Override
    public void add(Candidat candidat) {
        String qry = "INSERT INTO Users (numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance, user_type, intervieweur_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, candidat.getNumTel());
            pstm.setInt(2, candidat.getJoursOuvrables());
            pstm.setString(3, candidat.getNom());
            pstm.setString(4, candidat.getPrenom());
            pstm.setString(5, candidat.getAddress());
            pstm.setString(6, candidat.getEmail());
            pstm.setString(7, candidat.getGender());
            pstm.setString(8, candidat.getDepartment());
            pstm.setString(9, candidat.getDesignation());
            pstm.setDate(10, new java.sql.Date(candidat.getDateDeNaissance().getTime()));
            pstm.setString(11, candidat.getClass().getSimpleName()); // User type (Candidat)
            pstm.setInt(12, candidat.getIntervieweurId()); // Set the intervieweur_id

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    candidat.setId(generatedId); // Set the generated ID
                    System.out.println("Candidat ajouté avec succès. ID: " + generatedId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du candidat: " + e.getMessage());
        }
    }
    @Override
    public void update(Candidat candidat) {
        if (candidat.getId() <= 0) {
            System.out.println("Error: Candidat ID is not set for update!");
            return;
        }

        if (!isValidIntervieweurId(candidat.getIntervieweurId())) {
            System.out.println("Erreur: intervieweurId invalide.");
            return;
        }

        String qry = "UPDATE Users SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, department=?, designation=?, dateDeNaissance=?, intervieweur_id=? WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, candidat.getNumTel());
            pstm.setInt(2, candidat.getJoursOuvrables());
            pstm.setString(3, candidat.getNom());
            pstm.setString(4, candidat.getPrenom());
            pstm.setString(5, candidat.getAddress());
            pstm.setString(6, candidat.getEmail());
            pstm.setString(7, candidat.getGender());
            pstm.setString(8, candidat.getDepartment());
            pstm.setString(9, candidat.getDesignation());
            pstm.setDate(10, new java.sql.Date(candidat.getDateDeNaissance().getTime()));
            pstm.setInt(11, candidat.getIntervieweurId()); // Update intervieweur_id
            pstm.setInt(12, candidat.getId());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Update Success for Candidat ID: " + candidat.getId());
            } else {
                System.out.println("Update Failed: No rows updated. Check if ID exists in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du candidat: " + e.getMessage());
        }
    }
    @Override
    public void delete(Candidat candidat) {
        String qry = "DELETE FROM Users WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, candidat.getId());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Candidat supprimé avec succès! ID: " + candidat.getId());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du candidat: " + e.getMessage());
        }
    }

    public boolean isValidIntervieweurId(int intervieweurId) {
        String qry = "SELECT COUNT(*) FROM Users WHERE id = ? AND user_type = 'RH'";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, intervieweurId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if the intervieweurId exists and is an RH
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'intervieweurId: " + e.getMessage());
        }
        return false;
    }
}