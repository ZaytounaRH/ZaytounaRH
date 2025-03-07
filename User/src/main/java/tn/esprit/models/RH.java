package tn.esprit.models;

import java.sql.Date;

public class RH extends User {
    private int idRH;
    public RH(){}

    public RH(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, int idRH) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password);
        this.idRH = idRH;
    }

    public RH(String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, int idRH) {
        super(numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password);
        this.idRH = idRH;
    }

    public RH(String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password) {
        super(numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password);
    }

    // Getters and Setters
    public int getIdRH() {
        return idRH;
    }

    public void setIdRH(int idRH) {
        this.idRH = idRH;
    }


}
