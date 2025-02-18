package tn.esprit.models;

import java.sql.Date;

public class Candidat extends User {
    private int intervieweurId; // ID of the interviewer RH

    public Candidat() {}

    public Candidat(int id, int numTel, String joursOuvrables, String nom, String prenom, String address, String email, String gender, String department, String designation, Date dateDeNaissance, int intervieweurId) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance);
        this.intervieweurId = intervieweurId;
    }

    public int getIntervieweurId() { return intervieweurId; }
    public void setIntervieweurId(int intervieweurId) { this.intervieweurId = intervieweurId; }
}