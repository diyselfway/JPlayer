package com.julian.qplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.julian.qplayer.Player.A_PlayerActivity;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/3.
 */
public class SongListFragment extends Fragment {

    public static final String PLAYLIST = "playlist";
    public static final String CURRENT_SONG = "current_song";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container,false);
        ListView listView = (ListView) view.findViewById(R.id.list_song);
        TextView text_summary = (TextView) view.findViewById(R.id.list_sum_song);
        text_summary.setText("共" + MusicDB.getInstance(getActivity()).getMusics().size() + "首歌曲");
        listView.setNestedScrollingEnabled(true);
        SongListAdapter songListAdapter = new SongListAdapter(getActivity());
        listView.setAdapter(songListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), A_PlayerActivity.class);
                ArrayList<Integer> playlist = new ArrayList<Integer>();
                for (int i = 0; i < parent.getCount(); i++) {
                    playlist.add(i + 1);
                }
                intent.putIntegerArrayListExtra("playlist", playlist);
                intent.putExtra("current_position", position);
                startActivity(intent);
            }
        });
        return view;
    }
}
