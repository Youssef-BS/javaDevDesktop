package controllers;


import entities.Exercice;
import entities.Program;
import services.ProgramService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ProgramController implements Initializable {


    Stage stage = new Stage();
    int id = 0;

    @FXML
    private Button btnAjouter;
    @FXML
    private Button triId;


    @FXML
    private Button btnExercice;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private TableColumn<Program, Integer> colId;

    @FXML
    private TableColumn<Program, Date> colDeadline;

    @FXML
    private TableColumn<Program, String> colDescription;

    @FXML
    private TableColumn<Program, String> colDuree;

    @FXML
    private TableColumn<Program, String> colNom;

    @FXML
    private TableColumn<Program, Float> colPrix;
    @FXML
    private AnchorPane form_container;
    @FXML
    private Button clientBtn;
    @FXML
    private Button importerbtn;
    Image image;
    private File imported;
    @FXML
    private TableView<Program> table;
    UnaryOperator<TextFormatter.Change> filterNom = change -> {
        String newText = change.getControlNewText();
        if (Pattern.matches("[A-Z].*", newText) || newText.isEmpty()) {
            return change; // Accept the change if it begins with an uppercase letter or is empty
        } else {
            return null; // Reject the change
        }
    };
    UnaryOperator<TextFormatter.Change> filter = change -> {
        String newText = change.getControlNewText();
        if (Pattern.matches("[a-zA-Z]*", newText) || newText.isEmpty()) {
            return change; // Accept the change if it begins with an uppercase letter or is empty
        } else {
            return null; // Reject the change
        }
    };
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPrograms();
        tNom.setTextFormatter(new TextFormatter<>(filterNom));
        tDescription.setTextFormatter(new TextFormatter<>(filter));
        tDeadline.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });
        tPrix.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*\\.?\\d*") ) {
                tPrix.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                filterData(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public void showPrograms(){

        try {
            List<Program> list = programService.recuperer();
            ObservableList<Program> programs = FXCollections.observableArrayList(list);
            table.setItems(programs);
            colId.setCellValueFactory(new PropertyValueFactory<Program, Integer>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<Program, String>("nom"));
            colDescription.setCellValueFactory(new PropertyValueFactory<Program, String>("description"));
            colDuree.setCellValueFactory(new PropertyValueFactory<Program, String>("duree"));
            colDeadline.setCellValueFactory(new PropertyValueFactory<Program, Date>("registration_deadline"));
            colPrix.setCellValueFactory(new PropertyValueFactory<Program, Float>("prix"));
        }
        catch(SQLException e){
            throw new RuntimeException();
        }
    }

    @FXML
    private Button evenement;
    @FXML
    private DatePicker tDeadline;

    @FXML
    private TextField tDescription;

    @FXML
    private TextField tDuree;

    @FXML
    private TextField tNom;

    @FXML
    private TextField tPrix;
    @FXML
    private ImageView imgview;
    @FXML
    private ImageView signout;
    @FXML
    void clearFiealds(ActionEvent event) {
        clear();

    }
    private final ProgramService programService = new ProgramService();


    @FXML
    void createProgram(ActionEvent event) {
        String path = Program.path;
        path.replace("\\", "\\\\");
        try {
            programService.ajouter(new Program( tNom.getText(),tDescription.getText(), tDuree.getText(), tDeadline.getValue()
                    ,Float.parseFloat(tPrix.getText()),path));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Programme ajoutée avec succès");
            alert.showAndWait();
            clear();
            showPrograms();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void deleteProgram(ActionEvent event) {
       try{
        programService.supprimer(id);
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Information");
           alert.setContentText("Programme supprimée avec succès");
           alert.showAndWait();
           clear();
           showPrograms();
        }catch (SQLException e){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error");
           alert.setContentText(e.getMessage());
           alert.showAndWait();
        }


    }

    @FXML
    void editProgram(ActionEvent event) {
        String path = Program.path;
        path.replace("\\", "\\\\");
        try {
            programService.modifier(new Program( id,tNom.getText(),tDescription.getText(), tDuree.getText(), tDeadline.getValue()
                    ,Float.parseFloat(tPrix.getText()),path));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Programme modifié avec succès");
            alert.showAndWait();
            clear();
            showPrograms();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


    public void getData(MouseEvent mouseEvent) {
        Program program = table.getSelectionModel().getSelectedItem();
        id = program.getId();
        tNom.setText(program.getNom());
        tDescription.setText(program.getDescription());
        tDuree.setText(program.getDuree());
        tDeadline.setValue(program.getRegistration_deadline());
        tPrix.setText(String.valueOf(program.getPrix()));

        btnAjouter.setDisable(true);
    }
    public void clear(){
        tNom.setText(null);
        tDescription.setText(null);
        tDuree.setText(null);
        tDeadline.setValue(null);
        tPrix.setText(null);
        btnAjouter.setDisable(false);
        imgview.setImage(null);

    }
    @FXML
    void goToEvenement(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GestionEvent.fxml"));
            table.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void gotoClient(ActionEvent event)  {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/ProgClient.fxml"));
            table.getScene().setRoot(parent);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void importer(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));
        File file = fileChooser.showOpenDialog(form_container.getScene().getWindow());

        if(file != null){
            Program.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(),154,120,false,true);
            imgview.setImage(image);

        }

    }

    @FXML
    void updatePic(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));
        File file = fileChooser.showOpenDialog(form_container.getScene().getWindow());

        if(file != null){
            Program.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(),154,120,false,true);
            imgview.setImage(image);

        }
    }
    @FXML
    void gotoExercice(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GestionExercice.fxml"));
            table.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private TextField searchField;
    private void filterData(String query) throws SQLException {
        List<Program> filteredList = new ArrayList<>();
        for (Program program : programService.recuperer()) {
            if (program.getNom().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(program);
            }
        }
        ObservableList<Program> filteredPrograms = FXCollections.observableArrayList(filteredList);
        table.setItems(filteredPrograms); // Update the TableView with filtered data
    }

    @FXML
    void signout(MouseEvent event) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/CrudAdmin.fxml"));
            table.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}