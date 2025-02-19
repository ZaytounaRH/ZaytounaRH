package tn.esprit.test;

import tn.esprit.models.Admin;
import tn.esprit.services.ServiceAdmin;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        ServiceAdmin serviceAdmin = new ServiceAdmin();

        Admin admin = new Admin( 27446717, 5, "Fares", "Dammak", "123 Main St", "john.doe@example.com", "Male", "IT", "Developer", new Date(2005,07,19));
       // serviceAdmin.add(admin);
        //System.out.println("Admin ID: " + admin.getId());

        System.out.println(serviceAdmin.getAll());

        admin.setNom("Faress");
        System.out.println("Admin ID: " + admin.getId());
       // serviceAdmin.update(admin);

        System.out.println(serviceAdmin.getAll());

        //serviceAdmin.delete(admin);
    }
}