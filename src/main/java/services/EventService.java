package services;

import entities.Event;
import entities.Program;
import entities.reclamation;
import entities.repondre;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IService<Event>{
    private Connection connection;
    public EventService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(reclamation r) throws SQLException {

    }

    @Override
    public void update(reclamation r) throws SQLException {

    }

    @Override
    public void delete(reclamation reclamation) throws SQLException {

    }

    @Override
    public void add(repondre rp) throws SQLException {

    }

    @Override
    public void update(repondre rp) throws SQLException {

    }

    @Override
    public void delete(repondre rp) throws SQLException {

    }

    @Override
    public void ajouter(Event event) throws SQLException {
        String sql = "INSERT INTO event (nom, description, duree, type, date_debut, nb_participants, nb_max, localisation, status, program_id) " +
                "VALUES ('" + event.getNom() + "', '" + event.getDescription() + "', '" + event.getDuree() + "', '" + event.getType() + "', '" +
                event.getDate_debut() + "', '" + event.getNb_participants() + "', '" + event.getNb_max() + "', '" + event.getLocalisation() + "', '" +
                event.getStatus() + "', '" + event.getIdProgramme() + "')";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);



    }

    @Override
    public void modifier(Event event) throws SQLException {
        String sql = "UPDATE event set nom = ?,description = ?,duree = ?,type = ?,date_debut = ?,nb_max = ?, localisation = ?, status = ?, program_id = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, event.getNom());
        ps.setString(2, event.getDescription());
        ps.setString(3, event.getDuree());
        ps.setString(4, event.getType());
        ps.setDate(5, Date.valueOf((LocalDate) event.getDate_debut()));
        ps.setInt(6, event.getNb_max());
        ps.setString(7, event.getLocalisation());
        ps.setString(8, event.getStatus());
        ps.setInt(9, event.getIdProgramme());
        ps.setInt(10, event.getId());
        ps.executeUpdate();

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM event where id = ? ";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();

    }

    @Override
    public List<Event> recuperer(String name) throws SQLException {
        return null;
    }


    public List<Event> recuperer() throws SQLException {
        String sql = "select * from event";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Event> events = new ArrayList<>();

        while (rs.next()){
            Event ev = new Event();
            ev.setId(rs.getInt("id"));
            ev.setNom(rs.getString("nom"));
            ev.setDescription(rs.getString("description"));
            ev.setDuree(rs.getString("duree"));
            ev.setType(rs.getString("type"));
            ev.setDate_debut(rs.getDate("date_debut").toLocalDate());
            ev.setNb_participants(rs.getInt("nb_participants"));
            ev.setNb_max(rs.getInt("nb_max"));
            ev.setLocalisation(rs.getString("localisation"));
            ev.setStatus(rs.getString("status"));
            events.add(ev);
        }
        return events;
    }
    public List<Integer> recupererIDPROG() throws SQLException{
        String sql = "select id from program";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Integer> programs = new ArrayList<>();
        while (rs.next()){
            programs.add(rs.getInt("id"));
        }
        return programs;

    }
    public List<String> recupererNomPROG() throws SQLException{
        String sql = "select id from program";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<String> programs = new ArrayList<>();
        while (rs.next()){
            programs.add(rs.getString("nom"));
        }
        return programs;

    }
    public void updateParticipant(int id) throws SQLException{
        String selectSql = "SELECT * FROM event WHERE id = ?";
        PreparedStatement selectStatement = connection.prepareStatement(selectSql);
        selectStatement.setInt(1, id);
        ResultSet rs = selectStatement.executeQuery();

        // If the event exists, update nb_participants
        if (rs.next()) {
            int currentNbParticipants = rs.getInt("nb_participants");
            int newNbParticipants = currentNbParticipants + 1;

            // Update the event's nb_participants
            String updateSql = "UPDATE event SET nb_participants = ? WHERE id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setInt(1, newNbParticipants);
            updateStatement.setInt(2, id);
            updateStatement.executeUpdate();
        } else {
            System.out.println("Event not found for id: " + id);
        }

    }

    public List<Event> fetchParticipatedEvents(int userId) throws SQLException {
        List<Event> participatedEvents = new ArrayList<>();



        String query = "SELECT e.id, e.nom, e.description, e.duree " +
                "FROM participation p " +
                "JOIN event e ON p.event_id = e.id " +
                "WHERE p.user_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Event ev = new Event();
            ev.setId(resultSet.getInt("id"));
            ev.setNom(resultSet.getString("nom"));
            ev.setDescription(resultSet.getString("description"));
            ev.setDuree(resultSet.getString("duree"));
            participatedEvents.add(ev);
        }

        return participatedEvents;
    }

    }

