package tn.esprit.test;

import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.services.ServiceEmployeFormation;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.MyDatabase;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployeCertification;


import tn.esprit.services.ServiceCertification;
import tn.esprit.models.Certification;
import tn.esprit.utils.SessionManager;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;



public class Main {
    public static void main(String[] args) {

        ServiceFormation serviceFormation = new ServiceFormation();
        ServiceEmployeCertification employeCertification = new ServiceEmployeCertification();
        ServiceCertification serviceCertification = new ServiceCertification();
        ServiceEmployeFormation employeFormation = new ServiceEmployeFormation();

        /*
        Formation des employes
       //AJOUT
        List<Integer> employeIds = Arrays.asList(1, 2);
        service.affecterFormationAEmployes(4, employeIds);
         */



        int idFormationTest = 4;
        List<Employee> employes = employeFormation.getEmployesByFormation(idFormationTest);

        System.out.println("📌 Liste des employés inscrits à la formation " + idFormationTest + " :");
        for (Employee emp : employes) {
            System.out.println("➡ " + emp.getNom() + " " + emp.getPrenom() + " | Email: " + emp.getEmail());
        }




        /*
       //certification des employes
//AFFICHAGE
 int idEmploye =6;
        String nomEmploye = employeCertification.getNomEmployeById(idEmploye);

        List<Certification> certifications = employeCertification.getCertificationsByEmploye(idEmploye);

        System.out.println("Certifications de l'employé " + nomEmploye + ":" );
        for (Certification certif : certifications) {
            System.out.println(certif.getTitreCertif());
        }


//AJOUT
 int idemploye = 6;
        int idCertif = 8;
        Date dateObtention = Date.valueOf("2025-01-20");
        employeCertification.ajouterCertificationAEmploye(idemploye, idCertif, dateObtention);



 */





/*
CHOIX TYPE  USER
        User rhUser = new User();
        rhUser.setUserType("Admin");
        SessionManager.getInstance().login(rhUser);



 */



        //System.out.println(serviceFormation.getAll());

        //System.out.println(serviceCertification.getAll());
    }





/*
System.out.println(serviceFormation.getAll());
 //AJOUT

 Date dateDebut= Date.valueOf("2025-01-01");
        Date dateFin= Date.valueOf("2025-01-05");
        Formation formation = new Formation("ai", "Formation ", dateDebut,dateFin);
        serviceFormation.add(formation);
//UPDATE

 Date dateDebutModifiee= Date.valueOf("2020-01-01");
        Date dateFinModifiee= Date.valueOf("2020-01-05");
        Formation formation = new Formation("update", "formation modifiee", dateDebutModifiee,dateFinModifiee);
        formation.setIdFormation(5);
        serviceFormation.update(formation);
//DELETE

Formation formation = new Formation();
        formation.setIdFormation(4);
        serviceFormation.delete(formation);


 */

//CERTIFICATION

/*



        //AJOUT
Formation formation = new Formation();
        formation.setIdFormation(1);
        System.out.println(serviceFormation.getAll());
        serviceCertification.add(new Certification("machine_learning","nvidia",formation));

//UPDATE

Formation formation = new Formation();
        formation.setIdFormation(3);
        Certification certification = new Certification("administration systeme","esprit",formation);
        certification.setIdCertif(1);
        serviceCertification.update(certification);

//DELETE

Certification certification = new Certification();
        certification.setIdCertif(4);
        serviceCertification.delete(certification);

 */



















}
