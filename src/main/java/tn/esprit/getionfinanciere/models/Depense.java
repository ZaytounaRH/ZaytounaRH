package tn.esprit.getionfinanciere.models;

public class Depense {
    private int id;
    private double montantDepense;
    private String dateDepense;
    private String description;
    private int idBudget;

    // Constructeurs
    public Depense() {
    }

    public Depense(double montantDepense, String dateDepense, String description, int idBudget) {
        this.montantDepense = montantDepense;
        this.dateDepense = dateDepense;
        this.description = description;
        this.idBudget = idBudget;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontantDepense() {
        return montantDepense;
    }

    public void setMontantDepense(double montantDepense) {
        this.montantDepense = montantDepense;
    }

    public String getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(String dateDepense) {
        this.dateDepense = dateDepense;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(int idBudget) {
        this.idBudget = idBudget;
    }


    @Override
    public String toString() {
        return "Depense{" +
                "idDepense=" + id +
                ", montantDepense=" + montantDepense +
                ", dateDepense='" + dateDepense + '\'' +
                ", description='" + description + '\'' +
                ", idBudget=" + idBudget +
                "}\n";
    }
}
