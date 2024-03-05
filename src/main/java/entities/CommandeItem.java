
package entities;


public class CommandeItem {

    private int idCommande;
    private int Total;
    private int idUser;

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }
    public double getTotal()
    {
        return Total;
    }


    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String nom;
    private String email;

    public int getcommandeSt() {
        return commandeSt;
    }

    public void setcommandeSt(int commandeSt) {
        this.commandeSt = commandeSt;
    }

    private int commandeSt;

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    private String prenom;

    public CommandeItem(int idCommande, int total, Integer commandeSt, int idUser, String nom, String prenom, String email, Integer phoneNumber) {
        this.idCommande = idCommande;
        this.Total = total;
        this.commandeSt = commandeSt;
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.phoneNumber = phoneNumber;

    }

    private Integer phoneNumber;

    public int getIdCommande() {
        return idCommande;
    }


    public int getIdUser() {
        return idUser;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public CommandeItem(int idCommande, int total, int idUser) {
        this.idCommande = idCommande;
        this.Total = total;
        this.idUser = idUser;
    }


}
