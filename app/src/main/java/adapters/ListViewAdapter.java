package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        if(v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_city, null);
        }

        CityListItem item = list.get(position);
        String city = item.getCity();
        String currentTemp = item.getCurrentTemp();

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