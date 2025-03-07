package tn.esprit.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.models.Conge;
import tn.esprit.models.Employee;
import tn.esprit.models.User;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;

public class ServiceConge {

    private Connection connection;

    // ✅ Correction : Utiliser une connexion active
    public ServiceConge() {
        this.connection = MyDatabase.getInstance().getCnx();
        ensureConnection();
        System.out.println("ℹ️ Connexion utilisée dans ServiceConge : " + (connection != null ? "ACTIVE" : "FERMÉE"));
    }

    // 🔥 Vérifie si la connexion est toujours active avant toute requête SQL
    private void ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("⚠️ Connexion fermée, tentative de reconnexion...");
                this.connection = MyDatabase.getInstance().getCnx();
                System.out.println("✅ Connexion rétablie !");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la vérification de la connexion : " + e.getMessage());
        }
    }

    // ✅ Récupérer tous les congés
    public List<Conge> getAll() {
        ensureConnection(); // 🔥 Vérifie que la connexion est active
        List<Conge> conges = new ArrayList<>();

        String query = "SELECT * FROM conge";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("🔍 Exécution de la requête : " + query);

            while (resultSet.next()) {
                Conge conge = extractCongeFromResultSet(resultSet);
                conges.add(conge);
            }

            System.out.println("✅ Nombre de congés récupérés : " + conges.size());

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des congés : " + e.getMessage());
        }
        return conges;
    }

    // ✅ Récupérer les congés d'un employé spécifique
    public List<Conge> getAllByEmployee(int employeeId) {
        ensureConnection();
        List<Conge> conges = new ArrayList<>();
        String query = "SELECT * FROM conge WHERE employee_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Conge conge = extractCongeFromResultSet(resultSet);
                conges.add(conge);
            }

            System.out.println("✅ Nombre de congés récupérés pour Employee ID " + employeeId + " : " + conges.size());

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des congés de l'employé : " + e.getMessage());
        }
        return conges;
    }

    // ✅ Fonction pour extraire un objet Conge depuis un ResultSet
    private Conge extractCongeFromResultSet(ResultSet resultSet) throws SQLException {
        Conge conge = new Conge();
        conge.setIdConge(resultSet.getInt("id_conge"));
        conge.setDateDebut(resultSet.getDate("dateDebut"));
        conge.setDateFin(resultSet.getDate("dateFin"));
        conge.setMotif(resultSet.getString("motif"));
        conge.setStatut(Conge.Statut.valueOf(resultSet.getString("statut")));
        conge.setEmployee(getEmployeeById(resultSet.getInt("employee_id")));
        return conge;
    }

    // ✅ Récupérer un employé par ID
    public Employee getEmployeeById(int userId) {
        String query = "SELECT * FROM employee WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee employee = new Employee();
                employee.setIdEmployee(rs.getInt("employee_id"));

                System.out.println("ℹ️ Employé trouvé : ID = " + employee.getIdEmployee());

                if (employee.getIdEmployee() == 0) {
                    System.err.println("❌ Problème : L'ID de l'employé est 0 !");
                }

                return employee;
            } else {
                System.err.println("❌ Aucun employé trouvé pour user_id = " + userId);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la récupération de l'employé : " + e.getMessage());
        }

        return null;  // Retourne null si l'utilisateur n'a pas d'employé associé
    }


    public void add(Conge conge) {
        // Récupérer l'utilisateur actuellement connecté via SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.out.println("❌ Erreur : Aucun utilisateur connecté !");
            return;
        }

        // Vérifier si l'utilisateur est bien associé à un employé
        Employee employee = getEmployeeById(currentUser.getId());
        if (employee == null || employee.getIdEmployee() == 0) {
            System.out.println("❌ Erreur : Aucun employé valide trouvé pour cet utilisateur !");
            return;
        }

        conge.setEmployee(employee);

        // 🛠 Assurer que le statut n'est pas null
        if (conge.getStatut() == null) {
            System.out.println("⚠️ Statut null détecté, assignation automatique à EN_ATTENTE");
            conge.setStatut(Conge.Statut.EN_ATTENTE);
        }

        System.out.println("ℹ️ Ajout du congé pour Employee ID = " + conge.getEmployee().getIdEmployee());

        // 🔹 Préparer l'insertion SQL
        String sql = "INSERT INTO conge (dateDebut, dateFin, motif, statut, employee_id, rh_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(conge.getDateDebut().getTime()));
            ps.setDate(2, new java.sql.Date(conge.getDateFin().getTime()));
            ps.setString(3, conge.getMotif());
            ps.setString(4, conge.getStatut().toString()); // 🔥 Assuré que statut n'est pas null
            ps.setInt(5, conge.getEmployee().getIdEmployee());
            ps.setInt(6, getDefaultRHId());

            int rowsAffected = ps.executeUpdate(); // Exécuter la requête

            if (rowsAffected > 0) {
                System.out.println("✅ Congé ajouté avec succès !");
            } else {
                System.out.println("❌ Aucun enregistrement inséré !");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du congé : " + e.getMessage());
        }
    }


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
                    System.out.println("❌ Erreur : Le congé avec ID " + conge.getIdConge() + " n'existe pas !");
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la vérification du congé (ID " + conge.getIdConge() + ") : " + e.getMessage());
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
                System.out.println("➡️ ID : " + conge.getIdConge() +
                        ", Date début : " + conge.getDateDebut() +
                        ", Date fin : " + conge.getDateFin() +
                        ", Motif : " + conge.getMotif() +
                        ", Statut : " + conge.getStatut());
            } else {
                System.out.println("❌ Erreur : Aucune mise à jour effectuée.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la mise à jour du congé (ID " + conge.getIdConge() + ") : " + e.getMessage());
        }
    }


    // ✅ Récupérer un RH par défaut
    private int getDefaultRHId() {
        ensureConnection();
        String query = "SELECT id FROM users WHERE user_type = 'RH' LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("❌ Impossible de récupérer un RH par défaut : " + e.getMessage());
        }
        return 1;
    }

    public void delete(int idConge) {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.out.println("❌ Erreur : Aucun utilisateur connecté !");
            return;
        }

        // Vérifier si le congé appartient à l'utilisateur ou si c'est un RH
        String selectQuery = "SELECT employee_id FROM conge WHERE id_conge = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
            ps.setInt(1, idConge);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Erreur : Congé non trouvé !");
                return;
            }

            int employeeId = rs.getInt("employee_id");

            // Vérifier si c'est le RH ou l'employé propriétaire du congé
            if (!currentUser.getUserType().equalsIgnoreCase("RH") && currentUser.getId() != employeeId) {
                System.out.println("❌ Erreur : Vous n'avez pas le droit de supprimer ce congé !");
                return;
            }

            // Supprimer le congé
            String deleteQuery = "DELETE FROM conge WHERE id_conge = ?";
            try (PreparedStatement deletePs = connection.prepareStatement(deleteQuery)) {
                deletePs.setInt(1, idConge);
                int rowsAffected = deletePs.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("✅ Congé supprimé avec succès !");
                } else {
                    System.out.println("❌ Erreur : Aucun congé supprimé !");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du congé : " + e.getMessage());
        }
    }

}
