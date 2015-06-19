package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.example.ziga.weatherapp.R;

import adapters.CityAdapter;
import helpers.OtherHelper;


public class SearchFragment extends Fragment {

    private static int MAX_PAGES = 12; // Including the list of all locations & search fragment
    CityAdapter adpt;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);


        LinearLayout mainLayout = (LinearLayout) rootView.findViewById(R.id.search_layout);
        mainLayout.requestFocus(); // Just so the clearFocus() on searchView works, because the method always sets focus back to the first focusable view in activity- to layout in this case


        final AutoCompleteTextView searchView = (AutoCompleteTextView) rootView.findViewById(R.id.city_search);
        adpt = new CityAdapter(this.getActivity(), null);
        searchView.setAdapter(adpt);

        final OtherHelper helper = new OtherHelper(getActivity().getBaseContext());


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
                }

            }
        });

        return rootView;
    }


}