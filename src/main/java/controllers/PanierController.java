package controllers;

import entities.PanierItem;
import entities.ProductController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierController {

    @FXML
    private TableView<PanierItem> tableViews;
    @FXML
    private TableColumn<ProductController, Integer> idUserColumn;
    @FXML
    private TableColumn<ProductController, Integer> idProduitColumn;

    public void initialize() {
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        idProduitColumn.setCellValueFactory(new PropertyValueFactory<>("idProduit"));

        affiche(new ActionEvent());

    }
    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void affiche(ActionEvent actionEvent) {
        List<PanierItem> panierList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String selectQuery = "SELECT idUser, idProduit FROM panier";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    Integer idUser = resultSet.getInt("idUser");
                    Integer idProduit = resultSet.getInt("idProduit");

                    panierList.add(new PanierItem(idUser,idProduit));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tableViews.getItems().clear();
        tableViews.getItems().addAll(panierList);
    }


    public void last(ActionEvent actionEvent) {
    }
}
