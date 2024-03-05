package services;

import utils.MyDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WelcomeService {
    Connection connection;
    public WelcomeService(){
        connection = MyDatabase.getInstance().getConnection();
    }
    public List<Integer> recupererIDPUser() throws SQLException {
        String sql = "select idUser from user";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Integer> programs = new ArrayList<>();
        while (rs.next()){
            programs.add(rs.getInt("idUser"));
        }
        return programs;

    }
}
