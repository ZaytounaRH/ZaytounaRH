package tn.esprit.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tn.esprit.models.User;

public class UserService {
    private Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    // ✅ Récupérer un utilisateur par email
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getInt("numTel"),
                        rs.getInt("joursOuvrables"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("department"),
                        rs.getString("designation"),
                        rs.getDate("dateDeNaissance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Aucun utilisateur trouvé
    }

    // ✅ Récupérer le RH d’un employé
    public User getRHByEmployeeId(int employeeId) {
        User rh = null;
        String query = "SELECT rh_id FROM rh_employee WHERE employee_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int rhId = rs.getInt("rh_id");

                // Maintenant, récupérer les infos du RH depuis la table `users`
                String rhQuery = "SELECT * FROM users WHERE id = ?";
                try (PreparedStatement rhPs = connection.prepareStatement(rhQuery)) {
                    rhPs.setInt(1, rhId);
                    ResultSet rhRs = rhPs.executeQuery();

                    if (rhRs.next()) {
                        rh = new User(
                                rhRs.getInt("id"),
                                rhRs.getInt("numTel"),
                                rhRs.getInt("joursOuvrables"),
                                rhRs.getString("nom"),
                                rhRs.getString("prenom"),
                                rhRs.getString("address"),
                                rhRs.getString("email"),
                                rhRs.getString("gender"),
                                rhRs.getString("department"),
                                rhRs.getString("designation"),
                                rhRs.getDate("dateDeNaissance")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rh;
    }
}
