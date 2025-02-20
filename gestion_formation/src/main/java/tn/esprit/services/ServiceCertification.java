package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
import tn.esprit.models.Formation;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceCertification implements IService<Certification> {
    private final Connection cnx ;

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

        String qry ="INSERT INTO `certification`(`titreCertif`, `organismeCertif`) VALUES(?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1,certification.getTitreCertif());
            pstm.setString(2, certification.getOrganismeCertif());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public List<Certification> getAll() {
        List< Certification > certifications  = new ArrayList<>();
        String qry ="SELECT * FROM `certification`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Certification certification = new Certification();
                certification.setIdCertif(rs.getInt("idCertif"));
                certification.setTitreCertif(rs.getString("titreCertif"));
                certification.setOrganismeCertif(rs.getString("organismeCertif"));

                certifications.add(certification);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return certifications;
    }

    @Override
    public void update(Certification certification) {
        if (!isValidCertification(certification)) {
            System.out.println("Erreur : données invalides, update annulé !");
            return;
        }
        String qry = "UPDATE `certification` SET `titreCertif`=?,`organismeCertif`=? WHERE `idCertif`=?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, certification.getTitreCertif());
            pstm.setString(2,certification.getOrganismeCertif() );
            pstm.setInt(3, certification.getIdCertif());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(" Certification mise à jour avec succès !");
            } else {
                System.out.println(" Aucun enregistrement mis à jour. Vérifie l'ID.");
            }

        } catch (SQLException e) {
            System.out.println(" Erreur lors de la mise à jour : " + e.getMessage());
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









}
