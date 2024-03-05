package controllers;

import entities.Exercice;
import entities.Program;
import services.MyListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class cardProgramController implements Initializable {

    @FXML
    private Label deadlineP;

    @FXML
    private Label descriptionP;

    @FXML
    private Label dureeP;

    @FXML
    private Label nomP;
    private MyListener myListener;


    @FXML
    private Button participerbtn;

    @FXML
    private Label prixP;

    @FXML
    private ImageView imgview;
    private Program program;
    Image image;

    public void setProgram(Program program,MyListener myListener){
        this.program = program;
        this.myListener = myListener;
        nomP.setText(program.getNom());
        descriptionP.setText(program.getDescription());
        dureeP.setText(program.getDuree());
        prixP.setText(String.valueOf(program.getPrix()));
        deadlineP.setText(String.valueOf(program.getRegistration_deadline()));
        String path = "File:" + program.getImgsrc();
        image = new Image(path,301,140,false,true);
        imgview.setImage(image);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }


    @FXML
    void click(MouseEvent event) {
        myListener.onClickListener(program);
    }
    @FXML
    void choose(MouseEvent event) {
        myListener.onClickListener(program);
    }

}