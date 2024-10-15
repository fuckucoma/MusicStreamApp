package com.example.test;

import java.util.List;

public class TrackResponse {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data {
        private String title;
        private String description;
        private Artwork artwork;
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

        public class Artwork {
            private String _150x150;
            private String _480x480;
            private String _1000x1000;

            public Artwork(String _150x150, String _480x480, String _1000x1000) {
                this._150x150 = _150x150;
                this._480x480 = _480x480;
                this._1000x1000 = _1000x1000;
            }

            public String get150x150() {
                return _150x150;
            }

            public String get480x480() {
                return _480x480;
            }

            public String get1000x1000() {
                return _1000x1000;
            }


        }

    }
}

