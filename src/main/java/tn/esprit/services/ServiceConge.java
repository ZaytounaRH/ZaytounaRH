package tn.esprit.services;
import java.util.Date;
import tn.esprit.utils.MyDatabase;
import java.sql.Connection;
import tn.esprit.utils.MyDatabase;
import tn.esprit.models.Conge;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import tn.esprit.interfaces.Iservice;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.models.User;


public class ServiceConge implements Iservice<Conge> {
    private Connection connection;

    // ✅ Correction : Ajouter un constructeur qui prend une connexion en paramètre
    public ServiceConge(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Conge conge) {
        // Vérification pour éviter une NullPointerException
        if (conge.getDateDebut() == null || conge.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être null !");
        }

        String query = "INSERT INTO conge (dateDebut, dateFin, motif, statut, user) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            // Convertir java.util.Date en java.sql.Date
            java.sql.Date sqlDateDebut = new java.sql.Date(conge.getDateDebut().getTime());
            java.sql.Date sqlDateFin = new java.sql.Date(conge.getDateFin().getTime());

            pst.setDate(1, sqlDateDebut);
            pst.setDate(2, sqlDateFin);
            pst.setString(3, conge.getMotif());
            pst.setString(4, conge.getStatut());
            pst.setInt(5, conge.getUser().getId()); // Utiliser l'ID de l'utilisateur

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Congé ajouté avec succès !");
            } else {
                System.out.println("⚠️ Échec de l'ajout du congé.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du congé : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Conge> getAll() {
        List<Conge> conges = new ArrayList<>();
        String query = """
        SELECT 
            c.id_conge, c.dateDebut, c.dateFin, c.motif, c.statut, 
            e.nom AS employe_nom, 
            e.prenom AS employe_prenom, 
            e.user_type AS employe_type,
            r.nom AS rh_nom, 
            r.prenom AS rh_prenom, 
            r.user_type AS rh_type
        FROM conge c
        JOIN users e ON c.user = e.id AND e.user_type = 'Employee'
        LEFT JOIN rh_employee re ON re.employee_id = e.id
        LEFT JOIN users r ON re.rh_id = r.id AND r.user_type = 'RH';
    """;

        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // 🔹 Création de l'employé
                User employe = new User(
                        rs.getString("employe_nom"),
                        rs.getString("employe_prenom"),
                        rs.getString("employe_type")
                );

                // 🔹 Création du RH (si existant)
                User rh = null;
                if (rs.getString("rh_nom") != null) {
                    rh = new User(rs.getString("rh_nom"), rs.getString("rh_prenom"), rs.getString("rh_type"));
                }

                // 🔹 Création de l'objet Congé
                Conge conge = new Conge(
                        rs.getInt("id_conge"),
                        rs.getDate("dateDebut"),
                        rs.getDate("dateFin"),
                        rs.getString("motif"),
                        rs.getString("statut"),
                        employe,
                        rh  // Ajout du RH en paramètre s'il existe
                );

                conges.add(conge);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des congés : " + e.getMessage());
        }

        return conges;
    }




}



