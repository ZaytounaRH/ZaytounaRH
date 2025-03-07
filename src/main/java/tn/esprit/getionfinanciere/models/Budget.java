package tn.esprit.getionfinanciere.models;

public class Budget {
    private int id;
    private double montantAlloue;
    private String dateDebut;
    private String dateFin;
    private String typeBudget;
    private int idResponsable;

    public Budget() {
    }

    public Budget(double montantAlloue, String dateDebut, String dateFin, String typeBudget, int idResponsable) {
        this.montantAlloue = montantAlloue;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.typeBudget = typeBudget;
        this.idResponsable = idResponsable;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontantAlloue() {
        return montantAlloue;
    }

    public void setMontantAlloue(double montantAlloue) {
        this.montantAlloue = montantAlloue;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getTypeBudget() {
        return typeBudget;
    }

    public void setTypeBudget(String typeBudget) {
        this.typeBudget = typeBudget;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }


    @Override
    public String toString() {
        return "Budget{" +
                "idBudget=" + id +
                ", montantAlloue=" + montantAlloue +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", typeBudget='" + typeBudget + '\'' +
                ", idResponsable=" + idResponsable +
                "}\n";
    }

}
