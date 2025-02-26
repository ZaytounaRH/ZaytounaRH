package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.*;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceFormation implements IService<Formation> {
    private final Connection cnx ;

    public ServiceFormation() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Formation formation) {
        if (!isValidFormation(formation)) {
            System.out.println("Erreur : données invalides, insertion annulée !");
            return;
        }
        if(isFormationExists(formation.getNomFormation(), formation.getDescriptionFormation())){
            System.out.println("Erreur : Une formation avec le même nom et la même description existe déjà !");
            return;
        }

        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérification du rôle
        if (currentUser == null || !"RH".equalsIgnoreCase(currentUser.getUserType())) {
            System.out.println("Erreur : Seuls les RH peuvent ajouter une formation !");
            return;
        }


        String qry = "INSERT INTO `formation`( `nomFormation`, `descriptionFormation`, `dateDebutFormation`, `dateFinFormation` ) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getDescriptionFormation());
            pstm.setDate(3, formation.getDateDebutFormation());
            pstm.setDate(4, formation.getDateFinFormation());



            pstm.executeUpdate();
            System.out.println("Formation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Formation> getAll() {
        List<Formation> formations = new ArrayList<>();
        Map<Integer, Formation> formationMap = new HashMap<>(); // Utiliser une Map pour éviter les doublons

        String qry = "SELECT f.idFormation, f.nomFormation, f.descriptionFormation, f.dateDebutFormation, f.dateFinFormation, " +
                "c.titreCertif " +
                "FROM formation f " +
                "LEFT JOIN certification c ON f.idFormation = c.idFormation";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                int formationId = rs.getInt("idFormation");


                Formation formation = formationMap.get(formationId);
                if (formation == null) {
                    formation = new Formation();
                    formation.setIdFormation(formationId);
                    formation.setNomFormation(rs.getString("nomFormation"));
                    formation.setDescriptionFormation(rs.getString("descriptionFormation"));
                    formation.setDateDebutFormation(rs.getDate("dateDebutFormation"));
                    formation.setDateFinFormation(rs.getDate("dateFinFormation"));
                    formation.setCertifications(new ArrayList<>()); // Initialiser la liste des certifications

                    formationMap.put(formationId, formation);
                    formations.add(formation);
                }

                // Ajouter la certification si elle existe
                String titreCertif = rs.getString("titreCertif");
                if (titreCertif != null) {
                    Certification certification = new Certification();
                    certification.setTitreCertif(titreCertif);
                    formation.getCertifications().add(certification);
                }
            }

            rs.close();
            pstm.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des formations : " + e.getMessage());
        }

        return formations;
    }

    @Override
    public void update(Formation formation) {
        if (!isValidFormation(formation)) {
            System.out.println("Erreur : données invalides, update annulé !");
            return;
        }
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérification du rôle
        if (currentUser == null || !"RH".equalsIgnoreCase(currentUser.getUserType())) {
            System.out.println("Erreur : Seuls les RH peuvent modifier une formation !");
            return;
        }

        String qry = "UPDATE `formation` SET `nomFormation` = ?, `descriptionFormation` = ?, `dateDebutFormation` = ?, `dateFinFormation`= ? WHERE `idFormation` = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getDescriptionFormation());
            pstm.setDate(3, formation.getDateDebutFormation());
            pstm.setDate(4, formation.getDateFinFormation());


            pstm.setInt(5, formation.getIdFormation());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Formation mise à jour avec succès !");
            } else {
                System.out.println("Aucun enregistrement mis à jour. Vérifie l'ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }


    @Override
    public void delete(Formation formation) {

        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Vérification du rôle
        if (currentUser == null || !"RH".equalsIgnoreCase(currentUser.getUserType())) {
            System.out.println("Erreur : Seuls les RH peuvent supprimer une formation !");
            return;
        }
        String qry = "DELETE FROM formation WHERE idFormation = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, formation.getIdFormation());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Formation supprimée avec succès !");
            } else {
                System.out.println("Aucun enregistrement trouvé pour suppression.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }


    public static boolean isValidFormation(Formation formation) {
        if (formation.getNomFormation() == null || formation.getNomFormation().isEmpty()) {
            System.out.println("Le nom de la formation ne peut pas être vide !");
            return false;
        }
        if (formation.getDescriptionFormation() == null || formation.getDescriptionFormation().length() < 10) {
            System.out.println("La description doit contenir au moins 10 caractères !");
            return false;
        }
        if (formation.getDateDebutFormation() == null || formation.getDateFinFormation() == null) {
            System.out.println("Les dates de début et de fin ne peuvent pas être nulles !");
            return false;
        }
        if (formation.getDateFinFormation().before(formation.getDateDebutFormation())) {
            System.out.println("La date de fin doit être postérieure à la date de début !");
            return false;
        }


        return true;
    }
    public boolean isFormationExists(String nomFormation, String descriptionFormation) {
        String qry = "SELECT COUNT(*) FROM formation WHERE nomFormation = ? AND descriptionFormation = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, nomFormation);
            pstm.setString(2, descriptionFormation);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return false;
    }



    public List<Rh> getAllRH() {
        List<Rh> rhList = new ArrayList<>();
        String query = "SELECT * FROM rh"; // Table RH

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Rh rh = new Rh();
                rh.setRh_id(rs.getInt("idRH"));
                rh.setNom(rs.getString("nom"));
                rhList.add(rh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rhList;
    }

    public List<Certification> getAllCertifications() {
        List<Certification> certifications = new ArrayList<>();
        String query = "SELECT * FROM certification";

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Certification certif = new Certification();
                certif.setIdCertif(rs.getInt("idCertif"));
                certif.setTitreCertif(rs.getString("titreCertif"));
                certifications.add(certif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return certifications;
    }

}
