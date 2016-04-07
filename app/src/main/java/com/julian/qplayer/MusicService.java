package com.julian.qplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Julian on 2015/12/24.
 */
public class MusicService extends Service {
    public static final String TAG = "MusicService";
    private MediaPlayer mPlayer;
    private String path;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
//        Log.d(TAG,"intent:"+intent.getStringExtra(PlayerActivity.PATH));
//        path = intent.getStringExtra(PlayerActivity.PATH);
//        try {
//            mPlayer.setDataSource(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return new LocalBinder();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG,"onStart");
//        if (mPlayer != null){
//            mPlayer.stop();
//            mPlayer.release();
//        }
//        mPlayer = new MediaPlayer();
//        try {
//            mPlayer.setDataSource(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            mPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mPlayer.start();
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
//        if (mPlayer != null){
//            mPlayer.stop();
//            mPlayer.release();
//        }
//        mPlayer = new MediaPlayer();
//        try {
//            mPlayer.setDataSource(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            mPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        if (mPlayer != null){
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        mPlayer.stop();
        mPlayer.release();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        mPlayer.stop();
        return super.onUnbind(intent);
    }

    class LocalBinder extends Binder{
        MusicService getService(){
            Log.d(TAG,"getService");
            return MusicService.this;
        }
    }

    public void say(String string){
        Toast.makeText(this, string ,Toast.LENGTH_SHORT).show();
    }

    public MediaPlayer getPlayer()  {
        Log.d(TAG,"getPlayer");
        return mPlayer;
    }


}
