package org.example.musicplayer.song_searching;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalMp3Handler {

    public static final String MP3_PATH = "src/main/java/org/example/musicplayer/library/";

    public List<String> listMp3Files() {
        List<String> mp3FileNames = new ArrayList<>();

        File mp3Folder = new File(MP3_PATH);
        if(mp3Folder.isDirectory()) {
            File[] files = mp3Folder.listFiles();
            if(files != null) {
                for(File file : files) {
                    if(file.isFile() && file.getName().endsWith(".mp3")) {
                        mp3FileNames.add(file.getName());
                    }
                }
            }
        }
        return  mp3FileNames;
    }
}
