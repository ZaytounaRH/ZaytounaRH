package tn.esprit.services;

import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Size;
import java.util.ArrayList;

import java.util.List;

public class FaceRecognitionService {

    // ✅ Comparaison de l'image capturée avec celles de la base
    public static int compareFaceWithDatabase(Mat capturedFace, List<Mat> storedFaces, List<Integer> userIds) {
        System.out.println("🔍 Comparaison de l'image capturée avec les images en base de données...");

        // 🔴 Vérification des données
        if (capturedFace.empty()) {
            System.out.println("❌ Erreur : L'image capturée est vide !");
            return -1;
        }
        if (storedFaces.isEmpty() || userIds.isEmpty()) {
            System.out.println("❌ Erreur : Aucune image stockée dans la base !");
            return -1;
        }

        // ✅ Normalisation : Redimensionner toutes les images à une taille fixe (100x100)
        int width = 100, height = 100;
        MatVector images = new MatVector(storedFaces.size());
        Mat labels = new Mat(storedFaces.size(), 1, org.bytedeco.opencv.global.opencv_core.CV_32SC1);

        for (int i = 0; i < storedFaces.size(); i++) {
            Mat resizedFace = new Mat();
            opencv_imgproc.resize(storedFaces.get(i), resizedFace, new Size(width, height));

            // 🔴 Vérification que l'image n'est pas vide avant de l'ajouter
            if (resizedFace.empty()) {
                System.out.println("⚠️ Image vide ignorée pour ID: " + userIds.get(i));
                continue;
            }

            images.put(i, resizedFace);
            labels.ptr(i, 0).putInt(userIds.get(i));
        }

        // ✅ Entraîner le modèle avec les images traitées
        LBPHFaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
        faceRecognizer.train(images, labels);

        // ✅ Redimensionner aussi l'image capturée pour correspondre
        Mat resizedCapturedFace = new Mat();
        opencv_imgproc.resize(capturedFace, resizedCapturedFace, new Size(width, height));

        // 🔍 Faire la prédiction
        IntPointer predictedLabel = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        faceRecognizer.predict(resizedCapturedFace, predictedLabel, confidence);

        System.out.println("🔎 Résultat de la prédiction : ID = " + predictedLabel.get() + " | Confiance = " + confidence.get());

        // ✅ Ajuster le seuil de confiance pour améliorer la précision
        double seuilConfiance = 80.0; // Ajustable entre 40-60 selon les résultats
        return (confidence.get() < seuilConfiance) ? predictedLabel.get() : -1;
    }
}
