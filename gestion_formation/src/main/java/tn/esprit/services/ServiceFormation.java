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

        String qry = "INSERT INTO `formation`( `nomFormation`, `descriptionFormation`, `dateDebutFormation`, `dateFinFormation`,  `employee_id`, `idCertif`,`rh_id`  ) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getDescriptionFormation());
            pstm.setDate(3, formation.getDateDebutFormation());
            pstm.setDate(4, formation.getDateFinFormation());
            pstm.setInt(5, formation.getEmploye().getIdEmploye());

            pstm.setInt(6,formation.getCertification().getIdCertif());
            pstm.setInt(7, formation.getRh().getRh_id());

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
                formation.setDateDebutFormation(rs.getDate("dateDebutFormation"));
                formation.setDateFinFormation(rs.getDate("dateFinFormation"));


                int idEmploye = rs.getInt("employee_id");
                int rh_id = rs.getInt("rh_id");
                int idCertif = rs.getInt("idCertif");



                Employe employe = null;
                Rh rh = null;
                Certification certification = null;

                if (idEmploye > 0) {
                    String employeQry = "SELECT nom FROM users WHERE id = ? AND user_type = 'Employee'";
                    PreparedStatement pstmtEmploye = cnx.prepareStatement(employeQry);
                    pstmtEmploye.setInt(1, idEmploye);
                    ResultSet rsEmploye = pstmtEmploye.executeQuery();

                    if (rsEmploye.next()) {
                        employe = new Employe(idEmploye, rsEmploye.getString("nom"));
                    }


                }

                if (rh_id > 0) {
                    String rhQry = "SELECT nom FROM users WHERE id = ? AND user_type = 'RH'";
                    PreparedStatement pstmtRh = cnx.prepareStatement(rhQry);
                    pstmtRh.setInt(1, rh_id);
                    ResultSet rsRh = pstmtRh.executeQuery();

                    if (rsRh.next()) {
                       rh = new Rh(rh_id, rsRh.getString("nom"));
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
                                rsCertif.getString("titreCertif")
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
        String qry = "UPDATE `formation` SET `nomFormation` = ?, `descriptionFormation` = ?, `dateDebutFormation` = ?,`dateFinFormation`= ? ,`employee_id` = ?, `idCertif`= ?, `rh_id` = ? WHERE `idFormation` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getDescriptionFormation());
            pstm.setDate(3, formation.getDateDebutFormation());
            pstm.setDate(4, formation.getDateFinFormation());


            pstm.setInt(5, formation.getEmploye().getIdEmploye());
            pstm.setInt(6, formation.getCertification().getIdCertif());
            pstm.setInt(7, formation.getRh().getRh_id());
            pstm.setInt(8, formation.getIdFormation());

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
        if (formation.getDateDebutFormation() == null || formation.getDateFinFormation() == null) {
            System.out.println("Les dates de début et de fin ne peuvent pas être nulles !");
            return false;
        }
        if (formation.getDateFinFormation().before(formation.getDateDebutFormation())) {
            System.out.println("La date de fin doit être postérieure à la date de début !");
            return false;
        }

        if (formation.getRh().getRh_id() <= 0) {
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

    public List<Employe> getAllEmployes() {
        List<Employe> employes = new ArrayList<>();
        String query = "SELECT * FROM employe";

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Employe employe = new Employe();
                employe.setIdEmploye(rs.getInt("idEmploye"));
                employe.setNom(rs.getString("nom"));
                employes.add(employe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    public List<Rh> getAllRH() {
        List<Rh> rhList = new ArrayList<>();
        String query = "SELECT * FROM rh"; // Table RH

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Rh rh = new Rh();
                rh.setRh_id(rs.getInt("idRH"));
                rh.setNom(rs.getString("nom"));
                rhList.add(rh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rhList;
    }

    public List<Certification> getAllCertifications() {
        List<Certification> certifications = new ArrayList<>();
        String query = "SELECT * FROM certification";

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Certification certif = new Certification();
                certif.setIdCertif(rs.getInt("idCertif"));
                certif.setTitreCertif(rs.getString("titreCertif"));
                certifications.add(certif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return certifications;
    }

}
