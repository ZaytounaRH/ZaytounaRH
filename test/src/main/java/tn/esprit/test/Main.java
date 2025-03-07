package tn.esprit.test;
import tn.esprit.models.Formation;
import tn.esprit.services.ServiceFormation;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.MyDatabase;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        ServiceFormation serviceFormation = new ServiceFormation();
        serviceFormation.add(new Formation(
                "testing",
                Date.valueOf("2025-01-03"),
                Date.valueOf("2025-01-09"),
                4,
                4
        ));
        System.out.println(serviceFormation.getAll());
    }
}
