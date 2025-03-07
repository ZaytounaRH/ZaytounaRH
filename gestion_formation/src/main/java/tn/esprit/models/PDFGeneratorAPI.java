package tn.esprit.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.net.URLEncoder;

public class PDFGeneratorAPI {

   // private static final String API_URL = "https://api.pdfmonkey.io/api/v1/documents";  // 🔄 URL corrigée
    //private static final String API_KEY = "dgHZvLXsvsoHybtbBYghQ2HzamKqHnju";  // Remplace par ta clé API PDFMonkey
    //private static final String TEMPLATE_ID = "0321DFF1-B1E4-4EB3-88A3-8A3CEFF09D23"; // ✅ Ton ID de template ici

    private static final String API_KEY = "AW6jPq2PrNr4AfBE0BvpPQ2D7c4S7TFRYvluYu91OZklagq6Ajmvna3YnOpdTgOp"; // Remplace par ta clé
    private static final String API_URL = "https://api.html2pdf.app/v1/generate"; // API de génération de PDF

/*
    public static String genererPDF(String titre, String organisme, String dateObtention) {
        try {
            String apiKey = "AW6jPq2PrNr4AfBE0BvpPQ2D7c4S7TFRYvluYu91OZklagq6Ajmvna3YnOpdTgOp";
            URL url = new URL("https://api.html2pdf.app/v1/generate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // ✅ Construire le HTML du PDF
            String htmlContent = "<html><body>"
                    + "<h1>Certification : " + titre + "</h1>"
                    + "<p>Organisme : " + organisme + "</p>"
                    + "<p>Date d'obtention : " + dateObtention + "</p>"
                    + "</body></html>";

            // ✅ Construire le JSON de la requête
            String jsonInput = "{"
                    + "\"html\": \"" + htmlContent.replace("\"", "\\\"") + "\","
                    + "\"apiKey\": \"" + apiKey + "\""
                    + "}";

            System.out.println("📤 Payload envoyé : " + jsonInput);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // ✅ Lire la réponse
            int responseCode = connection.getResponseCode();
            System.out.println("🔍 Code de réponse HTTP : " + responseCode);

            if (responseCode == 200) {
                try (InputStream inputStream = connection.getInputStream();
                     Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {

                    String responseBody = scanner.useDelimiter("\\A").next();
                    System.out.println("📥 Réponse brute de l'API : " + responseBody);

                    // ✅ Vérifier si la réponse contient une URL valide
                    if (responseBody.startsWith("http")) {
                        System.out.println("✅ URL du PDF récupérée : " + responseBody);
                        return responseBody;  // ✅ Retourne directement l'URL du PDF
                    }

                    System.out.println("⚠️ La réponse ne contient pas d'URL valide !");
                    return null;
                }
            } else {
                System.out.println("⚠️ Erreur lors de la génération du PDF.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // ✅ Retourne `null` en cas d'erreur
    }


    private static String extractPdfUrl(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(jsonResponse);
            return responseJson.path("download_url").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String genererQRCode(String pdfUrl, String filename) {
        try {
            if (pdfUrl == null || pdfUrl.isEmpty()) {
                System.out.println("⚠️ L'URL du PDF est vide !");
                return null;
            }

            // ✅ Encoder l'URL pour éviter les caractères spéciaux
            String encodedPdfUrl = URLEncoder.encode(pdfUrl, StandardCharsets.UTF_8.toString());

            // ✅ Construire l'URL du QR Code
            String qrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + encodedPdfUrl;

            // ✅ Télécharger le QR Code
            BufferedImage image = ImageIO.read(new URL(qrCodeUrl));

            // ✅ Définir un dossier pour stocker les QR Codes
            File qrCodeDir = new File("qr_codes");
            if (!qrCodeDir.exists()) {
                qrCodeDir.mkdirs();
            }

            // ✅ Construire le chemin du fichier QR Code
            String filePath = "qr_codes/" + filename + ".png";
            File qrCodeFile = new File(filePath);

            // ✅ Sauvegarder l’image du QR Code
            ImageIO.write(image, "png", qrCodeFile);

            System.out.println("✅ QR Code généré avec succès : " + filePath);
            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Erreur lors de la génération du QR Code.");
            return null;
        }
    }
    public static void afficherQRCode(String qrCodePath) {
        Stage stage = new Stage();
        StackPane root = new StackPane();

        Image qrCodeImage = new Image("file:" + qrCodePath);
        ImageView imageView = new ImageView(qrCodeImage);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        root.getChildren().add(imageView);
        Scene scene = new Scene(root, 250, 250);
        stage.setTitle("QR Code - Certification");
        stage.setScene(scene);
        stage.show();
    }
/*
    public static void téléchargerPDF(String downloadUrl) {
        try {
            // Utiliser l'ID de tâche pour récupérer le PDF
            URL url = new URL(downloadUrl);  // Utilisation de l'URL correcte fournie par l'API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

            int responseCode = connection.getResponseCode();
            System.out.println("📥 Code de réponse du téléchargement : " + responseCode);

            String contentType = connection.getHeaderField("Content-Type");
            System.out.println("Content-Type: " + contentType);

            if (responseCode == 200 && "application/pdf".equalsIgnoreCase(contentType)) {
                // Enregistrer le fichier PDF
                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream fileOutputStream = new FileOutputStream("certification.pdf")) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("✅ PDF téléchargé avec succès : certification.pdf");
                }
            } else {
                // Afficher la réponse en cas d'erreur
                try (InputStream errorStream = connection.getErrorStream();
                     Scanner scanner = new Scanner(errorStream, StandardCharsets.UTF_8.name())) {
                    String responseBody = scanner.hasNext() ? scanner.useDelimiter("\\A").next() : "Réponse vide";
                    System.out.println("⚠️ Erreur lors du téléchargement du PDF. Détails : " + responseBody);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Nouvelle méthode pour récupérer la liste des documents et voir s'il est bien créé
    public static void listerDocuments() {
        try {
            URL url = new URL("https://api.pdfmonkey.io/api/v1/documents");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

            int responseCode = connection.getResponseCode();
            System.out.println("📥 Code de réponse du listing : " + responseCode);

            InputStream responseStream = (responseCode >= 200 && responseCode < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            if (responseStream != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(responseStream);
                System.out.println("📋 Liste des documents générés : " + responseJson.toPrettyString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String attendreGenerationPDF(String documentId) {
        try {
            URL url = new URL("https://api.pdfmonkey.io/api/v1/documents/" + documentId);
            int maxRetries = 20;  // Augmenter le nombre de tentatives
            int retryDelay = 8000; // Augmenter l'attente à 5 secondes entre chaque tentative

            for (int i = 0; i < maxRetries; i++) {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode responseJson = objectMapper.readTree(connection.getInputStream());

                    String status = responseJson.path("document").path("status").asText();
                    String downloadUrl = responseJson.path("document").path("download_url").asText();
                    String failureCause = responseJson.path("document").path("failure_cause").asText();

                    System.out.println("🔄 Statut du document : " + status);

                    // ✅ Affichage des logs de génération
                    JsonNode logs = responseJson.path("document").path("generation_logs");
                    if (!logs.isMissingNode() && logs.isArray()) {
                        System.out.println("📜 Logs de génération reçus : " + logs.toPrettyString());
                        for (JsonNode log : logs) {
                            System.out.println(log.path("timestamp").asText() + " - " + log.path("message").asText());
                        }
                    }

                    if (!failureCause.isEmpty()) {
                        System.out.println("❌ Erreur lors de la génération : " + failureCause);
                        return null;
                    }

                    if ("success".equals(status) && !downloadUrl.isEmpty()) {
                        System.out.println("✅ PDF prêt à être téléchargé : " + downloadUrl);
                        return downloadUrl;
                    }
                }

                // Attendre avant la prochaine tentative
                Thread.sleep(retryDelay);
            }

            System.out.println("❌ Le document n'a pas été généré après plusieurs tentatives.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

 */

}

/*
PDF SHIH 9BAL QRCODE
public static void genererPDF(String titre, String organisme, String dateObtention) {
    try {
        String apiKey = "TA_NOUVELLE_CLE_HTML2PDF";  // Remplace par ta clé HTML2PDF.app
        URL url = new URL("https://api.html2pdf.app/v1/generate");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);

        // ✅ Construire le HTML du PDF
        String htmlContent = "<html><body>"
                + "<h1>Certification : " + titre + "</h1>"
                + "<p>Organisme : " + organisme + "</p>"
                + "<p>Date d'obtention : " + dateObtention + "</p>"
                + "</body></html>";

        // ✅ Construire le JSON de la requête
        String jsonInput = "{"
                + "\"html\": \"" + htmlContent.replace("\"", "\\\"") + "\","
                + "\"apiKey\": \"" + apiKey + "\""
                + "}";

        System.out.println("📤 Payload envoyé : " + jsonInput);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // ✅ Vérifier la réponse
        int responseCode = connection.getResponseCode();
        System.out.println("🔍 Code de réponse HTTP : " + responseCode);

        if (responseCode == 200) {
            // ✅ Télécharger le PDF
            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream fileOutputStream = new FileOutputStream("certification.pdf")) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("✅ PDF téléchargé avec succès : certification.pdf");
            }
        } else {
            // Lire l'erreur détaillée
            try (InputStream errorStream = connection.getErrorStream();
                 Scanner scanner = new Scanner(errorStream, StandardCharsets.UTF_8.name())) {
                String responseBody = scanner.hasNext() ? scanner.useDelimiter("\\A").next() : "Réponse vide";
                System.out.println("⚠️ Erreur détaillée : " + responseBody);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

 */