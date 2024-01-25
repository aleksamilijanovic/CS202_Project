package org.example.musicplayer.junit_tests;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.musicplayer.user.Mp3Library;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Mp3LibraryTest {

    @BeforeAll
    public static void setUpJavaFX() {
        // Initialize JavaFX toolkit for headless testing
        JFXPanel jfxPanel = new JFXPanel();
    }

    @Test
    public void testMp3Library() {
        // Create a dummy MediaPlayer
        MediaPlayer mediaPlayer = new MediaPlayer(new Media("dummy"));

        // Create an instance of Mp3Library
        Mp3Library mp3Library = new Mp3Library(mediaPlayer);

        // Check if the Mp3Library is not null
        assertNotNull(mp3Library);

        // Test getSelectedSong() method
        assertEquals(null, mp3Library.getSelectedSong());

        // Test selectSong() method
        mp3Library.selectSong("SampleSong.mp3");
        assertEquals("SampleSong.mp3", mp3Library.getSelectedSong());

        // Test getIndex() method
        assertEquals(-1, mp3Library.getIndex("NonExistingSong.mp3"));
        assertEquals(0, mp3Library.getIndex("SampleSong.mp3"));

        // Test getIndexOfSelectedItem() method
        assertEquals(-1, mp3Library.getIndexOfSelectedItem());

        // Test playSelectedSong() method
        mp3Library.playSelectedSong();

        mediaPlayer.dispose();
    }
}

