package com.jenjenfuture.projectuas_2201750761;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.id_main_activity_tab_layout);
        viewPager = findViewById(R.id.id_main_activity_view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this.getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment frag = viewPagerAdapter.fragmentList[position];
                if (frag != null && frag instanceof SavedMovieFragment) {
                    ((SavedMovieFragment)frag).onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }
}