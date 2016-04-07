package com.julian.qplayer;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/8.
 */
public class Artist {
    private int id;
    private String name;
    private int count;
    private int totalTime;
    private ArrayList<Integer> songlist = new ArrayList<>();
    private ArrayList<Integer> albumlist = new ArrayList<>();
    private boolean hasPicture = false;
    // 0表示未知，1表示有图片链接，-1表示没有
    private int hasPicLink = 0;
    private ArtistInfo artistInfo = new ArtistInfo();

    public Artist(int id) {
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public ArrayList<Integer> getSonglist() {
        return songlist;
    }

    public void setSonglist(ArrayList<Integer> songlist) {
        this.songlist = songlist;
    }

    public ArrayList<Integer> getAlbumlist() {
        return albumlist;
    }

    public void setAlbumlist(ArrayList<Integer> albumlist) {
        this.albumlist = albumlist;
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

    public ArtistInfo getArtistInfo() {
        return artistInfo;
    }

    public void setArtistInfo(ArtistInfo artistInfo) {
        this.artistInfo = artistInfo;
    }
}
