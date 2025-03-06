package tn.esprit.getionfinanciere.services;


import tn.esprit.getionfinanciere.models.Commande;
import tn.esprit.getionfinanciere.models.Produit;
import tn.esprit.getionfinanciere.repository.CommandeRepository;
import tn.esprit.getionfinanciere.repository.DepenseRepository;
import tn.esprit.getionfinanciere.repository.ProduitRepository;

public class ServiceCommande {

  private final ProduitRepository produitRepository = new ProduitRepository();
  private final DepenseRepository depenseRepository = new DepenseRepository();
  private final CommandeRepository commandeRepository = new CommandeRepository();

public void addDepense(Commande commande){
  Produit produit = produitRepository.findById(commande.getIdProduit());
  double prix = produit.getPrix() * commande.getQuantite();
  commande.setPrixCommande(prix);
  commandeRepository.add(commande);
}

}