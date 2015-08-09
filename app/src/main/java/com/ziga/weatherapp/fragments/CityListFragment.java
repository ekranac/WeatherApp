package com.ziga.weatherapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ziga.weatherapp.R;

import com.ziga.weatherapp.helpers.OtherHelper;
import com.ziga.weatherapp.helpers.setListContent;


public class CityListFragment extends Fragment {

    OtherHelper helper;

    public CityListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        helper = new OtherHelper(getActivity().getBaseContext());

        final ListView listView = (ListView) rootView.findViewById(R.id.city_listview);
        new setListContent(listView, rootView, getActivity()).execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
                mViewPager.setCurrentItem(position + 2, true);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                if (position != 0)
                {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Remove city")
                            .setMessage("Are you sure you want to remove this city?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    helper.removeCity(position, rootView, listView, getActivity());
                                }
                            })

                            .setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })

                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                }

                return true;
            }
        });

        return rootView;
    }
}


