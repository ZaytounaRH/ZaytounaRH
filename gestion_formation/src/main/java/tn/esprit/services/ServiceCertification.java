package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
import tn.esprit.models.Formation;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceCertification implements IService<Certification> {
    private  Connection cnx ;

    public ServiceCertification() {

        cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(Certification certification) {
        if (!isValidCertification(certification)) {
            System.out.println("Erreur : données invalides, insertion annulée !");
            return;
        }

        if (isCertificationExists(certification.getTitreCertif(), certification.getOrganismeCertif())) {
            System.out.println("Erreur : Une certification avec le même titre et le même organisme existe déjà !");
            return;
        }
        if (certification.getFormation() == null) {
            System.out.println("Erreur : La certification doit être associée à une formation !");
            return;
        }

        try {
            // Vérifiez si la connexion est ouverte avant de continuer
            if (cnx == null || cnx.isClosed()) {
                cnx = MyDatabase.getInstance().getCnx();
            }
        String qry ="INSERT INTO `certification`(`titreCertif`, `organismeCertif`,`idFormation`) VALUES(?,?,?)";
        try(PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, certification.getTitreCertif());
            pstm.setString(2, certification.getOrganismeCertif());
            pstm.setInt(3, certification.getFormation().getIdFormation());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Certification ajoutée avec succès !");
            }
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public List<Certification> getAll() {
        System.out.println("🔎 getAll des certifs() appelé !");

        List<Certification> certifications = new ArrayList<>();
        String qry = "SELECT c.idCertif, c.titreCertif, c.organismeCertif, f.nomFormation " +
                "FROM certification c " +
                "LEFT JOIN formation f ON c.idFormation = f.idFormation";

        try {
            if (cnx == null || cnx.isClosed()) {
                System.out.println("Connexion fermée ! Réouverture...");
                cnx = MyDatabase.getInstance().getCnx();
            }
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Certification certification = new Certification();
                certification.setIdCertif(rs.getInt("idCertif"));
                certification.setTitreCertif(rs.getString("titreCertif"));
                certification.setOrganismeCertif(rs.getString("organismeCertif"));

                // Récupérer uniquement le nom de la formation
                String nomFormation = rs.getString("nomFormation");
                if (nomFormation != null) {
                    certification.setFormation(new Formation());
                    certification.getFormation().setNomFormation(nomFormation);
                }

                certifications.add(certification);
            }

            rs.close();
            stm.close();

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des certifications : " + e.getMessage());
        }

        return certifications;
    }

    @Override
    public void update(Certification certification) {
        if (!isValidCertification(certification)) {
            System.out.println("Erreur : données invalides, update annulé !");
            return;
        }

        String qry = "UPDATE `certification` SET `titreCertif`=?, `organismeCertif`=?, `idFormation`=? WHERE `idCertif`=?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, certification.getTitreCertif());
            pstm.setString(2, certification.getOrganismeCertif());

            // Vérifier si une formation est associée
            if (certification.getFormation() != null) {
                pstm.setInt(3, certification.getFormation().getIdFormation());
            } else {
                pstm.setNull(3, java.sql.Types.INTEGER); // Si aucune formation, mettre NULL
            }

            pstm.setInt(4, certification.getIdCertif());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Certification mise à jour avec succès !");
            } else {
                System.out.println("Aucun enregistrement mis à jour. Vérifie l'ID.");
            }

            pstm.close(); // Fermer le PreparedStatement après utilisation

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Certification certification) {
        String qry = "DELETE FROM `certification` WHERE idCertif=?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, certification.getIdCertif());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(" certification supprimée avec succès !");
            } else {
                System.out.println("Aucun enregistrement trouvé pour suppression.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }

    }
    public static boolean isValidCertification(Certification certification) {
        if (certification.getTitreCertif() == null || certification.getTitreCertif().isEmpty()) {
            System.out.println("Le titre de la certification ne peut pas être vide !");
            return false;
        }
        if (certification.getOrganismeCertif() == null || certification.getOrganismeCertif().isEmpty()) {
            System.out.println("L'organisme de la certification ne peut pas être vide !");
            return false;
        }


        return true;

    }

    public boolean isCertificationExists(String titreCertif, String organismeCertif) {
        String qry = "SELECT COUNT(*) FROM certification WHERE titreCertif = ? AND organismeCertif = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, titreCertif);
            pstm.setString(2, organismeCertif);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retourne true si la certification existe déjà
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return false;
    }


    public Certification getCertificationById(int idCertif) {
        System.out.println("🔎 getCertificationById() appelé avec idCertif = " + idCertif);

        Certification certification = null;
        String qry = "SELECT c.idCertif, c.titreCertif, c.organismeCertif, f.nomFormation " +
                "FROM certification c " +
                "LEFT JOIN formation f ON c.idFormation = f.idFormation " +
                "WHERE c.idCertif = ?";

        try {
            if (cnx == null || cnx.isClosed()) {
                System.out.println("Connexion fermée ! Réouverture...");
                cnx = MyDatabase.getInstance().getCnx();
            }

            PreparedStatement pst = cnx.prepareStatement(qry);
            pst.setInt(1, idCertif);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                certification = new Certification();
                certification.setIdCertif(rs.getInt("idCertif"));
                certification.setTitreCertif(rs.getString("titreCertif"));
                certification.setOrganismeCertif(rs.getString("organismeCertif"));

                // Récupérer uniquement le nom de la formation
                String nomFormation = rs.getString("nomFormation");
                if (nomFormation != null) {
                    certification.setFormation(new Formation());
                    certification.getFormation().setNomFormation(nomFormation);
                }
            }

            rs.close();
            pst.close();

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la certification par ID : " + e.getMessage());
        }

        return certification;
    }

    public Date getDateObtention(int certifId) {
        Date dateObtention = null;
        String query = "SELECT ec.dateObtention " +
                "FROM employe_certification ec " +
                "WHERE ec.idCertif = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            // Paramétrer la requête avec l'idCertif
            statement.setInt(1, certifId);

            // Exécuter la requête et récupérer le résultat
            ResultSet resultSet = statement.executeQuery();

            // Si une date d'obtention est trouvée, la récupérer
            if (resultSet.next()) {
                dateObtention = resultSet.getDate("dateObtention");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la date d'obtention : " + e.getMessage());
            e.printStackTrace();
        }

        return dateObtention;
    }






}
