package controllers;
import entities.ProductController;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import entities.PanierItem;

import controllers.ProductItemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import midlleware.TokenManager;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientController {


    public int row = 0;
    public int col = 0;
    private int status;
    private int idPanier;
    private int idProduit;
    private double totalcommande;
    double total=0.0;
    @FXML
    public Label totalLabel;
    @FXML
    public GridPane gridPanes;
    @FXML
    public ChoiceBox choiceid;
    @FXML
    private Button commande;
    @FXML
    private Button deleteprod;
    @FXML
    private Button refpanier;
    @FXML
    private Button add;
    @FXML
    private Button filtre;
    @FXML
    private Button last;
    @FXML
    private AnchorPane anchor;
    @FXML
    private ChoiceBox choiceprod;
    @FXML
    private TextField name;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<ProductController, String> productNameColumn;
    @FXML
    private TableColumn<ProductController, String> photoColumn;
    @FXML
    private TableColumn<ProductController, Integer>productpriceColumn;

    @FXML
    private RadioButton prixabove50;
    @FXML
    private RadioButton prixbelow50;
    @FXML
    private RadioButton all;
    @FXML
    private TableView<PanierItem> tableViews;
    @FXML
    private TableColumn<ProductController, Integer> idUserColumn;
    @FXML
    private TableColumn<ProductController, Integer> idProduitColumn;
    @FXML
    private TableColumn<ProductController, String> nomProduitColumn;
    @FXML
    private TableColumn<ProductController, String>prixProduitColumn;
    @FXML
    private TableColumn<ProductController, String>idPanierColumn;


    public void affiche(ActionEvent actionEvent) {
        gridPanes.getChildren().clear();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String selectQuery = "SELECT DISTINCT nom_produit, prix_produit, photo_produit FROM produit WHERE quantite_produit > 0";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = selectStatement.executeQuery()) {

                while (resultSet.next()) {
                    String productName = resultSet.getString("nom_produit");
                    String photoPath = resultSet.getString("photo_produit");
                    Integer productPrice = resultSet.getInt("prix_produit");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductItem.fxml"));
                    GridPane productItem = loader.load();
                    ProductItemController controller = loader.getController();
                    controller.setProduct(new ProductController(productName, productPrice, photoPath));
                    gridPanes.add(productItem, col, row);
                    col++;
                    if (col == 3 ) {
                        col = 0;
                        row++;
                    }
                }
                if(col%3!=0){
                    row++;
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }



    public void initialize() {
        setupTableViewSelectionListener();
        idPanierColumn.setCellValueFactory(new PropertyValueFactory<>("idPanier"));
        nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        idProduitColumn.setCellValueFactory(new PropertyValueFactory<>("idProduit"));
        prixProduitColumn.setCellValueFactory(new PropertyValueFactory<>("prixProduit"));
        ColumnConstraints columnConstraints = new ColumnConstraints();
        RowConstraints rowConstraints = new RowConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setMaxWidth(350.0);
        columnConstraints.setMinWidth(265.0);
        columnConstraints.setPrefWidth(350.0);
        rowConstraints.setVgrow(Priority.SOMETIMES);
        rowConstraints.setMinHeight(310.0);
        rowConstraints.setMaxHeight(500.0);
        rowConstraints.setPrefHeight(150.0);
        affiche(new ActionEvent());
            gridPanes.getColumnConstraints().addAll(columnConstraints, columnConstraints, columnConstraints);

        if(row%3!=0){
            row++;}
        System.out.println(row);
            for(int i=0;i<row;i++) {
            gridPanes.getRowConstraints().addAll(rowConstraints);
        }
    }


    public void searchProduct() {
        gridPanes.getChildren().clear();
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
                String selectQuery;
                if (prixabove50.isSelected()) {
                    selectQuery = "SELECT distinct nom_produit, prix_produit, photo_produit FROM produit WHERE prix_produit >= 50";
                }
                else if (prixbelow50.isSelected()){
                    selectQuery = "SELECT distinct nom_produit, prix_produit, photo_produit FROM produit WHERE prix_produit < 50";
                }
                else {
                    selectQuery = "SELECT distinct nom_produit, prix_produit, photo_produit FROM produit";
                }
                try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                     ResultSet resultSet = selectStatement.executeQuery()) {
                    int row = 0;
                    int col = 0;
                        while (resultSet.next()) {
                            String productName = resultSet.getString("nom_produit");
                            String photoPath = resultSet.getString("photo_produit");
                            Integer productPrice = resultSet.getInt("prix_produit");
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductItem.fxml"));
                            GridPane productItem = loader.load();
                            ProductItemController controller = loader.getController();
                            controller.setProduct(new ProductController(productName, productPrice, photoPath));
                            gridPanes.add(productItem, col, row);

                            col++;
                            if (col == 3) {
                                col = 0;
                                row++;
                            }
                        }
                }
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
    }


    public void searchproduct(ActionEvent actionEvent) {
        searchProduct();
    }

    @FXML
    private void idChoiceBox() {
        ObservableList<Integer> userId = FXCollections.observableArrayList();


        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String query = "SELECT idUser FROM user";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("idUser");
                    userId.add(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
        choiceid.setItems(userId);
    }


    @FXML
    private void productChoiceBox() {
        ObservableList<String> productName = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String query = "SELECT DISTINCT nom_produit FROM produit";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nomProduit = resultSet.getString("nom_produit");
                    productName.add(nomProduit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }

        choiceprod.setItems(productName);
    }
    private int getProductQuantity(int productId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String selectQuery = "SELECT quantite_produit FROM produit WHERE idProduit = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, productId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("quantite_produit");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
        return -1;
    }
    private int getExistingProductId(Integer idProduit) {

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String selectQuery = "SELECT idProduit FROM produit WHERE idProduit = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, idProduit);


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
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
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
    @FXML
    private void deleteProduct(ActionEvent actionEvent) {
        Integer userid = userId;

        String Queryadd = "UPDATE produit SET quantite_produit = quantite_produit + 1 WHERE idProduit = ?";
        if (userid != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
                String query = "SELECT idProduit FROM produit WHERE idProduit = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, idProduit);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int existingProductId = getExistingProductId(idProduit);
                            if (existingProductId != -1) {
                                updateProductQuantity(existingProductId, Queryadd);
                                /*showAlert("Product quantity updated successfully!");*/
                                String deletequery = "DELETE FROM panier WHERE idUser = ? AND idPanier = ?";
                                try (PreparedStatement insertStatement = connection.prepareStatement(deletequery)) {
                                    insertStatement.setInt(1, userid);
                                    insertStatement.setInt(2,idPanier);
                                    insertStatement.executeUpdate();
                                    showAlert("Product deleted from the basket successfully!");
                                    affichepa(new ActionEvent());

                                }}

                        } else {
                            System.out.println("Product not found.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Database error: " + e.getMessage());
            }
        } else {
            System.out.println("Please select both a user and a product.");
        }
    }
    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void addproduct(ActionEvent actionEvent) {
        String Queryadd = "UPDATE produit SET quantite_produit = quantite_produit - 1 WHERE idProduit = ?";
        //Integer userId = (Integer) choiceid.getValue();
        String productName = choiceprod.getValue().toString();
        if ( productName != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
                String query = "SELECT idProduit FROM produit WHERE nom_produit = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, productName);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int productId = resultSet.getInt("idProduit");
                            int existingProductId = getExistingProductId(productId);
                            int productQuantity = getProductQuantity(productId);
                            if (existingProductId != -1) {
                                if(productQuantity<1){
                                    showAlert("this product is empty try again later");
                                }
                                else{
                                updateProductQuantity(existingProductId, Queryadd);
                                /*showAlert("Product quantity updated successfully!");*/

                                String insretquery = "INSERT INTO panier (idProduit, idUser)  VALUES (?, ?)";
                                try (PreparedStatement insertStatement = connection.prepareStatement(insretquery)) {
                                    insertStatement.setInt(1, productId);
                                    insertStatement.setInt(2, userId);
                                    insertStatement.executeUpdate();
                                    showAlert("Product added to the basket successfully!");
                                    affichepa(new ActionEvent());
                                }}
                            }
                        } else {
                            System.out.println("Product not found.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Database error: " + e.getMessage());
            }
        } else {
            System.out.println("Please select both a user and a product.");
        }

    }

    public void affichepanier(Integer userId) {
        List<PanierItem> panierList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String selectQuery = "SELECT panier.idUser,panier.idPanier, produit.idProduit, produit.nom_produit, produit.prix_produit, panier.status " +
                    "FROM panier " +
                    "INNER JOIN produit ON panier.idProduit = produit.idProduit " +
                    "WHERE panier.idUser = ? AND panier.status = 0";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, userId);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer idUser = resultSet.getInt("idUser");
                        Integer idProduit = resultSet.getInt("idProduit");
                        String nomProduit = resultSet.getString("nom_produit");
                        Integer prixProduit = resultSet.getInt("prix_produit");
                        Integer idPanier = resultSet.getInt("idPanier");
                        Integer status = resultSet.getInt("status");
                        total+=prixProduit;
                        totalcommande=total;
                        panierList.add(new PanierItem(idUser, idProduit, nomProduit,prixProduit,idPanier));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tableViews.getItems().clear();
        tableViews.getItems().addAll(panierList);
        totalLabel.setText("Total purchases: "+total+"DT");
        total=0;
    }

    public void affichepa(ActionEvent actionEvent) {
        //Integer selecteduserId = (Integer) choiceid.getValue();

        if (userId != 0) {
            affichepanier(userId);
        } else {
            System.out.println("Please select a product ID to delete.");
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupTableViewSelectionListener() {
        tableViews.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idPanier = newSelection.getIdPanier();
                idProduit = newSelection.getIdProduit();

            }
        });
    }

    public void confirmcommande(ActionEvent actionEvent) {
        Integer userid = userId;

        if (userid != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
                String insertQuery = "INSERT INTO commande (Total, idUser) VALUES (?,?)";
                String updateQuery = "UPDATE panier SET status=1 WHERE idUser = ? ";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                     PreparedStatement updatePanierStatement = connection.prepareStatement(updateQuery))
                     {
                    preparedStatement.setDouble(1,totalcommande );
                    preparedStatement.setInt(2, userId);
                    int rowsAffectedCommande = preparedStatement.executeUpdate();
                    updatePanierStatement.setInt(1, userId);
                         pdfex(new ActionEvent());
                    int rowsAffectedPanier = updatePanierStatement.executeUpdate();
                    if (rowsAffectedCommande > 0 && rowsAffectedPanier > 0) {
                        showAlert("Items added to command successfully!");

                        affichepa(new ActionEvent());


                    } else {
                        showAlert("No items found in panier for the selected user.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Database error: " + e.getMessage());
            }
        } else {
            System.out.println("Please select a user.");
        }
}


    public void pdfex(ActionEvent actionEvent) {
        Integer userid = userId;
        String pdfFilePath = "panier_data.pdf";

        // Establish database connection
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pifinal", "root", "")) {
            String selectQuery = "SELECT produit.nom_produit, produit.prix_produit, panier.idUser FROM panier INNER JOIN produit ON panier.idProduit = produit.idProduit WHERE panier.idUser = ? AND  panier.status = 0";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                // Set the value of the parameter marker (?)
                statement.setInt(1, userid);
                try (PDDocument document = new PDDocument()) {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                        contentStream.setNonStrokingColor(Color.RED);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(250, 750);
                        contentStream.showText("Cash Receipt");
                        contentStream.endText();
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.setNonStrokingColor(Color.BLACK);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(260, 500);
                        contentStream.showText("THANK YOU!");
                        contentStream.endText();
                        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                        float margin = 50;
                        float yStart = page.getMediaBox().getHeight() - margin;
                        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                        float bottomMargin = 70;
                        float yPosition = yStart;
                        int rowsPerPage = 25;
                        int rowCount = 0;
                        boolean newPage = false;

                        String[] headers = {"Produit", "Prix"};
                        float rowHeight = 20;
                        float cellMargin = 5;
                        float[] columnWidths = {200, 100};
                        drawTableHeader(contentStream, yPosition, rowHeight, cellMargin, tableWidth, headers, columnWidths);
                        yPosition -= rowHeight;

                        double totalSum = 0;

                        try (ResultSet resultSet = statement.executeQuery()) {
                            while (resultSet.next()) {
                                if (rowCount == rowsPerPage) {
                                    contentStream.close();
                                    PDPage newPageObj = new PDPage();
                                    document.addPage(newPageObj);
                                    try (PDPageContentStream newContentStream = new PDPageContentStream(document, newPageObj)) {
                                        drawTableHeader(newContentStream, yStart, rowHeight, cellMargin, tableWidth, headers, columnWidths);
                                        yPosition = yStart - rowHeight;
                                        rowCount = 0;
                                    }
                                }
                                if (yPosition - rowHeight - bottomMargin <= 0) {
                                    newPage = true;
                                    break;
                                }
                                String nomProduit = resultSet.getString("nom_produit");
                                double prixProduit = resultSet.getDouble("prix_produit");
                                int idUser = resultSet.getInt("idUser");

                                drawTableRow(contentStream, yPosition, rowHeight, cellMargin, tableWidth, new String[]{nomProduit, String.valueOf(prixProduit)});
                                yPosition -= rowHeight;
                                rowCount++;

                                totalSum += prixProduit;
                            }

                            drawTotalRow(contentStream, yPosition, rowHeight, cellMargin, tableWidth, totalSum);
                        }

                        contentStream.close();
                        document.save(pdfFilePath);

                        File pdfFile = new File(pdfFilePath);
                        Desktop.getDesktop().open(pdfFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void drawTableHeader(PDPageContentStream contentStream, float y, float rowHeight, float cellMargin, float tableWidth, String[] headers, float[] columnWidths) throws IOException {
        float x = 200;
        contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y - rowHeight);
        for (int i = 0; i < headers.length; i++) {
            contentStream.showText(headers[i]);
            contentStream.newLineAtOffset(columnWidths[i], 0);
        }
        contentStream.endText();
    }

    private void drawTableRow(PDPageContentStream contentStream, float y, float rowHeight, float cellMargin, float tableWidth, String[] row) throws IOException {
        float x = 200;
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y - rowHeight);
        for (int i = 0; i < row.length; i++) {
            contentStream.showText(row[i]);
            contentStream.newLineAtOffset(getColumnWidth(i), 0);
        }
        contentStream.endText();
    }

    private void drawTotalRow(PDPageContentStream contentStream, float y, float rowHeight, float cellMargin, float tableWidth, double totalSum) throws IOException {
        float x = 200;
        contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y - rowHeight);
        contentStream.showText("Total: ");
        contentStream.newLineAtOffset(200, 0);
        contentStream.showText(String.valueOf(totalSum));
        contentStream.endText();
    }

    private float getColumnWidth(int columnIndex) {
        if (columnIndex == 0) {
            return 200;
        } else if (columnIndex == 1) {
            return 100;
        } else {
            return 0;
        }
    }


    @FXML
    public void logout(){
        TokenManager.clearToken();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface.fxml"));
            Parent root = loader.load();
            totalLabel.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}