package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Employee;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployee implements IService<Employee> {
    protected Connection cnx;

    public ServiceEmployee() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Employee employee) {
        // Validate user_type
        String userType = employee.getUserType();
        if (!userType.equals("EMPLOYEE")) {
            System.out.println("Erreur : Le type d'utilisateur doit être 'EMPLOYEE' !");
            return;
        }

        // Insert into the 'users' table first
        String userQuery = "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, employee.getNumTel());
            pst.setInt(2, employee.getJoursOuvrables());
            pst.setString(3, employee.getNom());
            pst.setString(4, employee.getPrenom());
            pst.setString(5, employee.getAddress());
            pst.setString(6, employee.getEmail());
            pst.setString(7, employee.getGender());
            pst.setDate(8, employee.getDateDeNaissance());
            pst.setString(9, userType);
            pst.setString(10, employee.getPassword());

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    employee.setIdEmployee(userId);  // Store the generated user ID as Employee ID
                    System.out.println("Utilisateur Employee ajouté avec succès !");
                }
            }

            // Now insert the Employee-specific information into the 'employee' table
            String employeeQuery = "INSERT INTO employee (user_id) VALUES (?)";  // Reference the 'user_id' from the 'users' table
            try (PreparedStatement pstEmp = cnx.prepareStatement(employeeQuery)) {
                pstEmp.setInt(1, employee.getIdEmployee());
                pstEmp.executeUpdate();
                System.out.println("Employee spécifique ajouté avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'Employee : " + e.getMessage());
        }
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM users WHERE user_type = 'EMPLOYEE'";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("idEmployee"),
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
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des Employees : " + e.getMessage());
        }

        return employees;
    }

    @Override
    public void update(Employee employee) {
        String query = "UPDATE employee SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, dateDeNaissance=?, user_type=?, password=? WHERE idEmployee=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, employee.getNumTel());
            pst.setInt(2, employee.getJoursOuvrables());
            pst.setString(3, employee.getNom());
            pst.setString(4, employee.getPrenom());
            pst.setString(5, employee.getAddress());
            pst.setString(6, employee.getEmail());
            pst.setString(7, employee.getGender());
            pst.setDate(8, employee.getDateDeNaissance());
            pst.setString(9, employee.getUserType());
            pst.setString(10, employee.getPassword());
            pst.setInt(11, employee.getIdEmployee());

            pst.executeUpdate();
            System.out.println("Employee mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'Employee : " + e.getMessage());
        }
    }

    @Override
    public void delete(Employee employee) {
        String query = "DELETE FROM employee WHERE idEmployee=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, employee.getIdEmployee());

            pst.executeUpdate();
            System.out.println("Employee supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'Employee : " + e.getMessage());
        }
    }
}
