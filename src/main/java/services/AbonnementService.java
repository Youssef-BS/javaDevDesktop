package services;

import entities.Abonnement;
import entities.reclamation;
import entities.repondre;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class AbonnementService implements IService<Abonnement>{

    private Connection connection;
    //
    public AbonnementService(){
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
    public void ajouter(Abonnement abn) throws SQLException {
        String sql = "INSERT INTO abonnement (dateDebut, dateFin, prix, payment, idUser) " +
                "VALUES (?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1,java.sql.Date.valueOf(LocalDate.now()) );
            LocalDate currentDatePlus30Days = LocalDate.now().plusDays(30);
            preparedStatement.setDate(2, java.sql.Date.valueOf(currentDatePlus30Days));
            preparedStatement.setDouble(3, abn.getPrix());
            preparedStatement.setBoolean(4, abn.isPayment());
            preparedStatement.setInt(5, abn.getIdUser());
//            preparedStatement.setInt(6 , abn.getIdGym());
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void modifier(Abonnement abn) throws SQLException {
        String sql = "UPDATE abonnement SET dateDebut = ?, dateFin = ?, prix = ?, idUser = ? WHERE idAbonnement = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1,java.sql.Date.valueOf(abn.getDateDebut()) );
            ps.setDate(2,java.sql.Date.valueOf(abn.getDateFin()) );
            ps.setDouble(3, abn.getPrix());
            ps.setInt(4, abn.getIdUser());
            ps.setInt(5, abn.getIdAbonnement());
            ps.executeUpdate();
        }
    }
    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM abonnement WHERE idAbonnement = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List recuperer(String name) throws SQLException {
        return null;
    }

    public List<Abonnement> subscribeAllSubscribes() throws SQLException {
        String sql = "SELECT * FROM abonnement " ;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Abonnement> abns = new ArrayList<>();
        while (rs.next()){
            Abonnement abn = new Abonnement();
            abn.setIdAbonnement(rs.getInt("idAbonnement"));
            abn.setPayment(rs.getBoolean("payment"));
            abn.setDateDebut(rs.getDate("dateDebut").toLocalDate());
            abn.setDateFin(rs.getDate("dateFin").toLocalDate());
            abn.setPrix(rs.getInt("prix"));
            abns.add(abn);
        }

        return abns;

    }

    public List<Abonnement> subscribeListforAnUser(int id) throws SQLException {
        String sql = "SELECT * FROM abonnement WHERE idUser = " + id;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Abonnement> abns = new ArrayList<>();
        while (rs.next()){
            Abonnement abn = new Abonnement();
            abn.setPayment(rs.getBoolean("payment"));
            abn.setDateDebut(rs.getDate("dateDebut").toLocalDate());
            abn.setDateFin(rs.getDate("dateFin").toLocalDate());
            abn.setPrix(rs.getInt("prix"));
            abn.setIdUser(rs.getInt("idUser"));
            abns.add(abn);
        }
        return abns;
    }
    public Abonnement fetchOneSubscribe(int id) throws SQLException {
        String sql = "SELECT * FROM abonnement WHERE idAbonnement = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Abonnement abonnement = new Abonnement();
                    abonnement.setDateDebut(rs.getDate("dateDebut").toLocalDate());
                    abonnement.setDateFin(rs.getDate("dateFin").toLocalDate());
                    abonnement.setPrix(rs.getInt("prix"));
                    abonnement.setIdUser(rs.getInt("idUser"));
                    return abonnement;
                } else {
                    return null;
                }
            }
        }
    }

}
