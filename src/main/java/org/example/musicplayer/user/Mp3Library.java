package org.example.musicplayer.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.example.musicplayer.song_searching.LocalMp3Handler;
import java.util.List;

public class Mp3Library extends VBox {

    private LocalMp3Handler localMp3Handler;
    private List<String> mp3FileNames;

    private ListView<String> listView;

    public Mp3Library() {
        localMp3Handler = new LocalMp3Handler();
        mp3FileNames = localMp3Handler.listMp3Files();

        initializeListView();

        // Add a listener to handle song selection
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Handle the selected song, you can trigger actions like playing it
                System.out.println("Selected Song: " + newValue);
            }
        });

        // Wrap the ListView in a ScrollPane for scrolling if there are many songs
        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToWidth(true);

        // Set the spacing between songs
        setSpacing(10);

        // Add the ScrollPane to the VBox
        getChildren().add(scrollPane);
    }

    private void initializeListView() {
        listView = new ListView<>();
        listView.getItems().addAll(mp3FileNames);
    }

    public void refreshLibrary() {
        mp3FileNames = localMp3Handler.listMp3Files();
        listView.getItems().setAll(mp3FileNames);
    }

    public String getSelectedSong() {
        return listView.getSelectionModel().getSelectedItem();
    }

    public void selectSong(String songName) {
        listView.getSelectionModel().select(songName);
    }
}
