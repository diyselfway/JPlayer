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
 * Created by Julian on 2016/4/10.
 */
public class AlbumListAdapter extends BaseAdapter {

    private final ArrayList<Album> mAlbums;
    private Context mContext;
    private final ArrayList<Music> mMusics;

    public AlbumListAdapter(Context context) {
        mContext = context;
        mAlbums = MusicDB.getInstance(mContext).getAlbums();
        mMusics = MusicDB.getInstance(mContext).getMusics();
    }

    @Override
    public int getCount() {
        return mAlbums.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlbums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Album album = mAlbums.get(position);
        ViewHolder holder = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.view_album_b,null);
            holder = new ViewHolder();
            holder.imageAlbum = (ImageView) convertView.findViewById(R.id.album_iamge_cover);
            holder.textCover = (TextView) convertView.findViewById(R.id.album_text_cover);
            holder.textAlbumName = (TextView) convertView.findViewById(R.id.album_text_name);
            holder.textSingerName = (TextView) convertView.findViewById(R.id.album_text_singername);
            holder.textYear = (TextView) convertView.findViewById(R.id.album_text_year);
            holder.textCount = (TextView) convertView.findViewById(R.id.album_text_count);
            convertView.setTag(holder);
        }else holder = (ViewHolder) convertView.getTag();




        boolean hasPic = false;
        for (int songId : album.getSonglist()){
            Music song = mMusics.get(songId-1);
            if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()){
//                String picPathStr = null;
                holder.imageAlbum.setVisibility(View.VISIBLE);
                holder.textCover.setVisibility(View.INVISIBLE);
                Glide.with(mContext)
                        .load(MusicDB.getInstance(mContext).selectPicturePath(song,MusicDB.PREFER_ALBUM))
                        .into(holder.imageAlbum);
                holder.imageAlbum.setVisibility(View.VISIBLE);
                holder.textCover.setVisibility(View.INVISIBLE);
                hasPic = true;
                break;
            }
        }

        if (!hasPic){
            holder.imageAlbum.setVisibility(View.INVISIBLE);
            holder.textCover.setVisibility(View.VISIBLE);
            holder.textCover.setBackgroundColor(Color.parseColor(Tools.randomColor()));
            holder.textCover.setText(String.valueOf(album.getName().charAt(0)));
        }

        String year = "未知年代";
        if (album.getYear()!=null){
            year = album.getYear() + "年";
        }
        holder.textAlbumName.setText(album.getName());
        holder.textYear.setText(year);
        holder.textSingerName.setText(album.getArtist());
        holder.textCount.setText(album.getSonglist().size() + "首" + " • 共"+ album.getTotalTime()/1000/60 + "分钟");
        return convertView;
    }

    private class ViewHolder{
        ImageView imageAlbum;
        TextView textCover;
        TextView textAlbumName;
        TextView textSingerName;
        TextView textYear;
        TextView textCount;
    }
}
