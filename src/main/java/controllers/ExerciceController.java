package controllers;

import entities.Exercice;
import entities.Program;
import services.ExerciceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExerciceController implements Initializable {
    int id = 0;
    private ExerciceService exerciceService = new ExerciceService();
    @FXML
    private Button btnAjouter;
    @FXML
    private Button triId;

    @FXML
    private Button trnom;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button clientBtn;

    @FXML
    private TableColumn<Exercice, String> colDescription;

    @FXML
    private TableColumn<Exercice, String> colDifficulte;

    @FXML
    private TableColumn<Exercice, String> colDuree;

    @FXML
    private TableColumn<Exercice, Integer> colId;

    @FXML
    private TableColumn<Exercice, String> colNom;

    @FXML
    private TableColumn<Exercice, Integer> coliDprog;

    @FXML
    private AnchorPane form_container;

    @FXML
    private Button gestionEvenement;

    @FXML
    private ComboBox<Integer> idProgram;
    @FXML
    private ImageView signout;

    @FXML
    private Button importerbtn;

    @FXML
    private TextField tDescription;

    @FXML
    private ComboBox<String> tDifficulte;

    @FXML
    private TextField tDuree;

    @FXML
    private TextField tNom;
    private String url;
    @FXML
    private MediaView video = new MediaView(null);
    private Media media;
    private MediaPlayer mediaPlayer;

    @FXML
    private TableView<Exercice> table;
    List<String> diff = new ArrayList<>();

    @FXML
    void clearFiealds(ActionEvent event) {
clear();
    }

    @FXML
    void createExercice(ActionEvent event) {
        try {
            exerciceService.ajouter(new Exercice( tNom.getText(),tDescription.getText(), tDuree.getText(), tDifficulte.getValue(),url, (int)idProgram.getValue()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Exercice ajoutée avec succès");
            alert.showAndWait();
            clear();
            showEvents();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    void deleteExercice(ActionEvent event) {
        try{
            exerciceService.supprimer(id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Evenement supprimée avec succès");
            alert.showAndWait();
            clear();
            showEvents();
        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void editExercice(ActionEvent event) {
        try {
            exerciceService.modifier(new Exercice(id, tNom.getText(),tDescription.getText(), tDuree.getText(), tDifficulte.getValue(),url,idProgram.getValue()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Exercice ajoutée avec succès");
            alert.showAndWait();
            clear();
            showEvents();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void getData(MouseEvent event) {
        Exercice exercice = table.getSelectionModel().getSelectedItem();
        id = exercice.getId();
        tNom.setText(exercice.getNom());
        tDescription.setText(exercice.getDescription());
        tDuree.setText(exercice.getDuree());

        btnAjouter.setDisable(true);
    }
    public void showEvents(){
        try {
            List<Exercice> list = exerciceService.recuperer();
            ObservableList<Exercice> exercices = FXCollections.observableArrayList(list);
            table.setItems(exercices);
            colId.setCellValueFactory(new PropertyValueFactory<Exercice, Integer>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<Exercice, String>("nom"));
            colDescription.setCellValueFactory(new PropertyValueFactory<Exercice, String>("description"));
            colDuree.setCellValueFactory(new PropertyValueFactory<Exercice, String>("duree"));
            colDifficulte.setCellValueFactory(new PropertyValueFactory<Exercice,String>("difficulte"));
            coliDprog.setCellValueFactory(new PropertyValueFactory<Exercice,Integer>("idprogramme"));

        }
        catch(SQLException e){
            throw new RuntimeException();
        }

    }
    @FXML
    void goToProgram(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GestionProgram.fxml"));
            table.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void gotoClient(ActionEvent event) {

    }

    @FXML
    void importer(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media");
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null){
             url = selectedFile.toURI().toString();
            media = new Media(url);
            mediaPlayer = new MediaPlayer(media);

            video.setMediaPlayer(mediaPlayer);
            //Scene scene = video.getScene();
           /* video.fitWidthProperty().bind(scene.widthProperty());
            video.fitHeightProperty().bind(scene.heightProperty());*/


        }
    }

    public void clear(){
        tNom.setText(null);
        tDescription.setText(null);
        tDuree.setText(null);

        btnAjouter.setDisable(false);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showEvents();
        try {
            idProgram.setItems(FXCollections.observableList(exerciceService.recupererIDPROG()));
            diff.add("Easy");
            diff.add("Medium");
            diff.add("Difficult");
            tDifficulte.setItems(FXCollections.observableList(diff));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterData(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    private TextField searchField;
    private void filterData(String query) throws SQLException {
        List<Exercice> filteredList = new ArrayList<>();
        for (Exercice exercice : exerciceService.recuperer()) {
            if (exercice.getNom().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(exercice);
            }
        }
        ObservableList<Exercice> filteredExercices = FXCollections.observableArrayList(filteredList);
        table.setItems(filteredExercices); // Update the TableView with filtered data
    }
    @FXML
    void triIid(ActionEvent event) throws SQLException {
        List<Exercice> list = exerciceService.triNom();
        ObservableList<Exercice> exercices = FXCollections.observableArrayList(list);
        table.setItems(exercices);
    }

    @FXML
    void triNom(ActionEvent event) {

    }
    @FXML
    void signout(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/welcome.fxml"));
            table.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
