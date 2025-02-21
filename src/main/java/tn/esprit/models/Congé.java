package tn.esprit.models;

import java.util.Date;

public class Congé {

    private int id_congé;
    private Date dateDebut;
    private Date dateFin;
    private Date dateDemande;
    private String motif;

    // Constructeur par défaut
    public Congé() {
    }

    // Constructeur avec tous les attributs
    public Congé(int id_congé, Date dateDebut, Date dateFin, Date dateDemande, String motif) {
        this.id_congé = id_congé;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dateDemande = dateDemande;
        this.motif = motif;
    }

    // Constructeur sans id_congé (pour l'insertion dans la base)
    public Congé(Date dateDebut, Date dateFin, Date dateDemande, String motif) {

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dateDemande = dateDemande;
        this.motif = motif;
    }

    // Getters et Setters
    public int getId_congé() {
        return id_congé;
    }

    public void setId_congé(int id_congé) {
        this.id_congé = id_congé;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }




    @Override
    public String toString() {
        return "Congé{" +
                "id_congé=" + id_congé +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", dateDemande=" + dateDemande +
                ", motif='" + motif + '\'' +
                '}';
    }
}
