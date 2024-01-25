package org.example.musicplayer.user;

import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.musicplayer.song_searching.LocalMp3Handler;

import java.io.File;
import java.util.List;

public class Mp3Library extends VBox {

    private MediaPlayer mediaPlayer;
    private LocalMp3Handler localMp3Handler;
    private List<String> mp3FileNames;

    private ListView<String> listView;

    public Mp3Library(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        localMp3Handler = new LocalMp3Handler();
        mp3FileNames = localMp3Handler.listMp3Files();

        initializeListView();

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                playSelectedSong();
            }
        });

        // Add a listener to handle song selection
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Handle the selected song, you can trigger actions like playing it
            System.out.println("Selected Song: " + newValue);
        });

        // Wrap the ListView in a ScrollPane for scrolling if there are many songs
        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToWidth(true);

        // Set the spacing between songs
        setSpacing(10);

        // Add the ScrollPane to the VBox
        getChildren().add(scrollPane);
    }

    public void playSelectedSong() {
        String selectedSong = listView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            // Trigger the action to play the selected song
            System.out.println("Playing selected song: " + selectedSong);
            mediaPlayer.stop();  // Stop the current playback
            String contentRoot = "src/main/java/org/example/musicplayer/library";
            String filePath = new File(contentRoot, selectedSong).toURI().toString();
            mediaPlayer.dispose();  // Dispose of the current MediaPlayer instance
            mediaPlayer = new MediaPlayer(new Media(filePath));  // Create a new Media instance
            mediaPlayer.play();  // Start playback
        }
    }

    public void selectedCurrentlyPlayingSong(String currentlyPlayingSong) {
        listView.getSelectionModel().select(currentlyPlayingSong);
    }

    private void initializeListView() {
        listView = new ListView<>();
        listView.getItems().addAll(mp3FileNames);
    }


    public String getSelectedSong() {
        return listView.getSelectionModel().getSelectedItem();
    }

    public void selectSong(String songName) {
        listView.getSelectionModel().select(songName);
    }


    public int getIndex(String target) {
        return mp3FileNames.indexOf(target);
    }

    public int getIndexOfSelectedItem() {
        return mp3FileNames.indexOf(listView.getSelectionModel().getSelectedItem());
    }
}
