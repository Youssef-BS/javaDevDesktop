package entities;

public class Gym {
    private static int idGym;
    private String nomGym,adresse,photoGym;

    public Gym(int idGym, String nomGym, String adresse, String photoGym) {
        this.idGym = idGym;
        this.nomGym = nomGym;
        this.adresse = adresse;
        this.photoGym = photoGym;
    }
    public Gym( String nomGym, String adresse, String photoGym) {
        this.nomGym = nomGym;
        this.adresse = adresse;
        this.photoGym = photoGym;
    }
    public Gym(int idGym , String nomGym, String adresse) {
        this.idGym = idGym ;
        this.nomGym = nomGym;
        this.adresse = adresse;

    }

    public Gym() {
    }

    public static int getIdGym() {
        return idGym;
    }

    public void setIdGym(int idGym) {
        this.idGym = idGym;
    }

    public String getNomGym() {
        return nomGym;
    }

    public void setNomGym(String nomGym) {
        this.nomGym = nomGym;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPhotoGym() {
        return photoGym;
    }

    public void setPhotoGym(String photoGym) {
        this.photoGym = photoGym;
    }

    @Override
    public String toString() {
        return "gym{" +
                "idGym=" + idGym +
                ", nomGym='" + nomGym + '\'' +
                ", adresse='" + adresse + '\'' +
                ", photoGym='" + photoGym + '\'' +
                '}';
    }


}

