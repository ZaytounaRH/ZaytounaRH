package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
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









}
