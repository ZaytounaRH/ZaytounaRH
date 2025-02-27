package tn.esprit.getionfinanciere.models;

import tn.esprit.getionfinanciere.models.enums.Status;

public class Commande {

    private int id;
    private String dateCommande;
    private int quantite;
    private String statutCommande;
    private int idFournisseur;
    private int idResponsable;
    private String description;


    // Constructeurs
    public Commande() {
    }

    public Commande(String dateCommande, int quantite, int idFournisseur, int idResponsable, String description ) {
        this.dateCommande = dateCommande;
        this.quantite = quantite;
        this.statutCommande = Status.PENDING.getLabel();
        this.idFournisseur = idFournisseur;
        this.idResponsable = idResponsable;
        this.description = description;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(String dateCommande) {
        this.dateCommande = dateCommande;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getStatutCommande() {
        return statutCommande;
    }

    public void setStatutCommande(String statutCommande) {
        this.statutCommande = statutCommande;
    }

    public int getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(int idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + id +
                ", dateCommande='" + dateCommande + '\'' +
                ", quantite=" + quantite +
                ", statutCommande='" + statutCommande + '\'' +
                ", idFournisseur=" + idFournisseur +
                ", idResponsable=" + idResponsable +
                ", description='" + description + '\'' +
                "}\n";
    }

    public void setStatusCommande(String value) {
    }

    public void setMontant(double v) {
    }


}
