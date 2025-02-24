package tn.esprit.test;

import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.MyDatabase;
import tn.esprit.models.Rh;
import tn.esprit.models.Employe;
import tn.esprit.services.ServiceEmployeCertification;


import tn.esprit.services.ServiceCertification;
import tn.esprit.models.Certification;

import java.sql.Date;
import java.util.List;

import static tn.esprit.services.ServiceFormation.isValidFormation;

public class Main {
    public static void main(String[] args) {

        ServiceFormation serviceFormation = new ServiceFormation();
        ServiceEmployeCertification employeCertification = new ServiceEmployeCertification();
        ServiceCertification serviceCertification = new ServiceCertification();
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


        Employe employe = new Employe(6);
        Rh rh = new Rh(4);
        Certification certification = new Certification(6);
        Date dateDebut= Date.valueOf("2023-01-01");
        Date dateFin= Date.valueOf("2023-01-05");

        Formation formation = new Formation("java", "Formation en java", dateDebut,dateFin,employe, rh, certification);
        serviceFormation.add(formation);





        System.out.println(serviceFormation.getAll());
       // System.out.println(serviceCertification.getAll());
    }





/*
System.out.println(serviceFormation.getAll());
 //AJOUT

//UPDATE

Employe employe = new Employe(8);
        Rh rh = new Rh(4);
        Certification certif = new Certification(7);
        Date dateDebutModifiee= Date.valueOf("2024-01-01");
        Date dateFinModifiee= Date.valueOf("2024-01-05");
        Formation formation = new Formation("update", "formation modifiee", dateDebutModifiee,dateFinModifiee, employe, rh, certif);
        formation.setIdFormation(19);
        serviceFormation.update(formation);
//DELETE

 Formation formation = new Formation();
        formation.setIdFormation(19);
        serviceFormation.delete(formation);


 */

//CERTIFICATION

/*



        //AJOUT

        serviceCertification.add(new Certification("machine_learning","nvidia"));
//UPDATE

        Certification certification = new Certification("controle saisie","esprit");
        certification.setIdCertif(7);
        serviceCertification.update(certification);

//DELETE

        Certification certification = new Certification();
        certification.setIdCertif(4);
        serviceCertification.delete(certification);

 */



















}
