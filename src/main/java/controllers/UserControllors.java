package controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentCreateParams;
import entities.Abonnement;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import midlleware.TokenManager;
import services.AbonnementService;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import com.stripe.model.PaymentIntent;





public class UserControllors implements Initializable {
    private int selectedSubscriptionAmount;
    @FXML
    private Pane menu;

    @FXML
    private Button first;

    @FXML
    private Button second;

    @FXML
    private Button last;

    @FXML
    private GridPane userGridPane;

    @FXML
    private AnchorPane Subscribes ;

    @FXML
    private AnchorPane ListUser;

    @FXML
    private Button view ;

    @FXML
    private Button buy ;

    @FXML
    private AnchorPane payment ;

    @FXML
    private WebView webView;

    final AbonnementService abn = new AbonnementService();

    final int idUser = TokenManager.decodeId();


    @FXML
    public void logout() {
        TokenManager.clearToken();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            menu.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JavaAppBridge userAppBridge;

    public void setUserAppBridge(JavaAppBridge userAppBridge) {
        this.userAppBridge = userAppBridge;
    }

    @FXML
    private void initialize() {
        first.setOnAction(event -> handleButtonClick(40));
        second.setOnAction(event -> handleButtonClick(115));
        last.setOnAction(event -> handleButtonClick(400));
    }
    private void handleButtonClick(int subscriptionAmount) {
        try {
            selectedSubscriptionAmount = subscriptionAmount;
            int idUser = TokenManager.decodeId();
            Abonnement newAbn = new Abonnement(subscriptionAmount, true, idUser, 1);
            abn.ajouter(newAbn);
            payment.setVisible(true);
            Subscribes.setVisible(false);
            ListUser.setVisible(false);
            processPayment(selectedSubscriptionAmount);
        } catch (SQLException e) {
            // Handle the exception appropriately (e.g., log, show an error message)
            e.printStackTrace();
            // Show an error message to the user
            // TODO: Implement error handling for the user
        }
    }


    private void processPayment(int subscriptionAmount) {
        try {
            PaymentIntent paymentIntent = createPaymentIntent(subscriptionAmount);
            String clientSecret = paymentIntent.getClientSecret();
            if (userAppBridge != null) {
                userAppBridge.processPayment(clientSecret, subscriptionAmount);
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }

    public void handlePaymentWithToken(String clientSecret, int subscriptionAmount){

    }

    private PaymentIntent createPaymentIntent(int subscriptionAmount) throws StripeException {
        Stripe.apiKey = "sk_test_51OpDKRCibHqyLeRNfSFnvYh560p6TQMn1w4HNpXcaceOgxCWXBpLBHGdqlYvy2z7BXbIJxogUkcj09plAfYZn2Ll00N4Xg9N5V";
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (subscriptionAmount * 100))
                .setCurrency("usd")
                .build();
        return PaymentIntent.create(params);
    }


    public void initializeSubscribes() {
        try {

            List<Abonnement> Subscribes = abn.subscribeListforAnUser(idUser);
            displaySubscribesForAnUser(Subscribes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displaySubscribesForAnUser(List<Abonnement> abns) {
        userGridPane.getColumnConstraints().clear();
        userGridPane.getChildren().clear();
        int maxColumns = 3;
        Insets userMargin = new Insets(30);
        for (int i = 0; i < abns.size(); i++) {
            Abonnement subscribe = abns.get(i);
            VBox subsBox = new VBox();
            subsBox.setStyle("-fx-background-color:#112D4E; -fx-padding: 50; -fx-min-width: 200px ;  -fx-background-radius :13px ;-fx-alignment: center");
            LocalDate dD = subscribe.getDateDebut() ;
            LocalDate dF = subscribe.getDateFin() ;
            LocalDate currentDate = LocalDate.now();
            Label status = new Label() ;
            if(currentDate.isAfter(dF)) {
                status.setText("INACTIVE");
                status.setStyle("-fx-text-fill: red");
            }else {
                status.setText("ACTIVE");
                status.setStyle("-fx-text-fill: green");
            }
            Label startDate = new Label(""+dD);
            Label lastDate = new Label( ""+dF);
            startDate.setStyle("-fx-alignment: center ; -fx-text-fill: white ; -fx-padding: 5px");
            lastDate.setStyle("-fx-alignment: center ; -fx-text-fill: white ; -fx-padding: 5px");
            subsBox.getChildren().addAll(startDate, lastDate,  status);
            int column = i % maxColumns;
            int row = i / maxColumns;
            userGridPane.add(subsBox, column, row);
            GridPane.setMargin(subsBox, userMargin);
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            userGridPane.getColumnConstraints().add(colConstraints);
        }
    }
    String filePath = "file:///C:/Users/youssef/Downloads/GymProject/PIDEV3A9/src/main/java/controllers/stripe.html";
    URI uri = URI.create(filePath);
    Path path = Paths.get(uri);
    String absolutePath = path.toAbsolutePath().toString();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = webView.getEngine();
        try {
            byte[] htmlBytes = Files.readAllBytes(Paths.get(absolutePath));
            String htmlContent = new String(htmlBytes);
            webEngine.loadContent(htmlContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JavaAppBridge javaAppBridge = new JavaAppBridge();
        javaAppBridge.setUserController(this);
        javaAppBridge.inject(webEngine);
        initializeSubscribes();
        userAppBridge = javaAppBridge;
    }

    @FXML
    public void handleClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == view) {
            Subscribes.setVisible(false);
            ListUser.setVisible(true);
            payment.setVisible(false);
        } else if (actionEvent.getSource() == buy) {
            if (selectedSubscriptionAmount > 0) {
                handleButtonClick(selectedSubscriptionAmount);
                showCreditCardForm();
            } else {
                System.out.println("Please select a subscription amount.");
            }
        }
    }

    private void showCreditCardForm() {

        processPaymentAndDatabaseUpdate();
    }


    private void processPaymentAndDatabaseUpdate() {
        try {
            processPayment(selectedSubscriptionAmount);
            int idUser = TokenManager.decodeId();
            Abonnement newAbn = new Abonnement(selectedSubscriptionAmount, true, idUser, 1);
            abn.ajouter(newAbn);
            payment.setVisible(true);
            Subscribes.setVisible(false);
            ListUser.setVisible(false);
        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void switchProduct() {
        try {
            int idUser = TokenManager.decodeId();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientProduct.fxml"));
            Parent root = loader.load();
            ClientController pcC = loader.getController();
            pcC.setUserId(idUser);
            buy.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchProg() {
        try {
            int idUser = TokenManager.decodeId();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgClient.fxml"));
            Parent root = loader.load();
            ProgClientController pcC = loader.getController();
            pcC.setIdUserClient(idUser);
            buy.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchRec() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutrec.fxml"));
            Parent root = loader.load();
            buy.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchGym() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGym.fxml"));
            Parent root = loader.load();
            buy.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @FXML
//    public void switchRec() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReclamation.fxml"));
//            Parent root = loader.load();
//            buy.getScene().setRoot(root);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

@FXML
    public void returnToMain(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface.fxml"));
            Parent root = loader.load();
            buy.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
