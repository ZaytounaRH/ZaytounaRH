package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private static MyDatabase instance;
    private final String URL = "jdbc:mysql://127.0.0.1:3306/gestionformation";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private Connection cnx;

    // Constructeur privé pour éviter l'instanciation multiple
    private MyDatabase() {
        try {
            // Tentative de connexion à la base de données
            cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("connected ...");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    // Méthode pour récupérer l'instance unique de la classe MyDatabase
    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    // Méthode pour obtenir la connexion à la base de données
    public Connection getCnx() {
        try {
            // Vérification si la connexion est fermée ou invalide
            if (cnx == null || cnx.isClosed()) {
                cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD); // Réouverture de la connexion
                System.out.println("Connexion rétablie...");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la connexion : " + e.getMessage());
        }
        return cnx;
    }

    // Méthode pour fermer proprement la connexion
    public void closeConnection() {
        try {
            if (cnx != null && !cnx.isClosed()) {
                cnx.close();
                System.out.println("Connexion fermée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}
