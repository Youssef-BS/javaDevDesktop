package controllers;

import entities.Exercice;
import entities.Program;
import services.ExerciceService;
import services.ProgramService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ExerciceClientController implements Initializable {
    @FXML
    private ImageView backProg;
    @FXML
    private GridPane exerciceGridPane;
    Program program;

    public void setProgram(Program program) {
        this.program = program;
       //displayExerciceCrds();
    }

    @FXML
    private AnchorPane exerciceTable;
    private final ExerciceService exerciceService = new ExerciceService();
    private ObservableList<Exercice> cardListData = FXCollections.observableArrayList();
    public ObservableList<Exercice> exercicesGetData(){
        try {
            List<Exercice> list = exerciceService.recuperer();
            System.out.println(list);
            cardListData = FXCollections.observableArrayList(list);
            return cardListData;
        }catch(SQLException e)
        {
            throw new RuntimeException();
        }

    }
    public void displayExerciceCrds(){
        cardListData.clear();
        cardListData.addAll(exercicesGetData());
        int row = 0;
        int column = 0;
        exerciceGridPane.getRowConstraints().clear();
        exerciceGridPane.getColumnConstraints().clear();
        for (Exercice exercice : cardListData){
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/cardExercice.fxml"));
                AnchorPane pane = load.load();
                cardExerciceController cardC = load.getController();
                cardC.setExercice(exercice);

                if(column == 1){
                    column = 0;
                    row += 1;
                }
                exerciceGridPane.add(pane, column++, row);
                exerciceGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                exerciceGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                exerciceGridPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                exerciceGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                exerciceGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                exerciceGridPane.setMaxHeight(Region.USE_PREF_SIZE);
                exerciceGridPane.setMargin(pane, new Insets(15));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void backProg(MouseEvent event) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/ProgClient.fxml"));
            Parent parent = load.load();
            exerciceTable.getScene().setRoot(parent);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayExerciceCrds();
    }
}
