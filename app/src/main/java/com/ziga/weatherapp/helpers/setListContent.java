package com.ziga.weatherapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ziga.weatherapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ziga.weatherapp.adapters.ListViewAdapter;
import com.ziga.weatherapp.models.CityListItem;
import com.ziga.weatherapp.models.Weather;


public class setListContent extends AsyncTask<Void, Void, List<CityListItem>>
{
    ListView listView;
    OtherHelper helper;
    Context c;
    Boolean isFahrenheit;
    View rootView;

    ProgressBar bar;

    private static final String PREF_KEY_WOEIDS = "Woeids";
    private static final String PREF_KEY_CITIES = "Cities";

    public setListContent(ListView listView, View rootView, Context c)
    {
        super();
        this.listView = listView;
        this.rootView = rootView;
        this.c = c;
        this.helper = new OtherHelper(c);
        this.isFahrenheit = helper.getUnits();

        bar = (ProgressBar) rootView.findViewById(R.id.list_progress_bar);

    }

    @Override
    protected void onPreExecute()
    {
        if(bar!=null)
        {
            bar.bringToFront();
        }
    }

    @Override
    protected List<CityListItem> doInBackground(Void... params)
    {
        SharedPreferences prefs = helper.getMyPreferences();
        List<String> woeids = Arrays.asList(prefs.getString(PREF_KEY_WOEIDS, null).split("  "));
        List<String> cities = Arrays.asList(prefs.getString(PREF_KEY_CITIES, null).split("  "));

        List<CityListItem> list = new ArrayList<CityListItem>();

        for(int i = 0; i<woeids.size(); i++)
        {
            String woeid = woeids.get(i);
            String city = cities.get(i);
            String url = "";

            if(!isFahrenheit)
            {
                url = YahooClient.makeWeatherURL(woeid, "c");
            }
            else
            {
                url = YahooClient.makeWeatherURL(woeid, "f");
            }

            Weather weather = YahooClient.getWeatherData(url);
            CityListItem item = new CityListItem(city, weather.condition.getTemp() + weather.units.getTemperature(), weather.condition.getCode());

            list.add(item);
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<CityListItem> list)
    {
        if(bar!=null)
        {
            bar.setVisibility(View.INVISIBLE);
        }

        ListViewAdapter adapter = new ListViewAdapter(c, R.layout.list_city, list);
        if(listView!=null)
        {
            listView.setAdapter(adapter);
        }
    }
}