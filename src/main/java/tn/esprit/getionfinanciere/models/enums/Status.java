package tn.esprit.getionfinanciere.models.enums;

public enum Status {

    PENDING("En attente"),
    COMPLETED("Validée"),
    CANCELED("Annulée");

    private String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

}
