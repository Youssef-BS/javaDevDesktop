package controllers;

import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RegisterControllers {

    @FXML
    private TextField fisrtNameFx ;

    @FXML
    private TextField lastNameFx ;
    @FXML
    private TextField photoFx ;
    @FXML
    private TextField emailFx;
    @FXML
    private PasswordField passwordFx;
    @FXML
    private PasswordField passwordFx2;
    @FXML
    private TextField ageFx;
    @FXML
    private TextField phoneFx;


    private final UserService userService = new UserService();

    @FXML
    void ajouter(ActionEvent event) {
        try {
            String firstName = fisrtNameFx.getText();
            String lastName = lastNameFx.getText();
            String photo = photoFx.getText();
            String email = emailFx.getText();
            String password = passwordFx.getText();
            String confirmPassword = passwordFx2.getText();
            int age = Integer.parseInt(ageFx.getText());
            String phone = phoneFx.getText();

// Validate First Name and Last Name (Assuming they should not be empty)
            if (firstName.isEmpty() || lastName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setContentText("Name and last name Require !");
                alert.showAndWait();
                return;
            }

// Validate Email (Assuming a basic email format check)
            if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setContentText("Email must be contains @ .  and write correctly");
                alert.showAndWait();
                return;
            }



            if (!password.equals(confirmPassword)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Passwords do not match");
                alert.showAndWait();
                return;
            }
            User newUser = new User(firstName, lastName, age , email, photo , password, phone);
            userService.ajouter(newUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("User added successfully");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void naviguer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            fisrtNameFx.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}