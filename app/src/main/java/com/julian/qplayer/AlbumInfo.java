package com.julian.qplayer;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/14.
 */
public class AlbumInfo {

    @SerializedName("album")
    private AlbumEntity info;

    public void setInfo(AlbumEntity info) {
        this.info = info;
    }

    public AlbumEntity getInfo() {
        return info;
    }

    public static class AlbumEntity {

        private ArrayList<ImageEntity> image = new ArrayList<>();

        public void setImage(ArrayList<ImageEntity> image) {
            this.image = image;
        }

        public ArrayList<ImageEntity> getImage() {
            return image;
        }

        public static class ImageEntity {
            @SerializedName("#text")
            private String url = "";
            private String size;

            public void setUrl(String url) {
                this.url = url;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getUrl() {
                return url;
            }

            public String getSize() {
                return size;
            }
        }
    }
}
