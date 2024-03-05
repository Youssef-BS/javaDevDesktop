package services;



import entities.reclamation;
import entities.repondre;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class ServiceReclamation implements IService<reclamation> {

    private final Connection connection;

    public ServiceReclamation() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(reclamation r) throws SQLException {
        String sql = "INSERT INTO reclamation (description, type, titre, date_reclamation, iduser) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, r.getDescription());
            ps.setString(2, r.getType());
            ps.setString(3, r.getTitre());

            // Assuming getDateReclamation() returns a LocalDate
            // Convert LocalDate to java.sql.Date
            if (r.getDate_reclamation() != null) {
                ps.setDate(4, Date.valueOf(r.getDate_reclamation()));
            } else {
                ps.setNull(4, Types.DATE); // Handle null date case
            }

            ps.setInt(5, r.getIduser()); // Assuming getIduser() correctly returns the user's ID
            ps.executeUpdate();
            System.out.println("Reclamation added successfully");
        }
    }



    @Override
    public void update(reclamation r) throws SQLException {
        String sql = "UPDATE reclamation SET description = ?, titre = ?, type = ?, date_reclamation = ?, iduser = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, r.getDescription());
            ps.setString(2, r.getTitre());
            ps.setString(3, r.getType());
            // Convert LocalDate to java.sql.Date
//            if (r.getDate_reclamation() != null) {
                ps.setDate(4, Date.valueOf(r.getDate_reclamation()));
//            } else {
//                ps.setNull(4, java.sql.Types.DATE); // Handle potential null date case
//            }
            ps.setInt(5, r.getIduser());
            ps.setInt(6, r.getId()); // WHERE clause to match the record by ID
            ps.executeUpdate();
            System.out.println("Reclamation updated successfully");
        }
    }


    @Override
    public void delete(reclamation reclamation) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id = ?"; // Changed from idReclamation to id
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, reclamation.getId()); // Use the updated parameter name
            ps.executeUpdate();
            System.out.println("Reclamation deleted successfully");
        }

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



    public List<reclamation> showReclamation() throws SQLException {
        String sql = "SELECT * FROM reclamation";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<reclamation> reclamations = new ArrayList<>();
            while (rs.next()) {
                reclamation r = new reclamation();
                r.setDescription(rs.getString("description"));
                r.setTitre(rs.getString("titre"));
                r.setType(rs.getString("type"));
                r.setId(rs.getInt("id"));
                r.setIduser(rs.getInt("iduser"));

                // Correctly convert the SQL DATE to LocalDate
                Date date = rs.getDate("date_reclamation");
                if (date != null) {
                    r.setDate_reclamation(date.toLocalDate());
                }

                reclamations.add(r);
            }
            return reclamations;
        }
    }


    public static List<Integer> fetchUserIds() throws SQLException {
        List<Integer> userIds = new ArrayList<>();
        // Utiliser MyDatabase pour établir une connexion
        Connection connection = MyDatabase.getInstance().getConnection();

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            String query = "SELECT DISTINCT idUser FROM user";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                userIds.add(resultSet.getInt("iduser"));
            }
        } finally {
            // Assurer la fermeture des ressources dans un bloc finally pour éviter les fuites de ressources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Ou une autre forme de logging
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Ou une autre forme de logging
                }
            }
            // Note: La connexion n'est pas fermée ici car elle est gérée par MyDatabase.getInstance()
        }

        return userIds;
    }







    public List<reclamation> getUnansweredReclamations() throws SQLException {
        List<reclamation> unAnsweredReclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation WHERE id NOT IN (SELECT idreclamation FROM repondre)";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reclamation rec = new reclamation();
                // Remplissez l'objet rec à partir de resultSet
                rec.setId(resultSet.getInt("id"));
                rec.setDescription(resultSet.getString("description"));
                rec.setTitre(resultSet.getString("titre"));
                rec.setType(resultSet.getString("type"));
                rec.setDate_reclamation(resultSet.getDate("date_reclamation").toLocalDate());
                rec.setIduser(resultSet.getInt("iduser"));
                unAnsweredReclamations.add(rec);
            }
        }
        return unAnsweredReclamations;
    }

    public List<reclamation> getAnsweredReclamations() throws SQLException {
        List<reclamation> answeredReclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation WHERE id IN (SELECT idreclamation FROM repondre)";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reclamation rec = new reclamation();
                // Remplissez l'objet rec à partir de resultSet
                rec.setId(resultSet.getInt("id"));
                rec.setDescription(resultSet.getString("description"));
                rec.setTitre(resultSet.getString("titre"));
                rec.setType(resultSet.getString("type"));
                rec.setDate_reclamation(resultSet.getDate("date_reclamation").toLocalDate());
                rec.setIduser(resultSet.getInt("iduser"));
                answeredReclamations.add(rec);
            }
        }
        return answeredReclamations;
    }


    @Override
    public void ajouter(reclamation reclamation) throws SQLException {

    }

    @Override
    public void modifier(reclamation reclamation) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {

    }

    @Override
    public List<reclamation> recuperer(String name) throws SQLException {
        return null;
    }
}
