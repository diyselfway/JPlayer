package com.julian.qplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Julian on 2015/12/10.
 */
public class PlayButton extends View {

    private int mBackgroundColor;
    private int mForegroundColor;
    private Paint mPaint;
    private boolean mIsPlaying;

    public PlayButton(Context context) {
        this(context, null);
    }

    public PlayButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundColor = getResources().getColor(R.color.colorAccent);
        mForegroundColor = Color.WHITE;
        mPaint = new Paint();
        mIsPlaying = false;
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void changeState(){
        mIsPlaying = !mIsPlaying;
        invalidate();
    }

    public void setIsPlaying(boolean isPlaying) {
        mIsPlaying = isPlaying;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆形背景
        mPaint.setColor(mBackgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = Math.min(cx, cy);
        canvas.drawCircle(cx, cy, radius, mPaint);
        int sideLength = (int)(radius*0.75);

        if (mIsPlaying){
            //画双矩形
            mPaint.setColor(mForegroundColor);
            canvas.drawRect(cx - sideLength / 2, cy - sideLength / 2, cx + sideLength / 2, cy + sideLength / 2, mPaint);
            mPaint.setColor(mBackgroundColor);
            canvas.drawRect(cx - sideLength/9, cy - sideLength/2, cx + sideLength/9, cy + sideLength/2,mPaint);
        }else {
            //画前面的三角形
            mPaint.setColor(mForegroundColor);
            Path path = new Path();
            path.moveTo((float)(cx - sideLength / 6* Math.sqrt(3)), cy - sideLength / 2);
            path.lineTo((float)(cx + sideLength / 3* Math.sqrt(3)), cy);
            path.lineTo((float)(cx - sideLength / 6* Math.sqrt(3)), cy + sideLength / 2);
            path.close();
            canvas.drawPath(path, mPaint);
        }

    }
}
