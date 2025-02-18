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

        Employe employe = new Employe(1);  // Ici, tu passes l'objet employé avec son ID
        Rh rh = new Rh(3);  // Ici, tu passes l'objet RH avec son ID
        Certification certification = new Certification(6);



        //AJOUT
        Formation formation = new Formation("Formation finance", "Formation complète sur finance", "15 jours",employe, rh, certification);


       //serviceFormation.add(formation);
        System.out.println(serviceFormation.getAll());

/*
//UPDATE
        Formation formationnn = new Formation("Formation UML", "Formation complète sur UML", "2 mois", employe.getIdEmploye(), rh.getIdRh(), formation.getIdFormation());
        formationnn.setIdFormation(1);
        serviceFormation.update(formation);


        //DELETE

        Formation formationn = new Formation();
        formationn.setIdFormation(1);
        serviceFormation.delete(formationn);

        //AFFICHAGE







        //CERTIFICATION
        ServiceCertification serviceCertification = new ServiceCertification();

        //AJOUT
        Date dateCertiff = Date.valueOf("2025-01-11");
        serviceCertification.add(new Certification("testing",dateCertiff,2));

        //AFFICHAGE
        System.out.println(serviceCertification.getAll());

        //UPDATE
        Date dateCertif = Date.valueOf("2024-01-11");
        Certification certification = new Certification("updateing",dateCertif,2);
        certification.setIdCertif(3);
        serviceCertification.update(certification);



        //DELETE
       Certification certificationn = new Certification();
        certificationn.setIdCertif(3);
        serviceCertification.delete(certificationn);


    }
 */

}}
