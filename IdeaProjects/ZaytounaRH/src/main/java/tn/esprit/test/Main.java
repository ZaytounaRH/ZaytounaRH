package tn.esprit.test;

import tn.esprit.models.Reclamation;
import tn.esprit.models.Reponse;
import tn.esprit.models.Assurance;
import tn.esprit.services.ServiceReclamation;
import tn.esprit.services.ServiceReponse;
import tn.esprit.services.ServiceAssurance;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        // Création des services
        ServiceReclamation serviceReclamation = new ServiceReclamation();
        ServiceReponse serviceReponse = new ServiceReponse();
        ServiceAssurance serviceAssurance = new ServiceAssurance();

        // Création et ajout d'une réclamation
        Reclamation reclamation = new Reclamation("Réclamation 1",
                "Description ici",
                LocalDate.of(2025, 2, 12),
                Reclamation.StatutReclamation.EN_COURS,
                Reclamation.PrioriteReclamation.FAIBLE,
                "user/docs/reclamation.pdf");
        serviceReclamation.add(reclamation);

        // Mise à jour d'une réclamation
        reclamation.setTitre("Réclamation mise à jour");
        serviceReclamation.update(reclamation);

        // Suppression d'une réclamation
        serviceReclamation.delete(reclamation);

        // Création et ajout d'une réponse
        Reponse reponse = new Reponse("Réponse à la réclamation", LocalDate.of(2025, 2, 13));
        serviceReponse.add(reponse);

        // Mise à jour d'une réponse
        reponse.setContenu("Réponse mise à jour");
        serviceReponse.update(reponse);

        // Suppression d'une réponse
        serviceReponse.delete(reponse);

        // Création et ajout d'une assurance
        Assurance assurance = new Assurance("Assurance Santé",
                Assurance.TypeAssurance.SANTE,
                LocalDate.of(2025, 12, 31));
        serviceAssurance.add(assurance);

        // Mise à jour d'une assurance
        assurance.setNom("Assurance Vie");
        serviceAssurance.update(assurance);

        // Suppression d'une assurance
        serviceAssurance.delete(assurance);

        // Affichage des réclamations après opérations
        System.out.println("Liste des réclamations après les opérations :");
        serviceReclamation.getAll().forEach(System.out::println);

        // Affichage des réponses après opérations
        System.out.println("Liste des réponses après les opérations :");
        serviceReponse.getAll().forEach(System.out::println);

        // Affichage des assurances après opérations
        System.out.println("Liste des assurances après les opérations :");
        serviceAssurance.getAll().forEach(System.out::println);
    }
}
