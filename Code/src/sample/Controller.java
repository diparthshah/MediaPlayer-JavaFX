package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import java.io.File;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class Controller implements Initializable{

    @FXML private MediaView mediaView;

    @FXML private MediaPlayer player;

    @FXML private Media media;

    @FXML private Slider volumeSlider;

    @FXML private Slider progressSlider;

    @Override
    public void initialize(URL locate, ResourceBundle resources){
        String path = new File("src/sample.mp4").getAbsolutePath();
        media = new Media (new File (path).toURI().toString());
        player = new MediaPlayer(media);
        mediaView.setMediaPlayer(player);
        player.setAutoPlay(false);

        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();

        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

        volumeSlider.setValue(player.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable observable) {
                player.setVolume(volumeSlider.getValue() / 100);
            }

        });

        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                progressSlider.setValue(newValue.toSeconds());
            }
        });

        progressSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                player.seek(Duration.seconds(progressSlider.getValue()));
            }
        });
    }

    public void play(){
        player.seek(player.getCurrentTime());
        player.play();
        System.out.println("LOG :: MEDIA PLAY STARTED");
    }

    public void stop(){
        player.stop();
        System.out.println("LOG :: MEDIA PLAY STOPPED");
    }

    public void pause(){
        player.pause();
        System.out.println("LOG :: MEDIA PLAY PAUSED");
    }

    public void reload(){
        player.seek(player.getStartTime());
        player.play();
        System.out.println("LOG :: MEDIA PLAY RELOADED");
    }

    public void pickFile(){

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4/MP3", "*.mp4","*.mp3"));
        File video = fc.showOpenDialog(null);
        String fileName = video.getPath();
        if (video != null){
            System.out.println("LOG :: SELECTED FILE : "+fileName);
            player.dispose();
            media = new Media (new File(video.getAbsolutePath()).toURI().toString());
            player = new MediaPlayer (media);
            mediaView.setMediaPlayer(player);
            player.play();
        }

    }
}