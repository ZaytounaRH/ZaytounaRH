package tn.esprit.models;
import java.time.LocalDate;
import java.time.LocalTime;

import java.time.LocalDate;
import java.time.LocalTime;

import java.time.LocalDate;
import java.time.LocalTime;
public class Entretien {
    private int idEntretien;
    private LocalDate dateEntretien;
    private LocalTime heureEntretien;
    private TypeEntretien typeEntretien;
    private StatutEntretien statut ;  // Statut par défaut
    private String commentaire;
    private OffreEmploi offreEmploi; // Relation bidirectionnelle avec OffreEmploi

    private Candidat candidat;
    private RH RH;
    // Enum pour le type d'entretien
    public enum TypeEntretien {
        PRESENTIEL,
        DISTANCIEL,
        TELEPHONIQUE
    }

    // Enum pour le statut d'entretien
    public enum StatutEntretien {
        PLANIFIE,
        EN_COURS,
        TERMINE,
        ANNULE
    }

    // Getters et Setters
    public int getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(int idEntretien) {
        this.idEntretien = idEntretien;
    }

    public LocalDate getDateEntretien() {
        return dateEntretien;
    }

    public void setDateEntretien(LocalDate dateEntretien) {
        this.dateEntretien = dateEntretien;
    }

    public LocalTime getHeureEntretien() {
        return heureEntretien;
    }

    public void setHeureEntretien(LocalTime heureEntretien) {
        this.heureEntretien = heureEntretien;
    }

    public TypeEntretien getTypeEntretien() {
        return typeEntretien;
    }

    public void setTypeEntretien(TypeEntretien typeEntretien) {
        this.typeEntretien = typeEntretien;
    }

    public StatutEntretien getStatut() {
        return statut;
    }

    public void setStatut(StatutEntretien statut) {
        this.statut = statut;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public OffreEmploi getOffreEmploi() {
        return offreEmploi;
    }

    public void setOffreEmploi(OffreEmploi offreEmploi) {
        this.offreEmploi = offreEmploi;
    }

    public Candidat getCandidat() {
        return candidat;
    }
    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    @Override
    public String toString() {
        return "Entretien{" +
                "idEntretien=" + idEntretien +
                ", dateEntretien=" + dateEntretien +
                ", heureEntretien=" + heureEntretien +
                ", typeEntretien=" + typeEntretien +
                ", statut=" + statut +
                ", commentaire='" + commentaire + '\'' +
                ", Candidat=" + (candidat != null ? candidat.getNom() : "null") +
                '}';
    }
}
