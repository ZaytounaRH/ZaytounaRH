package tn.esprit.models;

public class Formation {
    private int idFormation;
    private String nomFormation;
    private String descriptionFormation;
    private String dureeFormation;
    private int idEmploye;
    private int idRH;


    public Formation() {

    }

    public Formation(int idFormation, String nomFormation, String descriptionFormation, String dureeFormation, int idEmploye, int idRH) {
        this.idFormation = idFormation;
        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
        this.dureeFormation = dureeFormation;
        this.idEmploye = idEmploye;
        this.idRH = idRH;
    }

    public Formation(String nomFormation, String descriptionFormation, String dureeFormation, int idEmploye, int idRH) {

        this.nomFormation = nomFormation;
        this.descriptionFormation = descriptionFormation;
        this.dureeFormation = dureeFormation;
        this.idEmploye = idEmploye;
        this.idRH = idRH;
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

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public int getIdRH() {
        return idRH;
    }

    public void setIdRH(int idRH) {
        this.idRH = idRH;
    }
    @Override
    public String toString() {
        return "Formation{" +
                "id=" + idFormation +
                ", nom=" + nomFormation +
                ", description='" + descriptionFormation +
                ", duree ='" + dureeFormation  +
                ", idEmploye=" + idEmploye+
                ", idRH=" + idRH +
                "}\n";
    }

}
