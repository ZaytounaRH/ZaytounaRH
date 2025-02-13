package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Formation;
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

        String qry ="INSERT INTO `formation`( `nomFormation`, `descriptionFormation`, `dureeFormation`, `idEmploye`, `idRH`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1,formation.getNomFormation());
            pstm.setString(2, formation.getDescriptionFormation());
            pstm.setString(3, formation.getDureeFormation());
            pstm.setInt(4, formation.getIdEmploye());
            pstm.setInt(5, formation.getIdRH());


            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }


    @Override
    public List<Formation> getAll() {


        List<Formation> formations = new ArrayList<>();
        String qry ="SELECT * FROM `formation`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
               Formation formation = new Formation();
               formation.setIdFormation(rs.getInt("idFormation"));
               formation.setNomFormation(rs.getString("nomFormation"));
               formation.setDescriptionFormation(rs.getString("descriptionFormation"));
               formation.setDureeFormation(rs.getString("dureeFormation"));
               formation.setIdEmploye(rs.getInt("idEmploye"));
               formation.setIdRH(rs.getInt("idRH"));

                formations.add(formation);
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return formations;
    }
    @Override
    public void update(Formation formation) {
        String qry = "UPDATE `formation` SET `nomFormation` = ?, `descriptionFormation` = ?, `dureeFormation` = ?, `idEmploye` = ?, `idRH` = ? WHERE `idFormation` = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getDescriptionFormation());
            pstm.setString(3, formation.getDureeFormation());
            pstm.setInt(4, formation.getIdEmploye());
            pstm.setInt(5, formation.getIdRH());
            pstm.setInt(6, formation.getIdFormation());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(" Formation mise à jour avec succès !");
            } else {
                System.out.println(" Aucun enregistrement mis à jour. Vérifie l'ID.");
            }

        } catch (SQLException e) {
            System.out.println(" Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Formation formation) {
        String qry = "DELETE FROM `formation` WHERE `idFormation` = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, formation.getIdFormation());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(" Formation supprimée avec succès !");
            } else {
                System.out.println("Aucun enregistrement trouvé pour suppression.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }


    }
}


