package controllers;

import entities.Abonnement;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import entities.User;
import javafx.geometry.Insets;
import java.awt.image.BufferedImage;
import javax.servlet.http.Cookie;
import java.io.File;
import java.io.IOException;
import javafx.scene.layout.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import midlleware.TokenManager;
import services.AbonnementService;
import services.UserService;
import javafx.application.Application;
//import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import javax.swing.text.html.ImageView;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.CheckBox;

public class CrudUsersControllors  implements Initializable {

    @FXML
    private Button btnMain;

    @FXML
    private Button upload;
    @FXML
    private AnchorPane stackPane;
    @FXML
    private AnchorPane setOneUser;
    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;
    @FXML
    private Button btnPackages;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnSignout;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnMenuSubs;
    @FXML
    private Pane pnlCustomer;
    @FXML
    private Pane pnlOrders;
    @FXML
    private Pane pnlOverview;
    @FXML
    private Pane pnlMenus;
    @FXML
    private Pane pnlCustome;
    @FXML
    private AnchorPane addUser;
    @FXML
    private AnchorPane statisctic;
    @FXML
    private AnchorPane profile;
    @FXML
    private TextField fistnameFx;
    @FXML
    private TextField lastNameFx;
    @FXML
    private TextField emailFx;
    @FXML
    private TextField isAdminFx;
    @FXML
    private TextField isCoachFx;
    @FXML
    private TextField passwordFx;
    @FXML
    private TextField phoneFx;
    @FXML
    private TextField photoFx;
    @FXML
    private TextField ageFx;
    @FXML
    private GridPane userGridPane;

    @FXML
    private GridPane userGridPane1;
    @FXML
    private AnchorPane Users;

    @FXML
    private AnchorPane subcribeList;

    @FXML
    private AnchorPane subscribes;

    @FXML
    private ImageView photoProfile;

    @FXML
    private ImageView photoImageView;
    @FXML
    private CheckBox adminChecked;

    @FXML
    private CheckBox coachChecked;

    @FXML
    private TextField searchInput;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField prix;

    @FXML
    private TextField idUserSubsribe;


    final UserService userService = new UserService();
    final AbonnementService abnsService = new AbonnementService();

    public void logout() {
        TokenManager.clearToken();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            btnSignout.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
//            tableUsers.setVisible(false);
            addUser.setVisible(true);
            statisctic.setVisible(false);
            profile.setVisible(false);
            Users.setVisible(false);
            subcribeList.setVisible(false);
            subscribes.setVisible(false);
        }
        if (actionEvent.getSource() == btnOverview) {
//            tableUsers.setVisible(false);
            addUser.setVisible(false);
            statisctic.setVisible(true);
            profile.setVisible(false);
            Users.setVisible(false);
            subcribeList.setVisible(false);
            subscribes.setVisible(false);
        }
        if (actionEvent.getSource() == btnMenus) {
//            tableUsers.setVisible(true);
            addUser.setVisible(false);
            statisctic.setVisible(false);
            profile.setVisible(false);
            Users.setVisible(true);
            subcribeList.setVisible(false);
            subscribes.setVisible(false);
        }
        if (actionEvent.getSource() == btnMain) {
//            tableUsers.setVisible(false);
            addUser.setVisible(false);
            statisctic.setVisible(true);
            profile.setVisible(false);
            Users.setVisible(true);
            subcribeList.setVisible(false);
            subscribes.setVisible(false);
        }
        if (actionEvent.getSource() == btnOrders) {
//            tableUsers.setVisible(false);
            addUser.setVisible(false);
            statisctic.setVisible(false);
            profile.setVisible(false);
            Users.setVisible(false);
            subcribeList.setVisible(false);
            subscribes.setVisible(false);
        }
        if (actionEvent.getSource() == btnSettings) {
//            tableUsers.setVisible(false);
            addUser.setVisible(false);
            statisctic.setVisible(false);
            profile.setVisible(true);
            Users.setVisible(false);
            subcribeList.setVisible(false);
            subscribes.setVisible(false);
        }
        if (actionEvent.getSource() == btnMenuSubs) {
//            tableUsers.setVisible(false);
            addUser.setVisible(false);
            statisctic.setVisible(false);
            profile.setVisible(false);
            Users.setVisible(false);
            subcribeList.setVisible(true);
            subscribes.setVisible(false);
        }
    }

    String fileUpload = "";

    @FXML
    private void handleUploadButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File initialDirectory = new File("C:\\Users\\youssef\\Downloads\\GymProject\\PIDEV3A9\\src\\main\\java\\img");
        if (initialDirectory.exists() && initialDirectory.isDirectory()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            photoImageView.setImage(image);
            String photoFilePath = selectedFile.getAbsolutePath();
            fileUpload = photoFilePath;
        }
    }

    public void addUser() {
        try {
            String firstName = fistnameFx.getText();
            String lastName = lastNameFx.getText();
            String email = emailFx.getText();
            String password = passwordFx.getText();
            int age = Integer.parseInt(ageFx.getText());
            String phone = phoneFx.getText();
            int admin = Integer.parseInt(String.valueOf(isAdminFx.getText()));
            int coach = Integer.parseInt(String.valueOf(isCoachFx.getText()));
            User newUser = new User(firstName, lastName, age, email, fileUpload, password, phone, coach, admin);
            userService.ajouter(newUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("User added successfully");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void initialize() {
        try {
            List<User> users = userService.recuperer(searchInput.getText());
            displayUsers(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayUsers(List<User> users) {
        userGridPane.getColumnConstraints().clear();
        userGridPane.getChildren().clear();
        int maxColumns = 3;
        Insets userMargin = new Insets(30);
        for (int i = 0; i < users.size(); i++) {
            Button btnDelete = new Button();
            Button FetchUser = new Button();
            btnDelete.setText("Delete");
            FetchUser.setText("See User");
            VBox.setMargin(FetchUser, new Insets(10, 0, 10, 0));
            btnDelete.setStyle("-fx-background-radius: 15px; -fx-background-color: #3F72AF");
            FetchUser.setStyle("-fx-background-radius: 15px; -fx-background-color: #3F72AF");
            User user = users.get(i);
            VBox userBox = new VBox();
            btnDelete.setOnAction(event -> DeleteUser(user.getId(), userBox));
            FetchUser.setOnAction(event -> fetchUser(user.getId()));
            userBox.setStyle("-fx-background-color:#112D4E; -fx-padding: 50; -fx-min-width: 200px ;  -fx-background-radius :13px ;-fx-alignment: center");
            Label nameLabel = new Label("name is " + user.getNom() + "\n" + "last name is " + user.getPrenom() + "\n" + "adresse is " + user.getEmail());
            nameLabel.setStyle("-fx-alignment: center ; -fx-text-fill: white ; -fx-padding: 5px");
            if (!user.getPhotoProfile().isEmpty()) {
                ImageView img = new ImageView(new Image("file:" + user.getPhotoProfile()));
                img.setFitWidth(100);
                img.setFitHeight(100);
                userBox.getChildren().addAll(img, nameLabel, FetchUser, btnDelete);
            } else {
                userBox.getChildren().addAll(nameLabel, FetchUser, btnDelete);
            }

            int column = i % maxColumns;
            int row = i / maxColumns;
            userGridPane.add(userBox, column, row);
            GridPane.setMargin(userBox, userMargin);
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            userGridPane.getColumnConstraints().add(colConstraints);
        }
    }

    private void DeleteUser(int id, VBox userBox) {
        try {
            UserService userService1 = new UserService();
            userService1.supprimer(id);
            userGridPane.getChildren().remove(userBox);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int idUser;

    private void fetchUser(int id) {
        try {
            User userFetch = userService.fetchUser(id);
            idUser = id;
            addUser.setVisible(true);
            Users.setVisible(false);
            fistnameFx.setText(userFetch.nom);
            lastNameFx.setText(userFetch.prenom);
            ImageView img = new ImageView(new Image("file:" + userFetch.getPhotoProfile()));
            img.setFitWidth(100);
            img.setFitHeight(100);
            photoImageView.setImage(img.getImage());
            emailFx.setText(userFetch.email);
            passwordFx.setText(userFetch.password);
            ageFx.setText(String.valueOf(userFetch.age));
            phoneFx.setText(String.valueOf(userFetch.phoneNumber));
            isAdminFx.setText(String.valueOf(userFetch.isAdmin));
            isCoachFx.setText(String.valueOf(userFetch.isAdmin));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void emptyInputs() {
        fistnameFx.setText("");
        lastNameFx.setText("");
        emailFx.setText("");
        passwordFx.setText("");
        ageFx.setText("");
        phoneFx.setText("");
        isAdminFx.setText("");
        isCoachFx.setText("");
    }

    @FXML
    private void updateUser() {
        try {
            String firstName = fistnameFx.getText();
            String lastName = lastNameFx.getText();
            String email = emailFx.getText();
            String password = passwordFx.getText();
            int age = Integer.parseInt(ageFx.getText());
            String phone = phoneFx.getText();
            int admin = Integer.parseInt(String.valueOf(isAdminFx.getText()));
            int coach = Integer.parseInt(String.valueOf(isCoachFx.getText()));
            User userUpdated = new User(firstName, lastName, age, email, fileUpload, password, phone, coach, admin, idUser);
            userService.modifier(userUpdated);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("User Updated");
            alert.showAndWait();
            idUser = -1;
            emptyInputs();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void search() {
        if (adminChecked.isSelected()) {
            try {
                List<User> users = userService.admins(searchInput.getText());
                displayUsers(users);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (coachChecked.isSelected()) {
            try {
                List<User> users = userService.coach(searchInput.getText());
                displayUsers(users);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                List<User> users = userService.recuperer(searchInput.getText());
                displayUsers(users);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void initializeSubscribes() {
        try {
            List<Abonnement> abns = abnsService.subscribeAllSubscribes();
            displaySubscribes(abns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displaySubscribes(List<Abonnement> abns) {
        userGridPane1.getColumnConstraints().clear();
        userGridPane1.getChildren().clear();
        int maxColumns = 3;
        Insets userMargin = new Insets(30);
        for (int i = 0; i < abns.size(); i++) {
            Abonnement subscribe = abns.get(i);
            VBox subsBox = new VBox();
            Button btnDelete = new Button();
            Button fetchSubscribe = new Button();
            btnDelete.setText("Delete");
            fetchSubscribe.setText("See Subscribe");
            btnDelete.setOnAction(event -> deleteSubscribe(subscribe.getIdAbonnement(), subsBox));
            fetchSubscribe.setOnAction(event -> fetchSubscribe(subscribe.getIdAbonnement()));
            VBox.setMargin(fetchSubscribe, new Insets(10, 0, 10, 0));
            btnDelete.setStyle("-fx-background-radius: 15px; -fx-background-color: #3F72AF");
            fetchSubscribe.setStyle("-fx-background-radius: 15px; -fx-background-color: #3F72AF");
            subsBox.setStyle("-fx-background-color:#112D4E; -fx-padding: 50; -fx-min-width: 200px ;  -fx-background-radius :13px ;-fx-alignment: center");
            LocalDate dD = subscribe.getDateDebut();
            LocalDate dF = subscribe.getDateFin();
            LocalDate currentDate = LocalDate.now();
            Label status = new Label();
            if (currentDate.isAfter(dF)) {
                status.setText("INACTIVE");
                status.setStyle("-fx-text-fill: red");
            } else {
                status.setText("ACTIVE");
                status.setStyle("-fx-text-fill: green");
            }
            Label startDate = new Label("" + dD);
            Label lastDate = new Label("" + dF);
            startDate.setStyle("-fx-alignment: center ; -fx-text-fill: white ; -fx-padding: 5px");
            lastDate.setStyle("-fx-alignment: center ; -fx-text-fill: white ; -fx-padding: 5px");
            subsBox.getChildren().addAll(startDate, lastDate, status, fetchSubscribe, btnDelete);
            int column = i % maxColumns;
            int row = i / maxColumns;
            userGridPane1.add(subsBox, column, row);
            GridPane.setMargin(subsBox, userMargin);
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            userGridPane1.getColumnConstraints().add(colConstraints);
        }
    }

    public void deleteSubscribe(int id, VBox subsBox) {
        try {
            abnsService.supprimer(id);
            userGridPane1.getChildren().remove(subsBox);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void updateSubscribe() {
        try {
            LocalDate dateD = dateDebut.getValue();
            LocalDate dateF = dateFin.getValue();
            int price = Integer.parseInt(String.valueOf(Integer.parseInt(prix.getText())));
            int idUser = Integer.parseInt(String.valueOf(idUserSubsribe.getText()));
            Abonnement abn = new Abonnement(dateD, dateF, price, idUser, idSubscribe);
            abnsService.modifier(abn);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Subscribe Updated");
            emptyInputsForSubcribes();
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int idSubscribe;

    private void fetchSubscribe(int id) {
        try {
            Abonnement abn = abnsService.fetchOneSubscribe(id);
            idSubscribe = id;
            dateDebut.setValue(abn.dateDebut);
            dateFin.setValue(abn.dateFin);
            prix.setText(String.valueOf(abn.prix));
            idUserSubsribe.setText(String.valueOf(abn.idUser));
            subscribes.setVisible(true);
            subcribeList.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void emptyInputsForSubcribes() {
        dateDebut.setValue(null);
        dateFin.setValue(null);
        prix.setText("");
        idUserSubsribe.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSubscribes();
        initialize();
    }

    @FXML
    public void switchGym() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGym.fxml"));
            Parent root = loader.load();
            prix.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminProduct.fxml"));
            Parent root = loader.load();
            prix.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchProgram() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionProgram.fxml"));
            Parent root = loader.load();
            prix.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void switchReclamation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutrec.fxml"));
            Parent root = loader.load();
            prix.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}