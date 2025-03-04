package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.models.EmployeFormation;
import tn.esprit.utils.MyDatabase;
import tn.esprit.services.ServiceEmployee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployeFormation implements IService<EmployeFormation> {
    private Connection cnx ;
    private final ServiceEmployee serviceEmploye = new ServiceEmployee();

    public ServiceEmployeFormation() {
        cnx=MyDatabase.getInstance().getCnx();
    }

    public void affecterFormationAEmployes(int idFormation, List<Integer> employeIds) {


        try{
            if (cnx == null || cnx.isClosed()) {
                cnx = MyDatabase.getInstance().getCnx();
            }

            String qry = "INSERT INTO employe_formation (employee_id, idFormation,`dateParticipation`) VALUES (?, ?,?)";

            try (PreparedStatement pstm = cnx.prepareStatement(qry)){
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

             }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affectation de la formation : " + e.getMessage());
        }
    }

    private boolean isEmployeInscrit(int empId, int idFormation) {
        try{
            if (cnx == null || cnx.isClosed()) {
                cnx = MyDatabase.getInstance().getCnx();
            }
        String qry = "SELECT COUNT(*) FROM employe_formation WHERE employee_id = ? AND idFormation = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)){



            pstm.setInt(1, empId);
            pstm.setInt(2, idFormation);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification : " + e.getMessage());
        }
        return false;
    }

    public List<Employee> afficherEmployesParFormation(int idFormation) {
        List<Employee> employeesInFormation = new ArrayList<>();

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = MyDatabase.getInstance().getCnx();
                if (cnx == null || cnx.isClosed()) {
                    System.out.println("La connexion à la base de données n'a pas pu être rétablie !");
                    return employeesInFormation;
                }
            }

            // 1. Récupérer le nom de la formation
            String nomFormation = "Formation inconnue";

            String queryFormation = "SELECT nomFormation FROM formation WHERE idFormation = ?";


            try (PreparedStatement pstmFormation = cnx.prepareStatement(queryFormation)) {
                pstmFormation.setInt(1, idFormation);
                ResultSet rsFormation = pstmFormation.executeQuery();
                if (rsFormation.next()) {
                    nomFormation = rsFormation.getString("nomFormation");
                } else {
                    System.out.println("Formation non trouvée.");
                    return employeesInFormation; // Si aucune formation n'est trouvée, on retourne une liste vide
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la récupération du nom de la formation : " + e.getMessage());
                return employeesInFormation;
            }

            // 2. Récupérer tous les employés via getAll()
            List<Employee> allEmployees = serviceEmploye.getAll();
            // List<Employee> employeesInFormation = new ArrayList<>();

            // 3. Filtrer ceux qui sont inscrits à la formation
            String queryEmployes = "SELECT employee_id FROM employe_formation WHERE idFormation = ?";

            try (PreparedStatement pstm = cnx.prepareStatement(queryEmployes)) {
                pstm.setInt(1, idFormation);
                ResultSet rs = pstm.executeQuery();

                while (rs.next()) {
                    int empId = rs.getInt("employee_id");

                    // Chercher l'employé correspondant dans la liste récupérée via getAll()
                    for (Employee emp : allEmployees) {
                        if (emp.getId() == empId) {
                            employeesInFormation.add(emp);
                            break;
                        }
                    }
                }
            }
        }catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des employés inscrits à la formation : " + e.getMessage());

        }
        return employeesInFormation;

    }

    public void modifierListeEmployesFormation(int idFormation, List<Integer> newEmployeIds) {
        List<Integer> currentEmployeeIds = getEmployesInscrits(idFormation);

      for (Integer empId : currentEmployeeIds) {
            if (!newEmployeIds.contains(empId)) {
                supprimerEmployeDeFormation(empId, idFormation);
            }
        }

        for (Integer empId : newEmployeIds) {
            if (!currentEmployeeIds.contains(empId)) {
                affecterFormationAEmployes(idFormation, List.of(empId));
            }
        }

        System.out.println(" La liste des employés a été mise à jour.");
    }
    private List<Integer> getEmployesInscrits(int idFormation) {
        List<Integer> employeeIds = new ArrayList<>();
        String query = "SELECT employee_id FROM employe_formation WHERE idFormation = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(query)) {
            pstm.setInt(1, idFormation);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                employeeIds.add(rs.getInt("employee_id"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des employés inscrits à la formation : " + e.getMessage());
        }

        return employeeIds;
    }


    public void supprimerEmployeDeFormation(int empId, int idFormation) {

        if (!isEmployeInscrit(empId, idFormation)) {
            System.out.println(" L'employé n'est pas inscrit à la formation ");
            return;
        }


        String query = "DELETE FROM employe_formation WHERE employee_id = ? AND idFormation = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(query)) {
            pstm.setInt(1, empId);
            pstm.setInt(2, idFormation);
            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("L'employé avec a été supprimé de la formation ");
            } else {
                System.out.println("Erreur lors de la suppression de l'employé.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'employé de la formation : " + e.getMessage());
        }
    }

    @Override
    public void add(EmployeFormation employeFormation) {
        String qry = "INSERT INTO employe_formation (employee_id, idFormation, dateParticipation) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, employeFormation.getEmployee_id());
            pstm.setInt(2, employeFormation.getIdFormation());
            pstm.setDate(3, new java.sql.Date(employeFormation.getDateParticipation().getTime()));
            pstm.executeUpdate();
            System.out.println("Affectation de la formation réussie !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affectation : " + e.getMessage());
        }
    }
    @Override
    public List<EmployeFormation> getAll() {
        List<EmployeFormation> employeFormations = new ArrayList<>();
        String qry = "SELECT * FROM employe_formation";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                EmployeFormation employeFormation = new EmployeFormation(
                        rs.getInt("employee_id"),
                        rs.getInt("idFormation"),
                        rs.getDate("dateParticipation")
                );
                employeFormations.add(employeFormation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des affectations : " + e.getMessage());
        }
        return employeFormations;
    }
    @Override
    public void update(EmployeFormation employeFormation) {

    }
    @Override
    public void delete(EmployeFormation employeFormation) {

    }
}
