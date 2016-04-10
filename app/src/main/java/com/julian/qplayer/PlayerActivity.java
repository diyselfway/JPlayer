package com.julian.qplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String UPDATE_WIDGET = "com.julian.musicplayer.UPDATE_WIDGET";
    public static final int PROGRESS = 1;
    public static final String TAG = "PlayerActivity";
    private int mCurrentPosition = 0;
    private int currentSongId = 1;
    private PlayButton mPlayButton;
    private ImageButton mButtonLast;
    private ImageButton mButtonNext;
    private MediaPlayer mPlayer;
    private TextView mTextSongName;
    private ImageView mCover;
    private SeekBar mSeekBar;
    private ArrayList<Integer> mPlaylist;
    private ArrayList<Music> mMusics;
    private static class MyHandler extends Handler{
        WeakReference<PlayerActivity> mPlayerActivityWeakReference;
        public MyHandler(PlayerActivity activity){
            mPlayerActivityWeakReference = new WeakReference<PlayerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayerActivity activity = mPlayerActivityWeakReference.get();
            MediaPlayer player = activity.mPlayer;
            SeekBar seekBar = activity.mSeekBar;
            Handler handler = activity.mHandler;
            switch (msg.what){
                //更新播放进度条
                case PROGRESS:
                    if (player != null) {
                        try {
                            seekBar.setProgress(player.getCurrentPosition());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessageDelayed(PROGRESS,100);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private Handler mHandler = new MyHandler(this);
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case MusicWidget.LAST_SONG:
                    lastSong();
                    break;
                case MusicWidget.CHANGE_STATE:
                    changeState();
                    break;
                case MusicWidget.NEXT_SONG:
                    nextSong();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_player);

        mMusics = MusicDB.getInstance(this).getMusics();
        mPlayer = MyApplication.mPlayer;
        Log.d(TAG, "is mPlayer null :" + (mPlayer == null));
        mPlaylist = getIntent().getIntegerArrayListExtra("playlist");
        mCurrentPosition = getIntent().getIntExtra("current_position", 0);
        currentSongId = mPlaylist.get(mCurrentPosition);
        findViews();
        setListeners();
//        initWidget();
        updateDate(currentSongId);
        play();
    }

    private void findViews() {
        mPlayButton = (PlayButton) findViewById(R.id.playButton);
        mButtonLast = (ImageButton) findViewById(R.id.button_last);
        mButtonNext = (ImageButton) findViewById(R.id.button_next);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mCover = (ImageView) findViewById(R.id.image_cover);
        mTextSongName = (TextView) findViewById(R.id.tittle);
    }

    private void setListeners() {
        mPlayButton.setOnClickListener(this);
        mButtonLast.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (currentSongId < mPlaylist.size()) {
                    currentSongId++;
                } else currentSongId = 1;
                updateDate(currentSongId);
                play();
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mPlayer.seekTo(i);
                    mPlayer.start();
                    mPlayButton.setIsPlaying(true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                changeState();
                break;
            case R.id.button_last:
                lastSong();
                break;
            case R.id.button_next:
                nextSong();
                break;
        }
    }

    public void play() {
        mPlayer.reset();
        try {
            mPlayer.setDataSource(mMusics.get(currentSongId-1).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSeekBar.setMax(mPlayer.getDuration());
        mPlayer.start();
        mPlayButton.setIsPlaying(true);
        mHandler.sendEmptyMessage(PROGRESS);
        updateWidget();

    }

    private void updateWidget() {
        //更新桌面小插件
        Intent intent_update_widget = new Intent();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mMusics.get(currentSongId-1).getPath());
        intent_update_widget.putExtra("cover",mmr.getEmbeddedPicture());
        intent_update_widget.putExtra("title", mMusics.get(currentSongId-1).getName());
        intent_update_widget.putExtra("isPlaying",mPlayer.isPlaying());
        intent_update_widget.setAction(UPDATE_WIDGET);
        sendBroadcast(intent_update_widget);
    }

    public void lastSong(){
        if (mCurrentPosition > 0) {
            mCurrentPosition--;
            currentSongId = mPlaylist.get(mCurrentPosition);
        } else {
            mCurrentPosition = mPlaylist.size()-1;
            currentSongId = mPlaylist.get(mCurrentPosition);
        }
        updateDate(currentSongId);
        play();
    }
    public void nextSong(){
        if (mCurrentPosition < mPlaylist.size()-1) {
            mCurrentPosition++;
            currentSongId = mPlaylist.get(mCurrentPosition);
        } else{
            mCurrentPosition = 0;
            currentSongId = mPlaylist.get(mCurrentPosition);
        }
        updateDate(currentSongId);
        play();
    }

    public void updateDate(int currentSongId){
        Music currentSong =  mMusics.get(currentSongId-1);
        mTextSongName.setText(currentSong.getName());
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mMusics.get(currentSongId-1).getPath());
        if (currentSong.hasCover()){
            byte[] coverBytes = mmr.getEmbeddedPicture();
            mCover.setImageBitmap(BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length));
        }else {
            mCover.setImageResource(R.drawable.cover);
        }
    }

    public void changeState(){
        Log.d(TAG,"mPlayer is null:"+ (mPlayer == null));
        if(mPlayer == null){
            Intent intent = new Intent(PlayerActivity.this,MusicService.class);
            startService(intent);
        }
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updateWidget();
        mPlayButton.changeState();
    }
}
