package controllers;

import services.WelcomeService;
import utils.MyDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML
    private Button admin;
    @FXML
    private ComboBox<Integer> userID;
    WelcomeService welcomeService = new WelcomeService();

    @FXML
    private Button client;

    @FXML
    void admin(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GestionProgram.fxml"));
            admin.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void client(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/ProgClient.fxml"));
            Parent parent = load.load();
            ProgClientController pcC = load.getController();
            pcC.setIdUserClient(userID.getValue());
            admin.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {


            userID.setItems(FXCollections.observableList(welcomeService.recupererIDPUser()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
