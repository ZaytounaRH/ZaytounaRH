package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.RH;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRH implements IService<RH> {
    protected Connection cnx;

    public ServiceRH() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(RH rh) {
        // Validate user_type
        String userType = rh.getUserType();
        if (!userType.equals("RH")) {
            System.out.println("Erreur : Le type d'utilisateur doit être 'RH' !");
            return;
        }

        // Insert into the 'users' table first
        String userQuery = "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, rh.getNumTel());
            pst.setInt(2, rh.getJoursOuvrables());
            pst.setString(3, rh.getNom());
            pst.setString(4, rh.getPrenom());
            pst.setString(5, rh.getAddress());
            pst.setString(6, rh.getEmail());
            pst.setString(7, rh.getGender());
            pst.setDate(8, rh.getDateDeNaissance());
            pst.setString(9, userType);
            pst.setString(10, rh.getPassword());

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    rh.setIdRH(userId);  // Store the generated user ID as RH ID
                    System.out.println("Utilisateur RH ajouté avec succès !");
                }
            }

            // Now insert the RH specific information into the 'rh' table
            String rhQuery = "INSERT INTO rh (user_id) VALUES (?)";  // Reference the 'user_id' from the 'users' table
            try (PreparedStatement pstRh = cnx.prepareStatement(rhQuery)) {
                pstRh.setInt(1, rh.getIdRH());
                pstRh.executeUpdate();
                System.out.println("RH spécifique ajouté avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du RH : " + e.getMessage());
        }
    }

    @Override
    public List<RH> getAll() {
        List<RH> rhs = new ArrayList<>();
        String query = "SELECT * FROM users WHERE user_type = 'RH'";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                RH rh = new RH(
                        rs.getInt("idRH"),
                        rs.getString("numTel"),
                        rs.getInt("joursOuvrables"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getDate("dateDeNaissance"),
                        rs.getString("user_type"),
                        rs.getString("password")
                );
                rhs.add(rh);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des RHs : " + e.getMessage());
        }

        return rhs;
    }

    @Override
    public void update(RH rh) {
        String query = "UPDATE rh SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, dateDeNaissance=?, user_type=?, password=? WHERE idRH=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, rh.getNumTel());
            pst.setInt(2, rh.getJoursOuvrables());
            pst.setString(3, rh.getNom());
            pst.setString(4, rh.getPrenom());
            pst.setString(5, rh.getAddress());
            pst.setString(6, rh.getEmail());
            pst.setString(7, rh.getGender());
            pst.setDate(8, rh.getDateDeNaissance());
            pst.setString(9, rh.getUserType());
            pst.setString(10, rh.getPassword());
            pst.setInt(11, rh.getIdRH());

            pst.executeUpdate();
            System.out.println("RH mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du RH : " + e.getMessage());
        }
    }

    @Override
    public void delete(RH rh) {
        String query = "DELETE FROM rh WHERE idRH=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, rh.getIdRH());

            pst.executeUpdate();
            System.out.println("RH supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du RH : " + e.getMessage());
        }
    }
}