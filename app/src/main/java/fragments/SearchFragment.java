package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.ziga.weatherapp.R;

import helpers.JSONHelper;


public class SearchFragment extends Fragment {

    String[] list = {"Albania","Kosovo","Slovenia"};

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ArrayAdapter<String> adapter;

        AutoCompleteTextView view = (AutoCompleteTextView) rootView.findViewById(R.id.city_search);

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_detail, list);
        view.setAdapter(adapter);

        return rootView;
    }
}