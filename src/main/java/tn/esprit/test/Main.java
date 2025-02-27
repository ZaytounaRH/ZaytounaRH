package tn.esprit.test;

import tn.esprit.models.Candidat;
import java.sql.Date;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.Entretien;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.models.Entretien.StatutEntretien;
import tn.esprit.models.Entretien.TypeEntretien;
import tn.esprit.services.ServiceEntretien;
import tn.esprit.services.ServiceOffreEmploi;
import tn.esprit.models.RH;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.utils.SessionManager;

public class Main {
    public static void main(String[] args) {
        // 🟢 GESTION DES ENTRETIENS
        // Étape 1 : Créer un Candidat et l'ajouter à la session
        Candidat candidat = new Candidat(3, "John Doe", 2, "Ons", "Bekri", "aaaaaaaaaaaa", "bekrions@gmail.com", "Femme", Date.valueOf("2003-06-06"), "candidat", "gfbgfbfb", 2, "Actif");
        SessionManager.getInstance().setCurrentUser(candidat);  // Simule un candidat connecté

        // 🟢 INITIALISATION DE LA SESSION POUR LE RH
       // RH rh = new RH(1, "1234567890", 5, "Nom", "Prenom", "Adresse", "email@example.com", "Femme", Date.valueOf("2000-01-01"), "admin", "password123", 1);
        //SessionManager.getInstance().setCurrentUser(rh);

        // 🔥 CREATION D'UNE NOUVELLE OFFRE D'EMPLOI
        OffreEmploi nouvelleOffre = new OffreEmploi();
        nouvelleOffre.setTitreOffre("Développeur Full Stack");
        nouvelleOffre.setDescription("Nous recherchons un développeur Full Stack expérimenté.");
        nouvelleOffre.setDatePublication(Date.valueOf(LocalDate.now()));
        nouvelleOffre.setSalaire(4500.0);
        nouvelleOffre.setStatut(StatutOffre.OUVERTE);
        nouvelleOffre.setEntretiens(new ArrayList<>()); // Pas d'entretiens pour cette offre

        // 🚀 AJOUT DE L'OFFRE D'EMPLOI VIA LE SERVICE
        ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
        serviceOffreEmploi.add(nouvelleOffre);

        System.out.println("\n✅ Nouvelle offre d'emploi ajoutée avec succès !");
        System.out.println(nouvelleOffre);

        ///////////////////////////////////////////////
        // 🟢 RÉCUPÉRATION ET AFFICHAGE DE TOUTES LES OFFRES D'EMPLOI
        List<OffreEmploi> listeOffres = serviceOffreEmploi.getAll();
        System.out.println("\n📋 Liste des offres d'emploi :");
        for (OffreEmploi offre : listeOffres) {
            System.out.println("ID: " + offre.getIdOffre() + ", Titre: " + offre.getTitreOffre() + ", Salaire: " + offre.getSalaire());
        }

        ///////////////////////////////////////////////

        // Créer une OffreEmploi pour l'entretien
        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setIdOffre(3);
        offreEmploi.setTitreOffre("Développeur Java");
        offreEmploi.setDescription("Poste de développeur Java");
        offreEmploi.setSalaire(3000);
        offreEmploi.setStatut(StatutOffre.OUVERTE);

        // Étape 3 : Créer un Entretien et l'associer à l'OffreEmploi
        Entretien entretienToAdd = new Entretien();
        entretienToAdd.setDateEntretien(LocalDate.now());
        entretienToAdd.setHeureEntretien(LocalTime.of(10, 30));
        entretienToAdd.setTypeEntretien(TypeEntretien.PRESENTIEL);
        entretienToAdd.setCommentaire("Entretien pour le poste de Développeur Java");
        entretienToAdd.setOffreEmploi(offreEmploi);

        // 🚀 AJOUT DE L'ENTRETIEN
        ServiceEntretien entretienService = new ServiceEntretien();
        entretienService.add(entretienToAdd);

        // 🟢 RÉCUPÉRATION DES ENTRETIENS
        List<Entretien> listeEntretiens = entretienService.getAll();

        if (!listeEntretiens.isEmpty()) {
            for (Entretien entretien : listeEntretiens) {
                System.out.println("\nEntretien ID: " + entretien.getIdEntretien());
                System.out.println("Offre d'Emploi: " + entretien.getOffreEmploi().getTitreOffre());
                System.out.println("Date: " + entretien.getDateEntretien());
                System.out.println("Heure: " + entretien.getHeureEntretien());
                System.out.println("Statut: " + entretien.getStatut());
                System.out.println("Candidat: " + entretien.getCandidat().getNom() + " " + entretien.getCandidat().getPrenom());
                System.out.println("-----------------------------------------");
            }
        } else {
            System.out.println("Aucun entretien trouvé ou accès non autorisé.");
        }

        // 🟢 MISE À JOUR D'UN ENTRETIEN
        if (!listeEntretiens.isEmpty()) {
            Entretien entretienToUpdate = listeEntretiens.get(0);

            entretienToUpdate.setDateEntretien(LocalDate.of(2025, 5, 15));
            entretienToUpdate.setHeureEntretien(LocalTime.of(14, 45));
            entretienToUpdate.setTypeEntretien(TypeEntretien.TELEPHONIQUE);
            entretienToUpdate.setCommentaire("Entretien reprogrammé pour le poste de Développeur Java");
            entretienToUpdate.setStatut(StatutEntretien.TERMINE);

            entretienService.update(entretienToUpdate);

            System.out.println("\n✅ Entretien mis à jour avec succès !");
        }

        // 🟢 SUPPRESSION D'UN ENTRETIEN
        if (!listeEntretiens.isEmpty()) {
            Entretien entretienToRemove = listeEntretiens.get(0);
            entretienService.remove(entretienToRemove.getIdEntretien());
            System.out.println("\n🗑️ Entretien supprimé avec succès : ID " + entretienToRemove.getIdEntretien());
        }

        // 🔍 AFFICHAGE APRÈS SUPPRESSION
        listeEntretiens = entretienService.getAll();
        System.out.println("\nAprès la suppression :");
        if (!listeEntretiens.isEmpty()) {
            for (Entretien entretien : listeEntretiens) {
                System.out.println("Entretien ID: " + entretien.getIdEntretien());
                System.out.println("Candidat : " + entretien.getCandidat().getNom() + " " + entretien.getCandidat().getPrenom());
                System.out.println("Offre : " + entretien.getOffreEmploi().getTitreOffre());
                System.out.println("Commentaire : " + entretien.getCommentaire());
                System.out.println("-----------------------------------------");
            }
        } else {
            System.out.println("Aucun entretien restant.");
        }
    }
}
