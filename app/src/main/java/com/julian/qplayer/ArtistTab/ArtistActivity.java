package com.julian.qplayer.ArtistTab;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.julian.qplayer.Artist;
import com.julian.qplayer.Music;
import com.julian.qplayer.MusicDB;
import com.julian.qplayer.R;
import com.julian.qplayer.Tools;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/14.
 */
public class ArtistActivity extends AppCompatActivity {
    private ArrayList<Music> mMusics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_artist);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ImageView artistImage = (ImageView) findViewById(R.id.image_cover_artist);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_artist);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_artist);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_artist);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ArtistSongListFragment());
        fragmentList.add(new ArtistAlbumListFragment());
        fragmentList.add(new ArtistInfoFragment());
        ArtistFragmentPagerAdapter adapter = new ArtistFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText(adapter.getTitle(i));
        }
        Artist artist = MusicDB.getInstance(this).findArtistByName(getIntent().getStringExtra("artistName"));
        ArrayList<Integer> songlist = artist.getSonglist();
        mMusics = MusicDB.getInstance(this).getMusics();
        collapsingToolbarLayout.setTitle(mMusics.get(songlist.get(0) - 1).getArtist());
//        String picPathStr = null;
        for (int songId : songlist) {
            Music song = mMusics.get(songId - 1);
            if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()) {
//                if (song.hasPicArtist()) {
//                    picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
//                } else if (song.hasPicAlbum()) {
//                    picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator + song.getAlbum() + ".png";
//                } else if (song.hasCover()) {
//                    picPathStr = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator + song.getAlbum() + ".png";
//                }
//                if (picPathStr != null) {
                try {
//                        FileInputStream fileInputStream = new FileInputStream(picPathStr);
                    Bitmap bitmap = MusicDB.getInstance(this).selectPicture(song, MusicDB.PREFER_ARTIST);
                    artistImage.setImageBitmap(bitmap);
                    int[] avgColor = Tools.getAverageColor(bitmap, 0.65, 0.1, 0.4);
                    int gray = (299 * avgColor[0] + 587 * avgColor[1] + 114 * avgColor[2]) / 1000;
                    if (gray > 128) {
                        collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);
                        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
                        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
                        collapsingToolbarLayout.setContentScrimColor(Color.rgb(avgColor[0], avgColor[1], avgColor[2]));
                        collapsingToolbarLayout.setStatusBarScrimColor(Color.rgb(avgColor[0], avgColor[1], avgColor[2]));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
//                }
            }
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
