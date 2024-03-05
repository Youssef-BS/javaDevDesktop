package entities;



import java.time.LocalDate;

public class reclamation {
    private String description, titre, type;
    private int id, iduser;
    private LocalDate date_reclamation; // Removed the static keyword

    // Constructor with all fields
    public reclamation(String description, String titre, String type, LocalDate date_reclamation, int id, int iduser) {
        this.description = description;
        this.titre = titre;
        this.type = type;
        this.date_reclamation = date_reclamation; // Assign the passed date_reclamation
        this.id = id;
        this.iduser = iduser;
    }

    // Constructor without 'id' field
    public reclamation(String description, String titre, String type, LocalDate date_reclamation, int iduser) {
        this.description = description;
        this.titre = titre;
        this.type = type;
        this.date_reclamation = date_reclamation; // Assign the passed date_reclamation
        this.iduser = iduser;
    }

    // Default constructor
    public reclamation() {
    }



    // Getters and setters
    public String getDescription() {
        return description;
    }

    public String getTitre() {
        return titre;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getIduser() {
        return iduser;
    }

    public LocalDate getDate_reclamation() {
        return date_reclamation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate_reclamation(LocalDate date_reclamation) {
        this.date_reclamation = date_reclamation;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "reclamation{" +
                "description='" + description + '\'' +
                ", titre='" + titre + '\'' +
                ", type='" + type + '\'' +
                ", date_reclamation=" + date_reclamation +
                ", idReclamation=" + id +
                ", idUser=" + iduser +
                '}';
    }
}
