package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Reclamation;
import tn.esprit.utils.MyDatabase;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


import java.sql.*;

public class ServiceReclamation implements IService<Reclamation> {
    private Connection cnx;

    // Constructeur sans paramètre, utilisation du singleton MyDatabase pour obtenir la connexion
    public ServiceReclamation() {
        cnx = MyDatabase.getInstance().getCnx();  // Obtention de la connexion
    }

    public boolean exists(String titre, String description, String incidentType,
                          LocalDate dateSoumission, String statut, String priorite) {
        // Exemple d'implémentation avec une requête en utilisant JDBC ou une autre technologie de persistance.

        // Si vous utilisez JDBC :
        // Exemple de code pour une requête SQL
        String query = "SELECT COUNT(*) FROM reclamation WHERE titre = ? AND description = ? " +
                "AND incidentType = ? AND date_soumission = ? AND statut = ? AND priorite = ?";


        // Utilisation de votre méthode de gestion des connexions à la base de données
        // Exécution de la requête pour vérifier l'existence d'une réclamation
        int count = 0;

        try (Connection conn = MyDatabase.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, titre);
            stmt.setString(2, description);
            stmt.setString(3, incidentType);
            stmt.setObject(4, dateSoumission);
            stmt.setString(5, statut);
            stmt.setString(6, priorite);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestion d'erreur
        }

        return count > 0; // Si une réclamation identique existe, count sera supérieur à 0
    }


    @Override
    public void add(Reclamation reclamation) {
        String qry = "INSERT INTO `reclamation`(`titre`, `description`, `dateSoumission`, `statut`, `priorite`, `pieceJointe`) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, reclamation.getTitre());
            pstm.setString(2, reclamation.getDescription());
            pstm.setDate(3, Date.valueOf(reclamation.getDateSoumission()));
            pstm.setString(4, reclamation.getStatut().name());
            pstm.setString(5, reclamation.getPriorite().name());
            pstm.setString(6, reclamation.getPieceJointe());

            pstm.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réclamation: " + e.getMessage());
        }
    }

    @Override
    public List<Reclamation> getAll() {
        //create Qry sql
        //execution
        //Mapping data


        List<Reclamation> reclamations = new ArrayList<>();
        String qry ="SELECT * FROM `reclamation`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Reclamation p = new Reclamation();
                p.setIdR(rs.getInt("idR"));
                //p.setTitre(rs.getString(2));
                p.setTitre(rs.getString("titre"));
                p.setDescription(rs.getString("description"));
                p.setDateSoumission(rs.getDate("dateSoumission").toLocalDate());
                p.setStatut(Reclamation.StatutReclamation.valueOf(rs.getString("statut")));
                p.setPriorite(Reclamation.PrioriteReclamation.valueOf(rs.getString("priorite")));
                p.setPieceJointe(rs.getString("pieceJointe"));

                reclamations.add(p);
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reclamations;
    }

    @Override
    public void update(Reclamation reclamation) {
        String qry = "UPDATE `reclamation` SET `titre`=?, `description`=?, `dateSoumission`=?, `statut`=?, `priorite`=?, `pieceJointe`=? WHERE `idR`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, reclamation.getTitre());
            pstm.setString(2, reclamation.getDescription());
            pstm.setDate(3, Date.valueOf(reclamation.getDateSoumission()));
            pstm.setString(4, reclamation.getStatut().name());
            pstm.setString(5, reclamation.getPriorite().name());
            pstm.setString(6, reclamation.getPieceJointe());
            pstm.setInt(7, reclamation.getIdR());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Reclamation reclamation) {
        String qry = "DELETE FROM `reclamation` WHERE `idR`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, reclamation.getIdR());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
