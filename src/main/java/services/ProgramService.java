package services;

import entities.Program;
import entities.reclamation;
import entities.repondre;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProgramService implements IService<Program>{


    private Connection connection;

    public ProgramService(){
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
    public void ajouter(Program program) throws SQLException {
        String sql = "insert into program(nom,description,duree,registration_deadline,prix,imgsrc)"
                +"values(?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, program.getNom());
        ps.setString(2, program.getDescription());
        ps.setString(3, program.getDuree());
        ps.setDate(4, Date.valueOf((LocalDate) program.getRegistration_deadline()));
        ps.setDouble(5, program.getPrix());
        ps.setString(6,program.getImgsrc());
        ps.executeUpdate();
    }

    @Override
    public void modifier(Program program) throws SQLException {
        String sql = "UPDATE program set nom = ?,description = ?,duree = ?,registration_deadline = ?,prix = ?, imgsrc = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, program.getNom());
        ps.setString(2, program.getDescription());
        ps.setString(3, program.getDuree());
        ps.setDate(4, Date.valueOf((LocalDate) program.getRegistration_deadline()));
        ps.setDouble(5, program.getPrix());
        ps.setString(6, program.getImgsrc());
        ps.setInt(7, program.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM program where id = ? ";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Program> recuperer(String name) throws SQLException {
        return null;
    }


    public List<Program> recuperer() throws SQLException {
        String sql = "select * from program";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Program> programs = new ArrayList<>();

        while (rs.next()){
            Program pr = new Program();
            pr.setId(rs.getInt("id"));
            pr.setNom(rs.getString("nom"));
            pr.setDescription(rs.getString("description"));
            pr.setDuree(rs.getString("duree"));
            pr.setRegistration_deadline(rs.getDate("registration_deadline").toLocalDate());
            pr.setPrix(rs.getFloat("prix"));
            pr.setImgsrc(rs.getString("imgsrc"));
            programs.add(pr);
        }
        return programs;
    }

    public List<Program> recupererClientPrograms() throws SQLException {
        LocalDate currentDate = LocalDate.now();
        String sql = "select * from program WHERE registration_deadline >= CURRENT_DATE ";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Program> programs = new ArrayList<>();
        while (rs.next()){
            Program pr = new Program();
            pr.setId(rs.getInt("id"));
            pr.setNom(rs.getString("nom"));
            pr.setDescription(rs.getString("description"));
            pr.setDuree(rs.getString("duree"));
            pr.setRegistration_deadline(rs.getDate("registration_deadline").toLocalDate());
            pr.setPrix(rs.getFloat("prix"));
            pr.setImgsrc(rs.getString("imgsrc"));
            programs.add(pr);
        }
        return programs;
    }
    public List<Program> fetchSubscribedPrograms(int userId) throws SQLException {
        List<Program> subscribedPrograms = new ArrayList<>();



            String query = "SELECT p.id, p.nom, p.description, p.duree " +
                    "FROM inscription up " +
                    "JOIN program p ON up.program_id = p.id " +
                    "WHERE up.user_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Program pr = new Program();
                pr.setId(resultSet.getInt("id"));
                pr.setNom(resultSet.getString("nom"));
                pr.setDescription(resultSet.getString("description"));
                pr.setDuree(resultSet.getString("duree"));
                subscribedPrograms.add(pr);
            }

        return subscribedPrograms;
    }

}
