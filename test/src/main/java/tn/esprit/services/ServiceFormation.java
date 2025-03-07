package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Formation;
import tn.esprit.models.Formation;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements IService<Formation> {
    private Connection cnx;

    public ServiceFormation() {
        cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(Formation formation) {
        String qry ="INSERT INTO `Formation`(`titre`, `date_debut`, `date_fin`, `idRH`, `idEmploye`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getTitre());
            pstm.setDate(2, formation.getDate_debut());
            pstm.setDate(3, formation.getDate_fin());
            pstm.setInt(4, formation.getIdRH());
            pstm.setInt(5, formation.getIdEmploye());

            pstm.executeUpdate();
            System.out.println("Formation ajoutée avec succès !");



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    @Override
    public List<Formation> getAll() {
        List<Formation> formations = new ArrayList<>();
        String qry ="SELECT * FROM Formation ";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Formation p = new Formation(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin"),
                        rs.getInt("idRH"),
                        rs.getInt("idEmploye")
                );
                formations.add(p);
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return formations;
    }
}
