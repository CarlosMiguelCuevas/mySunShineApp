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

        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_forecast,parent,false);

        return new ViewHolder(card);
    }

    //it is used byt the layout manager to replace the data
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        mDataSet.moveToPosition(position);

        holder.mTextView.setText(convertCursorRowToUXFormat(mDataSet));
        holder.mImageView.setImageResource(getTheImage(mDataSet));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.v(LOG_TAG,"Im tapping");
//                mListener.ClickOnItemList(mDataSet.get(position));
//            }
//        });
    }

    //it is used by the layout manager
    @Override
    public int getItemCount() {
        return mDataSet.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;


        public ViewHolder(View parent) {
            super(parent);
            mTextView = (TextView)parent.findViewById(R.id.list_item_forecast_textview);
            mImageView = (ImageView)parent.findViewById(R.id.item_frescast_imageview);
        }
    }

    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
        int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
        int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
        int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
        int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);

        String highAndLow = formatHighLows(
                cursor.getDouble(idx_max_temp),
                cursor.getDouble(idx_min_temp));

        return Utility.formatDate(cursor.getLong(idx_date)) +
                " - " + cursor.getString(idx_short_desc) +
                " - " + highAndLow;
    }

    private int getTheImage(Cursor cursor) {
        int idx_description = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
        String description = cursor.getString(idx_description);
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

    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

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
