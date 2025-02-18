package tn.esprit.models;

import java.sql.Date;

public class Certification {
    private int idCertif;
    private String titreCertif;
    private String organismeCertif;

    public Certification() {

    }
    public Certification(int idCertif) {
        this.idCertif = idCertif;
    }
    public Certification(int idCertif, String titreCertif, String organismeCertif) {
        this.idCertif = idCertif;
        this.titreCertif = titreCertif;
       this.organismeCertif = organismeCertif;
    }

    public Certification( String titreCertif, String organismeCertif) {

        this.titreCertif = titreCertif;
        this.organismeCertif = organismeCertif;
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
    @Override
    public String toString() {
        return "Certification{" +
                "id=" + idCertif +
                ", titre=" + titreCertif +
                ", organisme='" + organismeCertif +
                "}\n";
    }


}
