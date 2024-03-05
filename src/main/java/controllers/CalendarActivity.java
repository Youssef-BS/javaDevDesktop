package controllers;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Random;

public class CalendarActivity {

    private ZonedDateTime dateSet;
    private int idcalandrier,idqym ;
    private String typeActivite,description ;
    private LocalDate dateActivite ;
    private String clientName;
    private Integer serviceNo;
    private String heureFermeture;

    //    public Calendrier(int idqym, String typeActivite, String description, LocalDate , String heureFermeture) {
//        this.idqym = idqym;
//        this.typeActivite = typeActivite;
//        this.description = description;
//        this dateActivite = dateActivite;
//        this.heureFermeture = heureFermeture;
//    }
    public CalendarActivity( String typeActivite, String description, LocalDate dateActivite, String heureFermeture) {
        this.typeActivite = typeActivite;
        this.description = description;
        this.dateActivite = (dateActivite == null) ? LocalDate.now().withDayOfMonth(1) : dateActivite;

        // Ensure the day is between 1 and 30
        int year = 2024;
        int month = 3;

        // Set a random day between 1 and 30 for the given year and month
        int randomDay = new Random().nextInt(30) + 1;

        this.dateActivite = LocalDate.of(year, month, randomDay);
        this.heureFermeture = heureFermeture;
    }
    public CalendarActivity(ZonedDateTime dateSet, String clientName, Integer serviceNo) {
        this.dateSet = dateSet;
        this.clientName = clientName;
        this.serviceNo = serviceNo;
    }
    public CalendarActivity(int idqym ,String typeActivite, String description, LocalDate dateActivite, String heureFermeture) {
        this.idqym = idqym ;
        this.typeActivite = typeActivite;
        this.description = description;
        int year = 2024;
        int month = 3;

        // Set a random day between 1 and 30 for the given year and month
        int randomDay = new Random().nextInt(30) + 1;

        this.dateActivite = LocalDate.of(year, month, randomDay);
        this.heureFermeture = heureFermeture;
    }

//    public Calendrier (int idqym , String typeActivite , String description ,  LocalDate dateActivite , String heureFermeture){
//        this.idqym = idqym ;
//        this.typeActivite = typeActivite ;

    public ZonedDateTime getDateSet() {
        return dateSet;
    }

    public void setDateSet(ZonedDateTime dateSet) {
       this.dateSet = dateSet ;
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
//        this dateActivite  = dateActivite ;
//        this.heureFermeture = heureFermeture ;
//    }

    public CalendarActivity(){}

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

    public LocalDate getDate_activite() {
        return dateActivite;
    }

    public void setDate_activite(LocalDate dateActivite) {
        int year = 2024;
        int month = 3;

        // Set a random day between 1 and 30 for the given year and month
        int randomDay = new Random().nextInt(30) + 1;

        this.dateActivite = LocalDate.of(year, month, randomDay);
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
                ", dateActivite=" + dateActivite +
                ", heureFermeture=" + heureFermeture +
                '}';
    }
}
