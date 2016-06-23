package mx.com.cubozsoft.sunshineapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfWeather.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfWeather#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfWeather extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String ARG_PARAM1 = "param1";
    static final String ARG_PARAM2 = "param2";
    List<ForecastItem> mDataList;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;

    // TODO: Rename and change types of parameters
    String mParam1;
    String mParam2;

    OnFragmentInteractionListener mListener;

    public ListOfWeather() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOfWeather.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfWeather newInstance(String param1, String param2) {
        ListOfWeather fragment = new ListOfWeather();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId =item.getItemId();

        if(itemId == R.id.action_refresh)
        {
            udpateData();
            return true;
        }
        else if (itemId == R.id.action_map)
        {
            String pc = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.location_key),"");
            Intent mapIntent = new Intent();
            mapIntent.setAction(Intent.ACTION_VIEW);
            mapIntent.setData(Uri.parse(getString(R.string.querymap,pc)));

            if(mapIntent.resolveActivity(getActivity().getPackageManager()) != null)
            {
                startActivity(mapIntent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        udpateData();
    }

    private void udpateData() {
        String place = PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getString(getString(R.string.location_key),getString(R.string.default_location));

        new FetchWeatherTask().execute(place);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_of_weather_fragment,menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_of_weather, container, false);


        mDataList = new ArrayList<>();

        mManager = new LinearLayoutManager(getContext());
        mAdapter = new ForecastAdapter(mDataList,getActivity());

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_forecast);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void ClickOnItemList(ForecastItem item);
    }

    class FetchWeatherTask extends AsyncTask<String, Void, ForecastItem[]>
    {
        final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";


        final String PLACE_KEY = "q";
        final String NUM_DAY_KEY = "cnt";
        final String UNITS_KEY = "units";
        final String MODE_KEY = "mode";
        final String API_KEY = "appid";


        @Override
        protected ForecastItem[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String place = params[0];
            int num_days = 7;
            String units = "metric";
            String mode = "json";
            final String API_PARAM = "a653a131acc24d3204bf63d343fb80d2";

            String uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PLACE_KEY,place)
                    .appendQueryParameter(NUM_DAY_KEY,Integer.toString(num_days))
                    .appendQueryParameter(UNITS_KEY,units)
                    .appendQueryParameter(MODE_KEY,mode)
                    .appendQueryParameter(API_KEY,API_PARAM)
                    .build().toString();

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(uri);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                return getWeatherDataFromJson(forecastJsonStr, num_days);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ForecastItem[] strings) {

            mDataList.clear();

            for(ForecastItem item : strings)
            {
                mDataList.add(item);
            }
            mAdapter.notifyDataSetChanged();

        }

        /* The date/time conversion code is going to be moved outside the asynctask later,
        * so for convenience we're breaking it out into its own method now.
        */
        private String getReadableDateString(long time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
        private String formatHighLows(double high, double low) {
            // For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            //if Imperial we convert
            String actualValue =PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.pref_units_key),"");
            String metric = getString(R.string.default_value_list_units);
            if( actualValue != metric){
                roundedHigh = convertTempUnits(roundedHigh);
                roundedLow = convertTempUnits(roundedLow);
            }


            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private int getIamge(String description)
        {
            int image = 0;

            switch(description){
                case "Clouds":
                    image = R.drawable.cludy;
                    break;
                case "Rain":
                    image = R.drawable.rainy;
                    break;
                case "Wind":
                    image = R.drawable.windy;
                    break;
                default:
                    image = R.drawable.sunny;
                    break;
            }

            return image;
        }

        private ForecastItem[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "main";
            final String OWM_MAX = "temp_max";
            final String OWM_MIN = "temp_min";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            ForecastItem[] resultforecasts = new ForecastItem[numDays];
            for(int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;
                String formattedForecast = "";
                int imageForecast;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);
                switch (i){
                    case 0:
                        day = getString(R.string.today);
                        break;
                    case 1:
                        day = getString(R.string.tomorrow);
                        break;
                    default:
                        day = getReadableDateString(dateTime);
                        break;
                }


                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                formattedForecast = day + " - " + description + " - " + highAndLow;
                imageForecast = getIamge(description);
                resultforecasts[i] = new ForecastItem(formattedForecast,imageForecast);
            }

            return resultforecasts;

        }

        public long convertTempUnits(long temp){
            long f;

            f = temp * (9/5) + 32;
            return f;
        }

    }
}
