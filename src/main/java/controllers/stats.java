package controllers;
import java.io.IOException;

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
            String query = "SELECT nomProduit, SUM(quantiteProduit) AS totalQuantity FROM produit GROUP BY nomProduit";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String productName = resultSet.getString("nomProduit");
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
            String query = "SELECT p.nomproduit, COUNT(*) AS count, pa.idProduit ,COUNT(*) AS totalQuantity " +
                    "FROM panier pa " +
                    "JOIN produit p ON pa.idProduit = p.idProduit " +
                    "WHERE pa.status = 1 "+
                    "GROUP BY pa.idProduit";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nomProduit = resultSet.getString("nomProduit");
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







}
