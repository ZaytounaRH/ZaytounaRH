package tn.esprit.test;

import tn.esprit.models.User;
import tn.esprit.services.ServiceUser;

import java.sql.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceUser serviceUser = new ServiceUser();
        User newUser = new User( "27446717", 5, "Fares", "Dammmak", "123 Street", "fares@example.com", "Male", new Date(2002,07,19), "Admin", "securePass");
        serviceUser.add(newUser);
        System.out.println("Nouvel ID généré : " + newUser.getId());

    }
}
