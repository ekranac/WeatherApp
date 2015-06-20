package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    public static PlaceholderFragment newInstance(int sectionNumber) {

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

        Context c = getActivity().getBaseContext();

        Log.i("POSITION", Integer.toString(sectionNumber));

        new getWeather(c, rootView, sectionNumber).execute();
        return rootView;
    }
}


class getWeather extends AsyncTask<Void, Void, String>
{

    Context c;
    View rootView;
    Integer position;

    public getWeather(Context c, View rootView, Integer position)
    {
        super();
        this.c = c;
        this.rootView = rootView;
        this.position = position;
    }

    @Override
    protected String doInBackground(Void... params)
    {
        OtherHelper h = new OtherHelper(c);

        if(position==2)
        {
            h.setCurrentLocationWoeid();
        }

        SharedPreferences prefs = h.getMyPreferences();
        List<String> list = Arrays.asList(prefs.getString("Woeids", null).split(","));

        // TODO
        // Create url, parse through it, get data, write it in a model
        String url="";
        if(position>1)
        {
            url = list.get(position-2);
        }


        return url;

    }

    @Override
    protected void onPostExecute(String url)
    {
        if(position>1)
        {
            TextView tv_woeid = (TextView) rootView.findViewById(R.id.tv_woeid);
            tv_woeid.setText(url);
        }

    }
}