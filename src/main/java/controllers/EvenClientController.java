package controllers;

import entities.Event;
import entities.Program;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.EventService;
import services.MyListener;
import services.SelectedEvent;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EvenClientController implements Initializable {


    Connection connection = MyDatabase.getInstance().getConnection();
    private final EventService eventService = new EventService();
    @FXML
    private GridPane eventGridPane;
    private SelectedEvent selectedEvent;
    @FXML
    private Label datedebut;
    @FXML
    private Button gotoprogrammesbtn;
    @FXML
    private Label dispo;

    @FXML
    private ImageView signout;

    private Event chosenEvent ;
    @FXML
    private AnchorPane eventTable;

    @FXML
    private Label localisation;

    @FXML
    private ImageView qrcodeimg;
    private Image image;
    private static int idUserClient;

    public void setIdUserClient(int idUserClient) {
        this.idUserClient = idUserClient;
    }
    @FXML
    private Label tnom;
    @FXML
    private Button calendrier;
    @FXML
    void displaycalendar(ActionEvent actionEvent) {
        Stage primaryStage = new Stage();
        CalendarView calendarView = new CalendarView();
        Calendar calendar = new Calendar("Events");
        CalendarSource calendarSource = new CalendarSource("Event Source");
        calendarSource.getCalendars().add(calendar);

        try {
            List<Event> events = eventService.recuperer();
            for (Event event : events) {
                Entry<String> event1 = new Entry<>(event.getNom());
                event1.changeStartDate(event.getDate_debut());



                //CalendarEvent calendarEvent = new CalendarEvent(event.getNom(), event.getDate_debut());
                calendar.addEntry(event1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        calendarView.getCalendarSources().add(calendarSource);

        Scene scene = new Scene(calendarView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Event Calendar");
        primaryStage.show();

    }


    private ObservableList<Event> cardListData = FXCollections.observableArrayList();
    public ObservableList<Event> eventGetData(){
        try {
            List<Event> list = eventService.recuperer();
            cardListData = FXCollections.observableArrayList(list);
            return cardListData;
        }catch(SQLException e)
        {
            throw new RuntimeException();
        }

    }

    public void insertEventForUser(int userId, int eventId) throws SQLException {
        String sql = "INSERT INTO participation (event_id,user_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1,eventId);
            pstmt.setInt(2, userId );
            pstmt.executeUpdate();
        }
    }
    public static void generateQRCode(String data, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

        String path = "C:/Users/youssef/Desktop/PI/src/main/resources/qrcode.png";
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Paths.get(path));
    }
    public void setchosenEvent(Event event) {
        tnom.setText(event.getNom());
        datedebut.setText(String.valueOf(event.getDate_debut()));
        dispo.setText(String.valueOf(event.getNb_max() - event.getNb_participants()));
        localisation.setText(event.getLocalisation());
        String data = "Id user" + idUserClient +
                "Id Event: " + event.getId() +
                "Nom evenement : "  + event.getNom() +
                "Participant nb : " + String.valueOf(event.getNb_max() - event.getNb_participants()) +
                "****Bienvenue***";

        try {
            generateQRCode(data,200,200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        image = new Image("File:"+"C:/Users/youssef/Desktop/PI/src/main/resources/qrcode.png",200,200,false,true);
        qrcodeimg.setImage(image);

        chosenEvent = event;
    }
    public void displayEventCrds(){
        cardListData.clear();
        cardListData.addAll(eventGetData());
        if (cardListData.size() > 0) {
            //setchosenEvent(cardListData.get(0));
            selectedEvent = new SelectedEvent() {
                @Override
                public void onClickListener(Event event) {

                        setchosenEvent(event);



                }




            };
        }
        int row = 0;
        int column = 0;
        eventGridPane.getRowConstraints().clear();
        eventGridPane.getColumnConstraints().clear();
        for (Event event : cardListData){
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/cardEvent.fxml"));
                AnchorPane pane = load.load();
                cardEventController cardC = load.getController();
                cardC.setEvent(event,selectedEvent);

                if(column == 1){
                    column = 0;
                    row += 1;
                }
                eventGridPane.add(pane, column++, row);

                eventGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                eventGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                eventGridPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                eventGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                eventGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                eventGridPane.setMaxHeight(Region.USE_PREF_SIZE);
                eventGridPane.setMargin(pane, new Insets(20));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void signout(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/welcome.fxml"));
            eventTable.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void gotoProgramscl(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/ProgClient.fxml"));
            Parent parent = load.load();
            eventTable.getScene().setRoot(parent);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void confirmerParticip(ActionEvent event) {
        try{
        if(isUserParticipated(idUserClient,chosenEvent.getId()) == true)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Subscription Error");
            alert.setHeaderText(null);
            alert.setContentText("You are already participated to this event!");
            alert.showAndWait();


        }
        else{
            insertEventForUser(idUserClient,chosenEvent.getId());
        }}catch (SQLException e){}
    }
    private boolean isUserParticipated(int userId, int eventId) {

        try {
            String query = "SELECT * FROM participation WHERE user_id = ? AND event_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @FXML
    void showParticipatedEvents(ActionEvent event) {
        // Create a new stage for the window
        Stage stage = new Stage();

        // Create TableView and configure columns
        TableView<Event> table = new TableView<>();
        TableColumn<Event, Integer> colId = new TableColumn<>("ID");
        TableColumn<Event, String> colDescription = new TableColumn<>("DESCRIPTION");
        TableColumn<Event, String> colDuree = new TableColumn<>("DUREE");
        TableColumn<Event, String> colNom= new TableColumn<>("NOM");

        table.getColumns().addAll(colId, colNom, colDescription, colDuree);
        // Populate TableView with subscribed programs
        try {
            List<Event> list = eventService.fetchParticipatedEvents(idUserClient);
            ObservableList<Event> events = FXCollections.observableArrayList(list);
            table.setItems(events);
            colId.setCellValueFactory(new PropertyValueFactory<Event, Integer>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<Event, String>("nom"));
            colDescription.setCellValueFactory(new PropertyValueFactory<Event, String>("description"));
            colDuree.setCellValueFactory(new PropertyValueFactory<Event, String>("duree"));

        }
        catch(SQLException e){
            throw new RuntimeException();
        }

        // Add TableView to the scene and set it as the root
        stage.setScene(new Scene(table));
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayEventCrds();
    }
}
