package entities;

import java.time.LocalDate;

public class Program {
    private int id;
    private String nom;
    private String description;
    private String duree;
    private LocalDate registration_deadline;
    private float prix;
    private String imgsrc;
    public static String path;
    public Program(){
    }
    public Program(int id, String nom, String description, String duree, LocalDate registration_deadline, float prix, String imgsrc){
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.duree = duree;
        this.registration_deadline = registration_deadline;
        this.prix = prix;
        this.imgsrc = imgsrc;
    }
    public Program(String nom, String description, String duree, LocalDate registration_deadline, float prix, String imgsrc){
        this.nom = nom;
        this.description = description;
        this.duree = duree;
        this.registration_deadline = registration_deadline;
        this.prix = prix;
        this.imgsrc = imgsrc;
    }


    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getRegistration_deadline() {
        return registration_deadline;
    }

    public float getPrix() {
        return prix;
    }

    public String getDescription() {
        return description;
    }

    public String getDuree() {
        return duree;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setRegistration_deadline(LocalDate registration_deadline) {
        this.registration_deadline = registration_deadline;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
}
