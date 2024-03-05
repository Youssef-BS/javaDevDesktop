package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;

public class Home {

    @FXML
    private ToggleGroup claim;
    @FXML
    private RadioButton gyms;
    @FXML
    private RadioButton products;

    @FXML
    public void initialize() {

        claim = new ToggleGroup();
        gyms.setToggleGroup(claim);
        products.setToggleGroup(claim);
        gyms.setSelected(true);
    }


   }
