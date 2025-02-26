package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Candidat;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCandidat implements IService<Candidat> {
    protected Connection cnx;

    public ServiceCandidat() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Candidat candidat) {
        String query = "INSERT INTO candidat (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, candidat.getNumTel());
            pst.setInt(2, candidat.getJoursOuvrables());
            pst.setString(3, candidat.getNom());
            pst.setString(4, candidat.getPrenom());
            pst.setString(5, candidat.getAddress());
            pst.setString(6, candidat.getEmail());
            pst.setString(7, candidat.getGender());
            pst.setDate(8, candidat.getDateDeNaissance());
            pst.setString(9, candidat.getUserType());
            pst.setString(10, candidat.getPassword());
            pst.setString(11, candidat.getStatus());

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    candidat.setCandidat_id(generatedKeys.getInt(1));
                }
            }

            System.out.println("Candidat ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du Candidat : " + e.getMessage());
        }
    }

    @Override
    public List<Candidat> getAll() {
        List<Candidat> candidats = new ArrayList<>();
        String query = "SELECT * FROM candidat";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Candidat candidat = new Candidat(
                        rs.getInt("idCandidat"),
                        rs.getString("numTel"),
                        rs.getInt("joursOuvrables"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getDate("dateDeNaissance"),
                        rs.getString("userType"),
                        rs.getString("password"),
                        rs.getString("status")
                );
                candidats.add(candidat);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des Candidats : " + e.getMessage());
        }

        return candidats;
    }

    @Override
    public void update(Candidat candidat) {
        String query = "UPDATE candidat SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, dateDeNaissance=?, userType=?, password=?, status=? WHERE idCandidat=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, candidat.getNumTel());
            pst.setInt(2, candidat.getJoursOuvrables());
            pst.setString(3, candidat.getNom());
            pst.setString(4, candidat.getPrenom());
            pst.setString(5, candidat.getAddress());
            pst.setString(6, candidat.getEmail());
            pst.setString(7, candidat.getGender());
            pst.setDate(8, candidat.getDateDeNaissance());
            pst.setString(9, candidat.getUserType());
            pst.setString(10, candidat.getPassword());
            pst.setString(11, candidat.getStatus());
            pst.setInt(12, candidat.getCandidat_id());

            pst.executeUpdate();
            System.out.println("Candidat mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du Candidat : " + e.getMessage());
        }
    }

    @Override
    public void delete(Candidat candidat) {
        String query = "DELETE FROM candidat WHERE idCandidat=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, candidat.getCandidat_id());

            pst.executeUpdate();
            System.out.println("Candidat supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du Candidat : " + e.getMessage());
        }
    }
}
