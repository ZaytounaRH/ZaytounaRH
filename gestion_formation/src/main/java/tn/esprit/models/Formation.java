package tn.esprit.models;

public class Formation {
    private int idFormation;
    private String nomFormation;
    private String descriptionFormation;
    private String dureeFormation;
    private Employe employe; // Relation avec un employé
    private Rh rh; // Relation avec un RH
    private Certification certification; // Une seule certification par formation

    public Formation() {
    }

    public Formation(int idFormation, String nomFormation, String descriptionFormation, String dureeFormation, Employe employe, Rh rh, Certification certification) {
        this.idFormation = idFormation;
        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
        this.dureeFormation = dureeFormation;
        this.employe = employe;
        this.rh = rh;
        this.certification = certification;
    }

    public Formation(String nomFormation, String descriptionFormation, String dureeFormation, Employe employe, Rh rh, Certification certification) {
        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
        this.dureeFormation = dureeFormation;
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

    public String getDureeFormation() {
        return dureeFormation;
    }

    public void setDureeFormation(String dureeFormation) {
        this.dureeFormation = dureeFormation;
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
                ", duree='" + dureeFormation +
                ", employe=" + (employe != null ? employe.getIdEmploye() : "null") +
                ", rh=" + (rh != null ? rh.getIdRh() : "null") +
                ", certification=" + (certification != null ? certification.getTitreCertif() : "null") +
                "}\n";
    }
}
