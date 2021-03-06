package com.ziga.weatherapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ziga.weatherapp.fragments.CityListFragment;
import com.ziga.weatherapp.fragments.PlaceholderFragment;
import com.ziga.weatherapp.fragments.SearchFragment;
import com.ziga.weatherapp.helpers.OtherHelper;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new CityListFragment();
            case 1:
                return new SearchFragment();
            default:
                return PlaceholderFragment.newInstance(position);

        }
    }

    @Override
    public int getCount() {
        OtherHelper helper = new OtherHelper(context);
        return helper.getCityCount();

    }
}
