package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.models.EmployeFormation;
import tn.esprit.utils.MyDatabase;
import tn.esprit.services.ServiceEmployee;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployeFormation implements IService<EmployeFormation> {
    private Connection cnx ;
    private final ServiceEmployee serviceEmploye = new ServiceEmployee();

    public ServiceEmployeFormation() {
        cnx=MyDatabase.getInstance().getCnx();
        if (cnx == null) {
            System.out.println("Erreur : la connexion est fermée ou n'est pas initialisée.");
        } else {
            System.out.println("Connexion ouverte avec succès.");
        }


    }
    private boolean isConnectionClosed() {
        try {
            return cnx.isClosed();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de la connexion : " + e.getMessage());
            return true;
        }
    }


    public void affecterFormationAEmployes(int idFormation, List<Integer> employeIds) {
        String checkQuery = "SELECT e.employee_id FROM employee e " +
                "JOIN users u ON e.user_id = u.id " +
                "WHERE e.employee_id = ?";

        String insertQuery = "INSERT INTO employe_formation (employee_id, idFormation, dateParticipation) " +
                "SELECT ?, ?, CURDATE() FROM DUAL " +
                "WHERE EXISTS (" +
                "    SELECT 1 FROM employee e " +
                "    JOIN users u ON e.user_id = u.id " +
                "    WHERE e.employee_id = ? " +
                ")";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = MyDatabase.getInstance().getCnx();  // Rétablir la connexion si elle est fermée
            }

            try (PreparedStatement checkStmt = cnx.prepareStatement(checkQuery);
                 PreparedStatement insertStmt = cnx.prepareStatement(insertQuery)) {

                for (Integer employeId : employeIds) {
                    // Vérifier si l'employé existe
                    checkStmt.setInt(1, employeId);
                    ResultSet rs = checkStmt.executeQuery();

                    if (!rs.next()) {
                        System.out.println("Employé avec ID " + employeId + " inexistant. Ignoré.");
                        continue;
                    }

                    // Insérer l'employé dans la formation
                    insertStmt.setInt(1, employeId);
                    insertStmt.setInt(2, idFormation);
                    insertStmt.setInt(3, employeId);
                    insertStmt.executeUpdate();
                }

                System.out.println("Affectation terminée avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affectation des employés à la formation : " + e.getMessage());
        }
    }

    //ya9ra fel idemploye 0
   /* public void affecterFormationAEmployes(int idFormation, List<Integer> employeIds) {


        try{
            if (cnx == null || cnx.isClosed()) {
                cnx = MyDatabase.getInstance().getCnx();
            }

            String qry = "INSERT INTO employe_formation (employee_id, idFormation,`dateParticipation`) VALUES (?, ?,?)";

            try (PreparedStatement pstm = cnx.prepareStatement(qry)){
            for (Integer empId : employeIds) {
                if (!isEmployeInscrit(empId, idFormation)) {
                    System.out.println("Erreur : L'employé avec ID " + empId + " n'existe pas.");

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


    */
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
//connexion 3al base
public List<Employee> afficherEmployesParFormation(int idFormation) {
    List<Employee> employes = new ArrayList<>();
    String qry = "SELECT e.employee_id, u.nom, u.prenom, u.email " +
            "FROM employe_formation ef " +
            "JOIN employee e ON ef.employee_id = e.employee_id " +
            "JOIN users u ON e.user_id = u.id " +
            "WHERE ef.idFormation = ?";

    try {
        if (cnx == null || cnx.isClosed()) {
            cnx = MyDatabase.getInstance().getCnx();  // Rétablir la connexion si elle est fermée
        }

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, idFormation);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Employee employe = new Employee();
                employe.setId(rs.getInt("employee_id"));
                employe.setNom(rs.getString("nom"));
                employe.setPrenom(rs.getString("prenom"));
                employe.setEmail(rs.getString("email"));  // Exemple pour email, à adapter selon votre classe Employe
                employes.add(employe);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des employés par formation : " + e.getMessage());
        }
    } catch (SQLException e) {
        System.out.println("Erreur de connexion ou de requête : " + e.getMessage());
    }

    return employes;
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
        String query = "DELETE FROM employe_formation WHERE employee_id = ? AND idFormation = ?";

        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, empId);
            statement.setInt(2, idFormation);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employé " + empId + " supprimé de la formation " + idFormation);
            } else {
                System.out.println("Aucune correspondance trouvée pour suppression.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*
    public void supprimerEmployeDeFormation(int empId, int idFormation) {
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = MyDatabase.getInstance().getCnx();  // Rétablir la connexion si elle est fermée
            }
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
        }
        }
        catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'employé de la formation : " + e.getMessage());
        }
    }


 */
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
    public List<Object[]> afficherFormationsByEmployee(int employeeId) {
        List<Object[]> formations = new ArrayList<>();
        String query = "SELECT f.idFormation, f.nomFormation, f.descriptionFormation, f.dateDebutFormation, f.dateFinFormation, ef.dateParticipation " +
                "FROM employe_formation ef " +
                "JOIN formation f ON ef.idFormation = f.idFormation " +
                "WHERE ef.employee_id = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            // Paramétrer la requête avec l'ID de l'employé
            statement.setInt(1, employeeId);

            // Exécuter la requête et récupérer les résultats
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Formation formation = new Formation();
                formation.setIdFormation(resultSet.getInt("idFormation"));
                formation.setNomFormation(resultSet.getString("nomFormation"));
                formation.setDescriptionFormation(resultSet.getString("descriptionFormation"));
                formation.setDateDebutFormation(resultSet.getDate("dateDebutFormation"));
                formation.setDateFinFormation(resultSet.getDate("dateFinFormation"));
                Date dateParticipation = resultSet.getDate("dateParticipation");
                formations.add(new Object[]{formation, dateParticipation});
                //formations.add(formation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des formations : " + e.getMessage());
            e.printStackTrace();
        }

        return formations;
    }

}
