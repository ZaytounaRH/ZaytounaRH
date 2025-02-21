package tn.esprit.models;

import java.sql.Date;

public class Employee extends User {
    private int responsableId;
    private String responsableName;


    public Employee() {}

    public Employee(int numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, String department, String designation, Date dateDeNaissance, int responsableId, String responsableName) {
        super(numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance);
        this.responsableId = responsableId;
        this.responsableName = responsableName;
    }

    public Employee(int id, int numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, String department, String designation, Date dateDeNaissance, int responsableId, String responsableName) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance);
        this.responsableId = responsableId;
        this.responsableName = responsableName;
    }

    public int getResponsableId() { return responsableId; }
    public void setResponsableId(int responsableId) { this.responsableId = responsableId; }

    public String getResponsableName() { return responsableName; }
    public void setResponsableName(String responsableName) { this.responsableName = responsableName; }
}
