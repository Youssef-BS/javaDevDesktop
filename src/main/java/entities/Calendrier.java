package entities;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class Calendrier {

    private ZonedDateTime dateSet;
    private int idcalandrier,idqym ;
    private String typeActivite,description ;
    private LocalDate date ;
    private String clientName;
    private Integer serviceNo;
    private String heureFermeture;

//    public Calendrier(int idqym, String typeActivite, String description, LocalDate date, String heureFermeture) {
//        this.idqym = idqym;
//        this.typeActivite = typeActivite;
//        this.description = description;
//        this.date = date;
//        this.heureFermeture = heureFermeture;
//    }
    public Calendrier( String typeActivite, String description, LocalDate date, String heureFermeture) {
        this.typeActivite = typeActivite;
        this.description = description;
        this.date = date;
        this.heureFermeture = heureFermeture;
    }
    public Calendrier(ZonedDateTime dateSet, String clientName, Integer serviceNo) {
        this.dateSet = dateSet;
        this.clientName = clientName;
        this.serviceNo = serviceNo;
    }
    public Calendrier(int idqym ,String typeActivite, String description, LocalDate date, String heureFermeture) {
        this.idqym = idqym ;
        this.typeActivite = typeActivite;
        this.description = description;
        this.date = date;
        this.heureFermeture = heureFermeture;
    }

//    public Calendrier (int idqym , String typeActivite , String description ,  LocalDate date , String heureFermeture){
//        this.idqym = idqym ;
//        this.typeActivite = typeActivite ;

    public ZonedDateTime getDateSet() {
        return dateSet;
    }

    public void setDateSet(ZonedDateTime dateSet) {
        this.dateSet = dateSet;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(Integer serviceNo) {
        this.serviceNo = serviceNo;
    }
//        this.description = description ;
//        this.date  = date ;
//        this.heureFermeture = heureFermeture ;
//    }

    public Calendrier(){}

    public int getIdcalandrier() {
        return idcalandrier;
    }

    public void setIdcalandrier(int idcalandrier) {
        this.idcalandrier = idcalandrier;
    }

    public int getIdqym() {
        return idqym;
    }

    public void setIdqym(int idqym) {
        this.idqym = idqym;
    }

    public String getTypeActivite() {
        return typeActivite;
    }

    public void setTypeActivite(String typeActivite) {
        this.typeActivite = typeActivite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHeureFermeture() {
        return heureFermeture;
    }

    public void setHeureFermeture(String heureFermeture) {
        this.heureFermeture = heureFermeture;
    }

    @Override
    public String toString() {
        return "Calendrier{" +
                "idcalandrier=" + idcalandrier +
                ", idqym=" + idqym +
                ", typeActivite='" + typeActivite + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", heureFermeture=" + heureFermeture +
                '}';
    }
}
