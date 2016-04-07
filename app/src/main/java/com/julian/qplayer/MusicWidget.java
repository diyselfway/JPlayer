package com.julian.qplayer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

/**
 * Created by Julian on 2015/12/29.
 */
public class MusicWidget extends AppWidgetProvider {

    public static final String CHANGE_STATE = "com.julian.musicplayer.CHANGE_STATE";
    public static final String  LAST_SONG= "com.julian.musicplayer.LAST_SONG";
    public static final String NEXT_SONG="com.julian.musicplayer.NEXT_SONG";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PlayerActivity.UPDATE_WIDGET)){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_music);
            byte[] coverBytes = intent.getByteArrayExtra("cover");
            if (coverBytes != null){
                remoteViews.setImageViewBitmap(R.id.image_widget_cover,BitmapFactory.decodeByteArray(coverBytes,0,coverBytes.length));
            }
            remoteViews.setTextViewText(R.id.text_widget_song_name,intent.getStringExtra("title"));
            if(intent.getBooleanExtra("isPlaying",false)){
                remoteViews.setImageViewResource(R.id.image_button_widget_play,R.drawable.pause);
            }else {
                remoteViews.setImageViewResource(R.id.image_button_widget_play,R.drawable.play);
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context,MusicWidget.class);
            appWidgetManager.updateAppWidget(componentName,remoteViews);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_music);
        int[] id = {R.id.image_button_widget_previous,R.id.image_button_widget_play,R.id.image_button_widget_next};
        String[] action = {LAST_SONG,CHANGE_STATE,NEXT_SONG};
        for(int i=0;i<=2;i++){
            Intent intent = new Intent();
            intent.setAction(action[i]);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,intent,0 );
            remoteViews.setOnClickPendingIntent(id[i],pendingIntent);
        }
        Intent intent = new Intent(context,PlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        remoteViews.setOnClickPendingIntent(R.id.image_widget_cover,pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.text_widget_song_name,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
