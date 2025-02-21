package tn.esprit.test;

import tn.esprit.models.Congé;
import tn.esprit.models.présence;
import tn.esprit.services.ServiceCongé;
import tn.esprit.services.ServicePrésence;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServiceCongé spCongé = new ServiceCongé(); // Service pour gérer les congés
        ServicePrésence spPrésence = new ServicePrésence(); // Service pour gérer les présences
        Scanner scanner = new Scanner(System.in);

        // Format de date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm:ss");

        try {
            // Demande des informations pour la présence
            System.out.println("\nVeuillez entrer la date de la présence (format: yyyy-MM-dd) :");
            String datePrésenceInput = scanner.nextLine();
            Date datePrésence = sdf.parse(datePrésenceInput);

            System.out.println("Veuillez entrer l'heure d'arrivée de la présence (format: HH:mm:ss) :");
            String heureArrivéInput = scanner.nextLine();
            Date heureArrivé = sdfHeure.parse(heureArrivéInput);

            System.out.println("Veuillez entrer l'heure de départ de la présence (format: HH:mm:ss) :");
            String heureDepartInput = scanner.nextLine();
            Date heureDepart = sdfHeure.parse(heureDepartInput);

            System.out.println("Veuillez entrer le statut de la présence (ex: présent, absent) :");
            String statutInput = scanner.nextLine();

            System.out.println("Veuillez entrer l'ID de l'employé :");
            int idEmployeInput = Integer.parseInt(scanner.nextLine());

            System.out.println("Veuillez entrer l'ID du congé associé (si aucun, entrez 0) :");
            String input = scanner.nextLine().trim();

            Integer idCongé = null;

            if (!input.equals("0")) {
                try {
                    idCongé = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Entrée invalide pour l'ID du congé. Veuillez entrer un nombre valide ou 0.");
                    return;
                }
            }

            // Création de l'objet présence
            présence presence = new présence(datePrésence, heureArrivé, heureDepart, statutInput, idEmployeInput);

            // Association avec un congé si applicable
            if (idCongé != null) {
                Congé congé = new Congé();
                congé.setId_congé(idCongé);
                presence.setCongé(congé);
            }

            // Ajout de la présence en base de données
            spPrésence.add(presence);
            System.out.println("Présence ajoutée avec succès!");

            // Affichage des présences enregistrées
            afficherPrésencesAvecCongés(spPrésence);

        } catch (Exception e) {
            System.out.println("Erreur lors de la gestion des entrées : " + e.getMessage());
        } finally {
            scanner.close();
        }
    }


    // Fonction pour afficher toutes les présences, avec les employés et congés associés
    public static void afficherPrésencesAvecCongés(ServicePrésence spPrésence) {
        List<présence> listePrésences = spPrésence.getAll(); // Récupérer les présences depuis le service

        if (listePrésences.isEmpty()) {
            System.out.println("⚠️ Aucune présence enregistrée.");
            return;
        }

        System.out.println(" Liste des présences :");
        for (présence p : listePrésences) {
            System.out.println(p);

            // Vérifier si l'employé est bien associé
            if (p.getEmploye() != null) {
                System.out.println("  -> Employé associé : " + p.getEmploye().getNom() + " " + p.getEmploye().getPrenom());
            } else {
                System.out.println("  -> Employé associé :  Inconnu");
            }

            // Vérifier si un congé est associé
            if (p.getCongé() != null) {
                System.out.println("  -> Congé associé : " + p.getCongé());
            }
        }
    }
}
