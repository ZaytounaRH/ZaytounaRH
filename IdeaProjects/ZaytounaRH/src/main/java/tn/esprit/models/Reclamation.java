package tn.esprit.models;

import java.util.Date;

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

    private int idR ;
    private String titre, description  , pieceJointe ;
    private StatutReclamation statut;  // Utilisation de l'enum
    private PrioriteReclamation priorite;  // Utilisation de l'enum
    private Date dateSoumission ;


    public Reclamation() {
    }

    public Reclamation( String titre, String description, Date dateSoumission,
                        StatutReclamation statut, PrioriteReclamation priorite, String pieceJointe) {
        this.titre = titre;
        this.description = description;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.priorite = priorite;
        this.pieceJointe = pieceJointe;
    }


    public Reclamation(int idR, String titre, String description, Date dateSoumission,
                       StatutReclamation statut, PrioriteReclamation priorite, String pieceJointe) {
        this.idR = idR;
        this.titre = titre;
        this.description = description;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.priorite = priorite;
        this.pieceJointe = pieceJointe;
    }

    public int getIdR() {
        return idR;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getPieceJointe() {
        return pieceJointe;
    }

    public StatutReclamation getStatut() {
        return statut;
    }

    public PrioriteReclamation getPriorite() {
        return priorite;
    }

    public Date getDateSoumission() {
        return dateSoumission;
    }

    public void setIdR(int idR) {
        this.idR = idR;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public void setStatut(StatutReclamation statut) {
        this.statut = statut;
    }

    public void setPriorite(PrioriteReclamation priorite) {
        this.priorite = priorite;
    }

    public void setDateSoumission(Date dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "idR=" + idR +
                ", titre=" + titre +
                ", description='" + description + '\'' +
                ", dateSoumission='" + dateSoumission + '\'' +
                ", statut='" + statut + '\'' +
                ", priorite='" + priorite + '\'' +
                ", pieceJointe='" + pieceJointe + '\'' +
                "}\n";
    }
}

