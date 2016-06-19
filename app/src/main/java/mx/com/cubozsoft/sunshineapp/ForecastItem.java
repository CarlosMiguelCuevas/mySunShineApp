package mx.com.cubozsoft.sunshineapp;

/**
 * Created by carlos on 19/06/16.
 */
public class ForecastItem {
    private String forecast;
    private int image;


    public ForecastItem(String forecast, int image) {
        this.setForecast(forecast);
        this.setImage(image);
    }

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
}
