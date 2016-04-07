package com.julian.qplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Julian on 2016/3/5.
 */
public class Tools {
    //申请权限
    public static void requestPermission(Context context, String permission, Activity requestActivity, int requestCoed) {
        if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
//                if (shouldShowRequestPermissionRationale(
//                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    // Explain to the user why we need to read the contacts
//                }

            ActivityCompat.requestPermissions(requestActivity, new String[]{permission},
                    requestCoed);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant

            return;
        }
    }

    //取得当前图片某一高度的平均RGB值
    //ratio_height 取点的垂向位置，0为顶部，1为底部
    //start_width 取点的横向起点，0为左边界，1为右边界
    //end_width 取点的横向终点，0为左边界，1为右边界

    public static int[] getAverageColor(Bitmap bitmap,double ratio_height,double start_width,double end_width ) {
        int[] avgColor = new int[3];
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i =  (int)(width*start_width); i < (int)(width*end_width); i++) {
            int color = bitmap.getPixel(i, (int)(height*ratio_height));
            r = r + Color.red(color);
            g = g + Color.green(color);
            b = b + Color.blue(color);
            avgColor = new int[]{r*2/width, g*2/width, b*2/width};
        }
        return avgColor;
    }

    public static String randomColor(){
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#303F9F");
        colors.add("#2196F3");
        colors.add("#FF5722");
        colors.add("#607D8B");
        colors.add("#9C27B0");
        colors.add("#8BC34A");
        colors.add("#FFC107");
        colors.add("#E91E63");
        colors.add("#455A64");
        colors.add("#9C27B0");
        Random random = new Random();
        int lucky =random.nextInt(10);
        return colors.get(lucky);
    }
}
