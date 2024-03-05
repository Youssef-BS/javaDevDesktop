package services;

import controllers.CalendarActivity;
import entities.Calendrier;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendrierService {


    private final Connection connection;

    public CalendrierService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Calendrier calendrier) throws SQLException {
        String sql = "INSERT INTO calandrier (date_activite, typeActivite, description, heureFermuture, idGym) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(calendrier.getDate()));
            preparedStatement.setString(2, calendrier.getTypeActivite());
            preparedStatement.setString(3, calendrier.getDescription());
            preparedStatement.setString(4, calendrier.getHeureFermeture());
            preparedStatement.setInt(5, calendrier.getIdqym());

            preparedStatement.executeUpdate();
        }
    }

    public void modifier(CalendarActivity calendrier) throws SQLException {
        String sql = "update calandrier set date_activite = ?, typeActivite = ?, description =?, heureFermuture =?, idGym =? where idCalandrier = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setDate(1, Date.valueOf(calendrier.getDate_activite()));
        ps.setString(2, calendrier.getTypeActivite());
        ps.setString(3, calendrier.getDescription());
        ps.setString(4, calendrier.getHeureFermeture());
        ps.setInt(5, calendrier.getIdqym());
        ps.setInt(6, calendrier.getIdcalandrier());
        ps.executeUpdate();
    }


    public void supprimer(int idCalendrier) throws SQLException {
        String sql = "delete from calandrier where idCalandrier = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,idCalendrier);
        ps.executeUpdate();
    }

    public List<CalendarActivity> recuperer() throws SQLException {
        List<CalendarActivity> calendriers = new ArrayList<>();
        String sql = "SELECT * FROM calandrier";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                CalendarActivity calendrier = new CalendarActivity();
                calendrier.setIdcalandrier(rs.getInt("idCalandrier")); // Check the column name
                calendrier.setTypeActivite(rs.getString("typeActivite"));
                calendrier.setDescription(rs.getString("description"));
                calendrier.setHeureFermeture(rs.getString("heureFermuture"));
                calendrier.setIdqym(rs.getInt("idGym"));
                calendriers.add(calendrier);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or print the exception
            throw e; // Rethrow the exception to signal the error
        }

        return calendriers;
    }

}


