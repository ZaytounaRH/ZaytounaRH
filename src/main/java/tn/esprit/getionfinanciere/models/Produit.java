package tn.esprit.getionfinanciere.models;

public class Produit {
  private int id;
  private String produitName;
  private double prix;
  private String nomFournisseur;
  private int idFournisseur;


  public Produit(){
  }

  public Produit(int idFournisseur, double prix, String produitName) {
    this.idFournisseur = idFournisseur;
    this.prix = prix;
    this.produitName = produitName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getPrix() {
    return prix;
  }

  public void setPrix(double prix) {
    this.prix = prix;
  }

  public int getIdFournisseur() {
    return idFournisseur;
  }

  public void setIdFournisseur(int idFournisseur) {
    this.idFournisseur = idFournisseur;
  }

  public String getProduitName() {
    return produitName;
  }

  public void setProduitName(String produitName) {
    this.produitName = produitName;
  }

  public String getNomFournisseur() {
    return nomFournisseur;
  }

  public void setNomFournisseur(String nomFournisseur) {
    this.nomFournisseur = nomFournisseur;
  }

  @Override
  public String toString() {
    return "Produit{" +
        "id=" + id +
        ", prix=" + prix +
        ", idFournisseur=" + idFournisseur +
        ", produit='" + produitName + '\'' +
        '}';
  }
}
