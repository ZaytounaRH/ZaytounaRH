package tn.esprit.test;

import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.MyDatabase;

import tn.esprit.services.ServiceCertification;
import tn.esprit.models.Certification;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {

        ServiceFormation serviceFormation = new ServiceFormation();

        //AJOUT
        serviceFormation.add(new Formation("Java Avancé", "Formation complète sur Java", "3 mois", 4, 4));
        serviceFormation.add(new Formation("test","unix","15 jours",1,1));


        //UPDATE
        Formation formation = new Formation("test","unix","1 jour",1,1);
        formation.setIdFormation(1);
        serviceFormation.update(formation);


        //DELETE

        Formation formationn = new Formation();
        formationn.setIdFormation(1);
        serviceFormation.delete(formationn);

        //AFFICHAGE
        System.out.println(serviceFormation.getAll());






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
}
