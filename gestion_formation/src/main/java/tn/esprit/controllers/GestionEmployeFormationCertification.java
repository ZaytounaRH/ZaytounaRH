package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import tn.esprit.models.Certification;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployeCertification;
import tn.esprit.services.ServiceEmployeFormation;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.SessionManager;
import tn.esprit.services.ServiceCertification;
import tn.esprit.utils.MyDatabase;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class GestionEmployeFormationCertification {
    @FXML
    private VBox cardContainer;
    @FXML
    private Button btnAfficherCertifs;
    @FXML
    private Button btnAfficherFormations;
    private ServiceEmployeFormation serviceEmployeFormation;
    private ServiceEmployeCertification serviceEmployeCertification;

    public GestionEmployeFormationCertification() {
        serviceEmployeCertification = new ServiceEmployeCertification();
    serviceEmployeFormation = new ServiceEmployeFormation();
    }


    @FXML
    private void afficherCertifications() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser instanceof Employee) {
            int employeeId = ((Employee) currentUser).getIdEmployee();
            List<Object[]> certifications = serviceEmployeCertification.afficherCertificationsByCurrentUser(employeeId);

            if (certifications.isEmpty()) {
                Label noData = new Label("Aucune certification trouvée.");
                noData.setFont(new Font(16));
                cardContainer.getChildren().add(noData);
            } else {
                /*for (Certification certif : certifications) {
                    cardContainer.getChildren().add(creerCard(certif));
                }

                 */
                for (Object[] obj : certifications) {
                    Certification certif = (Certification) obj[0];
                    Date dateObtention = (Date) obj[1];
                    if (dateObtention != null) {
                        cardContainer.getChildren().add(creerCard(certif, dateObtention));
                    } else {
                        // Si la date est nulle, tu peux afficher un message alternatif
                        cardContainer.getChildren().add(creerCard(certif, null));
                    }
                }
            }
        } else {
            Label errorLabel = new Label("L'utilisateur connecté n'est pas un employé.");
            errorLabel.setFont(new Font(16));
            cardContainer.getChildren().add(errorLabel);
        }
    }
    private VBox creerCard(Certification certif, Date dateObtention) {
        VBox card = new VBox();
        card.setSpacing(5);
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-padding: 10; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);");
        card.setPrefWidth(300);

        Text titre = new Text(certif.getTitreCertif());
        titre.setFont(new Font(18));
        titre.setStyle("-fx-font-weight: bold;");

        Text organisme = new Text("Organisme: " + certif.getOrganismeCertif());
        organisme.setFont(new Font(14));
        /*
        Text dateText = new Text("Date d'obtention: " + dateObtention.toString());
        dateText.setFont(new Font(14));

         */

        String dateString = "Date d'obtention: N/A";
        if (dateObtention != null) {
            dateString = "Date d'obtention: " + dateObtention.toString();
        } else {
            System.out.println("Date d'obtention est null");
        }

        Text dateText = new Text(dateString);
        dateText.setFont(new Font(14));

        Button detailsButton = new Button("Voir Détails");
        detailsButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        detailsButton.setOnAction(e -> afficherDetails(certif));

        card.getChildren().addAll(titre, organisme,dateText,detailsButton);
        return card;
    }
    private void afficherDetails(Certification certif) {
        System.out.println("Détails de la certification : " + certif.getTitreCertif());
        // Tu peux ouvrir une nouvelle fenêtre ou afficher plus d'infos
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void afficherFormations() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser instanceof Employee) {
            int employeeId = ((Employee) currentUser).getIdEmployee();
            List<Object[]> formations = serviceEmployeFormation.afficherFormationsByEmployee(employeeId);

            // Vider le conteneur avant d'ajouter les nouvelles formations
            cardContainer.getChildren().clear();

            if (formations.isEmpty()) {
                Text noData = new Text("Aucune formation trouvée.");
                cardContainer.getChildren().add(noData);
            } else {
                // Ajouter les cartes de formations
                for (Object[] data : formations) {
                    Formation formation = (Formation) data[0];
                    Date dateParticipation = (Date) data[1];
                    if (dateParticipation != null) {
                        cardContainer.getChildren().add(creerCard(formation,dateParticipation));
                    }
                    else {
                        cardContainer.getChildren().add(creerCard(formation, null));
                    }
                }

            }
        } else {
            Text errorLabel = new Text("L'utilisateur connecté n'est pas un employé.");
            cardContainer.getChildren().add(errorLabel);
        }
    }
    private VBox creerCard(Formation formation,Date dateParticipation) {
        VBox card = new VBox();
        card.setSpacing(5);
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-padding: 10; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);");
        card.setPrefWidth(300);

        Text titre = new Text(formation.getNomFormation());
        titre.setStyle("-fx-font-weight: bold;");

        Text description = new Text("Description: " + formation.getDescriptionFormation());
        Text dates = new Text("De " +formation.getDateDebutFormation() + " à " + formation.getDateFinFormation());
        Text participation = new Text("Date de participation: " + dateParticipation);

        card.getChildren().addAll(titre, description, dates, participation);

        return card;
    }
/*
    private VBox creerCard(Formation formation) {
        VBox card = new VBox();
        card.setSpacing(5);
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-padding: 10; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);");
        card.setPrefWidth(300);

        Text titre = new Text(formation.getNomFormation());
        titre.setStyle("-fx-font-weight: bold;");

        Text description = new Text("Description: " + formation.getDescriptionFormation());
        Text dates = new Text("De " + formation.getDateDebutFormation() + " à " + formation.getDateFinFormation());

        card.getChildren().addAll(titre, description, dates);

        return card;
    }



 */


}
