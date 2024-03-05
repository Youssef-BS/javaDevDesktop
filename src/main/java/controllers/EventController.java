package controllers;

import entities.Event;
import entities.Exercice;
import entities.Program;
import services.EventService;
import com.calendarfx.view.CalendarView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class EventController {
    int id = 0;
    private final EventService eventService = new EventService();

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
    private ImageView signout;

    @FXML
    private Button btnSupprimer;
    @FXML
    private Button gestionProgramme;

    @FXML
    private TableColumn<Event, Date> colDateDebut;

    @FXML
    private TableColumn<Event, String> colDescription;

    @FXML
    private TableColumn<Event, String> colDuree;

    @FXML
    private TableColumn<Event, Integer> colId;

    @FXML
    private TableColumn<Event, String> colLocalisation;

    @FXML
    private TableColumn<Event, Integer> colMax;

    @FXML
    private TableColumn<Event, String> colNom;

    @FXML
    private TableColumn<Event, Integer> colParticipans;

    @FXML
    private TableColumn<Event, String> colStatus;

    @FXML
    private TableColumn<Event, String> colType;

    @FXML
    private DatePicker tDateDebut;

    @FXML
    private TextField tDescription;

    @FXML
    private TextField tDuree;

    @FXML
    private TextField tLocalisation;

    @FXML
    private TextField tNBmax;

    @FXML
    private TextField tNom;

    @FXML
    private TextField tStatus;

    @FXML
    private TextField tType;
    @FXML
    private TableView<Event> table;
    @FXML
    private ComboBox<Integer> idprogram;
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



    @FXML
    void initialize() throws SQLException {
        showEvents();
        controleSaisie();
        try {
            idprogram.setItems(FXCollections.observableList(eventService.recupererIDPROG()));


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
    public void controleSaisie(){
        tNom.setTextFormatter(new TextFormatter<>(filterNom));
        tDescription.setTextFormatter(new TextFormatter<>(filter));

        tDateDebut.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });
        tNBmax.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tNBmax.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        tStatus.setTextFormatter(new TextFormatter<>(filter));
        tType.setTextFormatter(new TextFormatter<>(filter));
    }
    public void showEvents(){
        try {
            List<Event> list = eventService.recuperer();
            ObservableList<Event> events = FXCollections.observableArrayList(list);
            table.setItems(events);
            colId.setCellValueFactory(new PropertyValueFactory<Event, Integer>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<Event, String>("nom"));
            colDescription.setCellValueFactory(new PropertyValueFactory<Event, String>("description"));
            colDuree.setCellValueFactory(new PropertyValueFactory<Event, String>("duree"));
            colDateDebut.setCellValueFactory(new PropertyValueFactory<Event, Date>("date_debut"));
            colType.setCellValueFactory(new PropertyValueFactory<Event, String>("type"));
            colParticipans.setCellValueFactory(new PropertyValueFactory<Event, Integer>("nb_participants"));
            colMax.setCellValueFactory(new PropertyValueFactory<Event, Integer>("nb_max"));
            colLocalisation.setCellValueFactory(new PropertyValueFactory<Event, String>("localisation"));
            colStatus.setCellValueFactory(new PropertyValueFactory<Event, String>("status"));
        }
        catch(SQLException e){
            throw new RuntimeException();
        }

    }

    @FXML
    void clearFiealds(ActionEvent event) {
        clear();

    }
    @FXML
    void createEvent(ActionEvent event) {
        try {
            eventService.ajouter(new Event( tNom.getText(),tDescription.getText(), tDuree.getText(), tType.getText(), tDateDebut.getValue()
                    ,0,Integer.parseInt(tNBmax.getText()), tLocalisation.getText(), tStatus.getText(),idprogram.getValue()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Evenement ajoutée avec succès");
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
    void deleteEvent(ActionEvent event) {
        try{
            eventService.supprimer(id);
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
    void editEvent(ActionEvent event) {
        try {
            eventService.modifier(new Event( id,tNom.getText(),tDescription.getText(), tDuree.getText(), tType.getText(), tDateDebut.getValue()
                    ,0,Integer.parseInt(tNBmax.getText()),tLocalisation.getText(),tStatus.getText(),idprogram.getValue()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Evenement ajoutée avec succès");
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
    public void getData(MouseEvent mouseEvent) {
        Event event = table.getSelectionModel().getSelectedItem();
        id = event.getId();
        tNom.setText(event.getNom());
        tDescription.setText(event.getDescription());
        tDuree.setText(event.getDuree());
        tType.setText(event.getType());
        tDateDebut.setValue(event.getDate_debut());
        tNBmax.setText(String.valueOf(event.getNb_max()));
        tLocalisation.setText(event.getLocalisation());
        tStatus.setText(event.getStatus());
        btnAjouter.setDisable(true);
    }
    public void clear(){
        tNom.setText(null);
        tDescription.setText(null);
        tDuree.setText(null);
        tDateDebut.setValue(null);
        tType.setText(null);
        tNBmax.setText(null);
        tLocalisation.setText(null);
        tStatus.setText(null);
        btnAjouter.setDisable(false);

    }
    @FXML
    void goToProgramme(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/GestionProgram.fxml"));
            table.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void gotoClient(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/EvenClient.fxml"));
            table.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private TextField searchField;
    private void filterData(String query) throws SQLException {
        List<Event> filteredList = new ArrayList<>();
        for (Event event : eventService.recuperer()) {
            if (event.getNom().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(event);
            }
        }
        ObservableList<Event> filteredExercices = FXCollections.observableArrayList(filteredList);
        table.setItems(filteredExercices); // Update the TableView with filtered data
    }
    @FXML
    void triIid(ActionEvent event) throws SQLException {

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
