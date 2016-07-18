package mx.com.cubozsoft.sunshineapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.com.cubozsoft.sunshineapp.data.WeatherContract;
import mx.com.cubozsoft.sunshineapp.listaPrincipal.ListOfWeather;

/**
 * Created by carlos on 19/06/16.
 */
public class ForecastAdapter extends AbstractRecyclerView<ForecastAdapter.ViewHolder>{

    private final String LOG_TAG = ForecastAdapter.class.getSimpleName();
    private ListOfWeather.OnFragmentInteractionListener mListener;
    private Context mContext;
    private int mRowIDColumn;
    private boolean mDataValid;
    private boolean mUseTodayLayout;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;


    public void setSpecialLayoutUsage(boolean specialLayoutUsage) {
        this.mUseTodayLayout = specialLayoutUsage;
    }

    //region deal with differents layouts
    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout)? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    //endregion

    public ForecastAdapter(Cursor mDataSet, Context context) {
        super(mDataSet);

        if(context instanceof ListOfWeather.OnFragmentInteractionListener)
        {
            this.mListener = (ListOfWeather.OnFragmentInteractionListener) context;
            this.mContext = context;
        }
        else
        {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener.");
        }
    }

    //it is used by the layout manager. simply create the object of the view holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutId = -1;

        if(viewType == VIEW_TYPE_TODAY)
        {
            layoutId = R.layout.today_list_item;
        }
        else
        {
            layoutId = R.layout.list_item_forecast;
        }

        View card = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);

        return new ViewHolder(card);
    }

    //it is used byt the layout manager to replace the data
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewMaxTemp;
        public TextView mTextViewMinTemp;
        public TextView mTextViewDescription;
        public TextView mTextViewDate;
        public ImageView mImageView;


        public ViewHolder(View parent) {
            super(parent);
            mTextViewDate = (TextView)parent.findViewById(R.id.list_item_date_textview);
            mTextViewDescription = (TextView)parent.findViewById(R.id.list_item_forecast_textview);
            mTextViewMaxTemp = (TextView)parent.findViewById(R.id.list_item_high_textview);
            mTextViewMinTemp = (TextView)parent.findViewById(R.id.list_item_low_textview);
            mImageView = (ImageView)parent.findViewById(R.id.list_item_icon);
        }
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final Cursor cursor, final int position) {
        boolean metirc = Utility.isMetric(mContext);
        double min = cursor.getDouble(ListOfWeather.COL_WEATHER_MIN_TEMP);
        double max = cursor.getDouble(ListOfWeather.COL_WEATHER_MAX_TEMP);
        long dateMill = cursor.getLong(ListOfWeather.COL_WEATHER_DATE);

        holder.mTextViewDate.setText(Utility.getFriendlyDayString(mContext,dateMill));
        holder.mTextViewDescription.setText(cursor.getString(ListOfWeather.COL_WEATHER_DESC));
        holder.mTextViewMaxTemp.setText(Utility.formatTemperature(mContext,max,metirc));
        holder.mTextViewMinTemp.setText(Utility.formatTemperature(mContext,min,metirc));

        int weatherId = cursor.getInt(ListOfWeather.COL_WEATHER_CONDITION_ID);

        int imageResorce = (getItemViewType(position) == VIEW_TYPE_TODAY)?
                Utility.getArtResourceForWeatherCondition(weatherId) :
                Utility.getIconResourceForWeatherCondition(weatherId) ;

        holder.mImageView.setImageResource(imageResorce);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.ClickOnItemList(cursor,position);
            }
        });
    }
}
