package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Employee;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployee implements IService<Employee> {

    @Override
    public void add(Employee employee) {
        String query =  "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, employee.getNumTel());
            statement.setInt(2, employee.getJoursOuvrables());
            statement.setString(3, employee.getNom());
            statement.setString(4, employee.getPrenom());
            statement.setString(5, employee.getAddress());
            statement.setString(6, employee.getEmail());
            statement.setString(7, employee.getGender());
            statement.setDate(8,employee.getDateDeNaissance());
            statement.setString(9, employee.getUserType());
            statement.setString(10, employee.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password FROM users WHERE user_type = 'EMPLOYEE'";
        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee(
                resultSet.getInt("id"),  // Fetching the 'id' field
                        resultSet.getString("numTel"),
                        resultSet.getInt("joursOuvrables"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("address"),
                        resultSet.getString("email"),
                        resultSet.getString("gender"),
                        resultSet.getDate("dateDeNaissance"),
                        resultSet.getString("user_type"),
                        resultSet.getString("password")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public void update(Employee employee) {
        String query = "UPDATE users SET numTel = ?, joursOuvrables = ?, nom = ?, prenom = ?, address = ?, email = ?, gender = ?, dateDeNaissance = ?, user_type = ?, password = ? WHERE id = ?";
        try (PreparedStatement ps = MyDatabase.getInstance().getCnx().prepareStatement(query)) {
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
        Connection connection = MyDatabase.getInstance().getCnx();
        try (
             PreparedStatement statement1 = connection.prepareStatement(deleteEmployeeQuery) ;
            PreparedStatement statement2 = connection.prepareStatement(deleteUserQuery)) {

            statement1.setInt(1, employee.getIdEmployee());
            statement1.executeUpdate();
                statement2.setInt(1, employee.getId());
                statement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

