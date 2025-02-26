package tn.esprit.services;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployeFormation {
    private final Connection cnx ;
    public ServiceEmployeFormation() {
        cnx=MyDatabase.getInstance().getCnx();
    }

    public void affecterFormationAEmployes(int idFormation, List<Integer> employeIds) {
        String qry = "INSERT INTO employe_formation (employee_id, idFormation,`dateParticipation`) VALUES (?, ?,?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            for (Integer empId : employeIds) {
                if (!isEmployeInscrit(empId, idFormation)) {
                    pstm.setInt(1, empId);
                    pstm.setInt(2, idFormation);
                    pstm.setDate(3, new java.sql.Date(System.currentTimeMillis()));


                    pstm.addBatch();
                }
            }

            pstm.executeBatch();
            System.out.println("Formation affectée avec succès aux employés !");

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affectation de la formation : " + e.getMessage());
        }
    }
    private boolean isEmployeInscrit(int empId, int idFormation) {
        String qry = "SELECT COUNT(*) FROM employe_formation WHERE employee_id = ? AND idFormation = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, empId);
            pstm.setInt(2, idFormation);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification : " + e.getMessage());
        }
        return false;
    }
}
