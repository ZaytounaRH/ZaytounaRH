package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
import tn.esprit.models.Formation;
import tn.esprit.models.Employe;
import tn.esprit.models.Rh;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements IService<Formation> {
    private final Connection cnx ;

    public ServiceFormation() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Formation formation) {
        if (!isValidFormation(formation)) {
            System.out.println("Erreur : données invalides, insertion annulée !");
            return;
        }
        if(isFormationExists(formation.getNomFormation(), formation.getDescriptionFormation())){
            System.out.println("Erreur : Une formation avec le même nom et la même description existe déjà !");
            return;
        }

        String qry = "INSERT INTO `formation`( `nomFormation`, `descriptionFormation`, `dureeFormation`, `idEmploye`, `idCertif`,`idRH`  ) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getDescriptionFormation());
            pstm.setString(3, formation.getDureeFormation());
            pstm.setInt(4, formation.getEmploye().getIdEmploye());

            pstm.setInt(5,formation.getCertification().getIdCertif());
            pstm.setInt(6, formation.getRh().getIdRh());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Formation> getAll() {
        List<Formation> formations = new ArrayList<>();
        String qry = "SELECT * FROM `formation`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Formation formation = new Formation();
                formation.setIdFormation(rs.getInt("idFormation"));
                formation.setNomFormation(rs.getString("nomFormation"));
                formation.setDescriptionFormation(rs.getString("descriptionFormation"));
                formation.setDureeFormation(rs.getString("dureeFormation"));


                int idEmploye = rs.getInt("idEmploye");
                int idRH = rs.getInt("idRH");
                int idCertif = rs.getInt("idCertif");



                Employe employe = null;
                Rh rh = new Rh(idRH);
                Certification certification = null;

                if (idEmploye > 0) {
                    String employeQry = "SELECT nom FROM employe WHERE idEmploye = ?"; //table employe n'a pas de nom tbale user a le nom prenom etc donc pour recupere le nom de l'employe il faut verifier que id_user==id_employe
                    PreparedStatement pstmtEmploye = cnx.prepareStatement(employeQry);
                    pstmtEmploye.setInt(1, idEmploye);
                    ResultSet rsEmploye = pstmtEmploye.executeQuery();

                    if (rsEmploye.next()) {
                        employe = new Employe(idEmploye, rsEmploye.getString("nom"));
                    }


                }

                if (idRH > 0) {
                    String rhQry = "SELECT nom FROM rh WHERE idRH = ?";
                    PreparedStatement pstmtRh = cnx.prepareStatement(rhQry);
                    pstmtRh.setInt(1, idRH);
                    ResultSet rsRh = pstmtRh.executeQuery();

                    if (rsRh.next()) {
                       rh = new Rh(idRH, rsRh.getString("nom"));
                    }
                }

                if (idCertif > 0) {
                    // Récupérer l'objet Certification complet depuis la base de données
                    String certifQry = "SELECT * FROM certification WHERE idCertif = ?";
                    PreparedStatement pstmt = cnx.prepareStatement(certifQry);
                    pstmt.setInt(1, idCertif);
                    ResultSet rsCertif = pstmt.executeQuery();

                    if (rsCertif.next()) {
                        certification = new Certification(
                                rsCertif.getInt("idCertif"),
                                rsCertif.getString("titreCertif") // Assurez-vous que ce champ existe bien
                        );
                    }
                }


                formation.setEmploye(employe);
                formation.setRh(rh);
                formation.setCertification(certification);

                formations.add(formation);
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return formations;
    }

    @Override
    public void update(Formation formation) {
        if (!isValidFormation(formation)) {
            System.out.println("Erreur : données invalides, update annulé !");
            return;
        }
        String qry = "UPDATE `formation` SET `nomFormation` = ?, `descriptionFormation` = ?, `dureeFormation` = ?, `idEmploye` = ?, `idCertif`= ?, `idRH` = ? WHERE `idFormation` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getDescriptionFormation());
            pstm.setString(3, formation.getDureeFormation());


            pstm.setInt(4, formation.getEmploye().getIdEmploye());
            pstm.setInt(5, formation.getCertification().getIdCertif());
            pstm.setInt(6, formation.getRh().getIdRh());
            pstm.setInt(7, formation.getIdFormation());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Formation mise à jour avec succès !");
            } else {
                System.out.println("Aucun enregistrement mis à jour. Vérifie l'ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Formation formation) {
        String qry = "DELETE FROM formation WHERE idFormation = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, formation.getIdFormation());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Formation supprimée avec succès !");
            } else {
                System.out.println("Aucun enregistrement trouvé pour suppression.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }


    public static boolean isValidFormation(Formation formation) {
        if (formation.getNomFormation() == null || formation.getNomFormation().isEmpty()) {
            System.out.println("Le nom de la formation ne peut pas être vide !");
            return false;
        }
        if (formation.getDescriptionFormation() == null || formation.getDescriptionFormation().length() < 10) {
            System.out.println("La description doit contenir au moins 10 caractères !");
            return false;
        }
        if (!formation.getDureeFormation().matches("\\d+ jours")) {
            System.out.println("La durée doit être au format 'X jours' !");
            return false;
        }

        if (formation.getRh().getIdRh() <= 0) {
            System.out.println("rh responsable manquant ! ");
        }
        if  (formation.getEmploye().getIdEmploye() <= 0) {
            System.out.println("employe participant manquant ! ");
        }

        return true;
    }
    public boolean isFormationExists(String nomFormation, String descriptionFormation) {
        String qry = "SELECT COUNT(*) FROM formation WHERE nomFormation = ? AND descriptionFormation = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, nomFormation);
            pstm.setString(2, descriptionFormation);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return false;
    }

}
