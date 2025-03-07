package tn.esprit.models;
import java.util.Date;

import java.util.List;

public class OffreEmploi {
    private int idOffre;
    private String titreOffre;
    private String description;
    private java.util.Date datePublication;
    private double salaire;
    private StatutOffre statut;  // Utilisation de l'énum pour le statut
    private RH rh;  // Association avec un RH
    private List<Entretien> entretiens;  // Liste d'entretiens associés

    // Enum pour le statut des offres d'emploi
    public enum StatutOffre {
        OUVERTE,
        FERMEE,
        POURVUE,
        ANNULEE,
        ENCOURS
    }
    // OffreEmploi.setRh(RH);  // Associez le RH à l'offre d'emploi

    // Getters et Setters
    public int getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
    }

    public String getTitreOffre() {
        return titreOffre;
    }

    public void setTitreOffre(String titreOffre) {
        this.titreOffre = titreOffre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(java.util.Date datePublication) {
        this.datePublication = datePublication;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public StatutOffre getStatut() {
        return statut;
    }

    public void setStatut(StatutOffre statut) {
        this.statut = statut;
    }

    public RH getRh() {
        return rh;
    }


    public void setRh(RH rh) {
        this.rh = rh;
    }

    public List<Entretien> getEntretiens() {
        return entretiens;
    }

    public void setEntretiens(List<Entretien> entretiens) {
        this.entretiens = entretiens;
    }
    @Override
    public String toString() {
        return "OffreEmploi{" +
                "titreOffre='" + titreOffre + '\'' +
                ", description='" + description + '\'' +
                ", datePublication=" + datePublication +
                ", salaire=" + salaire +
                '}';
    }

}
/*
@Override
public String toString() {
    return "OffreEmploi{" +
            "idOffre=" + idOffre +
            ", titreOffre='" + titreOffre + '\'' +
            ", description='" + description + '\'' +
            ", datePublication=" + datePublication +
            ", salaire=" + salaire +
            ", statut=" + statut +
            ", rh=" + (rh != null ? rh.getNom() : "null") +
            ", entretiens=" + entretiens +
            '}';
}
*/
