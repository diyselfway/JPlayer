package com.julian.qplayer.lrc;

/**
 * Created by Julian on 2016/3/17.
 */
public class Lrc{
    private long time;
    private String content;

    public Lrc(long time, String content) {
        this.time = time;
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
