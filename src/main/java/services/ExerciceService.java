package services;

import entities.Exercice;
import entities.Program;
import entities.reclamation;
import entities.repondre;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExerciceService implements IService<Exercice> {
    private Connection connection;
    public ExerciceService(){connection = MyDatabase.getInstance().getConnection();
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
    public void ajouter(Exercice exercice) throws SQLException {
        String sql = "insert into exercice(nom,description,duree,difficulte,video,idprogramme)"
                +"values(?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, exercice.getNom());
        ps.setString(2, exercice.getDescription());
        ps.setString(3, exercice.getDuree());
        ps.setString(4, exercice.getDifficulte());
        ps.setString(5,exercice.getVideo());
        ps.setInt(6,exercice.getIdprogramme());
        ps.executeUpdate();

    }

    @Override
    public void modifier(Exercice exercice) throws SQLException {
        String sql = "UPDATE exercice set nom = ?,description = ?,duree = ?,difficulte = ?,video = ?, idprogramme = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, exercice.getNom());
        ps.setString(2, exercice.getDescription());
        ps.setString(3, exercice.getDuree());
        ps.setString(4, exercice.getDifficulte());
        ps.setString(5,exercice.getVideo());
        ps.setInt(6,exercice.getIdprogramme());
        ps.setInt(7,exercice.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM exercice where id = ? ";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Exercice> recuperer(String name) throws SQLException {
        return null;
    }


    public List<Exercice> recuperer() throws SQLException {
        String sql = "select * from exercice";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Exercice> exercices = new ArrayList<>();

        while (rs.next()){
            Exercice pr = new Exercice();
            pr.setId(rs.getInt("id"));
            pr.setNom(rs.getString("nom"));
            pr.setDescription(rs.getString("description"));
            pr.setDuree(rs.getString("duree"));
            pr.setDifficulte(rs.getString("difficulte"));
            pr.setVideo(rs.getString("video"));
            pr.setIdprogramme(rs.getInt("idprogramme"));
            exercices.add(pr);
        }
        return exercices;
    }
    public List<Exercice> recuperer(int id) throws SQLException {
        String sql = "select * from exercice where idprogramme = "+String.valueOf(id);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Exercice> exercices = new ArrayList<>();

        while (rs.next()){
            Exercice pr = new Exercice();
            pr.setId(rs.getInt("id"));
            pr.setNom(rs.getString("nom"));
            pr.setDescription(rs.getString("description"));
            pr.setDuree(rs.getString("duree"));
            pr.setDifficulte(rs.getString("difficulte"));
            pr.setVideo(rs.getString("video"));
            pr.setIdprogramme(rs.getInt("idprogramme"));
            exercices.add(pr);
        }
        return exercices;
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
    public List<Exercice> triNom() throws SQLException {
        String sql = "select * from exercice order by nom ";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Exercice> exercices = new ArrayList<>();

        while (rs.next()){
            Exercice pr = new Exercice();
            pr.setId(rs.getInt("id"));
            pr.setNom(rs.getString("nom"));
            pr.setDescription(rs.getString("description"));
            pr.setDuree(rs.getString("duree"));
            pr.setDifficulte(rs.getString("difficulte"));
            pr.setVideo(rs.getString("video"));
            pr.setIdprogramme(rs.getInt("idprogramme"));
            exercices.add(pr);
        }
        return exercices;
    }
    public List<Exercice> triId() throws SQLException {
        String sql = "select * from exercice order by nom ";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Exercice> exercices = new ArrayList<>();

        while (rs.next()){
            Exercice pr = new Exercice();
            pr.setId(rs.getInt("id"));
            pr.setNom(rs.getString("nom"));
            pr.setDescription(rs.getString("description"));
            pr.setDuree(rs.getString("duree"));
            pr.setDifficulte(rs.getString("difficulte"));
            pr.setVideo(rs.getString("video"));
            pr.setIdprogramme(rs.getInt("idprogramme"));
            exercices.add(pr);
        }
        return exercices;
    }
}
