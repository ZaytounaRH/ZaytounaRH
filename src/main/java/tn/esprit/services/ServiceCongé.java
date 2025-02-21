package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.utils.MyDatabase;

import tn.esprit.models.Congé;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServiceCongé implements IService<Congé> {
    private Connection cnx;

    public ServiceCongé() {
        cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(Congé congé) {
        String qry = "INSERT INTO `congé`(`dateDebut`, `dateFin`, `dateDemande`, `motif`) VALUES (?,?,?,?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            // Convertir java.util.Date en java.sql.Date
            pstm.setDate(1, new java.sql.Date(congé.getDateDebut().getTime()));
            pstm.setDate(2, new java.sql.Date(congé.getDateFin().getTime()));
            pstm.setDate(3, new java.sql.Date(congé.getDateDemande().getTime()));
            pstm.setString(4, congé.getMotif());
            int rowsInserted = pstm.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Congé ajouté avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du congé : " + e.getMessage());
        }
    }
    @Override
    public List<Congé> getAll() {
        List<Congé> congés = new ArrayList<>();
        String qry = "SELECT * FROM `congé`";  // Sélectionne les données de la table congé

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Congé congé = new Congé();

                // Mappe les colonnes de la base de données avec les attributs de l'objet Congé
                congé.setId_congé(rs.getInt("id_congé"));
                congé.setDateDebut(rs.getDate("dateDebut"));
                congé.setDateFin(rs.getDate("dateFin"));
                congé.setDateDemande(rs.getDate("dateDemande"));
                congé.setMotif(rs.getString("motif"));


                congés.add(congé);  // Ajoute le congé à la liste
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des congés : " + e.getMessage());
        }

        return congés;  // Retourne la liste des congés
    }

    @Override
    public void update(Congé congé) {
        String query = "UPDATE `congé` SET `dateDebut` = ?, `dateFin` = ?, `dateDemande` = ?, `motif` = ? WHERE `id_congé` = ?";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Remplir les paramètres de la requête
            ps.setDate(1, new java.sql.Date(congé.getDateDebut().getTime()));  // Convertir Date en java.sql.Date
            ps.setDate(2, new java.sql.Date(congé.getDateFin().getTime()));    // Convertir Date en java.sql.Date
            ps.setDate(3, new java.sql.Date(congé.getDateDemande().getTime())); // Convertir Date en java.sql.Date
            ps.setString(4, congé.getMotif());

            ps.setInt(5, congé.getId_congé());

            // Exécuter la mise à jour
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Le congé a été mis à jour avec succès.");
            } else {
                System.out.println("Aucun congé trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du congé : " + e.getMessage());
        }
    }



    @Override
    public void delete() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Entrez l'ID du congé à supprimer :");
        int id = Integer.parseInt(scan.nextLine());

        String sql = "DELETE FROM `congé` WHERE `id_congé` = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Le congé a été supprimé avec succès.");
            } else {
                System.out.println("⚠ Aucun congé trouvé avec l'ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du congé : " + e.getMessage());
        }
    }
    public Congé getCongéById(int idCongé) {
        Congé congé = null;
        String qry = "SELECT * FROM `congé` WHERE `id_congé` = ?";  // Requête SQL pour récupérer le congé par son ID

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, idCongé);  // Paramétrage de l'ID du congé dans la requête

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    congé = new Congé();
                    // Remplir l'objet Congé avec les données récupérées de la base
                    congé.setId_congé(rs.getInt("id_congé"));
                    congé.setDateDebut(rs.getDate("dateDebut"));
                    congé.setDateFin(rs.getDate("dateFin"));
                    congé.setDateDemande(rs.getDate("dateDemande"));
                    congé.setMotif(rs.getString("motif"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du congé par ID : " + e.getMessage());
        }

        return congé;  // Retourne le congé trouvé (ou null si aucun congé n'est trouvé avec cet ID)
    }



}





