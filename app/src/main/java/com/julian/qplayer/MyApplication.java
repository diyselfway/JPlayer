package com.julian.qplayer;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Julian on 2016/3/13.
 */
public class MyApplication extends Application {
    public static final String TAG = "MyApplication";
    public static MediaPlayer mPlayer;
    private MusicService mMusicService;

    @Override
    public void onCreate() {
        super.onCreate();
        MusicDB.getInstance(getApplicationContext());
        bindMusicService();
    }

    private void bindMusicService() {
        Log.d(TAG, "bindMusicService");
        //绑定音乐服务
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MusicService.LocalBinder localBinder = (MusicService.LocalBinder) iBinder;
                mMusicService = localBinder.getService();
                mPlayer = mMusicService.getPlayer();
                Log.d(TAG, "onServiceConnected");
                Log.d(TAG, "is mPlayer null:" + (mPlayer == null));
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent_bind_service = new Intent(MyApplication.this, MusicService.class);
        bindService(intent_bind_service, serviceConnection, BIND_AUTO_CREATE);
        Log.d(TAG, "bindService");
        Intent intent_start_service = new Intent(MyApplication.this, MusicService.class);
        startService(intent_start_service);
        Log.d(TAG, "startService");
    }
}
