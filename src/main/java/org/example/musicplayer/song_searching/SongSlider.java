package org.example.musicplayer.song_searching;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;



public class SongSlider extends Slider {

    private static final Duration SLIDER_UPDATE_INTERVAL = Duration.millis(500);


    private MediaPlayer mediaPlayer;
    private final Timeline sliderUpdateTimeline;

    public SongSlider(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        System.out.println(this.mediaPlayer);
        System.out.println(mediaPlayer);
        sliderUpdateTimeline = new Timeline(new KeyFrame(SLIDER_UPDATE_INTERVAL, event -> updateSlider()));
        initializeSlider();
    }

    private void initializeSlider() {
        setMin(0.0);
        setValue(0.0);

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                sliderUpdateTimeline.play();
            } else {
                sliderUpdateTimeline.pause();
            }
        });

        mediaPlayer.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == MediaPlayer.Status.PLAYING) {
                sliderUpdateTimeline.play();
            } else {
                if (oldValue == MediaPlayer.Status.PLAYING) {
                    // The song was playing, but now it's paused or stopped
                    sliderUpdateTimeline.pause();
                }
            }
        });

        valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
            }
        });
    }



    public void updateSlider() {
        System.out.println("Media player: " + mediaPlayer);
        System.out.println("Media status: " + mediaPlayer.getStatus());
        System.out.println("Timeline status: " + sliderUpdateTimeline.getStatus());
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            Duration totalDuration = mediaPlayer.getMedia().getDuration();
            Duration currentDuration = mediaPlayer.getCurrentTime();

            if (totalDuration.toMillis() > 0) {
                double progress = currentDuration.toMillis() / totalDuration.toMillis();
                double newValue = progress * totalDuration.toSeconds();

                setValue(newValue);

                System.out.println("Slider updated: " + newValue);
            }
        }
    }


    public void resetSlider() {
        setValue(0.0);
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public Timeline getSliderUpdateTimeline() {
        return sliderUpdateTimeline;
    }
}
