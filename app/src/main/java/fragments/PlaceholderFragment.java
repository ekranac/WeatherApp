package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ziga.weatherapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

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

        fragment.setArguments(args); // Where there is 'set', there is always 'get'!
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        int sectionNumber = getArguments().getInt("section_number");

        OtherHelper helper = new OtherHelper(getActivity().getBaseContext());


        if(sectionNumber==2)
        {
            Context c = getActivity().getBaseContext();
            LocationManager lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
            Location location = YahooClient.getLastKnownLocation(lm, c);
            String latitude = Double.toString(location.getLatitude());
            String longitude = Double.toString(location.getLongitude());

            new getCurrentLocationWoeid(c, latitude, longitude, rootView).execute();
        }
        else
        {
            String woeid = Integer.toString(sectionNumber);

            try {
                SharedPreferences prefs = helper.getMyPreferences();
                List<String> list = Arrays.asList(prefs.getString("Woeids", null).split(","));
                if (list.get(sectionNumber-2) != null) {
                    woeid = list.get(sectionNumber-2);
                }
            } catch(Throwable t) {}

            TextView tv_woeid = (TextView) rootView.findViewById(R.id.tv_woeid);
            tv_woeid.setText(woeid);
        }
        return rootView;
    }
}

class getCurrentLocationWoeid extends AsyncTask<String, Void, String>
{

    Context c;
    String lat;
    String lon;
    View rootView;

    public getCurrentLocationWoeid(Context c, String lat, String lon, View rootView)
    {
        super();
        this.c = c;
        this.lat = lat;
        this.lon = lon;
        this.rootView = rootView;
    }

    @Override
    protected String doInBackground(String... params)
    {

        String url = YahooClient.makeCurrentLocationURL(lat, lon);
        Log.i("THE URL", url);
        HttpURLConnection yahooHttpConn = null;

        String result = "";

        try {
            yahooHttpConn = (HttpURLConnection) (new URL(url)).openConnection();
            yahooHttpConn.connect();
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));

            int event = parser.getEventType();

            String tagName = null;
            String currentTag = null;
            while(event!=XmlPullParser.END_DOCUMENT)
            {
                tagName = parser.getName();

                if (event == XmlPullParser.START_TAG) {
                    currentTag = tagName;
                }
                else if(event == XmlPullParser.TEXT)
                {
                    if("woeid".equals(currentTag))
                    {
                        OtherHelper helper = new OtherHelper(c);
                        helper.addWoeidToSharedPreferences(parser.getText(), 0);

                        result = parser.getText();
                    }
                }
                event = parser.next();
            }
        }catch(Throwable t) {
            t.printStackTrace();
            // Log.e("Error in getCityList", t.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result)
    {
        TextView tv_woeid = (TextView) rootView.findViewById(R.id.tv_woeid);
        tv_woeid.setText(result);
    }
}