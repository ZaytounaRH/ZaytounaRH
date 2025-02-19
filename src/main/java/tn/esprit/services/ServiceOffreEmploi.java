package tn.esprit.services;

import tn.esprit.interfaces.Iservice;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.models.Entretien;
import tn.esprit.models.RH;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffreEmploi implements Iservice<OffreEmploi> {
    private Connection cnx;

    public ServiceOffreEmploi() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(OffreEmploi offreEmploi) {
        if (offreEmploi.getDatePublication() != null) {
            String qry = "INSERT INTO `OffreEmploi`(`titreOffre`, `description`, `datePublication`, `salaire`, `statut`, `idRH`) VALUES (?,?,?,?,?,?)";

            try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setString(1, offreEmploi.getTitreOffre());
                pstm.setString(2, offreEmploi.getDescription());
                pstm.setDate(3, new java.sql.Date(offreEmploi.getDatePublication().getTime()));
                pstm.setDouble(4, offreEmploi.getSalaire());
                pstm.setString(5, offreEmploi.getStatut().name());

                // Vérification de l'ID RH
                if (offreEmploi.getRh() != null && offreEmploi.getRh().getIdRH() > 0) {
                    pstm.setInt(6, offreEmploi.getRh().getIdRH()); // Lier l'ID de RH à l'insertion
                } else {
                    System.out.println("RH manquant ou invalide pour l'offre.");
                    pstm.setNull(6, Types.INTEGER); // Ne pas lier un RH si aucun RH n'est défini
                }

                pstm.executeUpdate();

                // Récupérer l'ID généré pour l'OffreEmploi
                try (ResultSet rs = pstm.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idOffre = rs.getInt(1);
                        offreEmploi.setIdOffre(idOffre);
                    }
                }

                // Vérification et ajout des entretiens
                if (offreEmploi.getEntretiens() != null) {
                    ServiceEntretien serviceEntretien = new ServiceEntretien();
                    for (Entretien entretien : offreEmploi.getEntretiens()) {
                        entretien.setOffreEmploi(offreEmploi); // Associer l'entretien à l'offre
                        serviceEntretien.add(entretien);
                    }
                }

                System.out.println("Offre d'emploi ajoutée avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout de l'offre d'emploi : " + e.getMessage());
            }
        } else {
            System.out.println("Date invalide, l'ajout de l'offre d'emploi est annulé.");
        }
    }

    @Override
    public List<OffreEmploi> getAll() {
        List<OffreEmploi> offres = new ArrayList<>();
        String qry = "SELECT * FROM `OffreEmploi`";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                OffreEmploi offre = new OffreEmploi();
                offre.setIdOffre(rs.getInt("idOffre"));
                offre.setTitreOffre(rs.getString("titreOffre"));
                offre.setDescription(rs.getString("description"));
                offre.setDatePublication(rs.getDate("datePublication"));
                offre.setSalaire(rs.getDouble("salaire"));

                String statutString = rs.getString("statut").toUpperCase();
                try {
                    StatutOffre statutEnum = StatutOffre.valueOf(statutString);
                    offre.setStatut(statutEnum);
                } catch (IllegalArgumentException e) {
                    System.out.println("Statut invalide trouvé : " + statutString + ". Statut par défaut 'ENCOURS' utilisé.");
                    offre.setStatut(StatutOffre.ENCOURS);
                }

                // Charger RH
                int idRH = rs.getInt("idRH");
                RH rh = null;
                if (idRH > 0) {
                    String rhQry = "SELECT nom FROM rh WHERE idRH = ?";
                    try (PreparedStatement pstmtRh = cnx.prepareStatement(rhQry)) {
                        pstmtRh.setInt(1, idRH);
                        try (ResultSet rsRh = pstmtRh.executeQuery()) {
                            if (rsRh.next()) {
                                rh = new RH(idRH, rsRh.getString("nom"));
                            }
                        }
                    }
                }
                offre.setRh(rh);

                // Charger les entretiens
                ServiceEntretien serviceEntretien = new ServiceEntretien();
                List<Entretien> entretiens = serviceEntretien.getAllByOffre(offre.getIdOffre());
                offre.setEntretiens(entretiens != null ? entretiens : new ArrayList<>());

                offres.add(offre);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des offres : " + e.getMessage());
        }

        return offres;
    }

    @Override
    public void update(OffreEmploi offreEmploi) {
        String statut = offreEmploi.getStatut().name().toUpperCase();

        if (StatutOffre.valueOf(statut) != null) {
            String qry = "UPDATE `OffreEmploi` SET `titreOffre` = ?, `description` = ?, `datePublication` = ?, `salaire` = ?, `statut` = ?, `idRH` = ? WHERE `idOffre` = ?";

            try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
                pstm.setString(1, offreEmploi.getTitreOffre());
                pstm.setString(2, offreEmploi.getDescription());
                pstm.setDate(3, new java.sql.Date(offreEmploi.getDatePublication().getTime()));
                pstm.setDouble(4, offreEmploi.getSalaire());
                pstm.setString(5, statut);

                // Mise à jour de RH
                if (offreEmploi.getRh() != null && offreEmploi.getRh().getIdRH() > 0) {
                    pstm.setInt(6, offreEmploi.getRh().getIdRH()); // Lier l'ID de RH à l'update
                } else {
                    pstm.setNull(6, Types.INTEGER); // Ne pas lier un RH si aucun RH n'est défini
                }

                pstm.setInt(7, offreEmploi.getIdOffre());

                int rowsAffected = pstm.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Offre d'emploi mise à jour avec succès !");
                } else {
                    System.out.println("Aucune offre d'emploi trouvée avec cet ID : " + offreEmploi.getIdOffre());
                }

                // Mise à jour des entretiens
                if (offreEmploi.getEntretiens() != null) {
                    ServiceEntretien serviceEntretien = new ServiceEntretien();
                    for (Entretien entretien : offreEmploi.getEntretiens()) {
                        entretien.setOffreEmploi(offreEmploi);
                        serviceEntretien.update(entretien);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la mise à jour de l'offre d'emploi : " + e.getMessage());
            }
        } else {
            System.out.println("Statut invalide. Veuillez choisir parmi : " + StatutOffre.values());
        }
    }

    @Override
    public void remove(int idOffre) {
        String qry = "DELETE FROM `OffreEmploi` WHERE `idOffre` = ?";

        try {
            ServiceEntretien serviceEntretien = new ServiceEntretien();
            serviceEntretien.removeByOffre(idOffre);

            try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
                pstm.setInt(1, idOffre);
                int rowsAffected = pstm.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Offre d'emploi supprimée avec succès !");
                } else {
                    System.out.println("Aucune offre trouvée avec cet ID : " + idOffre);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'offre d'emploi : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public OffreEmploi getById(int idOffre) {
        OffreEmploi offreEmploi = null;
        String qry = "SELECT * FROM `OffreEmploi` WHERE `idOffre` = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, idOffre);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    offreEmploi = new OffreEmploi();
                    offreEmploi.setIdOffre(rs.getInt("idOffre"));
                    offreEmploi.setTitreOffre(rs.getString("titreOffre"));
                    offreEmploi.setDescription(rs.getString("description"));
                    offreEmploi.setDatePublication(rs.getDate("datePublication"));
                    offreEmploi.setSalaire(rs.getDouble("salaire"));
                    offreEmploi.setStatut(StatutOffre.valueOf(rs.getString("statut").toUpperCase()));

                    // Charger RH
                    int idRH = rs.getInt("idRH");
                    RH rh = null;
                    if (idRH > 0) {
                        String rhQry = "SELECT nom FROM rh WHERE idRH = ?";
                        try (PreparedStatement pstmtRh = cnx.prepareStatement(rhQry)) {
                            pstmtRh.setInt(1, idRH);
                            try (ResultSet rsRh = pstmtRh.executeQuery()) {
                                if (rsRh.next()) {
                                    rh = new RH(idRH, rsRh.getString("nom"));
                                }
                            }
                        }
                    }
                    offreEmploi.setRh(rh);

                    // Charger les entretiens
                    ServiceEntretien serviceEntretien = new ServiceEntretien();
                    List<Entretien> entretiens = serviceEntretien.getAllByOffre(idOffre);
                    offreEmploi.setEntretiens(entretiens != null ? entretiens : new ArrayList<>());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'offre : " + e.getMessage());
        }

        return offreEmploi;
    }


    ////////////////////////////////////////////////////
    public List<OffreEmploi> getOffresByRH(int idRH) {
        List<OffreEmploi> offres = new ArrayList<>();
        String query = "SELECT * FROM OffreEmploi WHERE idRH = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idRH);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    OffreEmploi offre = new OffreEmploi();
                    offre.setIdOffre(rs.getInt("idOffre"));
                    offre.setTitreOffre(rs.getString("titreOffre"));
                    offre.setDescription(rs.getString("description"));
                    offre.setDatePublication(rs.getDate("datePublication"));
                    offre.setSalaire(rs.getDouble("salaire"));
                    offre.setStatut(StatutOffre.valueOf(rs.getString("statut").toUpperCase()));

                    // Associer le RH à l'offre
                    RH rh = null;
                    String rhQry = "SELECT * FROM rh WHERE idRH = ?";
                    try (PreparedStatement pstmtRh = cnx.prepareStatement(rhQry)) {
                        pstmtRh.setInt(1, idRH);
                        try (ResultSet rsRh = pstmtRh.executeQuery()) {
                            if (rsRh.next()) {
                                rh = new RH(idRH, rsRh.getString("nom"));
                            }
                        }
                    }

                    if (rh != null) {
                        offre.setRh(rh); // Si RH trouvé, l'associer à l'offre
                    } else {
                        System.out.println("RH avec ID " + idRH + " non trouvé.");
                    }

                    offres.add(offre);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des offres : " + e.getMessage());
        }

        return offres;
    }




    ///////////////////////////////////////////////////////////////////
    public List<RH> getAllRHFromOffres() {
        List<RH> rhList = new ArrayList<>();
        String query = "SELECT DISTINCT rh.idRH, rh.nom FROM OffreEmploi oe JOIN rh ON oe.idRH = rh.idRH";

        try (PreparedStatement pst = cnx.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                RH rh = new RH();
                rh.setIdRH(rs.getInt("idRH"));
                rh.setNom(rs.getString("nom"));
                rhList.add(rh);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des responsables RH : " + e.getMessage());
        }

        return rhList;
    }


}
