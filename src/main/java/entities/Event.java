package entities;

import java.time.LocalDate;

public class Event {
    private int id;
    private String nom;
    private String description;
    private String duree;
    private String type;
    private LocalDate date_debut;
    private int nb_participants;
    private int nb_max;
    private String localisation;
    private String status;
    private int idProgramme;
    public Event(){

    }
    public Event(int id, String nom, String description, String duree,String type,LocalDate date_debut,int nb_participants,int nb_max, String localisation, String status, int idProgramme){
        this.id = id;
        this.nom = nom;
        this.duree = duree;
        this.type = type;
        this.date_debut = date_debut;
        this.nb_participants = nb_participants;
        this.nb_max = nb_max;
        this.localisation = localisation;
        this.status = status;
        this.description = description;
        this.idProgramme = idProgramme;
    }
    public Event(String nom, String description, String duree,String type,LocalDate date_debut,int nb_participants,int nb_max, String localisation, String status, int idProgramme){
        this.nom = nom;
        this.duree = duree;
        this.type = type;
        this.date_debut = date_debut;
        this.nb_participants = nb_participants;
        this.nb_max = nb_max;
        this.localisation = localisation;
        this.status = status;
        this.description = description;
        this.idProgramme = idProgramme;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public LocalDate getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(LocalDate date_debut) {
        this.date_debut = date_debut;
    }

    public int getNb_participants() {
        return nb_participants;
    }

    public void setNb_participants(int nb_participants) {
        this.nb_participants = nb_participants;
    }

    public int getNb_max() {
        return nb_max;
    }

    public void setNb_max(int nb_max) {
        this.nb_max = nb_max;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdProgramme() {
        return idProgramme;
    }

    public void setIdProgramme(int idProgramme) {
        this.idProgramme = idProgramme;
    }
}
