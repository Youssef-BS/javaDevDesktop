package entities;

import java.time.LocalDate;

public class repondre {
    private int id;
    private String titre;
    private String reponse;
    private LocalDate date_reponse; // Assuming date_reponse is of type Date
    private int iduser;
    private int idreclamation;

      private LocalDate date_reclamation;

    // Constructor with all attributes
    public repondre(int id, String titre, String reponse, LocalDate date_reponse, int iduser, int idreclamation) {
        this.id = id;
        this.titre = titre;
        this.reponse = reponse;
        this.date_reponse = date_reponse;
        this.iduser = iduser;
        this.idreclamation = idreclamation;
    }

    // Constructor without id (useful if id is auto-generated)
    public repondre(String titre, String reponse, LocalDate date_reponse, int iduser, int idreclamation) {
        this.titre = titre;
        this.reponse = reponse;
        this.date_reponse = date_reponse;
        this.iduser = iduser;
        this.idreclamation = idreclamation;
    }

    // Default constructor
    public repondre() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getReponse() {
        return reponse;
    }

    public LocalDate getDate_reponse() {
        return date_reponse;
    }

    public int getIduser() {
        return iduser;
    }

    public int getIdreclamation() {
        return idreclamation;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public void setDate_reponse(LocalDate date_reponse) {
        this.date_reponse = date_reponse;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public void setIdreclamation(int idreclamation) {
        this.idreclamation = idreclamation;
    }

    @Override
    public String toString() {
        return "repondre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", reponse='" + reponse + '\'' +
                ", date_reponse=" + date_reponse +
                ", iduser=" + iduser +
                ", idreclamation=" + idreclamation +
                '}';
    }


    public LocalDate getDate_reclamation() {
        return null;
    }
}
