package tn.esprit.models;

import java.sql.Date;

public class Candidat extends User {
    protected int candidat_id;
    protected String status;

    public Candidat() {}

    public Candidat(String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, int candidat_id, String status) {
        super(numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password);
        this.candidat_id = candidat_id;
        this.status = status;
    }

    public Candidat(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, int candidat_id, String status) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password);
        this.candidat_id = candidat_id;
        this.status = status;
    }

    public Candidat(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, String status) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password);
        this.status = status;
    }

    public int getCandidat_id() {
        return candidat_id;
    }

    public void setCandidat_id(int candidat_id) {
        this.candidat_id = candidat_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Candidat{" +
                "status='" + status + '\'' +
                ", id=" + id +
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