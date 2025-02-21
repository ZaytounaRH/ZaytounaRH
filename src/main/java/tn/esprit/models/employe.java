package tn.esprit.models;
import java.util.Date;


public class employe {
    private int idEmploye;
    private String nom;
    private String prenom;
    private int numTel;
    private String email;
    private Date dateDeNaissance;
    private String gender;
    private String address;
    private String department;
    private String designation;
    private int joursOuvrables;
    public employe() {}

    public employe(int idEmploye, String nom, String prenom, int numTel, String email, Date dateDeNaissance, String gender, String address, String department, String designation, int joursOuvrables) {
        this.idEmploye = idEmploye;
        this.nom = nom;
        this.prenom = prenom;
        this.numTel = numTel;
        this.email = email;
        this.dateDeNaissance = dateDeNaissance;
        this.gender = gender;
        this.address = address;
        this.department = department;
        this.designation = designation;
        this.joursOuvrables = joursOuvrables;
    }
    // Getters et Setters
    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
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
    public int getNumTel() {
        return numTel;
    }
    public void setNumTel(int numTel) {
        this.numTel = numTel;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getDateDeNaissance() {
        return dateDeNaissance;
    }
    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    public int getJoursOuvrables() {
        return joursOuvrables;
    }
    public void setJoursOuvrables(int joursOuvrables) {
        this.joursOuvrables = joursOuvrables;
    }
    @Override
    public String toString() {
        return nom + " " + prenom;  // Affiche uniquement le nom et prénom
    }

}




