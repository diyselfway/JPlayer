package com.julian.qplayer;

import android.app.Application;

/**
 * Created by Julian on 2016/3/13.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MusicDB.getInstance(getApplicationContext());
    }
}
