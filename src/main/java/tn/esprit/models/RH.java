package tn.esprit.models;

import java.sql.Date;

public class RH extends User {
    protected int idRH;

    public RH() {}

    public RH(String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, String image, int idRH) {
        super(numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password, image);
        this.idRH = idRH;
    }

    public RH(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, String image, int idRH) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password, image);
        this.idRH = idRH;
    }

    public RH(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, String image) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password, image);
    }

    public int getIdRH() {
        return idRH;
    }

    public void setIdRH(int idRH) {
        this.idRH = idRH;
    }

    @Override
    public String toString() {
        return "RH{" +
                "idRH=" + idRH +
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
