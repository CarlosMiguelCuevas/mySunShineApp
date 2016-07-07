package mx.com.cubozsoft.sunshineapp.listaPrincipal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
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

import mx.com.cubozsoft.sunshineapp.FetchWeatherTask;
import mx.com.cubozsoft.sunshineapp.ForecastAdapter;
import mx.com.cubozsoft.sunshineapp.ForecastItem;
import mx.com.cubozsoft.sunshineapp.R;
import mx.com.cubozsoft.sunshineapp.Utility;
import mx.com.cubozsoft.sunshineapp.WifiConectorReciever;
import mx.com.cubozsoft.sunshineapp.data.WeatherContract;


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
    Cursor mDataList;
    BroadcastReceiver mReceiver;

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
    public void onStop() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }

        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId =item.getItemId();

        if(itemId == R.id.action_refresh)
        {
            updateData();
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
        //registering the receiver

        IntentFilter filter =  new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mReceiver = new WifiConectorReciever();
        getActivity().registerReceiver(mReceiver,filter);

        super.onStart();
//        updateData();
    }

    private void updateData() {
        String place = Utility.getPreferredLocation(getContext());

        new FetchWeatherTask(getContext()).execute(place);
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

        String locationSetting = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

        mDataList = getActivity().getContentResolver().query(weatherForLocationUri,null,null,null,sortOrder);

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

}
