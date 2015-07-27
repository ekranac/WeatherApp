package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ziga.weatherapp.R;

import helpers.OtherHelper;

public class AboutActivity extends ActionBarActivity
{

    private String DEGREES_CELSIUS = "°C";
    private String DEGREES_FAHRENHEIT = "°F";
    private String PREF_UNITS_ARG = "isFahrenheit";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_about);

        OtherHelper helper = new OtherHelper(getBaseContext());
        Switch mSwitch = (Switch) findViewById(R.id.units_switch);
        final TextView tv_unit = (TextView) findViewById(R.id.tv_unit);

        TextView tv_credits = (TextView) findViewById(R.id.about_credits);
        tv_credits.setMovementMethod(LinkMovementMethod.getInstance());


        Boolean isFahrenheit = helper.getUnits();
        if(!isFahrenheit)
        {
            mSwitch.setChecked(false);
            tv_unit.setText(DEGREES_CELSIUS);
        }
        else
        {
            mSwitch.setChecked(true);
            tv_unit.setText(DEGREES_FAHRENHEIT);
        }

        final SharedPreferences prefs = helper.getMyPreferences();
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean(PREF_UNITS_ARG, isChecked).apply();
                if (isChecked) {
                    tv_unit.setText(DEGREES_FAHRENHEIT);
                } else {
                    tv_unit.setText(DEGREES_CELSIUS);
                }

                // Restart app to refresh units
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}
