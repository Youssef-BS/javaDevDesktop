package services;

import entities.Gym;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymService  {


    private final Connection connection;

    public GymService(){
        connection = MyDatabase.getInstance().getConnection();
    }


    public void ajouter(Gym gym) throws SQLException {
        String sql = "insert into gym (nomGym,adresse,photoGym) values('"+gym.getNomGym()+"', '"+gym.getAdresse()+"', '" +
                gym.getPhotoGym() +"')";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }


    public void modifier(Gym gym) throws SQLException {
        String sql = "update gym set nomGym = ?, adresse = ?where idGym = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, gym.getNomGym());
        ps.setString(2, gym.getAdresse());
        ps.setInt(3, gym.getIdGym());
        ps.executeUpdate();
    }


    public void supprimer(int idGym) throws SQLException {
        String sql = "delete from gym where idGym = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,idGym);
        ps.executeUpdate();
    }



    public  Gym afficherUnGym(int idGym) throws SQLException {
        String sql = "select * from gym where idGym=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idGym);
        ResultSet rs = ps.executeQuery();
        Gym p = null;

        if (rs.next()){
             p = new Gym();
            p.setIdGym(rs.getInt("idGym"));
            p.setNomGym(rs.getString("nomGym"));
            p.setAdresse(rs.getString("adresse"));
            p.setPhotoGym(rs.getString("photoGym"));

       }
        rs.close();
        ps.close();

        return p;

    }

    public List<Gym> recupererByAddresse(String adresse) throws SQLException {
        String sql = "select * from gym where adresse='"+adresse+"'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Gym> gyms = new ArrayList<>();
        while (rs.next()){
            Gym p = new Gym();
            p.setIdGym(rs.getInt("idGym"));
            p.setNomGym(rs.getString("nomGym"));
            p.setAdresse(rs.getString("adresse"));
            p.setPhotoGym(rs.getString("photoGym"));
            gyms.add(p);
        }
        return gyms;
    }

    public List<Gym> recuperer() throws SQLException {
        String sql = "select * from gym";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Gym> gyms = new ArrayList<>();

        while (rs.next()){
            Gym p = new Gym();
            p.setIdGym(rs.getInt("idGym"));
            p.setNomGym(rs.getString("nomGym"));
            p.setAdresse(rs.getString("adresse"));
            p.setPhotoGym(rs.getString("photoGym"));



            gyms.add(p);
        }
        return gyms;
    }
}


