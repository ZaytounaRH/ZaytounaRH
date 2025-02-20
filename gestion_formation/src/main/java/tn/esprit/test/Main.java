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

        int idEmploye =1;
        String nomEmploye = employeCertification.getNomEmployeById(idEmploye);

        List<Certification> certifications = employeCertification.getCertificationsByEmploye(idEmploye);

        System.out.println("Certifications de l'employé " + nomEmploye + ":" );
        for (Certification certif : certifications) {
            System.out.println(certif.getTitreCertif());
        }



 */
        //DELETE

        Formation formation = new Formation();
        formation.setIdFormation(5);
        serviceFormation.delete(formation);



     System.out.println(serviceFormation.getAll());
    }





/*
 //AJOUT
        Employe employe = new Employe(1);
        Rh rh = new Rh(3);
        Certification certification = new Certification(6);
        Formation formation = new Formation("controle", "Formation controlee", "15 jours",employe, rh, certification);
        serviceFormation.add(formation);

//UPDATE
        Employe employe = new Employe(2);
        Rh rh = new Rh(3);
        Certification certif = new Certification(7);
        Formation formation = new Formation("update controle", "description modifiee", "2 jours", employe, rh, certif);
        formation.setIdFormation(6);
        serviceFormation.update(formation);




 */

//CERTIFICATION

/*
        //AJOUT

        serviceCertification.add(new Certification("machine_learning","nvidia"));
        //AFFICHAGE

        //UPDATE

        Certification certification = new Certification("modified","google");
        certification.setIdCertif(8);
        serviceCertification.update(certification);
 //DELETE
        Certification certification = new Certification();
        certification.setIdCertif(5);
        serviceCertification.delete(certification);
        System.out.println(serviceCertification.getAll());

 */



















}
