package com.jenjenfuture.projectuas_2201750761;

import android.content.Context;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    String fragmentTitleList[] = new String[] { "Search Movie", "Saved Movie"};

    public Fragment[] fragmentList = new Fragment[fragmentTitleList.length];
    private Context context;

    public ViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SearchMovieFragment();
            case 1:
                return new SavedMovieFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragmentTitleList.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        fragmentList[position]  = createdFragment;
        return createdFragment;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList[position];
    }

}
