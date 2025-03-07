package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private static MyDatabase instance;
    private final String URL = "jdbc:mysql://127.0.0.1:3306/rhzaytouna";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private Connection cnx;

    // ✅ Constructeur qui établit la connexion
    private MyDatabase() {
        try {
            cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connecté à la base de données.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion : " + e.getMessage());
        }
    }

    // ✅ Méthode Singleton : retourne l'instance unique
    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    // ✅ Vérifie si la connexion est encore valide avant de la retourner
    public Connection getCnx() {
        try {
            if (cnx == null || cnx.isClosed()) { // 🔥 Vérification
                cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("♻️ Nouvelle connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnx;
    }

    // ✅ Méthode pour fermer la connexion proprement
    public void closeConnection() {
        try {
            if (cnx != null && !cnx.isClosed()) {
                cnx.close();
                System.out.println("🛑 Connexion fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
