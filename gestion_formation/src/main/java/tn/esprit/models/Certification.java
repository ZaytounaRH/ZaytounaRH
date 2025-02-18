package tn.esprit.models;

import java.sql.Date;

public class Certification {
    private int idCertif;
    private String titreCertif;
    private Date dateCertif;
    private int idFormation;

    public Certification() {

    }
    public Certification(int idCertif) {
        this.idCertif = idCertif;
    }
    public Certification(int idCertif, String titreCertif, Date dateCertif, int idFormation) {
        this.idCertif = idCertif;
        this.titreCertif = titreCertif;
        this.dateCertif = dateCertif;
        this.idFormation = idFormation;
    }

    public Certification( String titreCertif, Date dateCertif, int idFormation) {

        this.titreCertif = titreCertif;
        this.dateCertif = dateCertif;
        this.idFormation = idFormation;
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

    public Date getDateCertif() {
        return dateCertif;
    }

    public void setDateCertif(Date dateCertif) {
        this.dateCertif = dateCertif;
    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }
    @Override
    public String toString() {
        return "Certification{" +
                "id=" + idCertif +
                ", titre=" + titreCertif +
                ", date='" + dateCertif +
                ", idFormation=" + idFormation+

                "}\n";
    }
}
