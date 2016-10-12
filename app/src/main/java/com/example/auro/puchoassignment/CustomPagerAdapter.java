package com.example.auro.puchoassignment;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created on 12/10/16.
 * @author Arijit Banerjee
 */

class CustomPagerAdapter extends PagerAdapter {

    private String[] mTabNames;
    private LayoutInflater mLayoutInflater;

    CustomPagerAdapter(Context context) {
        if (context != null) {
            mTabNames = new String[]{
                    context.getString(R.string.sign_in),
                    context.getString(R.string.sign_up)};

            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view;
        switch (position) {
            case 0:
                view = mLayoutInflater.inflate(R.layout.layout_sign_in,container,false);
                container.addView(view);
                return view;
            case 1:
                view = mLayoutInflater.inflate(R.layout.layout_sign_up, container, false);
                container.addView(view);
                return view;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNames[position];
    }


    @Override
    public int getCount() {
        return mTabNames.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
