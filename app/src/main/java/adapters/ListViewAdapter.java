package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ziga.weatherapp.R;

import java.util.List;

import models.CityListItem;


public class ListViewAdapter extends BaseAdapter
{
    Context context;
    int resource;
    List<CityListItem> list;

    public ListViewAdapter(Context context, int resource, List<CityListItem> list)
    {
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        int color;

        if(v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_city, null);
        }

        CityListItem item = list.get(position);
        String city = item.getCity();
        String currentTemp = item.getCurrentTemp();
        String code = item.getCode();

        switch(code)
        {
            case "0":
            case "2":
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
                color =  context.getResources().getColor(R.color.storm_blue);

                break;

            case "5":
            case "6":
            case "7":
            case "18":
                color = context.getResources().getColor(R.color.snow_blue);
                break;

            case "8":
            case "9":
            case "10":
            case "11":
            case "12":
            case "40":
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
                color = context.getResources().getColor(R.color.snow_blue);
                break;

            case "17":
            case "35":
                color = context.getResources().getColor(R.color.rain_blue);
                break;

            case "19":
                color = context.getResources().getColor(R.color.dust_yellow);
                break;

            case "20":
            case "21":
            case "22":
                color = context.getResources().getColor(R.color.fog_gray);
                break;

            case "23":
            case "24":
                color = context.getResources().getColor(R.color.wind_blue);
                break;

            case "25":
                color = context.getResources().getColor(R.color.snow_blue);
                break;

            case "26":
                color = context.getResources().getColor(R.color.cloud_blue);
                break;

            case "27":
                color = context.getResources().getColor(R.color.night_blue);
                break;

            case "28":
                color = context.getResources().getColor(R.color.cloud_blue);
                break;

            case "29":
            case "33":
                color = context.getResources().getColor(R.color.night_blue);
                break;

            case "30":
            case "34":
            case "44":
                color = context.getResources().getColor(R.color.cloud_blue);
                break;

            case "31":
                color = context.getResources().getColor(R.color.night_blue);
                break;

            case "32":
            case "36":
                color = context.getResources().getColor(R.color.clear_blue);
                break;

            default:
                color = context.getResources().getColor(R.color.wind_blue);
                break;
        }

        v.setBackgroundColor(color);

        TextView tv_city = (TextView) v.findViewById(R.id.list_city_textview);
        TextView tv_temp = (TextView) v.findViewById(R.id.list_temp_textview);

        tv_city.setText(city);
        tv_temp.setText(currentTemp);


        return v;
    }

    @Override
    public CityListItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}