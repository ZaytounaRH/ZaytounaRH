package tn.esprit.models;

public class RH {
    private int idRH;
    private String nom;

    // Constructeur par défaut
    public RH() {
    }

    // Constructeur avec un argument
    public RH(int idRH,String nom) {
        this.nom = nom;
    }
    public RH(int idRH) {
        this.idRH = idRH;
    }

    // Getters et Setters
    public int getIdRH() {
        return idRH;
    }

    public void setIdRH(int idRH) {
        this.idRH = idRH;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
