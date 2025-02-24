package tn.esprit.getionfinanciere.services;


import tn.esprit.getionfinanciere.interfaces.IService;
import tn.esprit.getionfinanciere.models.Fournisseur;
import tn.esprit.getionfinanciere.models.enums.TypeService;
import tn.esprit.getionfinanciere.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFournisseur implements IService<Fournisseur> {
    private final Connection cnx;

    public ServiceFournisseur() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Fournisseur fournisseur) {
        String qry = "INSERT INTO `Fournisseur`(`nomFournisseur`, `adresse`, `contact`, `typeService`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, fournisseur.getNomFournisseur());
            pstm.setString(2, fournisseur.getAdresse());
            pstm.setString(3, fournisseur.getContact());
            pstm.setString(4, fournisseur.getTypeService().name());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Fournisseur> getAll() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String qry = "SELECT * FROM `Fournisseur`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Fournisseur f = new Fournisseur();
                f.setId(rs.getInt("id"));
                f.setNomFournisseur(rs.getString("nomFournisseur"));
                f.setAdresse(rs.getString("adresse"));
                f.setContact(rs.getString("contact"));
                f.setTypeService(TypeService.valueOf(rs.getString("typeService")));

                fournisseurs.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return fournisseurs;
    }

    @Override
    public void update(Fournisseur fournisseur) {
        String qry = "UPDATE `Fournisseur` SET `nomFournisseur`=?, `adresse`=?, `contact`=?, `typeService`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, fournisseur.getNomFournisseur());
            pstm.setString(2, fournisseur.getAdresse());
            pstm.setString(3, fournisseur.getContact());
            pstm.setString(4, fournisseur.getTypeService().name());
            pstm.setInt(5, fournisseur.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Fournisseur fournisseur) {
        String qry = "DELETE FROM `Fournisseur` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, fournisseur.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}