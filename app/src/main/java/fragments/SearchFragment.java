package fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ziga.weatherapp.R;
import com.nvanbenschoten.motion.ParallaxImageView;

import activities.AboutActivity;
import adapters.CityAdapter;
import helpers.OtherHelper;
import helpers.setListContent;


public class SearchFragment extends Fragment {

    private static int MAX_PAGES = 12; // Including the list of all locations & search fragment
    CityAdapter adpt;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ParallaxImageView globe = (ParallaxImageView) rootView.findViewById(R.id.globe_icon);
        globe.registerSensorManager();

        FrameLayout mainLayout = (FrameLayout) rootView.findViewById(R.id.search_layout);
        mainLayout.requestFocus(); // Just so the clearFocus() on searchView works, because the method always sets focus back to the initially focused view in activity- to layout in this case
        final OtherHelper helper = new OtherHelper(getActivity().getBaseContext());

        // Set adapter to searchView
        final AutoCompleteTextView searchView = (AutoCompleteTextView) rootView.findViewById(R.id.city_search);
        adpt = new CityAdapter(this.getActivity(), null);
        searchView.setAdapter(adpt);

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Hide keyboard on select
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                searchView.setText("");
                searchView.clearFocus();


                // Get woeid, save to SharedPreferences
                if (helper.getCityCount() < MAX_PAGES)
                {
                    helper.addWoeidToSharedPreferences(adpt.getItem(position).getWoeid(), null);
                    ViewPager vp = (ViewPager) getActivity().findViewById(R.id.pager);
                    vp.getAdapter().notifyDataSetChanged();

                    String city = parent.getItemAtPosition(position).toString();
                    String two = view.toString();
                    Log.i("CITY", city);
                    helper.addCityToSharedPreferences(city, null, true);


                    ListView listView = (ListView) getActivity().findViewById(R.id.city_listview);
                    new setListContent(listView, helper, getActivity().getBaseContext()).execute(); // Refresh list
                }

            }
        });


        final Button btn_clearSearch = (Button) rootView.findViewById(R.id.btn_clear_search);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(searchView.length()==0)
                {
                    btn_clearSearch.setVisibility(View.INVISIBLE);
                }
                else
                {
                    btn_clearSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setText("");
            }
        });

        RelativeLayout about_btn = (RelativeLayout) rootView.findViewById(R.id.about_btn);
        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout banner = (RelativeLayout) rootView.findViewById(R.id.about_btn);
        Drawable background = banner.getBackground();
        background.setAlpha(80);

        return rootView;
    }

}