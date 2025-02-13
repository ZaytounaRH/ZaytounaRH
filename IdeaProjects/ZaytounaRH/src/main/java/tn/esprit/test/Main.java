package tn.esprit.test;

import tn.esprit.models.Reclamation;
import tn.esprit.models.Incident;
import tn.esprit.services.ServiceReclamation;
import tn.esprit.services.ServiceIncident;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        // Création des services
        ServiceReclamation serviceReclamation = new ServiceReclamation();
        ServiceIncident serviceIncident = new ServiceIncident();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            // 🔹 Ajout d'une réclamation
            Date dateReclamation = sdf.parse("12-02-2025");
            Reclamation reclamation = new Reclamation("Réclamation 1", "Description ici", dateReclamation,
                    Reclamation.StatutReclamation.EN_COURS,
                    Reclamation.PrioriteReclamation.FAIBLE,
                    "user/docs/reclamation.pdf");
            serviceReclamation.add(reclamation);

            // 🔹 Ajout d'un incident
            Date dateIncident = sdf.parse("10-02-2025");
            Incident incident = new Incident("Usine 1",
                    Incident.GraviteIncident.MOYEN,
                    dateIncident,
                    "Alerte envoyée au responsable");
            serviceIncident.add(incident);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 🔹 Affichage des réclamations
        System.out.println("📌 Liste des réclamations :");
        System.out.println(serviceReclamation.getAll());

        // 🔹 Affichage des incidents
        System.out.println("⚠️ Liste des incidents :");
        System.out.println(serviceIncident.getAll());
    }
}
