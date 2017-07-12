package com.my.android.library;


import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Administrator on 2017/1/24 0024.
 * ViewPager适配器
 */
public class MyViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> view;

    public MyViewPagerAdapter(ArrayList<View> view) {
        this.view = view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(view.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public int getCount() {
        if (view != null) {
            return view.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(view.get(arg1), 0);
        return view.get(arg1);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

}
