package tn.esprit.models;



public class Certification {
    private int idCertif;
    private String titreCertif;
    private String organismeCertif;
    private Formation formation;
    public Certification() {

    }
    public Certification(int idCertif) {
        this.idCertif = idCertif;
    }
    public Certification(int idCertif, String titreCertif, String organismeCertif, Formation formation) {
        this.idCertif = idCertif;
        this.titreCertif = titreCertif;
       this.organismeCertif = organismeCertif;
       this.formation = formation;
    }

    public Certification( String titreCertif, String organismeCertif, Formation formation) {

        this.titreCertif = titreCertif;
        this.organismeCertif = organismeCertif;
        this.formation = formation;
    }

    public Certification(int idCertif, String titreCertif) {
        this.idCertif = idCertif;
        this.titreCertif = titreCertif;
    }

    public int getIdCertif() {
        return idCertif;
    }

    public void setIdCertif(int idCertif) {
        this.idCertif = idCertif;
    }

    public String getTitreCertif() {
        return titreCertif;
    }

    public void setTitreCertif(String titreCertif) {
        this.titreCertif = titreCertif;
    }

    public String getOrganismeCertif() {
        return organismeCertif;
    }

    public void setOrganismeCertif(String organismeCertif) {
        this.organismeCertif = organismeCertif;
    }
    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }
    @Override
    public String toString() {
        String formationNom = (formation != null) ? formation.getNomFormation() : "Aucune formation";

        return "Certification{ " +
                "Titre = " + titreCertif +
                ", Organisme = " + organismeCertif +
                ", Formation = " + formationNom +
                " }\n";
    }




}
