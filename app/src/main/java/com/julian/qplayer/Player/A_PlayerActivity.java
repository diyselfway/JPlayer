package com.julian.qplayer.Player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.julian.qplayer.Music;
import com.julian.qplayer.MusicDB;
import com.julian.qplayer.MusicService;
import com.julian.qplayer.MusicWidget;
import com.julian.qplayer.PlayButton;
import com.julian.qplayer.R;
import com.julian.qplayer.Utils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/16.
 */
public class A_PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mAlbumImage;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;


    public static final String UPDATE_WIDGET = "com.julian.musicplayer.UPDATE_WIDGET";
    public static final int PROGRESS = 1;
    public static final String TAG = "A_PlayerActivity";
    public static final int STATE_START = 0;
    public static final int STATE_CONTINUE = 1;
    private int mCurrentPosition = 0;
    private int currentSongId = 1;
    private PlayButton mPlayButton;
    private ImageButton mButtonLast;
    private ImageButton mButtonNext;
    private MediaPlayer mPlayer;
    private SeekBar mSeekBar;
    private ArrayList<Integer> mPlaylist;
    private ArrayList<Music> mMusics;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TextView mTextProgressTime;
    private TextView mTextTotalTime;
    private boolean isFirstTime = true;


    private Handler mHandler = new MyHandler();

    private BroadcastReceiver mReceiver = new MyBroadcastReceiver();
    private ListView mPlaylistView;
//    private LrcInfo mLrcInfo;
    private ImageView mAlbumImageMask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_a);
        mPlayer = MusicService.getPlayer();
//        mPlayer = MyApplication.mPlayer;
        findViews();
        setListeners();
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mMusics = MusicDB.getInstance(this).getMusics();
        mPlaylist = getIntent().getIntegerArrayListExtra("playlist");
        mCurrentPosition = getIntent().getIntExtra("current_position", 0);
        int state = getIntent().getIntExtra("state",STATE_START);
        currentSongId = mPlaylist.get(mCurrentPosition);
        setTotalText(currentSongId);
        final Music currentSong = mMusics.get(currentSongId - 1);
        mCollapsingToolbarLayout.setTitle(currentSong.getName());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedCollapsingToolbarLayout);
//        mCollapsingToolbarLayout.setExpandedTitleColor(Color.argb(0, 128, 128, 128));
        setAlbumImage(currentSong);
        mViewPager.setAdapter(new FragmentStatePagerAdapter_A(getSupportFragmentManager(), mPlaylist));
        Log.d(TAG, "1");
        mViewPager.setCurrentItem(mCurrentPosition);
        Log.d(TAG, "2");
        mPlaylistView.setAdapter(new PlaylistAdapter_A(this, mPlaylist));
        mPlaylistView.setNestedScrollingEnabled(true);
        mPlaylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPosition = position;
                currentSongId = mPlaylist.get(position);
                updateDate(currentSongId);
                Log.d(TAG, "mPlaylistView");
                play();
            }
        });

//        initWidget();
//        updateDate(currentSongId);
//        play();
        switch (state){
            case STATE_START:{
                play();
                break;
            }
            case STATE_CONTINUE:{
                continuePlay();
                break;
            }
        }

//        initLrc(currentSong);
    }

    private void continuePlay() {
        Log.d(TAG,"continuePlay");
//        mPlayer.reset();
//        try {
//            mPlayer.setDataSource(mMusics.get(currentSongId-1).getPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            mPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        mSeekBar.setMax(mPlayer.getDuration());
//        mPlayer.start();
        mPlayButton.setIsPlaying(true);
        mHandler.sendEmptyMessage(PROGRESS);
//        updateWidget();

    }

//    private void initLrc(Music currentSong) {
//        if (currentSong.hasLrc()){
//            mAlbumImageMask.setVisibility(View.VISIBLE);
//        }else mAlbumImageMask.setVisibility(View.INVISIBLE);
//        if (currentSong.hasLrc()){
//            LrcParser lrcParser = new LrcParser();
//            String dirPathStr = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + File.separator + "lrcs" + File.separator;
//            String lrcPath = dirPathStr + currentSong.getName() + ".lrc";
//            try {
//                mLrcInfo = lrcParser.parser(lrcPath);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }else mLrcInfo = null;
//    }

    private class MyHandler extends Handler {
//        WeakReference<A_PlayerActivity> mPlayerActivityWeakReference;
//        public MyHandler(A_PlayerActivity activity){
//            mPlayerActivityWeakReference = new WeakReference<A_PlayerActivity>(activity);
//        }

        @Override
        public void handleMessage(Message msg) {
//            A_PlayerActivity activity = mPlayerActivityWeakReference.get();
//            MediaPlayer player = MusicService.getPlayer();
//            MediaPlayer player = MyApplication.mPlayer;
//            SeekBar seekBar = activity.mSeekBar;
//            SeekBar seekBar = mSeekBar;
//            TextView textProgressTime =mTextProgressTime;
            Handler handler = mHandler;
            switch (msg.what){
                //更新播放进度条
                case PROGRESS:
                    if (mPlayer != null) {
                        try {
                            mSeekBar.setProgress(mPlayer.getCurrentPosition());
                            int total = mPlayer.getCurrentPosition();
                            int m = total/1000/60;
                            String mm = m + "";
                            if (m < 10){
                                mm = "0" + m;
                            }
                            int s = total/1000%60;
                            String ss = s + "";
                            if (s<10) {
                                ss = "0"+ s;
                            }
                            mTextProgressTime.setText(mm + ":" + ss);
//                            if (mLrcInfo.getLrcs().get((long)(player.getCurrentPosition())/10*10)){
//                                mCollapsingToolbarLayout.setTitle(mLrcInfo.getInfos().get((long)(player.getCurrentPosition())/10*10));
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessageDelayed(PROGRESS,1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{
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
    }

    private void setAlbumImage(Music song) {
//        String picPathStr = null;
//        if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()) {
//            if (song.hasPicArtist()) {
//                picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
//            }else if (song.hasPicAlbum()) {
//                picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator + song.getAlbum() + ".png";
//            } else if (song.hasCover()) {
//                picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator + song.getAlbum() + ".png";
//            }
//            if (picPathStr != null) {
            //                    FileInputStream fileInputStream = new FileInputStream(picPathStr);
            Glide.with(this)
                    .load(MusicDB.getInstance(this).selectPicturePath(song, MusicDB.PREFER_BEST))
//                    .bitmapTransform(new BlurTransformation(this))
                    .asBitmap()
                    .error(R.drawable.album)
                    .into(new BitmapImageViewTarget(mAlbumImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            Palette.Swatch swatch = Utils.getSwatch(resource);
                            mCollapsingToolbarLayout.setContentScrimColor(swatch.getRgb());
                            mCollapsingToolbarLayout.setStatusBarScrimColor(swatch.getRgb());
                        }
                    });
//                    Bitmap bitmap = MusicDB.getInstance(this).selectPicture(song,MusicDB.PREFER_BEST);
//                    mAlbumImage.setImageBitmap(bitmap);
//                    int[] avgColor = Tools.getAverageColor(bitmap, 0.8, 0.1, 0.8);
//                    int gray = (299 * avgColor[0] + 587 * avgColor[1] + 114 * avgColor[2]) / 1000;
//                    if (gray > 220) {
////                        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);
//                        mCollapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
//                        mCollapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));
//                    } else {
////                        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
//                        mCollapsingToolbarLayout.setContentScrimColor(Color.rgb(avgColor[0], avgColor[1], avgColor[2]));
//                        mCollapsingToolbarLayout.setStatusBarScrimColor(Color.rgb(avgColor[0], avgColor[1], avgColor[2]));
//                    }
            //            }
//        }else {
////            mAlbumImage.setImageResource(R.drawable.album);
//            Glide.with(this)
//                    .load(R.drawable.album)
//                    .asBitmap()
//                    .into(new BitmapImageViewTarget(mAlbumImage){
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            super.setResource(resource);
//                            Palette.Swatch swatch = Utils.getSwatch(resource);
//                            mCollapsingToolbarLayout.setContentScrimColor(swatch.getRgb());
//                            mCollapsingToolbarLayout.setStatusBarScrimColor(swatch.getRgb());
//                        }
//                    });
//        }
    }

    private void findViews() {
        mPlayButton = (PlayButton) findViewById(R.id.playButton_a);
        mButtonLast = (ImageButton) findViewById(R.id.button_last_a);
        mButtonNext = (ImageButton) findViewById(R.id.button_next_a);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar_a);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_player_a);
        mAlbumImage = (ImageView) findViewById(R.id.image_cover_player_a);
//        mAlbumImageMask = (ImageView) findViewById(R.id.image_cover_mask_player_a);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_player_a);
        mViewPager = (ViewPager) findViewById(R.id.viewPager_player_a);
        mTextProgressTime = (TextView) findViewById(R.id.text_progressTime_a);
        mTextTotalTime = (TextView) findViewById(R.id.text_totalTime_a);
        mPlaylistView = (ListView) findViewById(R.id.list_playlist_a);
    }

    private void setListeners() {
        mPlayButton.setOnClickListener(this);
        mButtonLast.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mCurrentPosition < mPlaylist.size()) {
                    mCurrentPosition++;
                    currentSongId = mPlaylist.get(mCurrentPosition);
                } else currentSongId = mPlaylist.get(0);
                updateDate(currentSongId);
                Log.d(TAG, "mPlayer");
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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Music currentSong = mMusics.get(mPlaylist.get(position) - 1);
                mCollapsingToolbarLayout.setTitle(currentSong.getName());
                currentSongId = mPlaylist.get(position);
                setAlbumImage(currentSong);
//                initLrc(currentSong);
                Log.d(TAG, "onPageSelected");
                if(isFirstTime){
                    isFirstTime = false;
                }else play();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTotalText(int currentSongId) {
        Music currentSong = mMusics.get(currentSongId-1);
        int total = currentSong.getDuration();
        int m = total/1000/60;
        String mm = m + "";
        if (m < 10){
            mm = "0" + m;
        }
        int s = total/1000%60;
        String ss = s + "";
        if (s<10) {
            ss = "0"+ s;
        }
        mTextTotalTime.setText(mm + ":" + ss);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton_a:
                changeState();
                break;
            case R.id.button_last_a:
                lastSong();
                break;
            case R.id.button_next_a:
                nextSong();
                break;
        }
    }

    public void play() {
        Log.d(TAG,"play");
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
        Log.d(TAG, "lastSong");
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
        Log.d(TAG, "nextSong");
        play();
    }

    public void updateDate(int currentSongId){
        Music currentSong =  mMusics.get(currentSongId-1);
//        initLrc(currentSong);
        setAlbumImage(currentSong);
        mViewPager.setCurrentItem(mCurrentPosition);
        int total = currentSong.getDuration();
        int m = total/1000/60;
        String mm = m + "";
        if (m < 10){
            mm = "0" + m;
        }
        int s = total/1000%60;
        String ss = s + "";
        if (s<10) {
            ss = "0"+ s;
        }
        mTextTotalTime.setText(mm + ":" + ss);
//        if (currentSong.hasLrc()){
//            mAlbumImageMask.setVisibility(View.VISIBLE);
//        }else mAlbumImageMask.setVisibility(View.INVISIBLE);
//        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//        mmr.setDataSource(mMusics.get(currentSongId-1).getPath());
//        if (currentSong.hasCover()){
//            byte[] coverBytes = mmr.getEmbeddedPicture();
//            mCover.setImageBitmap(BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length));
//        }else {
//            mCover.setImageResource(R.drawable.cover);
//        }
    }

    public void changeState(){
        Log.d(TAG, "mPlayer is null:" + (mPlayer == null));
//        if(mPlayer == null){
//            Intent intent = new Intent(A_PlayerActivity.this,MusicService.class);
//            startService(intent);
//        }
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updateWidget();
        mPlayButton.changeState();
    }

    private class FragmentStatePagerAdapter_A extends FragmentStatePagerAdapter {
        ArrayList<Integer> mPlaylist;

        public FragmentStatePagerAdapter_A(FragmentManager fm, ArrayList<Integer> playlist) {
            super(fm);
            mPlaylist = playlist;
        }

        @Override
        public Fragment getItem(int position) {
            return ViewPagerFragment_A.newInstance(mPlaylist.get(position));
        }

        @Override
        public int getCount() {
            return mPlaylist.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
