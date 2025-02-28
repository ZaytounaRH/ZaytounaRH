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


    public void setCurrentUser(User user) {
         this.currentUser= user;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}