package mx.com.cubozsoft.sunshineapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForecast = (ForecastItem) getArguments().getParcelable(FORECAST_KEY);
        }
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
