package mx.com.cubozsoft.sunshineapp.detalle;

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

import mx.com.cubozsoft.sunshineapp.ForecastItem;
import mx.com.cubozsoft.sunshineapp.R;


public class DetailFragment extends Fragment {

    public static final String FORECAST_KEY = "forecastKey";

    private Uri mForecast;



    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(Uri data) {
        DetailFragment fragment = new DetailFragment();
        Uri args = data;
        fragment.setUri(data);
        return fragment;
    }

    public void setUri(Uri data)
    {
        mForecast = data;
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
        myShareIntent.putExtra(Intent.EXTRA_TEXT,mForecast.toString());

        //we set the intent to the shareactionprovider
        myShareActionProvider.setShareIntent(myShareIntent);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView image =(ImageView) rootView.findViewById(R.id.iamgeDeatil);
        TextView textForecast = (TextView) rootView.findViewById(R.id.forecastDetailText);

        image.setImageResource(R.drawable.cludy);
        textForecast.setText(mForecast.toString());

        return rootView;
    }

}
