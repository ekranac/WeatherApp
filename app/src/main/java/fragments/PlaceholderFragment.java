package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ziga.weatherapp.R;
import com.google.gson.Gson;



public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_FIRST_NAME = "first_name";
    private static final String ARG_LAST_NAME = "last_name";

    public class Thing
    {
        public String firstName;
        public String lastName;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {

        // Generate array of json objects
        String[] JSONArray = {
                "{'id':1,'firstName':'Lokesh','lastName':'Gupta','roles':['ADMIN','MANAGER']}",
                "{'id':2,'firstName':'Ziga','lastName':'Besal','roles':['ADMIN','MANAGER']}"
        };

        Gson gson = new Gson();

        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_FIRST_NAME, gson.fromJson(JSONArray[sectionNumber], Thing.class).firstName);
        args.putString(ARG_LAST_NAME, gson.fromJson(JSONArray[sectionNumber], Thing.class).lastName);

        fragment.setArguments(args); // Where there is 'set', there is always 'get'!
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.section_label);

        int sectionNumber = getArguments().getInt("section_number");
        String firstName = getArguments().getString("first_name");
        String lastName = getArguments().getString("last_name");

        tv.setText("My name is " + firstName + " " + lastName + " and my number is " + Integer.toString(sectionNumber));

        return rootView;
    }
}