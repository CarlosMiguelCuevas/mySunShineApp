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

import java.util.ArrayList;
import java.util.List;

import mx.com.cubozsoft.sunshineapp.data.WeatherContract;
import mx.com.cubozsoft.sunshineapp.listaPrincipal.ListOfWeather;

/**
 * Created by carlos on 19/06/16.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder>{
    private final String LOG_TAG = ForecastAdapter.class.getSimpleName();
    private Cursor mDataSet = null;
    private ListOfWeather.OnFragmentInteractionListener mListener;
    private Context mContext;
    private int mRowIDColumn;
    private boolean mDataValid;

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    //region deal with differents layouts
    @Override
    public int getItemViewType(int position) {
        return (position == 0)? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    //endregion

    public ForecastAdapter(Cursor mDataSet, Context context) {
        this.mDataSet = mDataSet;
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

        View card;

        if(viewType == VIEW_TYPE_TODAY)
        {
             card = LayoutInflater.from(mContext).inflate(R.layout.today_list_item,parent,false);
        }
        else
        {
            card = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_forecast,parent,false);
        }


        return new ViewHolder(card);
    }

    //it is used byt the layout manager to replace the data
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(!mDataSet.moveToPosition(position))
        {
            return;
        }

        boolean metirc = Utility.isMetric(mContext);
        double min = mDataSet.getDouble(ListOfWeather.COL_WEATHER_MIN_TEMP);
        double max = mDataSet.getDouble(ListOfWeather.COL_WEATHER_MAX_TEMP);
        long dateMill = mDataSet.getLong(ListOfWeather.COL_WEATHER_DATE);

        holder.mTextViewDate.setText(Utility.getFriendlyDayString(mContext,dateMill));
        holder.mTextViewDescription.setText(mDataSet.getString(ListOfWeather.COL_WEATHER_DESC));
        holder.mTextViewMaxTemp.setText(Utility.formatTemperature(max,metirc));
        holder.mTextViewMinTemp.setText(Utility.formatTemperature(min,metirc));
//        holder.mImageView.setImageResource(getTheImage(mDataSet));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.ClickOnItemList(mDataSet,position);
            }
        });
    }

    //it is used by the layout manager
    @Override
    public int getItemCount() {

        if (mDataValid) {
            return mDataSet.getCount();
        } else {
            return 0;
        }

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
//            mImageView = (ImageView)parent.findViewById(R.id.list_item_icon);
        }
    }

//    private String convertCursorRowToUXFormat(Cursor cursor) {
//
//        String highAndLow = formatHighLows(
//                cursor.getDouble(ListOfWeather.COL_WEATHER_MAX_TEMP),
//                cursor.getDouble(ListOfWeather.COL_WEATHER_MIN_TEMP));
//
//        return Utility.formatDate(cursor.getLong(ListOfWeather.COL_WEATHER_DATE)) +
//                " - " + cursor.getString(ListOfWeather.COL_WEATHER_DESC) +
//                " - " + highAndLow;
//    }

//    private int getTheImage(Cursor cursor) {
//        int idx_description = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
//        String description = cursor.getString(idx_description);
//        int image = 0;
//
//        switch(description){
//            case "Clouds":
//                image = R.drawable.cludy;
//                break;
//            case "Rain":
//                image = R.drawable.rainy;
//                break;
//            case "Wind":
//                image = R.drawable.windy;
//                break;
//            default:
//                image = R.drawable.sunny;
//                break;
//        }
//
//        return image;
//    }

//    private String formatHighLows(double high, double low) {
//        boolean isMetric = Utility.isMetric(mContext);
//        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
//        return highLowStr;
//    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor == mDataSet) {
            return;
        }

        if (newCursor != null) {
            mDataSet = newCursor;
            mRowIDColumn = mDataSet.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
            mDataSet = null;
            mRowIDColumn = -1;
            mDataValid = false;
        }
    }


}
