package tn.esprit.test;

import tn.esprit.models.Candidat;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.Entretien;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.models.Entretien.StatutEntretien;
import tn.esprit.models.Entretien.TypeEntretien;
import tn.esprit.services.ServiceOffreEmploi;
import tn.esprit.services.ServiceEntretien;
import tn.esprit.models.RH;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Créer un responsable RH (ici, ID = 3 par exemple)
        RH rh = new RH(3); // ID du responsable RH
        rh.setNom("Bekri");

        // Créer une offre d'emploi
        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setTitreOffre("Développeur Java");
        offreEmploi.setDescription("Nous recherchons un développeur Java expérimenté.");
        offreEmploi.setDatePublication(java.sql.Date.valueOf(LocalDate.now()));
        offreEmploi.setSalaire(3000.0);
        offreEmploi.setStatut(StatutOffre.ENCOURS);
        offreEmploi.setRh(rh);  // Assigner le responsable RH à l'offre d'emploi

        // Créer un autre responsable RH pour tester
        RH rh2 = new RH(4); // ID du responsable RH 2
        rh2.setNom("Zheni");

        // Créer une deuxième offre d'emploi
        OffreEmploi offreEmploi2 = new OffreEmploi();
        offreEmploi2.setTitreOffre("Développeur Front-End");
        offreEmploi2.setDescription("Nous recherchons un développeur Front-End expérimenté.");
        offreEmploi2.setDatePublication(java.sql.Date.valueOf(LocalDate.now()));
        offreEmploi2.setSalaire(2500.0);
        offreEmploi2.setStatut(StatutOffre.ENCOURS);
        offreEmploi2.setRh(rh2);  // Assigner le responsable RH à la deuxième offre d'emploi

        // Créer un candidat
        Candidat candidat = new Candidat(1);

        // Créer des entretiens associés à l'offre d'emploi
        Entretien entretien1 = new Entretien();
        entretien1.setDateEntretien(LocalDate.of(2025, 2, 25));
        entretien1.setHeureEntretien(LocalTime.of(10, 0));
        entretien1.setTypeEntretien(TypeEntretien.PRESENTIEL);
        entretien1.setStatut(StatutEntretien.PLANIFIE);
        entretien1.setCommentaire("Entretien prévu avec le candidat.");
        entretien1.setOffreEmploi(offreEmploi);  // Associer l'entretien à l'offre d'emploi
        entretien1.setCandidat(candidat);  // Associer le candidat à l'entretien

        Entretien entretien2 = new Entretien();
        entretien2.setDateEntretien(LocalDate.of(2025, 2, 26));
        entretien2.setHeureEntretien(LocalTime.of(14, 0));
        entretien2.setTypeEntretien(TypeEntretien.DISTANCIEL);
        entretien2.setStatut(StatutEntretien.PLANIFIE);
        entretien2.setCommentaire("Entretien en ligne avec un autre candidat.");
        entretien2.setOffreEmploi(offreEmploi);  // Associer l'entretien à l'offre d'emploi
        entretien2.setCandidat(candidat);  // Associer le candidat à l'entretien

        // Ajouter les entretiens à l'offre d'emploi
        List<Entretien> entretiens = new ArrayList<>();
        entretiens.add(entretien1);
        entretiens.add(entretien2);
        offreEmploi.setEntretiens(entretiens);

        // Créer des services pour gérer les offres d'emploi et les entretiens
        ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
        serviceOffreEmploi.add(offreEmploi); // Ajouter l'offre d'emploi

        ServiceEntretien serviceEntretien = new ServiceEntretien();
        serviceEntretien.add(entretien1); // Ajouter l'entretien
        serviceEntretien.add(entretien2);

        // Créer une liste de responsables RH
        List<RH> listRH = new ArrayList<>();
        listRH.add(rh);
        listRH.add(rh2); // Ajouter d'autres responsables RH si nécessaire...

        // Récupérer et afficher les offres d'emploi pour chaque responsable RH
        for (RH responsable : listRH) {
            List<OffreEmploi> offresRH = serviceOffreEmploi.getOffresByRH(responsable.getIdRH());
            System.out.println("Offres d'emploi gérées par RH : " + responsable.getNom());
            for (OffreEmploi offre : offresRH) {
                System.out.println(offre);
            }
        }

        // Récupérer toutes les offres d'emploi
        List<OffreEmploi> allOffres = serviceOffreEmploi.getAll();
        System.out.println("\nListe des offres d'emploi :");
        for (OffreEmploi offre : allOffres) {
            System.out.println(offre);
        }

        // Récupérer les entretiens associés
        List<Entretien> entretiensList = serviceEntretien.getAll();
        System.out.println("\nListe des entretiens :");
        for (Entretien entretien : entretiensList) {
            System.out.println(entretien);
        }
        // Mise à jour d'une offre d'emploi et de ses entretiens
        offreEmploi.setTitreOffre("Développeur Full Stack");
        serviceOffreEmploi.update(offreEmploi);

        // Mise à jour d'un entretien
        entretien1.setStatut(StatutEntretien.TERMINE);
        serviceEntretien.update(entretien1);

        // Supprimer un entretien
          serviceEntretien.remove(entretien2.getIdEntretien());

        // Supprimer une offre d'emploi et ses entretiens associés
         serviceOffreEmploi.remove(offreEmploi.getIdOffre());
    }


}
