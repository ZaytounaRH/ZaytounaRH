package tn.esprit.models;

import java.sql.Date;
import java.time.LocalDate;

public class Assurance {
    public enum TypeAssurance {
        SANTE,
        ACCIDENT_TRAVAIL,
        VIE,
        RESPONSABILITE_CIVILE
    }

    private int idA;
    private String nom;
    private TypeAssurance type; // Utilisation de l'enum pour le type d'assurance
    private LocalDate dateExpiration; // Utilisation de LocalDate pour la gestion de la date

    // Constructeur par défaut
    public Assurance() {
    }

    // Constructeur avec tous les attributs
    public Assurance(String nom, TypeAssurance type, LocalDate dateExpiration) {
        this.nom = nom;
        this.type = type;
        this.dateExpiration = dateExpiration;
    }

    // Constructeur avec id
    public Assurance(int idA, String nom, TypeAssurance type, LocalDate dateExpiration) {
        this.idA = idA;
        this.nom = nom;
        this.type = type;
        this.dateExpiration = dateExpiration;
    }

    // Getters et Setters
    public int getIdA() {
        return idA;
    }

    public void setIdA(int idA) {
        this.idA = idA;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeAssurance getType() {
        return type;
    }

    public void setType(TypeAssurance type) {
        this.type = type;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    @Override
    public String toString() {
        return "Assurance{" +
                "idA=" + idA +
                ", nom='" + nom + '\'' +
                ", type=" + type +
                ", dateExpiration=" + dateExpiration +
                '}';
    }
}


