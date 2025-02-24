package tn.esprit.getionfinanciere.services;


import tn.esprit.getionfinanciere.interfaces.IService;
import tn.esprit.getionfinanciere.models.Depense;
import tn.esprit.getionfinanciere.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDepense implements IService<Depense> {
    private final Connection cnx;

    public ServiceDepense() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Depense depense) {
        String qry = "INSERT INTO `Depense`(`montantDepense`, `dateDepense`, `description`, `idBudget`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDouble(1, depense.getMontantDepense());
            pstm.setString(2, depense.getDateDepense());
            pstm.setString(3, depense.getDescription());
            pstm.setInt(4, depense.getIdBudget());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Depense> getAll() {
        List<Depense> depenses = new ArrayList<>();
        String qry = "SELECT * FROM `Depense`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Depense d = new Depense();
                d.setId(rs.getInt("id"));
                d.setMontantDepense(rs.getDouble("montantDepense"));
                d.setDateDepense(rs.getString("dateDepense"));
                d.setDescription(rs.getString("description"));
                d.setIdBudget(rs.getInt("idBudget"));

                depenses.add(d);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return depenses;
    }

    @Override
    public void update(Depense depense) {
        String qry = "UPDATE `Depense` SET `montantDepense`=?, `dateDepense`=?, `description`=?, `idBudget`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDouble(1, depense.getMontantDepense());
            pstm.setString(2, depense.getDateDepense());
            pstm.setString(3, depense.getDescription());
            pstm.setInt(4, depense.getIdBudget());
            pstm.setInt(5, depense.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Depense depense) {
        String qry = "DELETE FROM `Depense` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, depense.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
