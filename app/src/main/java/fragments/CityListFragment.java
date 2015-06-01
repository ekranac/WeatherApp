package fragments;

import android.content.Context;
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


public class CityListFragment extends Fragment {


    public CityListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        SharedPreferences prefs = getActivity().getBaseContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        List<String> woeids = Arrays.asList(prefs.getString("Woeids", null).split(","));

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                this.getActivity().getBaseContext(),
                R.layout.list_city,
                R.id.list_city_textview,
                woeids
        );

        ListView listView = (ListView) rootView.findViewById(R.id.city_listview);
        listView.setAdapter(mAdapter);

        return rootView;
    }
}