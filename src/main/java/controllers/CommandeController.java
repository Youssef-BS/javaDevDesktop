package controllers;

import entities.CommandeItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class CommandeController {



    @FXML
    private TableColumn<CommandeItem, String > lastnameColumn;

    @FXML
    private TableColumn<CommandeItem, Integer> statusColumn;
    @FXML
    private TableColumn<CommandeItem, Integer> phoneColumn;
    @FXML
    private TableColumn<CommandeItem, String> nameColumn;
    @FXML
    private TableColumn<CommandeItem, String> emailColumn;
    @FXML
    private TableColumn<CommandeItem, Integer> commandeIDColumn;
    @FXML
    private TableColumn<CommandeItem, Integer> totalcommandeColumn;
    @FXML
    private TableColumn<CommandeItem, Integer> idUserColumn;
    @FXML
    private TableView<CommandeItem> tableViews;


    @FXML
    private Button conf;
    @FXML
    private Button ref;
    @FXML
    private Button refr;
    @FXML
    private Button last;
    private int idCommande;
    private int idUser;
    private double Total;
    private String prenom;
    private String nom;




    private String getUserEmail(int userID) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String selectQuery = "SELECT email FROM user WHERE idUser = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, userID);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("email");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return null;
}

    public void sendmail(String email,String sub,String bd){
        final String username = "ahmet26chokri@gmail.com";
        final String password = "bezjagxfsdulzzry";

        String to = email;
        String subject = sub;
        String body =bd;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }





    public void initialize() {

        affiche(new ActionEvent());
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        commandeIDColumn.setCellValueFactory(new PropertyValueFactory<>("idCommande"));
        totalcommandeColumn.setCellValueFactory(new PropertyValueFactory<>("Total"));
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        setupTableViewSelectionListener();
    }

    private void setupTableViewSelectionListener() {
        tableViews.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                 idCommande = newSelection.getIdCommande();
                 idUser = newSelection.getIdUser();
                 Total = newSelection.getTotal();
                prenom = newSelection.getPrenom();
                nom = newSelection.getNom();
            }
        });
    }
    public void Confirm(ActionEvent actionEvent) {
        delete();
        affiche(new ActionEvent());
        showAlert("The order has been confirmed successfully");
        String userEmail=getUserEmail(idUser);
        String Subject="Confirmation of Your Order and Delivery Timeline";
        String Body="Dear "+prenom+" "+nom+",\nI hope this message finds you well.\n" +
                "We are pleased to inform you that your order is confirmed and on its way!\n" +
                "The total price for your order is " +
                Total+"DT.\n\nExpect delivery within 48 hours.\n\n\n"+
                "Best regards,\n" +
                "GYMSPHERE ©2024";
        sendmail(userEmail,Subject,Body);
    }
    public void refuse(ActionEvent actionEvent) {
        delete();
        affiche(new ActionEvent());
        showAlert("The order has been confirmed rejected");
        String userEmail=getUserEmail(idUser);
        String Subject="Order Refusal Notification";
        String Body="Dear "+prenom+" "+nom+",\n"+
                "We regret to inform you that we are unable to process your recent order at this time.\n" +
                "If you have any questions or concerns, please feel free to contact us. We appreciate your understanding.\n\n" +
                "\nBest regards,\n\n" +
                "GYMSPHERE ©2024";
        sendmail(userEmail,Subject,Body);

    }
    public void delete() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String updateQuery = "UPDATE commande SET commandeSt = 1 WHERE idCommande = ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, idCommande);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {


                } else {
                    showAlert("Unfortunately, there was an error");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void affiche(ActionEvent actionEvent) {
        List<CommandeItem> CommandeList = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String selectQuery = "SELECT commande.idCommande, commande.Total, commande.commandeSt, user.idUser, user.nom, user.prenom, user.email, user.phoneNumber " +
                    "FROM commande " +
                    "INNER JOIN user ON commande.idUser = user.idUser " +
                    "WHERE commande.commandeSt = 0";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    Integer idCommande = resultSet.getInt("idCommande");
                    Integer Total = resultSet.getInt("Total");
                    Integer status = resultSet.getInt("commandeSt");
                    Integer idUser = resultSet.getInt("idUser");
                    String name = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String email = resultSet.getString("email");
                    Integer phone = resultSet.getInt("phoneNumber");
                    CommandeList.add(new CommandeItem(idCommande,Total,status,idUser,name,prenom,email,phone));
                }
            }
        } catch (
                SQLException ex) {
            ex.printStackTrace();
        }
        tableViews.getItems().clear();
        tableViews.getItems().addAll(CommandeList);
    }


    public void ret(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/AdminProduct.fxml"));
            tableViews.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
