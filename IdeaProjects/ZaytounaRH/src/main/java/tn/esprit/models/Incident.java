package tn.esprit.models;

import java.util.Date;

public class Incident {
    public enum GraviteIncident {
        LEGER,
        MOYEN,
        GRAVE
    }

    private int idI;
    private String lieu;
    private GraviteIncident gravite;
    private Date dateIncident;
    private String actionsEffectuees;

    // Constructeur par défaut
    public Incident() {
    }

    // Constructeur sans id (utile pour les insertions)
    public Incident(String lieu, GraviteIncident gravite, Date dateIncident, String actionsEffectuees) {
        this.lieu = lieu;
        this.gravite = gravite;
        this.dateIncident = dateIncident;
        this.actionsEffectuees = actionsEffectuees;
    }

    // Constructeur avec id (utile pour les requêtes)
    public Incident(int idI, String lieu, GraviteIncident gravite, Date dateIncident, String actionsEffectuees) {
        this.idI = idI;
        this.lieu = lieu;
        this.gravite = gravite;
        this.dateIncident = dateIncident;
        this.actionsEffectuees = actionsEffectuees;
    }

    // Getters
    public int getIdI() {
        return idI;
    }

    public String getLieu() {
        return lieu;
    }

    public GraviteIncident getGravite() {
        return gravite;
    }

    public Date getDateIncident() {
        return dateIncident;
    }

    public String getActionsEffectuees() {
        return actionsEffectuees;
    }

    // Setters
    public void setIdI(int idI) {
        this.idI = idI;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setGravite(GraviteIncident gravite) {
        this.gravite = gravite;
    }

    public void setDateIncident(Date dateIncident) {
        this.dateIncident = dateIncident;
    }

    public void setActionsEffectuees(String actionsEffectuees) {
        this.actionsEffectuees = actionsEffectuees;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "idI=" + idI +
                ", lieu='" + lieu + '\'' +
                ", gravite=" + gravite +
                ", dateIncident=" + dateIncident +
                ", actionsEffectuees='" + actionsEffectuees + '\'' +
                "}\n";
    }
}


