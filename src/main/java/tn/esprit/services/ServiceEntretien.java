package tn.esprit.services;

import tn.esprit.interfaces.Iservice;
import tn.esprit.models.Entretien;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.Candidat;
import tn.esprit.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEntretien implements Iservice<Entretien> {
    private Connection cnx;

    public ServiceEntretien() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Entretien entretien) {
        String qry = "INSERT INTO `Entretien`(`dateEntretien`, `heureEntretien`, `typeEntretien`, `statut`, `commentaire`, `idOffre`,`idCandidat`) VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setDate(1, Date.valueOf(entretien.getDateEntretien()));  // LocalDate -> Date
            pstm.setTime(2, Time.valueOf(entretien.getHeureEntretien())); // LocalTime -> Time
            pstm.setString(3, entretien.getTypeEntretien().name());
            pstm.setString(4, entretien.getStatut().name());
            pstm.setString(5, entretien.getCommentaire());
            pstm.setInt(6, entretien.getOffreEmploi().getIdOffre());  // Association avec l'OffreEmploi

            // Vérification de Candidat et assignation
            if (entretien.getCandidat() != null && entretien.getCandidat().getIdCandidat() > 0) {
                pstm.setInt(7, entretien.getCandidat().getIdCandidat());
            } else {
                pstm.setNull(7, Types.INTEGER);  // Si aucun Candidat n'est sélectionné
            }

            // Exécuter la requête d'insertion
            pstm.executeUpdate();

            // Récupérer l'ID généré pour l'entretien (auto-incrémenté)
            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    int idEntretien = rs.getInt(1);
                    entretien.setIdEntretien(idEntretien);  // L'ID auto-incrémenté est assigné à l'objet
                }
            }

            System.out.println("Entretien ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'entretien : " + e.getMessage());
        }
    }

    @Override
    public List<Entretien> getAll() {
        List<Entretien> entretiens = new ArrayList<>();
        String qry = "SELECT * FROM `Entretien`";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Entretien entretien = new Entretien();
                entretien.setIdEntretien(rs.getInt("idEntretien"));
                entretien.setDateEntretien(rs.getDate("dateEntretien").toLocalDate());
                entretien.setHeureEntretien(rs.getTime("heureEntretien").toLocalTime());
                entretien.setTypeEntretien(Entretien.TypeEntretien.valueOf(rs.getString("typeEntretien")));
                entretien.setStatut(Entretien.StatutEntretien.valueOf(rs.getString("statut")));
                entretien.setCommentaire(rs.getString("commentaire"));

                // Charger l'offre d'emploi associée
                int idOffre = rs.getInt("idOffre");
                ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
                OffreEmploi offreEmploi = serviceOffreEmploi.getById(idOffre);
                entretien.setOffreEmploi(offreEmploi);

                // Charger Candidat
                int idCandidat = rs.getInt("idCandidat");
                Candidat candidat = null;
                if (idCandidat > 0) {
                    String candidatQry = "SELECT nom FROM candidat WHERE idCandidat = ?";
                    try (PreparedStatement pstmtCandidat = cnx.prepareStatement(candidatQry)) {
                        pstmtCandidat.setInt(1, idCandidat);
                        try (ResultSet rsCandidat = pstmtCandidat.executeQuery()) {
                            if (rsCandidat.next()) {
                                candidat = new Candidat(idCandidat, rsCandidat.getString("nom"));
                            }
                        }
                    }
                }
                entretien.setCandidat(candidat);

                entretiens.add(entretien);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des entretiens : " + e.getMessage());
        }

        return entretiens;
    }

    @Override
    public void update(Entretien entretien) {
        String qry = "UPDATE `Entretien` SET `dateEntretien` = ?, `heureEntretien` = ?, `typeEntretien` = ?, `statut` = ?, `commentaire` = ?, `idOffre` = ? , `idCandidat` = ? WHERE `idEntretien` = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setDate(1, Date.valueOf(entretien.getDateEntretien()));  // LocalDate -> Date
            pstm.setTime(2, Time.valueOf(entretien.getHeureEntretien())); // LocalTime -> Time
            pstm.setString(3, entretien.getTypeEntretien().name());
            pstm.setString(4, entretien.getStatut().name());
            pstm.setString(5, entretien.getCommentaire());
            pstm.setInt(6, entretien.getOffreEmploi().getIdOffre());  // Association avec l'OffreEmploi
            pstm.setInt(7, entretien.getIdEntretien());
            // Mise à jour de Candidat
            if (entretien.getCandidat() != null && entretien.getCandidat().getIdCandidat() > 0) {
                pstm.setInt(8, entretien.getCandidat().getIdCandidat()); // Lier l'ID de Candidat à l'update
            } else {
                pstm.setNull(8, Types.INTEGER); // Ne pas lier un Candidat si aucun Candidat n'est défini
            }
            pstm.executeUpdate();
            System.out.println("Entretien mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'entretien : " + e.getMessage());
        }
    }

    @Override
    public void remove(int idEntretien) {
        String qry = "DELETE FROM `Entretien` WHERE `idEntretien` = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, idEntretien);
            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Entretien supprimé avec succès !");
            } else {
                System.out.println("Aucun entretien trouvé avec cet identifiant : " + idEntretien);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'entretien : " + e.getMessage());
        }
    }

    // Méthode pour récupérer un entretien par son ID
    public Entretien getById(int idEntretien) {
        Entretien entretien = null;
        String qry = "SELECT * FROM `Entretien` WHERE `idEntretien` = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, idEntretien);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    entretien = new Entretien();
                    entretien.setIdEntretien(rs.getInt("idEntretien"));
                    entretien.setDateEntretien(rs.getDate("dateEntretien").toLocalDate());
                    entretien.setHeureEntretien(rs.getTime("heureEntretien").toLocalTime());
                    entretien.setTypeEntretien(Entretien.TypeEntretien.valueOf(rs.getString("typeEntretien")));
                    entretien.setStatut(Entretien.StatutEntretien.valueOf(rs.getString("statut")));
                    entretien.setCommentaire(rs.getString("commentaire"));
                    // Charger l'offre d'emploi associée
                    int idOffre = rs.getInt("idOffre");
                    ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
                    OffreEmploi offreEmploi = serviceOffreEmploi.getById(idOffre);
                    entretien.setOffreEmploi(offreEmploi);
                    // Charger Candidat
                    int idCandidat = rs.getInt("idCandidat");
                    Candidat candidat = null;
                    if (idCandidat > 0) {
                        String candidatQry = "SELECT nom FROM candidat WHERE idCandidat = ?";
                        try (PreparedStatement pstmtCandidat = cnx.prepareStatement(candidatQry)) {
                            pstmtCandidat.setInt(1, idCandidat);
                            try (ResultSet rsCandidat = pstmtCandidat.executeQuery()) {
                                if (rsCandidat.next()) {
                                    candidat = new Candidat(idCandidat, rsCandidat.getString("nom"));
                                }
                            }
                        }
                    }
                    entretien.setCandidat(candidat);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'entretien : " + e.getMessage());
        }

        return entretien;
    }

    // Méthode pour supprimer tous les entretiens liés à une offre
    public void removeByOffre(int idOffre) {
        String qry = "DELETE FROM `Entretien` WHERE `idOffre` = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, idOffre);
            pstm.executeUpdate();
            System.out.println("Tous les entretiens associés à l'offre ont été supprimés.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des entretiens liés à l'offre : " + e.getMessage());
        }
    }

    public List<Entretien> getAllByOffre(int idOffre) {
        List<Entretien> entretiens = new ArrayList<>();
        String qry = "SELECT e.*, c.* FROM Entretien e LEFT JOIN Candidat c ON e.idCandidat = c.idCandidat WHERE e.idOffre = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, idOffre);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Entretien entretien = new Entretien();
                entretien.setIdEntretien(rs.getInt("idEntretien"));
                entretien.setDateEntretien(rs.getDate("dateEntretien").toLocalDate());
                entretien.setHeureEntretien(rs.getTime("heureEntretien").toLocalTime());
                entretien.setTypeEntretien(Entretien.TypeEntretien.valueOf(rs.getString("typeEntretien")));
                entretien.setStatut(Entretien.StatutEntretien.valueOf(rs.getString("statut")));
                entretien.setCommentaire(rs.getString("commentaire"));

                // Associer l'offre d'emploi
                OffreEmploi offreEmploi = new OffreEmploi();
                offreEmploi.setIdOffre(rs.getInt("idOffre"));
                entretien.setOffreEmploi(offreEmploi);

                // Associer le candidat
                Candidat candidat = new Candidat();
                candidat.setIdCandidat(rs.getInt("idCandidat"));
                candidat.setNom(rs.getString("nom"));
             //   System.out.println("Candidat ID: " + candidat.getIdCandidat() + " Nom: " + candidat.getNom()); // Add debugging output

                entretien.setCandidat(candidat);

                entretiens.add(entretien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entretiens;
    }

}
