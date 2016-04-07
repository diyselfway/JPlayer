package com.julian.qplayer.ArtistTab;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.julian.qplayer.Artist;
import com.julian.qplayer.MusicDB;
import com.julian.qplayer.Player.A_PlayerActivity;
import com.julian.qplayer.R;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/14.
 */
public class ArtistSongListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_songlist, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_artist_songlist);
        listView.setNestedScrollingEnabled(true);
        Artist artist = MusicDB.getInstance(getActivity()).findArtistByName(getActivity().getIntent().getStringExtra("artistName"));
        final ArrayList<Integer> songlist = artist.getSonglist();
        ArtistSongListAdapter adapter = new ArtistSongListAdapter(getActivity(),songlist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), A_PlayerActivity.class);
                intent.putIntegerArrayListExtra("playlist", songlist);
                intent.putExtra("current_position", position);
                startActivity(intent);
            }
        });
        return view;
    }
}
