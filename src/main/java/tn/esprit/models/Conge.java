package tn.esprit.models;

import java.util.Date;

public class Conge {

    private int idConge;
    private Date dateDebut;
    private Date dateFin;
    private String motif;
    private String statut;
    private User user;  // ✅ Remplacement de idEmploye par Employee
    private User rh;


    public Conge(Date dateDebut, Date dateFin, String motif, String statut, User user) {
        if (dateDebut != null && dateFin != null && dateFin.before(dateDebut)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être avant la date de début.");
        }
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.user = user;
    }


    public Conge(int idConge, Date dateDebut, Date dateFin, String motif, String statut, User user) {
        this.idConge = idConge;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.user = user;
    }

    public Conge(int idConge, Date dateDebut, Date dateFin, String motif, String statut, User user, User rh) {
        this.idConge = idConge;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.user = user;
        this.rh = rh; // Ajout du RH
    }



    // Getters et Setters
    public int getIdConge() {
        return idConge;
    }

    public void setIdConge(int idConge) {
        this.idConge = idConge;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        if (this.dateFin != null && dateDebut.after(this.dateFin)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin.");
        }
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        if (this.dateDebut != null && dateFin.before(this.dateDebut)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être avant la date de début.");
        }
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public User getUser() {  // ✅ Mise à jour du getter
        return user;
    }

    public void setUser(User user) {  // ✅ Mise à jour du setter
        this.user = user;
    }

    public User getRh() { return rh; }
    public void setRh(User rh) { this.rh = rh; }



    @Override
    public String toString() {
        String userInfo = (user != null) ? (user.getNom() + " " + user.getPrenom()) : "Non attribué";
        return "Conge{" +
                "id=" + idConge +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", motif='" + motif + '\'' +
                ", statut='" + statut + '\'' +
                ", utilisateur=" + userInfo +
                '}';
    }

}
