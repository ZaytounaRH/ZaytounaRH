package tn.esprit.getionfinanciere.services;


import tn.esprit.getionfinanciere.interfaces.IService;
import tn.esprit.getionfinanciere.models.Budget;
import tn.esprit.getionfinanciere.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceBudget implements IService<Budget> {
    private final Connection cnx;

    public ServiceBudget() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Budget budget) {
        String qry = "INSERT INTO `Budget`(`montantAlloue`, `dateDebut`, `dateFin`, `typeBudget`, `idResponsable`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDouble(1, budget.getMontantAlloue());
            pstm.setString(2, budget.getDateDebut());
            pstm.setString(3, budget.getDateFin());
            pstm.setString(4, budget.getTypeBudget());
            pstm.setInt(5, budget.getIdResponsable());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Budget> getAll() {
        List<Budget> budgets = new ArrayList<>();
        String qry = "SELECT * FROM `Budget`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Budget b = new Budget();
                b.setId(rs.getInt("id"));
                b.setMontantAlloue(rs.getDouble("montantAlloue"));
                b.setDateDebut(rs.getString("dateDebut"));
                b.setDateFin(rs.getString("dateFin"));
                b.setTypeBudget(rs.getString("typeBudget"));
                b.setIdResponsable(rs.getInt("idResponsable"));

                budgets.add(b);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return budgets;
    }

    @Override
    public void update(Budget budget) {
        String qry = "UPDATE `Budget` SET `montantAlloue`=?, `dateDebut`=?, `dateFin`=?, `typeBudget`=?, `idResponsable`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setDouble(1, budget.getMontantAlloue());
            pstm.setString(2, budget.getDateDebut());
            pstm.setString(3, budget.getDateFin());
            pstm.setString(4, budget.getTypeBudget());
            pstm.setInt(5, budget.getIdResponsable());
            pstm.setInt(6, budget.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Budget budget) {
        String qry = "DELETE FROM `Budget` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, budget.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}