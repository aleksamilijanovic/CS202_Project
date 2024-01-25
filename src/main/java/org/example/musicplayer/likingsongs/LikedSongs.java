package org.example.musicplayer.likingsongs;

import java.time.Duration;

public class LikedSongs {

    private final String albumCoverUrl;
    private final String name;
    private final String artist;
    private final String album;
    private final Duration duration;

    public LikedSongs(String name, String artist, String album, Duration duration, String albumCoverUrl) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.albumCoverUrl = albumCoverUrl;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }
}
