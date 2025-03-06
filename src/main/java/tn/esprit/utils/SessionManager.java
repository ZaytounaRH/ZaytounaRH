package tn.esprit.utils;

import tn.esprit.models.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private String verificationCode; // Add this field to store the verification code


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
        this.verificationCode = null; // Clear the verification code on logout

    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // Methods to set and get the verification code
    public void setVerificationCode(String code) {
        this.verificationCode = code;
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
