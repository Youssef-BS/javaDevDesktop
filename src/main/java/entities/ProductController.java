package entities;

import javafx.event.ActionEvent;

import java.io.IOException;
public class ProductController {

    private String photoPath;


    private Integer productID;

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public ProductController(Integer productID, String productName,Integer productPrice, String photoPath, Integer productQuantity ) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.photoPath = photoPath;
        this.productQuantity = productQuantity;

    }

    private Integer productQuantity;
    private String productName;
    private String productImagePath;
    private Integer productPrice;

    public ProductController(Integer productID, String productName, Integer productPrice, String photoPath) {
        this.productName = productName;
        this.productID = productID;
        this.photoPath = photoPath;
        this.productPrice = productPrice;
    }
    public ProductController(String productName, String photoPath) {
        this.productName = productName;
        this.photoPath = photoPath;
    }
    public ProductController(String productName, Integer productPrice, String photoPath) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.photoPath = photoPath;
    }
    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }



}
