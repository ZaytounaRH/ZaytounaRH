package tn.esprit.models;
import java.sql.Date;
import java.sql.Time;
public class Presence {
    private int idPresence;
    private Date date;
    private Time heureArrive;
    private Time heureDepart;
    private Employee employee; // Lien avec l'employé

    // ✅ Constructeur par défaut
    public Presence() {}

    // ✅ Constructeur avec tous les paramètres
    public Presence(int idPresence, Date date, Time heureArrive, Time heureDepart, Employee employee) {
        this.idPresence = idPresence;
        this.date = date;
        this.heureArrive = heureArrive;
        this.heureDepart = heureDepart;
        this.employee = employee;
    }

    // ✅ GETTERS
    public int getIdPresence() {
        return idPresence;
    }

    public Date getDate() {
        return date;
    }

    public Time getHeureArrive() {
        return heureArrive;
    }

    public Time getHeureDepart() {
        return heureDepart;
    }

    public Employee getEmployee() {
        return employee;
    }

    // ✅ SETTERS
    public void setIdPresence(int idPresence) {
        this.idPresence = idPresence;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setHeureArrive(Time heureArrive) {
        this.heureArrive = heureArrive;
    }

    public void setHeureDepart(Time heureDepart) {
        this.heureDepart = heureDepart;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Presence{" +
                "idPresence=" + idPresence +
                ", date=" + date +
                ", heureArrive=" + heureArrive +
                ", heureDepart=" + heureDepart +
                ", employee=" + (employee != null ? employee.getNom() + " " + employee.getPrenom() : "Aucun") +
                '}';
    }



    public String getStatut() {
        Time heureNormale = Time.valueOf("08:00:00"); // Heure normale d'arrivée (08h00)
        if (heureArrive.after(heureNormale)) {
            return "Retard";
        }
        return "Présent";
    }
}


