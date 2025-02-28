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
        // Change 'idEmployee' to 'id' in the SELECT query
        String query = "SELECT id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password FROM users WHERE user_type = 'EMPLOYEE'";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("id"),  // Fetching the 'id' field
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



    public void update(Employee employee) {
        String query = "UPDATE users SET numTel = ?, joursOuvrables = ?, nom = ?, prenom = ?, address = ?, email = ?, gender = ?, dateDeNaissance = ?, user_type = ?, password = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, employee.getNumTel());
            ps.setInt(2, employee.getJoursOuvrables());
            ps.setString(3, employee.getNom());
            ps.setString(4, employee.getPrenom());
            ps.setString(5, employee.getAddress());
            ps.setString(6, employee.getEmail());
            ps.setString(7, employee.getGender());
            ps.setDate(8, employee.getDateDeNaissance());
            ps.setString(9, employee.getUserType());
            ps.setString(10, employee.getPassword());
            ps.setInt(11, employee.getId());  // Ensure the ID is set here

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("No employee found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }




    @Override
    public void delete(Employee employee) {
        String deleteEmployeeQuery = "DELETE FROM employee WHERE user_id=?";
        String deleteUserQuery = "DELETE FROM users WHERE id=?";

        try (PreparedStatement pst1 = cnx.prepareStatement(deleteEmployeeQuery);
             PreparedStatement pst2 = cnx.prepareStatement(deleteUserQuery)) {

            // Delete the employee record first (since it's linked to the users table)
            pst1.setInt(1, employee.getIdEmployee());
            pst1.executeUpdate();

            // Now delete the user record
            pst2.setInt(1, employee.getId());
            pst2.executeUpdate();

            System.out.println("Employee and corresponding user deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting the employee: " + e.getMessage());
        }
    }

}
