package tn.esprit.models;

import java.sql.Date;

public class User {
    private int id;
    private int numTel;
    private int joursOuvrables;
    private String nom;
    private String prenom;
    private String address;
    private String email;
    private String gender;
    private String department;
    private String designation;
    private Date dateDeNaissance;

    public User() {}

    public User(int numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, String department, String designation, Date dateDeNaissance) {
        this.numTel = numTel;
        this.joursOuvrables = joursOuvrables;
        this.nom = nom;
        this.prenom = prenom;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.department = department;
        this.designation = designation;
        this.dateDeNaissance = dateDeNaissance;
    }

    public User(int id, int numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, String department, String designation, Date dateDeNaissance) {
        this.id = id;
        this.numTel = numTel;
        this.joursOuvrables = joursOuvrables;
        this.nom = nom;
        this.prenom = prenom;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.department = department;
        this.designation = designation;
        this.dateDeNaissance = dateDeNaissance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumTel() {
        return numTel;
    }

    public void setNumTel(int numTel) {
        this.numTel = numTel;
    }

    public int getJoursOuvrables() {
        return joursOuvrables;
    }

    public void setJoursOuvrables(int joursOuvrables) {
        this.joursOuvrables = joursOuvrables;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Date getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }
}
