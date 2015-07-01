package helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ziga.weatherapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.ListViewAdapter;
import models.CityListItem;
import models.Weather;


public class setListContent extends AsyncTask<Void, Void, List<CityListItem>>
{
    ListView listView;
    OtherHelper helper;
    Context c;
    Boolean isFahrenheit;

    public setListContent(ListView listView, OtherHelper helper, Context c)
    {
        super();
        this.listView = listView;
        this.helper = helper;
        this.c = c;
        this.isFahrenheit = helper.getUnits();

    }

    @Override
    protected List<CityListItem> doInBackground(Void... params)
    {
        SharedPreferences prefs = helper.getMyPreferences();
        List<String> woeids = Arrays.asList(prefs.getString("Woeids", null).split("  "));
        List<String> cities = Arrays.asList(prefs.getString("Cities", null).split("  "));

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

            Weather weather = YahooClient.getWeatherData(url, null);
            CityListItem item = new CityListItem(city, weather.condition.getTemp() + weather.units.getTemperature());

            list.add(item);
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<CityListItem> list)
    {
        ListViewAdapter adapter = new ListViewAdapter(c, R.layout.list_city, list);
        listView.setAdapter(adapter);
    }
}