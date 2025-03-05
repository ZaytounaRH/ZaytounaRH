package tn.esprit.test;

import tn.esprit.models.Employee;
import tn.esprit.models.User;
import tn.esprit.services.ServiceEmployee;
import tn.esprit.services.ServiceUser;

import java.sql.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceEmployee serviceUser = new ServiceEmployee();
        Employee employee = new Employee("27 446 717",5,"Fares","Dammak","Manouba","dammak@gmail.com","Male",new Date(2002,02,1),"EMPLOYEE","fares","img");
        serviceUser.add(employee);

    }
}
