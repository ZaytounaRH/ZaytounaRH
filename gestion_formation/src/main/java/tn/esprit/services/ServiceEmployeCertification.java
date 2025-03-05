package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Certification;
import tn.esprit.models.EmployeCertification;
import tn.esprit.models.EmployeFormation;
import tn.esprit.models.Employee;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;


public class ServiceEmployeCertification implements IService<EmployeCertification> {

    private Connection cnx ;
    private final ServiceEmployee serviceEmploye = new ServiceEmployee();
    public ServiceEmployeCertification() {
        cnx=MyDatabase.getInstance().getCnx();
    }



//ya9ra id employe 0
    public List<Certification> getCertificationsByEmployee(int employee_id) {
        List<Certification> certifications = new ArrayList<>();
        String qry = "SELECT c.idCertif, c.titreCertif  FROM certification c JOIN employe_certification ec ON c.idCertif = ec.idCertif WHERE ec.employee_id = ?";
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = MyDatabase.getInstance().getCnx();  // Rétablir la connexion si elle est fermée
            }
            try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
                pstm.setInt(1, employee_id);
                ResultSet rs = pstm.executeQuery();

                while (rs.next()) {
                    Certification certif = new Certification(
                            rs.getInt("idCertif"),
                            rs.getString("titreCertif")
                    );
                    certifications.add(certif);
                }

                Employee employee = null;
                List<Employee> allEmployees = serviceEmploye.getAll(); // Utilise la méthode getAll() pour récupérer tous les employés

                for (Employee emp : allEmployees) {
                    if (emp.getId() == employee_id) {
                        employee = emp;
                        break;
                    }
                }

                if (employee != null) {
                    System.out.println("Liste des certifications de l'employé : " + employee.getNom() + " " + employee.getPrenom());
                    if (certifications.isEmpty()) {
                        System.out.println("Aucune certification trouvée pour cet employé.");
                    } else {
                        for (Certification cert : certifications) {
                            System.out.println("Certification : " + cert.getTitreCertif());
                        }
                    }
                } else {
                    System.out.println("Employé  " + employee.getNom() + " non trouvé.");
                }
            }} catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des certifications : " + e.getMessage());
        }

        return certifications;
    }

    public List<Object[]> afficherCertificationsByCurrentUser(int employee_id) {
        List<Object[]> certifications = new ArrayList<>();
        String query = "SELECT c.idCertif, c.titreCertif, c.organismeCertif, c.idFormation, ec.dateObtention " +
                "FROM employe_certification ec " +
                "JOIN certification c ON ec.idCertif = c.idCertif " +
                "WHERE ec.employee_id = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            // Paramétrer la requête avec l'employee_id
            statement.setInt(1, employee_id);

            // Exécuter la requête et récupérer les résultats
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Certification certification = new Certification();
                certification.setIdCertif(resultSet.getInt("idCertif"));
                certification.setTitreCertif(resultSet.getString("titreCertif"));
                certification.setOrganismeCertif(resultSet.getString("organismeCertif"));
                //certification.setFormation(resultSet.getInt("idFormation"));
                Date dateObtention = resultSet.getDate("dateObtention");
                System.out.println("Date d'obtention récupérée: " + dateObtention);
                certifications.add(new Object[]{certification, dateObtention});
                //certifications.add(certification);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des certifications : " + e.getMessage());
            e.printStackTrace();
        }

        return certifications;
    }

    public void ajouterCertificationAEmploye(int idEmploye, int idCertif, Date dateObtention) {
        if (!controleSaisieCertification(idEmploye, idCertif, dateObtention)) {
            System.out.println("Erreur : données invalides, insertion annulée !");
            return;
        }
        try{
            if (cnx == null || cnx.isClosed()) {
                cnx = MyDatabase.getInstance().getCnx();
            }

        String qry = "INSERT INTO employe_certification (employee_id, idCertif, dateObtention) VALUES (?, ?, ?)";

        try (PreparedStatement pstm = cnx.prepareStatement(qry);){

            pstm.setInt(1, idEmploye);
            pstm.setInt(2, idCertif);
            pstm.setDate(3, dateObtention);

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Certification ajoutée avec succès !");
            } else {
                System.out.println("Erreur lors de l'ajout de la certification.");
            }
        }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la certification : " + e.getMessage());
        }
    }



    public boolean controleSaisieCertification(int idEmploye, int idCertif, Date dateObtention) {

        if (idEmploye <= 0) {
            System.out.println("L'ID de l'employé doit être un entier positif.");
            return false;
        }


        if (idCertif <= 0) {
            System.out.println("L'ID de la certification doit être un entier positif.");
            return false;
        }

        if (dateObtention == null) {
            System.out.println("La date d'obtention est obligatoire.");
            return false;
        }

        Date currentDate = new Date(System.currentTimeMillis());
        if (dateObtention.after(currentDate)) {
            System.out.println("La date d'obtention ne peut pas être dans le futur.");
            return false;
        }


        return true;
    }
    public void modifierCertificationEmploye(int employeeId, int ancienneCertifId, int nouvelleCertifId) {
        String checkQuery = "SELECT COUNT(*) FROM employe_certification WHERE employee_id = ? AND idCertif = ?";
        String updateQuery = "UPDATE employe_certification SET idCertif = ? WHERE employee_id = ? AND idCertif = ?";

        try (PreparedStatement checkStmt = cnx.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, employeeId);
            checkStmt.setInt(2, ancienneCertifId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {

                try (PreparedStatement updateStmt = cnx.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, nouvelleCertifId);
                    updateStmt.setInt(2, employeeId);
                    updateStmt.setInt(3, ancienneCertifId);

                    int rowsUpdated = updateStmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println(" Certification modifiée avec succès ");
                    } else {
                        System.out.println(" Échec de la modification de la certification.");
                    }
                }
            } else {
                System.out.println("L'employé n'est pas associé à cette certification.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la certification : " + e.getMessage());
        }
    }



    @Override
    public List<EmployeCertification> getAll() {
        return null;
    }
    @Override
    public void add(EmployeCertification employeCertification) {

    }
    @Override
    public void update(EmployeCertification employeCertification) {

    }
    @Override
    public void delete(EmployeCertification employeCertification) {

    }

}
