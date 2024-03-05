package entities;
import  java.time.LocalDate ;

public class Abonnement {
    public int idAbonnement , prix , idUser , idGym ;
    public LocalDate dateDebut , dateFin ;
    public boolean payment ;

    public Abonnement(){

    }

    public Abonnement(int idAbonnement, int prix, int idUser, int idGym, LocalDate dateDebut, LocalDate dateFin , boolean payment) {
        this.idAbonnement = idAbonnement;
        this.prix = prix;
        this.idUser = idUser;
        this.idGym = idGym;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.payment = payment ;
    }
    public Abonnement(int prix ,int idUser, int idGym, LocalDate dateDebut, LocalDate dateFin , boolean payment) {
        this.prix = prix;
        this.idUser = idUser;
        this.idGym = idGym;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.payment = payment ;
    }

    public Abonnement(int prix, LocalDate dateDebut, LocalDate dateFin , boolean payment) {
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.payment = payment ;
    }

    public Abonnement(int prix, boolean payment ,  int idUser , int idGym ) {
        this.prix = prix;
        this.idUser = idUser;
        this.idGym = idGym;
        this.payment = payment ;
    }

    public  Abonnement (LocalDate dateDebut , LocalDate dateFin , int prix , int idUser){
        this.dateDebut = dateDebut ;
        this.dateFin = dateFin ;
        this.prix = prix ;
        this.idUser = idUser ;
    }

    public  Abonnement (LocalDate dateDebut , LocalDate dateFin , int prix , int idUser , int idAbonnement){
        this.dateDebut = dateDebut ;
        this.dateFin = dateFin ;
        this.prix = prix ;
        this.idUser = idUser ;
        this.idAbonnement = idAbonnement ;
    }


    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public Abonnement(int prix, int idUser, int idGym, LocalDate dateDebut, LocalDate dateFin) {
        this.prix = prix;
        this.idUser = idUser;
        this.idGym = idGym;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "idAbonnement=" + idAbonnement +
                ", prix=" + prix +
                ", idUser=" + idUser +
                ", idGym=" + idGym +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }

    public  int getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdGym() {
        return idGym;
    }

    public void setIdGym(int idGym) {
        this.idGym = idGym;
    }

    public LocalDate getDateDebut() {
        return  dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return  dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
}
