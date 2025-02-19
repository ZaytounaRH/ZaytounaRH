package tn.esprit.services;
import tn.esprit.models.Certification;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployeCertification {

    private final Connection cnx ;
    public ServiceEmployeCertification() {
        cnx=MyDatabase.getInstance().getCnx();
    }
    public List<Certification> getCertificationsByEmploye(int idEmploye) {
        List<Certification> certifications=new ArrayList<>();
        String qry = "SELECT c.idCertif, c.titreCertif FROM certification c JOIN employe_certification ec ON c.idCertif = ec.idCertif WHERE ec.idEmploye = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, idEmploye);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Certification certif = new Certification(
                        rs.getInt("idCertif"),
                        rs.getString("titreCertif")
                );
                certifications.add(certif);

        }}
        catch (SQLException e) {
                System.out.println( e.getMessage());
        }
        return certifications;
    }
    public String getNomEmployeById(int idEmploye) {
        String qry = "SELECT nom FROM employe WHERE idEmploye = ?";
        String nom = "Inconnu"; // Valeur par défaut si l'employé n'est pas trouvé

        try {
            PreparedStatement pstmt = cnx.prepareStatement(qry);
            pstmt.setInt(1, idEmploye);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                nom = rs.getString("nom");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nom de l'employé : " + e.getMessage());
        }
        return nom;
    }

}
