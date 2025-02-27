package tn.esprit.getionfinanciere.repository;


import tn.esprit.getionfinanciere.interfaces.IService;
import tn.esprit.getionfinanciere.models.Commande;
import tn.esprit.getionfinanciere.models.enums.Status;
import tn.esprit.getionfinanciere.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeRepository implements IService<Commande> {
    private final Connection cnx;

    public CommandeRepository() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Commande commande) {
        String qry = "INSERT INTO `Commande`(`dateCommande`, `quantite`, `statutCommande`, `idFournisseur`, `idResponsable` , `description`) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, commande.getDateCommande());
            pstm.setDouble(2, commande.getQuantite());
            pstm.setString(3, Status.PENDING.getLabel());
            pstm.setInt(4, commande.getIdFournisseur());
            pstm.setInt(5, commande.getIdResponsable());
            pstm.setString(6, commande.getDescription());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Commande> getAll() {
        List<Commande> commandes = new ArrayList<>();
        String qry = "SELECT * FROM `Commande`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Commande c = new Commande();
                c.setId(rs.getInt("id"));
                c.setDateCommande(rs.getString("dateCommande"));
                c.setQuantite(rs.getInt("quantite"));
                c.setStatutCommande(rs.getString("statutCommande"));
                c.setIdFournisseur(rs.getInt("idFournisseur"));
                c.setIdResponsable(rs.getInt("idResponsable"));
                c.setDescription(rs.getString("description"));

                commandes.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return commandes;
    }

    @Override
    public void update(Commande commande) {
        String qry = "UPDATE `Commande` SET `dateCommande`=?, `quantite`=?, `statutCommande`=?, `idFournisseur`=?, `idResponsable`=?, `description`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, commande.getDateCommande());
            pstm.setDouble(2, commande.getQuantite());
            pstm.setString(3, commande.getStatutCommande());
            pstm.setInt(4, commande.getIdFournisseur());
            pstm.setInt(5, commande.getIdResponsable());
            pstm.setInt(6, commande.getId());
            pstm.setString(7, commande.getDescription());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Commande commande) {
        String qry = "DELETE FROM `Commande` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, commande.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}