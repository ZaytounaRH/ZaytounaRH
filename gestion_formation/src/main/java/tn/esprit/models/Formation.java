package tn.esprit.models;

import java.sql.Date;
import java.util.List;

public class Formation {
    private int idFormation;
    private String nomFormation;
    private String descriptionFormation;
   private Date dateDebutFormation;
   private Date dateFinFormation;
    private List<Certification> certifications;
    public Formation() {
    }

    public Formation(int idFormation, String nomFormation, String descriptionFormation, Date dateDebutFormation,Date dateFinFormation, List<Certification> certifications) {
        this.idFormation = idFormation;
        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
       this.dateDebutFormation = dateDebutFormation;
       this.dateFinFormation = dateFinFormation;


        this.certifications = certifications;
    }
    public Formation(String nomFormation, String descriptionFormation, Date dateDebutFormation,Date dateFinFormation) {
        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
        this.dateDebutFormation = dateDebutFormation;
        this.dateFinFormation = dateFinFormation;

    }

    public Formation(String nomFormation, String descriptionFormation, Date dateDebutFormation,Date dateFinFormation, List<Certification> certifications) {
        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
        this.dateDebutFormation = dateDebutFormation;
        this.dateFinFormation = dateFinFormation;
        this.certifications = certifications;
    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public String getNomFormation() {
        return nomFormation;
    }

    public void setNomFormation(String nomFormation) {
        this.nomFormation = nomFormation;
    }

    public String getDescriptionFormation() {
        return descriptionFormation;
    }

    public void setDescriptionFormation(String descriptionFormation) {
        this.descriptionFormation = descriptionFormation;
    }

    public Date getDateDebutFormation() {
        return dateDebutFormation;
    }

    public void setDateDebutFormation(Date dateDebutFormation) {
        this.dateDebutFormation = dateDebutFormation;
    }

    public Date getDateFinFormation() {
        return dateFinFormation;
    }

    public void setDateFinFormation(Date dateFinFormation) {
        this.dateFinFormation = dateFinFormation;
    }
    public List<Certification> getCertifications() {
        return certifications;
    }
public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
}

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + idFormation +
                ", nom=" + nomFormation +
                ", description='" + descriptionFormation +
                ", dateDebut='" + dateDebutFormation+
                ", dateFin='" + dateFinFormation+
                ", certifications=" + certifications +
                "}\n";
    }


}
