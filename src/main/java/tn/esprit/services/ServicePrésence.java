package tn.esprit.services;

import tn.esprit.models.présence;
import tn.esprit.models.Congé;
import tn.esprit.models.employe;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicePrésence {
    private Connection cnx;

    // Constructeur pour initialiser la connexion
    public ServicePrésence() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Fonction pour ajouter une présence
    public void add(présence présence) {
        String qry = "INSERT INTO présence(date, heureArrivé, heureDepart, statut, idEmploye, id_congé) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setDate(1, new java.sql.Date(présence.getDate().getTime()));
            pstm.setTime(2, new java.sql.Time(présence.getHeureArrivé().getTime()));
            pstm.setTime(3, new java.sql.Time(présence.getHeureDepart().getTime()));
            pstm.setString(4, présence.getStatut());
            pstm.setInt(5, présence.getIdEmploye());

            // Si un congé est associé à la présence
            if (présence.getCongé() != null) {
                pstm.setInt(6, présence.getCongé().getId_congé());
            } else {
                pstm.setNull(6, java.sql.Types.INTEGER);
            }

            int rowsInserted = pstm.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Présence ajoutée avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la présence : " + e.getMessage());
        }
    }

    public employe getEmployeById(int idEmploye) {
        employe emp = null;
        String qry = "SELECT * FROM employe WHERE idEmploye = ?"; // Vérifie que 'employe' est le bon nom

        try (PreparedStatement pst = cnx.prepareStatement(qry)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    emp = new employe(); // Utilisation de la convention camelCase
                    emp.setIdEmploye(rs.getInt("idEmploye"));
                    emp.setNom(rs.getString("nom"));
                    emp.setPrenom(rs.getString("prenom"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'employé : " + e.getMessage());
        }
        return emp;
    }



    // Fonction pour récupérer toutes les présences
    public List<présence> getAll() {
        List<présence> présences = new ArrayList<>();
        String qry = "SELECT * FROM présence";

        // Charger tous les congés associés dans une map
        Map<Integer, Congé> congrèsMap = loadCongésMap();

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                présence presence = new présence();
                presence.setId_présence(rs.getInt("id_présence"));
                presence.setDate(rs.getDate("date"));
                presence.setHeureArrivé(rs.getTime("heureArrivé"));
                presence.setHeureDepart(rs.getTime("heureDepart"));
                presence.setStatut(rs.getString("statut"));
                presence.setIdEmploye(rs.getInt("idEmploye"));

                // Récupérer l'employé par ID
                employe emp = getEmployeById(rs.getInt("idEmploye"));
                presence.setEmploye(emp);

                // Récupérer le congé associé à l'aide de la Map
                int idCongé = rs.getInt("id_congé");
                if (idCongé > 0 && congrèsMap.containsKey(idCongé)) {
                    presence.setCongé(congrèsMap.get(idCongé));
                }

                présences.add(presence);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des présences : " + e.getMessage());
        }

        return présences;
    }

    // Fonction pour récupérer tous les congés dans une Map
    private Map<Integer, Congé> loadCongésMap() {
        Map<Integer, Congé> congrèsMap = new HashMap<>();
        String qry = "SELECT * FROM congé";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Congé congé = new Congé();
                congé.setId_congé(rs.getInt("id_congé"));
                congé.setDateDebut(rs.getDate("dateDebut"));
                congé.setDateFin(rs.getDate("dateFin"));
                congé.setMotif(rs.getString("motif"));
                congrèsMap.put(congé.getId_congé(), congé);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des congés : " + e.getMessage());
        }
        return congrèsMap;
    }

    // Optionnel: Fonction pour récupérer un congé par son ID (si nécessaire pour d'autres usages)
    public Congé getCongéById(int idCongé) {
        Congé congé = null;
        String qry = "SELECT * FROM congé WHERE id_congé = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, idCongé);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                congé = new Congé();
                congé.setId_congé(rs.getInt("id_congé"));
                congé.setDateDebut(rs.getDate("dateDebut"));
                congé.setDateFin(rs.getDate("dateFin"));
                congé.setMotif(rs.getString("motif"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du congé : " + e.getMessage());
        }
        return congé;
    }

    // Fonction pour mettre à jour une présence
    public void update(présence présence) {
        String qry = "UPDATE présence SET date=?, heureArrivé=?, heureDepart=?, statut=?, idEmploye=?, id_congé=? WHERE id_présence=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setDate(1, new java.sql.Date(présence.getDate().getTime()));
            pstm.setTime(2, new java.sql.Time(présence.getHeureArrivé().getTime()));
            pstm.setTime(3, new java.sql.Time(présence.getHeureDepart().getTime()));
            pstm.setString(4, présence.getStatut());
            pstm.setInt(5, présence.getIdEmploye());

            if (présence.getCongé() != null) {
                pstm.setInt(6, présence.getCongé().getId_congé());
            } else {
                pstm.setNull(6, java.sql.Types.INTEGER);
            }

            pstm.setInt(7, présence.getId_présence()); // ID de la présence à mettre à jour

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Présence mise à jour avec succès !");
            } else {
                System.out.println("Aucune mise à jour effectuée, vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la présence : " + e.getMessage());
        }
    }
    public présence getById(int id) {
        présence presence = null;
        String query = "SELECT * FROM présence WHERE id_présence = ?";

        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                presence = new présence(
                        rs.getInt("id_présence"),
                        rs.getDate("date"),
                        rs.getTime("heureArrivé"),
                        rs.getTime("heureDepart"),
                        rs.getString("statut"),
                        rs.getInt("idEmploye")
                );

                // Vérifier s'il y a un congé associé
                int idCongé = rs.getInt("idCongé");
                if (idCongé != 0) {  // Supposons que 0 signifie "aucun congé"
                    Congé congé = new Congé();
                    congé.setId_congé(idCongé);
                    presence.setCongé(congé);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la présence : " + e.getMessage());
        }

        return presence;
    }

    // Fonction pour supprimer une présence
    public void delete(int idPrésence) {
        String qry = "DELETE FROM présence WHERE id_présence=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, idPrésence);

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Présence supprimée avec succès !");
            } else {
                System.out.println("Aucune présence trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la présence : " + e.getMessage());
        }
    }

}
