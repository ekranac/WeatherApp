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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.ziga.weatherapp.R;

import activities.MainActivity;


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

        if(sectionNumber==1)
        {

            LocationManager lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
            Location location  = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            args.putDouble(LONGITUDE, longitude);
            args.putDouble(LATITUDE, latitude);
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

        // TODO if is keyboard is active/ visible---> do that

        int sectionNumber = getArguments().getInt("section_number");
        tv.setText(Integer.toString(sectionNumber));

        if(sectionNumber==1)
        {
            Log.i("Latitude::Longitude", Double.toString(getArguments().getDouble("current_latitude")) + "::" + Double.toString(getArguments().getDouble("current_longitude")));
        }
        return rootView;
    }
}