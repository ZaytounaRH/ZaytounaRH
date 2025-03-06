package tn.esprit.getionfinanciere.repository;


import tn.esprit.getionfinanciere.interfaces.IService;
import tn.esprit.getionfinanciere.models.Produit;
import tn.esprit.getionfinanciere.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitRepository implements IService<Produit> {
    private final Connection cnx;

    public ProduitRepository() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Produit produit) {
        String qry = "INSERT INTO `Produit`(`produitName`, `prix`, `idFournisseur`, `nomFournisseur`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, produit.getProduitName());
            pstm.setDouble(2, produit.getPrix());
            pstm.setInt(3, produit.getIdFournisseur());
            pstm.setString(4, produit.getNomFournisseur());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Produit> getAll() {
        List<Produit> produits = new ArrayList<>();
        String qry = "SELECT p.*, f.nomFournisseur FROM Produit p " +
            "JOIN Fournisseur f ON p.idFournisseur = f.id";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Produit produit = new Produit();
                produit.setId(rs.getInt("id"));
                produit.setProduitName(rs.getString("produitName"));
                produit.setPrix(rs.getDouble("prix"));
                produit.setIdFournisseur(rs.getInt("idFournisseur"));
                produit.setNomFournisseur(rs.getString("nomFournisseur"));  // Set supplier name
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return produits;
    }

    @Override
    public void update(Produit Produit) {
        String qry = "UPDATE `Produit` SET `produit`=?, `prix`=?, `idFournisseur`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, Produit.getProduitName());
            pstm.setDouble(2, Produit.getPrix());
            pstm.setInt(3, Produit.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Produit Produit) {
        String qry = "DELETE FROM `Produit` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, Produit.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Produit findById(int id) {
        String qry = "SELECT * FROM `Produit` WHERE `id`=?";
        Produit produit = null;
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                produit = new Produit();
                produit.setId(rs.getInt("id"));
                produit.setProduitName(rs.getString("produitName"));
                produit.setPrix(rs.getDouble("prix"));
                produit.setNomFournisseur(rs.getString("nomFournisseur"));
                produit.setIdFournisseur(rs.getInt("idFournisseur"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return produit;
    }


    public List<Produit> getProduitsByFournisseur(int id) {
        String qry = "SELECT * FROM `Produit` WHERE `idFournisseur`=?";
        List<Produit> produits = new ArrayList<>();

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id); // Set the fournisseur ID
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Produit produit = new Produit();
                produit.setId(rs.getInt("id"));
                produit.setProduitName(rs.getString("produitName"));
                produit.setPrix(rs.getDouble("prix"));
                produit.setNomFournisseur(rs.getString("nomFournisseur"));
                produit.setIdFournisseur(rs.getInt("idFournisseur"));
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return produits; // Return the list of produits
    }

}
