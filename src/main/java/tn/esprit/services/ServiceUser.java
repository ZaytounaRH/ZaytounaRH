package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Candidat;
import tn.esprit.models.Employee;
import tn.esprit.models.RH;
import tn.esprit.models.User;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {
    private Connection connection ;
    public ServiceUser() {
        this.connection=MyDatabase.getInstance().getCnx();
        //cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(User user) {
        String query = "INSERT INTO users (numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, user_type, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

    @Override
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


  /*  public User authenticateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        User user = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {


            // Paramétrer la requête avec l'email et le mot de passe
            statement.setString(1, email);
            statement.setString(2, password);

            // Exécuter la requête et récupérer les résultats
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String userType = resultSet.getString("user_type");

                if ("RH".equals(userType)) {
                    user = new RH(); // Crée un objet de type RH
                    String rhQuery = "SELECT rh_id FROM rh WHERE user_id = ?"; // Supposons que user_id est la clé étrangère
                    try (PreparedStatement rhStatement = connection.prepareStatement(rhQuery)) {
                        rhStatement.setInt(1, resultSet.getInt("id"));
                        ResultSet rhResultSet = rhStatement.executeQuery();

                        // Si on trouve un RH dans la table rh, récupérer l'idRH
                        if (rhResultSet.next()) {
                            int idRH = rhResultSet.getInt("rh_id");
                            ((RH) user).setIdRH(rhResultSet.getInt("rh_id"));  // Assigner idRH à l'objet RH
                            System.out.println("l'id du rh est :" + idRH);
                        }
                    }





                } else {
                    user = new User(); // Crée un objet de type User
                }

                // Si l'utilisateur est trouvé, créer un objet User et le remplir avec les données de la base
                //user = new User();
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


                System.out.println("Type d'utilisateur dans la base de données : " + user.getUserType());

                // Enregistrer l'utilisateur dans la session
                SessionManager.getInstance().login(user);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retourner l'utilisateur trouvé ou null si non trouvé
        return user;
    }*/
  // Fonction d'authentification de l'utilisateur
  public User authenticateUser(String email, String password) {
      String query = "SELECT * FROM users WHERE email = ? AND password = ?";
      User user = null;

      try (PreparedStatement statement = connection.prepareStatement(query)) {
          // Paramétrer la requête avec l'email et le mot de passe
          statement.setString(1, email);
          statement.setString(2, password);

          // Exécuter la requête et récupérer les résultats
          ResultSet resultSet = statement.executeQuery();

          if (resultSet.next()) {
              String userType = resultSet.getString("user_type");

              // Création de l'objet utilisateur selon son type
              switch (userType) {
                  case "RH":
                      user = new RH();
                      try (PreparedStatement rhStatement = connection.prepareStatement(
                              "SELECT rh_id FROM rh WHERE user_id = ?")) {
                          rhStatement.setInt(1, resultSet.getInt("id"));
                          try (ResultSet rhResultSet = rhStatement.executeQuery()) {
                              if (rhResultSet.next()) {
                                  int idRH = rhResultSet.getInt("rh_id");
                                  ((RH) user).setIdRH(idRH);
                                  System.out.println("ID RH récupéré : " + idRH);
                              }
                          }
                      }
                      break;

                  case "Employee":
                      user = new Employee();
                      try (PreparedStatement empStatement = connection.prepareStatement(
                              "SELECT employee_id FROM employee WHERE user_id = ?")) {
                          empStatement.setInt(1, resultSet.getInt("id"));
                          try (ResultSet empResultSet = empStatement.executeQuery()) {
                              if (empResultSet.next()) {
                                  int idEmp = empResultSet.getInt("employee_id");
                                  ((Employee) user).setIdEmployee(idEmp);
                                  System.out.println("ID Employé récupéré : " + idEmp);
                              }
                          }
                      }
                      break;

                  case "Candidat":
                      user = new Candidat();
                      try (PreparedStatement cStatement = connection.prepareStatement(
                              "SELECT candidat_id FROM candidat WHERE user_id = ?")) {
                          cStatement.setInt(1, resultSet.getInt("id"));
                          try (ResultSet cResultSet = cStatement.executeQuery()) {
                              if (cResultSet.next()) {
                                  int idCandidat = cResultSet.getInt("candidat_id");
                                  ((Candidat) user).setCandidat_id(idCandidat);
                                  System.out.println("ID Candidat récupéré : " + idCandidat);
                              }
                          }
                      }
                      break;

                  default:
                      user = new User();
                      break;
              }

              // Remplissage des informations utilisateur
              user.setId(resultSet.getInt("id"));
              user.setNumTel(resultSet.getString("numTel"));
              user.setJoursOuvrables(resultSet.getInt("joursOuvrables"));
              user.setNom(resultSet.getString("nom"));
              user.setPrenom(resultSet.getString("prenom"));
              user.setAddress(resultSet.getString("address"));
              user.setEmail(resultSet.getString("email"));
              user.setGender(resultSet.getString("gender"));
              user.setDateDeNaissance(resultSet.getDate("dateDeNaissance"));
              user.setUserType(userType);
              user.setPassword(resultSet.getString("password"));

              System.out.println("Utilisateur authentifié avec succès. Type : " + user.getUserType());

              // Enregistrer l'utilisateur dans la session
              SessionManager.getInstance().login(user);
          }
      } catch (SQLException e) {
          System.err.println("Erreur lors de l'authentification de l'utilisateur : " + e.getMessage());
          e.printStackTrace();
      }

      return user; // Retourner l'utilisateur trouvé ou null si non trouvé
  }
}
