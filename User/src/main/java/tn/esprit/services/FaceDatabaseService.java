package tn.esprit.services;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import tn.esprit.utils.MyDatabase;
import tn.esprit.services.FaceRecognitionService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Size;

public class FaceDatabaseService {

    // ✅ Charger une seule image d'un utilisateur spécifique
    public static Mat loadUserImageFromDB(int userId) {
        System.out.println("📥 Chargement de l'image utilisateur depuis la base de données pour ID: " + userId);

        Connection cnx = MyDatabase.getInstance().getCnx();
        String query = "SELECT image FROM users WHERE id = ?";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String imagePath = rs.getString("image");
                System.out.println("📷 Image trouvée : " + imagePath);
                return opencv_imgcodecs.imread(imagePath, opencv_imgcodecs.IMREAD_GRAYSCALE);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du chargement de l'image : " + e.getMessage());
        }

        return new Mat(); // Retourne une Mat vide en cas d'erreur
    }

    // ✅ Charger toutes les images des utilisateurs (avec ID associé)
    // Charger plusieurs images par utilisateur
    public static List<Mat> loadAllUserImagesFromDB() {
        System.out.println("📥 Chargement de plusieurs images par utilisateur...");

        List<Mat> images = new ArrayList<>();
        Connection cnx = MyDatabase.getInstance().getCnx();
        String query = "SELECT id, image FROM users ORDER BY id";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("id");
                String imagePath = rs.getString("image");

                // Charger l'image
                Mat image = opencv_imgcodecs.imread(imagePath, opencv_imgcodecs.IMREAD_GRAYSCALE);

                if (!image.empty()) {
                    // ✅ Appliquer le prétraitement
                    Mat processedImage = preprocessImage(image);
                    images.add(processedImage);
                    System.out.println("🆔 Image chargée pour ID: " + userId + " → " + imagePath);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du chargement des images : " + e.getMessage());
        }

        return images;
    }


    // ✅ Charger tous les ID des utilisateurs
    public static List<Integer> loadAllUserIds() {
        System.out.println("📥 Chargement des ID des utilisateurs...");

        List<Integer> userIds = new ArrayList<>();
        Connection cnx = MyDatabase.getInstance().getCnx();
        String query = "SELECT id FROM users ORDER BY id"; // ⚡ Assurer l'ordre

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                userIds.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du chargement des ID : " + e.getMessage());
        }

        return userIds;
    }

    // ✅ Charger tous les noms des utilisateurs
    public static List<String> loadAllUserNames() {
        System.out.println("📥 Chargement des noms des utilisateurs...");

        List<String> userNames = new ArrayList<>();
        Connection cnx = MyDatabase.getInstance().getCnx();
        String query = "SELECT nom FROM users ORDER BY id"; // ⚡ Utilisation de 'nom' au lieu de 'name'

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                userNames.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du chargement des noms : " + e.getMessage());
        }

        return userNames;
    }
    private static Mat preprocessImage(Mat image) {
        Mat resizedImage = new Mat();
        opencv_imgproc.resize(image, resizedImage, new Size(100, 100)); // 🟢 Uniformiser la taille

        Mat equalizedImage = new Mat();
        opencv_imgproc.equalizeHist(resizedImage, equalizedImage); // 🟢 Améliorer le contraste

        return equalizedImage;
    }
}
