package controllers;

import entities.Gym;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import services.GymService;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficherGymController implements Initializable {
    /*

    @FXML
    private TableColumn<Gym, Integer> ageCol;

    @FXML
    private TableColumn<Gym, String> nomCol;

    @FXML
    private TableColumn<Gym, String> prenomCol;

    @FXML
    private TableView<Gym> tableView;

    private final GymService gymService = new GymService();

    @FXML
    void initialize() {
        try {
            List<Gym> gyms = gymService.recuperer();
            ObservableList<Gym> observableList = FXCollections.observableList(gyms);
            tableView.setItems(observableList);

            nomCol.setCellValueFactory(new PropertyValueFactory<Gym, String>("nom"));
            prenomCol.setCellValueFactory(new PropertyValueFactory<Gym, String>("prenom"));
            ageCol.setCellValueFactory(new PropertyValueFactory<Gym, Integer>("age"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     */

    @FXML
    private Button searchBtn;
    @FXML
    private TextField search;

    @FXML
    private Button quiter;

    @FXML
    private GridPane gymsFX;
    GymService gymS = new GymService();
    ObservableList<Gym> gyms;
    boolean isSearched = false;
    void getAllFromDB(){
        try {
            // Récupérer toutes les gyms
            if (search.getText().isEmpty()){
                isSearched = false;
            }
            if (isSearched){
                gyms.remove(0,gyms.size());
                gymsFX.getChildren().remove(0,gymsFX.getChildren().size());
                gyms = FXCollections.observableArrayList(gymS.recupererByAddresse(search.getText()));
            }else {
                gyms = FXCollections.observableArrayList(gymS.recuperer());
            }

            // Nombre de gyms par ligne
            int produitsParLigne = 3;

            // Ajout des données dans la GridPane
            int row = 0;
            int col = 0;
            for (int i = 0; i < gyms.size(); i++) {


                Gym Gym = gyms.get(i);
                String photo = Gym.getPhotoGym();
                String nom = Gym.getNomGym();
                String adresse = Gym.getAdresse();
                Image imageP = new Image("file:C:///Users//youssef//Desktop//PI//image//image//" + photo);
                ImageView imageView = new ImageView(imageP);
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);
                Label labelPhoto = new Label();
                labelPhoto.setGraphic(imageView);

                //Label labelPhoto = new Label(imageP);
                Label labelNom = new Label("Nom: " + nom);
                Label labeladresse = new Label("adresse: " + adresse);

//                labelNom.setStyle("-fx-text-fill: white");
//                labeladresse.setStyle("-fx-text-fill: white ");
                // Créer un VBox pour empiler verticalement les labels
                VBox vbox = new VBox(15); // Espacement de 10 pixels entre chaque Gym
                vbox.setStyle("-fx-background-color: #B4D4FF ;-fx-padding: 15px ; -fx-background-radius: 15px ; -fx-alignment: center ");
                vbox.getChildren().addAll(new VBox(5, labelPhoto, labelNom, labeladresse));

                // Ajouter un gestionnaire d'événements pour ouvrir le fichier FXML spécifique lorsque vous cliquez sur la photo

                final int GymId = Gym.getIdGym(); // Capturer l'ID du Gym pour l'utiliser dans le gestionnaire d'événements
                labelPhoto.setOnMouseClicked(event -> {
                    loadFXML("/AfficherUnGym.fxml", GymId);
                    System.out.println("hos****" + GymId);
                });


                // Ajouter du padding pour créer un espace vertical entre les colonnes
                if (col > 0) {
                    Insets insets = new Insets(0, 10, 10, 0); //haut / droite / bas / gauche
                    gymsFX.add(vbox, col, row);
                    GridPane.setMargin(vbox, insets);
                } else {
                    gymsFX.add(vbox, col, row);
                }

                // Incrémenter les indices de colonne et de ligne
                col++;
                if (col == produitsParLigne) {
                    col = 0;
                    row++;
                }
            }

            // Ajouter de l'espace vertical entre les lignes
            gymsFX.setVgap(10);

        } catch (SQLException e) {
            // Gérer les exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des factures");
            alert.setContentText("Une erreur s'est produite lors du chargement des factures depuis la base de données.");
            alert.showAndWait();
        }
    }



    @FXML
    void  quitter(){
        try {
            if (search.getText().isEmpty()) {
                isSearched = false;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGym.fxml"));
                Parent root = loader.load();
                quiter.getScene().setRoot(root);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void  quitter1(){
        quiter.setVisible(false);
        quiter.setManaged(false);
                try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherGym.fxml"));
                Parent root = loader.load();
                quiter.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void ajouter(){
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterGym.fxml"));
           Parent root = loader.load();
           search.getScene().setRoot(root);
       }catch (Exception e){
           System.out.println(e.getMessage());
       }
    }
    boolean verif(){
        if (search.getText().isEmpty()){
            isSearched = false;
        }
        return true ;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quiter.setManaged(false);
          getAllFromDB();
    }


    private void loadFXML(String s, int productId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AfficherUnGym.fxml"));
            Parent root = fxmlLoader.load();

            // Accéder au contrôleur du fichier FXML chargé
            AfficherUnGymController controller = fxmlLoader.getController();

            // Passer l'ID du Gym au contrôleur
            controller.setGymId(productId);

            gymsFX.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void search() {
        // Récupérer toutes les gyms

        isSearched = true;
        quiter.setVisible(true);
        quiter.setManaged(true);
        getAllFromDB();
        // Nombre de gyms par ligne
            /*int produitsParLigne = 3;
            // Ajout des données dans la GridPane
            int row = 0;
            int col = 0;
            for (int i = 0; i < gyms.size(); i++) {
                Gym Gym = gyms.get(i);
                String photo = Gym.getPhotoGym();
                String nom = Gym.getNomGym();
                String adresse = Gym.getAdresse();
                Image imageP = new Image("file:///C://Users//Houssem//Desktop//GymProject//GymProject//PIDEV3A9//image//" + photo);
                ImageView imageView = new ImageView(imageP);
                imageView.setFitHeight(160);
                imageView.setFitWidth(160);
                Label labelPhoto = new Label();
                labelPhoto.setGraphic(imageView);

                //Label labelPhoto = new Label(imageP);
                Label labelNom = new Label("Nom: " + nom);
                Label labeladresse = new Label("adresse: " + adresse);


                // Créer un VBox pour empiler verticalement les labels
                VBox vbox = new VBox(10); // Espacement de 10 pixels entre chaque Gym

                vbox.getChildren().addAll(new VBox(5, new Label("Entité gym " + (i + 1)), labelPhoto, labelNom, labeladresse));

                // Ajouter un gestionnaire d'événements pour ouvrir le fichier FXML spécifique lorsque vous cliquez sur la photo

                final int GymId = Gym.getIdGym(); // Capturer l'ID du Gym pour l'utiliser dans le gestionnaire d'événements
                labelPhoto.setOnMouseClicked(event -> {
                    loadFXML("/AfficherUnGym.fxml", GymId);
                    System.out.println("hos****" + GymId);

                });


                // Ajouter du padding pour créer un espace vertical entre les colonnes
                if (col > 0) {
                    Insets insets = new Insets(0, 10, 10, 0); //haut / droite / bas / gauche
                    gymsFX.add(vbox, col, row);
                    GridPane.setMargin(vbox, insets);
                } else {
                    gymsFX.add(vbox, col, row);
                }

                // Incrémenter les indices de colonne et de ligne
                col++;
                if (col == produitsParLigne) {
                    col = 0;
                    row++;
                }
            }

            // Ajouter de l'espace vertical entre les lignes
            gymsFX.setVgap(10);*/

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

    }


    public void handleButtonAction(javafx.event.ActionEvent actionEvent) {
        search();
        quitter();
    }
}
