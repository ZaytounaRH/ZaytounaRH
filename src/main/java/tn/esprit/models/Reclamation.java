package tn.esprit.models;

import java.time.LocalDate;

public class Reclamation {

    public enum StatutReclamation {
        EN_ATTENTE,
        EN_COURS,
        RESOLU
    }

    public enum PrioriteReclamation {
        FAIBLE,
        MOYENNE,
        ELEVEE
    }

    public enum IncidentType {
        ACCIDENT_TRAVAIL,
        MALADIE_PROFESSIONNELLE,
        DÉFAUT_COUVERTURE,
        LITIGE_CONTRAT
    }

    private int idR;
    private String titre;
    private String description;
    private IncidentType incidentType;
    private LocalDate dateSoumission;
    private StatutReclamation statut;
    private PrioriteReclamation priorite;
    private String pieceJointe;
    private Integer idAssurance; // index pour l'assurance liée

    // Constructeur par défaut
    public Reclamation() {
    }

    // Constructeur avec tous les attributs
    public Reclamation(String titre, String description, IncidentType incidentType, LocalDate dateSoumission,
                       StatutReclamation statut, PrioriteReclamation priorite, String pieceJointe, Integer idAssurance) {
        this.titre = titre;
        this.description = description;
        this.incidentType = incidentType;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.priorite = priorite;
        this.pieceJointe = pieceJointe;
        this.idAssurance = idAssurance;
    }

    // Constructeur avec id
    public Reclamation(int idR, String titre, String description, IncidentType incidentType, LocalDate dateSoumission,
                       StatutReclamation statut, PrioriteReclamation priorite, String pieceJointe, Integer idAssurance) {
        this.idR = idR;
        this.titre = titre;
        this.description = description;
        this.incidentType = incidentType;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.priorite = priorite;
        this.pieceJointe = pieceJointe;
        this.idAssurance = idAssurance;
    }

    // Getters et Setters
    public int getIdR() {
        return idR;
    }

    public void setIdR(int idR) {
        this.idR = idR;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IncidentType getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(IncidentType incidentType) {
        this.incidentType = incidentType;
    }

    public LocalDate getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDate dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public StatutReclamation getStatut() {
        return statut;
    }

    public void setStatut(StatutReclamation statut) {
        this.statut = statut;
    }

    public PrioriteReclamation getPriorite() {
        return priorite;
    }

    public void setPriorite(PrioriteReclamation priorite) {
        this.priorite = priorite;
    }

    public String getPieceJointe() {
        return pieceJointe;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public Integer getIdAssurance() {
        return idAssurance;
    }

    public void setIdAssurance(Integer idAssurance) {
        this.idAssurance = idAssurance;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "idR=" + idR +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", incidentType=" + incidentType +
                ", dateSoumission=" + dateSoumission +
                ", statut=" + statut +
                ", priorite=" + priorite +
                ", pieceJointe='" + pieceJointe + '\'' +
                ", idAssurance=" + idAssurance +
                '}';
    }
}
