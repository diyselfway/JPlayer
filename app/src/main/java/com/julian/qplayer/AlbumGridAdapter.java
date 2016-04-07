package com.julian.qplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/4.
 */
public class AlbumGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Album> mAlbums;
    private ArrayList<Music> mMusics;

    public AlbumGridAdapter(Context context) {
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
        return mAlbums.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music firstSong = mMusics.get(mAlbums.get(position).getSonglist().get(0) - 1);
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.view_album, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.album_iamge_cover);
            holder.text_album = (TextView) convertView.findViewById(R.id.album_text_name);
            holder.text_singer = (TextView) convertView.findViewById(R.id.album_text_singername);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.album_info);
            holder.text_cover = (TextView) convertView.findViewById(R.id.album_text_cover);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        String album_name = firstSong.getAlbum();
        holder.text_album.setText(album_name);
//        holder.text_singer.setText(firstSong.getArtist() + " • " + mAlbums.get(position).getSize() + "首歌曲");
        holder.text_singer.setText(firstSong.getArtist());

        boolean hasPic = false;
        for (int songId : mAlbums.get(position).getSonglist()) {
            Music song = mMusics.get(songId - 1);
            if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()) {
//                String picPathStr = null;
                holder.imageView.setVisibility(View.VISIBLE);
                holder.text_cover.setVisibility(View.INVISIBLE);
//                if (song.hasPicAlbum()) {
//                    picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator+ song.getAlbum() + ".png";
//                } else if (song.hasCover()){
//                    picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator+ song.getAlbum() + ".png";
//                } else if (song.hasPicArtist()) {
//                    picPathStr = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator +  song.getArtist() + ".png";
//                }
                //                    FileInputStream fileInputStream = new FileInputStream(picPathStr);
//                    Bitmap bitmap = MusicDB.getInstance(mContext).selectPicture(song,MusicDB.PREFER_ALBUM);
                final ViewHolder finalHolder = holder;
                Glide.with(mContext)
                        .load(MusicDB.getInstance(mContext).selectPicturePath(song,MusicDB.PREFER_ALBUM))
                        .asBitmap()
                        .into(new BitmapImageViewTarget(finalHolder.imageView){
                            @Override
                            protected void setResource(Bitmap resource) {
                                super.setResource(resource);
                                Palette.Swatch swatch = Utils.getSwatch(resource);
                                finalHolder.linearLayout.setBackgroundColor(swatch.getRgb());
                                finalHolder.text_album.setTextColor(swatch.getBodyTextColor());
                                finalHolder.text_singer.setTextColor(swatch.getTitleTextColor());
                            }
                        });
//                    holder.imageView.setImageBitmap(bitmap);
//                    int[] avgColor = Tools.getAverageColor(bitmap, 0.5, 0.25, 0.75);
//                    holder.linearLayout.setBackgroundColor(Color.argb(255, avgColor[0], avgColor[1], avgColor[2]));
//                    int gray = (299 * avgColor[0] + 587 * avgColor[1] + 114 * avgColor[2]) / 1000;
//                    if (gray < 190) {
//                        holder.text_album.setTextColor(Color.WHITE);
//                        holder.text_singer.setTextColor(Color.WHITE);
//                    } else {
//                        holder.text_album.setTextColor(Color.rgb(255 - gray, 255 - gray, 255 - gray));
//                        holder.text_singer.setTextColor(Color.rgb(255 - gray, 255 - gray, 255 - gray));
//                    }
                hasPic = true;
                break;
            }
        }
        if (!hasPic) {
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.text_cover.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(R.drawable.album);
            holder.linearLayout.setBackgroundColor(Color.parseColor(Tools.randomColor()));
            holder.text_album.setTextColor(Color.WHITE);
            holder.text_singer.setTextColor(Color.WHITE);
            holder.text_cover.setBackgroundColor(Color.parseColor(Tools.randomColor()));
            holder.text_cover.setText(String.valueOf(album_name.charAt(0)));
        }
        return convertView;
    }

    private final class ViewHolder{
        ImageView imageView;
        TextView text_album;
        TextView text_singer;
        LinearLayout linearLayout;
        TextView text_cover;
    }
}
