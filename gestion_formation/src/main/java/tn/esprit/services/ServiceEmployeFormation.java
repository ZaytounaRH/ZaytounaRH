package tn.esprit.services;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.utils.MyDatabase;
import tn.esprit.services.ServiceEmployee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployeFormation {
    private final Connection cnx ;
    private final ServiceEmployee serviceEmploye = new ServiceEmployee();

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

    public List<Employee> getEmployesByFormation(int idFormation) {
        List<Employee> employees = new ArrayList<>();
        List<Integer> employeeIds = new ArrayList<>();


        String qry = "SELECT employee_id FROM employe_formation WHERE idFormation = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, idFormation);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                employeeIds.add(rs.getInt("employee_id"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des employés par formation : " + e.getMessage());
            return employees;
        }


        List<Employee> allEmployees = serviceEmploye.getAll();
        for (Employee emp : allEmployees) {
            if (employeeIds.contains(emp.getId())) {
                employees.add(emp);
            }
        }

        return employees;
    }



}
