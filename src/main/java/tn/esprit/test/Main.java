package tn.esprit.test;

import tn.esprit.models.Employee;
import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;

import java.sql.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceUser serviceUser = new ServiceUser();
        List<User> users = serviceUser.getAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            // Iterate through the list of users and print their details
            for (User user : users) {
                System.out.println("User ID: " + user.getId());
                System.out.println("Name: " + user.getNom() + " " + user.getPrenom());
                System.out.println("Email: " + user.getEmail());
                System.out.println("Phone Number: " + user.getNumTel());
                System.out.println("User Type: " + user.getUserType());
                System.out.println("Date of Birth: " + user.getDateDeNaissance());
                System.out.println("------------------------------------------------");
            }
        }

    }
}
