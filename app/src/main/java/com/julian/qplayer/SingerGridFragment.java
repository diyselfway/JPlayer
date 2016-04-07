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

import com.julian.qplayer.ArtistTab.ArtistActivity;

/**
 * Created by Julian on 2016/3/3.
 */
public class SingerGridFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_singer_grid, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_singer);
        gridView.setNestedScrollingEnabled(true);
        gridView.setAdapter(new SingerGridAdapter(getActivity()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ArtistActivity.class);
                intent.putExtra("artistName", MusicDB.getInstance(getActivity()).getArtists().get(position).getName());
//                intent.putExtra("flag","singer");
                startActivity(intent);
            }
        });
        return view;
    }
}
