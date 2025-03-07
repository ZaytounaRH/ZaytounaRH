package tn.esprit.models;

import java.sql.Date;

public class Formation {
    private int id;
    private String titre;
    private Date date_debut;
    private Date date_fin;
    private int idRH;
    private int idEmploye;

    public Formation(int id,String titre, Date date_debut, Date date_fin, int idRH, int idEmploye) {
        this.id = id;
        this.titre = titre;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.idRH = idRH;
        this.idEmploye = idEmploye;
    }
    public Formation(String titre, Date date_debut, Date date_fin, int idRH, int idEmploye) {
        this.titre = titre;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.idRH = idRH;
        this.idEmploye = idEmploye;
    }
    public String getTitre() { return titre; }
    public Date getDate_debut() { return date_debut; }
    public Date getDate_fin() { return date_fin; }
    public int getIdRH() { return idRH; }
    public int getIdEmploye() { return idEmploye; }

    @Override
    public String toString() {
        return titre + date_debut + date_fin + idRH + idEmploye;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public void setIdRH(int idRH) {
        this.idRH = idRH;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }
}