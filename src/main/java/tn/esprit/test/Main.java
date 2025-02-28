package tn.esprit.test;

import tn.esprit.models.Conge;
import tn.esprit.models.Employee;
import tn.esprit.models.User;
import tn.esprit.services.ServiceConge;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;
import tn.esprit.models.Presence;
import tn.esprit.services.PresenceService;
import java.sql.Time;
import java.sql.Date;
import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1️⃣ Connexion à la base de données
        Connection connection = MyDatabase.getInstance().getCnx();
        if (connection == null) {
            System.out.println("❌ Erreur : Connexion non établie !");
        } else {
            System.out.println("✅ Connexion réussie !");
        }

        System.out.println("✅ Connexion établie avec la base de données.");

        // 2️⃣ Création d'un employé et connexion (simuler un employé)
        Employee employe = new Employee();
        employe.setIdEmployee(3);  // Assurez-vous que cet ID existe dans la table employee !
        employe.setNom("Dupont");
        employe.setPrenom("Jean");
        employe.setUserType("EMPLOYE");
        employe.setEmail("jean.dupont@example.com");
        employe.setNumTel("123456789");

        // Connexion de l'employé via le SessionManager
        User currentUser = new User();
        currentUser.setId(3);
        currentUser.setUserType("Employee");
        SessionManager.getInstance().setCurrentUser(currentUser);
        System.out.println("ℹ️ Utilisateur connecté : " + currentUser.getUserType());

        // 3️⃣ Création d'un congé pour l'employé
        ServiceConge serviceConge = new ServiceConge(connection);

        Conge conge = new Conge();
        conge.setDateDebut(new java.util.Date()); // Date actuelle
        conge.setDateFin(new java.util.Date());  // Vous pouvez ajuster la date de fin
        conge.setMotif("Maladie");
        conge.setStatut(Conge.statut.EN_ATTENTE);

        serviceConge.add(conge);

        // 4️⃣ Récupération et affichage des congés de l'employé connecté
        List<Conge> conges = serviceConge.getAll();
        if (conges.isEmpty()) {
            System.out.println("❌ Aucun congé trouvé.");
        } else {
            System.out.println("✅ Liste des congés de l'employé connecté :");
            for (Conge c : conges) {
                System.out.println("ℹ️ Congé ID: " + c.getIdConge() +
                        ", Date début: " + c.getDateDebut() +
                        ", Date fin: " + c.getDateFin() +
                        ", Motif: " + c.getMotif() +
                        ", Statut: " + c.getStatut());
            }
        }

        // 5️⃣ Mettre à jour un congé (pour un RH uniquement)
        // Simuler un utilisateur RH
        User rhUser = new User();
        rhUser.setId(1);
        rhUser.setUserType("RH");
        SessionManager.getInstance().setCurrentUser(rhUser);

        // 6️⃣ Récupération et affichage des congés pour l'utilisateur RH
        System.out.println("✅ Liste des congés pour le RH :");
        List<Conge> allCongeForRH = serviceConge.getAll(); // RH peut voir tous les congés
        if (allCongeForRH.isEmpty()) {
            System.out.println("❌ Aucun congé trouvé.");
        } else {
            for (Conge c : allCongeForRH) {
                System.out.println("ℹ️ Congé ID: " + c.getIdConge() +
                        ", Date début: " + c.getDateDebut() +
                        ", Date fin: " + c.getDateFin() +
                        ", Motif: " + c.getMotif() +
                        ", Statut: " + c.getStatut());
            }
        }

        // 5️⃣ Mettre à jour un congé (pour un RH uniquement)
        Conge congeToUpdate = serviceConge.getById(37);  // Utiliser un ID existant
        if (congeToUpdate != null) {
            // Modifier tous les champs
            congeToUpdate.setDateDebut(new java.util.Date());  // Exemple de nouvelle date de début
            congeToUpdate.setDateFin(new java.util.Date());    // Exemple de nouvelle date de fin
            congeToUpdate.setMotif("Maladie prolongée");       // Modifier le motif
            congeToUpdate.setStatut(Conge.statut.ACCEPTE);
            // Mettre à jour le congé dans la base de données
            // Mettre à jour le congé dans la base de données
            serviceConge.update(congeToUpdate);
            System.out.println("✅ Le congé ID " + congeToUpdate.getIdConge() + " a été mis à jour avec succès.");
        } else {
            System.out.println("❌ Aucun congé trouvé à mettre à jour.");
        }

        // 7️⃣ Suppression d'un congé (seul un RH peut le faire)
        int idCongeASupprimer = 33; // Remplace par un ID de congé existant

// Vérifier l'utilisateur connecté
        currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && "RH".equalsIgnoreCase(currentUser.getUserType())) {
            System.out.println("ℹ️ RH connecté : Suppression du congé en cours...");

            // Vérifier si le congé existe avant de supprimer
            Conge congeASupprimer = serviceConge.getById(idCongeASupprimer);
            if (congeASupprimer != null) {
                serviceConge.remove(idCongeASupprimer);
                System.out.println("✅ Congé ID " + idCongeASupprimer + " supprimé avec succès !");
            } else {
                System.out.println("❌ Erreur : Aucun congé trouvé avec l'ID " + idCongeASupprimer);
            }
        } else {
            System.out.println("❌ Erreur : Seul un RH peut supprimer un congé !");
        }


        // 4️⃣ Création du service de présence
        PresenceService servicePresence = new PresenceService(connection);

// 🔍 Récupérer l'utilisateur connecté depuis SessionManager
        currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null && ("EMPLOYE".equalsIgnoreCase(currentUser.getUserType()) || "RH".equalsIgnoreCase(currentUser.getUserType()))) {
            // 🔍 Afficher les informations de l'utilisateur connecté
            System.out.println("🔍 Utilisateur connecté : " + currentUser.getUserType());

            // 🔽 Création et initialisation de la présence
            Presence presence = new Presence();
            presence.setUser(currentUser); // ✅ Associer l'utilisateur connecté sans forcer le type
            presence.setDate(new Date(System.currentTimeMillis()));
            presence.setHeureArrivee(Time.valueOf("08:30:00"));
            presence.setHeureDepart(Time.valueOf("18:00:00"));

            // ✅ Ajouter la présence via le service
            servicePresence.add(presence);

        }
}
    }

