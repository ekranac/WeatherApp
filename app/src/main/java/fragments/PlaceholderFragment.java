package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ziga.weatherapp.R;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import helpers.OtherHelper;
import helpers.YahooClient;
import models.Weather;


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

        new getWeather(c, rootView, sectionNumber).execute();
        return rootView;
    }
}


class getWeather extends AsyncTask<Void, Void, Weather>
{

    Context c;
    View rootView;
    Integer position;
    OtherHelper h;
    Boolean isFahrenheit;

    public getWeather(Context c, View rootView, Integer position)
    {
        super();
        this.c = c;
        this.rootView = rootView;
        this.position = position;
        this.h = new OtherHelper(c);
        this.isFahrenheit = h.getUnits();
    }

    @Override
    protected Weather doInBackground(Void... params)
    {
        if(position==2)
        {
            h.setCurrentLocationData();
        }

        SharedPreferences prefs = h.getMyPreferences();
        List<String> list = Arrays.asList(prefs.getString("Woeids", null).split("  "));

        String url="";
        String woeid="";
        Weather weather = new Weather();
        HttpURLConnection yahooHttpConn = null;

        if(position>1)
        {
            woeid = list.get(position - 2);
            if(!isFahrenheit)
            {
                url = YahooClient.makeWeatherURL(woeid, "c");
            }
            else
            {
                url = YahooClient.makeWeatherURL(woeid, "f");
            }
            weather = YahooClient.getWeatherData(url, yahooHttpConn);
        }

        return weather;

    }

    @Override
    protected void onPostExecute(Weather weather)
    {

        ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        bar.setVisibility(View.INVISIBLE);

        if(position>1)
        {
            TextView tv_city = (TextView) rootView.findViewById(R.id.tv_city);
            TextView tv_temp = (TextView) rootView.findViewById(R.id.tv_current_temp);
            TextView tv_condition = (TextView) rootView.findViewById(R.id.tv_condition);

            TextView tv_forecast_d1 = (TextView) rootView.findViewById(R.id.tv_forecast_d1);
            TextView tv_forecast_d2 = (TextView) rootView.findViewById(R.id.tv_forecast_d2);
            TextView tv_forecast_d3 = (TextView) rootView.findViewById(R.id.tv_forecast_d3);

            if(position==2)
            {
                tv_city.setText(h.getCurrentCity());
            }
            else
            {
                tv_city.setText(weather.location.getCity());
            }
            tv_temp.setText(weather.condition.getTemp() + weather.units.getTemperature());
            tv_condition.setText(weather.condition.getText());

            List<String> forecast_day = Arrays.asList(weather.forecast.getDay().split("  "));
            tv_forecast_d1.setText(forecast_day.get(1));
            tv_forecast_d2.setText(forecast_day.get(2));
            tv_forecast_d3.setText(forecast_day.get(3));


        }

    }
}