package com.julian.qplayer.lrc;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/16.
 */
public class LrcJson {


    private int count;

    private ArrayList<ResultEntity> result;

    public void setCount(int count) {
        this.count = count;
    }

    public void setResult(ArrayList<ResultEntity> result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<ResultEntity> getResult() {
        return result;
    }

    public static class ResultEntity {
        @SerializedName("lrc")
        private String lrcUrl;

        public void setLrcUrl(String lrcUrl) {
            this.lrcUrl = lrcUrl;
        }

        public String getLrcUrl() {
            return lrcUrl;
        }
    }
}
