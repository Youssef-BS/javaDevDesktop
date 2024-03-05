package controllers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import entities.ProductController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;

public class AdminController {



    @FXML
    private TableView<ProductController> tableViews;

    @FXML
    private TableColumn<ProductController, String> productNameColumn;
    @FXML
    private TableColumn<ProductController, String> photoColumn;
    @FXML
    private TableColumn<ProductController, Integer> productIDColumn;
    @FXML
    private TableColumn<ProductController, Integer>productpriceColumn;

    @FXML
    private TableColumn<ProductController, Integer>productquantColumn;

    @FXML
    private TableColumn<ProductController, Image> imageprod;

    private String productName;
    private int productPrice;
    private String photopath;
    private int productQuantity;

    @FXML
    private Label welcomeText;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField description;
    @FXML
    private TextField prix;
    @FXML
    private ChoiceBox choice;
    @FXML
    private ImageView imageshow;
    @FXML
    private ImageView productview;
    @FXML
    private TextField name;
    @FXML
    private TextField stock;
    @FXML
    private TextField price;
    @FXML
    private Button importButton;
    @FXML
    private Button confirm;
    @FXML
    private Button yess;
    @FXML
    private AnchorPane anchoradd;
    @FXML
    private AnchorPane anchorup;
    @FXML
    private Button last;
    @FXML
    private Button update;
    @FXML
    private Button comm;
    @FXML
    private Button updatephoto;
    @FXML
    private Button conf;
    @FXML
    private Button del;
    @FXML
    private Button button;

    @FXML
    private ChoiceBox<String> coachChoiceBox;
    private final String URL = "jdbc:mysql://localhost:3306/pi";
    private final String USER = "root";
    private final String PASS = "";
    private Connection connection;
    private File selectedFile;

    private int idproduct;
    private double newPrices;
    public AdminController() {
        try {

            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    private int getExistingProductId(String productName, int productPrice, String photoHash) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi", "root", "")) {
            String selectQuery = "SELECT idProduit FROM produit WHERE nomProduit = ? AND prixProduit = ? AND photoProduit = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, productName);
                preparedStatement.setDouble(2, productPrice);
                preparedStatement.setString(3, photoHash);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("idProduit");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
        return -1;
    }
    private void updateProductQuantity(int productId,String Query) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi", "root", "")) {
            String updateQuery = Query;
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, productId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void confirm(ActionEvent event) {
        String descr = description.getText();
        String price = prix.getText();
        String Queryadd = "UPDATE produit SET quantiteProduit = quantiteProduit + 1 WHERE idProduit = ?";
        int quan=1;
        if (descr.isEmpty() || selectedFile == null) {
            showAlert("Please enter a product name and select a photo.");
            return;
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi", "root", "")) {
            String photoPath = selectedFile.getAbsolutePath();
            int existingProductId = getExistingProductId(descr, Integer.parseInt(price), photoPath);
            if (existingProductId != -1) {
                updateProductQuantity(existingProductId, Queryadd);
                affiche(new ActionEvent());
                showAlert("Product quantity updated successfully!");
            }
            else{
            String insertQuery = "INSERT INTO produit (nomProduit, prixProduit, photoProduit, quantiteProduit) VALUES (?, ?, ?, ?)";

            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, descr);
                insertStatement.setString(2, price);
                insertStatement.setString(3, photoPath);
                insertStatement.setInt(4, quan);
                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    affiche(new ActionEvent());
                    showAlert("Product added successfully!");

                } else {
                    showAlert("Unfortunately, there was an error");
                }
            }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            showAlert("Please enter a valid price.");
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error: " + ex.getMessage());
        }
    }


    @FXML
    private void deleteProduct(ActionEvent actionEvent) {
        Integer idprod = idproduct;
        String nameprod = productName;
        int prixprod = productPrice;
        String photoprd = photopath;
        String Queryadd = "UPDATE produit SET quantiteProduit = quantiteProduit - 1 WHERE idProduit = ?";
        int existingProductId = getExistingProductId(nameprod, prixprod, photoprd);
        if (existingProductId != -1) {
            updateProductQuantity(existingProductId, Queryadd);
            affiche(new ActionEvent());
            showAlert("Product quantity updated successfully!");
        }
        else{

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi", "root", "")) {
            String deleteQuery = "DELETE FROM produit WHERE idProduit = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, idprod);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    affiche(new ActionEvent());
                    showAlert("The product has been deleted successfully!");
                } else {
                    showAlert("Unfortunately, there was an error");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
    }}

    public void initialize() {
        setupTableViewSelectionListener();
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productpriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photoPath"));
        productquantColumn.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        photoColumn.setCellFactory(param -> new TableCell<ProductController, String>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    imageView.setImage(null);
                    setGraphic(null);
                } else {
                    Image image = new Image(new File(item).toURI().toString());
                    imageView.setImage(image);
                    imageView.setFitWidth(180);
                    imageView.setFitHeight(200);
                    setGraphic(imageView);
                }
            }
        });
        affiche(new ActionEvent());

    }

    private void setupTableViewSelectionListener() {
        tableViews.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                productName = newSelection.getProductName();
                productPrice = newSelection.getProductPrice();
                idproduct = newSelection.getProductID();
                photopath = newSelection.getPhotoPath();
                productQuantity= newSelection.getProductQuantity();
                name.setText(productName);
                price.setText(Double.toString(productPrice));
                stock.setText(Integer.toString(productQuantity));
                try {
                    Image image = new Image(new File(photopath).toURI().toString());
                    imageshow.setImage(image);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error loading image: " + e.getMessage());
                }
            }
        });
    }
    public void affiche(ActionEvent actionEvent) {

        List<ProductController> productList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi", "root", "")) {
            String selectQuery = "SELECT idProduit, nomProduit, prixProduit, photoProduit , quantiteProduit FROM produit";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    String productName = resultSet.getString("nomProduit");
                    Integer productID = resultSet.getInt("idProduit");
                    Integer productPrice = resultSet.getInt("prixProduit");
                    String photoPath  = resultSet.getString("photoProduit");
                    Integer productQuantity = resultSet.getInt("quantiteProduit");
                    productList.add(new ProductController(productID,productName,productPrice,photoPath,productQuantity));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tableViews.getItems().clear();
        tableViews.getItems().addAll(productList);
    }



    public void imports(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                selectedFile = file;
                FileInputStream fis = new FileInputStream(file);
                Image image = new Image(fis);
                productview.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void importphoto(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                selectedFile = file;
                FileInputStream fis = new FileInputStream(file);
                Image image = new Image(fis);
                imageshow.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }}






    public void updater(ActionEvent actionEvent) {
        Integer idprod = idproduct;
        String newName = name.getText();
        String newPrice = price.getText();
        String newQuantity = stock.getText();
        String newPhotoPath = (selectedFile != null) ? selectedFile.getAbsolutePath() : null;
        String url = "jdbc:mysql://localhost:3306/pi";
        String username = "root";
        String password = "";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            StringBuilder sqlBuilder = new StringBuilder("UPDATE produit SET ");
            ArrayList<Object> parameters = new ArrayList<>();
            if (newName != null && !newName.isEmpty()) {
                sqlBuilder.append("nomProduit = ?, ");
                parameters.add(newName);
            }
            if (newPrice != null && !newPrice.isEmpty()) {
                sqlBuilder.append("prixProduit = ?, ");
                parameters.add(newPrice);
            }
            if (newPhotoPath != null && !newPhotoPath.isEmpty()) {
                sqlBuilder.append("photoProduit = ?, ");
                parameters.add(newPhotoPath);
            }
            if (newQuantity != null && !newQuantity.isEmpty()) {
                sqlBuilder.append("quantiteProduit = ?, ");
                parameters.add(newQuantity);
            }
            sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
            sqlBuilder.append(" WHERE idProduit = ?");
            parameters.add(idprod);
            PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                affiche(new ActionEvent());
                showAlert("The product has been updated successfully!");
            } else {
                showAlert("Unfortunately, there was an error");
            }
        } catch (SQLException e) {
            System.err.println("Error updating information: " + e.getMessage());
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void makesolde(double newPrices) {
        Integer idprod = idproduct;
        String newName = name.getText();

        String newPhotoPath = (selectedFile != null) ? selectedFile.getAbsolutePath() : null;


        String url = "jdbc:mysql://localhost:3306/pi";
        String username = "root";
        String password = "";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            StringBuilder sqlBuilder = new StringBuilder("UPDATE produit SET ");
            ArrayList<Object> parameters = new ArrayList<>();
            if (newName != null && !newName.isEmpty()) {
                sqlBuilder.append("nomProduit = ?, ");
                parameters.add(newName);
            }

                sqlBuilder.append("prixProduit = ?, ");
                parameters.add(newPrices);

            if (newPhotoPath != null && !newPhotoPath.isEmpty()) {
                sqlBuilder.append("photoProduit = ?, ");
                parameters.add(newPhotoPath);
            }
            sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
            sqlBuilder.append(" WHERE idProduit = ?");
            parameters.add(idprod);
            PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("The product has been updated successfully!");
            } else {
                showAlert("Unfortunately, there was an error");
            }
        } catch (SQLException e) {
            System.err.println("Error updating information: " + e.getMessage());
        }
    }

    public void bigsolde(double pourcentage) {
        String newPrice = price.getText();
        double updatedOldPrice = Double.parseDouble(newPrice);
        newPrices = updatedOldPrice*pourcentage;
        makesolde(newPrices);
        affiche(new ActionEvent());
    }
    public void solde50(ActionEvent actionEvent) {
        bigsolde(0.5);

    }

    public void solde40(ActionEvent actionEvent) {
        bigsolde(0.6);
    }

    public void solde30(ActionEvent actionEvent) {
        bigsolde(0.7);
    }

    public void solde20(ActionEvent actionEvent) {
        bigsolde(0.8);
    }

    public void solde10(ActionEvent actionEvent) {
        bigsolde(0.9);
    }



}

