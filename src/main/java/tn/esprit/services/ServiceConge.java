package tn.esprit.services;
import java.sql.*;
import java.util.Collections;
import java.util.Date;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.MyDatabase;
import tn.esprit.models.Conge;
import tn.esprit.interfaces.Iservice;

import java.util.ArrayList;
import java.util.List;
import tn.esprit.models.User;
import tn.esprit.utils.SessionManager;
import tn.esprit.models.Employee;



public class ServiceConge implements Iservice<Conge> {
    private Connection connection;

    // ✅ Correction : Ajouter un constructeur qui prend une connexion en paramètre
    public ServiceConge(Connection connection) {
        this.connection = connection;
    }

     private int getDefaultRHId() {
          String query = "SELECT id FROM users WHERE user_type = 'RH' LIMIT 1";
          try (PreparedStatement ps = connection.prepareStatement(query);
               ResultSet rs = ps.executeQuery()) {
              if (rs.next()) {
                  return rs.getInt("id");
              }
          } catch (SQLException e) {
              System.err.println("❌ Impossible de récupérer un RH par défaut : " + e.getMessage());
          }
          return 1; // Valeur par défaut (à remplacer par un vrai RH)
      }
    /*@Override
      private Employee getEmployeeByUserId(int userId) {
          String query = "SELECT * FROM employee WHERE user_id = ?";
          try (PreparedStatement ps = connection.prepareStatement(query)) {
              ps.setInt(1, userId);
              ResultSet rs = ps.executeQuery();
              if (rs.next()) {
                  Employee employee = new Employee();
                  employee.setId(rs.getInt("employee_id")); // ✅ Correct selon ta table
                  return employee;
              }
          } catch (SQLException e) {
              System.err.println("❌ Impossible de récupérer l'employé : " + e.getMessage());
          }
          return null;
      }*/


    private Employee getEmployeeByUserId(int userId) {
        String query = "SELECT * FROM employee WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Employee employee = new Employee();
                employee.setIdEmployee(rs.getInt("employee_id")); // ✅ Correct selon ta table
                System.out.println("ℹ️ Employé trouvé : ID = " + employee.getIdEmployee());
                return employee;
            }
        } catch (SQLException e) {
            System.err.println("❌ Impossible de récupérer l'employé : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void add(Conge conge) {
        // Récupérer l'utilisateur actuellement connecté via SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérifier que l'utilisateur est bien connecté
        if (currentUser == null) {
            System.out.println("❌ Erreur : Aucun utilisateur connecté !");
            return;
        }

        // Récupérer l'Employee correspondant à l'utilisateur
        Employee employee = getEmployeeByUserId(currentUser.getId());
        if (employee == null) {
            System.out.println("❌ Erreur : Aucun employé associé à cet utilisateur !");
            return;
        }

        conge.setEmployee(employee);

        // Debugging: Vérifier si l'ID est bien récupéré
        System.out.println("ℹ️ Ajout du congé pour Employee ID = " + conge.getEmployee().getIdEmployee());
        conge.setStatut(Conge.statut.EN_ATTENTE);
        String sql = "INSERT INTO conge (dateDebut, dateFin, motif, statut, employee_id, rh_id) VALUES (?, ?, ?, ?, ?, 1)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(conge.getDateDebut().getTime()));
            ps.setDate(2, new java.sql.Date(conge.getDateFin().getTime()));
            ps.setString(3, conge.getMotif());
            ps.setString(4, conge.getStatut().toString());
            ps.setInt(5, conge.getEmployee().getIdEmployee()); // ✅ Utilisation de l'ID correct

            ps.executeUpdate();
            System.out.println("✅ Congé ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du congé : " + e.getMessage());
        }
    }

    public List<Conge> getAll() {
        List<Conge> conges = new ArrayList<>();
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            String query;
            PreparedStatement preparedStatement;

            if (SessionManager.getInstance().isUserType("RH")) {
                query = "SELECT * FROM conge";
                preparedStatement = connection.prepareStatement(query);
            } else if (SessionManager.getInstance().isUserType("Employee")) {
                int employeId = currentUser.getId();
                query = "SELECT * FROM conge WHERE employee_id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, employeId);
            } else {
                System.out.println("❌ Erreur : Type d'utilisateur inconnu.");
                return conges;
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Conge conge = new Conge();
                conge.setIdConge(resultSet.getInt("id_conge"));
                conge.setDateDebut(resultSet.getDate("dateDebut"));
                conge.setDateFin(resultSet.getDate("dateFin"));
                conge.setMotif(resultSet.getString("motif"));
                conge.setStatut(Conge.statut.valueOf(resultSet.getString("statut")));

                Employee employee = getEmployeeById(resultSet.getInt("employee_id"));
                conge.setEmployee(employee);
                conges.add(conge);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des congés : " + e.getMessage());
        }
        return conges;
    }

    public Employee getEmployeeById(int id) {
        Employee employee = null;
        String query = "SELECT e.*, c.*, u.nom AS nom_employee, u.prenom AS prenom_employee " +
                "FROM conge e " +
                "JOIN employee c ON e.employee_id = c.employee_id " +
                "JOIN users u ON c.user_id = u.id " +
                "WHERE c.employee_id = ?";  // Ajoutez la condition WHERE pour filtrer par l'ID de l'employé
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);  // Assurez-vous que l'ID est passé correctement dans la requête
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                employee = new Employee();
                employee.setId(rs.getInt("employee_id"));
                employee.setNom(rs.getString("nom_employee"));
                employee.setPrenom(rs.getString("prenom_employee"));
                //employee.setUserType(rs.getString("user_type"));
                // employee.setNumTel(rs.getString("num_tel"));
                //employee.setJoursOuvrables(rs.getInt("jours_ouvrables"));
                //  employee.setAddress(rs.getString("address"));
                //  employee.setEmail(rs.getString("email"));
                //employee.setGender(rs.getString("gender"));
                // employee.setDateDeNaissance(rs.getDate("date_de_naissance"));
                //employee.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de l'employé : " + e.getMessage());
        }
        return employee;
    }




    public Conge getById(int id) {
        Conge conge = null;
        try {
            String query = "SELECT * FROM conge WHERE id_conge = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                conge = new Conge();
                conge.setIdConge(resultSet.getInt("id_conge"));
                conge.setDateDebut(resultSet.getDate("dateDebut"));
                conge.setDateFin(resultSet.getDate("dateFin"));
                conge.setMotif(resultSet.getString("motif"));
                // Assurez-vous que l'ID de l'employé est bien récupéré, et peut-être que vous devrez aussi
                // récupérer les données de l'employé associé.
                int employeeId = resultSet.getInt("employee_id");
                // Vous pouvez ajouter la récupération des informations de l'employé ici si nécessaire.
                // Exemple: conge.setEmployee(new Employee(employeeId, ...));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération du congé : " + e.getMessage());
        }
        return conge;
    }

    @Override
    public void update(Conge conge) {
        // Récupérer l'utilisateur actuellement connecté via SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérifier que l'utilisateur est bien connecté
        if (currentUser == null) {
            System.out.println("❌ Erreur : Aucun utilisateur connecté !");
            return;
        }

        // Vérifier que l'utilisateur est un RH
        if (currentUser.getUserType() == null || !currentUser.getUserType().equalsIgnoreCase("RH")) {
            System.out.println("❌ Erreur : Seul un responsable RH peut modifier un congé !");
            return;
        }

        // Vérifier que le congé existe dans la base de données
        String selectQuery = "SELECT * FROM conge WHERE id_conge = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
            ps.setInt(1, conge.getIdConge());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("❌ Erreur : Le congé n'existe pas !");
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la vérification du congé : " + e.getMessage());
            return;
        }

        // Mettre à jour le congé
        String updateQuery = "UPDATE conge SET dateDebut = ?, dateFin = ?, motif = ?, statut = ? WHERE id_conge = ?";
        try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            ps.setDate(1, new java.sql.Date(conge.getDateDebut().getTime()));
            ps.setDate(2, new java.sql.Date(conge.getDateFin().getTime()));
            ps.setString(3, conge.getMotif());
            ps.setString(4, conge.getStatut().toString()); // Statut du congé
            ps.setInt(5, conge.getIdConge());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Congé mis à jour avec succès !");
            } else {
                System.out.println("❌ Erreur : Aucune mise à jour effectuée.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour du congé : " + e.getMessage());
        }
    }
    public void remove(int idconge){}

}