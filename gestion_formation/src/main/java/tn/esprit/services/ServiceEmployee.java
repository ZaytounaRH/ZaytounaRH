package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Employee;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceEmployee implements IService<Employee> {
    protected Connection cnx;

public ServiceEmployee() {
    cnx = MyDatabase.getInstance().getCnx();
}
    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM users WHERE user_type = 'EMPLOYEE'";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("numTel"),
                        rs.getInt("joursOuvrables"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getDate("dateDeNaissance"),
                        rs.getString("user_type"),
                        rs.getString("password")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des Employees : " + e.getMessage());
        }

        return employees;
    }

    @Override
    public void add(Employee employee) {

    }
    @Override
    public void update(Employee employee) {

    }
    @Override
    public void delete(Employee employee) {

    }
}
