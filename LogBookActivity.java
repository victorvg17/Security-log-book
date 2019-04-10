package com.example.victo.logbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class LogBookActivity extends AppCompatActivity{

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private LogBookPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logbook_activity);
        Toast.makeText(getApplicationContext(), "Hi, you are in LogBook activity layout", Toast.LENGTH_SHORT);

        mTabLayout = findViewById(R.id.main_tab_layout);
        mViewPager = findViewById(R.id.main_view_pager);
        mTabLayout.setupWithViewPager(mViewPager);
        mPagerAdapter = new LogBookPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            int currPosition = 0;
            int currPosition = 1;
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int newPosition) {
                FragmentLifeCycle fragmentToShow = (FragmentLifeCycle) mPagerAdapter.getItem(newPosition);
                fragmentToShow.onDisplay();
                FragmentLifeCycle fragmentToHide = (FragmentLifeCycle) mPagerAdapter.getItem(currPosition);
                fragmentToHide.onBackground();

                currPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


}
