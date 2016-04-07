package com.julian.qplayer.Player;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.julian.qplayer.Music;
import com.julian.qplayer.MusicDB;
import com.julian.qplayer.R;
import com.julian.qplayer.Tools;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/16.
 */
public class PlaylistAdapter_A extends BaseAdapter {
    private ArrayList<Integer> mPlaylist;
    private Context mContext;
    private ArrayList<Music> mMusics;

    public PlaylistAdapter_A(Context context, ArrayList<Integer> playlist) {
        mContext = context;
        mPlaylist = playlist;
        mMusics = MusicDB.getInstance(mContext).getMusics();
    }

    @Override
    public int getCount() {
        return mPlaylist.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusics.get(mPlaylist.get(position)-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.view_song,null);
            holder = new ViewHolder();
            holder.albumImage = (ImageView) convertView.findViewById(R.id.song_iamge_cover);
            holder.songName = (TextView) convertView.findViewById(R.id.song_text_song_name);
            holder.singerName = (TextView) convertView.findViewById(R.id.song_text_singer);
            holder.text_cover = (TextView) convertView.findViewById(R.id.song_text_cover);
            convertView.setTag(holder);
        }else holder = (ViewHolder) convertView.getTag();
        Music song = mMusics.get(mPlaylist.get(position) - 1);
        String song_name = song.getName();
        if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()){
//            String picPathStr = null;
//            if (song.hasPicAlbum()) {
//                picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator+ song.getAlbum() + ".png";
//            } else if (song.hasCover()){
//                picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator+ song.getAlbum() + ".png";
//            } else if (song.hasPicArtist()) {
//                picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator +  song.getArtist() + ".png";
//            }
            //                FileInputStream fileInputStream = new FileInputStream(picPathStr);
//                Bitmap bitmap = MusicDB.getInstance(mContext).selectPicture(song,MusicDB.PREFER_ALBUM);
//                holder.albumImage.setImageBitmap(bitmap);
            Glide.with(mContext)
                    .load(MusicDB.getInstance(mContext).selectPicturePath(song,MusicDB.PREFER_ALBUM))
                    .into(holder.albumImage);
            holder.albumImage.setVisibility(View.VISIBLE);
            holder.text_cover.setVisibility(View.INVISIBLE);
        }else {
            holder.albumImage.setVisibility(View.INVISIBLE);
            holder.text_cover.setVisibility(View.VISIBLE);
            holder.text_cover.setBackgroundColor(Color.parseColor(Tools.randomColor()));
            holder.text_cover.setText(String.valueOf(song_name.charAt(0)));
        }
        holder.songName.setText(song_name);
        holder.singerName.setText(song.getArtist());
        return convertView;
    }

    private final class ViewHolder{
        ImageView albumImage;
        TextView songName;
        TextView singerName;
        TextView text_cover;
    }
}
