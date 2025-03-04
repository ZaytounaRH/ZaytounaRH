package tn.esprit.utils;

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
        this.currentUser = user;
    }

    // Méthode pour déconnecter l'utilisateur
    public void logout() {
        this.currentUser = null;
    }

    // Retourne l'utilisateur actuellement connecté
    public User getCurrentUser() {
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

    // Méthode pour vérifier si un utilisateur est connecté
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }
}