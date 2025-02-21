package tn.esprit.services;

import tn.esprit.models.Employee;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployee extends ServiceUser<Employee> {
    private Connection cnx;

    public ServiceEmployee() {
        cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(Employee employee) {
        if (!isValidResponsableId(employee.getResponsableId())) {
            System.out.println("Erreur: responsableId invalide.");
            return;
        }

        // Insert into users table
        String userQry = "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance, user_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement userPstm = cnx.prepareStatement(userQry, Statement.RETURN_GENERATED_KEYS)) {
            userPstm.setInt(1, employee.getNumTel());
            userPstm.setInt(2, employee.getJoursOuvrables());
            userPstm.setString(3, employee.getNom());
            userPstm.setString(4, employee.getPrenom());
            userPstm.setString(5, employee.getAddress());
            userPstm.setString(6, employee.getEmail());
            userPstm.setString(7, employee.getGender());
            userPstm.setString(8, employee.getDepartment());
            userPstm.setString(9, employee.getDesignation());
            userPstm.setDate(10, new java.sql.Date(employee.getDateDeNaissance().getTime()));
            userPstm.setString(11, "Employee");

            int affectedRows = userPstm.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = userPstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        employee.setId(generatedId);

                        // Insert into employee table
                        String employeeQry = "INSERT INTO employee (employee_id, responsable_id) VALUES (?, ?)";
                        try (PreparedStatement employeePstm = cnx.prepareStatement(employeeQry)) {
                            employeePstm.setInt(1, generatedId);
                            employeePstm.setInt(2, employee.getResponsableId());
                            employeePstm.executeUpdate();
                        }

                        System.out.println("Employee ajouté avec succès. ID: " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'employé: " + e.getMessage());
        }
    }

    @Override
    public void update(Employee employee) {
        if (employee.getId() <= 0) {
            System.out.println("Erreur: ID employé non valide pour la mise à jour !");
            return;
        }

        // Update users table
        String userQry = "UPDATE users SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, department=?, designation=?, dateDeNaissance=? WHERE id=?";
        try (PreparedStatement userPstm = cnx.prepareStatement(userQry)) {
            userPstm.setInt(1, employee.getNumTel());
            userPstm.setInt(2, employee.getJoursOuvrables());
            userPstm.setString(3, employee.getNom());
            userPstm.setString(4, employee.getPrenom());
            userPstm.setString(5, employee.getAddress());
            userPstm.setString(6, employee.getEmail());
            userPstm.setString(7, employee.getGender());
            userPstm.setString(8, employee.getDepartment());
            userPstm.setString(9, employee.getDesignation());
            userPstm.setDate(10, new java.sql.Date(employee.getDateDeNaissance().getTime()));
            userPstm.setInt(11, employee.getId());

            int userRowsUpdated = userPstm.executeUpdate();

            // Update employee table
            String employeeQry = "UPDATE employee SET responsable_id=? WHERE employee_id=?";
            try (PreparedStatement employeePstm = cnx.prepareStatement(employeeQry)) {
                employeePstm.setInt(1, employee.getResponsableId());
                employeePstm.setInt(2, employee.getId());
                int employeeRowsUpdated = employeePstm.executeUpdate();

                if (userRowsUpdated > 0 && employeeRowsUpdated > 0) {
                    System.out.println("Mise à jour réussie pour l'employé ID: " + employee.getId());
                } else {
                    System.out.println("Échec de la mise à jour: ID inexistant.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'employé: " + e.getMessage());
        }
    }

    @Override
    public void delete(Employee employee) {
        if (isResponsableForOthers(employee.getId())) {
            System.out.println("Erreur: Cet employé est responsable d'autres employés. Veuillez réassigner les employés avant de supprimer.");
            return;
        }

        // Delete from employee table
        String employeeQry = "DELETE FROM employee WHERE employee_id=?";
        try (PreparedStatement employeePstm = cnx.prepareStatement(employeeQry)) {
            employeePstm.setInt(1, employee.getId());
            int employeeRowsDeleted = employeePstm.executeUpdate();

            // Delete from users table
            String userQry = "DELETE FROM users WHERE id=?";
            try (PreparedStatement userPstm = cnx.prepareStatement(userQry)) {
                userPstm.setInt(1, employee.getId());
                int userRowsDeleted = userPstm.executeUpdate();

                if (employeeRowsDeleted > 0 && userRowsDeleted > 0) {
                    System.out.println("Employé supprimé avec succès! ID: " + employee.getId());
                } else {
                    System.out.println("Aucun employé trouvé avec cet ID.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'employé: " + e.getMessage());
        }
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String qry = "SELECT u.*, e.responsable_id FROM users u " +
                "JOIN employee e ON u.id = e.employee_id " +
                "WHERE u.user_type = 'Employee'";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Employee employee = new Employee(
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
                        rs.getDate("dateDeNaissance"),
                        rs.getInt("responsable_id"), // Fetch responsable_id from the employee table
                        null // responsable_name is not stored in the database
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des employés: " + e.getMessage());
        }
        return employees;
    }

    public boolean isValidResponsableId(int responsableId) {
        String qry = "SELECT COUNT(*) FROM Users WHERE id = ? AND user_type = 'RH'";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, responsableId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // True if ID exists and is RH
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du responsableId: " + e.getMessage());
        }
        return false;
    }

    public boolean isResponsableForOthers(int employeeId) {
        String qry = "SELECT COUNT(*) FROM Users WHERE responsable_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, employeeId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // True if employee is a responsable
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du responsable: " + e.getMessage());
        }
        return false;
    }
    public Employee getById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Employee(
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
                        rs.getDate("dateDeNaissance"),
                        rs.getInt("responsable_id"),
                        null // responsable_name is not stored in the database
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}