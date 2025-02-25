package tn.esprit.test;

import tn.esprit.models.Conge;
import tn.esprit.models.User;
import tn.esprit.services.ServiceConge;
import tn.esprit.services.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zaytouna", "root", "");
            UserService userService = new UserService(connection);
            ServiceConge congeService = new ServiceConge(connection);
            Scanner scanner = new Scanner(System.in);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // 🔹 Demander l'email de l'utilisateur
            System.out.print("🔹 Veuillez entrer votre email : ");
            String email = scanner.nextLine();

            User employe = userService.getUserByEmail(email);
            if (employe == null) {
                System.out.println("❌ Aucun utilisateur trouvé avec cet email !");
                return;
            }

            System.out.println("\n✅ Utilisateur trouvé : " + employe.getNom() + " " + employe.getPrenom() + " (ID: " + employe.getId() + ")\n");

            // 🔹 Récupération du RH associé
            User rh = userService.getRHByEmployeeId(employe.getId());
            if (rh != null) {
                System.out.println("👨‍💼 Responsable RH : " + rh.getNom() + " " + rh.getPrenom());
            } else {
                System.out.println("⚠️ Aucun responsable RH trouvé pour cet employé.");
            }

            // 🔹 Saisie des dates
            Date parsedDateDebut = null;
            Date parsedDateFin = null;

            while (parsedDateDebut == null) {
                System.out.print("📅 Entrez la date de début du congé (yyyy-MM-dd) : ");
                String dateDebut = scanner.nextLine();
                try {
                    parsedDateDebut = dateFormat.parse(dateDebut);
                } catch (ParseException e) {
                    System.out.println("❌ Format invalide. Veuillez entrer une date correcte !");
                }
            }

            while (parsedDateFin == null) {
                System.out.print("📅 Entrez la date de fin du congé (yyyy-MM-dd) : ");
                String dateFin = scanner.nextLine();
                try {
                    parsedDateFin = dateFormat.parse(dateFin);
                    if (parsedDateFin.before(parsedDateDebut)) {
                        System.out.println("⚠️ La date de fin ne peut pas être avant la date de début !");
                        parsedDateFin = null;
                    }
                } catch (ParseException e) {
                    System.out.println("❌ Format invalide. Veuillez entrer une date correcte !");
                }
            }

            // 🔹 Saisie du motif et du statut
            System.out.print("✍️  Entrez le motif du congé : ");
            String motif = scanner.nextLine();

            System.out.print("📌 Entrez le statut du congé (ex: 'En attente', 'Approuvé', 'Refusé') : ");
            String statut = scanner.nextLine();

            // 🔹 Création et ajout du congé
            Conge conge = new Conge(parsedDateDebut, parsedDateFin, motif, statut, employe);
            congeService.add(conge);
            System.out.println("\n✅ Congé ajouté avec succès !\n");

            // 🔹 Affichage des congés
            System.out.println("📜 Liste des congés : ");
            List<Conge> conges = congeService.getAll();
            for (Conge c : conges) {
                System.out.println("----------------------------------------------------");
                System.out.println("📅 Congé ID : " + c.getIdConge());
                System.out.println("👤 Employé : " + c.getUser().getNom() + " " + c.getUser().getPrenom());
                System.out.println("📅 Début : " + dateFormat.format(c.getDateDebut()));
                System.out.println("📅 Fin : " + dateFormat.format(c.getDateFin()));
                System.out.println("📝 Motif : " + c.getMotif());
                System.out.println("📌 Statut : " + c.getStatut());
                System.out.println("----------------------------------------------------");
            }

            // 🔹 Fermeture des ressources
            scanner.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
