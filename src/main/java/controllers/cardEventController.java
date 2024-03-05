package controllers;

        import entities.Event;
        import entities.Program;
        import services.EventService;
        import services.SelectedEvent;
        import com.google.zxing.BarcodeFormat;
        import com.google.zxing.WriterException;
        import com.google.zxing.client.j2se.MatrixToImageWriter;
        import com.google.zxing.common.BitMatrix;
        import com.google.zxing.qrcode.QRCodeWriter;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.Alert;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.input.MouseEvent;

        //import java.awt.event.MouseEvent;
        import java.io.IOException;
        import java.net.URL;
        import java.nio.file.Paths;
        import java.sql.SQLException;
        import java.util.ResourceBundle;

public class cardEventController implements Initializable {

    @FXML
    private Label deadlineP;

    @FXML
    private Label descriptionP;

    @FXML
    private Label dureeP;

    @FXML
    private Label nbMax;

    @FXML
    private Label nomP;
    private SelectedEvent selectedEvent;
    EventService eventService = new EventService();

    @FXML
    private Button participerbtn;

    private Event event;
    public void setEvent(Event event,SelectedEvent selectedEvent){
        this.event = event;
        this.selectedEvent=selectedEvent;
        nomP.setText(event.getNom());
        descriptionP.setText(event.getDescription());
        dureeP.setText(event.getDuree());
        nbMax.setText(String.valueOf(event.getNb_max()));
        deadlineP.setText(String.valueOf(event.getDate_debut()));
    }
    @FXML
    void click(MouseEvent mouseEvent) {
    //    selectedEvent.onClickListener(event);

    }
    @FXML
    void choose(MouseEvent mouseEvent) throws SQLException {
        if (event.getNb_max() - event.getNb_participants() > 0)
        {
        selectedEvent.onClickListener(event);
        eventService.updateParticipant(event.getId());
        participerbtn.setDisable(true);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information");
            alert.setContentText("Nombre Participants maximal");
            alert.showAndWait();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
