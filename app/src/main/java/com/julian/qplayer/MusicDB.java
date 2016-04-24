package com.julian.qplayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.julian.qplayer.lrc.LrcInfo;
import com.julian.qplayer.lrc.LrcJson;
import com.julian.qplayer.lrc.LrcParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Julian on 2016/3/8.
 * 音乐数据
 */
public class MusicDB {
    private static final String TAG = MusicDB.class.getSimpleName();
    public static final int PREFER_ARTIST = 0;
    public static final int PREFER_ALBUM = 1;
    public static final int PREFER_BEST = 2;
    private ArrayList<Music> mMusics = new ArrayList<>();
    private ArrayList<Album> mAlbums = new ArrayList<>();
    private ArrayList<Artist> mArtists = new ArrayList<>();
    private volatile static MusicDB instance;
    private static Context appContext;

    private MusicDB(Context appContext) {
        Cursor sCursor;
        sCursor = appContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        int id = 0;
        if (sCursor != null && sCursor.moveToFirst()) {
            do {
                String duration = sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                //过滤长度小于30s的音频文件
                if (Integer.parseInt(duration) < 30000) continue;
                id++;
                Music music = new Music(id);
                music.set_id(Integer.parseInt(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))));
                music.setName(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                music.setArtist(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                music.setAlbum(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                music.setDuration(Integer.parseInt(duration));
                music.setPath(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                music.setDispName(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setSize(Integer.parseInt(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))));
                music.setYear(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)));
                mMusics.add(music);
            } while (sCursor.moveToNext());
            sCursor.close();
        }

        //整理专辑数据
        ArrayList<String> albumlist = new ArrayList<>();
        for (Music music : mMusics) {
            if (!albumlist.contains(music.getAlbum())) {
                Album album = new Album(albumlist.size() + 1);
                album.setName(music.getAlbum());
                album.setArtist(music.getArtist());
                mAlbums.add(album);
                albumlist.add(music.getAlbum());
            }
        }
        for (int i = 0; i < albumlist.size(); i++) {
            int size = 0;
            int totalTime = 0;
            ArrayList<Integer> songlist = new ArrayList<>();
            for (Music music : mMusics) {
                if (music.getAlbum().equals(mAlbums.get(i).getName())) {
                    size++;
                    songlist.add(music.getId());
                    totalTime += music.getDuration();
                }
            }
            mAlbums.get(i).setSize(size);
            mAlbums.get(i).setSonglist(songlist);
            mAlbums.get(i).setTotalTime(totalTime);
            mAlbums.get(i).setYear(mMusics.get(songlist.get(0) - 1).getYear());
        }

        //整理歌手数据
        ArrayList<String> artistlist = new ArrayList<>();
        for (Music music : mMusics) {
            if (!artistlist.contains(music.getArtist())) {
                Artist artist = new Artist(artistlist.size() + 1);
                artist.setName(music.getArtist());
                mArtists.add(artist);
                artistlist.add(music.getArtist());
            }
        }
        for (int i = 0; i < artistlist.size(); i++) {
            int count = 0;
            int totalTime = 0;
            ArrayList<Integer> songlist = new ArrayList<>();
            ArrayList<String> album_list = new ArrayList<>();
            for (Music music : mMusics) {
                if (music.getArtist().equals(mArtists.get(i).getName())) {
                    count++;
                    songlist.add(music.getId());
                    totalTime += music.getDuration();
                    if (!album_list.contains(music.getAlbum())) {
                        album_list.add(music.getAlbum());
                    }
                }
            }
            ArrayList<Integer> albumIdList = new ArrayList<>();
            for (int j = 0; j < album_list.size(); j++) {
                for (Album album : mAlbums) {
                    if (album.getName().equals(album_list.get(j))) {
                        albumIdList.add(album.getId());
                    }
                }
            }
            mArtists.get(i).setCount(count);
            mArtists.get(i).setSonglist(songlist);
            mArtists.get(i).setTotalTime(totalTime);
            mArtists.get(i).setAlbumlist(albumIdList);
        }

        initMusicData();

        //下载歌词文件
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                downloadLrc();
//            }
//        }).start();
    }

    public static MusicDB getInstance(Context c) {
        appContext = c.getApplicationContext();
        if (instance == null) {
            synchronized (MusicDB.class) {
                if (instance == null) {
                    instance = new MusicDB(appContext);
                }
            }
        }
        return instance;
    }

    private void initMusicData() {
        //获取并保存内置专辑封面
        String dirPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator;
        File dirPath = new File(dirPathStr);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        for (Music music : mMusics) {
            String filePathStr = dirPathStr + music.getAlbum() + ".png";
            File file = new File(filePathStr);
            if (file.exists()) {
                music.setHasCover(true);
            } else {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(music.getPath());
                byte[] coverBytes = mmr.getEmbeddedPicture();
                if (coverBytes == null) {
                    music.setHasCover(false);
                } else {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (file.getTotalSpace() < 1000) {
                        file.delete();
                        music.setHasCover(false);
                    }
                }
            }
        }
        initArtistInfo();
        initAlbumInfo();
        initLrc();
    }

    public ArrayList<Music> getMusics() {
        return mMusics;
    }

    public ArrayList<Album> getAlbums() {
        return mAlbums;
    }

    public ArrayList<Artist> getArtists() {
        return mArtists;
    }

    //联网获取歌手信息
    public void getArtistInfo() {
        String urlBase = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&api_key=fdb3a51437d4281d4d64964d333531d4&format=json&artist=";
        String dirPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator;
        File dirPath = new File(dirPathStr);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        for (Artist artist : mArtists) {
            String picPath = dirPathStr + artist.getName() + ".png";
            File pic = new File(picPath);
            if (pic.exists()) {
                artist.setHasPicture(true);
//                artist.setHasPicLink(1);
                for (int songId : artist.getSonglist()) {
                    mMusics.get(songId - 1).setHasPicArtist(true);
                }
            } else {
                try {
                    String urlStr = urlBase + artist.getName();
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout(30000);
                    connection.setInstanceFollowRedirects(true);
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader reader = new InputStreamReader(inputStream);
                        Gson gson = new Gson();
                        JsonReader jsonReader = new JsonReader(reader);
                        jsonReader.setLenient(true);
                        ArtistInfo artistInfo = gson.fromJson(jsonReader, ArtistInfo.class);
//                        artist.setArtistInfo(artistInfo);
                        String picUrlStr = artistInfo.getInfo().getImage().get(3).getUrl();
                        Log.d(TAG, artist.getId() + " " + artist.getName() + " " + picUrlStr);
                        if (!picUrlStr.isEmpty()) {
                            url = new URL(picUrlStr);
                            artist.setHasPicLink(1);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(30000);
                            connection.setReadTimeout(60000);
                            connection.setInstanceFollowRedirects(true);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                inputStream = connection.getInputStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                FileOutputStream fileOutputStream = new FileOutputStream(picPath);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                                artist.setHasPicture(true);
//                                artist.setHasPicLink(1);
                                for (int songId : artist.getSonglist()) {
                                    mMusics.get(songId - 1).setHasPicArtist(true);
                                }
                                Log.d(TAG, artist.getName() + "头像下载并保存成功");
                                inputStream.close();
                                fileOutputStream.close();
                            }
                        } else {
                            artist.setHasPicLink(-1);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                RequestQueue requestQueue = Volley.newRequestQueue(appContext);
//                if (artist.hasPicLink() == -1) continue;
//                String urlStr = urlBase + artist.getName();
//                GsonRequest<ArtistInfo> gsonRequest = new GsonRequest<>(urlStr, ArtistInfo.class, new Response.Listener<ArtistInfo>() {
//                    @Override
//                    public void onResponse(ArtistInfo artistInfo) {
//                        artist.setArtistInfo(artistInfo);
//                        String picUrlStr = artistInfo.getInfo().getImage().get(3).getUrl();
//                        Log.d(TAG, artist.getId() + " " + artist.getName() + " Info :" + artist.getArtistInfo().getInfo().getImage().get(3).getUrl());
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, artist.getId() + " " + artist.getName() + " Info 获取失败");
//                    }
//                });
//                requestQueue.add(gsonRequest);
            }
            Log.d(TAG, artist.getName() + "有图片链接：" + artist.hasPicLink());
//            Log.d(TAG, artist.getId() + ": " + artist.getName());
        }
    }

    //整理歌手信息
    public void initArtistInfo() {
//        String urlBase = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&api_key=fdb3a51437d4281d4d64964d333531d4&format=json&artist=";
        String dirPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator;
        File dirPath = new File(dirPathStr);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        for (Artist artist : mArtists) {
            String picPath = dirPathStr + artist.getName() + ".png";
            File pic = new File(picPath);
            if (pic.exists()) {
                artist.setHasPicture(true);
//                artist.setHasPicLink(1);
                for (int songId : artist.getSonglist()) {
                    mMusics.get(songId - 1).setHasPicArtist(true);
                }
            }
//            else {
//                try {
//                    String urlStr = urlBase + artist.getName();
//                    URL url = new URL(urlStr);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setConnectTimeout(30000);
//                    connection.setReadTimeout(30000);
//                    connection.setInstanceFollowRedirects(true);
//                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        InputStream inputStream = connection.getInputStream();
//                        InputStreamReader reader = new InputStreamReader(inputStream);
//                        Gson gson = new Gson();
//                        JsonReader jsonReader = new JsonReader(reader);
//                        jsonReader.setLenient(true);
//                        ArtistInfo artistInfo = gson.fromJson(jsonReader, ArtistInfo.class);
////                        artist.setArtistInfo(artistInfo);
//                        String picUrlStr = artistInfo.getInfo().getImage().get(3).getUrl();
//                        Log.d(TAG, artist.getId() + " " + artist.getName() + " " + picUrlStr);
//                        if (!picUrlStr.isEmpty()) {
//                            url = new URL(picUrlStr);
//                            artist.setHasPicLink(1);
//                            connection = (HttpURLConnection) url.openConnection();
//                            connection.setConnectTimeout(30000);
//                            connection.setReadTimeout(60000);
//                            connection.setInstanceFollowRedirects(true);
//                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                                inputStream = connection.getInputStream();
//                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                                FileOutputStream fileOutputStream = new FileOutputStream(picPath);
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
//                                artist.setHasPicture(true);
////                                artist.setHasPicLink(1);
//                                for (int songId : artist.getSonglist()) {
//                                    mMusics.get(songId - 1).setHasPicArtist(true);
//                                }
//                                Log.d(TAG, artist.getName() + "头像下载并保存成功");
//                                inputStream.close();
//                                fileOutputStream.close();
//                            }
//                        } else {
//                            artist.setHasPicLink(-1);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                RequestQueue requestQueue = Volley.newRequestQueue(appContext);
//                if (artist.hasPicLink() == -1) continue;
//                String urlStr = urlBase + artist.getName();
//                GsonRequest<ArtistInfo> gsonRequest = new GsonRequest<>(urlStr, ArtistInfo.class, new Response.Listener<ArtistInfo>() {
//                    @Override
//                    public void onResponse(ArtistInfo artistInfo) {
//                        artist.setArtistInfo(artistInfo);
//                        String picUrlStr = artistInfo.getInfo().getImage().get(3).getUrl();
//                        Log.d(TAG, artist.getId() + " " + artist.getName() + " Info :" + artist.getArtistInfo().getInfo().getImage().get(3).getUrl());
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, artist.getId() + " " + artist.getName() + " Info 获取失败");
//                    }
//                });
//                requestQueue.add(gsonRequest);
//            }
//            Log.d(TAG, artist.getName() + "有图片链接：" + artist.hasPicLink());
//            Log.d(TAG, artist.getId() + ": " + artist.getName());
        }
    }

    //联网获取专辑信息
    public void getAlbumInfo() {
//        String urlBase = "http://ws.audioscrobbler.com/2.0/?api_key=87a4fc682c7e61c0b880b798a472ab6f&format=json&autocorrect=1&method=album.getInfo&artist=歌手名&album=专辑名";
        String urlBase = "http://ws.audioscrobbler.com/2.0/?api_key=87a4fc682c7e61c0b880b798a472ab6f&format=json&autocorrect=1&method=album.getInfo";
        String dirPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator;
        File dirPath = new File(dirPathStr);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        for (Album album : mAlbums) {
            String picPath = dirPathStr + album.getName() + ".png";
            File pic = new File(picPath);
            if (pic.exists()) {
                album.setHasPicture(true);
                for (int songId : album.getSonglist()) {
                    mMusics.get(songId - 1).setHasPicAlbum(true);
                }
            } else {
                try {
                    String urlStr = urlBase + "&artist=" + album.getArtist() + "&album=" + album.getName();
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout(30000);
                    connection.setInstanceFollowRedirects(true);
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader reader = new InputStreamReader(inputStream);
                        Gson gson = new Gson();
                        JsonReader jsonReader = new JsonReader(reader);
                        jsonReader.setLenient(true);
                        AlbumInfo albumInfo = gson.fromJson(jsonReader, AlbumInfo.class);
                        if (albumInfo.getInfo() == null) {
                            album.setHasPicLink(-1);
                            Log.d(TAG, album.getName() + "有图片链接：" + album.hasPicLink());
                            continue;
                        }
//                        album.setAlbumInfo(albumInfo);
                        String picUrlStr = albumInfo.getInfo().getImage().get(3).getUrl();
                        Log.d(TAG, album.getId() + " " + album.getName() + " " + picUrlStr);
                        if (!picUrlStr.isEmpty()) {
                            url = new URL(picUrlStr);
                            album.setHasPicLink(1);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(30000);
                            connection.setReadTimeout(60000);
                            connection.setInstanceFollowRedirects(true);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                inputStream = connection.getInputStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                FileOutputStream fileOutputStream = new FileOutputStream(picPath);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                                album.setHasPicture(true);
                                for (int songId : album.getSonglist()) {
                                    mMusics.get(songId - 1).setHasPicAlbum(true);
                                }
                                Log.d(TAG, album.getName() + "专辑封面下载并保存成功");
                                inputStream.close();
                                fileOutputStream.close();
                            }
                        } else {
                            album.setHasPicLink(-1);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, album.getName() + "有图片链接：" + album.hasPicLink());
        }
    }

    //整理专辑信息
    public void initAlbumInfo() {
//        String urlBase = "http://ws.audioscrobbler.com/2.0/?api_key=87a4fc682c7e61c0b880b798a472ab6f&format=json&autocorrect=1&method=album.getInfo";
        String dirPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator;
        File dirPath = new File(dirPathStr);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        for (Album album : mAlbums) {
            String picPath = dirPathStr + album.getName() + ".png";
            File pic = new File(picPath);
            if (pic.exists()) {
                album.setHasPicture(true);
                for (int songId : album.getSonglist()) {
                    mMusics.get(songId - 1).setHasPicAlbum(true);
                }
            }
//            else {
//                try {
//                    String urlStr = urlBase + "&artist=" + album.getArtist() + "&album=" +album.getName();
//                    URL url = new URL(urlStr);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setConnectTimeout(30000);
//                    connection.setReadTimeout(30000);
//                    connection.setInstanceFollowRedirects(true);
//                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        InputStream inputStream = connection.getInputStream();
//                        InputStreamReader reader = new InputStreamReader(inputStream);
//                        Gson gson = new Gson();
//                        JsonReader jsonReader = new JsonReader(reader);
//                        jsonReader.setLenient(true);
//                        AlbumInfo albumInfo = gson.fromJson(jsonReader, AlbumInfo.class);
//                        if (albumInfo.getInfo()== null){
//                            album.setHasPicLink(-1);
//                            Log.d(TAG, album.getName() + "有图片链接：" + album.hasPicLink());
//                            continue;
//                        }
////                        album.setAlbumInfo(albumInfo);
//                        String picUrlStr = albumInfo.getInfo().getImage().get(3).getUrl();
//                        Log.d(TAG, album.getId() + " " + album.getName() + " " + picUrlStr);
//                        if (!picUrlStr.isEmpty()) {
//                            url = new URL(picUrlStr);
//                            album.setHasPicLink(1);
//                            connection = (HttpURLConnection) url.openConnection();
//                            connection.setConnectTimeout(30000);
//                            connection.setReadTimeout(60000);
//                            connection.setInstanceFollowRedirects(true);
//                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                                inputStream = connection.getInputStream();
//                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                                FileOutputStream fileOutputStream = new FileOutputStream(picPath);
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
//                                album.setHasPicture(true);
//                                for (int songId : album.getSonglist()) {
//                                    mMusics.get(songId - 1).setHasPicAlbum(true);
//                                }
//                                Log.d(TAG, album.getName() + "专辑封面下载并保存成功");
//                                inputStream.close();
//                                fileOutputStream.close();
//                            }
//                        } else {
//                            album.setHasPicLink(-1);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            Log.d(TAG, album.getName() + "有图片链接：" + album.hasPicLink());
        }
    }

    //联网下载歌词
    private void downloadLrc() {
//        String baseUrl = "http://geci.me/api/lyric/浮夸/陈奕迅";
        String baseUrl = "http://geci.me/api/lyric/";
        String dirPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + File.separator + "lrcs" + File.separator;
        File dirPath = new File(dirPathStr);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        for (Music song : mMusics) {
            String lrcPath = dirPathStr + song.getName() + ".lrc";
            File lrc = new File(lrcPath);
            if (lrc.exists()) {
                song.setHasLrc(true);
            } else {
                String urlStr = baseUrl + song.getName() + "/" + song.getArtist();
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout(30000);
                    connection.setInstanceFollowRedirects(true);
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader reader = new InputStreamReader(inputStream);
                        Gson gson = new Gson();
                        JsonReader jsonReader = new JsonReader(reader);
                        jsonReader.setLenient(true);
                        LrcJson lrcJson = gson.fromJson(jsonReader, LrcJson.class);
                        if (lrcJson.getResult() != null) {
                            for (int i = 0; i < lrcJson.getResult().size(); i++) {
                                String lrcUrl = lrcJson.getResult().get(i).getLrcUrl();
                                Log.d(TAG, song.getName() + "歌词地址：" + lrcUrl);
                                URL urlLrc = new URL(lrcUrl);
                                HttpURLConnection connectionLrc = (HttpURLConnection) urlLrc.openConnection();
                                connectionLrc.setConnectTimeout(30000);
                                connectionLrc.setReadTimeout(60000);
                                connectionLrc.setInstanceFollowRedirects(true);
                                if (connectionLrc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    InputStream inputStreamLrc = connectionLrc.getInputStream();
                                    FileOutputStream fileOutputStreamLrc = new FileOutputStream(lrc);
                                    byte[] buffer = new byte[1024 * 4];
                                    while (inputStreamLrc.read(buffer) != -1) {
                                        fileOutputStreamLrc.write(buffer);
                                    }
                                    inputStreamLrc.close();
                                    fileOutputStreamLrc.close();
                                    song.setHasLrc(true);
                                    Log.d(TAG, song.getName() + "歌词下载并保存成功");
                                    LrcParser lrcParser = new LrcParser();
//                                    String lrcDirPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + File.separator + "lrcs" + File.separator;
//                                    String lrcPathTemp = lrcDirPathStr + song.getName() + ".lrc";
                                    try {
                                        LrcInfo lrcInfo = lrcParser.parser(lrcPath);
                                        if (lrcInfo == null | lrcInfo.getLrcs().size() < 10) {
                                            (new File(lrcPath)).delete();
                                            song.setHasLrc(false);
                                            Log.d(TAG, song.getName() + "歌词不满足要求，删除");
                                            continue;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                        }
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //整理歌词信息
    private void initLrc() {
//        String baseUrl = "http://geci.me/api/lyric/浮夸/陈奕迅";
//        String baseUrl = "http://geci.me/api/lyric/";
        String dirPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + File.separator + "lrcs" + File.separator;
        File dirPath = new File(dirPathStr);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        for (Music song : mMusics) {
            String lrcPath = dirPathStr + song.getName() + ".lrc";
            File lrc = new File(lrcPath);
            if (lrc.exists()) {
                song.setHasLrc(true);
            }
//            else {
//                String urlStr = baseUrl + song.getName() + "/" + song.getArtist();
//                try {
//                    URL url = new URL(urlStr);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setConnectTimeout(30000);
//                    connection.setReadTimeout(30000);
//                    connection.setInstanceFollowRedirects(true);
//                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        InputStream inputStream = connection.getInputStream();
//                        InputStreamReader reader = new InputStreamReader(inputStream);
//                        Gson gson = new Gson();
//                        JsonReader jsonReader = new JsonReader(reader);
//                        jsonReader.setLenient(true);
//                        LrcJson lrcJson = gson.fromJson(jsonReader, LrcJson.class);
//                        if (lrcJson.getResult() != null) {
//                            for (int i = 0; i < lrcJson.getResult().size(); i++) {
//                                String lrcUrl = lrcJson.getResult().get(i).getLrcUrl();
//                                Log.d(TAG,song.getName() + "歌词地址：" + lrcUrl );
//                                URL urlLrc = new URL(lrcUrl);
//                                HttpURLConnection connectionLrc = (HttpURLConnection) urlLrc.openConnection();
//                                connectionLrc.setConnectTimeout(30000);
//                                connectionLrc.setReadTimeout(60000);
//                                connectionLrc.setInstanceFollowRedirects(true);
//                                if (connectionLrc.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                                    InputStream inputStreamLrc = connectionLrc.getInputStream();
//                                    FileOutputStream fileOutputStreamLrc = new FileOutputStream(lrc);
//                                    byte[] buffer = new byte[1024*4];
//                                    while (inputStreamLrc.read(buffer) != -1) {
//                                        fileOutputStreamLrc.write(buffer);
//                                    }
//                                    inputStreamLrc.close();
//                                    fileOutputStreamLrc.close();
//                                    song.setHasLrc(true);
//                                    Log.d(TAG,song.getName() + "歌词下载并保存成功" );
//                                    break;
//                                }
//                            }
//                        }
//                        inputStream.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public Artist findArtistBySongId(int songId) {
        Artist targetArtist = null;
        for (Artist artist : mArtists) {
            if (Objects.equals(mMusics.get(songId - 1).getArtist(), artist.getName())) {
                targetArtist = artist;
            }
        }
        return targetArtist;
    }

    public Album findAlbumBySongId(int songId) {
        Album targetAlbum = null;
        for (Album album : mAlbums) {
            if (Objects.equals(mMusics.get(songId - 1).getAlbum(), album.getName())) {
                targetAlbum = album;
            }
        }
        return targetAlbum;
    }

    public Artist findArtistByName(String artistName) {
        Artist targetArtist = null;
        for (Artist artist : mArtists) {
            if (artist.getName().equals(artistName)) {
                targetArtist = artist;
            }
        }
        return targetArtist;
    }

    public Album findAlbumByName(String albumName) {
        Album targetAlbum = null;
        for (Album album : mAlbums) {
            if (album.getName().equals(albumName)) {
                targetAlbum = album;
            }
        }
        return targetAlbum;
    }

    public Album findAlbumById(int albumId) {
        Album targetAlbum = null;
        for (Album album : mAlbums) {
            if (album.getId() == albumId) {
                targetAlbum = album;
            }
        }
        return targetAlbum;
    }

//    public class GsonRequest<T> extends Request<T> {
//
//        private final Response.Listener<T> mListener;
//        private Gson mGson;
//        private Class<T> mClass;
//
//        public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
//            super(method, url, errorListener);
//            mGson = new Gson();
//            mClass = clazz;
//            mListener = listener;
//        }
//
//        public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
//            this(Method.GET, url, clazz, listener, errorListener);
//        }
//
//        @Override
//        protected Response<T> parseNetworkResponse(NetworkResponse response) {
//            String jsonString = null;
//            try {
//                jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                return Response.success(mGson.fromJson(jsonString, mClass), HttpHeaderParser.parseCacheHeaders(response));
//            } catch (UnsupportedEncodingException e) {
//                return Response.error(new ParseError(e));
//            }
//        }
//
//        @Override
//        protected void deliverResponse(T response) {
//            mListener.onResponse(response);
//        }
//    }

    public String selectPicturePath(Music song, int prefer) {
        String targetPicPathStr = "";
        switch (prefer) {
            case PREFER_ARTIST: {
                if (song.hasPicArtist()) {
                    targetPicPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
                } else if (song.hasPicAlbum()) {
                    targetPicPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator + song.getAlbum() + ".png";
                } else if (song.hasCover()) {
                    targetPicPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator + song.getAlbum() + ".png";
                }
                break;
            }
            case PREFER_ALBUM: {
                if (song.hasPicAlbum()) {
                    targetPicPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator + song.getAlbum() + ".png";
                } else if (song.hasCover()) {
                    targetPicPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator + song.getAlbum() + ".png";
                } else if (song.hasPicArtist()) {
                    targetPicPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
                }
                break;
            }
            case PREFER_BEST: {
                File file;
                long sizeArtist = 0;
                String artistPicPathStr = "";
                if (song.hasPicArtist()) {
                    artistPicPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
                    file = new File(artistPicPathStr);
                    sizeArtist = file.getTotalSpace();
                }
                long sizeAlbum = 0;
                String albumPicPathStr = "";
                if (song.hasPicAlbum()) {
                    albumPicPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator + song.getAlbum() + ".png";
                    file = new File(albumPicPathStr);
                    sizeAlbum = file.getTotalSpace();
                }
                String tempPathStr = sizeArtist > sizeAlbum ? artistPicPathStr : albumPicPathStr;
                long tempSize = sizeArtist > sizeAlbum ? sizeArtist : sizeAlbum;
                long sizeCover = 0;
                String coverPicPathStr = "";
                if (song.hasCover()) {
                    coverPicPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator + song.getAlbum() + ".png";
                    file = new File(coverPicPathStr);
                    sizeCover = file.getTotalSpace();
                }
                targetPicPathStr = sizeCover > tempSize ? coverPicPathStr : tempPathStr;
                break;
            }
        }
        return targetPicPathStr;
    }

    public Bitmap selectPicture(Music song, int prefer) throws FileNotFoundException {
        String picPathStr = "";
        Bitmap bitmap = null;
        File file;
        switch (prefer) {
            case PREFER_ARTIST: {
                if (song.hasPicArtist()) {
                    picPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
                    FileInputStream fileInputStream = new FileInputStream(picPathStr);
                    bitmap = BitmapFactory.decodeStream(fileInputStream);
                }
                if (bitmap == null) {
                    song.setHasPicArtist(false);
                    file = new File(picPathStr);
                    if (file.exists()) {
                        file.delete();
                    }
                    int sizeAlbum = 0;
                    Bitmap bitmapAlbum = null;
                    if (song.hasPicAlbum()) {
                        picPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator + song.getAlbum() + ".png";
                        FileInputStream fileInputStream = new FileInputStream(picPathStr);
                        bitmapAlbum = BitmapFactory.decodeStream(fileInputStream);
                        if (bitmapAlbum == null) {
                            song.setHasPicAlbum(false);
                            file = new File(picPathStr);
                            if (file.exists()) {
                                file.delete();
                            }
                        } else sizeAlbum = bitmapAlbum.getWidth() * bitmapAlbum.getHeight();
                    }
                    int sizeCover = 0;
                    Bitmap bitmapCover = null;
                    if (song.hasCover()) {
                        picPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator + song.getAlbum() + ".png";
                        FileInputStream fileInputStream = new FileInputStream(picPathStr);
                        bitmapCover = BitmapFactory.decodeStream(fileInputStream);
                        if (bitmapCover == null) {
                            song.setHasCover(false);
                            file = new File(picPathStr);
                            if (file.exists()) {
                                file.delete();
                            }
                        } else sizeCover = bitmapCover.getWidth() * bitmapCover.getHeight();
                    }
                    bitmap = sizeAlbum > sizeCover ? bitmapAlbum : bitmapCover;
                }
                break;
            }
            case PREFER_ALBUM: {
                if (song.hasPicAlbum() | song.hasCover()) {
                    long sizeAlbum = 0;
                    Bitmap bitmapAlbum = null;
                    if (song.hasPicAlbum()) {
                        picPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator + song.getAlbum() + ".png";
                        FileInputStream fileInputStream = new FileInputStream(picPathStr);
                        bitmapAlbum = BitmapFactory.decodeStream(fileInputStream);
                        if (bitmapAlbum == null) {
                            song.setHasPicAlbum(false);
                            file = new File(picPathStr);
                            if (file.exists()) {
                                file.delete();
                            }
                        } else sizeAlbum = bitmapAlbum.getWidth() * bitmapAlbum.getHeight();
                    }
                    long sizeCover = 0;
                    Bitmap bitmapCover = null;
                    if (song.hasCover()) {
                        picPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator + song.getAlbum() + ".png";
                        FileInputStream fileInputStream = new FileInputStream(picPathStr);
                        bitmapCover = BitmapFactory.decodeStream(fileInputStream);
                        if (bitmapCover == null) {
                            song.setHasCover(false);
                            file = new File(picPathStr);
                            if (file.exists()) {
                                file.delete();
                            }
                        } else sizeCover = bitmapCover.getWidth() * bitmapCover.getHeight();
                    }
                    bitmap = sizeAlbum > sizeCover ? bitmapAlbum : bitmapCover;
                }
                if (bitmap == null && song.hasPicArtist()) {
                    picPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
                    FileInputStream fileInputStream = new FileInputStream(picPathStr);
                    bitmap = BitmapFactory.decodeStream(fileInputStream);
                    if (bitmap == null) {
                        song.setHasPicArtist(false);
                        file = new File(picPathStr);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
                break;
            }
            case PREFER_BEST: {
                long sizeArtist = 0;
                Bitmap bitmapArtist = null;
                if (song.hasPicArtist()) {
                    picPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "artists" + File.separator + song.getArtist() + ".png";
                    FileInputStream fileInputStream = new FileInputStream(picPathStr);
                    bitmapArtist = BitmapFactory.decodeStream(fileInputStream);
                    if (bitmapArtist == null) {
                        song.setHasPicAlbum(false);
                        file = new File(picPathStr);
                        if (file.exists()) {
                            file.delete();
                        }
                    } else sizeArtist = bitmapArtist.getWidth() * bitmapArtist.getHeight();
                }
                long sizeAlbum = 0;
                Bitmap bitmapAlbum = null;
                if (song.hasPicAlbum()) {
                    picPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "albums" + File.separator + song.getAlbum() + ".png";
                    FileInputStream fileInputStream = new FileInputStream(picPathStr);
                    bitmapAlbum = BitmapFactory.decodeStream(fileInputStream);
                    if (bitmapAlbum == null) {
                        song.setHasPicAlbum(false);
                        file = new File(picPathStr);
                        if (file.exists()) {
                            file.delete();
                        }
                    } else sizeAlbum = bitmapAlbum.getWidth() * bitmapAlbum.getHeight();
                }
                Bitmap bitmapTemp = sizeArtist > sizeAlbum ? bitmapArtist : bitmapAlbum;
                long sizeTemp = 0;
                if (bitmapTemp != null) {
                    sizeTemp = bitmapTemp.getWidth() * bitmapTemp.getHeight();
                }
                long sizeCover = 0;
                Bitmap bitmapCover = null;
                if (song.hasCover()) {
                    picPathStr = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "covers" + File.separator + song.getAlbum() + ".png";
                    FileInputStream fileInputStream = new FileInputStream(picPathStr);
                    bitmapCover = BitmapFactory.decodeStream(fileInputStream);
                    if (bitmapCover == null) {
                        song.setHasCover(false);
                        file = new File(picPathStr);
                        if (file.exists()) {
                            file.delete();
                        }
                    } else sizeCover = bitmapCover.getWidth() * bitmapCover.getHeight();
                }
                bitmap = sizeCover > sizeTemp ? bitmapCover : bitmapTemp;
                break;
            }
        }
        return bitmap;
    }
}
