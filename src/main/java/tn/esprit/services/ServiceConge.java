package tn.esprit.services;

import tn.esprit.interfaces.Iservice;
import tn.esprit.models.Conge;
import tn.esprit.models.Employee;
import tn.esprit.models.User;
import tn.esprit.utils.SessionManager;
import tn.esprit.models.RH;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceConge implements Iservice<Conge> {
    private Connection connection;

    // ✅ Constructeur qui prend une connexion en paramètre
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
        return 1; // Valeur par défaut si aucun RH trouvé
    }

    private Employee getEmployeeByUserId(int userId) {
        String query = "SELECT * FROM employee WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Employee employee = new Employee();
                employee.setIdEmployee(rs.getInt("employee_id"));
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
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Erreur : Aucun utilisateur connecté !");
            return;
        }

        Employee employee = getEmployeeByUserId(currentUser.getId());
        if (employee == null) {
            System.out.println("❌ Erreur : Aucun employé associé à cet utilisateur !");
            return;
        }

        conge.setEmployee(employee);
        conge.setStatut(Conge.statut.EN_ATTENTE);

        String sql = "INSERT INTO conge (dateDebut, dateFin, motif, statut, employee_id, rh_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(conge.getDateDebut().getTime()));
            ps.setDate(2, new java.sql.Date(conge.getDateFin().getTime()));
            ps.setString(3, conge.getMotif());
            ps.setString(4, conge.getStatut().toString());
            ps.setInt(5, conge.getEmployee().getIdEmployee());
            ps.setInt(6, getDefaultRHId()); // Utilisation de la méthode pour récupérer RH par défaut
            ps.executeUpdate();
            System.out.println("✅ Congé ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du congé : " + e.getMessage());
        }
    }

    public List<Conge> getAll() {
        List<Conge> conges = new ArrayList<>();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        String query;
        PreparedStatement preparedStatement;

        try {
            if (SessionManager.getInstance().isUserType("RH")) {
                query = "SELECT * FROM conge";
                preparedStatement = connection.prepareStatement(query);
            } else if (SessionManager.getInstance().isUserType("Employee")) {
                query = "SELECT * FROM conge WHERE employee_id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, currentUser.getId());
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

                // Récupération de l'employé associé
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
        String query = "SELECT e.*, u.nom AS nom_employee, u.prenom AS prenom_employee " +
                "FROM employee e " +
                "JOIN users u ON e.user_id = u.id " +
                "WHERE e.employee_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Employee employee = new Employee();
                employee.setIdEmployee(rs.getInt("employee_id"));
                employee.setNom(rs.getString("nom_employee"));
                employee.setPrenom(rs.getString("prenom_employee"));
                return employee;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de l'employé : " + e.getMessage());
        }
        return null;
    }

    public Conge getById(int id) {
        Conge conge = null;
        String query = "SELECT * FROM conge WHERE id_conge = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                conge = new Conge();
                conge.setIdConge(resultSet.getInt("id_conge"));
                conge.setDateDebut(resultSet.getDate("dateDebut"));
                conge.setDateFin(resultSet.getDate("dateFin"));
                conge.setMotif(resultSet.getString("motif"));
                conge.setStatut(Conge.statut.valueOf(resultSet.getString("statut")));
                // Récupérer l'employé associé si nécessaire
                conge.setEmployee(getEmployeeById(resultSet.getInt("employee_id")));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération du congé : " + e.getMessage());
        }
        return conge;
    }

    @Override
    public void update(Conge conge) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Erreur : Aucun utilisateur connecté !");
            return;
        }

        if (!"RH".equalsIgnoreCase(currentUser.getUserType())) {
            System.out.println("❌ Erreur : Seul un responsable RH peut modifier un congé !");
            return;
        }

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

        String updateQuery = "UPDATE conge SET dateDebut = ?, dateFin = ?, motif = ?, statut = ? WHERE id_conge = ?";
        try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            ps.setDate(1, new java.sql.Date(conge.getDateDebut().getTime()));
            ps.setDate(2, new java.sql.Date(conge.getDateFin().getTime()));
            ps.setString(3, conge.getMotif());
            ps.setString(4, conge.getStatut().toString());
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

    public void remove(int idconge) {
        // TODO: Implémentation de la méthode pour supprimer un congé
    }
}
