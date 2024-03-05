package services;

import entities.User;
import entities.reclamation;
import entities.repondre;
import io.jsonwebtoken.security.Keys;
import javafx.fxml.FXML;
import midlleware.TokenManager;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class UserService implements IService<User>{


    private Connection connection;
//
    public UserService(){
        connection = MyDatabase.getInstance().getConnection();
    }
    private static final SecretKey YOUR_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

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




    //
@Override
public void ajouter(User user) throws SQLException {
    String sql = "INSERT INTO user (nom, prenom, photoProfile, age, email, password, phoneNumber , isAdmin , isCoach) " +
            "VALUES (?, ?, ?, ?, ?, ?, ? , ? ,?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, user.getNom());
        preparedStatement.setString(2, user.getPrenom());
        preparedStatement.setString(3, user.getPhotoProfile());
        preparedStatement.setInt(4, user.getAge());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setString(6, user.getPassword());
        preparedStatement.setString(7, user.getPhoneNumber());
        preparedStatement.setInt(8, user.isAdmin());
        preparedStatement.setInt(9, user.isCoach());
        preparedStatement.executeUpdate();
    }
}


//    String token = TokenManager.generateJwtToken(user.getId(), user.getEmail(), user.isAdmin(), user.isCoach());
//                        TokenManager.saveToken(token);

//    @Override
public String authentifier(User user) throws SQLException {
    String sql = "SELECT idUser, email, isAdmin, isCoach, password FROM user WHERE email = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, user.getEmail());

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int idUser = resultSet.getInt("idUser");
                String email = resultSet.getString("email");
                int isAdmin = resultSet.getInt("isAdmin");
                int isCoach = resultSet.getInt("isCoach");
                String storedPasswordHash = resultSet.getString("password");

                if (passwordsMatch(user.getPassword(), storedPasswordHash)) {
                    String token = TokenManager.generateJwtToken(idUser, email, isAdmin, isCoach);
                    TokenManager.saveToken(token);
                    return token;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    } catch (SQLException e) {
        throw new SQLException("Error while authenticating user", e);
    }
}

    private boolean passwordsMatch(String providedPassword, String storedPasswordHash) {
        // Implement a secure password matching logic here, using a hashing library (e.g., BCrypt)
        // Example using BCrypt:
        // return BCrypt.checkpw(providedPassword, storedPasswordHash);
        return providedPassword.equals(storedPasswordHash);  // Replace this line with actual secure comparison
    }




    @Override
    public void modifier(User user) throws SQLException {
        String sql = "UPDATE user SET nom = ?, prenom = ?, email = ?, age = ?, phoneNumber = ?, password = ?, isAdmin = ?, isCoach = ?, photoProfile = ? WHERE idUser = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getAge());
            ps.setString(5, user.getPhoneNumber());
            ps.setString(6, user.getPassword());
            ps.setInt(7, user.isAdmin());
            ps.setInt(8, user.isCoach());
            ps.setString(9, user.getPhotoProfile());
            ps.setInt(10, user.getId());
            ps.executeUpdate();
        }
    }

    //
    @Override
    public void supprimer(int id) throws SQLException {
     String sql = "Delete from user where idUser = ? " ;
     PreparedStatement st = connection.prepareStatement(sql);
     st.setInt(1,id);
     st.executeUpdate();
    }



    public List<User> recuperer(String name) throws SQLException {
        String sql = "SELECT * FROM user WHERE nom LIKE '%" + name + "%'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<User> users = new ArrayList<>();
        while (rs.next()){
            User user = new User();
            user.setId(rs.getInt("idUser"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPhotoProfile(rs.getString("photoProfile"));
            user.setPhoneNumber(rs.getString("phoneNumber"));
            user.setAge(rs.getInt("age"));
            user.setCoach(rs.getInt("isCoach"));
            user.setAdmin(rs.getInt("isAdmin"));
            users.add(user);
        }
        return users;
    }


    public List<User> admins(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM user WHERE isAdmin = 1 AND nom LIKE '%" + searchTerm + "%'";
        List<User> users = new ArrayList<>();

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("idUser"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhotoProfile(rs.getString("photoProfile"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setAge(rs.getInt("age"));
                user.setCoach(rs.getInt("isCoach"));
                user.setAdmin(rs.getInt("isAdmin"));
                users.add(user);
            }
        }

        return users;
    }


    public List<User> coach(String searchTerm) throws SQLException {
        String sql = "SELECT * FROM user WHERE isCoach = 1 AND nom LIKE '%" + searchTerm + "%'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<User> users = new ArrayList<>();
        while (rs.next()){
            User user = new User();
            user.setId(rs.getInt("idUser"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPhotoProfile(rs.getString("photoProfile"));
            user.setPhoneNumber(rs.getString("phoneNumber"));
            user.setAge(rs.getInt("age"));
            user.setCoach(rs.getInt("isCoach"));
            user.setAdmin(rs.getInt("isAdmin"));
            users.add(user);
        }
        return users;
    }

    public User fetchUser(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE idUser = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("idUser"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhotoProfile(rs.getString("photoProfile"));
                    user.setPhoneNumber(rs.getString("phoneNumber"));
                    user.setAge(rs.getInt("age"));
                    user.setCoach(rs.getInt("isCoach"));
                    user.setAdmin(rs.getInt("isAdmin"));
                    return user;
                } else {
                    return null;
                }
            }
        }
    }

    public boolean fetchOneByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM user WHERE phoneNumber = ?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, phone);
        ResultSet resultSet = st.executeQuery();
        boolean userFound = resultSet.next();
        resultSet.close();
        st.close();
        return userFound;
    }

    public void insertOtpCode(String otp , String phoneNumber) throws SQLException {

            String sql = "Update user set  otp = ?  where phoneNumber = (?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, otp);
            st.setString(2, phoneNumber);
            st.executeUpdate();
    }

    public User fetchUserOtp(String phone) throws SQLException {
        String sql = "SELECT * FROM user WHERE phoneNumber = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, phone);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setOtp(rs.getString("otp"));
                    return user;
                } else {
                    return null;
                }
            }
        }
    }
    public void updatePassword (String newPassword , String phone) throws SQLException {
        String sql = "Update user set  password = ? , otp = NULL  where phoneNumber = (?)";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, newPassword);
        st.setString(2, phone);
        st.executeUpdate();
    }

}
