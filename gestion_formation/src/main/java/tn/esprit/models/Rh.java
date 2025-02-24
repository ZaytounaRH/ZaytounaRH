package tn.esprit.models;

public class Rh {
    private int rh_id;
    private String nom;

    public Rh() {
    }

    public Rh(int rh_id) {
        this.rh_id= rh_id;
    }

    public Rh(int rh_id, String nom) {
        this.rh_id = rh_id;
        this.nom = nom;
    }

    public int getRh_id() {
        return rh_id;
    }

    public void setRh_id(int rh_id) {
        this.rh_id = rh_id;
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
