package tn.esprit.models;

public class Rh {
    private int idRh;
    private String nom;

    public Rh() {
    }

    public Rh(int idRh) {
        this.idRh = idRh;
    }

    public Rh(int idRh, String nom) {
        this.idRh = idRh;
        this.nom = nom;
    }


    public int getIdRh() {
        return idRh;
    }

    public void setIdRh(int idRh) {
        this.idRh = idRh;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    @Override
    public String toString() {
        return nom;
    }
}
