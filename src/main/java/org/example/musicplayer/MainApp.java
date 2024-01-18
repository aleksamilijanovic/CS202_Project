package org.example.musicplayer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.musicplayer.song_searching.LocalMp3Handler;
import org.example.musicplayer.song_searching.SpotifyApiHandler;

import java.io.File;
import java.util.List;


public class MainApp extends Application {
    @Override
    public void start(Stage stage) {

        stage.setTitle("Music Player");
        Button playButton = new Button("Play");
        Button stopButton = new Button("Stop");

        playButton.setOnAction(event -> playMusic());
        stopButton.setOnAction(event -> stopMusic());


        VBox root = new VBox(10);
        root.getChildren().addAll(playButton, stopButton);

        Scene scene = new Scene(root, 300, 200);

        stage.setScene(scene);
        stage.show();
    }

    private void playMusic() {
        String accessToken = SpotifyApiHandler.getAccessToken();
        if(accessToken != null){
            LocalMp3Handler localMp3Handler = new LocalMp3Handler();
            List<String> mp3FileNames = localMp3Handler.listMp3Files();

            if(!mp3FileNames.isEmpty()){
                for(String mp3FileName : mp3FileNames){
                    String artistName = localMp3Handler.extractArtistName(new File(LocalMp3Handler.MP3_PATH + mp3FileName));
                    SpotifyApiHandler spotifyApiHandler = new SpotifyApiHandler(accessToken);
                    spotifyApiHandler.getTrackInfo(accessToken,"Vermilion", "Slipknot");
                    System.out.println("Playing " + mp3FileName);
                }
            } else {
                System.out.println("No MP3 files found");
            }
        } else {
            System.out.println("Failed to retrieve access token");
        }
    }

    private void stopMusic() {
        System.out.println("Stopping playback");
    }

    public static void main(String[] args) {

        launch();
    }
}