package mx.com.cubozsoft.sunshineapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class DetailFragment extends Fragment {

    public static final String FORECAST_KEY = "forecastKey";

    private ForecastItem mForecast;



    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(Bundle itemBundle) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = itemBundle;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detal_menu,menu);

        //get a referenc to the item in the menu
        MenuItem shareItem = menu.findItem(R.id.action_share);
        //we get teh action provider usinfg the menu
        ShareActionProvider myShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        //we craete the intent with the info to share
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT,mForecast.getForecast());

        //we set the intent to the shareactionprovider
        myShareActionProvider.setShareIntent(myShareIntent);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForecast = (ForecastItem) getArguments().getParcelable(FORECAST_KEY);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView image =(ImageView) rootView.findViewById(R.id.iamgeDeatil);
        TextView textForecast = (TextView) rootView.findViewById(R.id.forecastDetailText);

        image.setImageResource(mForecast.getImage());
        textForecast.setText(mForecast.getForecast());

        return rootView;
    }

}
