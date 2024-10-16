package com.example.test;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackResponse {
    @SerializedName("data")
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data {
        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("artwork")
        private Artwork artwork;

        @SerializedName("id")
        private String id;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Artwork getArtwork() {
            return artwork;
        }

        public String getId() {
            return id;
        }

        public static class Artwork {
            @SerializedName("150x150")
            private String size150x150;

            @SerializedName("480x480")
            private String size480x480;

            @SerializedName("1000x1000")
            private String size1000x1000;

            public String get150x150() {
                return size150x150;
            }

            public String get480x480() {
                return size480x480;
            }

            public String get1000x1000() {
                return size1000x1000;
            }
        }
    }
}
