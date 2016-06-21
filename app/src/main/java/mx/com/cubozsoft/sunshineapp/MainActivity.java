package mx.com.cubozsoft.sunshineapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ListOfWeather.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void ClickOnItemList(ForecastItem data) {
        Toast.makeText(this, data.getForecast(),Toast.LENGTH_SHORT).show();
    }
}
