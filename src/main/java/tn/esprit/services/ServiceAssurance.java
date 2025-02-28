package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Assurance;
import tn.esprit.utils.MyDatabase;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ServiceAssurance implements IService<Assurance> {
    private Connection cnx;

    public ServiceAssurance() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Assurance assurance) {
        // Requête SQL pour ajouter une assurance
        String qry = "INSERT INTO `assurance`(`nom`, `type`, `dateExpiration`) VALUES (?, ?, ?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, assurance.getNom());
            pstm.setString(2, assurance.getType().name());
            pstm.setDate(3, Date.valueOf(assurance.getDateExpiration())); // Conversion de LocalDate en java.sql.Date

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Assurance> getAll() {
        // Requête SQL pour récupérer toutes les assurances
        List<Assurance> assurances = new ArrayList<>();
        String qry = "SELECT * FROM `assurance`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Assurance a = new Assurance();
                a.setIdA(rs.getInt("idA"));
                a.setNom(rs.getString("nom"));
                a.setType(Assurance.TypeAssurance.valueOf(rs.getString("type"))); // Conversion du type depuis l'énumération
                a.setDateExpiration(rs.getDate("dateExpiration").toLocalDate()); // Conversion de java.sql.Date en LocalDate

                assurances.add(a);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return assurances;
    }

    @Override
    public void update(Assurance assurance) {
        String qry = "UPDATE `assurance` SET `nom`=?, `type`=?, `dateExpiration`=? WHERE `idA`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, assurance.getNom());
            pstm.setString(2, assurance.getType().name());
            pstm.setDate(3, Date.valueOf(assurance.getDateExpiration()));
            pstm.setInt(4, assurance.getIdA());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Assurance assurance) {
        String qry = "DELETE FROM `assurance` WHERE `idA`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, assurance.getIdA());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode de recherche par nom (recherche partielle, insensible à la casse)
    public List<Assurance> searchByName(String keyword) {
        List<Assurance> assurances = new ArrayList<>();
        String qry = "SELECT * FROM `assurance` WHERE LOWER(`nom`) LIKE LOWER(?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, "%" + keyword + "%"); // Recherche partielle
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Assurance a = new Assurance();
                a.setIdA(rs.getInt("idA"));
                a.setNom(rs.getString("nom"));
                a.setType(Assurance.TypeAssurance.valueOf(rs.getString("type")));
                a.setDateExpiration(rs.getDate("dateExpiration").toLocalDate());
                assurances.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return assurances;
    }

    // Méthode de recherche par type d'assurance
    public List<Assurance> searchByType(Assurance.TypeAssurance type) {
        List<Assurance> assurances = new ArrayList<>();
        String qry = "SELECT * FROM `assurance` WHERE `type` = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, type.name()); // Utilisation de name() pour convertir l'enum en String
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Assurance a = new Assurance();
                a.setIdA(rs.getInt("idA"));
                a.setNom(rs.getString("nom"));
                a.setType(Assurance.TypeAssurance.valueOf(rs.getString("type")));
                a.setDateExpiration(rs.getDate("dateExpiration").toLocalDate());
                assurances.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return assurances;
    }

    // Méthode pour trier les assurances par date d'expiration
    public List<Assurance> sortByDateExpiration(boolean ascending) {
        List<Assurance> assurances = new ArrayList<>();
        String order = ascending ? "ASC" : "DESC";
        String qry = "SELECT * FROM `assurance` ORDER BY `dateExpiration` " + order;

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Assurance a = new Assurance();
                a.setIdA(rs.getInt("idA"));
                a.setNom(rs.getString("nom"));
                a.setType(Assurance.TypeAssurance.valueOf(rs.getString("type")));
                a.setDateExpiration(rs.getDate("dateExpiration").toLocalDate());
                assurances.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return assurances;
    }

}
