package tn.esprit.test;

import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.MyDatabase;
import tn.esprit.models.Rh;
import tn.esprit.models.Employe;


import tn.esprit.services.ServiceCertification;
import tn.esprit.models.Certification;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {

        ServiceFormation serviceFormation = new ServiceFormation();
/*
//AJOUT
        Employe employe = new Employe(1);
        Rh rh = new Rh(3);
        Certification certification = new Certification(6);
        Formation formation = new Formation("Formation finance", "Formation complète sur finance", "15 jours",employe, rh, certification);
       serviceFormation.add(formation);

//UPDATE
        Employe employe = new Employe(2, "Nouveau Employé");
        Rh rh = new Rh(3, "Nouveau RH");
        Certification certif = new Certification(1, "Certif Updated");
        Formation formation = new Formation("Formation updated", "description modifiee", "2 jours", employe, rh, certif);
        formation.setIdFormation(2);
        serviceFormation.update(formation);
//DELETE

        Formation formation = new Formation();
        formation.setIdFormation(7);
        serviceFormation.delete(formation);
        System.out.println(serviceFormation.getAll());
 */

//CERTIFICATION
        ServiceCertification serviceCertification = new ServiceCertification();
/*
        //AJOUT

        serviceCertification.add(new Certification("machine_learning","nvidia"));
        //AFFICHAGE

        //UPDATE

        Certification certification = new Certification("modified","google");
        certification.setIdCertif(8);
        serviceCertification.update(certification);


 */


        //DELETE
        Certification certification = new Certification();
        certification.setIdCertif(5);
        serviceCertification.delete(certification);
        System.out.println(serviceCertification.getAll());
/*














    }
 */

}}
