package tn.esprit.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import tn.esprit.models.Presence;
import tn.esprit.models.User;
import tn.esprit.services.ServicePresence;
import tn.esprit.utils.SessionManager;
import java.io.IOException;
import java.util.List;
public class ListePresence {
    @FXML
    private VBox cardContainer; // Conteneur des cartes

    @FXML
    private Button btnRetour;

    @FXML
    private Button btnAjouterPresence;

    private final ServicePresence servicePresence = new ServicePresence();

    @FXML
    public void initialize() {
        // Charger les présences
        loadPresences();

        // Vérifier si l'utilisateur peut ajouter une présence
        if (!SessionManager.getInstance().isUserType("RH") && !SessionManager.getInstance().isUserType("Employee")) {
            btnAjouterPresence.setVisible(false);
        }

        // Gestion du bouton "Retour"
        btnRetour.setOnAction(event -> {
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.close(); // Fermer la fenêtre actuelle
        });

        // Gestion du bouton "Ajouter une présence"
        btnAjouterPresence.setOnAction(event -> ouvrirFormulaireAjoutPresence());
    }

    private void loadPresences() {
        System.out.println("🔄 Chargement des présences...");

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.err.println("❌ ERREUR : Aucun utilisateur connecté !");
            return;
        }

        List<Presence> presences;

        // RH peut voir toutes les présences
        if (SessionManager.getInstance().isUserType("RH")) {
            presences = servicePresence.getAll();
        }
        // Employé ne voit que ses propres présences
        else {
            presences = servicePresence.getAllByEmployee(currentUser.getId());
        }

        System.out.println("✅ Nombre de présences récupérées : " + presences.size());

        if (presences.isEmpty()) {
            System.out.println("⚠️ Aucune présence trouvée !");
            return;
        }

        cardContainer.getChildren().clear();

        for (Presence presence : presences) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardPresence.fxml"));
                HBox card = loader.load();

                System.out.println("✔️ Carte de présence chargée avec succès pour la date : " + presence.getDate());

                CardPresence controller = loader.getController();
                controller.setPresence(presence);

                // ✅ Ajouter boutons Modifier et Supprimer pour le RH
                if (SessionManager.getInstance().isUserType("RH")) {
                    Button btnModifier = new Button("Modifier");
                    btnModifier.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                    btnModifier.setOnAction(event -> ouvrirFormulaireModificationPresence(presence));

                    Button btnSupprimer = new Button("Supprimer");
                    btnSupprimer.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                    btnSupprimer.setOnAction(event -> supprimerPresence(presence.getIdPresence()));

                    // Ajouter les boutons dans un `HBox` et s'assurer qu'il est bien placé
                    HBox buttonContainer = new HBox(10);
                    buttonContainer.getChildren().addAll(btnModifier, btnSupprimer);
                    card.getChildren().add(buttonContainer);
                }

                cardContainer.getChildren().add(card);
            } catch (IOException e) {
                System.err.println("❌ Erreur lors du chargement de la carte : " + e.getMessage());
            }
        }
    }

    // ✅ Ouvrir le formulaire d'ajout de présence
    private void ouvrirFormulaireAjoutPresence() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutPresence.fxml"));
            AnchorPane ajoutPresencePane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter une présence");
            stage.setScene(new Scene(ajoutPresencePane));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'ouverture du formulaire d'ajout de présence : " + e.getMessage());
        }
    }

    // ✅ Ouvrir le formulaire de modification d'une présence
    private void ouvrirFormulaireModificationPresence(Presence presence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPresence.fxml"));
            AnchorPane modificationPane = loader.load();

            ModifierPresence controller = loader.getController();
            controller.setPresence(presence);

            Stage stage = new Stage();
            stage.setTitle("Modifier la présence");
            stage.setScene(new Scene(modificationPane));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'ouverture du formulaire de modification de présence : " + e.getMessage());
        }
    }

    // ✅ Supprimer une présence
    private void supprimerPresence(int idPresence) {
        servicePresence.delete(idPresence);
        loadPresences(); // Recharger la liste après suppression
    }
}

