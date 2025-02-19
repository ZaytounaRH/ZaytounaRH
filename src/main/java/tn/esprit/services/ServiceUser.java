package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.*;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser<T extends User> implements IService<T> {
    protected Connection cnx;

    public ServiceUser() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(T user) {
        String qry = "INSERT INTO Users (numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance, user_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, user.getNumTel());
            pstm.setInt(2, user.getJoursOuvrables());
            pstm.setString(3, user.getNom());
            pstm.setString(4, user.getPrenom());
            pstm.setString(5, user.getAddress());
            pstm.setString(6, user.getEmail());
            pstm.setString(7, user.getGender());
            pstm.setString(8, user.getDepartment());
            pstm.setString(9, user.getDesignation());
            pstm.setDate(10, new java.sql.Date(user.getDateDeNaissance().getTime()));
            pstm.setString(11, user.getClass().getSimpleName());

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    user.setId(generatedId);  // **Fix: Set the generated ID**
                    System.out.println("User ajouté avec succès. ID: " + generatedId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        }
    }


    @Override
    public List<T> getAll() {
        List<T> users = new ArrayList<>();
        String qry = "SELECT * FROM Users";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                // Create a User object based on the user_type
                String userType = rs.getString("user_type");
                T user = null;
                switch (userType) {
                    case "Admin":
                        user = (T) new Admin(rs.getInt("id"), rs.getInt("numTel"), rs.getInt("joursOuvrables"), rs.getString("nom"), rs.getString("prenom"), rs.getString("address"), rs.getString("email"), rs.getString("gender"), rs.getString("department"), rs.getString("designation"), rs.getDate("dateDeNaissance"));
                        break;
                    case "RH":
                        user = (T) new RH(rs.getInt("id"), rs.getInt("numTel"), rs.getInt("joursOuvrables"), rs.getString("nom"), rs.getString("prenom"), rs.getString("address"), rs.getString("email"), rs.getString("gender"), rs.getString("department"), rs.getString("designation"), rs.getDate("dateDeNaissance"), null, null);
                        break;
                    case "Employee":
                        user = (T) new Employee(rs.getInt("id"), rs.getInt("numTel"), rs.getInt("joursOuvrables"), rs.getString("nom"), rs.getString("prenom"), rs.getString("address"), rs.getString("email"), rs.getString("gender"), rs.getString("department"), rs.getString("designation"), rs.getDate("dateDeNaissance"), rs.getInt("responsable_id"));
                        break;
                    case "Candidat":
                        user = (T) new Candidat(rs.getInt("id"), rs.getInt("numTel"), rs.getInt("joursOuvrables"), rs.getString("nom"), rs.getString("prenom"), rs.getString("address"), rs.getString("email"), rs.getString("gender"), rs.getString("department"), rs.getString("designation"), rs.getDate("dateDeNaissance"), rs.getInt("intervieweur_id"));
                        break;
                }
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void update(T user) {
        String qry = "UPDATE Users SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, department=?, designation=?, dateDeNaissance=? WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, user.getNumTel());
            pstm.setInt(2, user.getJoursOuvrables());
            pstm.setString(3, user.getNom());
            pstm.setString(4, user.getPrenom());
            pstm.setString(5, user.getAddress());
            pstm.setString(6, user.getEmail());
            pstm.setString(7, user.getGender());
            pstm.setString(8, user.getDepartment());
            pstm.setString(9, user.getDesignation());
            pstm.setDate(10, new java.sql.Date(user.getDateDeNaissance().getTime()));
            pstm.setInt(11, user.getId());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Update Success for User ID: " + user.getId());
            } else {
                System.out.println("Update Failed: No rows updated. Check if ID exists in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
        }
    }


    @Override
    public void delete(T user) {
        String qry = "DELETE FROM Users WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, user.getId());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User supprimé avec succès! ID: " + user.getId());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
        }
    }
}