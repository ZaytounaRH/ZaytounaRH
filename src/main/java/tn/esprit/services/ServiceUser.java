package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.User;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {
    protected Connection cnx;

    public ServiceUser() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(User user) {
        String query = "INSERT INTO user (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getNumTel());
            pst.setInt(2, user.getJoursOuvrables());
            pst.setString(3, user.getNom());
            pst.setString(4, user.getPrenom());
            pst.setString(5, user.getAddress());
            pst.setString(6, user.getEmail());
            pst.setString(7, user.getGender());
            pst.setDate(8, user.getDateDeNaissance());
            pst.setString(9, user.getUserType());
            pst.setString(10, user.getPassword());

            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
            System.out.println("Utilisateur ajouté avec succès ! ID: " + user.getId());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("numTel"),
                        rs.getInt("joursOuvrables"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getDate("dateDeNaissance"),
                        rs.getString("userType"),
                        rs.getString("password")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }

    @Override
    public void update(User user) {
        String query = "UPDATE user SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, dateDeNaissance=?, userType=?, password=? WHERE id=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, user.getNumTel());
            pst.setInt(2, user.getJoursOuvrables());
            pst.setString(3, user.getNom());
            pst.setString(4, user.getPrenom());
            pst.setString(5, user.getAddress());
            pst.setString(6, user.getEmail());
            pst.setString(7, user.getGender());
            pst.setDate(8, user.getDateDeNaissance());
            pst.setString(9, user.getUserType());
            pst.setString(10, user.getPassword());
            pst.setInt(11, user.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void delete(User user) {
        String query = "DELETE FROM user WHERE id=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, user.getId());

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Utilisateur supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }
}
