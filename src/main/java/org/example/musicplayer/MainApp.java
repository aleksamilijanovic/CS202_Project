package org.example.musicplayer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.musicplayer.song_searching.LocalMp3Handler;
import org.example.musicplayer.song_searching.SpotifyApiHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;

public class MainApp extends Application {

    private LocalMp3Handler localMp3Handler;
    private List<String> mp3FileNames;
    private int currentSongIndex = 0;

    private Duration currentDuration;

    private MediaPlayer mediaPlayer;
    @Override
    public void start(Stage stage) {

        stage.setTitle("Music Player");
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button nextButton = new Button("Next Song");
        Button previousButton = new Button("Previous Song");

        ToggleButton playToggleButton = new ToggleButton("Play");

        playToggleButton.setOnAction(event -> {
            if(playToggleButton.isSelected()){
                playMusic();
            } else {
                pauseMusic();
            }
        });

        playButton.setOnAction(event -> playMusic());
        pauseButton.setOnAction(event -> pauseMusic());
        nextButton.setOnAction(event -> playNextSong());
        previousButton.setOnAction(event -> playPreviousSong());

        VBox root = new VBox(10);
        root.getChildren().addAll(previousButton ,playToggleButton, nextButton );

        Scene scene = new Scene(root, 300, 200);

        stage.setScene(scene);
        stage.show();

        localMp3Handler = new LocalMp3Handler();
        mp3FileNames = localMp3Handler.listMp3Files();
    }
    private void playMusic() {
        String accessToken = SpotifyApiHandler.getAccessToken();
        if(accessToken != null){

            if(!mp3FileNames.isEmpty()){
                String mp3FileName = mp3FileNames.get(currentSongIndex);
                String artistName = localMp3Handler.extractArtistName(new File(LocalMp3Handler.MP3_PATH + mp3FileName));
                    SpotifyApiHandler spotifyApiHandler = new SpotifyApiHandler(accessToken);
                    spotifyApiHandler.getTrackInfo(accessToken, mp3FileName, artistName);

                    //Konstruisanje file path-a
                    String contentRoot = "src/main/java/org/example/musicplayer/library";

                    String filePath = new File(contentRoot, mp3FileName).toURI().toString();

                    Media media = new Media(filePath);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                mediaPlayer = new MediaPlayer(media);


                if(currentDuration != null){
                        mediaPlayer.seek(currentDuration);
                    }

                    mediaPlayer.play();

                    System.out.println("Playing " + mp3FileName);
                }
             else {
                System.out.println("No MP3 files found");
            }
        } else {
            System.out.println("Failed to retrieve access token");
    }
    }
    private void pauseMusic() {
        if(mediaPlayer != null){
            currentDuration = mediaPlayer.getCurrentTime();
            mediaPlayer.pause();
        }
        System.out.println("Stopping playback");
    }

    private void playNextSong() {
        if(!mp3FileNames.isEmpty()){
            currentSongIndex = (currentSongIndex + 1) % mp3FileNames.size();
            String nextSong = mp3FileNames.get(currentSongIndex);
            stopMediaPlayer();
            System.out.println("Next song: " + nextSong);
            playMusic();
        }
    }

    private void playPreviousSong() {
        if(!mp3FileNames.isEmpty()){
            currentSongIndex = (currentSongIndex - 1) % mp3FileNames.size();
            String previousSong = mp3FileNames.get(currentSongIndex);
            stopMediaPlayer();
            System.out.println("Previous song: " + previousSong);
            playMusic();
        }
    }

    private void stopMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }

    public static void main(String[] args) {

        launch();
    }
}