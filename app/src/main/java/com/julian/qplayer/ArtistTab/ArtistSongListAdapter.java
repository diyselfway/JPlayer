package com.julian.qplayer.ArtistTab;

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
 * Created by Julian on 2016/3/14.
 */
public class ArtistSongListAdapter extends BaseAdapter {
    private ArrayList<Integer> mSonglist;
    private Context mContext;
    private ArrayList<Music> mMusics;

    public ArtistSongListAdapter(Context context, ArrayList<Integer> songlist) {
        mContext = context;
        mMusics = MusicDB.getInstance(mContext).getMusics();
        mSonglist = songlist;
    }

    @Override
    public int getCount() {
        return mSonglist.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusics.get(mSonglist.get(position) - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music song = mMusics.get(mSonglist.get(position)-1);
        ViewHolder holder = null;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView =  inflater.inflate(R.layout.view_song_artist,null);
            holder = new ViewHolder();
            holder.albumImage = (ImageView) convertView.findViewById(R.id.song_iamge_cover_artist);
            holder.songName = (TextView) convertView.findViewById(R.id.song_text_song_name_artist);
            holder.albumName = (TextView) convertView.findViewById(R.id.song_text_album_name_artist);
            holder.textCover = (TextView) convertView.findViewById(R.id.song_text_cover_artist);
            convertView.setTag(holder);
        }else holder = (ViewHolder) convertView.getTag();
        String song_name = song.getName();
        if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()){
//            String picPathStr = null;
            holder.albumImage.setVisibility(View.VISIBLE);
            holder.textCover.setVisibility(View.INVISIBLE);
            Glide.with(mContext)
                    .load(MusicDB.getInstance(mContext).selectPicturePath(song,MusicDB.PREFER_ARTIST))
                    .into(holder.albumImage);
//            if (song.hasPicAlbum()) {
//                picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator+ song.getAlbum() + ".png";
//            } else if (song.hasCover()){
//                picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator+ song.getAlbum() + ".png";
//            } else if (song.hasPicArtist()) {
//                picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator +  song.getArtist() + ".png";
//            }
//            try {
//                FileInputStream fileInputStream = new FileInputStream(picPathStr);
//                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
//                holder.albumImage.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            holder.albumImage.setVisibility(View.VISIBLE);
//            holder.textCover.setVisibility(View.INVISIBLE);
        }else {
            holder.albumImage.setVisibility(View.INVISIBLE);
            holder.textCover.setVisibility(View.VISIBLE);
            holder.textCover.setBackgroundColor(Color.parseColor(Tools.randomColor()));
            holder.textCover.setText(String.valueOf(song_name.charAt(0)));
        }
        holder.songName.setText(song_name);
        holder.albumName.setText(song.getAlbum());
        return convertView;
    }

    private final class ViewHolder {
        ImageView albumImage;
        TextView songName;
        TextView albumName;
        TextView textCover;
    }
}
