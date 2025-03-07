package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.utils.SessionManager;
import tn.esprit.models.User;

import java.io.IOException;

public class MainFX extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage; // Stocke la référence pour navigation future
        testFXMLPath("/Login.fxml"); // ✅ Vérifie si le fichier Login.fxml est bien trouvé
        navigateToLogin();
    }

    // ✅ Méthode pour charger une nouvelle vue
    public static void loadFXML(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource(fxmlFile));
            if (loader.getLocation() == null) {
                throw new IOException("Fichier FXML introuvable : " + fxmlFile);
            }
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("❌ Erreur lors du chargement de : " + fxmlFile);
            e.printStackTrace();
        }
    }

    // ✅ Redirige vers la page de connexion
    public static void navigateToLogin() {
        SessionManager.getInstance().logout(); // Déconnexion de l'utilisateur
        loadFXML("/Login.fxml", "Login - Authentification");
    }

    // ✅ Redirige vers la gestion des congés selon le rôle de l'utilisateur
    public static void navigateAfterLogin() {
        User user = SessionManager.getInstance().getCurrentUser();

        if (user != null) {
            if ("RH".equals(user.getUserType())) {
                navigateToConges(); // RH accède à tous les congés
            } else if ("Employee".equals(user.getUserType())) {
                navigateToConges(); // Employé accède à ses congés
            } else {
                navigateToLogin(); // Sécurité : retour à login si rôle inconnu
            }
        } else {
            navigateToLogin(); // Sécurité : retour à login si pas d'utilisateur
        }
    }


    // ✅ Méthode pour rediriger vers la liste des congés (RH et Employés)
    public static void navigateToConges() {
        loadFXML("/ListeConges.fxml", "Liste des Congés");
    }

    // ✅ Méthode pour tester si le fichier FXML est bien trouvé
    public static void testFXMLPath(String path) {
        if (MainFX.class.getResource(path) == null) {
            System.out.println("❌ Le fichier FXML '" + path + "' n'est pas trouvé !");
        } else {
            System.out.println("✅ Le fichier FXML '" + path + "' est bien trouvé !");
        }
    }

}
