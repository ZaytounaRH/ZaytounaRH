package tn.esprit.models;

import java.sql.Date;

public class Formation {
    private int idFormation;
    private String nomFormation;
    private String descriptionFormation;
   private Date dateDebutFormation;
   private Date dateFinFormation;
    private Employe employe;
    private Rh rh;
    private Certification certification;

    public Formation() {
    }

    public Formation(int idFormation, String nomFormation, String descriptionFormation, Date dateDebutFormation,Date dateFinFormation, Employe employe, Rh rh, Certification certification) {
        this.idFormation = idFormation;
        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
       this.dateDebutFormation = dateDebutFormation;
       this.dateFinFormation = dateFinFormation;
        this.employe = employe;
        this.rh = rh;
        this.certification = certification;
    }

    public Formation(String nomFormation, String descriptionFormation, Date dateDebutFormation,Date dateFinFormation, Employe employe, Rh rh, Certification certification) {
        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
        this.dateDebutFormation = dateDebutFormation;
        this.dateFinFormation = dateFinFormation;
        this.employe = employe;
        this.rh = rh;
        this.certification = certification;
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

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Rh getRh() {
        return rh;
    }

    public void setRh(Rh rh) {
        this.rh = rh;
    }

    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + idFormation +
                ", nom=" + nomFormation +
                ", description='" + descriptionFormation +
                ", dateDebut='" + dateDebutFormation+
                ", dateFin='" + dateFinFormation+
                ", employe=" + (employe != null ? employe.getNom() : "null") +
                ", rh=" + (rh != null ? rh.getNom() : "null") +
                ", certification=" + (certification != null ? certification.getTitreCertif() : "null") +
                "}\n";
    }


}
