package tn.esprit.models;
import java.util.Date;
import java.text.SimpleDateFormat;


public class présence {
    private int id_présence;
    private Date date;
    private Date heureArrivé;
    private Date heureDepart;
    private String statut;
    private int idEmploye;
    private Congé congé;  //
    private employe employe;
// Instance de Congé

    public présence(int id_présence, Date date, Date heureArrivé, Date heureDepart, String statut, int idEmploye) {
        this.id_présence = id_présence;
        this.date = date;
        this.heureArrivé = heureArrivé;
        this.heureDepart = heureDepart;
        this.statut = statut;
        this.idEmploye = idEmploye;
    }

    public présence() {
    }

    public présence(Date datePrésence, Date heureArrivé, Date heureDepart, String statut, employe employe) {
        this.date = datePrésence;
        this.heureArrivé = heureArrivé;
        this.heureDepart = heureDepart;
        this.statut = statut;
        this.employe = employe;
    }

    public présence(Date date, Date heureArrivé, Date heureDepart, String statut, int idEmploye) {
        this.date = date;
        this.heureArrivé = heureArrivé;
        this.heureDepart = heureDepart;
        this.statut = statut;
        this.idEmploye = idEmploye;
    }
    // Getters et Setters

    public Congé getCongé() {
        return congé;
    }

    public void setCongé(Congé congé) {
        this.congé = congé;
    }

    public int getId_présence() {
        return id_présence;
    }

    public void setId_présence(int id_présence) {
        this.id_présence = id_présence;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getHeureArrivé() {
        return heureArrivé;
    }

    public void setHeureArrivé(Date heureArrivé) {
        this.heureArrivé = heureArrivé;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }
    public employe getEmploye() {
        return employe;
    }

    public void setEmploye(employe employe) {
        this.employe = employe;
    }


    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String employeInfo = (employe != null) ? employe.getNom() + " " + employe.getPrenom() : "Inconnu";

        return "Présence{" +
                "id_présence=" + id_présence +
                ", date=" + sdf.format(date) +
                ", heureArrivé=" + sdf.format(heureArrivé) +
                ", heureDepart=" + sdf.format(heureDepart) +
                ", statut='" + statut + '\'' +
                ", Employé=" + (employe!= null ? employe.getNom():"null") +
                '}';
    }


}
