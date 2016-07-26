package mx.com.cubozsoft.sunshineapp.listaPrincipal;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import mx.com.cubozsoft.sunshineapp.Utility;
import mx.com.cubozsoft.sunshineapp.data.WeatherContract;
import mx.com.cubozsoft.sunshineapp.detalle.DetailActivity;
import mx.com.cubozsoft.sunshineapp.detalle.DetailFragment;
import mx.com.cubozsoft.sunshineapp.ForecastItem;
import mx.com.cubozsoft.sunshineapp.R;
import mx.com.cubozsoft.sunshineapp.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements ListOfWeather.OnFragmentInteractionListener{

    private String mLocation;
    private int mSavedItemSelected = 0;
    private String FORECASTDETAILFRAGMENT_TAG = "FDF";
    private boolean mTwoPane = false;
    private String SAVE_ITEM_KEY = "saveselecteditem";


    @Override
    protected void onResume() {
        super.onResume();
        String actualSettingLocation = Utility.getPreferredLocation(this);
        if(!mLocation.equals(actualSettingLocation))
        {
            //it has change, this code handles the forecast List
            ListOfWeather forecastf =(ListOfWeather) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_forecast);

            forecastf.onLocationChanged();


            //this handles the detail in case it is two pane
            if(mTwoPane)
            {
                DetailFragment detail = (DetailFragment) getSupportFragmentManager()
                        .findFragmentByTag(FORECASTDETAILFRAGMENT_TAG);

                detail.onLocationChanged(actualSettingLocation);

            }

            mLocation = actualSettingLocation;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVE_ITEM_KEY,mSavedItemSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocation = Utility.getPreferredLocation(this);

        if(findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) {
                FragmentManager fragMan = getSupportFragmentManager();
                fragMan.beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), FORECASTDETAILFRAGMENT_TAG)
                        .addToBackStack(null)
                        .commit();
            }
            else
            {
                ListOfWeather forecastf =(ListOfWeather) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_forecast);

                int savedSelection = savedInstanceState.getInt(SAVE_ITEM_KEY);
                if(mSavedItemSelected != savedSelection) {
                    forecastf.onItemSelected(savedSelection);
                }
            }

        }
        else
        {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        ListOfWeather list = (ListOfWeather) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
        list.setUseTodayLayout(!mTwoPane);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.generalmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int settingButtonId = R.id.action_settings;

        if(settingButtonId == item.getItemId())
        {
            Intent settingsInten = new Intent(this, SettingsActivity.class);
            startActivity(settingsInten);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ClickOnItemList(Cursor data,int pos) {
//        Toast.makeText(this, data.getForecast(),Toast.LENGTH_SHORT).show();
//        Intent detailIntent = new Intent(this,DetailActivity.class);
//        detailIntent.putExtra(DetailFragment.FORECAST_KEY,data);
//        startActivity(detailIntent);
        data.moveToPosition(pos);
        long date = data.getLong(ListOfWeather.COL_WEATHER_DATE);
        String setting = Utility.getPreferredLocation(this);

        Uri uriData = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(setting,date);

        if(mTwoPane)
        {
            mSavedItemSelected = pos;
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.weather_detail_container,
                            DetailFragment.newInstance(uriData),
                            FORECASTDETAILFRAGMENT_TAG)
                    .commit();
        }
        else
        {
            Intent detailIntent = new Intent(this,DetailActivity.class);
            detailIntent.setData(uriData);
            startActivity(detailIntent);
        }


    }
}
