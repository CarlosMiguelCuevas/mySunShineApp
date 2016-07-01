package mx.com.cubozsoft.sunshineapp.listaPrincipal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        Log.v("state","OnCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.generalmenu,menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        Log.v("state","OnDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("state","OnStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("state","OnPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("state","OnResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("state","OnStart");
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
