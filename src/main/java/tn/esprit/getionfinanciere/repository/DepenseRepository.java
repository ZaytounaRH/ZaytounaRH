package tn.esprit.getionfinanciere.repository;


import tn.esprit.getionfinanciere.interfaces.IService;
import tn.esprit.getionfinanciere.models.Depense;
import tn.esprit.getionfinanciere.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepenseRepository implements IService<Depense> {
    private final Connection cnx;

    public DepenseRepository() {
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

    public Map<String, Double> getDepensesParMois() {
        Map<String, Double> depensesParMois = new HashMap<>();
        String qry = "SELECT DATE_FORMAT(dateDepense, '%Y-%m') AS mois, SUM(montantDepense) AS total FROM Depense GROUP BY mois";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                depensesParMois.put(rs.getString("mois"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return depensesParMois;
    }

}
