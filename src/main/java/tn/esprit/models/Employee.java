package tn.esprit.models;

import java.sql.Date;

public class Employee extends User {
    private int responsableId; // ID of the responsible RH

    public Employee() {}

    public Employee(int id, int numTel, String joursOuvrables, String nom, String prenom, String address, String email, String gender, String department, String designation, Date dateDeNaissance, int responsableId) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance);
        this.responsableId = responsableId;
    }

    public int getResponsableId() { return responsableId; }
    public void setResponsableId(int responsableId) { this.responsableId = responsableId; }
}