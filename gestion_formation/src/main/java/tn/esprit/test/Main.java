package tn.esprit.test;

import tn.esprit.models.*;
import tn.esprit.services.ServiceEmployeFormation;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.MyDatabase;
import tn.esprit.services.ServiceEmployeCertification;


import tn.esprit.services.ServiceCertification;
import tn.esprit.utils.SessionManager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Main {
    public static void main(String[] args) {

        ServiceFormation serviceFormation = new ServiceFormation();
        ServiceEmployeCertification serviceEmployeCertification = new ServiceEmployeCertification();
        ServiceCertification serviceCertification = new ServiceCertification();
        ServiceEmployeFormation serviceEmployeFormation = new ServiceEmployeFormation();

        /*
        Formation des employes

       //AJOUT
        List<Integer> employeIds = Arrays.asList(1, 2);
        serviceEmployeFormation.affecterFormationAEmployes(4, employeIds);

        //AFFICHAGE
         serviceEmployeFormation.afficherEmployesParFormation(4);

         //DELETE
         serviceEmployeFormation.supprimerEmployeDeFormation(1,6);

         //UPDATE
        List<Integer> newEmployeIds = Arrays.asList(1, 3);
        serviceEmployeFormation.modifierListeEmployesFormation(4, newEmployeIds);
         */

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
 int idemploye = 2;
        int idCertif = 3;
        Date dateObtention = Date.valueOf("2025-01-26");
        serviceEmployeCertification.ajouterCertificationAEmploye(idemploye, idCertif, dateObtention);

 */
serviceEmployeCertification.getCertificationsByEmployee(1);






/*
CHOIX TYPE  USER
        User rhUser = new User();
        rhUser.setUserType("Admin");
        SessionManager.getInstance().login(rhUser);
 */




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
