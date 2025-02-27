package tn.esprit.models;

import java.sql.Date;
import java.time.LocalDate;

public class Reponse {
    private int idRep;
    private String contenu;
    private LocalDate dateRep;  // Utilisation de LocalDate pour la gestion de la date
    private Reclamation reclamation;  // Ajouter une réclamation


    public Reponse() {
    }

    public Reponse(String contenu, LocalDate dateRep) {
        this.contenu = contenu;
        this.dateRep = dateRep;
    }

    public Reponse(int idRep, String contenu, LocalDate dateRep) {
        this.idRep = idRep;
        this.contenu = contenu;
        this.dateRep = dateRep;
    }

    public int getIdRep() {
        return idRep;
    }

    public void setIdRep(int idRep) {
        this.idRep = idRep;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDate getDateRep() {
        return dateRep;
    }

    public void setDateRep(LocalDate dateRep) {
        this.dateRep = dateRep;
    }

    /*public Reponse(int idRep, String contenu, LocalDate dateRep) {
        this.idRep = idRep;
        this.contenu = contenu;
        this.dateRep = dateRep;
    }*/

    // Nouveau constructeur avec Reclamation
    public Reponse(Reclamation reclamation, String contenu) {
        this.reclamation = reclamation;
        this.contenu = contenu;
        this.dateRep = LocalDate.now(); // Par défaut, la date est aujourd'hui
    }

    // Getters et Setters
    public Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "idRep=" + idRep +
                ", contenu='" + contenu + '\'' +
                ", dateRep=" + dateRep +
                '}';
    }
}


