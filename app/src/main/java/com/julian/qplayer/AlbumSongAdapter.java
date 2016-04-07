package com.julian.qplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/6.
 */
public class AlbumSongAdapter extends BaseAdapter {

    private ArrayList<Integer> mPlaylist;
    private Context mContext;
    private ArrayList<Music> mMusics;

    public AlbumSongAdapter(Context context, ArrayList<Integer> playlist){
        mPlaylist = playlist;
        mContext = context;
        mMusics = MusicDB.getInstance(mContext).getMusics();
    }
    @Override
    public int getCount() {
        return mPlaylist.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlaylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPlaylist.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music song = mMusics.get(mPlaylist.get(position)-1);
        ViewHolder holder = null;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.view_song_album,null);
            holder = new ViewHolder();
//            holder.albumImage = (ImageView) convertView.findViewById(R.id.song_iamge_cover);
            holder.songName = (TextView) convertView.findViewById(R.id.song_text_song_name_album);
            holder.duration = (TextView) convertView.findViewById(R.id.song_text_duration_album);
            holder.text_cover = (TextView) convertView.findViewById(R.id.song_text_id);
            convertView.setTag(holder);
        }else holder = (ViewHolder) convertView.getTag();
        String song_name = song.getName();

//        if (song.hasPicAlbum() || song.hasCover() || song.hasPicArtist()){
//            String picPathStr = null;
////            holder.albumImage.setVisibility(View.VISIBLE);
//            holder.text_cover.setVisibility(View.INVISIBLE);
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
////                holder.albumImage.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
////            holder.albumImage.setVisibility(View.VISIBLE);
//            holder.text_cover.setVisibility(View.INVISIBLE);
//        }else {
////            holder.albumImage.setVisibility(View.INVISIBLE);
//            holder.text_cover.setVisibility(View.VISIBLE);
//            holder.text_cover.setBackgroundColor(Color.parseColor(Tools.randomColor()));
//            holder.text_cover.setText(String.valueOf(song_name.charAt(0)));
//        }

//        if (song.hasCover()){
//            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//            mmr.setDataSource(song.getPath());
//            byte[] coverBytes = mmr.getEmbeddedPicture();
//            holder.albumImage.setVisibility(View.VISIBLE);
//            holder.text_cover.setVisibility(View.INVISIBLE);
//            holder.albumImage.setImageBitmap(BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length));
//        }else {
//            holder.albumImage.setVisibility(View.INVISIBLE);
//            holder.text_cover.setVisibility(View.VISIBLE);
//            holder.text_cover.setBackgroundColor(Color.parseColor(Tools.randomColor()));
//            holder.text_cover.setText(String.valueOf(song_name.charAt(0)));
//        }
        holder.songName.setText(song_name);
        int m = song.getDuration()/1000/60;
        int s = song.getDuration()/1000%60;
        holder.duration.setText("" + m + ":" + s);
        holder.text_cover.setText(""+ (position+1));
        return convertView;
    }

    private final class ViewHolder{
//        ImageView albumImage;
        TextView songName;
        TextView duration;
        TextView text_cover;
    }
}
