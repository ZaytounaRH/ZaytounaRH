package tn.esprit.test;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import tn.esprit.services.FaceRecognitionService;
import tn.esprit.services.FaceDatabaseService;

import java.io.File;
import java.util.List;

public class TestOpenCV {

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");

        System.out.println("🔍 Vérification de la webcam...");

        String cascadePath = "src/main/resources/haarcascade_frontalface_alt.xml";
        CascadeClassifier faceDetector = new CascadeClassifier(cascadePath);

        if (faceDetector.empty()) {
            System.err.println("❌ Erreur : Impossible de charger le classificateur HaarCascade !");
            return;
        }

        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("❌ Erreur : Impossible d'accéder à la webcam !");
            return;
        }

        Mat frame = new Mat();
        Mat grayscale = new Mat();
        boolean faceDetected = false;
        String filePath = "src/main/resources/faces/captured_face.jpg";

        while (!faceDetected) {
            camera.read(frame);
            if (frame.empty()) continue;

            opencv_imgproc.cvtColor(frame, grayscale, opencv_imgproc.COLOR_BGR2GRAY);

            RectVector faces = new RectVector();
            faceDetector.detectMultiScale(grayscale, faces);

            if (faces.size() > 0) {
                System.out.println("✅ Visage détecté !");
                File facesDir = new File("src/main/resources/faces");
                if (!facesDir.exists()) {
                    facesDir.mkdirs();
                }

                opencv_imgcodecs.imwrite(filePath, grayscale);
                System.out.println("📸 Image enregistrée sous : " + filePath);

                faceDetected = true;
            }
        }

        camera.release();

        // 🚀 Charger les données de la base
        List<Mat> storedFaces = FaceDatabaseService.loadAllUserImagesFromDB();
        List<Integer> userIds = FaceDatabaseService.loadAllUserIds();
        List<String> userNames = FaceDatabaseService.loadAllUserNames();

        // ✅ Vérification des listes
        System.out.println("📝 Liste des ID : " + userIds);
        System.out.println("📝 Liste des noms : " + userNames);

        for (int i = 0; i < userIds.size(); i++) {
            System.out.println("👤 ID: " + userIds.get(i) + " → Nom: " + userNames.get(i));
        }

        if (storedFaces.isEmpty() || userIds.isEmpty()) {
            System.out.println("❌ Aucune image trouvée dans la base de données !");
            return;
        }

        Mat capturedFace = opencv_imgcodecs.imread(filePath, opencv_imgcodecs.IMREAD_GRAYSCALE);

        int matchedUserId = FaceRecognitionService.compareFaceWithDatabase(capturedFace, storedFaces, userIds);

        if (matchedUserId != -1) {
            int index = userIds.indexOf(matchedUserId);
            if (index >= 0 && index < userNames.size()) {
                String matchedUserName = userNames.get(index);
                System.out.println("✅ Authentification réussie pour : " + matchedUserName + " (ID: " + matchedUserId + ")");
            } else {
                System.out.println("❌ Erreur : ID trouvé, mais pas de nom associé !");
            }
        } else {
            System.out.println("⛔ Authentification échouée !");
        }
    }
}
