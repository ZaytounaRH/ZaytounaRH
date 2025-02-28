package tn.esprit.services;

import tn.esprit.interfaces.Iservice;
import tn.esprit.models.Conge;
import tn.esprit.models.Presence;
import tn.esprit.models.User;
import tn.esprit.models.Employee;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PresenceService implements Iservice<Presence> {
    private Connection connection;

    // ✅ Constructeur avec connexion à la base de données
    public PresenceService(Connection connection) {
        this.connection = MyDatabase.getInstance().getCnx();
    }



    @Override
    public void add(Presence presence) {
        // Vérification utilisateur
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Aucun utilisateur connecté !");
            return;
        }
        System.out.println("🔍 Utilisateur connecté : ID = " + currentUser.getId());

        // Vérification connexion BDD
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("❌ Connexion à la base fermée !");
                return;
            } else {
                System.out.println("✅ Connexion BDD active !");
            }
        } catch (SQLException ex) {
            System.out.println("❌ Erreur vérification connexion BDD : " + ex.getMessage());
            return;
        }

        // Vérification récupération de l'employé
        Employee employee = getEmployeeById(currentUser.getId());
        if (employee == null) {
            System.out.println("❌ Aucun employé associé à cet utilisateur !");
            return;
        } else {
            System.out.println("✅ Employé trouvé : " + employee.getNom() + " " + employee.getPrenom() + " (ID: " + employee.getIdEmployee() + ")");
        }

        // Vérification présence
        presence.setEmployee(employee);
        if (presence.getEmployee() == null) {
            System.out.println("❌ L'employé est null après l'affectation !");
            return;
        }

        System.out.println("ℹ️ ID Employé avant insertion : " + presence.getEmployee().getIdEmployee());

        // Insertion en base
        String sql = "INSERT INTO presence (date, heureArrivee, heureDepart, employee_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(presence.getDate().getTime()));
            ps.setTime(2, presence.getHeureArrivee());
            ps.setTime(3, presence.getHeureDepart());
            ps.setInt(4, presence.getEmployee().getIdEmployee());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Présence ajoutée avec succès !");
            } else {
                System.out.println("⚠️ Aucun enregistrement ajouté !");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de l'ajout de la présence : " + e.getMessage());
            e.printStackTrace();
        }
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
    public List<Presence> getAll() {
        List<Presence> presences = new ArrayList<>();
        return presences;
    }

        public void update(Presence presence) {}

    public void remove(int idpresence){}



}
