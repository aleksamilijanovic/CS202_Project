package org.example.musicplayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.musicplayer.likingsongs.LikedSongs;



import java.time.Duration;
import java.util.List;

public class LikedSongWindow extends Stage {

    public LikedSongWindow(List<LikedSongs> likedSongs) {
        setTitle("Liked Songs");

        TableView<LikedSongs> tableView = new TableView<>();
        TableColumn<LikedSongs, String> nameColumn = new TableColumn<>("Name");
        TableColumn<LikedSongs, String> artistColumn = new TableColumn<>("Artist");
        TableColumn<LikedSongs, String> albumColumn = new TableColumn<>("Album");
        TableColumn<LikedSongs, Duration> durationColumn = new TableColumn<>("Duration");

        tableView.getColumns().addAll(nameColumn, artistColumn, albumColumn, durationColumn);

        ObservableList<LikedSongs> likedSongItems = FXCollections.observableArrayList();
        for (LikedSongs likedSong : likedSongs) {
            likedSongItems.add(likedSong);
            System.out.println("Added liked song to list: " + likedSong.getName());
        }

        tableView.setItems(likedSongItems);

        VBox root = new VBox(tableView);
        Scene scene = new Scene(root, 500, 500);

        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UTILITY);
    }
}
