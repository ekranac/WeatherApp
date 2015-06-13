package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ziga.weatherapp.R;

import java.util.Arrays;
import java.util.List;

import activities.MainActivity;
import helpers.OtherHelper;
import helpers.YahooClient;


public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LONGITUDE = "current_longitude";
    private static final String LATITUDE = "current_latitude";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, Context c) {

        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        if(sectionNumber==2)
        {
            try {
                LocationManager lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
                // Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location = YahooClient.getLastKnownLocation(lm, c);
                String latitude = Double.toString(location.getLatitude());
                String longitude = Double.toString(location.getLongitude());

                YahooClient.getCurrentLocationWoeid(latitude, longitude, c);
            } catch(Throwable t) {}
        }
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
        OtherHelper helper = new OtherHelper(getActivity().getBaseContext());

        int sectionNumber = getArguments().getInt("section_number");
        String woeid = Integer.toString(sectionNumber);

        try {
            SharedPreferences prefs = helper.getMyPreferences();
            List<String> list = Arrays.asList(prefs.getString("Woeids", null).split(","));
            if (list.get(sectionNumber-2) != null) {
                woeid = list.get(sectionNumber-2);
            }
        } catch(Throwable t) {}

        tv.setText(woeid);

        return rootView;
    }
}