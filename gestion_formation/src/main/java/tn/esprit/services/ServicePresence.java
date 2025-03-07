package tn.esprit.services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tn.esprit.models.Presence;
import tn.esprit.models.Employee;
import tn.esprit.models.User;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;
public class ServicePresence {
    private Connection connection;

    public ServicePresence() {
        this.connection = MyDatabase.getInstance().getCnx();
        ensureConnection();
        System.out.println("ℹ️ Connexion utilisée dans ServicePresence : " + (connection != null ? "ACTIVE" : "FERMÉE"));
    }

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

    public void add(Presence presence) {
        // Vérifier l'utilisateur connecté
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.out.println("❌ Erreur : Aucun utilisateur connecté !");
            return;
        }

        Employee employee = getEmployeeById(currentUser.getId());
        if (employee == null || employee.getIdEmployee() == 0) {
            System.out.println("❌ Erreur : Aucun employé valide trouvé pour cet utilisateur !");
            return;
        }

        presence.setEmployee(employee);

        System.out.println("ℹ️ Ajout de la présence pour Employee ID = " + presence.getEmployee().getIdEmployee());

        // Requête d'ajout dans la base
        String sql = "INSERT INTO presence (date, heureArrive, heureDepart, employee_id, rh_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(presence.getDate().getTime())); // Date
            ps.setTime(2, new java.sql.Time(presence.getHeureArrive().getTime())); // Heure d'arrivée
            ps.setTime(3, new java.sql.Time(presence.getHeureDepart().getTime())); // Heure de départ
            ps.setInt(4, presence.getEmployee().getIdEmployee()); // ID de l'employé
            ps.setInt(5, getDefaultRHId()); // ID du RH par défaut

            int rowsAffected = ps.executeUpdate(); // Exécuter la requête

            if (rowsAffected > 0) {
                System.out.println("✅ Présence ajoutée avec succès !");
            } else {
                System.out.println("❌ Aucun enregistrement inséré !");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de la présence : " + e.getMessage());
        }
    }

    public List<Presence> getAll() {
        List<Presence> presences = new ArrayList<>();
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            String query;
            PreparedStatement preparedStatement;

            // Vérifie si l'utilisateur est un RH
            if (SessionManager.getInstance().isUserType("RH")) {
                // Si l'utilisateur est RH, on récupère toutes les présences
                query = "SELECT * FROM presence";
                preparedStatement = connection.prepareStatement(query);
            }
            // Si l'utilisateur est un employé, on filtre les présences de cet employé
            else if (SessionManager.getInstance().isUserType("Employee")) {
                int employeId = currentUser.getId();  // Récupère l'ID de l'employé
                query = "SELECT * FROM presence WHERE employee_id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, employeId);  // Filtre par ID de l'employé
            } else {
                System.out.println("❌ Erreur : Type d'utilisateur inconnu.");
                return presences;  // Retourne une liste vide si l'utilisateur est inconnu
            }

            // Exécute la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            // Parcours les résultats et crée des objets Presence
            while (resultSet.next()) {
                Presence presence = new Presence();
                presence.setIdPresence(resultSet.getInt("id_presence"));
                presence.setDate(resultSet.getDate("date"));
                presence.setHeureArrive(resultSet.getTime("heureArrive"));
                presence.setHeureDepart(resultSet.getTime("heureDepart"));

                // Récupère l'employé associé à cette présence (si nécessaire)
                Employee employee = getEmployeeById(resultSet.getInt("employee_id"));
                presence.setEmployee(employee);
                presences.add(presence);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des présences : " + e.getMessage());
        }
        return presences;
    }


    private Presence extractPresenceFromResultSet(ResultSet resultSet) throws SQLException {
        Presence presence = new Presence();

        // Récupérer l'ID de la présence
        presence.setIdPresence(resultSet.getInt("id_presence"));

        // Récupérer la date de la présence (toujours de type java.sql.Date)
        java.sql.Date dateSQL = resultSet.getDate("date");
        presence.setDate(dateSQL);  // Pas de conversion nécessaire ici, on garde java.sql.Date

        // Récupérer les heures d'arrivée et de départ (toujours de type java.sql.Time)
        java.sql.Time heureArriveSQL = resultSet.getTime("heureArrive");
        presence.setHeureArrive(heureArriveSQL);  // Pas de conversion nécessaire ici, on garde java.sql.Time

        java.sql.Time heureDepartSQL = resultSet.getTime("heureDepart");
        presence.setHeureDepart(heureDepartSQL);  // Pas de conversion nécessaire ici, on garde java.sql.Time

        // Associer l'employé à cette présence en récupérant l'employé par son ID
        presence.setEmployee(getEmployeeById(resultSet.getInt("employee_id")));

        return presence;
    }
    // ✅ Récupérer les présences d'un employé spécifique
    public List<Presence> getAllByEmployee(int employeeId) {
        ensureConnection();
        List<Presence> presences = new ArrayList<>();
        String query = "SELECT * FROM presence WHERE employee_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Presence presence = extractPresenceFromResultSet(resultSet);
                presences.add(presence);
            }

            System.out.println("✅ Nombre de présences récupérées pour Employee ID " + employeeId + " : " + presences.size());

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des présences de l'employé : " + e.getMessage());
        }
        return presences;
    }



    public Employee getEmployeeById(int userId) {
        String query = "SELECT e.employee_id, u.nom, u.prenom, u.email " +
                "FROM employee e " +
                "JOIN users u ON e.user_id = u.id " +
                "WHERE e.user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee employee = new Employee();
                employee.setIdEmployee(rs.getInt("employee_id"));
                employee.setNom(rs.getString("nom"));
                employee.setPrenom(rs.getString("prenom"));
                employee.setEmail(rs.getString("email"));  // Ajout de l'email
                return employee;
            } else {
                System.err.println("❌ Aucun employé trouvé pour user_id = " + userId);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la récupération de l'employé : " + e.getMessage());
        }
        return null;
    }




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
        return 1; // ID RH par défaut si non trouvé
    }
    public void delete(int idPresence) {
        // Vérifier que l'utilisateur est un RH ou que l'employé supprime sa propre présence
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Erreur : Aucun utilisateur connecté !");
            return;
        }

        String selectQuery = "SELECT employee_id FROM presence WHERE id_presence = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
            ps.setInt(1, idPresence);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Erreur : Présence non trouvée !");
                return;
            }

            int employeeId = rs.getInt("employee_id");

            // Vérifier que l'utilisateur a le droit de supprimer (RH ou propriétaire de la présence)
            if (!currentUser.getUserType().equalsIgnoreCase("RH") && currentUser.getId() != employeeId) {
                System.out.println("❌ Erreur : Vous n'avez pas le droit de supprimer cette présence !");
                return;
            }

            // Supprimer la présence
            String deleteQuery = "DELETE FROM presence WHERE id_presence = ?";
            try (PreparedStatement deletePs = connection.prepareStatement(deleteQuery)) {
                deletePs.setInt(1, idPresence);
                int rowsAffected = deletePs.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("✅ Présence supprimée avec succès !");
                } else {
                    System.out.println("❌ Erreur : Aucun enregistrement supprimé !");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de la présence : " + e.getMessage());
        }
    }


    public void update(Presence presence) {
        // Vérifier que l'utilisateur est un RH
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null || !SessionManager.getInstance().isUserType("RH")) {
            System.out.println("❌ Erreur : Seul un responsable RH peut modifier une présence !");
            return;
        }

        String query = "UPDATE presence SET date = ?, heureArrive = ?, heureDepart = ? WHERE id_presence = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(presence.getDate().getTime())); // Date
            ps.setTime(2, new java.sql.Time(presence.getHeureArrive().getTime())); // Heure d'arrivée
            ps.setTime(3, new java.sql.Time(presence.getHeureDepart().getTime())); // Heure de départ
            ps.setInt(4, presence.getIdPresence()); // ID présence

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Présence mise à jour avec succès !");
            } else {
                System.out.println("❌ Erreur : Aucun enregistrement mis à jour !");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de la présence : " + e.getMessage());
        }
    }


}

