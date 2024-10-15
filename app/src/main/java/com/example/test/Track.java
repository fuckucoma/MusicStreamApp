package com.example.test;

public class Track {
    private final String title;
    private final String description;
    private final String artworkUrl;
    private final String trackId;

    public Track(String title, String description, String artworkUrl, String trackId) {
        this.title = title;
        this.description = description;
        this.artworkUrl = artworkUrl;
        this.trackId = trackId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public String getTrackId() {
        return trackId;
    }
}

