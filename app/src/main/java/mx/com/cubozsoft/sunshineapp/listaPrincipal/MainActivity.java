package mx.com.cubozsoft.sunshineapp.listaPrincipal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import mx.com.cubozsoft.sunshineapp.detalle.DetailActivity;
import mx.com.cubozsoft.sunshineapp.detalle.DetailFragment;
import mx.com.cubozsoft.sunshineapp.ForecastItem;
import mx.com.cubozsoft.sunshineapp.R;
import mx.com.cubozsoft.sunshineapp.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements ListOfWeather.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.generalmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int settingButtonId = R.id.action_settings;

        if(settingButtonId == item.getItemId())
        {
            Intent settingsInten = new Intent(this, SettingsActivity.class);
            startActivity(settingsInten);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ClickOnItemList(ForecastItem data) {
//        Toast.makeText(this, data.getForecast(),Toast.LENGTH_SHORT).show();
        Intent detailIntent = new Intent(this,DetailActivity.class);
        detailIntent.putExtra(DetailFragment.FORECAST_KEY,data);
        startActivity(detailIntent);
    }
}
