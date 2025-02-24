package tn.esprit.getionfinanciere.models;

public class Commande {

    private int id;
    private String dateCommande;
    private double montantTotal;
    private String statutCommande;
    private int idFournisseur;
    private int idResponsable;

    // Constructeurs
    public Commande() {
    }

    public Commande(String dateCommande, double montantTotal, String statutCommande, int idFournisseur, int idResponsable) {
        this.dateCommande = dateCommande;
        this.montantTotal = montantTotal;
        this.statutCommande = statutCommande;
        this.idFournisseur = idFournisseur;
        this.idResponsable = idResponsable;
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

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
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



    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + id +
                ", dateCommande='" + dateCommande + '\'' +
                ", montantTotal=" + montantTotal +
                ", statutCommande='" + statutCommande + '\'' +
                ", idFournisseur=" + idFournisseur +
                ", idResponsable=" + idResponsable +
                "}\n";
    }

    public void setStatusCommande(String value) {
    }

    public void setMontant(double v) {
    }


}
