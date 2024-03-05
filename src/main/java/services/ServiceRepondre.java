package services;
import entities.reclamation;
import entities.repondre;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRepondre implements IService<repondre> {

    private final Connection connection;

    public ServiceRepondre() {
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
        String sql = "INSERT INTO repondre (titre, reponse,date_reponse, iduser, idreclamation) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql); // Méthode connect() à définir pour obtenir une connexion à la base de données
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rp.getTitre());
            pstmt.setString(2, rp.getReponse());
            pstmt.setObject(3, rp.getDate_reponse());
            pstmt.setInt(4, rp.getIduser());
            pstmt.setInt(5, rp.getIdreclamation());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(repondre rp) throws SQLException {
        String sql = "UPDATE repondre SET titre = ?, reponse = ?, date_reponse = ?, iduser = ?, idreclamation = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, rp.getTitre());
            ps.setString(2, rp.getReponse());
            ps.setDate(3,  Date.valueOf(rp.getDate_reponse())); // Adjust for actual date type
            ps.setInt(4, rp.getIduser());
            ps.setInt(5, rp.getIdreclamation());
            ps.setInt(6, rp.getId());
            ps.executeUpdate();
            System.out.println("Reply updated successfully");
        }
    }

    @Override
    public void delete(repondre rp) throws SQLException {
        String sql = "DELETE FROM repondre WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rp.getId());
            ps.executeUpdate();
            System.out.println("Reply deleted successfully");
        }
    }


    public List<repondre> showReponse() throws SQLException {
        List<repondre> replies = new ArrayList<>();
        String sql = "SELECT r.*, rec.titre AS reclamation_titre FROM repondre r " +
                "JOIN reclamation rec ON r.idreclamation = rec.id";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                repondre rp = new repondre();
                rp.setId(rs.getInt("id"));
                rp.setTitre(rs.getString("titre"));
                rp.setReponse(rs.getString("reponse"));
                rp.setDate_reponse(rs.getDate("date_reponse").toLocalDate());
                rp.setIduser(rs.getInt("iduser"));
                rp.setIdreclamation(rs.getInt("idreclamation"));
                replies.add(rp);
            }
        }
        return replies;
    }

    @Override
    public void ajouter(repondre repondre) throws SQLException {

    }

    @Override
    public void modifier(repondre repondre) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {

    }

    @Override
    public List<repondre> recuperer(String name) throws SQLException {
        return null;
    }
}
