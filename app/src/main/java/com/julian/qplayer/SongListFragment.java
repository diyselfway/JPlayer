package com.julian.qplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.julian.qplayer.Player.A_PlayerActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/3.
 */
public class SongListFragment extends Fragment {

    public static final String PLAYLIST = "playlist";
    public static final String CURRENT_SONG = "current_song";
    private ArrayList<Music> mMusics;
    private MediaPlayer mPlayer = MyApplication.mPlayer;
    private ListView mListView ;
    private int mCurrentPosition = 0;
    public static final int PROGRESS = 1;
    private ProgressBar mProgressBar;
    private Handler mHandler = new MyHandler();
    private boolean isPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container,false);
        mMusics = MusicDB.getInstance(getActivity()).getMusics();
        mPlayer = MusicService.getPlayer();
//        mPlayer = MyApplication.mPlayer;
        Log.d("test", "is MyApplication.mPlayer null :" + (MyApplication.mPlayer == null));
        Log.d("test", "is mPlayer null :" + (mPlayer == null));
        mListView = (ListView) view.findViewById(R.id.list_song);
        TextView textSummary = (TextView) view.findViewById(R.id.list_sum_song);
        LinearLayout summary = (LinearLayout) view.findViewById(R.id.list_summary);
        final RelativeLayout controlBar = (RelativeLayout) getActivity().findViewById(R.id.controlBar);
        final ImageView albumImage = (ImageView) getActivity().findViewById(R.id.image_album_controlBar);
        final TextView textSongName = (TextView) getActivity().findViewById(R.id.text_songName_control);
        final TextView textSingerName = (TextView) getActivity().findViewById(R.id.text_singerName_control);
        final ImageButton buttonPlay = (ImageButton) getActivity().findViewById(R.id.playButton_controlBar);
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar_controlBar);
        textSummary.setText("共" + MusicDB.getInstance(getActivity()).getMusics().size() + "首歌曲");
        mListView.setNestedScrollingEnabled(true);
        SongListAdapter songListAdapter = new SongListAdapter(getActivity());
        mListView.setAdapter(songListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controlBar.setVisibility(View.VISIBLE);
                buttonPlay.setImageResource(R.drawable.pause);
                isPlaying = true;
                Music song = mMusics.get(position);
                Log.d("test", "albumImage is null:" + (albumImage == null));
                Glide.with(getActivity())
                        .load(MusicDB.getInstance(getActivity()).selectPicturePath(song, MusicDB.PREFER_ALBUM))
                        .into(albumImage);
                textSongName.setText(song.getName());
                textSingerName.setText(song.getArtist());
                play(position + 1);
                mCurrentPosition = position;
                mProgressBar.setMax(song.getDuration());
                mHandler.sendEmptyMessage(PROGRESS);


//                Intent intent = new Intent(getActivity(), A_PlayerActivity.class);
//                ArrayList<Integer> playlist = new ArrayList<Integer>();
//                for (int i = 0; i < parent.getCount(); i++) {
//                    playlist.add(i + 1);
//                }
//                intent.putIntegerArrayListExtra("playlist", playlist);
//                intent.putExtra("current_position", position);
//                startActivity(intent);
            }
        });
        controlBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), A_PlayerActivity.class);
                ArrayList<Integer> playlist = new ArrayList<Integer>();
                for (int i = 0; i < mMusics.size(); i++) {
                    playlist.add(i + 1);
                }
                intent.putIntegerArrayListExtra("playlist", playlist);
                intent.putExtra("current_position", mCurrentPosition);
                startActivity(intent);
            }
        });
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    buttonPlay.setImageResource(R.drawable.play);
                    mPlayer.pause();
                    isPlaying = false;
                }else {
                    buttonPlay.setImageResource(R.drawable.pause);
                    mPlayer.start();
                    isPlaying = true;
                }
            }
        });
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    public void play(int currentSongId) {
        Log.d("test", "is mPlayer null :" + (mPlayer == null));
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
//        mSeekBar.setMax(mPlayer.getDuration());
        mPlayer.start();
//        mPlayButton.setIsPlaying(true);
//        mHandler.sendEmptyMessage(PROGRESS);
//        updateWidget();

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //更新播放进度条
                case PROGRESS:
                    if (mPlayer != null) {
                        try {
                            mProgressBar.setProgress(mPlayer.getCurrentPosition());
//                            int total = mPlayer.getCurrentPosition();
//                            int m = total/1000/60;
//                            String mm = m + "";
//                            if (m < 10){
//                                mm = "0" + m;
//                            }
//                            int s = total/1000%60;
//                            String ss = s + "";
//                            if (s<10) {
//                                ss = "0"+ s;
//                            }
//                            mTextProgressTime.setText(mm + ":" + ss);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessageDelayed(PROGRESS,1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}