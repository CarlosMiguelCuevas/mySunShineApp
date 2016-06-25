package mx.com.cubozsoft.sunshineapp.detalle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import mx.com.cubozsoft.sunshineapp.R;
import mx.com.cubozsoft.sunshineapp.settings.SettingsActivity;

public class DetailActivity extends AppCompatActivity {

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
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState == null)
        {
            Bundle forecasBundle = getIntent().getExtras();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detailRoot,DetailFragment.newInstance(forecasBundle),"detailforecast")
                    .commit();
        }


    }
}
