package controllers;

import entities.Exercice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class cardExerciceController {
    @FXML
    private Button btnPlay;

    @FXML
    private Label description;

    @FXML
    private Label difficulte;
    @FXML
    private Label tnom;

    @FXML
    private Label lblDuration;

    @FXML
    private MediaView mediaView;
    @FXML
    private Slider slider;

    private Media media;
    private MediaPlayer mediaPlayer;
    private Exercice exercice;

    private boolean isPlayed = false;

    @FXML
    void btnPlay(MouseEvent event) {
        if(!isPlayed){
            btnPlay.setText("Pause");
            mediaPlayer.play();
            isPlayed = true;
        }else {
            btnPlay.setText("Play");
            mediaPlayer.pause();
            isPlayed = false;
        }
    }

    @FXML
    void btnStop(MouseEvent event) {
        btnPlay.setText("Play");
        mediaPlayer.stop();
        isPlayed = false;
    }


    public void setExercice(Exercice exercice) {



        this.exercice = exercice;
        tnom.setText(exercice.getNom());
        description.setText(exercice.getDescription());
        difficulte.setText(exercice.getDifficulte());
            media = new Media(exercice.getVideo());
            mediaPlayer = new MediaPlayer(media);

            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.currentTimeProperty().addListener(((observableValue, oldValue, newValue) -> {
                slider.setValue(newValue.toSeconds());
                lblDuration.setText("Duration: " + (int)slider.getValue() + " / " + (int)media.getDuration().toSeconds());
            }));

            mediaPlayer.setOnReady(() ->{
                Duration totalDuration = media.getDuration();
                slider.setMax(totalDuration.toSeconds());
                lblDuration.setText("Duration: 00 / " + (int)media.getDuration().toSeconds());
            });

            /*Scene scene = mediaView.getScene();
            mediaView.fitWidthProperty().bind(scene.widthProperty());
            mediaView.fitHeightProperty().bind(scene.heightProperty());*/

            //mediaPlayer.setAutoPlay(true);



    }

    @FXML
    private void sliderPressed(MouseEvent event){
        mediaPlayer.seek(Duration.seconds(slider.getValue()));
    }

}
