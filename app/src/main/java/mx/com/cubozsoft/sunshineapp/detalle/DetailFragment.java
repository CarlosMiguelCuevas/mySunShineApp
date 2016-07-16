package mx.com.cubozsoft.sunshineapp.detalle;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mx.com.cubozsoft.sunshineapp.ForecastItem;
import mx.com.cubozsoft.sunshineapp.R;
import mx.com.cubozsoft.sunshineapp.Utility;
import mx.com.cubozsoft.sunshineapp.data.WeatherContract;
import mx.com.cubozsoft.sunshineapp.data.WeatherProvider;
import mx.com.cubozsoft.sunshineapp.listaPrincipal.ListOfWeather;


public class DetailFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_WEATHER_HUMIDITY = 5;
    public static final int COL_WEATHER_WIND = 6;
    public static final int COL_WEATHER_PRESSURE = 7;
    public static final int COL_WEATHER_DEGREES = 8;
    public static final int COL_WEATHER_CONDITION_ID = 9;

    TextView mDetailForecast;
    TextView mDetailMax;
    TextView mDetailMin;
    TextView mDetailDateDay;
    TextView mDetailDateMonth;
    TextView mDetailHumidity;
    TextView mDetailWind;
    TextView mDetailPressure;
    ImageView mImage;



    private final static int ID_LOADER = 0;
    private final static String URI_GETTER = "geturi";
    public static final String FORECAST_KEY = "forecastKey";

    private String mForecast;
    private Uri mUriData = null;
    private ShareActionProvider mShareActionProvider;



    public DetailFragment() {
        // Required empty public constructor
    }

    public void onLocationChanged(String location) {

        long date = WeatherContract.WeatherEntry.getDateFromUri(mUriData);
        Uri newUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(location,date);
        mUriData = newUri;
        getLoaderManager().restartLoader(ID_LOADER,null,this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_LOADER,null,this);
    }

    public static DetailFragment newInstance(Uri data) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(URI_GETTER,data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detal_menu,menu);

        //get a referenc to the item in the menu
        MenuItem shareItem = menu.findItem(R.id.action_share);
        //we get teh action provider usinfg the menu
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

       if(mForecast != null)
       {
           //we set the intent to the shareactionprovider
           mShareActionProvider.setShareIntent(createShareForcastItem());
       }


        super.onCreateOptionsMenu(menu, inflater);
    }

    private Intent createShareForcastItem()
    {
        //we craete the intent with the info to share
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT,mForecast.toString());

        return myShareIntent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if(args != null)
        {
            mUriData = args.getParcelable(URI_GETTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mDetailForecast = (TextView) rootView.findViewById(R.id.detail_forecast_textview);
        mDetailMax = (TextView)rootView.findViewById(R.id.detail_high_textview);
        mDetailMin = (TextView)rootView.findViewById(R.id.detal_low_textview);
        mDetailDateDay = (TextView)rootView.findViewById(R.id.detail_date_day_textview);
        mDetailDateMonth = (TextView)rootView.findViewById(R.id.detail_date_month_textview);
        mDetailHumidity = (TextView)rootView.findViewById(R.id.detail_humidity_textView);
        mDetailWind = (TextView)rootView.findViewById(R.id.detail_wind_textView);
        mDetailPressure = (TextView)rootView.findViewById(R.id.detail_pressure_textView);
        mImage =(ImageView) rootView.findViewById(R.id.detail_icon);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(mUriData == null) {
            return null;
        }

        return new CursorLoader(getActivity(),
                mUriData,
                FORECAST_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(!data.moveToFirst())
        {
            return;
        }
        long date = data.getLong(COL_WEATHER_DATE);
        String dateDay = Utility.getDayName(getContext(),date);
        String dateMonth = Utility.getFormattedMonthDay(getContext(),date);
        String weatherDesc = data.getString(COL_WEATHER_DESC);
        boolean ismetric = Utility.isMetric(getActivity());
        String tempMax = Utility.formatTemperature(getContext(),data.getDouble(COL_WEATHER_MAX_TEMP),ismetric);
        String tempMin = Utility.formatTemperature(getContext(),data.getDouble(COL_WEATHER_MIN_TEMP),ismetric);
        String forecast = data.getString(COL_WEATHER_DESC);
        float humidity = data.getFloat(COL_WEATHER_HUMIDITY);
        float wind = data.getFloat(COL_WEATHER_WIND);
        float pressure = data.getFloat(COL_WEATHER_PRESSURE);
        float degrees = data.getFloat(COL_WEATHER_DEGREES);
        int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);
        mForecast = String.format("%s - %s - %s/%s",date,weatherDesc,tempMax,tempMin);


        mImage.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
        mDetailForecast.setText(forecast);
        mDetailMax.setText(tempMax);
        mDetailMin.setText(tempMin);
        mDetailDateDay.setText(dateDay);
        mDetailDateMonth.setText(dateMonth);
        mDetailHumidity.setText(getString(R.string.format_humidity,humidity));
        mDetailWind.setText(Utility.getFormattedWind(getContext(),wind,degrees));
        mDetailPressure.setText(getString(R.string.format_pressure,pressure));

        if(mShareActionProvider != null)
        {
            //we set the intent to the shareactionprovider
            mShareActionProvider.setShareIntent(createShareForcastItem());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


    }

}
