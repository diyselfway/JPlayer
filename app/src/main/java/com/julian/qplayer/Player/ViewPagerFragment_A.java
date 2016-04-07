package com.julian.qplayer.Player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.julian.qplayer.Music;
import com.julian.qplayer.MusicDB;
import com.julian.qplayer.R;

/**
 * Created by Julian on 2016/3/16.
 */
public class ViewPagerFragment_A extends Fragment {

    public static ViewPagerFragment_A newInstance(int songId){
        ViewPagerFragment_A fragment = new ViewPagerFragment_A();
        Bundle bundle = new Bundle();
        bundle.putInt("songId",songId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_player_a,container,false);
        int songId = getArguments().getInt("songId");
        Music song = MusicDB.getInstance(getActivity()).getMusics().get(songId - 1);
        TextView textSongName = (TextView) view.findViewById(R.id.text_songName_palyer_a);
        TextView textSingerName = (TextView) view.findViewById(R.id.text_singerName_palyer_a);
        TextView textAlbumInfo = (TextView) view.findViewById(R.id.text_albumInfo_player_a);
        textSongName.setText(song.getName());
        textSingerName.setText(song.getArtist());
//        String year = "未知年代";
//        if(song.getYear()!=null){
//            year = song.getYear();
//        }
        textAlbumInfo.setText(song.getAlbum() + " • " + (song.getYear()==null?"未知年代":song.getYear()));
        return view;
    }
}
