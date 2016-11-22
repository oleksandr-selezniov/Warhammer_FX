package MusicPlayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Random;

/**
 * Created by Dmitriy on 23.10.2016.
 */
public class MusicPlayerLogic {
    private static Random random = new Random();
    private static int fileNumber=random.nextInt(13);
    private static MediaPlayer mediaPlayer; // = new MediaPlayer(getMedia(System.getProperty("user.dir")+"\\MusicPlayer\\Music\\"+fileNumber+".mp3"));

    public static HBox getMusicPlayer(){
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        vBox.setMaxWidth(100);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(5));
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
        });

        Button next = getNextButton();
        Button prev = getPrevButton();
        Button pause = getPauseButton();
        Button play = getPlayButton();
        Slider volSlider = getVolSlider();

        vBox.getChildren().setAll(prev, play, pause, next);
        hBox.getChildren().addAll(volSlider, vBox);
        return hBox;
    }

    private static int getNextFileN(){
        if (fileNumber != 12){
            fileNumber = fileNumber+1;
        }
        return fileNumber;
    }

    private static int getPrevFileN(){
        if (fileNumber != 0){
        fileNumber = fileNumber-1;
        }
        return fileNumber;
    }

    private static Button getPlayButton(){
        Button play = new Button("Play");
        play.setMinWidth(50);
        play.setPadding(new Insets(5));
        play.setOnAction(e -> mediaPlayer.play());
    return play;
    }

    private static Button getPauseButton(){
        Button pause =new Button("Pause");
        pause.setMinWidth(50);
        pause.setPadding(new Insets(5));
        pause.setOnAction(e -> mediaPlayer.pause());
        return pause;
    }

    private static Button getNextButton(){
        Button next = new Button("Next");
        next.setMinWidth(50);
        next.setPadding(new Insets(5));
        next.setOnAction(e -> {
            mediaPlayer.dispose();
            mediaPlayer = new MediaPlayer(getMedia(System.getProperty("user.dir")+"\\MusicPlayer\\Music\\"+getNextFileN()+".mp3"));
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.play();
            });

        });
        return next;
    }

    private static Button getPrevButton(){
        Button prev = new Button("Prev");
        prev.setMinWidth(50);
        prev.setPadding(new Insets(5));
        prev.setOnAction(e -> {
            mediaPlayer.dispose();
            mediaPlayer = new MediaPlayer(getMedia(System.getProperty("user.dir")+"\\MusicPlayer\\Music\\"+getPrevFileN()+".mp3"));
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.play();
            });

        });
        return prev;
    }

    private static Slider getVolSlider(){
        Slider slider = new Slider();
        slider.setPrefWidth(15);
        slider.setPrefHeight(30);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setCursor(Cursor.HAND);
        slider.setMin(0);
        slider.setMax(1);
        slider.setValue(1);
        slider.setMajorTickUnit(0.1);
        slider.setBlockIncrement(0.1);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue());
        });
        return slider;
    }

    private static Media getMedia(String path){
        String mediaUrl = null;
        try {
            File file = new File(path);
            mediaUrl = file.toURI().toURL().toString();
        }catch (Exception eq){eq.printStackTrace();}
        return new Media(mediaUrl);
    }

}
