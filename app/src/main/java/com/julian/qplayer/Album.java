package com.julian.qplayer;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/8.
 */
public class Album {
    private int id;
    private String name;
    private int size;
    private int totalTime;
    private String artist;
    private String year;
    private ArrayList<Integer> songlist = new ArrayList<>();
    private AlbumInfo albumInfo = new AlbumInfo();
    private boolean hasPicture = false;
    // 0表示未知，1表示有图片链接，-1表示没有
    private int hasPicLink = 0;

    public Album(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public ArrayList<Integer> getSonglist() {
        return songlist;
    }

    public void setSonglist(ArrayList<Integer> songlist) {
        this.songlist = songlist;
    }

    public AlbumInfo getAlbumInfo() {
        return albumInfo;
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        this.albumInfo = albumInfo;
    }

    public boolean hasPicture() {
        return hasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public int hasPicLink() {
        return hasPicLink;
    }

    public void setHasPicLink(int hasPicLink) {
        this.hasPicLink = hasPicLink;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
