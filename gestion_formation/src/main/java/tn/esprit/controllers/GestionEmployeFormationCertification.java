package tn.esprit.controllers;
import tn.esprit.models.PDFGeneratorAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.models.Certification;
import tn.esprit.models.Employee;
import tn.esprit.models.Formation;
import tn.esprit.models.User;
import tn.esprit.services.*;
import tn.esprit.utils.SessionManager;
import tn.esprit.utils.MyDatabase;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GestionEmployeFormationCertification {
    @FXML
    private VBox cardContainer;
    @FXML
    private Button btnAfficherCertifs;
    @FXML
    private Button btnAfficherFormations;
    private ServiceEmployeFormation serviceEmployeFormation;
    private ServiceEmployeCertification serviceEmployeCertification;
    private ServiceCertification serviceCertification;

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

        String dateString = (dateObtention != null) ? "Date d'obtention: " + dateObtention.toString() : "Date d'obtention: N/A";
        Text dateText = new Text(dateString);
        dateText.setFont(new Font(14));

        Button detailsButton = new Button("Voir Détails");
        detailsButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        detailsButton.setOnAction(e -> afficherDetails(certif));
/*
        Button pdfButton = new Button("Télécharger PDF");
        pdfButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        pdfButton.setOnAction(e -> {
            String dateStr = (dateObtention != null) ? dateObtention.toString() : "N/A";
            String pdfUrl = PDFGeneratorAPI.genererPDF(certif.getTitreCertif(), certif.getOrganismeCertif(), dateStr);

            if (pdfUrl != null && !pdfUrl.isEmpty()) {
                System.out.println("✅ Le fichier PDF a été généré avec succès.");
            } else {
                System.out.println("⚠️ Erreur lors de la génération du PDF.");
            }

        });
        Button qrCodeButton = new Button("QR Code");
        qrCodeButton.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black;");
        qrCodeButton.setOnAction(e -> {
            String dateStr = (dateObtention != null) ? dateObtention.toString() : "N/A";

            // ✅ Générer le PDF et récupérer son URL
            String pdfUrl = PDFGeneratorAPI.genererPDF(certif.getTitreCertif(), certif.getOrganismeCertif(), dateStr);

            // ✅ Vérifier si l'URL du PDF est correcte avant de générer le QR Code
            if (pdfUrl == null || pdfUrl.isEmpty()) {
                System.out.println("⚠️ Erreur : L'URL du PDF est invalide !");
                return;
            }

            System.out.println("✅ URL du PDF générée : " + pdfUrl);

            // ✅ Générer le QR Code
            String qrCodePath = PDFGeneratorAPI.genererQRCode(pdfUrl, certif.getTitreCertif().replaceAll("\\s+", "_"));

            if (qrCodePath != null) {
                PDFGeneratorAPI.afficherQRCode(qrCodePath);
            } else {
                System.out.println("⚠️ Erreur : Impossible de générer le QR Code.");
            }
        });




 */

        card.getChildren().addAll(titre, organisme, dateText, detailsButton);
        return card;
    }

    private void afficherQRCode(Certification certif) {
        // Générer l'ID de la certification
        //String qrData = "certifId=" + certif.getIdCertif();  // Ou génère une URL pour ton application.
        String cheminFichier = "C:/Users/allan/IdeaProjects/gestion_formation/src/main/resources/certification-details.html";

        String qrData = "file:///" + cheminFichier.replace("\\", "/");  // Remplace les antislashes par des slashes

        qrData += "?id=" + certif.getIdCertif();
        // Utiliser une API externe pour générer le QR Code
        String qrUrl = "https://quickchart.io/qr?text=" + URLEncoder.encode(qrData, StandardCharsets.UTF_8) + "&size=200";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(qrUrl))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                Path qrPath = Paths.get("certificat_" + certif.getIdCertif() + ".png");
                Files.write(qrPath, response.body());

                Image qrImage = new Image(qrPath.toUri().toString());
                ImageView qrImageView = new ImageView(qrImage);
                qrImageView.setFitWidth(200);
                qrImageView.setFitHeight(200);

                // Affichage dans une boîte de dialogue
                Stage qrStage = new Stage();
                VBox qrBox = new VBox(qrImageView);
                qrBox.setStyle("-fx-padding: 20; -fx-alignment: center;");

                Scene scene = new Scene(qrBox, 250, 250);
                qrStage.setTitle("QR Code de " + certif.getTitreCertif());
                qrStage.setScene(scene);
                qrStage.show();
            } else {
                System.out.println("Erreur lors de la génération du QR Code.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void gererScanQRCode(String qrData) {
        // Extraire l'ID de la certification à partir du QR Code
        String[] parts = qrData.split("=");
        if (parts.length == 2 && parts[0].equals("certifId")) {
            int certifId = Integer.parseInt(parts[1]);

            // Récupérer la certification via l'ID

            Certification certif = serviceCertification.getCertificationById(certifId);
            if (certif != null) {
                // Récupérer la date d'obtention de la certification
                Date dateObtention = serviceCertification.getDateObtention(certifId);

                // Génére un fichier PDF de la certification
                PdfService pdfService = new PdfService();
                File pdfFile = pdfService.genererCertificatPdf(certif,dateObtention);

                if (pdfFile != null) {
                    // Afficher un message de succès ou ouvrir le fichier PDF
                    System.out.println("Le PDF a été généré avec succès : " + pdfFile.getAbsolutePath());
                } else {
                    System.out.println("Erreur lors de la génération du PDF.");
                }
            } else {
                System.out.println("Certification non trouvée.");
            }
        }
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
