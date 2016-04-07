package com.julian.qplayer;

/**
 * Created by Julian on 2016/3/8.
 */
public class Music {
    private int id;
    private int _id;
    private String name;
    private String artist;
    private String album;
    private int duration;
    private String path;
    private String dispName;
    private int size;
    private String year;
    private boolean hasCover = false;
    private boolean hasPicAlbum = false;
    private boolean hasPicArtist = false;
    private boolean hasLrc =false;

    public Music(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean hasCover() {
        return hasCover;
    }

    public void setHasCover(boolean hasCover) {
        this.hasCover = hasCover;
    }

    public boolean hasPicAlbum() {
        return hasPicAlbum;
    }

    public void setHasPicAlbum(boolean hasPicAlbum) {
        this.hasPicAlbum = hasPicAlbum;
    }

    public boolean hasPicArtist() {
        return hasPicArtist;
    }

    public void setHasPicArtist(boolean hasPicArtist) {
        this.hasPicArtist = hasPicArtist;
    }

    public boolean hasLrc() {
        return hasLrc;
    }

    public void setHasLrc(boolean hasLrc) {
        this.hasLrc = hasLrc;
    }
}
