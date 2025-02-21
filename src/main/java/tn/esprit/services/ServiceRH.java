package tn.esprit.services;

import tn.esprit.models.RH;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;

public class ServiceRH extends ServiceUser<RH> {
    private Connection cnx;

    public ServiceRH() {
        cnx = MyDatabase.getInstance().getCnx();
    }
}