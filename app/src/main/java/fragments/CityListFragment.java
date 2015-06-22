package fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ziga.weatherapp.R;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import activities.MainActivity;
import helpers.OtherHelper;
import helpers.YahooClient;
import models.Weather;


public class CityListFragment extends Fragment {

    ArrayAdapter<String> mAdapter;
    OtherHelper helper;

    public CityListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        helper = new OtherHelper(getActivity().getBaseContext());

        ListView listView = (ListView) rootView.findViewById(R.id.city_listview);

        SharedPreferences prefs = helper.getMyPreferences();
        try {
            List<String> cities = Arrays.asList(prefs.getString("Cities", null).split(","));

            mAdapter = new ArrayAdapter<String>(
                    this.getActivity().getBaseContext(),
                    R.layout.list_city,
                    R.id.list_city_textview,
                    cities
            );

            listView.setAdapter(mAdapter);
        } catch(Throwable t) {}

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Remove city")
                        .setMessage("Are you sure you want to remove this city?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity().getBaseContext(), "Removed :)", Toast.LENGTH_SHORT).show();
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

                return false;
            }
        });

        return rootView;
    }
}

