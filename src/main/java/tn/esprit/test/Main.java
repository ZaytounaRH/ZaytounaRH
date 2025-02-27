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
        Candidat candidat = new Candidat(3, "John Doe", 2, "Ons", "Bekri", "aaaaaaaaaaaa", "bekrions@gmail.com", "Femme", Date.valueOf("2003-06-06"), "candidat", "gfbgfbfb", 2, "Actif");
        SessionManager.getInstance().setCurrentUser(candidat);  // Simule un candidat connecté

        // Étape 2 : Créer une instance de RH et une OffreEmploi
        RH rh = new RH(1, "1234567890", 5, "Nom", "Prenom", "Adresse", "email@example.com", "Femme", Date.valueOf("2000-01-01"), "admin", "password123", 1);
        SessionManager.getInstance().setCurrentUser(rh);  // Simule un candidat connecté

        // Créer une OffreEmploi
        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setIdOffre(3);
        offreEmploi.setTitreOffre("Développeur Java");
        offreEmploi.setDescription("Poste de développeur Java");
        offreEmploi.setSalaire(3000);
        offreEmploi.setStatut(OffreEmploi.StatutOffre.OUVERTE);

        // Étape 3 : Créer un Entretien et l'associer à l'OffreEmploi
        Entretien entretienToAdd = new Entretien();
        entretienToAdd.setDateEntretien(LocalDate.now());
        entretienToAdd.setHeureEntretien(LocalTime.of(10, 30));
        entretienToAdd.setTypeEntretien(Entretien.TypeEntretien.PRESENTIEL);
        entretienToAdd.setCommentaire("Entretien pour le poste de Développeur Java");
        entretienToAdd.setOffreEmploi(offreEmploi);  // Association de l'offre à l'entretien

        // Étape 4 : Appeler la méthode add pour ajouter l'entretien
        ServiceEntretien entretienService = new ServiceEntretien(); // Créez une instance de votre service pour gérer les entretiens
        entretienService.add(entretienToAdd);  // Appel de la méthode add


        ///////////////////////////////////////
        // Appeler la méthode getAll() pour récupérer les entretiens
        ServiceEntretien serviceEntretien = new ServiceEntretien();
        List<Entretien> listeEntretiens = serviceEntretien.getAll();  // Renommé pour éviter la collision

        if (!listeEntretiens.isEmpty()) {
            for (Entretien entretien : listeEntretiens) {
                System.out.println("Entretien ID: " + entretien.getIdEntretien());
                System.out.println("Offre d'Emploi: " + entretien.getOffreEmploi().getTitreOffre());
                System.out.println("Date: " + entretien.getDateEntretien());
                System.out.println("Heure: " + entretien.getHeureEntretien());
                System.out.println("Statut: " + entretien.getStatut());
                System.out.println("Candidat: " + entretien.getCandidat().getNom() + " " + entretien.getCandidat().getPrenom());  // Affichage du nom et prénom du candidat

                System.out.println("-----------------------------------------");
            }
        } else {
            System.out.println("Aucun entretien trouvé ou accès non autorisé.");
        }


        // Étape 5 : Mise à jour complète d'un entretien
        Entretien entretienToUpdate = listeEntretiens.get(1);  // Récupérer le premier entretien de la liste

        // Modifier tous les champs
        entretienToUpdate.setDateEntretien(LocalDate.of(2025, 5, 15));  // Exemple de nouvelle date
        entretienToUpdate.setHeureEntretien(LocalTime.of(14, 45));  // Exemple de nouvelle heure
        entretienToUpdate.setTypeEntretien(Entretien.TypeEntretien.TELEPHONIQUE);  // Modifier le type d'entretien
        entretienToUpdate.setCommentaire("Entretien reprogrammé pour le poste de Développeur Java");
        entretienToUpdate.setStatut(Entretien.StatutEntretien.TERMINE);  // Modifier le statut

        // Mettre à jour l'entretien dans la base de données
        entretienService.update(entretienToUpdate);

        // Affichage des entretiens après modification
        System.out.println("\nAprès la mise à jour :");
        for (Entretien entretien : listeEntretiens) {
            System.out.println("Entretien ID: " + entretien.getIdEntretien());
            System.out.println("Offre d'Emploi: " + entretien.getOffreEmploi().getTitreOffre());
            System.out.println("Date: " + entretien.getDateEntretien());
            System.out.println("Heure: " + entretien.getHeureEntretien());
            System.out.println("Statut: " + entretien.getStatut());
            System.out.println("Candidat: " + entretien.getCandidat().getNom() + " " + entretien.getCandidat().getPrenom());
            System.out.println("-----------------------------------------");
        }




        // Suppression du premier entretien de la liste
        if (!listeEntretiens.isEmpty()) {
            Entretien entretienToRemove = listeEntretiens.get(0);
            entretienService.remove(entretienToRemove.getIdEntretien());
            System.out.println("\nEntretien supprimé avec succès : ID " + entretienToRemove.getIdEntretien());
        }

        // Récupérer et afficher les entretiens après suppression
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

    }}

