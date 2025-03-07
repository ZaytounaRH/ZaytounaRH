package tn.esprit.test;

import tn.esprit.models.Conge;
import tn.esprit.models.Employee;
import tn.esprit.models.User;
import tn.esprit.services.ServiceConge;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;
import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*// 1️⃣ Connexion à la base de données
        Connection connection = MyDatabase.getInstance().getCnx();
        if (connection == null) {
            System.out.println("❌ Erreur : Connexion non établie !");
            return; // Arrêter l'exécution si la connexion échoue
        }
        System.out.println("✅ Connexion réussie à la base de données.");

        // 2️⃣ Création d'un employé et connexion (simulation)
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
        currentUser.setUserType("EMPLOYE");
        SessionManager.getInstance().setCurrentUser(currentUser);
        System.out.println("ℹ️ Utilisateur connecté : " + currentUser.getUserType());

        // 3️⃣ Création d'un congé pour l'employé
        CongeService serviceConge = new CongeService(connection);

        Conge conge = new Conge();
        conge.setDateDebut(new java.util.Date()); // Date actuelle
        conge.setDateFin(new java.util.Date());  // Ajuster la date de fin si nécessaire
        conge.setMotif("Maladie");
        conge.setStatut(Conge.Statut.EN_ATTENTE);
        conge.setEmployeeId(3); // Assurez-vous d'ajouter l'ID employé

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

        // 5️⃣ Simuler la connexion d'un utilisateur RH
        User rhUser = new User();
        rhUser.setId(1);
        rhUser.setUserType("RH");
        SessionManager.getInstance().setCurrentUser(rhUser);
        System.out.println("ℹ️ Utilisateur connecté en tant que RH.");

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
        } */
    }
}
