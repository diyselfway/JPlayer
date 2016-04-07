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
 * Created by Julian on 2016/3/6.
 */
public class SingerGridAdapter extends BaseAdapter {
    private static final String TAG = SingerGridAdapter.class.getSimpleName();
    private Context mContext;
    private final ArrayList<Artist> mArtists;
    private final ArrayList<Music> mMusics;

    public SingerGridAdapter(Context context) {
        mContext = context;
        mArtists = MusicDB.getInstance(mContext).getArtists();
        mMusics = MusicDB.getInstance(mContext).getMusics();
    }

    @Override
    public int getCount() {
        return mArtists.size();
    }

    @Override
    public Object getItem(int position) {
        return mArtists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mArtists.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music firstSong = mMusics.get(mArtists.get(position).getSonglist().get(0) - 1);
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.view_singer, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.singer_iamge);
            holder.text_cover = (TextView) convertView.findViewById(R.id.singer_text_cover);
            holder.text_singer = (TextView) convertView.findViewById(R.id.singer_text_name);
            holder.text_info = (TextView) convertView.findViewById(R.id.singer_text_info);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.singer_info);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        holder.text_singer.setText(firstSong.getArtist());
        int count_song = mArtists.get(position).getCount();
        int count_album = mArtists.get(position).getAlbumlist().size();
        holder.text_info.setText(count_album + "张专辑" + " • " + count_song + "首作品");

        boolean hasPic = false;
        for (int songId : mArtists.get(position).getSonglist()) {
            Music song = mMusics.get(songId - 1);
            if (song.hasPicArtist() || song.hasPicAlbum() || song.hasCover()) {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.text_cover.setVisibility(View.INVISIBLE);
                //                    Bitmap bitmap = MusicDB.getInstance(mContext).selectPicture(song,MusicDB.PREFER_ARTIST);
//                    holder.imageView.setImageBitmap(bitmap);
                final ViewHolder finalHolder = holder;
                Glide.with(mContext)
                        .load(MusicDB.getInstance(mContext).selectPicturePath(song, MusicDB.PREFER_ARTIST))
                        .asBitmap()
                        .into(new BitmapImageViewTarget(holder.imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                super.setResource(resource);
                                Palette.Swatch swatch = Utils.getSwatch(resource);
                                finalHolder.linearLayout.setBackgroundColor(swatch.getRgb());
                                finalHolder.text_singer.setTextColor(swatch.getBodyTextColor());
                                finalHolder.text_info.setTextColor(swatch.getTitleTextColor());
                            }
                        });
//                    int[] avgColor = Tools.getAverageColor(bitmap, 0.5, 0.25, 0.75);
//                    holder.linearLayout.setBackgroundColor(Color.argb(255, avgColor[0], avgColor[1], avgColor[2]));
//                    int gray = (299 * avgColor[0] + 587 * avgColor[1] + 114 * avgColor[2]) / 1000;
//                    if (gray < 190) {
//                        holder.text_info.setTextColor(Color.WHITE);
//                        holder.text_singer.setTextColor(Color.WHITE);
//                    } else {
//                        holder.text_info.setTextColor(Color.rgb(255 - gray, 255 - gray, 255 - gray));
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
            holder.text_info.setTextColor(Color.WHITE);
            holder.text_singer.setTextColor(Color.WHITE);
            holder.text_cover.setBackgroundColor(Color.parseColor(Tools.randomColor()));
            holder.text_cover.setText(String.valueOf(firstSong.getArtist().charAt(0)));
        }
        return convertView;
    }

    private final class ViewHolder {
        ImageView imageView;
        TextView text_cover;
        TextView text_singer;
        TextView text_info;
        LinearLayout linearLayout;
    }
}
