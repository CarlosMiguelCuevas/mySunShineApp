package mx.com.cubozsoft.sunshineapp.listaPrincipal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import mx.com.cubozsoft.sunshineapp.FetchWeatherTask;
import mx.com.cubozsoft.sunshineapp.ForecastAdapter;
import mx.com.cubozsoft.sunshineapp.ForecastItem;
import mx.com.cubozsoft.sunshineapp.R;
import mx.com.cubozsoft.sunshineapp.Utility;
import mx.com.cubozsoft.sunshineapp.WifiConectorReciever;
import mx.com.cubozsoft.sunshineapp.data.WeatherContract;


public class ListOfWeather extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>{

    Cursor mDataList;
    BroadcastReceiver mReceiver;
    RecyclerView mRecyclerView;
    ForecastAdapter mAdapter;
    RecyclerView.LayoutManager mManager;
    OnFragmentInteractionListener mListener;
    String mLocationSetting = "";
    Uri mWeatherForLocationUri = null;
    String mSortOrder;

    private static final int LOADER_ID = 1024;

    public ListOfWeather() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID,null,this);
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

        mLocationSetting = Utility.getPreferredLocation(getActivity());
        mWeatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(mLocationSetting, System.currentTimeMillis());
        mSortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

        mDataList = getActivity().getContentResolver().query(mWeatherForLocationUri,null,null,null,mSortOrder);
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

    //region Loader Implementation
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this.getActivity(),
                mWeatherForLocationUri,null,null,null,mSortOrder);

        return loader;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //If the loader is reset, we need to clear out the
        //current cursor from the adapter.
        mAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //When the loader has loaded some data (either initially, or the
        //datasource has changed and a new cursor is being provided),
        //Then we'll swap out the curser in our recyclerview's adapter
        // and we'll create the adapter if necessary
        if (mAdapter == null) {
            mAdapter = new ForecastAdapter(mDataList,getActivity());
        }

        mAdapter.swapCursor(data);
    }
    //endregion
}
