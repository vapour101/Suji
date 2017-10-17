package event.Music;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

import static javafx.application.Application.launch;

public class PlaysMusic {

    //@Override
    public void start(/*Stage primaryStage*/) throws Exception{
        VBox root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root, 500,200);
        String musicFile = "sound6.mp3";

        System.out.println(System.getProperty("user.dir"));

        Media m = new Media( getClass().getResource("/sound6.mp3").toURI().toString());


        MediaPlayer player = new MediaPlayer(m);
        player.setAutoPlay(true);


        Media sound = new Media(new File(musicFile).toURI().toString());

        MediaView mediaView = new MediaView(player);
        root.getChildren().add(mediaView);


      //  primaryStage.setTitle("Media Player");
      //  primaryStage.setScene(scene);

      //  primaryStage.show();
    }



}
