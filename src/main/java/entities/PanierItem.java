package entities;

public class PanierItem {

    public int getIdUser() {
        return idUser;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    private int idUser;
    private int idProduit;

    public int getIdPanier() {
        return idPanier;
    }

    public void setIdPanier(int idPanier) {
        this.idPanier = idPanier;
    }

    private int idPanier;

    public int getPrixProduit() {
        return prixProduit;
    }

    public void setPrixProduit(int prixProduit) {
        this.prixProduit = prixProduit;
    }

    private int prixProduit;

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    private String nomProduit;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public PanierItem(Integer idUser, Integer idProduit) {
        this.idUser = idUser;
        this.idProduit = idProduit;
    }

    public PanierItem(Integer idUser, String nomProduit, int prixProduit) {
        this.idUser = idUser;
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
    }

    public PanierItem(Integer idUser, Integer idProduit, String nomProduit,int prixProduit) {
        this.idUser = idUser;
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
    }
    public PanierItem(Integer idUser, Integer idProduit, String nomProduit,int prixProduit,int idPanier) {
        this.idUser = idUser;
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
        this.idPanier = idPanier;
    }
    public PanierItem(Integer idUser, Integer idProduit, String nomProduit,int prixProduit,int idPanier,int status) {
        this.idUser = idUser;
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.prixProduit = prixProduit;
        this.idPanier = idPanier;
        this.status = status;
    }

}
