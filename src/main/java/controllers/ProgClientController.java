package controllers;
import entities.Event;
import entities.Program;
import services.MyListener;
import services.ProgramService;
import utils.MyDatabase;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ProgClientController implements Initializable {
    private final ProgramService programService = new ProgramService();
    @FXML
    private GridPane programGridPane;
    @FXML
    private Button evenement;
    private MyListener myListener;
    @FXML
    private ImageView signout;
    @FXML
    private Button confirmer;
    Connection connection = MyDatabase.getInstance().getConnection();
    @FXML
    private AnchorPane programTable;

    @FXML
    private Label tdescription;
    Image image;

    @FXML
    private Label tduree;
    private Program chosenProgram;

    @FXML
    private Label tnom;

    @FXML
    private Label tprix;

    @FXML
    private ImageView imageview;
    private static int idUserClient;

    public void setIdUserClient(int idUserClient) {
        this.idUserClient = idUserClient;
    }

    private ObservableList<Program> cardListData = FXCollections.observableArrayList();

    public ObservableList<Program> programsGetData() {
        try {
            List<Program> list = programService.recupererClientPrograms();
            cardListData = FXCollections.observableArrayList(list);
            return cardListData;
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    public void setchosenProgram(Program program) {
        chosenProgram = program;
        tdescription.setText(program.getDescription());
        tnom.setText(program.getNom());
        tprix.setText(String.valueOf(program.getPrix()) + " DT");
        tduree.setText(program.getDuree());
        String path = "File:" + program.getImgsrc();
        image = new Image(path,266,204,false,true);
        imageview.setImage(image);

    }

    public void displayProgramCrds() {
        cardListData.clear();
        cardListData.addAll(programsGetData());
        if (cardListData.size() > 0) {
            setchosenProgram(cardListData.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(Program program) {
                    setchosenProgram(program);

                }




            };
        }
        int row = 0;
        int column = 0;
        programGridPane.getRowConstraints().clear();
        programGridPane.getColumnConstraints().clear();
        for (Program program : cardListData) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/cardProgram.fxml"));
                AnchorPane pane = load.load();
                cardProgramController cardC = load.getController();
                cardC.setProgram(program, myListener);

                if (column == 3) {
                    column = 0;
                    row += 1;
                }
                programGridPane.add(pane, column++, row);
                programGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                programGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                programGridPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                programGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                programGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                programGridPane.setMaxHeight(Region.USE_PREF_SIZE);
                programGridPane.setMargin(pane, new Insets(5));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void confirmer(ActionEvent event) throws SQLException{
        try {
            if(isUserSubscribed(idUserClient,chosenProgram.getId())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Subscription Error");
                alert.setHeaderText(null);
                alert.setContentText("You are already subscribed to this program!");
                alert.showAndWait();
            }else{
                insertProgramForUser(idUserClient,chosenProgram.getId());
            }
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/ExerciceClient.fxml"));
            Parent parent = load.load();
            ExerciceClientController exC = load.getController();
            exC.setProgram(chosenProgram);
            programTable.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void insertProgramForUser(int userId, int programId) throws SQLException {
        String sql = "INSERT INTO inscription (user_id, program_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, programId);
            pstmt.executeUpdate();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayProgramCrds();



    }
    private boolean isUserSubscribed(int userId, int programId) {

        try {
            String query = "SELECT * FROM inscription WHERE user_id = ? AND program_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, programId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @FXML
    private Button showSubscribedProgramsButton;
    @FXML
    void showSubscribedPrograms(ActionEvent event) throws SQLException {



        // Create a new stage for the window
        Stage stage = new Stage();

        // Create TableView and configure columns
        TableView<Program> table = new TableView<>();
        TableColumn<Program, Integer> colId = new TableColumn<>("ID");
        TableColumn<Program, String> colDescription = new TableColumn<>("DESCRIPTION");
        TableColumn<Program, String> colDuree = new TableColumn<>("DUREE");
        TableColumn<Program, String> colNom= new TableColumn<>("NOM");

        table.getColumns().addAll(colId, colNom, colDescription, colDuree);
        // Populate TableView with subscribed programs
        try {
            List<Program> list = programService.fetchSubscribedPrograms(idUserClient);
            ObservableList<Program> programs = FXCollections.observableArrayList(list);
            table.setItems(programs);
            colId.setCellValueFactory(new PropertyValueFactory<Program, Integer>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<Program, String>("nom"));
            colDescription.setCellValueFactory(new PropertyValueFactory<Program, String>("description"));
            colDuree.setCellValueFactory(new PropertyValueFactory<Program, String>("duree"));

        }
        catch(SQLException e){
            throw new RuntimeException();
        }

        // Add TableView to the scene and set it as the root
        stage.setScene(new Scene(table));
        stage.show();
    }
    @FXML
    void signout(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/UserInterface.fxml"));
            signout.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void gotoevenement(ActionEvent event) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/EvenClient.fxml"));
            Parent parent = load.load();
            EvenClientController pcC = load.getController();
            pcC.setIdUserClient(idUserClient);
            programGridPane.getScene().setRoot(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}