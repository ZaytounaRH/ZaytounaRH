package tn.esprit.services;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.global.opencv_imgproc;
import java.util.concurrent.TimeUnit;

public class FaceCaptureService {

    public static String captureFace() {
        System.out.println("📸 Capture du visage...");

        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("❌ Erreur : Impossible d'accéder à la webcam !");
            return null;
        }

        Mat frame = new Mat();
        Mat grayscale = new Mat();

        boolean faceDetected = false;
        CascadeClassifier faceDetector = new CascadeClassifier("src/main/resources/haarcascade_frontalface_alt.xml");

        // Détection du visage
        while (!faceDetected) {
            camera.read(frame);
            if (frame.empty()) continue;

            opencv_imgproc.cvtColor(frame, grayscale, opencv_imgproc.COLOR_BGR2GRAY);
            RectVector faces = new RectVector();
            faceDetector.detectMultiScale(grayscale, faces);

            if (faces.size() > 0) {
                System.out.println("✅ Visage détecté !");
                faceDetected = true;

                // Simulation de la récupération de l'email après la détection du visage (à implémenter)
                String userEmail = getUserEmailFromFace(faces); // Fonction fictive pour l'exemple
                return userEmail; // Retourne l'email ou l'ID de l'utilisateur
            }
        }

        camera.release();
        return null;
    }

    // Exemple fictif pour récupérer l'email de l'utilisateur après la détection du visage
    private static String getUserEmailFromFace(RectVector faces) {
        // Logique de correspondance du visage à un utilisateur (par exemple via une base de données)
        return "test@example.com"; // Retourne un email fictif pour l'exemple
    }
}
