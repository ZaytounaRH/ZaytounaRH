package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private static MyDatabase instance;
    private Connection cnx;

    private MyDatabase() {
        try {
            // Assurer que le driver est chargé
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connexion à la base de données
            String url = "jdbc:mysql://127.0.0.1:3306/projet";
            String username = "root";
            String password = "";

            cnx = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion réussie à la base de données !");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL introuvable !");
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    public static synchronized MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
