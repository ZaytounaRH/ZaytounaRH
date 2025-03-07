package tn.esprit.controllers;
import com.mysql.cj.x.protobuf.MysqlxResultset;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import tn.esprit.models.Conge;
import tn.esprit.models.User;
import tn.esprit.services.ServiceConge;
import tn.esprit.services.ServicePresence;
import tn.esprit.utils.SessionManager;
import java.io.IOException;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ListeConges {
    @FXML
    private VBox cardContainer;  // Conteneur des cartes

    @FXML
    private Button btnRetour;

    @FXML
    private Button btnAjouterConge;

    @FXML
    private Button btnExportExcel;


    private ServiceConge serviceConge;

    public ListeConges() {
        this.serviceConge = new ServiceConge();
    }

    @FXML
    public void initialize() {
        // Charger les congés
        loadConges();

        // Gestion du bouton retour
        btnRetour.setOnAction(event -> {
            System.out.println("Retour au menu principal");
        });

        // ✅ Gestion du bouton "Ajouter un congé"
        btnAjouterConge.setOnAction(event -> {
            System.out.println("Bouton Ajouter un congé cliqué !");
            ouvrirFormulaireAjoutConge();
        });

    }


    private void loadConges() {
        System.out.println("🔄 Chargement des congés...");

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.err.println("❌ ERREUR : Aucun utilisateur connecté !");
            return;
        }

        List<Conge> conges;
        if (SessionManager.getInstance().isUserType("RH")) {
            conges = serviceConge.getAll();
        } else {
            conges = serviceConge.getAllByEmployee(currentUser.getId());
        }

        System.out.println("✅ Nombre de congés récupérés : " + conges.size());

        if (conges.isEmpty()) {
            System.out.println("⚠️ Aucun congé trouvé !");
            return;
        }

        cardContainer.getChildren().clear();

        for (Conge conge : conges) {
            try {
                String fxmlPath = "/CardConge.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                HBox card = loader.load();

                System.out.println("✔️ Carte de congé chargée avec succès pour : " + conge.getMotif());

                CardConge controller = loader.getController();
                controller.setConge(conge);

                // 🔹 Ajout des boutons Modifier et Supprimer
                HBox buttonContainer = new HBox(10); // Espacement entre les boutons

                // ✅ Bouton Modifier (Seulement pour le RH)
                if (SessionManager.getInstance().isUserType("RH")) {
                    Button btnModifier = new Button("Modifier");
                    btnModifier.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                    btnModifier.setOnAction(event -> ouvrirFormulaireModificationConge(conge));

                    buttonContainer.getChildren().add(btnModifier);
                }

                // ✅ Bouton Supprimer (RH et Employé propriétaire)
                if (SessionManager.getInstance().isUserType("RH") || currentUser.getId() == conge.getEmployee().getIdEmployee()) {
                    Button btnSupprimer = new Button("Supprimer");
                    btnSupprimer.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                    btnSupprimer.setOnAction(event -> {
                        serviceConge.delete(conge.getIdConge());
                        loadConges(); // Recharger la liste après suppression
                    });

                    buttonContainer.getChildren().add(btnSupprimer);
                }

                // Ajouter les boutons à la carte
                card.getChildren().add(buttonContainer);

                cardContainer.getChildren().add(card);
            } catch (IOException e) {
                System.err.println("❌ Erreur lors du chargement de la carte : " + e.getMessage());
            }
        }
    }


    // ✅ Correction : Utilisation de AnchorPane au lieu de VBox
    private void ouvrirFormulaireAjoutConge() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutConge.fxml"));
            AnchorPane ajoutCongePane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un congé");
            stage.setScene(new Scene(ajoutCongePane));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'ouverture du formulaire d'ajout de congé : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void ouvrirFormulaireModificationConge(Conge conge) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierConge.fxml"));
            AnchorPane modifierCongePane = loader.load();  // ✅ Changer VBox en AnchorPane

            ModifierConge controller = loader.getController();
            controller.setConge(conge);

            Stage stage = new Stage();
            stage.setTitle("Modifier le congé");
            stage.setScene(new Scene(modifierCongePane)); // ✅ Utiliser AnchorPane
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'ouverture du formulaire de modification : " + e.getMessage());
        }
    }


}

