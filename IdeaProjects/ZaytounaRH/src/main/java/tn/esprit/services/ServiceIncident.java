package tn.esprit.services;

import tn.esprit.interfaces.IServiceIncident;
import tn.esprit.models.Incident;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceIncident implements IServiceIncident<Incident> {
    private Connection cnx;

    public ServiceIncident() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Incident incident) {
        String qry = "INSERT INTO `incident`(`lieu`, `gravite`, `dateIncident`, `actionsEffectuees`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, incident.getLieu());
            pstm.setString(2, incident.getGravite().name());
            pstm.setDate(3, new Date(incident.getDateIncident().getTime()));
            pstm.setString(4, incident.getActionsEffectuees());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Incident> getAll() {
        List<Incident> incidents = new ArrayList<>();
        String qry = "SELECT * FROM `incident`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Incident i = new Incident();
                i.setIdI(rs.getInt("idI"));
                i.setLieu(rs.getString("lieu"));
                i.setGravite(Incident.GraviteIncident.valueOf(rs.getString("gravite")));
                i.setDateIncident(rs.getDate("dateIncident"));
                i.setActionsEffectuees(rs.getString("actionsEffectuees"));

                incidents.add(i);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return incidents;
    }

    /*
    @Override
    public void update(Incident incident) {

    }

    @Override
    public void delete(Incident incident) {

    }

     */
}
