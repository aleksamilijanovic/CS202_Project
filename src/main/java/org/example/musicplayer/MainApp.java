package org.example.musicplayer;


import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.musicplayer.likingsongs.LikedSongs;
import org.example.musicplayer.song_searching.LocalMp3Handler;
import org.example.musicplayer.song_searching.SongSlider;
import org.example.musicplayer.song_searching.SpotifyApiHandler;
import org.example.musicplayer.user.Mp3Library;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    private LikedSongWindow likedSongWindow;
    private LocalMp3Handler localMp3Handler;
    private List<String> mp3FileNames;
    private int currentSongIndex = 0;

    private Duration currentDuration;
    private MediaPlayer mediaPlayer;

    private ImageView albumCoverImage;

    private SongSlider songSlider;

    private Mp3Library mp3Library;

    private List<LikedSongs> likedSongs = new ArrayList<>();

    private SpotifyApiHandler spotifyApiHandler;


    @Override
    public void start(Stage stage) {
        stage.setTitle("Music Player");

        Button playSong = new Button("Play Song");
        Button pauseSong = new Button("Pause Song");
        Button nextButton = new Button("Next Song");
        Button previousButton = new Button("Previous Song");
        Button logOutButton = new Button("Log Out");

        Button likeSongButton = new Button("Like Song");
        Button likedSongsButton = new Button("Liked Songs");



        localMp3Handler = new LocalMp3Handler();
        mp3FileNames = localMp3Handler.listMp3Files();






        initializeMediaPlayer();
        songSlider = new SongSlider(mediaPlayer);



        playSong.setOnAction(event -> playMusic());
        pauseSong.setOnAction(event -> pauseMusic());

        nextButton.setOnAction(event -> playNextSong());
        previousButton.setOnAction(event -> playPreviousSong());

        likeSongButton.setOnAction(event -> likeCurrentSong());
        likedSongsButton.setOnAction(event -> showLikedSongs());

        logOutButton.setOnAction(event -> {
            stage.close();
            RegisterLoginScreen registerLoginScreen = new RegisterLoginScreen();
            registerLoginScreen.start(new Stage());

        });

        albumCoverImage = new ImageView();
        albumCoverImage.setFitHeight(200);
        albumCoverImage.setFitWidth(200);

        mp3Library = new Mp3Library(mediaPlayer);


        VBox albumCoverBox = new VBox(albumCoverImage);
        albumCoverBox.setAlignment(Pos.BOTTOM_LEFT);


        HBox controlButtons = new HBox(10);
        controlButtons.getChildren().addAll(previousButton, playSong, pauseSong, nextButton, likeSongButton, likedSongsButton);
        controlButtons.setAlignment(Pos.BOTTOM_CENTER);

        VBox root = new VBox(10);
        root.getChildren().addAll(logOutButton);
        VBox.setMargin(logOutButton, new Insets(10,10,10,10));

        VBox libraryBox = new VBox(mp3Library);
        libraryBox.setAlignment(Pos.CENTER_RIGHT);
        libraryBox.setPadding(new Insets(10,10,10,10));

        HBox topBar = new HBox(logOutButton);
        topBar.setAlignment(Pos.TOP_RIGHT);

        HBox bottomBar = new HBox(albumCoverImage, songSlider,controlButtons);
        bottomBar.setAlignment(Pos.BOTTOM_CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBar);
        borderPane.setBottom(bottomBar);
        borderPane.setRight(libraryBox);

        HBox.setHgrow(albumCoverImage, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(controlButtons, javafx.scene.layout.Priority.ALWAYS);

        Scene scene = new Scene(borderPane, 600, 400);

        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeMediaPlayer() {
        if (!mp3FileNames.isEmpty()) {
            String mp3FileName = mp3FileNames.get(currentSongIndex);
            String contentRoot = "src/main/java/org/example/musicplayer/library";
            String filePath = new File(contentRoot, mp3FileName).toURI().toString();

            mediaPlayer = new MediaPlayer(new Media(filePath));
            spotifyApiHandler = new SpotifyApiHandler(SpotifyApiHandler.getAccessToken());
        }
    }

    private void playMusic() {
        String accessToken = SpotifyApiHandler.getAccessToken();
        if (accessToken != null) {
            if (!mp3FileNames.isEmpty()) {
                String mp3FileName = mp3FileNames.get(currentSongIndex);
                String artistName = localMp3Handler.extractArtistName(new File(LocalMp3Handler.MP3_PATH + mp3FileName));
                SpotifyApiHandler spotifyApiHandler = new SpotifyApiHandler(accessToken);
                spotifyApiHandler.getTrackInfo(accessToken, mp3FileName, artistName);

                String albumCoverUrl = spotifyApiHandler.getAlbumCoverUrl();

                System.out.println(albumCoverUrl);

                loadAlbumCoverImage(albumCoverUrl);

                if (mediaPlayer == null) {
                    initializeMediaPlayer();
                }

                if (currentDuration != null) {
                    mediaPlayer.seek(currentDuration);
                }

                mediaPlayer.play();

                System.out.println("Playing " + mp3FileName);

                String selectedSong = mp3Library.getSelectedSong();
                mp3Library.selectedCurrentlyPlayingSong(selectedSong);

                songSlider.setMediaPlayer(mediaPlayer);

                songSlider.updateSlider();

                songSlider.getSliderUpdateTimeline().play();

            } else {
                System.out.println("No MP3 files found");
            }
        } else {
            System.out.println("Failed to retrieve access token");
        }
    }

    private void loadAlbumCoverImage(String albumCoverUrl) {
        Image image = new Image(albumCoverUrl);
        albumCoverImage.setImage(image);
    }

    private void pauseMusic() {
        if (mediaPlayer != null) {
                currentDuration = mediaPlayer.getCurrentTime();
                mediaPlayer.pause();
                songSlider.getSliderUpdateTimeline().pause();
                System.out.println("Pausing song");
        } else {
            System.out.println("No song is playing");
        }
    }

    private void playNextSong() {
        if (!mp3FileNames.isEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % mp3FileNames.size();
            String nextSong = mp3FileNames.get(currentSongIndex);
            songSlider.resetSlider();
            stopMediaPlayer();
            System.out.println("Next song: " + nextSong);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(event -> {
                initializeMediaPlayer();
                songSlider.setMediaPlayer(mediaPlayer);
                playMusic();
                songSlider.getSliderUpdateTimeline().play();
            });
            pause.play();
        }

    }

    private void playPreviousSong() {
        if (!mp3FileNames.isEmpty()) {
            currentSongIndex = (currentSongIndex - 1 + mp3FileNames.size()) % mp3FileNames.size();
            String previousSong = mp3FileNames.get(currentSongIndex);
            songSlider.resetSlider();
            System.out.println("1");
            stopMediaPlayer();
            System.out.println("Previous song: " + previousSong);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(event -> playMusic());
            System.out.println("2");
            initializeMediaPlayer();
            songSlider.setMediaPlayer(mediaPlayer);
            playMusic();
            songSlider.getSliderUpdateTimeline().play();
            pause.play();
        }

    }

    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            songSlider.getSliderUpdateTimeline().stop();
        }
    }

//    private void updateSliderForCurrentSong() {
//        if (mediaPlayer != null && mediaPlayer.getMedia() != null) {
//            System.out.println("Main Media player: " + mediaPlayer);
//            System.out.println("Main Media : " + mediaPlayer.getMedia());
//            Duration totalDuration = mediaPlayer.getMedia().getDuration();
//            double progress = mediaPlayer.getCurrentTime().toMillis() / totalDuration.toMillis();
//            songSlider.setValue(progress * totalDuration.toSeconds());
//        }
//    }

    private void likeCurrentSong() {
        if (!mp3FileNames.isEmpty()) {
            String currentSong = mp3FileNames.get(currentSongIndex);
            if (!isSongLiked(currentSong)) {
                LikedSongs likedSong = fetchLikedSongDetails(currentSong);
                likedSongs.add(likedSong);
                System.out.println("Liked song: " + likedSong.getName());
            } else {
                System.out.println("Song already liked");
            }
            System.out.println("Liked Songs List: " + likedSongs);
        }
    }

    private boolean isSongLiked(String songName) {
        return likedSongs.stream().anyMatch(likedSong -> likedSong.getName().equals(songName));
    }


    private void showLikedSongs() {
        if (likedSongWindow == null) {
            likedSongWindow = new LikedSongWindow(likedSongs);
        }
        likedSongWindow.show();
    }

    private LikedSongs fetchLikedSongDetails(String songName) {
        String artist = spotifyApiHandler.getFoundArtistName(songName);
        String album = spotifyApiHandler.getAlbumName(songName);
        java.time.Duration duration = spotifyApiHandler.getDuration(songName);

        System.out.println("Fetched details for liked song - Name: " + songName + ", Artist: " + artist + ", Album: " + album + ", Duration: " + duration);
        return new LikedSongs(songName, artist, album, duration, spotifyApiHandler.getAlbumCoverUrl());
    }


    public static void main(String[] args) {
        launch();
    }
}
