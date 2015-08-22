package com.ziga.weatherapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ziga.weatherapp.R;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import com.ziga.weatherapp.helpers.OtherHelper;
import com.ziga.weatherapp.helpers.YahooClient;
import com.ziga.weatherapp.models.Weather;


public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PlaceholderFragment newInstance(int sectionNumber)
    {
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
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

        Context c = getActivity().getBaseContext();

        TextView tv_go2search = (TextView) rootView.findViewById(R.id.tv_go2search);
        tv_go2search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
                mViewPager.setCurrentItem(1, true);
            }
        });

        LinearLayout banner = (LinearLayout) rootView.findViewById(R.id.white_banner);
        Drawable background = banner.getBackground();
        background.setAlpha(80);

        new setWeather(c, rootView, sectionNumber).execute();
        return rootView;
    }
}


class setWeather extends AsyncTask<Void, Void, Weather>
{

    Context context;
    View rootView;
    Integer position;
    OtherHelper h;
    Boolean isFahrenheit;


    private static final String PREF_KEY_WOEIDS = "Woeids";

    private static final String UNIT_CELSIUS = "c";
    private static final String UNIT_FAHRENHEIT = "f";

    public setWeather(Context context, View rootView, Integer position)
    {
        super();
        this.context = context;
        this.rootView = rootView;
        this.position = position;
        this.h = new OtherHelper(context);
        this.isFahrenheit = h.getUnits();
    }

    @Override
    protected Weather doInBackground(Void... params)
    {
        SharedPreferences prefs = h.getMyPreferences();
        List<String> list = Arrays.asList(prefs.getString(PREF_KEY_WOEIDS, null).split("  "));

        String url="";
        String woeid="";
        Weather weather = new Weather();

        if(position>1)
        {
            woeid = list.get(position - 2);
            if(woeid.contains("[") || woeid.contains("]"))
            {
                woeid = woeid.replace("[", "").replace("]", "");
            }
            if(!isFahrenheit)
            {
                url = YahooClient.makeWeatherURL(woeid, UNIT_CELSIUS);
            }
            else
            {
                url = YahooClient.makeWeatherURL(woeid, UNIT_FAHRENHEIT);
            }
            weather = YahooClient.getWeatherData(url);
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
            TextView tv_hi_lo = (TextView) rootView.findViewById(R.id.tv_hi_lo);
            TextView tv_condition = (TextView) rootView.findViewById(R.id.tv_condition);

            TextView tv_forecast_d1 = (TextView) rootView.findViewById(R.id.tv_forecast_d1);
            TextView tv_forecast_d2 = (TextView) rootView.findViewById(R.id.tv_forecast_d2);
            TextView tv_forecast_d3 = (TextView) rootView.findViewById(R.id.tv_forecast_d3);

            ImageView weatherIcon = (ImageView) rootView.findViewById(R.id.weather_icon);
            ImageView forecastIconOne = (ImageView) rootView.findViewById(R.id.forecast_image_one);
            ImageView forecastIconTwo = (ImageView) rootView.findViewById(R.id.forecast_image_two);
            ImageView forecastIconThree = (ImageView) rootView.findViewById(R.id.forecast_image_three);


            if(position==2)
            {
                tv_city.setText(h.getCurrentCity());
            }
            else
            {
                tv_city.setText(weather.location.getCity());
            }


            tv_temp.setText(weather.condition.getTemp() + weather.units.getTemperature());
            tv_hi_lo.setText("H " + Arrays.asList(weather.forecast.getHigh().split("  ")).get(0) + "  L " + Arrays.asList(weather.forecast.getLow().split("  ")).get(0));
            tv_condition.setText(weather.condition.getText());

            List<String> forecast_day = Arrays.asList(weather.forecast.getDay().split("  "));
            tv_forecast_d1.setText(forecast_day.get(1));
            tv_forecast_d2.setText(forecast_day.get(2));
            tv_forecast_d3.setText(forecast_day.get(3));

            List<String> codes = Arrays.asList((weather.condition.getCode() + "  " + weather.forecast.getCode()).split("  "));
            String code;
            int resource;
            int color;
            for(int i = 0; i < 4; i++)
            {
                code = codes.get(i);

                switch(code)
                {
                    case "0":
                    case "2":
                        resource = R.mipmap.tornado;
                        color = context.getResources().getColor(R.color.tornado_gray);
                        break;

                    case "1":
                    case "3":
                    case "4":
                    case "37":
                    case "38":
                    case "39":
                    case "45":
                    case "47":
                        resource = R.mipmap.storm;
                        color =  context.getResources().getColor(R.color.storm_blue);

                        break;

                    case "5":
                    case "6":
                    case "7":
                    case "18":
                        resource = R.mipmap.rainsnow;
                        color = context.getResources().getColor(R.color.snow_blue);
                        break;

                    case "8":
                    case "9":
                    case "10":
                    case "11":
                    case "12":
                    case "40":
                        resource = R.mipmap.rain;
                        color = context.getResources().getColor(R.color.rain_blue);

                        break;

                    case "13":
                    case "14":
                    case "15":
                    case "16":
                    case "41":
                    case "42":
                    case "43":
                    case "46":
                        resource = R.mipmap.snow;
                        color = context.getResources().getColor(R.color.snow_blue);
                        break;

                    case "17":
                    case "35":
                        resource = R.mipmap.hail;
                        color = context.getResources().getColor(R.color.rain_blue);
                        break;

                    case "19":
                        resource = R.mipmap.dust;
                        color = context.getResources().getColor(R.color.dust_yellow);
                        break;

                    case "20":
                    case "21":
                    case "22":
                        resource = R.mipmap.fog;
                        color = context.getResources().getColor(R.color.fog_gray);
                        break;

                    case "23":
                    case "24":
                        resource = R.mipmap.windy;
                        color = context.getResources().getColor(R.color.wind_blue);
                        break;

                    case "25":
                        resource = R.mipmap.cold;
                        color = context.getResources().getColor(R.color.snow_blue);
                        break;

                    case "26":
                        resource = R.mipmap.cloudy;
                        color = context.getResources().getColor(R.color.cloud_blue);
                        break;

                    case "27":
                        resource = R.mipmap.mostly_cloudy_n;
                        color = context.getResources().getColor(R.color.night_blue);
                        break;

                    case "28":
                        resource = R.mipmap.mostly_cloudy_d;
                        color = context.getResources().getColor(R.color.cloud_blue);
                        break;

                    case "29":
                    case "33":
                        resource = R.mipmap.partly_cloudy_n;
                        color = context.getResources().getColor(R.color.night_blue);
                        break;

                    case "30":
                    case "34":
                    case "44":
                        resource = R.mipmap.partly_cloudy_d;
                        color = context.getResources().getColor(R.color.cloud_blue);
                        break;

                    case "31":
                        resource = R.mipmap.clear_n;
                        color = context.getResources().getColor(R.color.night_blue);
                        break;

                    case "32":
                    case "36":
                        resource = R.mipmap.sun;
                        color = context.getResources().getColor(R.color.clear_blue);
                        break;

                    default:
                        resource = R.mipmap.no_data;
                        color = context.getResources().getColor(R.color.wind_blue);
                        break;
                }

                switch(i)
                {
                    case 0:
                        weatherIcon.setImageResource(resource);
                        rootView.setBackgroundColor(color);
                        break;
                    case 1:
                        forecastIconOne.setImageResource(resource);
                        break;
                    case 2:
                        forecastIconTwo.setImageResource(resource);
                        break;
                    case 3:
                        forecastIconThree.setImageResource(resource);
                        break;
                }
            }


        }

        // rootView.setBackgroundColor(context.getResources().getColor(R.color.white));

    }
}