package tn.esprit.models;

import java.sql.Date;

public class Candidat extends User {
    protected int idCandidat;
    protected String status;

    public Candidat() {}

    public Candidat(String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, String image, int idCandidat, String status) {
        super(numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password, image);
        this.idCandidat = idCandidat;
        this.status = status;
    }

    public Candidat(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, String image, int idCandidat, String status) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password, image);
        this.idCandidat = idCandidat;
        this.status = status;
    }

    public Candidat(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, String image, String status) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password, image);
        this.status = status;
    }

    public int getIdCandidat() {
        return idCandidat;
    }

    public void setIdCandidat(int idCandidat) {
        this.idCandidat = idCandidat;
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
                "idCandidat=" + idCandidat +
                ", status='" + status + '\'' +
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
                ", image='" + image + '\'' +
                '}';
    }
}
