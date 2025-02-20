package tn.esprit.models;

import java.sql.Date;

public class RH extends User {
    private String employeeListJson;
    private String candidatListJson;

    public RH() {}

    public RH(int id, int numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, String department, String designation, Date dateDeNaissance, String employeeListJson, String candidatListJson) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, department, designation, dateDeNaissance);
        this.employeeListJson = employeeListJson;
        this.candidatListJson = candidatListJson;
    }

    public String getEmployeeListJson() {
        return employeeListJson;
    }

    public void setEmployeeListJson(String employeeListJson) {
        this.employeeListJson = employeeListJson;
    }

    public String getCandidatListJson() {
        return candidatListJson;
    }

    public void setCandidatListJson(String candidatListJson) {
        this.candidatListJson = candidatListJson;
    }
}
