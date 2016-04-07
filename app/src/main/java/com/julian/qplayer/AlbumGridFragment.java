package com.julian.qplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/3.
 */
public class AlbumGridFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_grid,container,false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_album);
        gridView.setNestedScrollingEnabled(true);
        gridView.setAdapter(new AlbumGridAdapter(getActivity()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
