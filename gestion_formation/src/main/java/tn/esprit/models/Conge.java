package tn.esprit.models;
import java.util.Date;
public class Conge {
    private int idConge;
    private Date dateDebut;
    private Date dateFin;
    private String motif;
    private Statut statut;
    private Employee employee;  // ✅ Utilisation cohérente de Employee
    private RH rh;  // ✅ Utilisation cohérente de RH

    public enum Statut {
        EN_ATTENTE,
        ACCEPTE,
        REFUSE
    }

    // ✅ Constructeur par défaut (nécessaire pour éviter les erreurs)
    public Conge() {}

    // ✅ Constructeur sans ID (cas où l'ID est généré automatiquement)
    public Conge(Date dateDebut, Date dateFin, String motif, Statut statut, Employee employee) {
        if (dateDebut != null && dateFin != null && dateFin.before(dateDebut)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être avant la date de début.");
        }
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.employee = employee;
    }

    // ✅ Constructeur avec ID
    public Conge(int idConge, Date dateDebut, Date dateFin, String motif, Statut statut, Employee employee) {
        this.idConge = idConge;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.employee = employee;
    }

    // ✅ Constructeur avec RH
    public Conge(int idConge, Date dateDebut, Date dateFin, String motif, Statut statut, Employee employee, RH rh) {
        this.idConge = idConge;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.employee = employee;
        this.rh = rh;
    }

    // ✅ Getters et Setters
    public int getIdConge() { return idConge; }
    public void setIdConge(int idConge) { this.idConge = idConge; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public RH getRh() { return rh; }
    public void setRh(RH rh) { this.rh = rh; }

    @Override
    public String toString() {
        String employeeInfo = (employee != null) ? (employee.getNom() + " " + employee.getPrenom()) : "Non attribué";
        String rhInfo = (rh != null) ? (rh.getNom() + " " + rh.getPrenom()) : "Aucun RH";
        return "Conge{" +
                "id=" + idConge +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", motif='" + motif + '\'' +
                ", statut='" + statut + '\'' +
                ", employé=" + employeeInfo +
                ", RH=" + rhInfo +
                '}';
    }
}

