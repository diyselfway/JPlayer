package com.julian.qplayer.ArtistTab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

/**
 * Created by Julian on 2016/3/14.
 */
public class ArtistFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList;
    private String[] titles = new String[]{"作品","专辑","简介"};

    public ArtistFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public String getTitle(int position) {
        return titles[position];
    }
}
