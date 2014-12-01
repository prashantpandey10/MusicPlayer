package com.example.prashantpandey.prashant_app;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Prashant.Pandey on 2014-09-17.
 */
public class Tabs extends FragmentStatePagerAdapter {

    public Tabs(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
               return new Local();
            case 1:
                // Games fragment activity
                return new Online();
            case 2:
                // Movies fragment activity
                return new JSON();
        }

        return null;
    }

    @Override
    public int getCount() {

        // get item count - equal to number of tabs
        return 3;
    }

}