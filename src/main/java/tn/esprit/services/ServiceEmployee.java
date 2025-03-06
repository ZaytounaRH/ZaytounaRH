package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Employee;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployee implements IService<Employee> {
    protected Connection cnx;

    // Constructor to initialize the connection
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
        String query = "SELECT u.id AS user_id, u.*, e.employee_id " +
                "FROM users u " +
                "JOIN employee e ON u.id = e.user_id " +
                "WHERE u.user_type = 'Employee'";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (cnx == null) {
                System.out.println( "La connexion à la base de données est nulle !");
                return employees;
            }

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setNumTel(resultSet.getString("numTel"));
                employee.setJoursOuvrables(resultSet.getInt("joursOuvrables"));
                employee.setNom(resultSet.getString("nom"));
                employee.setPrenom(resultSet.getString("prenom"));
                employee.setAddress(resultSet.getString("address"));
                employee.setEmail(resultSet.getString("email"));
                employee.setGender(resultSet.getString("gender"));
                employee.setUserType(resultSet.getString("user_type"));
                employee.setPassword(resultSet.getString("password"));
                employee.setId(resultSet.getInt("user_id"));
                employee.setIdEmployee(resultSet.getInt("employee_id"));

                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public void update(Employee emp) {
        String query = "UPDATE users SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, dateDeNaissance=?, user_type=?, password=? WHERE id=?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            // Set the parameters
            statement.setString(1, emp.getNumTel());
            statement.setInt(2, emp.getJoursOuvrables());
            statement.setString(3, emp.getNom());
            statement.setString(4, emp.getPrenom());
            statement.setString(5, emp.getAddress());
            statement.setString(6, emp.getEmail());
            statement.setString(7, emp.getGender());
            statement.setDate(8, emp.getDateDeNaissance());
            statement.setString(9, emp.getUserType());
            statement.setString(10, emp.getPassword());
            statement.setInt(11, emp.getId()); // Update based on the employee's ID

            // Execute the update
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("No rows updated.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }


    @Override
    public void delete(Employee employee) {
        String deleteEmployeeQuery = "DELETE FROM employee WHERE user_id=?";
        String deleteUserQuery = "DELETE FROM users WHERE id=?";
        try (
                PreparedStatement statement1 = cnx.prepareStatement(deleteEmployeeQuery);
                PreparedStatement statement2 = cnx.prepareStatement(deleteUserQuery)) {

            statement1.setInt(1, employee.getIdEmployee());
            statement1.executeUpdate();
            statement2.setInt(1, employee.getId());
            statement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
