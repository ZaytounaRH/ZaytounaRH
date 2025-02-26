package tn.esprit.models;

import java.sql.Date;

public class EmployeCertification {
    private int idCertif;
    private int employee_id;
    private Date dateObtention;

    public EmployeCertification() {
    }

    public EmployeCertification(int idCertif, int employee_id, Date dateObtention) {
        this.idCertif = idCertif;
        this.employee_id = employee_id;
        this.dateObtention = dateObtention;
    }

    public int getIdCertif() {
        return idCertif;
    }

    public void setIdCertif(int idCertif) {
        this.idCertif = idCertif;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public Date getDateObtention() {
        return dateObtention;
    }

    public void setDateObtention(Date dateObtention) {
        this.dateObtention = dateObtention;
    }
}
