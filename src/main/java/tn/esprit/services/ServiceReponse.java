
package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Reponse;
import tn.esprit.utils.MyDatabase;
import tn.esprit.models.Reclamation;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ServiceReponse implements IService<Reponse> {
    private Connection cnx;

    public ServiceReponse() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public void updateReclamationStatus(int idReclamation, Reclamation.StatutReclamation statut) {
        String qry = "UPDATE reclamation SET statut = ? WHERE idR = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, statut.name()); // Convertir l'énumération en String pour SQL
            pstm.setInt(2, idReclamation);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void add(Reponse reponse) {
        String qry = "INSERT INTO reponse (reclamationId , contenu, dateRep) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, reponse.getReclamation().getIdR());  // ID de la réclamation associée
            pstm.setString(2, reponse.getContenu());
            pstm.setDate(3, Date.valueOf(reponse.getDateRep()));

            pstm.executeUpdate();

            // Mettre à jour le statut de la réclamation
            updateReclamationStatus(reponse.getReclamation().getIdR(), Reclamation.StatutReclamation.RESOLU);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public List<Reponse> getAll() {
        // Requête SQL pour récupérer toutes les réponses
        List<Reponse> reponses = new ArrayList<>();
        String qry = "SELECT * FROM `reponse`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Reponse r = new Reponse();
                r.setIdRep(rs.getInt("idRep"));
                r.setContenu(rs.getString("contenu"));
                r.setDateRep(rs.getDate("dateRep").toLocalDate());

                reponses.add(r);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reponses;
    }

    public List<Reponse> getAllByReclamation(Reclamation reclamation) {
        List<Reponse> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponse WHERE reclamationId  = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, reclamation.getIdR());  // Assurez-vous que `idRec` est l'ID de la réclamation
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Reponse r = new Reponse();
                r.setIdRep(rs.getInt("idRep"));
                r.setContenu(rs.getString("contenu"));
                r.setDateRep(rs.getDate("dateRep").toLocalDate());

                reponses.add(r);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reponses;
    }


    @Override
    public void update(Reponse reponse) {
        String qry = "UPDATE `reponse` SET `contenu`=?, `dateRep`=? WHERE `idRep`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, reponse.getContenu());
            pstm.setDate(2, Date.valueOf(reponse.getDateRep()));
            pstm.setInt(3, reponse.getIdRep());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Reponse reponse) {
        String qry = "DELETE FROM `reponse` WHERE `idRep`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, reponse.getIdRep());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

