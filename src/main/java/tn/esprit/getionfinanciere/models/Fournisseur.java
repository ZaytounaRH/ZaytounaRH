package tn.esprit.getionfinanciere.models;


import tn.esprit.getionfinanciere.models.enums.TypeService;

public class Fournisseur {
    private int id;
    private String nomFournisseur;
    private String adresse;
    private String contact;
    private TypeService typeService;


    public Fournisseur() {
    }

    public Fournisseur(String nomFournisseur, String adresse, String contact, TypeService typeService) {
        this.nomFournisseur = nomFournisseur;
        this.adresse = adresse;
        this.contact = contact;
        this.typeService = typeService;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public TypeService getTypeService() {
        return typeService;
    }

    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id=" + id +
                ", nomFournisseur='" + nomFournisseur + '\'' +
                ", adresse='" + adresse + '\'' +
                ", contact='" + contact + '\'' +
                ", typeService='" + typeService.name() + '\'' +
                "}\n";
    }
}
