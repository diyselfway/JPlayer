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
import com.julian.qplayer.Album;
import com.julian.qplayer.Music;
import com.julian.qplayer.MusicDB;
import com.julian.qplayer.R;
import com.julian.qplayer.Tools;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/14.
 */
public class ArtistAlbumListAdapter extends BaseAdapter {
    private ArrayList<Integer> mAlbumlist;
    private Context mContext;
    private ArrayList<Album> mAlbums;
    private ArrayList<Music> mMusics;

    public ArtistAlbumListAdapter(Context context, ArrayList<Integer> albumlist) {
        mContext = context;
        mAlbums = MusicDB.getInstance(mContext).getAlbums();
        mMusics = MusicDB.getInstance(mContext).getMusics();
        mAlbumlist = albumlist;
    }

    @Override
    public int getCount() {
        return mAlbumlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlbums.get(mAlbumlist.get(position) - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Album album = mAlbums.get(mAlbumlist.get(position)-1);
        ViewHolder holder = null;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView =  inflater.inflate(R.layout.view_album_artist,null);
            holder = new ViewHolder();
            holder.albumImage = (ImageView) convertView.findViewById(R.id.album_iamge_cover_artist);
            holder.textCover = (TextView) convertView.findViewById(R.id.album_text_cover_artist);
            holder.albumName = (TextView) convertView.findViewById(R.id.album_text_name_artist);
            holder.textYear = (TextView) convertView.findViewById(R.id.album_text_year_artist);
            holder.textCount = (TextView) convertView.findViewById(R.id.album_text_count_artist);
            convertView.setTag(holder);
        }else holder = (ViewHolder) convertView.getTag();

        boolean hasPic = false;
        for (int songId : album.getSonglist()){
            Music song = mMusics.get(songId-1);
            if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()){
//                String picPathStr = null;
                holder.albumImage.setVisibility(View.VISIBLE);
                holder.textCover.setVisibility(View.INVISIBLE);
//                if (song.hasPicAlbum()) {
//                    picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator+ song.getAlbum() + ".png";
//                } else if (song.hasCover()){
//                    picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator+ song.getAlbum() + ".png";
//                } else if (song.hasPicArtist()) {
//                    picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator +  song.getArtist() + ".png";
//                }
//                try {
////                    FileInputStream fileInputStream = new FileInputStream(picPathStr);
//                    Bitmap bitmap = MusicDB.getInstance(mContext).selectPicture(song,MusicDB.PREFER_ALBUM);
//                    holder.albumImage.setImageBitmap(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                Glide.with(mContext)
                        .load(MusicDB.getInstance(mContext).selectPicturePath(song,MusicDB.PREFER_ALBUM))
                        .into(holder.albumImage);
                holder.albumImage.setVisibility(View.VISIBLE);
                holder.textCover.setVisibility(View.INVISIBLE);
                hasPic = true;
                break;
            }
        }

        if (!hasPic){
            holder.albumImage.setVisibility(View.INVISIBLE);
            holder.textCover.setVisibility(View.VISIBLE);
            holder.textCover.setBackgroundColor(Color.parseColor(Tools.randomColor()));
            holder.textCover.setText(String.valueOf(album.getName().charAt(0)));
        }

        holder.albumName.setText(album.getName());
        String year = "未知年代";
        if (album.getYear()!=null){
            year = album.getYear() + "年";
        }
        holder.textYear.setText(year);
        holder.textCount.setText(album.getSonglist().size() + "首" + " • 共"+ album.getTotalTime()/1000/60 + "分钟");
        return convertView;
    }

    private final class ViewHolder {
        ImageView albumImage;
        TextView textCover;
        TextView albumName;
        TextView textYear;
        TextView textCount;
    }
}
