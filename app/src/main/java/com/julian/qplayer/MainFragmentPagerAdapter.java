package com.julian.qplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/3.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private  ArrayList<Fragment> mFragmentList;
    private String[] titles = new String[]{"歌曲","歌手","专辑"};

    public MainFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        mFragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);

    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public String getTitle(int position){
        return titles[position];
    }
}
