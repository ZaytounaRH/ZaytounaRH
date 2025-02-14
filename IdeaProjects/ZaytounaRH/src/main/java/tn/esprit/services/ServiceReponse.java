package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Reponse;
import tn.esprit.utils.MyDatabase;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ServiceReponse implements IService<Reponse> {
    private Connection cnx;

    public ServiceReponse() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Reponse reponse) {
        // Requête SQL pour ajouter une réponse
        String qry = "INSERT INTO `reponse`(`contenu`, `dateRep`) VALUES (?, ?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, reponse.getContenu());
            pstm.setDate(2, Date.valueOf(reponse.getDateRep()));

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Reponse> getAll() {
        // Requête SQL pour récupérer toutes les réponses
        List<Reponse> reponses = new ArrayList<>();
        String qry = "SELECT * FROM `reponse`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Reponse r = new Reponse();
                r.setIdRep(rs.getInt("idRep"));
                r.setContenu(rs.getString("contenu"));
                r.setDateRep(rs.getDate("dateRep").toLocalDate());

                reponses.add(r);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reponses;
    }

    @Override
    public void update(Reponse reponse) {
        String qry = "UPDATE `reponse` SET `contenu`=?, `dateRep`=? WHERE `idRep`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, reponse.getContenu());
            pstm.setDate(2, Date.valueOf(reponse.getDateRep()));
            pstm.setInt(3, reponse.getIdRep());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Reponse reponse) {
        String qry = "DELETE FROM `reponse` WHERE `idRep`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, reponse.getIdRep());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
