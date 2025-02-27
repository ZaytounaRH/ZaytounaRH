package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Reclamation;
import tn.esprit.utils.MyDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import java.sql.*;

public class ServiceReclamation implements IService<Reclamation> {
    private Connection cnx;

    // Constructeur sans paramètre, utilisation du singleton MyDatabase pour obtenir la connexion
    public ServiceReclamation() {
        cnx = MyDatabase.getInstance().getCnx();  // Obtention de la connexion
    }

    @Override
    public void add(Reclamation reclamation) {
        String qry = "INSERT INTO `reclamation`(`titre`, `description`, `incidentType`, `dateSoumission`, `statut`, `priorite`, `pieceJointe`, `idAssurance`) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, reclamation.getTitre());
            pstm.setString(2, reclamation.getDescription());
            pstm.setString(3, reclamation.getIncidentType().name());
            pstm.setDate(4, Date.valueOf(reclamation.getDateSoumission()));
            pstm.setString(5, reclamation.getStatut().name());
            pstm.setString(6, reclamation.getPriorite().name());
            pstm.setString(7, reclamation.getPieceJointe());
            if (reclamation.getIdAssurance() != null) {
                pstm.setInt(8, reclamation.getIdAssurance());
            } else {
                pstm.setNull(8, Types.INTEGER);
            }

            pstm.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réclamation: " + e.getMessage());
        }
    }


    @Override
    public List<Reclamation> getAll() {
        List<Reclamation> reclamations = new ArrayList<>();
        String qry = "SELECT * FROM `reclamation`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Reclamation p = new Reclamation();
                p.setIdR(rs.getInt("idR"));
                p.setTitre(rs.getString("titre"));
                p.setDescription(rs.getString("description"));

                String incidentTypeStr = rs.getString("incidentType");
                if (incidentTypeStr != null) {
                    p.setIncidentType(Reclamation.IncidentType.valueOf(incidentTypeStr));
                }

                p.setDateSoumission(rs.getDate("dateSoumission").toLocalDate());
                p.setStatut(Reclamation.StatutReclamation.valueOf(rs.getString("statut")));
                p.setPriorite(Reclamation.PrioriteReclamation.valueOf(rs.getString("priorite")));
                p.setPieceJointe(rs.getString("pieceJointe"));

                // Récupération de idAssurance
                int idAssurance = rs.getInt("idAssurance");
                if (!rs.wasNull()) {
                    p.setIdAssurance(idAssurance);
                }

                reclamations.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reclamations;
    }


    @Override
    public void update(Reclamation reclamation) {
        String qry = "UPDATE `reclamation` SET `titre`=?, `description`=?, `incidentType`=?, `dateSoumission`=?, `statut`=?, `priorite`=?, `pieceJointe`=?, `idAssurance`=? WHERE `idR`=?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, reclamation.getTitre());
            pstm.setString(2, reclamation.getDescription());

            if (reclamation.getIncidentType() != null) {
                pstm.setString(3, reclamation.getIncidentType().name());
            } else {
                pstm.setNull(3, Types.VARCHAR);
            }

            pstm.setDate(4, Date.valueOf(reclamation.getDateSoumission()));
            pstm.setString(5, reclamation.getStatut().name());
            pstm.setString(6, reclamation.getPriorite().name());
            pstm.setString(7, reclamation.getPieceJointe());

            if (reclamation.getIdAssurance() != null) {
                pstm.setInt(8, reclamation.getIdAssurance());
            } else {
                pstm.setNull(8, Types.INTEGER);
            }

            pstm.setInt(9, reclamation.getIdR());

            pstm.executeUpdate();
            System.out.println("Réclamation mise à jour avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la réclamation: " + e.getMessage());
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

    public Map<String, Object> getStatistiquesReclamations() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Total des réclamations
            String qryTotal = "SELECT COUNT(*) AS total FROM reclamation";
            Statement stmTotal = cnx.createStatement();
            ResultSet rsTotal = stmTotal.executeQuery(qryTotal);
            rsTotal.next();
            int total = rsTotal.getInt("total");

            // Nombre de réclamations par statut
            String qryStatut = "SELECT statut, COUNT(*) AS count FROM reclamation GROUP BY statut";
            Statement stmStatut = cnx.createStatement();
            ResultSet rsStatut = stmStatut.executeQuery(qryStatut);

            int enAttente = 0, enCours = 0, resolu = 0;
            while (rsStatut.next()) {
                String statut = rsStatut.getString("statut");
                int count = rsStatut.getInt("count");
                switch (statut) {
                    case "EN_ATTENTE":
                        enAttente = count;
                        break;
                    case "EN_COURS":
                        enCours = count;
                        break;
                    case "RESOLU":
                        resolu = count;
                        break;
                }
            }

            // Calcul du pourcentage des réclamations résolues
            double pourcentageResolues = (total > 0) ? ((double) resolu / total) * 100 : 0;

            // Ajout des statistiques au Map
            stats.put("Total", total);
            stats.put("En Attente", enAttente);
            stats.put("En Cours", enCours);
            stats.put("Résolu", resolu);
            stats.put("Pourcentage Résolu", pourcentageResolues);

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des statistiques : " + e.getMessage());
        }

        return stats;
    }

}
