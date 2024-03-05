package entities;

public class Exercice {
    private int id;
    private String nom;
    private String description;

    private String difficulte;
    private String duree;
    private String video;
    private int idprogramme;

    public Exercice(){

    }
    public Exercice(int id,String nom,String description,String duree,String difficulte,String video,int idprogramme){
        this.id=id;
        this.nom=nom;
        this.description=description;
        this.duree=duree;
        this.difficulte=difficulte;
        this.video=video;
        this.idprogramme=idprogramme;
    }
    public Exercice(String nom,String description,String duree,String difficulte,String video,int idprogramme){
        this.nom=nom;
        this.description=description;
        this.duree=duree;
        this.difficulte=difficulte;
        this.video=video;
        this.idprogramme=idprogramme;
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

    public String getDuree() {
        return duree;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getDescription() {
        return description;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(String difficulte) {
        this.difficulte = difficulte;
    }

    public int getIdprogramme() {
        return idprogramme;
    }

    public void setIdprogramme(int idprogramme) {
        this.idprogramme = idprogramme;
    }
}
