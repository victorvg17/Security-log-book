package com.example.victo.logbook;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LogBookPagerAdapter extends FragmentPagerAdapter {
    private static int NUMBER_OF_PAGES = 3;
    private VisitorsEntryFragment mVisitorsEntryFragment;
    private VisitorsListFragment mVisitorsListFragment;
    private VisitorsSummaryFragment mDaySummaryFragment;

    public LogBookPagerAdapter(FragmentManager fm){
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (mVisitorsEntryFragment == null){
                    mVisitorsEntryFragment = new VisitorsEntryFragment();
                }
                return mVisitorsEntryFragment;
            case 1:
                if (mVisitorsListFragment == null){
                    mVisitorsListFragment = new VisitorsListFragment();
                }
                return mVisitorsListFragment;
            case 2:
                if (mDaySummaryFragment == null){
                    mDaySummaryFragment = new VisitorsSummaryFragment();
                }
                return mDaySummaryFragment;
            default:
                if (mVisitorsEntryFragment == null){
                    mVisitorsEntryFragment = new VisitorsEntryFragment();
                }
                return mVisitorsEntryFragment;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_PAGES;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "VisitorsEntry";
            case 1:
                return "VisitorsList";
            case 2:
                return "DaySummary";
            default:
                return super.getPageTitle(position);
        }
    }
}
