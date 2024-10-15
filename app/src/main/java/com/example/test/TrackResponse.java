package com.example.test;

public class TrackResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        private String title;
        private String description;
        private String artwork;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getArtwork() {
            return artwork;
        }

        public class Artwork {
            private String url;

            public String getUrl() {
                return url;
            }
        }
    }
}

