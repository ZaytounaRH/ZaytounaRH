package tn.esprit.models;

import java.sql.Date;

public class Admin extends User {

    public Admin() {}

    public Admin(int id, int numTel, String joursOuvrables, String nom, String prenom, String address, String email, String gender, String department, String designation, Date dateDeNaissance) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance);
    }
}