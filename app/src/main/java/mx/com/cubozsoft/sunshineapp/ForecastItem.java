package mx.com.cubozsoft.sunshineapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by carlos on 19/06/16.
 */
public class ForecastItem implements Parcelable{
    private String forecast;
    private int image;


    public ForecastItem(String forecast, int image) {
        this.setForecast(forecast);
        this.setImage(image);
    }

    protected ForecastItem(Parcel in) {
        forecast = in.readString();
        image = in.readInt();
    }

    public static final Creator<ForecastItem> CREATOR = new Creator<ForecastItem>() {
        @Override
        public ForecastItem createFromParcel(Parcel in) {
            return new ForecastItem(in);
        }

        @Override
        public ForecastItem[] newArray(int size) {
            return new ForecastItem[size];
        }
    };

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(forecast);
        dest.writeInt(image);
    }
}
