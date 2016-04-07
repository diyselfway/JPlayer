package com.julian.qplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.julian.qplayer.ArtistTab.ArtistActivity;
import com.julian.qplayer.Player.A_PlayerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/6.
 */
public class AlbumActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = PlayerActivity.class.getSimpleName();
    private ArrayList<Integer> mPlaylist;
    private ArrayList<Music> mMusics;
    private Music mFirstSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mMusics = MusicDB.getInstance(AlbumActivity.this).getMusics();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_playlist);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_playlist);
        Album album = MusicDB.getInstance(this).findAlbumByName(getIntent().getStringExtra("albumName"));
        mPlaylist = album.getSonglist();
        mFirstSong = mMusics.get(mPlaylist.get(0) - 1);
        ImageView imageView = (ImageView) findViewById(R.id.image_cover_playlist);
        ListView listView = (ListView) findViewById(R.id.playlist);
        RelativeLayout summaryLayout = (RelativeLayout) findViewById(R.id.playlist_summary);
        ImageView imageSummary = (ImageView) findViewById(R.id.playlist_summary_image);
        TextView textSummaryUp = (TextView) findViewById(R.id.playlist_summary_text_up);
        TextView textSummaryDown = (TextView) findViewById(R.id.playlist_summary_text_down);
        listView.setNestedScrollingEnabled(true);
        listView.setAdapter(new AlbumSongAdapter(this, mPlaylist));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AlbumActivity.this, A_PlayerActivity.class);
                intent.putIntegerArrayListExtra("playlist", mPlaylist);
                intent.putExtra("current_position", position);
                startActivity(intent);
            }
        });
        imageSummary.setOnClickListener(this);
        textSummaryUp.setOnClickListener(this);
        textSummaryDown.setOnClickListener(this);
        String title = "";
        textSummaryUp.setText(mFirstSong.getArtist());
        String albumYear = "未知年代";
        for (int songId : mPlaylist) {
            Music song = mMusics.get(songId - 1);
            if (song.getYear() != null) {
                albumYear = song.getYear();
                break;
            }
        }
        for (int songId : mPlaylist) {
            Music song = mMusics.get(songId - 1);
            if (song.hasPicArtist()) {
                String picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
                Glide.with(this).load(picPathStr)
                        .error(R.drawable.singer)
                        .into(imageSummary);
//                try {
//                    FileInputStream fileInputStream = new FileInputStream(picPathStr);
//                    Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
//                    imageSummary.setImageBitmap(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                break;
            }
        }
        textSummaryDown.setText(albumYear + " • " + mPlaylist.size() + "首歌曲");
        String artistPicPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + mFirstSong.getArtist() + ".png";
        File artistPic = new File(artistPicPath);
        if (artistPic.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(artistPic);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                imageSummary.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        title = mFirstSong.getAlbum();
//        String picPathStr = null;
        for (int songId : mPlaylist) {
            Music song = mMusics.get(songId - 1);
            Glide.with(this).load(MusicDB.getInstance(this).selectPicturePath(song, MusicDB.PREFER_ALBUM))
                    .error(R.drawable.album)
                    .into(imageView);
//            if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()) {
//                if (song.hasPicAlbum()) {
//                    picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator + song.getAlbum() + ".png";
//                } else if (song.hasCover()) {
//                    picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator + song.getAlbum() + ".png";
//                } else if (song.hasPicArtist()) {
//                    picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
//                }
//                if (picPathStr != null) {
//                    try {
////                        FileInputStream fileInputStream = new FileInputStream(picPathStr);
//                        Bitmap bitmap = MusicDB.getInstance(this).selectPicture(song,MusicDB.PREFER_ALBUM);
//                        imageView.setImageBitmap(bitmap);
//                        int[] avgColor = Tools.getAverageColor(bitmap, 0.75, 0.1, 0.4);
//                        int gray = (299 * avgColor[0] + 587 * avgColor[1] + 114 * avgColor[2]) / 1000;
//                        if (gray > 150) {
//                            collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);
//                            collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
//                            collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));
//                        } else {
//                            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
//                            collapsingToolbarLayout.setContentScrimColor(Color.rgb(avgColor[0], avgColor[1], avgColor[2]));
//                            collapsingToolbarLayout.setStatusBarScrimColor(Color.rgb(avgColor[0], avgColor[1], avgColor[2]));
//                        }
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
        }
        collapsingToolbarLayout.setTitle(title);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playlist_summary_image:
            case R.id.playlist_summary_text_up:
            case R.id.playlist_summary_text_down:{
                Intent intent_artist = new Intent(AlbumActivity.this, ArtistActivity.class);
                intent_artist.putExtra("artistName", mFirstSong.getArtist());
                startActivity(intent_artist);
                break;
            }
        }
    }
}
