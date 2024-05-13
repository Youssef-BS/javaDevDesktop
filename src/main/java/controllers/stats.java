package controllers;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


public class stats {

    @FXML
    public AnchorPane main;
    public Button updatephoto;
    @FXML
    private PieChart productStatsChart;
    @FXML
    private PieChart ventes;

    public void initialize() {
        populatePieChart();
        populateUserPieChart();

    }

    private void populatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi", "root", "")) {
            String query = "SELECT nom_produit, SUM(quantite_produit) AS totalQuantity FROM produit GROUP BY nom_produit";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String productName = resultSet.getString("nom_produit");
                    int totalQuantity = resultSet.getInt("totalQuantity");
                    String label = productName + " (" + totalQuantity + ")";
                    pieChartData.add(new PieChart.Data(label, totalQuantity));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }

        productStatsChart.setData(pieChartData);
    }
    private void populateUserPieChart() {
        ObservableList<PieChart.Data> userData = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi", "root", "")) {
            String query = "SELECT p.nom_produit, COUNT(*) AS count, pa.idProduit ,COUNT(*) AS totalQuantity " +
                    "FROM panier pa " +
                    "JOIN produit p ON pa.idProduit = p.idProduit " +
                    "WHERE pa.status = 1 "+
                    "GROUP BY pa.idProduit";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nomProduit = resultSet.getString("nom_produit");
                    int count = resultSet.getInt("count");
                    int totalQuantity = resultSet.getInt("totalQuantity");
                    String label = nomProduit + " (" + totalQuantity + ")";
                    userData.add(new PieChart.Data(label, count));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
        ventes.setData(userData);
    }


    public void ret(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/AdminProduct.fxml"));
            main.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
