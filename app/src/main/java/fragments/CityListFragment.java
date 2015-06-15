package fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ziga.weatherapp.R;

import java.util.Arrays;
import java.util.List;

import helpers.OtherHelper;


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

        SharedPreferences prefs = helper.getMyPreferences();
        try {
            List<String> woeids = Arrays.asList(prefs.getString("Woeids", null).split(","));
            mAdapter = new ArrayAdapter<String>(
                    this.getActivity().getBaseContext(),
                    R.layout.list_city,
                    R.id.list_city_textview,
                    woeids
            );

            ListView listView = (ListView) rootView.findViewById(R.id.city_listview);
            listView.setAdapter(mAdapter);
        } catch(Throwable t) {}
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        helper = new OtherHelper(getActivity().getBaseContext());

        SharedPreferences prefs = helper.getMyPreferences();
        try {
            List<String> woeids = Arrays.asList(prefs.getString("Woeids", null).split(","));

            mAdapter = new ArrayAdapter<String>(
                    this.getActivity().getBaseContext(),
                    R.layout.list_city,
                    R.id.list_city_textview,
                    woeids
            );

            ListView listView = (ListView) getActivity().findViewById(R.id.city_listview);
            listView.setAdapter(mAdapter);
        } catch(Throwable t) {}
    }

}