package com.sensoapps.asenousy.thinkmargin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: return PriceFragment.newInstance();
            case 1: return MarginFragment.newInstance();
            default: return CostFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Price";
            case 1: return "Margin";
            default: return "Cost";
        }
    }
}