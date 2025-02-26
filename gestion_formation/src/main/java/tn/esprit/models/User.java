package tn.esprit.models;

import java.sql.Date;

public class User {
    protected int id;
    protected String numTel;
    protected int joursOuvrables;
    protected String nom;
    protected String prenom;
    protected String address;
    protected String email;
    protected String gender;
    protected Date dateDeNaissance;
    protected String userType;
    protected String password;

    public User() {}

    public User(String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password) {
        this.numTel = numTel;
        this.joursOuvrables = joursOuvrables;
        this.nom = nom;
        this.prenom = prenom;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.dateDeNaissance = dateDeNaissance;
        this.userType = userType;
        this.password = password;
    }

    public User(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password) {
        this.id = id;
        this.numTel = numTel;
        this.joursOuvrables = joursOuvrables;
        this.nom = nom;
        this.prenom = prenom;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.dateDeNaissance = dateDeNaissance;
        this.userType = userType;
        this.password = password;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
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

    public Date getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", numTel='" + numTel + '\'' +
                ", joursOuvrables=" + joursOuvrables +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateDeNaissance=" + dateDeNaissance +
                ", userType='" + userType + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
