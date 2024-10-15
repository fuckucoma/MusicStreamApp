package com.example.test;

public class Track {
    private final String title;
    private final String description;
    private final String artworkUrl;

    public Track(String title, String description, String artworkUrl) {
        this.title = title;
        this.description = description;
        this.artworkUrl = artworkUrl;
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
}

