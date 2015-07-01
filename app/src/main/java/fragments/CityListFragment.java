package fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.ziga.weatherapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.ListViewAdapter;
import helpers.OtherHelper;
import helpers.YahooClient;
import helpers.setListContent;
import models.CityListItem;
import models.Weather;


public class CityListFragment extends Fragment {

    BaseAdapter mAdapter;
    OtherHelper helper;

    public CityListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        helper = new OtherHelper(getActivity().getBaseContext());

        final ListView listView = (ListView) rootView.findViewById(R.id.city_listview);

        final SharedPreferences prefs = helper.getMyPreferences();
        /*try {
            List<String> cities = Arrays.asList(prefs.getString("Cities", null).split("  "));

            mAdapter = new ArrayAdapter<String>(
                    this.getActivity().getBaseContext(),
                    R.layout.list_city,
                    R.id.list_city_textview,
                    cities
            );

            listView.setAdapter(mAdapter);
        } catch(Throwable t) {}*/

        new setListContent(listView, helper, getActivity()).execute();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                if (position != 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Remove city")
                            .setMessage("Are you sure you want to remove this city?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    helper.removeCity(position, listView, getActivity());
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


