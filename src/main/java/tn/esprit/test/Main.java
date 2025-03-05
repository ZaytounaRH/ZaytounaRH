package tn.esprit.test;

import tn.esprit.models.Candidat;
import tn.esprit.models.OffreEmploi;
import tn.esprit.models.Entretien;
import tn.esprit.models.OffreEmploi.StatutOffre;
import tn.esprit.models.Entretien.StatutEntretien;
import tn.esprit.models.Entretien.TypeEntretien;
import tn.esprit.services.ServiceEntretien;
import tn.esprit.services.ServiceOffreEmploi;
import tn.esprit.models.RH;
import tn.esprit.utils.SessionManager;
import java.sql.Connection;
import tn.esprit.utils.MyDatabase;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Connection cnx = MyDatabase.getInstance().getCnx();

        // 🟢 GESTION DES ENTRETIENS
        // Étape 1 : Créer un Candidat et l'ajouter à la session
        //Candidat candidat = new Candidat(1, "John Doe", 2, "Ons", "Bekri", "aaaaaaaaaaaa", "bekrions@gmail.com", "Femme", Date.valueOf("2003-06-06"), "candidat", "gfbgfbfb", 1, "Actif");
        //SessionManager.getInstance().setCurrentUser(candidat);  // Simule un candidat connecté

        //RH rh = new RH(1, "1234567890", 5, "Nom", "Prenom", "Adresse", "email@example.com", "Femme", Date.valueOf("2000-01-01"), "admin", "password123", 1);
      //  SessionManager.getInstance().setCurrentUser(rh);

        System.out.println("Utilisateur connecté : " + SessionManager.getInstance().getCurrentUser().getClass().getSimpleName());

        // 🔥 CREATION D'UNE NOUVELLE OFFRE D'EMPLOI (Accessible uniquement pour RH)
        if (SessionManager.getInstance().getCurrentUser() instanceof RH) {
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
        } else {
            System.out.println("Erreur : Seul le RH peut ajouter une offre d'emploi !");
        }

        ///////////////////////////////////////////////
        // 🟢 RÉCUPÉRATION ET AFFICHAGE DE TOUTES LES OFFRES D'EMPLOI (Accessible uniquement pour RH)
       if (SessionManager.getInstance().getCurrentUser() instanceof RH) {
            ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
        List<OffreEmploi> offres = serviceOffreEmploi.getAll();
        System.out.println("Nombre d'offres récupérées : " + offres.size());

            if (offres.isEmpty()) {
                System.out.println("Aucune offre d'emploi disponible.");
            } else {
                System.out.println("Liste des offres d'emploi :");
                for (OffreEmploi offre : offres) {
                    System.out.println("\nOffre d'Emploi ID : " + offre.getIdOffre());
                    System.out.println("Titre : " + offre.getTitreOffre());
                    System.out.println("Description : " + offre.getDescription());
                    System.out.println("Salaire : " + offre.getSalaire() + " TND");
                    System.out.println("Statut : " + offre.getStatut());
                    System.out.println("Date de Publication : " + offre.getDatePublication());
                    System.out.println("Nombre d'entretiens associés : " + offre.getEntretiens().size());
                    System.out.println("-----------------------------------------");
                }
            }
        } else {
            System.out.println("Erreur : Seul le RH peut afficher la liste des offres !");
        }

        // 🟢 MISE À JOUR D'UNE OFFRE D'EMPLOI (Accessible uniquement pour RH)
        if (SessionManager.getInstance().getCurrentUser() instanceof RH) {
           ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
            List<OffreEmploi> offres = serviceOffreEmploi.getAll();

            if (!offres.isEmpty()) {
                OffreEmploi offreEmploiToUpdate = offres.get(1);

                // Mise à jour des détails de l'offre d'emploi
                offreEmploiToUpdate.setTitreOffre("Développeur Java Senior");
                offreEmploiToUpdate.setDescription("Nous recherchons un développeur Java Senior expérimenté.");
                offreEmploiToUpdate.setSalaire(5500.0);
                offreEmploiToUpdate.setStatut(StatutOffre.FERMEE);

                // Mise à jour de l'offre via le service
                serviceOffreEmploi.update(offreEmploiToUpdate);

                System.out.println("\n✅ Offre d'emploi mise à jour avec succès !");
            }
        } else {
            System.out.println("Erreur : Seul le RH peut mettre à jour une offre d'emploi !");
        }

        ///////////////////////////////////////////////

        // 🟢 SUPPRESSION D'UNE OFFRE D'EMPLOI (Accessible uniquement pour RH)
        if (SessionManager.getInstance().getCurrentUser() instanceof RH) {
           ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
           List<OffreEmploi> offres = serviceOffreEmploi.getAll();

            if (!offres.isEmpty()) {
                OffreEmploi offreToRemove = offres.get(0);
                serviceOffreEmploi.delete(offreToRemove);
                System.out.println("\n🗑️ Offre d'emploi supprimée avec succès : ID " + offreToRemove.getIdOffre());
            }
        } else {
            System.out.println("Erreur : Seul le RH peut supprimer une offre d'emploi !");
        }

        // 🔍 AFFICHAGE APRÈS SUPPRESSION
        if (SessionManager.getInstance().getCurrentUser() instanceof RH) {
            ServiceOffreEmploi serviceOffreEmploi = new ServiceOffreEmploi();
            List<OffreEmploi> offresPostSuppression = serviceOffreEmploi.getAll();
            System.out.println("\nAprès la suppression :");
            if (!offresPostSuppression.isEmpty()) {
                for (OffreEmploi offre : offresPostSuppression) {
                    System.out.println(offre);
                }
            } else {
                System.out.println("Aucune offre d'emploi trouvée.");
            }
        }

        // 🟢 CREATION D'UN ENTRETIEN (Accessible uniquement pour Candidat)
        if (SessionManager.getInstance().getCurrentUser() instanceof Candidat) {
            // Créer une OffreEmploi pour l'entretien
            OffreEmploi offreEmploi = new OffreEmploi();
            offreEmploi.setIdOffre(111);
            offreEmploi.setTitreOffre("Développeur Java");
            offreEmploi.setDescription("Poste de développeur Java");
            offreEmploi.setSalaire(3000);
            offreEmploi.setStatut(StatutOffre.OUVERTE);
            ServiceEntretien serviceEntretien = new ServiceEntretien(cnx);  // Passer la connexion ici

            // Créer un Entretien et l'associer à l'OffreEmploi
            Entretien entretienToAdd = new Entretien();
            entretienToAdd.setDateEntretien(LocalDate.now());
            entretienToAdd.setHeureEntretien(LocalTime.of(10, 30));
            entretienToAdd.setTypeEntretien(TypeEntretien.PRESENTIEL);
            entretienToAdd.setCommentaire("Entretien pour le poste de Développeur Java");
            entretienToAdd.setOffreEmploi(offreEmploi);
            //entretienToAdd.setCandidat(candidat);  // Associer le candidat à l'entretien
            serviceEntretien.add(entretienToAdd);
            // 🚀 AJOUT DE L'ENTRETIEN VIA LE SERVICE
            ServiceEntretien entretienService = new ServiceEntretien(cnx);  // Passer la connexion ici

            try {
                entretienService.add(entretienToAdd);
                System.out.println("\n✅ Entretien ajouté avec succès !");
            } catch (Exception e) {
                System.out.println("Erreur lors de l'ajout de l'entretien : " + e.getMessage());
            }
        } else {
            System.out.println("Erreur : Seuls les Candidats peuvent ajouter un entretien !");
        }

        // 🟢 RÉCUPÉRATION DES ENTRETIENS (Accessible uniquement pour RH)
        if (SessionManager.getInstance().getCurrentUser() instanceof RH) {
            ServiceEntretien entretienService = new ServiceEntretien(cnx);

            List<Entretien> listeEntretiens = entretienService.getAll();

            if (!listeEntretiens.isEmpty()) {
                for (Entretien entretien : listeEntretiens) {
                    System.out.println("\nEntretien ID: " + entretien.getIdEntretien());
                    System.out.println("Offre d'Emploi: " + entretien.getOffreEmploi().getTitreOffre());
                    System.out.println("Date: " + entretien.getDateEntretien());
                    System.out.println("Heure: " + entretien.getHeureEntretien());
                    System.out.println("Statut: " + entretien.getStatut());
                    System.out.println("Commentaire: " + entretien.getCommentaire());
                    System.out.println("-------------------------------------------");
                }
            } else {
                System.out.println("Aucun entretien trouvé.");
            }
        }

    }
}
