package tn.esprit.models;

import java.sql.Date;

public class EmployeFormation {
    private int employee_id;
    private int idFormation;
    private Date dateParticipation;

    public EmployeFormation() {

    }

    public EmployeFormation(int employee_id, int idFormation, Date dateParticipation) {
        this.employee_id = employee_id;
        this.idFormation = idFormation;
        this.dateParticipation = dateParticipation;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public Date getDateParticipation() {
        return dateParticipation;
    }

    public void setDateParticipation(Date dateParticipation) {
        this.dateParticipation = dateParticipation;
    }
}
