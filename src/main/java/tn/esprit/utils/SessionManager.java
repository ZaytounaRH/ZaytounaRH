package tn.esprit.utils;

import tn.esprit.models.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }



/*
    public User getCurrentUser() {
        if (currentUser == null) {
            currentUser = new User(); // Initialise un utilisateur vide pour éviter NullPointerException
            currentUser.setAddress("Adresse par défaut"); // Exemple d'initialisation
        }
        return currentUser;
    }
    zidou choufou m3a chat chnowa intom tist7a9ou
*/


    public void setCurrentUser(User user) {
         this.currentUser= user;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}