package tn.esprit.services;

import tn.esprit.interfaces.IServiceReclamation;
import tn.esprit.models.Reclamation;
import tn.esprit.utils.MyDatabase;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class ServiceReclamation implements IServiceReclamation<Reclamation> {
    private Connection cnx ;

    public ServiceReclamation(){
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Reclamation reclamation) {
        //create Qry SQL
        //execute Qry
        String qry ="INSERT INTO `reclamation`(`titre`, `description`, `dateSoumission` , `statut`, `priorite`, `pieceJointe`) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1,reclamation.getTitre());
            pstm.setString(2, reclamation.getDescription());
            pstm.setDate(3,new Date (reclamation.getDateSoumission().getTime()));
            pstm.setString(4,reclamation.getStatut().name());
            pstm.setString(5, reclamation.getPriorite().name());
            pstm.setString(6,reclamation.getPieceJointe());


            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public List<Reclamation> getAll() {
        //create Qry sql
        //execution
        //Mapping data


        List<Reclamation> reclamations = new ArrayList<>();
        String qry ="SELECT * FROM `reclamation`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Reclamation p = new Reclamation();
                p.setIdR(rs.getInt("idR"));
                //p.setTitre(rs.getString(2));
                p.setTitre(rs.getString("titre"));
                p.setDescription(rs.getString("description"));
                p.setDateSoumission(rs.getDate("dateSoumission"));
                p.setStatut(Reclamation.StatutReclamation.valueOf(rs.getString("statut")));
                p.setPriorite(Reclamation.PrioriteReclamation.valueOf(rs.getString("priorite")));
                p.setPieceJointe(rs.getString("pieceJointe"));

                reclamations.add(p);
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reclamations;
    }

    /*
    @Override
    public void update(Recalamtion reclamation) {

    }

    @Override
    public void delete(Recalamtion reclamation) {

    }

     */
}
