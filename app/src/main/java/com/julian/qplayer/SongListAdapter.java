package com.julian.qplayer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/3.
 */
public class SongListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Music> mMusics;

    public SongListAdapter(Context context) {
        mContext = context;
        mMusics = MusicDB.getInstance(mContext).getMusics();
    }

    @Override
    public int getCount() {
        return mMusics.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMusics.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.view_song, null);
            holder = new ViewHolder();
            holder.albumImage = (ImageView) convertView.findViewById(R.id.song_iamge_cover);
            holder.songName = (TextView) convertView.findViewById(R.id.song_text_song_name);
            holder.singerName = (TextView) convertView.findViewById(R.id.song_text_singer);
            holder.text_cover = (TextView) convertView.findViewById(R.id.song_text_cover);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        String song_name = mMusics.get(position).getName();
        Music song = mMusics.get(position);
        if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()) {
            holder.albumImage.setVisibility(View.VISIBLE);
            holder.text_cover.setVisibility(View.INVISIBLE);
//            Bitmap bitmap = null;
//            try {
//                bitmap = MusicDB.getInstance(mContext).selectPicture(song,MusicDB.PREFER_ALBUM);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            holder.albumImage.setImageBitmap(bitmap);
            Glide.with(mContext)
                    .load(MusicDB.getInstance(mContext).selectPicturePath(song, MusicDB.PREFER_ALBUM))
                    .into(holder.albumImage);
            holder.albumImage.setVisibility(View.VISIBLE);
            holder.text_cover.setVisibility(View.INVISIBLE);
        } else {
            holder.albumImage.setVisibility(View.INVISIBLE);
            holder.text_cover.setVisibility(View.VISIBLE);
            holder.text_cover.setBackgroundColor(Color.parseColor(Tools.randomColor()));
            holder.text_cover.setText(String.valueOf(song_name.charAt(0)));
        }
        holder.songName.setText(song_name);
        holder.singerName.setText(mMusics.get(position).getArtist());
        return convertView;
    }

    private final class ViewHolder {
        ImageView albumImage;
        TextView songName;
        TextView singerName;
        TextView text_cover;
    }
}
