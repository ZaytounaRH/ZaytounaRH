package tn.esprit.services;
import tn.esprit.models.Certification;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;


public class ServiceEmployeCertification {

    private final Connection cnx ;
    public ServiceEmployeCertification() {
        cnx=MyDatabase.getInstance().getCnx();
    }

    public List<Certification> getCertificationsByEmploye(int employee_id) {
        List<Certification> certifications=new ArrayList<>();
        String qry = "SELECT c.idCertif, c.titreCertif FROM certification c JOIN employe_certification ec ON c.idCertif = ec.idCertif WHERE ec.employee_id = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, employee_id);
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

    public void ajouterCertificationAEmploye(int idEmploye, int idCertif, Date dateObtention) {
        if (!controleSaisieCertification(idEmploye, idCertif, dateObtention)) {
            System.out.println("Erreur : données invalides, insertion annulée !");
            return;
        }


        String qry = "INSERT INTO employe_certification (employee_id, idCertif, dateObtention) VALUES (?, ?, ?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, idEmploye);
            pstm.setInt(2, idCertif);
            pstm.setDate(3, dateObtention);

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Certification ajoutée avec succès !");
            } else {
                System.out.println("Erreur lors de l'ajout de la certification.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la certification : " + e.getMessage());
        }
    }

    public String getNomEmployeById(int idEmploye) {
        String qry = "SELECT nom FROM users WHERE id = ? AND user_type = 'Employee'";
        String nom = "Inconnu";

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

    public boolean controleSaisieCertification(int idEmploye, int idCertif, Date dateObtention) {

        if (idEmploye <= 0) {
            System.out.println("L'ID de l'employé doit être un entier positif.");
            return false;
        }


        if (idCertif <= 0) {
            System.out.println("L'ID de la certification doit être un entier positif.");
            return false;
        }

        if (dateObtention == null) {
            System.out.println("La date d'obtention est obligatoire.");
            return false;
        }

        Date currentDate = new Date(System.currentTimeMillis());
        if (dateObtention.after(currentDate)) {
            System.out.println("La date d'obtention ne peut pas être dans le futur.");
            return false;
        }


        return true;
    }




}
