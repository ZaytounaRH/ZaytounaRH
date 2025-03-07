package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Candidat;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCandidat implements IService<Candidat> {

    @Override
    public void add(Candidat candidat) {
        String query = "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setString(1, candidat.getNumTel());
            statement.setInt(2, candidat.getJoursOuvrables());
            statement.setString(3, candidat.getNom());
            statement.setString(4, candidat.getPrenom());
            statement.setString(5, candidat.getAddress());
            statement.setString(6, candidat.getEmail());
            statement.setString(7, candidat.getGender());
            statement.setString(8,candidat.getUserType());
            statement.setString(9, "Candidat");
            statement.setString(10, candidat.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Candidat> getAll() {
        List<Candidat> candidats = new ArrayList<>();
        String query = "SELECT * FROM users WHERE user_type='Candidat'";        try (Connection connection = MyDatabase.getInstance().getCnx();
                                                                                     PreparedStatement statement = connection.prepareStatement(query);
                                                                                     ResultSet rs = statement.executeQuery()) {

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
                        rs.getString("user_type"),
                        rs.getString("password"),
                        rs.getString("status")
                );
                candidats.add(candidat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidats;
    }

    @Override
    public void update(Candidat candidat) {
        String query = "UPDATE candidat SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, dateDeNaissance=?, user_type=?, password=?, status=? WHERE idCandidat=?";
        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement pst= connection.prepareStatement(query)) {

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Candidat candidat) {
        String query = "DELETE FROM candidat WHERE candidat_id = ?";
        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, candidat.getCandidat_id());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
