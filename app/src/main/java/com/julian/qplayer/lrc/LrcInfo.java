package com.julian.qplayer.lrc;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/16.
 */
public class LrcInfo {
    private String title;//歌曲名
    private String singer;//演唱者
    private String album;//专辑
//    private HashMap<Long,String> infos;//保存歌词信息和时间点一一对应的Map
    private ArrayList<Lrc> lrcs = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

//    public HashMap<Long, String> getInfos() {
//        return infos;
//    }
//
//    public void setInfos(HashMap<Long, String> infos) {
//        this.infos = infos;
//    }


    public ArrayList<Lrc> getLrcs() {
        return lrcs;
    }

    public void setLrcs(ArrayList<Lrc> lrcs) {
        this.lrcs = lrcs;
    }


}
