package com.julian.qplayer;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Julian on 2016/3/18.
 */
public class Utils {
    public static Palette.Swatch getSwatch(Bitmap bitmap) {
        Palette.Swatch targetSwatch;
        Palette palette = Palette.from(bitmap).generate();
        if (palette.getVibrantSwatch()!=null){
            targetSwatch = palette.getVibrantSwatch();
        }else if (palette.getMutedSwatch()!=null){
            targetSwatch = palette.getMutedSwatch();
        }else {
            ArrayList<Palette.Swatch> swatches = new ArrayList<>();
            for (Palette.Swatch swatch : palette.getSwatches()){
                swatches.add(swatch);
            }
            Collections.sort(swatches, new Comparator<Palette.Swatch>() {
                @Override
                public int compare(Palette.Swatch lhs, Palette.Swatch rhs) {
                    return lhs.getPopulation() - rhs.getPopulation();
                }
            });
            targetSwatch = swatches.get(0);
        }
        return targetSwatch;
    }
}
