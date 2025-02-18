package tn.esprit.models;

public class Employe {
    private int idEmploye;
    private String nom;

    public Employe(int idEmploye, String nom) {
        this.idEmploye = idEmploye;
        this.nom= nom;
    }
    public Employe(int idEmploye) {
        this.idEmploye = idEmploye;

    }

    public void Employe() {

    }



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
    @Override
    public String toString() {
        return nom;
    }
}
