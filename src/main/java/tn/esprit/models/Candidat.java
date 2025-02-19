package tn.esprit.models;

public class Candidat {
    private int idCandidat;
    private String nom;

    // Constructor accepting idCandidat
    public Candidat(int idCandidat) {
        this.idCandidat = idCandidat;
    }

    // Constructor accepting idCandidat and nom
    public Candidat(int idCandidat, String nom) {
        this.idCandidat = idCandidat;
        this.nom = nom;
    }
public Candidat() {

}

    // Getters and setters
    public int getIdCandidat() {
        return idCandidat;
    }

    public void setIdCandidat(int idCandidat) {
        this.idCandidat = idCandidat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
