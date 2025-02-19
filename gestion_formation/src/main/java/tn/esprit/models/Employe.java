package tn.esprit.models;

public class Employe {
    private int idEmploye;
    private String nom;
    public Employe() {

    }

    public void Employe() {

    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }



    public void setNom(String nom) {
        this.nom = nom;
    }

    public Employe(int idEmploye, String nom) {
        this.idEmploye = idEmploye;
        this.nom= nom;
    }
    public Employe(int idEmploye) {
        this.idEmploye = idEmploye;

    }
    public int getIdEmploye() {
        return idEmploye;
    }
    public String getNom() {
        return nom;
    }

    public String getNomById (Employe e,int idEmploye) {
        if (e.getIdEmploye() == idEmploye) {
            return e.getNom();
        }

        return "Employé introuvable";
    }
}
