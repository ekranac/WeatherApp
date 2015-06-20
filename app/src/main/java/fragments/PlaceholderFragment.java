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

        Log.i("POSITION", Integer.toString(sectionNumber));

        new getWeather(c, rootView, sectionNumber).execute();
        return rootView;
    }
}


class getWeather extends AsyncTask<Void, Void, Weather>
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
    protected Weather doInBackground(Void... params)
    {
        OtherHelper h = new OtherHelper(c);

        if(position==2)
        {
            h.setCurrentLocationWoeid();
        }

        SharedPreferences prefs = h.getMyPreferences();
        List<String> list = Arrays.asList(prefs.getString("Woeids", null).split(","));

        String url="";
        String woeid="";
        Weather weather = new Weather();
        HttpURLConnection yahooHttpConn = null;
        if(position>1)
        {
            try {
                woeid = list.get(position-2);
                url = YahooClient.makeWeatherURL(woeid, "c");

                yahooHttpConn = (HttpURLConnection) (new URL(url)).openConnection();
                yahooHttpConn.connect();
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));

                int event = parser.getEventType();

                String tagName = null;
                while(event!=XmlPullParser.END_DOCUMENT) // Loop through XML data
                {
                    tagName = parser.getName();

                    if (event == XmlPullParser.START_TAG) {
                        if(tagName.equals("yweather:wind"))
                        {
                            weather.wind.setSpeed(parser.getAttributeValue(null, "speed"));
                        }

                        else if(tagName.equals("yweather:atmosphere"))
                        {
                            weather.atmosphere.setHumidity(parser.getAttributeValue(null, "humidity"));
                            weather.atmosphere.setPressure(parser.getAttributeValue(null, "pressure"));
                            weather.atmosphere.setVisibility(parser.getAttributeValue(null, "visibility"));
                        }

                        else if (tagName.equals("yweather:condition")) {
                            weather.condition.setTemp(parser.getAttributeValue(null, "temp"));
                            weather.condition.setText(parser.getAttributeValue(null, "text"));
                        }

                        else if (tagName.equals("yweather:units")) {
                            weather.units.setPressure(parser.getAttributeValue(null, "pressure"));
                            weather.units.setDistance(parser.getAttributeValue(null, "distance"));
                            weather.units.setSpeed(parser.getAttributeValue(null, "speed"));
                            weather.units.setTemperature("°" + parser.getAttributeValue(null, "temperature"));
                        }

                        else if (tagName.equals("yweather:location")) {
                            weather.location.setCity(parser.getAttributeValue(null, "city"));
                            weather.location.setCountry(parser.getAttributeValue(null, "country"));
                            weather.location.setRegion(parser.getAttributeValue(null, "region"));
                        }

                        else if (tagName.equals("yweather:forecast")) {
                            weather.forecast.addUpForecast();

                            if(weather.forecast.getForecastCount() < 5)
                            {
                                weather.forecast.setDay(weather.forecast.getDay() + parser.getAttributeValue(null, "day") + ",");
                                weather.forecast.setDate(weather.forecast.getDate() + parser.getAttributeValue(null, "date") + ",");
                                weather.forecast.setLow(weather.forecast.getLow() + parser.getAttributeValue(null, "low") + ",");
                                weather.forecast.setHigh(weather.forecast.getHigh() + parser.getAttributeValue(null, "high") + ",");
                                weather.forecast.setText(weather.forecast.getText() + parser.getAttributeValue(null, "text") + ",");
                            }
                            else if(weather.forecast.getForecastCount() == 5)
                            {
                                weather.forecast.setDay(weather.forecast.getDay() + parser.getAttributeValue(null, "day"));
                                weather.forecast.setDate(weather.forecast.getDate() + parser.getAttributeValue(null, "date"));
                                weather.forecast.setLow(weather.forecast.getLow() + parser.getAttributeValue(null, "low"));
                                weather.forecast.setHigh(weather.forecast.getHigh() + parser.getAttributeValue(null, "high"));
                                weather.forecast.setText(weather.forecast.getText() + parser.getAttributeValue(null, "text"));
                            }
                        }

                    }
                    event = parser.next();
                }
            }catch(Throwable t) {
                t.printStackTrace();
            }
        }


        return weather;

    }

    @Override
    protected void onPostExecute(Weather weather)
    {
        if(position>1)
        {
            TextView tv_city = (TextView) rootView.findViewById(R.id.tv_city);
            TextView tv_temp = (TextView) rootView.findViewById(R.id.tv_current_temp);
            TextView tv_condition = (TextView) rootView.findViewById(R.id.tv_condition);

            tv_city.setText(weather.location.getCity());
            tv_temp.setText(weather.condition.getTemp() + weather.units.getTemperature());
            tv_condition.setText(weather.forecast.getHigh());

        }

    }
}