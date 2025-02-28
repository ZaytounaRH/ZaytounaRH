package tn.esprit.models;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;


public class Presence {
    private int idPresence;
    private java.sql.Date date;
    private Time heureArrivee;
    private Time heureDepart;
    private Employee employee; // Association avec la classe Employe
    private RH rh;  // ✅ Utilisation cohérente de RH
    private User user; // Peut être un Employee ou un RH


    // Constructeur par défaut
    public Presence() {}

    // Constructeur avec paramètres
    public Presence(int idPresence, java.sql.Date date, Time heureArrivee, Time heureDepart, Employee employee) {
        this.idPresence = idPresence;
        this.date = date;
        this.heureArrivee = heureArrivee;
        this.heureDepart = heureDepart;
        this.employee = employee;
    }

    // Getters et Setters
    public int getIdPresence() {
        return idPresence;
    }

    public void setIdPresence(int idPresence) {
        this.idPresence = idPresence;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(Time heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public Time getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Time heureDepart) {
        this.heureDepart = heureDepart;
    }

    public Employee getEmployee() {  // ✅ Correction du getter
        return employee;
    }

    public void setEmployee(Employee employee) {  // ✅ Correction du setter
        this.employee = employee;
    }

    public RH getRh() {
        return rh;
    }

    public void setRh(RH rh) {
        this.rh = rh;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    // Méthode toString pour afficher les informations
    @Override
    public String toString() {
        return "Presence{" +
                "idPresence=" + idPresence +
                ", date='" + date + '\'' +
                ", heureArrivee='" + heureArrivee + '\'' +
                ", heureDepart='" + heureDepart + '\'' +
                ", employee=" + (employee != null ? employee.getNom() + " " + employee.getPrenom() : "null") +
                '}';
    }


}
