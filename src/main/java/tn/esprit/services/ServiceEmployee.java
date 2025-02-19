package tn.esprit.services;

import tn.esprit.models.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceEmployee extends ServiceUser<Employee> {

    @Override
    public void add(Employee employee) {
        if (!isValidResponsableId(employee.getResponsableId())) {
            System.out.println("Erreur: responsableId invalide.");
            return;
        }
        String qry = "INSERT INTO Users (numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance, user_type, responsable_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, employee.getNumTel());
            pstm.setInt(2, employee.getJoursOuvrables());
            pstm.setString(3, employee.getNom());
            pstm.setString(4, employee.getPrenom());
            pstm.setString(5, employee.getAddress());
            pstm.setString(6, employee.getEmail());
            pstm.setString(7, employee.getGender());
            pstm.setString(8, employee.getDepartment());
            pstm.setString(9, employee.getDesignation());
            pstm.setDate(10, new java.sql.Date(employee.getDateDeNaissance().getTime()));
            pstm.setString(11, employee.getClass().getSimpleName()); // User type (Employee)
            pstm.setInt(12, employee.getResponsableId()); // Set the responsable_id

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    employee.setId(generatedId); // Set the generated ID
                    System.out.println("Employee ajouté avec succès. ID: " + generatedId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'employé: " + e.getMessage());
        }
    }

    @Override
    public void update(Employee employee) {
        if (employee.getId() <= 0) {
            System.out.println("Error: Employee ID is not set for update!");
            return;
        }

        String qry = "UPDATE Users SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, department=?, designation=?, dateDeNaissance=?, responsable_id=? WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, employee.getNumTel());
            pstm.setInt(2, employee.getJoursOuvrables());
            pstm.setString(3, employee.getNom());
            pstm.setString(4, employee.getPrenom());
            pstm.setString(5, employee.getAddress());
            pstm.setString(6, employee.getEmail());
            pstm.setString(7, employee.getGender());
            pstm.setString(8, employee.getDepartment());
            pstm.setString(9, employee.getDesignation());
            pstm.setDate(10, new java.sql.Date(employee.getDateDeNaissance().getTime()));
            pstm.setInt(11, employee.getResponsableId()); // Update responsable_id
            pstm.setInt(12, employee.getId());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Update Success for Employee ID: " + employee.getId());
            } else {
                System.out.println("Update Failed: No rows updated. Check if ID exists in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'employé: " + e.getMessage());
        }
    }


    public boolean isValidResponsableId(int responsableId) {
        String qry = "SELECT COUNT(*) FROM Users WHERE id = ? AND user_type = 'RH'";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, responsableId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if the responsableId exists and is an RH
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du responsableId: " + e.getMessage());
        }
        return false;
    }

    public boolean isResponsableForOthers(int employeeId) {
        String qry = "SELECT COUNT(*) FROM Users WHERE responsable_id = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, employeeId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if the employee is a responsable for others
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du responsable: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void delete(Employee employee) {
        if (isResponsableForOthers(employee.getId())) {
            System.out.println("Erreur: Cet employé est responsable d'autres employés. Veuillez réassigner les employés avant de supprimer.");
            return;
        }

        String qry = "DELETE FROM Users WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, employee.getId());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee supprimé avec succès! ID: " + employee.getId());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'employé: " + e.getMessage());
        }
    }

}