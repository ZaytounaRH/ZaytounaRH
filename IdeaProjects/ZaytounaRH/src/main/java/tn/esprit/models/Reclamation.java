package tn.esprit.models;

import java.sql.Date;
import java.time.LocalDate;

public class Reclamation {

    public enum StatutReclamation {
        EN_ATTENTE,
        EN_COURS,
        RESOLU
    }

    public enum PrioriteReclamation {
        FAIBLE,
        MOYENNE,
        ELEVEE
    }

    private int idR;
    private String titre;
    private String description;
    private String pieceJointe;
    private StatutReclamation statut;
    private PrioriteReclamation priorite;
    private LocalDate dateSoumission; // Utilisation de LocalDate pour une gestion plus précise des dates.

    // Constructeur par défaut
    public Reclamation() {
    }

    // Constructeur avec tous les attributs
    public Reclamation(String titre, String description, LocalDate dateSoumission,
                       StatutReclamation statut, PrioriteReclamation priorite, String pieceJointe) {
        this.titre = titre;
        this.description = description;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.priorite = priorite;
        this.pieceJointe = pieceJointe;
    }

    // Constructeur avec id
    public Reclamation(int idR, String titre, String description, LocalDate dateSoumission,
                       StatutReclamation statut, PrioriteReclamation priorite, String pieceJointe) {
        this.idR = idR;
        this.titre = titre;
        this.description = description;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.priorite = priorite;
        this.pieceJointe = pieceJointe;
    }

    // Getters et Setters
    public int getIdR() {
        return idR;
    }

    public void setIdR(int idR) {
        this.idR = idR;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPieceJointe() {
        return pieceJointe;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public StatutReclamation getStatut() {
        return statut;
    }

    public void setStatut(StatutReclamation statut) {
        this.statut = statut;
    }

    public PrioriteReclamation getPriorite() {
        return priorite;
    }

    public void setPriorite(PrioriteReclamation priorite) {
        this.priorite = priorite;
    }

    public LocalDate getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDate dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "idR=" + idR +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateSoumission=" + dateSoumission +
                ", statut=" + statut +
                ", priorite=" + priorite +
                ", pieceJointe='" + pieceJointe + '\'' +
                '}';
    }
}
