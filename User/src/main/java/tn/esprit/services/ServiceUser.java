package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.User;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {

    @Override
    public void add(User user) {
        String query = "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = MyDatabase.getInstance().getCnx().prepareStatement(query)) {

            statement.setString(1, user.getNumTel());
            statement.setInt(2, user.getJoursOuvrables());
            statement.setString(3, user.getNom());
            statement.setString(4, user.getPrenom());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getGender());
            statement.setDate(8, user.getDateDeNaissance());
            statement.setString(9, user.getUserType());
            statement.setString(10, user.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setNumTel(resultSet.getString("numTel"));
                user.setJoursOuvrables(resultSet.getInt("joursOuvrables"));
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setAddress(resultSet.getString("address"));
                user.setEmail(resultSet.getString("email"));
                user.setGender(resultSet.getString("gender"));
                user.setDateDeNaissance(resultSet.getDate("dateDeNaissance"));
                user.setUserType(resultSet.getString("user_type"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void update(User user) {
        String query = "UPDATE users SET numTel = ?, joursOuvrables = ?, nom = ?, prenom = ?, address = ?, email = ?, gender = ?, dateDeNaissance = ?, user_type = ?, password = ? WHERE id = ?";
        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getNumTel());
            statement.setInt(2, user.getJoursOuvrables());
            statement.setString(3, user.getNom());
            statement.setString(4, user.getPrenom());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getGender());
            statement.setDate(8, user.getDateDeNaissance());
            statement.setString(9, user.getUserType());
            statement.setString(10, user.getPassword());
            statement.setInt(11, user.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete(User user) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Fonction d'authentification de l'utilisateur
    public User authenticateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        User user = null;

        try (Connection connection = MyDatabase.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Paramétrer la requête avec l'email et le mot de passe
            statement.setString(1, email);
            statement.setString(2, password);

            // Exécuter la requête et récupérer les résultats
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Si l'utilisateur est trouvé, créer un objet User et le remplir avec les données de la base
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setNumTel(resultSet.getString("numTel"));
                user.setJoursOuvrables(resultSet.getInt("joursOuvrables"));
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setAddress(resultSet.getString("address"));
                user.setEmail(resultSet.getString("email"));
                user.setGender(resultSet.getString("gender"));
                user.setDateDeNaissance(resultSet.getDate("dateDeNaissance"));
                user.setUserType(resultSet.getString("user_type"));
                user.setPassword(resultSet.getString("password"));

                // Enregistrer l'utilisateur dans la session
                SessionManager.getInstance().login(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retourner l'utilisateur trouvé ou null si non trouvé
        return user;
    }
}
