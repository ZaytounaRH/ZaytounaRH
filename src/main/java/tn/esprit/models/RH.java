package tn.esprit.models;

import java.sql.Date;
import java.util.List;

public class RH extends User {
    private List<Integer> employeeList; // List of Employee IDs
    private List<Integer> candidatList; // List of Candidat IDs

    public RH() {}

    public RH(int id, int numTel, String joursOuvrables, String nom, String prenom, String address, String email, String gender, String department, String designation, Date dateDeNaissance, List<Integer> employeeList, List<Integer> candidatList) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance);
        this.employeeList = employeeList;
        this.candidatList = candidatList;
    }

    public List<Integer> getEmployeeList() { return employeeList; }
    public void setEmployeeList(List<Integer> employeeList) { this.employeeList = employeeList; }
    public List<Integer> getCandidatList() { return candidatList; }
    public void setCandidatList(List<Integer> candidatList) { this.candidatList = candidatList; }
}