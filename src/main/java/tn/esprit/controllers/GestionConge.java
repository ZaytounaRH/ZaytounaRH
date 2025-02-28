package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import tn.esprit.models.Conge;
import tn.esprit.models.Employee;
import tn.esprit.utils.SessionManager;
import tn.esprit.models.User;

import java.sql.Date;

public class GestionConge {

    @FXML
    private DatePicker dpDateDebut;
    @FXML
    private DatePicker dpDateFin;
    @FXML
    private TextField tfMotif;
    @FXML
    private Label lbConges;
    // Supposons que tu aies un bouton de connexion
    @FXML
    private void handleLoginButton() {
        loginAsEmployee();  // Connecter l'employé
    }



    public void loginAsEmployee() {
        // Crée un objet Employee (id, nom, etc.)
        Employee employee = new Employee();
        employee.setId(1);  // Exemple d'ID d'employé
        employee.setNom("John Doe");  // Exemple de nom

        // Si l'employé existe dans la base de données, tu peux le récupérer et l'assigner à currentUser
        // Par exemple : employee = employeeService.getEmployeeById(1);

        // Connecter l'employé dans la session
        SessionManager.getInstance().login(employee);  // Ici, tu "connectes" l'employé
    }

    // Méthode pour ajouter un congé
    public void ajouterConge() {
        try {
            // Vérifier que les champs ne sont pas vides
            if (dpDateDebut.getValue() == null || dpDateFin.getValue() == null || tfMotif.getText().isEmpty()) {
                lbConges.setText("Tous les champs doivent être remplis.");
                return;
            }

            // Vérification de la date de fin > date de début
            if (dpDateFin.getValue().isBefore(dpDateDebut.getValue())) {
                lbConges.setText("La date de fin ne peut pas être avant la date de début.");
                return;
            }

            // Convertir les DatePicker en Date
            Date dateDebut = Date.valueOf(dpDateDebut.getValue());
            Date dateFin = Date.valueOf(dpDateFin.getValue());
            String motif = tfMotif.getText();

            // Récupérer l'utilisateur actuel (l'employé connecté)
            User currentUser = SessionManager.getInstance().getCurrentUser();

            // Vérifier si l'utilisateur est bien un Employee et non un User par défaut
            if (currentUser instanceof Employee) {
                Employee employeConnecte = (Employee) currentUser;

                // Créer le congé avec l'employé connecté
                Conge conge = new Conge(dateDebut, dateFin, motif, Conge.statut.EN_ATTENTE, employeConnecte);

                // Ajouter le congé à la base de données ou faire autre chose avec (par exemple, afficher dans la liste des congés)
                System.out.println("Congé ajouté : " + conge);

                lbConges.setText("Congé ajouté avec succès!");
            } else {
                lbConges.setText("Aucun employé connecté.");
            }
        } catch (IllegalArgumentException e) {
            lbConges.setText("Erreur : " + e.getMessage());
        }
    }

}
