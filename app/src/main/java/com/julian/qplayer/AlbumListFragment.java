package com.julian.qplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/4/10.
 */
public class AlbumListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_list,container,false);
        ListView listView = (ListView) view.findViewById(R.id.list_album);
        listView.setAdapter(new AlbumListAdapter(getActivity()));
        listView.setNestedScrollingEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Integer> playlist = MusicDB.getInstance(getActivity()).getAlbums().get(position).getSonglist();
                Intent intent = new Intent(getActivity(), AlbumActivity.class);
                Album album = MusicDB.getInstance(getActivity()).findAlbumByName(MusicDB.getInstance(getActivity()).getAlbums().get(position).getName());
                intent.putExtra("albumName", album.getName());
//                intent.putExtra("flag", "album");
                startActivity(intent);
            }
        });
        return view;
    }
}
