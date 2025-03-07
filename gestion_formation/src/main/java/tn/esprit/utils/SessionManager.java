package tn.esprit.utils;

import tn.esprit.models.Candidat;
import tn.esprit.models.Employee;
import tn.esprit.models.RH;
import tn.esprit.utils.MyDatabase;
import tn.esprit.models.User; // Assure-toi que l'import est correct

import java.sql.Connection;

public class SessionManager {

    private static SessionManager instance;
    private User currentUser;
private Connection connection;
    // Constructeur privé pour implémenter le singleton
    private SessionManager() {
        this.connection = MyDatabase.getInstance().getCnx();
    }

    // Retourne l'instance unique de SessionManager
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Méthode pour se connecter en tant qu'utilisateur
    public void login(User user) {
        System.out.println("Utilisateur connecté : " + user.getClass().getName());
        System.out.println("userType : " + user.getUserType());
        if (user instanceof RH) {
            this.currentUser = (RH) user;  // Cast de User à RH
            System.out.println("Utilisateur connecté de type RH");
        } else {
            this.currentUser = user;
            System.out.println("Utilisateur connecté de type User");
        }
        System.out.println("Type après connexion : " + (currentUser != null ? currentUser.getClass().getName() : "Aucun"));
    }

    // Méthode pour déconnecter l'utilisateur
    public void logout() {
        this.currentUser = null;
    }

    // Retourne l'utilisateur actuellement connecté
    public User getCurrentUser() {
        System.out.println("Type de l'utilisateur actuel : " + (currentUser != null ? currentUser.getClass().getName() : "Aucun"));

        return currentUser;
    }
    public Connection getConnection() {
        return connection;
    }


    // Méthode pour vérifier si l'utilisateur connecté est un RH
    public boolean isRH() {
        if (currentUser != null && currentUser.getUserType().equals("RH")) {
            return true;
        }
        return false;
    }
    public boolean isEmployee() {
        if (currentUser != null && currentUser.getUserType().equals("Employee")) {
            return true;
        }
        return false;
    }
    // Méthode pour vérifier si l'utilisateur connecté est un RH
    public boolean isCandidat() {
        if (currentUser != null && currentUser.getUserType().equals("Candidat")) {
            return true;
        }
        return false;
    }

    // Méthode pour vérifier si un utilisateur est connecté
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public int getCurrentRHId() {
        if (currentUser == null) {
            System.out.println("Aucun utilisateur connecté.");
            return -1;
        }
        if (currentUser instanceof RH) {
            RH rhUser = (RH) currentUser;
            int rhId = rhUser.getIdRH();
            if (rhId != 0) {
                return rhId;
            } else {
                System.out.println("L'ID RH est invalide.");
            }
        }
        System.out.println("L'utilisateur connecté n'est pas un RH.");
        return -1;
    }
public int getCurrentEmployeeId() {
        if (currentUser == null) {
            System.out.println("Aucun utilisateur connecté");
            return -1;
        }
        if (currentUser instanceof Employee) {
            Employee employee = (Employee) currentUser;
            int employeeId = employee.getIdEmployee();
            if (employeeId != 0) {
                return employeeId;

            }
            else {
                System.out.println("L'ID Employee est invalide.");
            }
        }
    System.out.println("L'utilisateur connecté n'est pas un employee");
        return -1;
}
    public int getCurrentCandidatId() {
        Candidat candidatuser = (Candidat) currentUser;
        int candidat_id = candidatuser.getCandidat_id();
        if (candidat_id != 0) {
            return candidat_id;
        } else {
            System.out.println("L'ID candidat est invalide.");
        }

        System.out.println("L'utilisateur connecté n'est pas un candidat.");
        return -1;

    }



}