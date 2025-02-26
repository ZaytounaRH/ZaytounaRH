package tn.esprit.test;
import tn.esprit.models.Candidat;
import java.sql.Date;

import tn.esprit.models.OffreEmploi;
import tn.esprit.models.Entretien;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.models.Entretien.StatutEntretien;
import tn.esprit.models.Entretien.TypeEntretien;
import tn.esprit.services.ServiceEntretien;
import tn.esprit.models.RH;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.utils.SessionManager;
public class Main {
    public static void main(String[] args) {
        // Étape 1 : Créer un Candidat et l'ajouter à la session
        Candidat candidat = new Candidat(1, "John Doe", 2, "Ons", "Bekri", "aaaaaaaaaaaa", "bekrions@gmail.com", "Femme", Date.valueOf("2003-06-06"), "candidat", "gfbgfbfb", 1, "Actif");
        SessionManager.getInstance().setCurrentUser(candidat);  // Simule un candidat connecté

        // Étape 2 : Créer une instance de RH et une OffreEmploi
      //  RH rh = new RH(1, "1234567890", 5, "Nom", "Prenom", "Adresse", "email@example.com", "Femme", Date.valueOf("2000-01-01"), "admin", "password123", 1001);
        //SessionManager.getInstance().setCurrentUser(rh);  // Simule un candidat connecté

        // Créer une OffreEmploi
        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setIdOffre(3);
        offreEmploi.setTitreOffre("Développeur Java");
        offreEmploi.setDescription("Poste de développeur Java");
        offreEmploi.setSalaire(3000);
        offreEmploi.setStatut(OffreEmploi.StatutOffre.OUVERTE);
       // offreEmploi.setRh(rh);

        // Étape 3 : Créer un Entretien et l'associer à l'OffreEmploi
        Entretien entretien = new Entretien();
        entretien.setDateEntretien(LocalDate.now());
        entretien.setHeureEntretien(LocalTime.of(10, 30));
        entretien.setTypeEntretien(Entretien.TypeEntretien.PRESENTIEL);
        entretien.setCommentaire("Entretien pour le poste de Développeur Java");
        entretien.setOffreEmploi(offreEmploi);  // Association de l'offre à l'entretien

        // Étape 4 : Appeler la méthode add pour ajouter l'entretien
        ServiceEntretien entretienService = new ServiceEntretien(); // Créez une instance de votre service pour gérer les entretiens
        entretienService.add(entretien);  // Appel de la méthode add
    }
}