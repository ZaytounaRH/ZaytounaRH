package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.RH;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRH implements IService<RH> {
    protected Connection cnx;

    // Constructor to initialize the connection
    public ServiceRH() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(RH rh) {
        String query =  "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, rh.getNumTel());
            statement.setInt(2, rh.getJoursOuvrables());
            statement.setString(3, rh.getNom());
            statement.setString(4, rh.getPrenom());
            statement.setString(5, rh.getAddress());
            statement.setString(6, rh.getEmail());
            statement.setString(7, rh.getGender());
            statement.setString(8, rh.getDateDeNaissance().toString());
            statement.setString(9, rh.getUserType());
            statement.setString(10, rh.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RH> getAll() {
        List<RH> rhList = new ArrayList<>();
        String query = "SELECT * FROM rh";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                RH rh = new RH();
                rh.setIdRH(resultSet.getInt("rh_id"));

                rhList.add(rh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rhList;
    }

    @Override
    public void update(RH rh) {
        String query = "UPDATE users SET numTel=?, joursOuvrables=?, nom=?, prenom=?, address=?, email=?, gender=?, dateDeNaissance=?, user_type=?, password=? WHERE id=?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, rh.getNumTel());
            statement.setInt(2, rh.getJoursOuvrables());
            statement.setString(3, rh.getNom());
            statement.setString(4, rh.getPrenom());
            statement.setString(5, rh.getAddress());
            statement.setString(6, rh.getEmail());
            statement.setString(7, rh.getGender());
            statement.setString(8, rh.getDateDeNaissance().toString());
            statement.setString(9, rh.getUserType());
            statement.setString(10, rh.getPassword());
            statement.setInt(11, rh.getIdRH());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(RH rh) {
        String query = "DELETE FROM rh WHERE rh_id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, rh.getIdRH());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
