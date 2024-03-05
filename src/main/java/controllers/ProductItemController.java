package entities;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.File;

public class ProductItemController {

    @FXML
    public BorderPane item;
    @FXML
    private ImageView imageView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    public void setProduct(ProductController product) {
        nameLabel.setText(product.getProductName());
        priceLabel.setText(String.valueOf(product.getProductPrice()));
        imageView.setImage(new Image(new File(product.getPhotoPath()).toURI().toString()));
    }


}


