package org.example.musicplayer.song_searching;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class LocalMp3Handler {

    private static final Logger logger = Logger.getLogger(LocalMp3Handler.class.getName());

    static {
        logger.setLevel(Level.WARNING);
    }

    public static final String MP3_PATH = "src/main/java/org/example/musicplayer/library/";

    public List<String> listMp3Files() {
        List<String> mp3FileNames = new ArrayList<>();

        File mp3Folder = new File(MP3_PATH);
        if (mp3Folder.isDirectory()) {
            File[] files = mp3Folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".mp3")) {
                        mp3FileNames.add(file.getName());
                        extractArtistName(file);
                    }
                }
            }
        }
        return mp3FileNames;
    }

    public String extractArtistName(File mp3File) {
        try {
            AudioFile audioFile = AudioFileIO.read(mp3File);
            Tag tag = audioFile.getTag();
            return tag.getFirst(FieldKey.ARTIST_SORT);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to extract artist name from " + mp3File.getName(), e);
            return null;
        }
    }

}
